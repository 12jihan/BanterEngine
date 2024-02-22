package game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import imgui.*;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiKey;
import imgui.gl3.ImGuiImplGl3;
import imgui.type.ImBoolean;
import imgui.type.ImInt;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import input.KeyboardInput;
import input.MouseInput;
import io.DisplaySettings;
import io.Window;
import models.CubeModel;
import models.LightCubeModel;
import models.entity.AssimpModelLoader;
import models.entity.Entity;
import models.entity.RawModel;
import models.mesh.Mesh;
import models.texture.Texture;
import rendering.Camera;
import rendering.Renderer;
import rendering.Scene;
import rendering.Shader;
import utils.GameClock;
import utils.GuiTools;
import utils.TimeManager;

@SuppressWarnings("unused")
public class Game {
    public static final int TARGET_FPS = 60;
    public static final int TARGET_UPS = 60;

    private static float[] MOUSE_SENSITIVITY = { 0.20f };
    private static float[] MOVEMENT_SPEED = { 0.05f };
    private boolean[] enable_wireframe = new boolean[] { false };

    // Window things:
    private Window window;
    private DisplaySettings win_opts;

    // GUI things:
    // private ImGuiImplGl3 gui;
    private GuiTools gui_tools;

    // Inputs:
    private MouseInput mouse;
    private KeyboardInput keyboard;

    // Models:
    private AssimpModelLoader model_loader;
    private RawModel model;

    // Added entities:
    private Entity test0;
    private Entity test1;
    private Entity test2;
    private Entity light_cube;

    // Scene/Renderer:
    private Scene scene;
    private Renderer renderer;

    // Other important settings:
    private int targetFps;
    private int targetUps;
    private int width;
    private int height;
    private boolean running = false;
    float rotation = 0.0f;
    ImInt entity_pos = new ImInt(0);

    Game() throws Exception {
        win_opts = new DisplaySettings();
        window = new Window("Banter Engine", win_opts,
                // Auto resizes:
                () -> {
                    resize();
                    return null;
                },
                // Wireframe mode:
                () -> {
                    wired();
                    return null;
                });
        // gui = new ImGuiImplGl3();
        targetFps = 60;
        targetUps = 60;
        scene = new Scene(window);
        renderer = new Renderer(scene);
        gui_tools = new GuiTools();

        // model_loader = new AssimpModelLoader();
        // model_loader.load_model("test-1",
        // "/Users/jareemhoff/dev/java/banter/res/models/sample_model.dae");
        running = true;
    }

    public void run() throws Exception {
        System.out.println("Banter Engine starting...");
        init();
        loop();
        System.out.println("Banter Engine cleaning loader...");
        cleanup();
    }

    private void init() throws Exception {

        // Window Stuff:
        window.init();

        // Create models and scenes:
        create_models_and_scenes();

        // initialize imgui:
        // init_imgui();
        gui_tools.init(this.window, this.scene);

        // Input controls:
        create_input_controls();

        // initialize the renderer:
        renderer.init();
        // Initialize the gui:

    }

    private void loop() {
        long initialTime = System.currentTimeMillis();
        float timeU = 1000.0f / targetUps;
        float timeR = targetFps > 0 ? 1000.0f / targetFps : 0;
        float deltaUpdate = 0;
        float deltaFps = 0;
        int frameCount = 0;
        long lastFpsTime = initialTime;
        long updateTime = initialTime;
        long fpsUpdateTime = 50; // Update FPS every 100 milliseconds

        glEnable(GL_DEPTH_TEST);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        while (!window.windowShouldClose() && running) {
            window.poll_events();
            long now = System.currentTimeMillis();
            deltaUpdate += (now - initialTime) / timeU;
            deltaFps += (now - initialTime) / timeR;

            // Inputs are updated:
            if (targetFps <= 0 || deltaFps >= 1) {
                input(window, scene, now - initialTime);
            }

            // Updates are updated:
            if (deltaUpdate >= 1) {
                long diffTimeMillis = now - updateTime;
                update();
                updateTime = now;
                deltaUpdate--;
            }

            // Renders are updated:
            if (targetFps <= 0 || deltaFps >= 1) {
                render();
                deltaFps--;

            }
            initialTime = now;
        }
        cleanup();
    }

    // Handling all inputs:
    private void input(Window window, Scene scene, long diffTimeMillis) {
        long _window = window.getWindow();
        Camera camera = scene.get_camera();
        float speed = MOVEMENT_SPEED[0];

        // Keyboard input:
        keyboard_input(_window, camera, speed, diffTimeMillis);
        // Mouse input:
        mouse_input(_window, camera, speed, diffTimeMillis);
    }

    // Updating any data that needs it:
    private void update() {

        // Removing sample rotation bit for now:
        // rotation += 1.5f;
        // if (rotation >= 360) {
        //     rotation = 0;
        // }
        // rotate_entity(test0, 1, 0, 0, rotation, false);
        // rotate_entity(test1, 0, 1, 0, rotation, false);
        // rotate_entity(test2, 0, 0, 1, rotation, false);
    }

