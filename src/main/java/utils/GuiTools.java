package utils;

import static org.lwjgl.glfw.GLFW.*;
import static imgui.ImGui.*;

import java.util.HashMap;
import java.util.Map;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiKey;
import imgui.gl3.ImGuiImplGl3;
import io.Window;
import models.entity.Entity;
import models.entity.Entity_Obj;
import rendering.Scene;

public class GuiTools {
    private ImGuiImplGl3 gui;
    private ImGuiIO io;
    private Scene scene;
    private Window window_class;
    private long window;

    private static float[] MOUSE_SENSITIVITY = { 0.20f };
    private static float[] MOVEMENT_SPEED = { 0.05f };
    private boolean[] enable_wireframe = new boolean[] { false };
    private boolean[] enable_rotation = new boolean[] { false, false, false };

    private Map<String, float[]> entity_position = new HashMap<String, float[]>();
    private Map<String, float[][]> entity_rotation = new HashMap<String, float[][]>();
    private Map<String, float[]> entity_scale = new HashMap<String, float[]>();

    public void init(Window window, Scene scene) {
        this.scene = scene;
        this.window_class = window;
        this.window = window_class.getWindow();
        this.gui = new ImGuiImplGl3();
        createContext();
        io = getIO();
        main_gui_configs();
        imgui_key_maps();

        // Initialize gui:
        gui.init("#version 330");
    };

    public void render_gui() {
        newFrame();
        begin("Banter Engine Settings");
        main_menu_bar();
        entity_ui();
        // show_demo();
        end();
        render();
        gui.renderDrawData(getDrawData());
    }

    private void main_menu_bar() {
        if (beginMainMenuBar()) {
            if (beginMenu("Examples")) {
                menuItem("null");
                menuItem("Console");
                menuItem("Log");
                menuItem("Simple layout");
                menuItem("Property editor");
                menuItem("Long text display");
                menuItem("Auto-resizing window");
                menuItem("Constrained-resizing window");
                menuItem("Simple overlay");
                menuItem("Manipulating window titles");
                menuItem("Custom rendering");
                menuItem("Documents");
                endMenu();
            }
            endMainMenuBar();
        }
        setNextWindowSize(482, window);
        setNextWindowPos(0, 0);
    }

    private void entity_ui() {
        if (collapsingHeader("Entities: " + scene.get_entities().size())) {
            for (Entity entity : scene.get_entities()) {

                // Data formation: TODO figure out how to make all this more efficient:
                Entity_Obj entity_transform_data = new Entity_Obj(entity);

                // If key already exists skip it for position rotation and scale:
                if (!entity_position.containsKey(entity.getId())) {
                    entity_position.put(entity.getId(), entity_transform_data.get_position());
                }
                if (!entity_rotation.containsKey(entity.getId())) {
                    entity_rotation.put(entity.getId(), entity_transform_data.get_rotation());
                }
                if (!entity_scale.containsKey(entity.getId())) {
                    entity_scale.put(entity.getId(), entity_transform_data.get_scale());
                }

                // Each entity's data:
                if (treeNode(entity.getId())) {
                    // Entity Position:
                    if (treeNode("Position")) {
                        labelText("X:", String.valueOf(entity.getPosition().x));
                        labelText("Y:", String.valueOf(entity.getPosition().y));
                        labelText("Z:", String.valueOf(entity.getPosition().z));

                        if (dragFloat3("Position Change", entity_position.get(entity.getId()),
                                0.001f,
                                -20.00f,
                                20.00f,
                                "%0.02f")) {

                            entity.setPosition(
                                    entity_position.get(entity.getId())[0],
                                    entity_position.get(entity.getId())[1],
                                    entity_position.get(entity.getId())[2]);
                            entity.updateModelMatrix();
                        }

                        treePop();
                    }

                    // Entity Rotation:
                    if (treeNode("Rotation")) {
                        labelText("X", String.valueOf(entity.getRotation().x));
                        labelText("Y", String.valueOf(entity.getRotation().y));
                        labelText("Z", String.valueOf(entity.getRotation().z));

                        // labelText("", "Edit Rotation:");
                        text("Edit Rotation:");
                        if (checkbox("X", enable_rotation[0])) {
                            enable_rotation[0] = !enable_rotation[0];
                        }

                        if (checkbox("Y", enable_rotation[1])) {
                            enable_rotation[1] = !enable_rotation[1];
                        }

                        if (checkbox("Z", enable_rotation[2])) {
                            enable_rotation[2] = !enable_rotation[2];
                        }

                        if (enable_rotation[0] || enable_rotation[1] || enable_rotation[2]) {
                            if (sliderAngle("Rotation Change", entity_rotation.get(entity.getId())[0])) {
                                entity.setRotation(
                                        enable_rotation[0] ? 1.0f : 0.0f,
                                        enable_rotation[1] ? 1.0f : 0.0f,
                                        enable_rotation[2] ? 1.0f : 0.0f,
                                        entity_rotation.get(entity.getId())[0][0]);
                                entity.updateModelMatrix();
                            }
                        }
                        treePop();
                    }

                    // Entity Scale:
                    if (treeNode("Scale")) {
                        text("Scale");
                        if (dragFloat("Scaler", entity_scale.get(entity.getId()), 0.001f, 0.01f, 10.00f, "%0.02f")) {
                            entity.setScale(entity_scale.get(entity.getId())[0]);
                            entity.updateModelMatrix();
                        }
                        treePop();
                    }

                    treePop();
                }
            }
        }
    }

