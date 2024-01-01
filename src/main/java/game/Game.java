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
import loader.RawModel;
import rendering.Renderer;
import shader_handling.ShaderProgram;
import utils.Utils;

public class Game {
    private DisplaySettings win_opts;
    private final WindowManager window;
    private final Loader loader;
    private final Renderer renderer;
    private ShaderProgram shader;
    private RawModel model;
    private boolean running;
    // -- Testing stuff:
    private int shaderProgramId;
    private int vertId;
    private int fragId;
    private int vao, vbo, ebo;
    // --

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
        System.out.println("Banter Engine cleaning loader...");
        cleanup();
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

        // Testing some shit to see if I can get this thing working properly:
        // generate shaders to be used:
        String vertSrc;
        String fragSrc;
        // Read src files:
        vertSrc = Utils.readFile("/Users/jareemhoff/dev/java/banter/src/resources/vertex.glsl");
        fragSrc = Utils.readFile("/Users/jareemhoff/dev/java/banter/src/resources/fragment.glsl");

        // vert shader:
        vertId = glCreateShader(GL_VERTEX_SHADER);
        System.out.println("vert id:" + vertId);
        glShaderSource(vertId, vertSrc);
        glCompileShader(vertId);
        if (glGetShaderi(vertId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println(glGetShaderInfoLog(vertId, 500));
            System.err.println("Could not compile vert shader!");
            System.exit(-1);
        }

        // frag shader:
        fragId = glCreateShader(GL_FRAGMENT_SHADER);
        System.out.println("frag id:" + fragId);
        glShaderSource(fragId, fragSrc);
        glCompileShader(fragId);
        if (glGetShaderi(fragId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println(glGetShaderInfoLog(fragId, 500));
            System.err.println("Could not compile frag shader!");
            System.exit(-1);
        }

        shaderProgramId = glCreateProgram();
        System.out.println("shader program id:" + shaderProgramId);
        if (shaderProgramId == 0) {
            System.err.println("don't work!");
        }
        // --
        // Deal with VAO, VBOS, and EBOs:
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            vbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            // FloatBuffer vbobuf = storeDataInFloatBuffer(vertices);
            FloatBuffer vbobuf = stack.callocFloat(vertices.length);
            vbobuf.put(vertices).flip();
            glBufferData(GL_ARRAY_BUFFER, vbobuf, GL_STATIC_DRAW);
            
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(0);
            
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            

            ebo = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
            IntBuffer intbuf = stack.callocInt(indices.length);
            intbuf.put(indices).flip();
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, intbuf, GL_STATIC_DRAW);

            
        }


        // Attach and link the shaders to the program:
        glAttachShader(shaderProgramId, vertId);
        glAttachShader(shaderProgramId, fragId);

        // Link the program:
        glLinkProgram(shaderProgramId);
        if (glGetProgrami(shaderProgramId, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("Could not link shader program!");
            System.exit(-1);
        }
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer dataBuffer = stack.callocInt(data.length);
            // puts the dat into the beginning of the Int buffer:
            dataBuffer.put(0, data);
            // Flip buffer to prepare it to be read from (common practice with buffers):
            dataBuffer.flip();

            // return the buffer for use:
            return dataBuffer;
        }
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer dataBuffer = stack.callocFloat(data.length);
            // puts the dat into the beginning of the float buffer:
            dataBuffer.put(0, data);
            // Flip buffer to prepare it to be read from (common practice with buffers):
            dataBuffer.flip();

            // return the buffer for use:
            return dataBuffer;
        }
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
        // shader.start();
        // renderer.render(model);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0f, 0.0f, 0.5f, 0f);
        glUseProgram(shaderProgramId);
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        // glDisableVertexAttribArray(0);
        // glBindVertexArray(0);
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
