package game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.*;

import display.DisplaySettings;
import display.WindowManager;
import loader.Loader;
import loader.RawModel;
import rendering.Renderer;

public class GameLoop {
    private DisplaySettings win_opts;
    private final WindowManager window;

    GameLoop() {
        win_opts = new DisplaySettings();
        System.out.println("win_opts: " + win_opts);
        window = new WindowManager("test", win_opts, () -> {
            resize();
            return null;
        });
    }

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

    public void run() {
        System.out.println("Banter Engine starting...");
        init();
        loop();
        cleanup();
    }

    private void init() {
        window.init();
    }

    private void loop() {
        createCapabilities();
        glClearColor(0.3f, 0.0f, 0.3f, 0.0f);

        while (!window.windowShouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            update(); // Update game logic
            render(); // Render graphics
        }
    }

    private void render() {
        System.out.println("Rendering...");
    }

    private void update() {
        window.update();
    }

    private void cleanup() {
        System.out.println("Banter Engine shutting down...");
        window.cleanup();
    }

    private void resize() {
        int width = window.getWidth();
        int height = window.getHeight();
        System.out.println("Resizing to:\n\t- " + "x: " + width + ", y: " + height);
    }

}
