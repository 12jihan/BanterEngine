package game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

import display.DisplaySettings;
import display.WindowManager;
import loader.Loader;
import loader.Mesh;
import loader.RawModel;
import rendering.Renderer;
import shader_handling.ShaderProgram;
import shader_handling.Shadering;
import utils.Utils;

public class Game {
    private DisplaySettings win_opts;
    private final WindowManager window;
    // private final Loader loader;
    private final Renderer renderer;
    private ShaderProgram shader;
    private RawModel model;
    // -- new test class:
    private Shadering shadering;
    private Mesh mesh;

    private boolean running;
    // -- Testing stuff:
    private int shaderProgramId;
    private int vertId;
    private int fragId;
    private int vao, vbo, ebo;
    // --

    Game() throws Exception {
        win_opts = new DisplaySettings();
        window = new WindowManager("test", win_opts, 
        () -> {
            resize();
            return null;
        },
        () -> {
            wired();
            return null;
        });
        renderer = new Renderer();
        shader = new ShaderProgram();
        shadering = new Shadering();
        mesh = new Mesh();
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
        window.init();
        shader.init();
        mesh.init(vertices, indices);

        
    }

    private void loop() {
        glClearColor(0.3f, 0.0f, 0.3f, 0.0f);
        while (!window.windowShouldClose()) {
            update(); // Update game logic
            render(); // Render graphics
        }
    }

    private void render() {
        // renderer.prepare();
        // shader.useProgram();
        // renderer.render(model);
        // glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        shader.render(mesh.getVaoList().get(0));
    }

    private void update() {
        window.update();
    }

    private void cleanup() {
        System.out.println("Banter Engine cleaning...");
        // renderer.cleanup();
        shader.clean();
        window.cleanup();
        System.out.println("Banter Engine shutting down...");
    }

    private void resize() {
        int width = window.getWidth();
        int height = window.getHeight();
        System.out.println("Resizing to:\n\t- " + "x: " + width + ", y: " + height);
    }

    public void wired() {
        shader.wired();
    }

}