    private void render() {
        if (enable_wireframe[0])
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        // glColor3f(1.0f, 1.0f, 1.0f);
        renderer.render();
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        gui_tools.render_gui();
        window.swap_buffers();
    }

    private void cleanup() {
        System.out.println("Banter Engine cleaning...");
        renderer.cleanup();
        gui_tools.cleanup();
        // ImGui.destroyContext();
        mouse.cleanup();
        window.cleanup();
        System.out.println("Banter Engine shutting down...");
    }

    public void keyboard_input(long window, Camera camera, float speed, long diffTimeMillis) {
        float _speed = speed;

        if (keyboard.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            _speed = speed * 2.5f;
        }
        if (keyboard.isKeyPressed(GLFW_KEY_W)) {
            camera.moveForward(_speed);
        }
        if (keyboard.isKeyPressed(GLFW_KEY_S)) {

            camera.moveBackwards(_speed);
        }
        if (keyboard.isKeyPressed(GLFW_KEY_A)) {

            camera.moveLeft(_speed);
        }
        if (keyboard.isKeyPressed(GLFW_KEY_D)) {

            camera.moveRight(_speed);
        }
        if (keyboard.isKeyPressed(GLFW_KEY_D)) {
            camera.moveRight(_speed);
        }
        if (keyboard.isKeyPressed(GLFW_KEY_SPACE)) {
            camera.moveUp(_speed);
        }
        if (keyboard.isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
            camera.moveDown(_speed);
        }
        if (keyboard.isKeyPressed(GLFW_KEY_COMMA)) {
            wired();
        }
    }

    public void mouse_input(long window, Camera camera, float speed, long diffTimeMillis) {
        // update scroll values back to 0:
        double deltaX = mouse.getX() - mouse.getLastX();
        double deltaY = mouse.getY() - mouse.getLastY();
        // double deltaY = mouse.getLastY() - mouse.getY();
        float rotationX = (float) Math.toRadians(deltaY * MOUSE_SENSITIVITY[0]); // Pitch
        float rotationY = (float) Math.toRadians(deltaX * MOUSE_SENSITIVITY[0]); // Yaw

        if (mouse.isLeftButtonPressed()) {
            camera.addRotation(rotationX, rotationY);
        }

        if (mouse.isRightButtonPressed()) {
            System.out.println("Mouse right button pressed!");
        }
        mouse.update();
        deltaX = 0;
        deltaY = 0;
    }

    /**
     * These are the miscellaneous methods for the game:
     */

    // Create input controls for keyboard and mouse:
    private void create_input_controls() {
        keyboard = new KeyboardInput(window);
        mouse = new MouseInput(window);
    }

    // Create Models and scenes:
    private void create_models_and_scenes() {
        // Create a model:
        RawModel model = new CubeModel().create_model();
        RawModel light_cube_model = new LightCubeModel().create_model();

        // Create new entity, then add the model to the entity ... after add entity to
        // scene:
        light_cube = new Entity("light_cube");
        light_cube.addModel(light_cube_model);
        position_entity(light_cube, 0.0f, 3.0f, 0.0f);
        scene.add_entity(light_cube);

        test0 = new Entity("test0");
        test0.addModel(model);
        position_entity(test0, -2.0f, 0.0f, 0.0f);
        scene.add_entity(test0);

        test1 = new Entity("test1");
        test1.addModel(model);
        position_entity(test1, 0.0f, 0.0f, 0.0f);
        scene.add_entity(test1);

        test2 = new Entity("test2");
        test2.addModel(model);
        position_entity(test2, 2.0f, 0.0f, 0.0f);
        scene.add_entity(test2);

        // Set camera position:
        scene.get_camera().setPosition(0.0f, 0.0f, 5.0f);
    }

    // Screen resize:
    private void resize() {
        width = window.getWidth();
        height = window.getHeight();
    }

    // Wireframe mode: TODO - Fix this method.
    public void wired() {
        renderer.wired();
    }

    // Scale Entity:
    public void scale_entity(Entity entity, float scale) {
        entity.setScale(scale);
        entity.updateModelMatrix();
    }

    // Entity Position:
    public void position_entity(Entity entity, float x, float y, float z) {
        entity.setPosition(x, y, z);
        entity.updateModelMatrix();
    }

    // Entity Rotation:
    public void rotate_entity(Entity entity, int x, int y, int z, float degrees, boolean debug) {
        entity.setRotation(x, y, z, (float) Math.toRadians(degrees));
        entity.updateModelMatrix();
        if (debug) {
            System.out.println("DEBUGGING ENTITY ROTATION: " + entity.getId() + "\n" + entity.getModelMatrix());
        }
    }
}
