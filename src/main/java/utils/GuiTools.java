package utils;

import static org.lwjgl.glfw.GLFW.*;

import java.util.HashMap;
import java.util.Map;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import imgui.ImGui;
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
    private Map<String, float[][]> entity_map = new HashMap<String, float[][]>();

    public void init(Window window, Scene scene) {
        this.scene = scene;
        this.window_class = window;
        this.window = window_class.getWindow();
        this.gui = new ImGuiImplGl3();
        ImGui.createContext();
        io = ImGui.getIO();
        main_gui_configs();
        imgui_key_maps();

        // Initialize gui:
        gui.init("#version 330");
    };

    public void render_gui() {
        ImGui.newFrame();
        ImGui.begin("Banter Engine Settings");
        main_menu_bar();
        entity_ui();
        show_demo();
        ImGui.end();
        ImGui.render();
        gui.renderDrawData(ImGui.getDrawData());
    }

    private void main_menu_bar() {
        if (ImGui.beginMainMenuBar()) {
            if (ImGui.beginMenu("Examples")) {
                ImGui.menuItem("null");
                ImGui.menuItem("Console");
                ImGui.menuItem("Log");
                ImGui.menuItem("Simple layout");
                ImGui.menuItem("Property editor");
                ImGui.menuItem("Long text display");
                ImGui.menuItem("Auto-resizing window");
                ImGui.menuItem("Constrained-resizing window");
                ImGui.menuItem("Simple overlay");
                ImGui.menuItem("Manipulating window titles");
                ImGui.menuItem("Custom rendering");
                ImGui.menuItem("Documents");
                ImGui.endMenu();
            }
            ImGui.endMainMenuBar();
        }
        ImGui.setNextWindowSize(482, window);
        ImGui.setNextWindowPos(0, 20);
    }

    private void entity_ui() {
        if (ImGui.collapsingHeader("Entities: " + scene.get_entities().size())) {
            for (Entity entity : scene.get_entities()) {

                Entity_Obj sudo_entity = new Entity_Obj(entity);
                Vector3f entity_position = entity.getPosition();
                Quaternionf entity_rotation = entity.getRotation();
                float entity_scale = entity.getScale();

                // If key already exists skip it:
                if (!entity_map.containsKey(entity.getId())) {
                    // Special Object created for the data that I need;
                    entity_map.put(entity.getId(), new float[][] { sudo_entity.get_position(),
                            sudo_entity.get_rotation(), sudo_entity.getScale() });
                }

                // Each entity's data:
                if (ImGui.treeNode(entity.getId())) {
                    // Entity Position:
                    if (ImGui.treeNode("Position")) {
                        ImGui.labelText("X:", String.valueOf(entity_position.x));
                        ImGui.labelText("Y:", String.valueOf(entity_position.y));
                        ImGui.labelText("Z:", String.valueOf(entity_position.z));

                        if (ImGui.dragFloat3("Position Change", entity_map.get(entity.getId())[0],
                                0.001f,
                                -20.00f,
                                20.00f,
                                "%0.02f")) {
                                    System.out.println(entity_map.get(entity.getId())[2][0]);
                            entity.setPosition(
                                    entity_map.get(entity.getId())[0][0],
                                    entity_map.get(entity.getId())[0][1],
                                    entity_map.get(entity.getId())[0][2]);
                            entity.updateModelMatrix();
                        }

                        if (ImGui.dragFloat3("Rotation Change", entity_map.get(entity.getId())[0],
                                0.001f,
                                -20.00f,
                                20.00f,
                                "%0.02f")) {
                                    System.out.println(entity_map.get(entity.getId())[2][0]);
                            entity.setPosition(
                                    entity_map.get(entity.getId())[0][0],
                                    entity_map.get(entity.getId())[0][1],
                                    entity_map.get(entity.getId())[0][2]);
                            entity.updateModelMatrix();
                        }
                        ImGui.treePop();
                    }

                    // Entity Rotation:
                    if (ImGui.treeNode("Rotation")) {
                        ImGui.labelText("X:", String.valueOf(entity.getRotation().x));
                        ImGui.labelText("Y:", String.valueOf(entity.getRotation().y));
                        ImGui.labelText("Z:", String.valueOf(entity.getRotation().z));
                        ImGui.treePop();
                    }

                    // Entity Scale:
                    if (ImGui.treeNode("Scale")) {
                        ImGui.labelText("size:", String.valueOf(entity.getScale()));
                        ImGui.treePop();
                    }

                    ImGui.treePop();
                }
            }
        }
    }

    private void show_demo() {
        ImGui.showDemoWindow();
    }

    private void main_gui_configs() {
        // ImGuiIO io = ImGui.getIO();
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
        ImGui.destroyContext();
    }
}