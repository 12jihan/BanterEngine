package io;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class MouseInput {
    private final long window;

    private double xPos, yPos, lastX, lastY, scrollX, scrollY;
    private boolean leftButtonPressed, rightButtonPressed;

    private final GLFWMouseButtonCallback mouseButtonCallback;
    private final GLFWCursorPosCallback cursorPosCallback;
    private final GLFWScrollCallback scrollCallback;

    public MouseInput(long window) {
        this.window = window;

        // Mouse button callback:
        mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                leftButtonPressed = button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE;
                rightButtonPressed = button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_RELEASE;
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

        // Set the callbacks:
        glfwSetMouseButtonCallback(this.window, mouseButtonCallback);
        glfwSetCursorPosCallback(this.window, cursorPosCallback);
        glfwSetScrollCallback(this.window, scrollCallback);
    }

    public void update() {
        scrollX = 0;
        scrollY = 0;
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
    }

}
