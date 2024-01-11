package rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL14.glBlendEquation;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import io.WindowManager;
import models.mesh.Mesh;
import models.texture.Texture;
import utils.Projection;
import utils.UniformsMap;

@SuppressWarnings("unused")
public class Scene {
    private UniformsMap uniformsMap;
    private static Shader shader;
    private static Mesh mesh;
    private static Texture texture;
    int width, height;

    // for testing purposes only:
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_FAR = 1000.0f;
    private static final float Z_NEAR = 0.01f;

    private static Matrix4f projection;
    private static Matrix4f orthoMatrix;
    private static Vector3f position;
    private static float rotation = -45.0f;
    private Matrix4f model_matrix;
    private Matrix4f view_matrix;
    private Matrix4f projection_matrix;
    WindowManager window;
    private static float scale;
    // ------

    private boolean wired = false;
    private int shader_id;
    private int texture_uni_0;

    public Scene(WindowManager window) {
        this.window = window;
        this.width = window.getWidth();
        this.height = window.getHeight();
        this.wired = false;
        shader = new Shader();
        mesh = new Mesh();

        // for testing purposes only:
        // model_matrix = new Matrix4f();
        // position = new Vector3f();
        // rotation = new Quaternionf();
        scale = 1;
        // ------
    }

    public void init() {
        // Vertices for testing:
        float[] positions = new float[] {
                0.5f, 0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                -0.5f, 0.5f, 0.0f,
        };

        float[] colors = new float[] {
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };

        int[] indices = new int[] {
                0, 1, 3, // first triangle
                1, 2, 3 // second triangle
        };

        float[] texture_coords = new float[] {
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 1.0f,
        };

        // setting up shader:
        shader.init();
        shader_id = shader.getShaderProgramId();
        uniformsMap = new UniformsMap(shader_id);

        orthoMatrix = new Matrix4f().ortho(0.0f, 800.0f, 0.0f, 600.0f, 0.1f, 100.0f);

        // create transformation:
        model_matrix = new Matrix4f().identity();
        view_matrix = new Matrix4f().identity();
        projection_matrix = new Matrix4f().identity();

        model_matrix.rotate((float) Math.toRadians(rotation), new Vector3f(1.0f, 0.0f, 0.0f));
        view_matrix.translate(new Vector3f(0.0f, 0.0f, -3.0f));
        projection_matrix.perspective((float) Math.toRadians(45.0f), window.getWidth() / window.getHeight(), 0.1f, 100.0f);

        // setting up mesh info:
        mesh.init(positions, colors, texture_coords, indices);
        // setup texture:
        texture = new Texture("/Users/jareemhoff/dev/java/banter/res/textures/checkermap.png", shader_id);
        // Activate the shader to set the uniform
        shader.use();

        // uniform in texture0:
        uniformsMap.createUniform("texture_sampler_0");
        // uniform in projection_matrix:
        uniformsMap.createUniform("view_matrix");
        uniformsMap.createUniform("model_matrix");
        uniformsMap.createUniform("projection_matrix");
    };
    
    public void render() {
        glClearColor(0.2f, 0.25f, 0f, 0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        System.out.println("width = " + window.getWidth() + " height = " + window.getHeight());
        // Use this to render in wireframe mode:
        if (wired) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        } else {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }
        shader.use();
        // Set the texture uniform to use texture unit 0
        // glUniform1i(texture_uni_0, 0);
        uniformsMap.setUniform("texture_sampler_0", 0);

        // Create transformations
        uniformsMap.setUniform("model_matrix", model_matrix);
        uniformsMap.setUniform("view_matrix", view_matrix);
        uniformsMap.setUniform("projection_matrix", projection_matrix);
        model_matrix.rotate((float) Math.toRadians(0.05), new Vector3f(0.0f, 0.0f, 1.0f));
        
        
        texture.bind(0);

        // Use the shader program
        shader.use();

        glBindVertexArray(mesh.getVaoId());
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        // completely optional to unbind the vao:
        texture.unbind();
        glBindVertexArray(0);
        glDisableVertexAttribArray(0);
    }

    public void createUniforms() {
    }

    public void cleanup() {
        shader.clean();
    }

    private void log_uniforms() {
        System.out.println("|--------------------------------------------------|");
        System.out.println("| Uniforms" + "                                         |");
        System.out.println("|--------------------------------------------------|");
        uniformsMap.getAllUniforms().forEach((k, v) -> {
            System.out.println("| Uniform:\t" + k.substring(0, 4) + "\t\t\t\t   |");
            System.out.println("| Location:\t" + v + "\t\t\t\t   |");
            System.out.println("|--------------------------------------------------|");
        });
    }

    public void wired() {
        wired = !wired;
    }

    public void project_init(int width, int height) {
        projection = new Matrix4f();
        updateProjMatrix(width, height);
        System.out.println("proj matrix: " + projection);
    }

    public Matrix4f getProjMatrix() {
        return projection;
    }

    public void updateProjMatrix(int width, int height) {
        projection.setPerspective(FOV, (float) window.getWidth() / window.getHeight(), Z_NEAR, Z_FAR);
    }
}
