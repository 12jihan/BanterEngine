package input;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.*;

import imgui.*;
import io.Window;

@SuppressWarnings("unused")
public class KeyboardInput {
    private final boolean[] keys = new boolean[GLFW_KEY_LAST];
    private final long window;

    private GLFWKeyCallback keyCallback;

    public KeyboardInput(Window window) {
        this.window = window.getWindow();
        // keyboard callback method:
        keyCallback = new GLFWKeyCallback() {
            ImGuiIO io = ImGui.getIO();

            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                System.out.println("testing:\t" + io.getMetricsActiveWindows());
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, true);
                }

                if (action == GLFW_PRESS) {
                    io.setKeysDown(key, true);
                } else if (action == GLFW_RELEASE) {
                    io.setKeysDown(key, false);
                }
            }
        };

        // GLFW callbacks:
        glfwSetKeyCallback(this.window, keyCallback);
    }

    public boolean isKeyPressed(int keycode) {
        return glfwGetKey(window, keycode) == GLFW_PRESS;
    }
}
