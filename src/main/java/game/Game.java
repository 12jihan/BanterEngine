package game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.*;

import display.DisplaySettings;
import display.WindowManager;
import loader.Loader;
import loader.RawModel;
import rendering.Renderer;

public class Game {
    private DisplaySettings win_opts;
    private final WindowManager window;
    private final Loader loader;
    private final Renderer renderer;
    private RawModel model;
    private boolean running;

    Game() {
        win_opts = new DisplaySettings();
        window = new WindowManager("test", win_opts, () -> {
            resize();
            return null;
        });
        loader = new Loader();
        renderer = new Renderer();
        running = true;
    }

    public void run() {
        System.out.println("Banter Engine starting...");
        init();
        loop();
        cleanup();
        System.out.println("Banter Engine cleaning loader...");
    }

    private void init() {
        // Vertices for testing:
        float[] vertices = {
                // left bottom of triangle
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                // right top of triangle
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
                -0.5f, 0.5f, 0f
        };

        window.init();
        model = loader.loadToVAO(vertices);
    }

    private void loop() {
        glClearColor(0.3f, 0.0f, 0.3f, 0.0f);

        while (!window.windowShouldClose()) {
            // Prepare the render:
            renderer.prepare();

            update(); // Update game logic
            render(); // Render graphics
        }
    }

    private void render() {
        // System.out.println("Rendering...");
        renderer.render(model);
    }

    private void update() {
        window.update();
    }

    private void cleanup() {
        System.out.println("Banter Engine cleaning...");
        // renderer.cleanup();
        loader.cleanup();
        window.cleanup();
        System.out.println("Banter Engine shutting down...");
    }

    private void resize() {
        int width = window.getWidth();
        int height = window.getHeight();
        System.out.println("Resizing to:\n\t- " + "x: " + width + ", y: " + height);
    }

}