    private void show_demo() {
        showDemoWindow();
    }

    private void main_gui_configs() {
        // ImGuiIO io = getIO();
        // Configuration for ImGui:
        int[] window_width = new int[1], window_height = new int[1];
        glfwGetWindowSize(window_class.getWindow(), window_width, window_height);

        int[] framebufferWidth = new int[1], framebufferHeight = new int[1];
        glfwGetFramebufferSize(window_class.getWindow(), framebufferWidth, framebufferHeight);

        float scaleX = (float) framebufferWidth[0] / (float) window_width[0];
        float scaleY = (float) framebufferHeight[0] / (float) window_height[0];

        io.setDisplaySize(window_width[0], window_height[0]);
        io.setDisplayFramebufferScale(scaleX, scaleY);

        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
        io.setConfigFlags(ImGuiConfigFlags.NoMouseCursorChange);
    }

    public ImGuiImplGl3 getGui() {
        return gui;
    }

    private void imgui_key_maps() {
        // Map ImGui keys
        io.setKeyMap(ImGuiKey.Tab, GLFW_KEY_TAB);
        io.setKeyMap(ImGuiKey.LeftArrow, GLFW_KEY_LEFT);
        io.setKeyMap(ImGuiKey.RightArrow, GLFW_KEY_RIGHT);
        io.setKeyMap(ImGuiKey.UpArrow, GLFW_KEY_UP);
        io.setKeyMap(ImGuiKey.DownArrow, GLFW_KEY_DOWN);
        io.setKeyMap(ImGuiKey.PageUp, GLFW_KEY_PAGE_UP);
        io.setKeyMap(ImGuiKey.PageDown, GLFW_KEY_PAGE_DOWN);
        io.setKeyMap(ImGuiKey.Home, GLFW_KEY_HOME);
        io.setKeyMap(ImGuiKey.End, GLFW_KEY_END);
        io.setKeyMap(ImGuiKey.Insert, GLFW_KEY_INSERT);
        io.setKeyMap(ImGuiKey.Delete, GLFW_KEY_DELETE);
        io.setKeyMap(ImGuiKey.Backspace, GLFW_KEY_BACKSPACE);
        io.setKeyMap(ImGuiKey.Space, GLFW_KEY_SPACE);
        io.setKeyMap(ImGuiKey.Enter, GLFW_KEY_ENTER);
        io.setKeyMap(ImGuiKey.Escape, GLFW_KEY_ESCAPE);
        io.setKeyMap(ImGuiKey.KeyPadEnter, GLFW_KEY_KP_ENTER);
        io.setKeyMap(ImGuiKey.A, GLFW_KEY_A);
        io.setKeyMap(ImGuiKey.C, GLFW_KEY_C);
        io.setKeyMap(ImGuiKey.V, GLFW_KEY_V);
        io.setKeyMap(ImGuiKey.X, GLFW_KEY_X);
        io.setKeyMap(ImGuiKey.Y, GLFW_KEY_Y);
        io.setKeyMap(ImGuiKey.Z, GLFW_KEY_Z);
    }

    public void cleanup() {
        gui.dispose();
        destroyContext();
    }
}