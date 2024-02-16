package input;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;
import org.lwjgl.glfw.*;

import imgui.*;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiKey;
import imgui.gl3.ImGuiImplGl3;

import io.Window;

public class MouseInput {
    private Window window;
    private long window_context;

    private double xPos, yPos, lastX, lastY, scrollX, scrollY;
    private Vector2f displVec = new Vector2f();
    private boolean entered;

    private final GLFWMouseButtonCallback mouseButtonCallback;
    private final GLFWCursorPosCallback cursorPosCallback;
    private final GLFWScrollCallback scrollCallback;
    private final GLFWCursorEnterCallback cursorEnterCallback;
    private boolean leftButtonPressed;
    private boolean rightButtonPressed;
    private boolean  midButtonPressed;
    // private final ImGuiIO io;

    public MouseInput(Window window) {
        this.window = window;
        this.window_context = this.window.getWindow();
        this.entered = false;

        // Mouse button callback:
        mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                ImGuiIO io = ImGui.getIO();
                boolean pressed = action == GLFW_PRESS;
                if (button == GLFW_MOUSE_BUTTON_LEFT)
                    io.setMouseDown(0, pressed);
                if (button == GLFW_MOUSE_BUTTON_RIGHT)
                    io.setMouseDown(1, pressed);
                if (button == GLFW_MOUSE_BUTTON_MIDDLE)
                    io.setMouseDown(2, pressed);

                // Process application mouse events:
                if (!io.getWantCaptureMouse()) {
                    leftButtonPressed = button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS;
                    rightButtonPressed = button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS;
                    midButtonPressed = button == GLFW_MOUSE_BUTTON_MIDDLE && action == GLFW_PRESS;
                }

            }
        };

        // Cursor position callback:
        cursorPosCallback = new GLFWCursorPosCallback() {
            ImGuiIO io = ImGui.getIO();

            @Override
            public void invoke(long window, double xpos, double ypos) {
                lastX = xPos;
                lastY = yPos;
                xPos = xpos;
                yPos = ypos;

                io.setMousePos((float) xpos, (float) yPos);
            }
        };

        // Scroll callback:
        scrollCallback = new GLFWScrollCallback() {
            ImGuiIO io = ImGui.getIO();

            @Override
            public void invoke(long window, double x_offset, double y_offset) {
                scrollX += x_offset;
                scrollY += y_offset;
                io.setMouseWheel(io.getMouseWheelH() + (float) x_offset);
                io.setMouseWheel(io.getMouseWheel() + (float) y_offset);
            }
        };

        // Enter callback:
        cursorEnterCallback = new GLFWCursorEnterCallback() {
            @Override
            public void invoke(long window, boolean entered) {
                if (entered) {
                    System.out.println("aaaah yeahh!");
                } else {
                    leftButtonPressed = false;
                    rightButtonPressed = false;
                    midButtonPressed = false;
                }
            }
        };

        // Set the callbacks:
        glfwSetCursorEnterCallback(this.window_context, cursorEnterCallback);
        glfwSetMouseButtonCallback(this.window_context, mouseButtonCallback);
        glfwSetCursorPosCallback(this.window_context, cursorPosCallback);
        glfwSetScrollCallback(this.window_context, scrollCallback);
    }

    public void update() {
        scrollX = 0;
        scrollY = 0;

        displVec.x = 0;
        displVec.y = 0;

        if (lastX > 0 && lastY > 0) {
            lastX = 0;
            lastY = 0;
        }
    }

    public double getX() {
        return xPos;
    }

    public double getY() {
        return yPos;
    }

    public double getLastX() {
        return lastX;
    }

    public double getLastY() {
        return lastY;
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }

    public double getScrollX() {
        return scrollX;
    }

    public double getScrollY() {
        return scrollY;
    }

    public boolean isEntered() {
        return entered;
    }

    public void cleanup() {
        mouseButtonCallback.free();
        cursorPosCallback.free();
        scrollCallback.free();
        cursorEnterCallback.free();
    }

}
