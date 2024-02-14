package game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.Timer;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import input.KeyboardInput;
import input.MouseInput;
import io.DisplaySettings;
import io.Window;
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
import utils.TimeManager;

@SuppressWarnings("unused")
public class Game {
    public static final int TARGET_FPS = 60;
    public static final int TARGET_UPS = 60;
    private static final float MOUSE_SENSITIVITY = 0.10f;
    private static final float MOVEMENT_SPEED = 0.05f;

    // Window things:
    private Window window;
    private DisplaySettings win_opts;

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
        targetFps = 60;
        targetUps = 60;
        scene = new Scene(window);
        renderer = new Renderer(scene);

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
        // Coordinates for stuff:
        float[] positions = new float[] {
                // V0
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,

                // For text coords in top face
                // V8: V4 repeated
                -0.5f, 0.5f, -0.5f,
                // V9: V5 repeated
                0.5f, 0.5f, -0.5f,
                // V10: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V11: V3 repeated
                0.5f, 0.5f, 0.5f,

                // For text coords in right face
                // V12: V3 repeated
                0.5f, 0.5f, 0.5f,
                // V13: V2 repeated
                0.5f, -0.5f, 0.5f,

                // For text coords in left face
                // V14: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V15: V1 repeated
                -0.5f, -0.5f, 0.5f,

                // For text coords in bottom face
                // V16: V6 repeated
                -0.5f, -0.5f, -0.5f,
                // V17: V7 repeated
                0.5f, -0.5f, -0.5f,
                // V18: V1 repeated
                -0.5f, -0.5f, 0.5f,
                // V19: V2 repeated
                0.5f, -0.5f, 0.5f,
        };

        float[] colors = new float[] {
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };

        int[] indices = new int[] {
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                8, 10, 11, 9, 8, 11,
                // Right face
                12, 13, 7, 5, 12, 7,
                // Left face
                6, 14, 4, 6, 15, 14,
                // Bottom face
                19, 16, 17, 19, 18, 16,
                // Back face
                7, 4, 5, 7, 6, 4
        };

        float[] texture_coords = new float[] {
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,

                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,

                // For text coords in top face
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,

                // For text coords in right face
                0.0f, 0.0f,
                0.0f, 0.5f,

                // For text coords in left face
                0.5f, 0.0f,
                0.5f, 0.5f,

                // For text coords in bottom face
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,
        };

        window.init();
        keyboard = new KeyboardInput(window);
        mouse = new MouseInput(window);

        // create the needed mesh and add it to the mix
        Mesh mesh = new Mesh();
        mesh.init(positions, colors, texture_coords, indices);
        RawModel model = new RawModel(mesh);

        // Create new entity, then add the model to the entity:
        test0 = new Entity("test0");
        test0.addModel(model);
        position_entity(test0, -2.0f, 0.0f, 0.0f);

        test1 = new Entity("test1");
        test1.addModel(model);
        position_entity(test1, 0.0f, 0.0f, 0.0f);

        test2 = new Entity("test2");
        test2.addModel(model);
        position_entity(test2, 2.0f, 0.0f, 0.0f);
        // add entity to the scene
        scene.add_entity(test0);
        scene.add_entity(test1);
        scene.add_entity(test2);
        // Set camera position:
        scene.get_camera().setPosition(0.0f, 0.0f, 5.0f);

        // initialize the renderer:
        renderer.init();
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
        // System.out.println("camera: " + camera.getPosition());
        float speed = MOVEMENT_SPEED;

        // Keyboard input:
        keyboard_input(_window, camera, speed, diffTimeMillis);
        // Mouse input:
        mouse_input(_window, camera, speed, diffTimeMillis);
    }

    // Updating any data that needs it:
    private void update() {
        window.update();
        rotation += 1.5f;
        if (rotation >= 360) {
            rotation = 0;
        }
        rotate_entity(test0, 1, 0, 0, rotation, false);
        rotate_entity(test1, 0, 1, 0, rotation, false);
        rotate_entity(test2, 0, 0, 1, rotation, false);
    }

    private void render() {
        renderer.render();
    }

    private void cleanup() {
        System.out.println("Banter Engine cleaning...");
        renderer.cleanup();
        window.cleanup();
        mouse.cleanup();
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
    }

    public void mouse_input(long window, Camera camera, float speed, long diffTimeMillis) {
        // update scroll values back to 0:
        double deltaX = mouse.getX() - mouse.getLastX();
        double deltaY = mouse.getY() - mouse.getLastY();
        // double deltaY = mouse.getLastY() - mouse.getY();
        float rotationX = (float) Math.toRadians(deltaY * MOUSE_SENSITIVITY); // Pitch
        float rotationY = (float) Math.toRadians(deltaX * MOUSE_SENSITIVITY); // Yaw

        if (mouse.isLeftButtonPressed()) {
            camera.addRotation(rotationX, rotationY);
        }

        if (mouse.isRightButtonPressed()) {
            System.out.println("Mouse right button pressed!");

        }

        mouse.update();
    }

    /**
     * These are the miscellaneous methods for the game:
     */

    // Screen resize:
    private void resize() {
        width = window.getWidth();
        height = window.getHeight();
    }

    // Wireframe mode: TODO - Fix this method.
    public void wired() {
        System.out.println("Wireframe mode toggled!");
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
