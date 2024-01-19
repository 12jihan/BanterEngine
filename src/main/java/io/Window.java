package io;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;
import java.util.concurrent.Callable;
import org.pmw.tinylog.Logger;

// import input.InputHandler;

public class Window {
    // check if system type is mac osx/m1 for compatibility:
    private long window;
    private int height, width;
    private String title;
    private DisplaySettings win_opts;
    // Have not implemented the mouse input just yet:
    // private InputHandler mouseInput;
    private Callable<Void> resizeFunc;
    private Callable<Void> keyInputFunc;

    public Window(String title, DisplaySettings win_opts, Callable<Void> resizeFunc,
            Callable<Void> keyInputFunc) {
        this.title = title;
        this.height = win_opts.height;
        this.width = win_opts.width;
        this.resizeFunc = resizeFunc;
        this.keyInputFunc = keyInputFunc;
        this.win_opts = win_opts;
    }

    public void init() {
        // Set the error callback:
        GLFWErrorCallback.createPrint(System.err).set();
        // Check if GLFW is initialized:
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        // Check if antiAliasing is enabled:
        if (win_opts.antiAliasing) {
            glfwWindowHint(GLFW_SAMPLES, 4);
        }
        // Check if the version of OpenGL is 3.2 or greater:
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

        // Used for mac compatibility layer - check if mac or something else:
        // if (win_opts.compatibleProfile) <-- used to be this line:
        if (win_opts.getSyscheck()) {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);
        } else {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        }

        // Maximize Window Conditional Statement:
        if (win_opts.width > 0 && win_opts.height > 0) {
            this.width = win_opts.width;
            this.height = win_opts.height;
        } else {
            glfwWindowHint(GLFW_MAXIMIZED, GL_TRUE);
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            width = vidMode.width();
            height = vidMode.height();
        }
        // Create the window:
        window = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        // Check if window is created:
        if (window == NULL)
            throw new RuntimeException("Failed to create GLFW window!");
        // Set the resize callback:
        glfwSetFramebufferSizeCallback(window, (window, w, h) -> {
            resized(w, h);
            glViewport(0, 0, width, height);
        });
        // Set the error callback:
        glfwSetErrorCallback((int errorCode, long msgPtr) -> Logger.error("Error code [{}], msg [{}]", errorCode,
                MemoryUtil.memUTF8(msgPtr)));
        // Set the key callback:
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            keyCallBack(key, action);
        });
        // Set the window to be the current context:
        glfwMakeContextCurrent(window);
        // turn on vsync if fps is greater than 0:
        if (win_opts.fps > 0) {
            glfwSwapInterval(0);
        } else {
            glfwSwapInterval(1);
        }
        glfwShowWindow(window);
        // glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        createCapabilities();
        // For resizing:
        IntBuffer bufferWidth = BufferUtils.createIntBuffer(1);
        IntBuffer bufferHeight = BufferUtils.createIntBuffer(1);
        glfwGetFramebufferSize(window, bufferWidth, bufferHeight);
        width = bufferWidth.get(0);
        height = bufferHeight.get(0);
    }

    // updates logic for window manager:
    public void update() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public void cleanup() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        GLFWErrorCallback callback = glfwSetErrorCallback(null);
        if (callback != null) {
            callback.free();
        }
        ;
    }

    /**
     * Getter and Setters:
     **/

    // Getter and Setter for created window::
    public long getWindow() {
        return window;
    }

    public void setWindow(long window) {
        this.window = window;
        Logger.info("Window set: " + window);
    }

    // Getter and Setter for window height:
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        Logger.info("Window height set to: " + height);
    }

    // Getter and Setter for window width:
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        Logger.info("Window width set to: " + width);

    }

    // Getter and Setter for the window title:
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Other specific settings for window:
     **/

    public boolean isKeyPressed(int keycode) {
        return glfwGetKey(window, keycode) == GLFW_PRESS;
    }

    public void pollEvents() {
        glfwPollEvents();
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(window);
    }

    protected void resized(int width, int height) {
        this.width = width;
        this.height = height;
        try {
            // Attempt to call the resize callback:
            resizeFunc.call();
        } catch (Exception excp) {
            Logger.error("Error calling resize callback:\n\t- ", excp);
        }
    }

    public void keyCallBack(int key, int action) {
        // System.out.println("Key:\n" + key + "\nAction:\n" + action);
        // We will detect this in the rendering loop:
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
            glfwSetWindowShouldClose(window, true);
        if (key == GLFW_KEY_HOME && action == GLFW_RELEASE) {
            try {
                // Attempt to call the key callback:
                keyInputFunc.call();
                System.out.println(key);
            } catch (Exception e) {
                Logger.error("Error with keycall back:\n\t -", e);
            }
        };
    }
}
