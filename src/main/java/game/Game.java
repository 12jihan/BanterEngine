package game;

import static org.lwjgl.opengl.GL11.*;

import io.DisplaySettings;
import io.WindowManager;
import models.entity.Model;
import models.mesh.Mesh;
import models.texture.Texture;
import rendering.Renderer;
import rendering.Scene;
import rendering.Shader;

// @SuppressWarnings("unused")
public class Game {
    private DisplaySettings win_opts;
    private final WindowManager window;
    private Shader shader;
    private Model model;
    private Mesh mesh;
    private Scene scene;
    private Texture texture;
    private boolean running;
    int width;
    int height;

    Game() throws Exception {
        win_opts = new DisplaySettings();
        window = new WindowManager("Banter Engine", win_opts,
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
        // Reimplement this later:
        // renderer = new Renderer();
        // shader = new Shader();
        // mesh = new Mesh();
        scene = new Scene(window);
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
        glClearColor(0.3f, 0.0f, 0.3f, 0.0f);
        while (!window.windowShouldClose()) {
            update(); // Update game logic
            glViewport(0, 0, window.getWidth(), window.getHeight());
            render(); // Render graphics
            
        }
    }

    private void render() {
        scene.render();
    }

    private void update() {
        window.update();
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
