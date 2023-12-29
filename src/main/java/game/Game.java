package game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.*;

import display.DisplaySettings;
import display.WindowManager;
import loader.Loader;
import loader.RawModel;
import rendering.Renderer;
import shader.ShaderProgram;
import shader.StaticShader;

public class Game {
    private DisplaySettings win_opts;
    private final WindowManager window;
    private final Loader loader;
    private final Renderer renderer;
    private ShaderProgram shader;
    private RawModel model;
    private boolean running;

    Game() throws Exception {
        win_opts = new DisplaySettings();
        window = new WindowManager("test", win_opts, () -> {
            resize();
            return null;
        });
        loader = new Loader();
        renderer = new Renderer();
        shader = new ShaderProgram();
        running = true;
    }

    public void run() throws Exception {
        System.out.println("Banter Engine starting...");
        init();
        loop();
        cleanup();
        System.out.println("Banter Engine cleaning loader...");
    }

    private void init() throws Exception {
        window.init();
        // Vertices for testing:
        float[] vertices = {
                -0.5f, 0.5f, 0f, // V0
                -0.5f, -0.5f, 0f, // V1
                0.5f, -0.5f, 0f, // V2
                0.5f, 0.5f, 0f, // V3
        };

        int[] indices = {
                0, 1, 3, // Top left triangle (V0, V1, V3)
                3, 1, 2 // Bottom right triangle (V3, V1, V2)
        };

        shader.init("/Users/jareemhoff/dev/java/banter/src/resources/vertex.glsl", "/Users/jareemhoff/dev/java/banter/src/resources/fragment.glsl");
        model = loader.loadToVAO(vertices, indices);
    }

    private void loop() {
        glClearColor(0.3f, 0.0f, 0.3f, 0.0f);

        while (!window.windowShouldClose()) {
            update(); // Update game logic
            // Prepare the render:
            renderer.prepare();
            // Start the shader:
            render(); // Render graphics
        }
    }
    
    private void render() {
        // System.out.println("Rendering...");
        shader.start();
        renderer.render(model);
        shader.stop();
    }

    private void update() {
        window.update();
    }

    private void cleanup() {
        System.out.println("Banter Engine cleaning...");
        // renderer.cleanup();
        shader.cleanup();
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
