package game;

import static org.lwjgl.opengl.GL11.*;

import java.util.Timer;

import org.lwjgl.glfw.GLFW;

import io.DisplaySettings;
import io.Window;
import models.entity.AssimpModelLoader;
import models.entity.Model;
import models.mesh.Mesh;
import models.texture.Texture;
import rendering.Renderer;
import rendering.Scene;
import rendering.Shader;
import utils.GameClock;
import utils.TimeManager;

@SuppressWarnings("unused")
public class Game {
    public static final int TARGET_FPS = 60;
    public static final int TARGET_UPS = 30;

    private AssimpModelLoader model_loader;
    private DisplaySettings win_opts;
    private Window window;
    private Scene scene;
    private GameClock clock;

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
        clock = new GameClock(TARGET_FPS, TARGET_UPS);

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
        window.init();
        scene.init();

    }

    private void loop() {
        long initialTime = System.currentTimeMillis();
        float timeU = 1000.0f / targetUps;
        float timeR = targetFps > 0 ? 1000.0f / targetFps : 0;
        float deltaUpdate = 0;
        float deltaFps = 0;
        int frameCount = 0;
        long lastFpsTime = initialTime;
        long fpsUpdateTime = 100; // Update FPS every 100 milliseconds

        glEnable(GL_DEPTH_TEST);
        glClearColor(0.3f, 0.0f, 0.3f, 0.0f);
        while (!window.windowShouldClose() && running) {
            long now = System.currentTimeMillis();
            deltaUpdate += (now - initialTime) / timeU;
            deltaFps += (now - initialTime) / timeR;

            if (targetFps <= 0 || deltaFps >= 1) {
                input();
                if (deltaUpdate >= 1.0f) {
                    update();
                    deltaUpdate--;
                }
                render();
                frameCount++;
                deltaFps--;
            }

            // Calculate and print FPS every 100 milliseconds
            if (now - lastFpsTime >= fpsUpdateTime) {
                double fps = (double) frameCount / ((now - lastFpsTime) / 1000.0);
                System.out.println("FPS: " + fps);
                frameCount = 0;
                lastFpsTime = now;
            }

            initialTime = now;
        }
    }

    private void input() {

    }

    private void update() {
        window.update();
    }

    private void render() {
        scene.render();
    }

    private void cleanup() {
        System.out.println("Banter Engine cleaning...");
        // renderer.cleanup();
        // shader.clean();
        scene.cleanup();
        window.cleanup();
        System.out.println("Banter Engine shutting down...");
    }

    private void resize() {
        width = window.getWidth();
        height = window.getHeight();
        // System.out.println("Resizing to:\n\t- " + "x: " + width + ", y: " + height);
    }

    public void wired() {
        System.out.println("Wireframe mode toggled!");
        scene.wired();
    }

}
