package game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.Timer;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

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
    private static final float MOUSE_SENSITIVITY = 0.1f;
    private static final float MOVEMENT_SPEED = 0.05f;

    private AssimpModelLoader model_loader;
    private DisplaySettings win_opts;
    private Window window;
    private Scene scene;
    private Entity test0;
    private Entity test1;
    private Entity test2;
    private RawModel model;
    private Renderer renderer;

    private int targetFps;
    private int targetUps;

    private int width;
    private int height;
    private boolean running = false;

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
        targetUps = 30;
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

        // create the needed mesh and add it to the mix
        Mesh mesh = new Mesh();
        mesh.init(positions, colors, texture_coords, indices);
        RawModel model = new RawModel(mesh);
        // Create new entity, then add the model to the entity:
        test0 = new Entity("test0");
        test0.addModel(model);
        test0.setPosition(-2.0f, 0.0f, 3.0f);
        // test0.updateModelMatrix();
        // test0.setRotation(1.0f, 0.0f, 0.0f, -45.0f);
        
        test1 = new Entity("test1");
        test1.addModel(model);
        test1.setPosition(0.0f, 0f, 1.0f);
        // test1.updateModelMatrix();
        // test1.setRotation(1.0f, 0.0f, 0.3f, -65.0f);
        
        test2 = new Entity("test2");
        test2.addModel(model);
        test2.setPosition(2.0f, 0f, 2.0f);
        // test2.updateModelMatrix();
        // test2.setRotation(0.5f, 1.0f, 0.0f, 65.0f);

        // add entity to the scene
        scene.add_entity(test0);
        scene.add_entity(test1);
        scene.add_entity(test2);
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
        long fpsUpdateTime = 50; // Update FPS every 100 milliseconds

        glEnable(GL_DEPTH_TEST);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        while (!window.windowShouldClose() && running) {
            long now = System.currentTimeMillis();
            deltaUpdate += (now - initialTime) / timeU;
            deltaFps += (now - initialTime) / timeR;

            if (targetFps <= 0 || deltaFps >= 1) {
                if (deltaUpdate >= 1.0f) {
                    update();
                    deltaUpdate--;
                }
                input(window, scene, (now - initialTime));
                render();
                frameCount++;
                deltaFps--;
            }

            // Calculate and print FPS every 100 milliseconds
            if (now - lastFpsTime >= fpsUpdateTime) {
                double fps = (double) frameCount / ((now - lastFpsTime) / 1000.0);
                frameCount = 0;
                lastFpsTime = now;
            }

            initialTime = now;
        }
    }

    private void update() {
        window.update();
        test0.getRotation().rotateAxis((float) Math.toRadians(2.0f), 0.3f, 0.1f, 0.5f);
        test1.getRotation().rotateAxis((float) Math.toRadians(5.0f), 0.0f, 0.2f, 0.5f);
        test2.getRotation().rotateAxis((float) Math.toRadians(5.0f), 1.0f, 0.50f, 0.0f);

    }

    private void render() {
        renderer.render();
    }

    private void cleanup() {
        System.out.println("Banter Engine cleaning...");
        renderer.cleanup();
        window.cleanup();
        System.out.println("Banter Engine shutting down...");
    }

    public void input(Window window, Scene scene, long diffTimeMillis) {
        Camera camera = scene.get_camera();
        // System.out.println("camera: " + camera.getPosition());
        float move = MOVEMENT_SPEED;

        if (window.isKeyPressed(GLFW_KEY_W)) {
            System.out.println("moving forwards: " + move + " - " + diffTimeMillis);
            camera.moveForward(move);
        } 
        if (window.isKeyPressed(GLFW_KEY_S)) {
            System.out.println("moving backwards: " + move + " - " + diffTimeMillis);
            camera.moveBackwards(move);
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            System.out.println("moving left: " + move + " - " + diffTimeMillis);
            camera.moveLeft(move);
        }
        if (window.isKeyPressed(GLFW_KEY_D)) {
            System.out.println("moving right: " + move + " - " + diffTimeMillis);
            camera.moveRight(move);
        }

        // MouseInput mouseInput = window.getMouseInput();
        // if (mouseInput.isRightButtonPressed()) {
        // Vector2f displVec = mouseInput.getDisplVec();
        // camera.addRotation((float) Math.toRadians(-displVec.x * MOUSE_SENSITIVITY),
        // (float) Math.toRadians(-displVec.y * MOUSE_SENSITIVITY));
        // }
    }

    private void resize() {
        width = window.getWidth();
        height = window.getHeight();
    }

    public void wired() {
        System.out.println("Wireframe mode toggled!");
        renderer.wired();
    }

}
