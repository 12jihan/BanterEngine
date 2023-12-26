package display;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryUtil;

import java.util.concurrent.Callable;
import org.pmw.tinylog.Logger;

import input.InputHandler;

public class WindowManager {
    // check if system type is mac osx/m1 for compatibility:
    private static final boolean SYSCHECK = System.getProperty("os.name").contains("Mac");
    private long window;
    private int height, width;
    private String title;
    // Have not implemented the mouse input just yet:
    // private InputHandler mouseInput;
    private Callable<Void> resizeFunc;

    public WindowManager(String title, WindowOptions opts, Callable<Void> resizeFunc) {
        this.title = title;
        this.height = opts.height;
        this.width = opts.width;
        this.resizeFunc = resizeFunc;

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        if (opts.antiAliasing) {
            glfwWindowHint(GLFW_SAMPLES, 4);
        }
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);

        // Used for mac compatibility layer - check if mac or something else:
        if (getSyscheck()) {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);
        } else {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        }

        // Maximize Window Conditional Statement:
        if (opts.width > 0 && opts.height > 0) {
            this.width = opts.width;
            this.height = opts.height;
        } else {
            glfwWindowHint(GLFW_MAXIMIZED, GL_TRUE);
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            width = vidMode.width();
            height = vidMode.height();
        }

        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create GLFW window!");
        glfwSetFramebufferSizeCallback(window, (window, w, h) -> resized(w, h));

        glfwSetErrorCallback((int errorCode, long msgPtr) -> Logger.error("Error code [{}], msg [{}]", errorCode,
                MemoryUtil.memUTF8(msgPtr)));

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            keyCallBack(key, action);
        });

        glfwMakeContextCurrent(window);
        if (opts.fps > 0) {
            glfwSwapInterval(0);
        } else {
            glfwSwapInterval(1);
        }

        glfwShowWindow(window);

        // For the resizing function:
        int[] arrWidth = new int[1];
        int[] arrHeight = new int[1];
        glfwGetFramebufferSize(window, arrWidth, arrHeight);
        width = arrWidth[0];
        height = arrHeight[0];

        // init();
    }

    public void init() {

    }

    // updates logic for window manager:
    public void update() {

    }

    // renders window manager assets:
    public void render() {

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
        System.out.println("Key:\n" + key + "\nAction:\n" + action);
        // We will detect this in the rendering loop:
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
            glfwSetWindowShouldClose(window, true);
    }

    public static boolean getSyscheck() {
        return SYSCHECK;
    }

    public static class WindowOptions {
        public int width, height;
        public int fps;
        public int target_ups;
        public boolean compatibleProfile;
        public boolean antiAliasing;
        public boolean vsync;

    }
}
