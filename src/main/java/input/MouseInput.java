package input;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwSetCursorEnterCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import io.Window;

public class MouseInput {
    private Window window;
    private long window_context;

    private double xPos, yPos, lastX, lastY, scrollX, scrollY;
    private boolean leftButtonPressed, rightButtonPressed;
    private Vector2f displVec = new Vector2f();
    private boolean entered;


    private final GLFWMouseButtonCallback mouseButtonCallback;
    private final GLFWCursorPosCallback cursorPosCallback;
    private final GLFWScrollCallback scrollCallback;
    private final GLFWCursorEnterCallback cursorEnterCallback;

    public MouseInput(Window window) {
        this.window = window;
        this.window_context = this.window.getWindow();
        this.entered = false;

        // Mouse button callback:
        mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                leftButtonPressed = button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS;
                rightButtonPressed = button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS;
            }
        };

        // Cursor position callback:
        cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                lastX = xPos;
                lastY = yPos;
                xPos = xpos;
                yPos = ypos;
            }
        };

        // Scroll callback:
        scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double x_offset, double y_offset) {
                scrollX += x_offset;
                scrollY += y_offset;
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

    public void cleanup() {
        mouseButtonCallback.free();
        cursorPosCallback.free();
        scrollCallback.free();
        cursorEnterCallback.free();
    }

}
