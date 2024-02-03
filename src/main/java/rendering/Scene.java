package rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import io.Window;
import models.entity.Entity;
import models.entity.RawModel;
import models.mesh.Mesh;
import models.texture.Texture;
import utils.Projection;
import utils.UniformsMap;

@SuppressWarnings("unused")
public class Scene {
    private UniformsMap uniformsMap;
    private static Shader shader;
    private static Mesh mesh1;
    private static Mesh mesh2;
    private static Texture texture;
    int width, height;
    int[] indices;

    // for testing purposes only:
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_FAR = 1000.0f;
    private static final float Z_NEAR = 0.01f;

    private static Camera camera;
    private static Projection projection_class;
    private static Matrix4f orthoMatrix;
    private static Vector3f position;
    private static float rotation = -45.0f;

    private Matrix4f model_matrix;
    private Matrix4f view_matrix;
    private Matrix4f projection_matrix;

    // Model map:
    List<Entity> entities = new ArrayList<>();

    Window window;
    private static float scale;
    // ------

    private boolean wired;
    private int shader_id;
    private int texture_uni_0;

    public Scene(Window window) {
        this.window = window;
        this.width = window.getWidth();
        this.height = window.getHeight();
        this.wired = false; 
        
        camera = new Camera();
        projection_class = new Projection(width, height);
        
        shader = new Shader();
        scale = 1;
    }

    public void init() {
        // Vertices for testing:
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

        
        // setting up shader:
        shader.init();
        shader_id = shader.getShaderProgramId();
        uniformsMap = new UniformsMap(shader_id);

        // create transformation:
        model_matrix = new Matrix4f().identity();
        model_matrix2 = new Matrix4f().identity();
        // view_matrix = new Matrix4f().identity();
        // Camera:
        view_matrix = camera.getViewMatrix();
        projection_matrix = new Matrix4f().identity();

        // mesh 1
        model_matrix.rotate((float) Math.toRadians(rotation), new Vector3f(0.0f, 1.0f, 0.0f));
        model_matrix.translate(new Vector3f(1.5f, 0.0f, 0.0f));
        models.add(model_matrix);
        // mesh 2
        model_matrix2.rotate((float) Math.toRadians(rotation), new Vector3f(0.0f, 0.0f, 1.0f));
        model_matrix2.translate(new Vector3f(-1.5f, 0.0f, 0.0f));
        models.add(model_matrix2);

        view_matrix.translate(new Vector3f(0.0f, 0.0f, -5.0f));
        view_matrix.rotate((float) Math.toRadians(0.0f), new Vector3f(0.0f, 1.0f, 0.0f));

        // setting up mesh info:
        mesh1.init(positions, colors, texture_coords, indices);
        meshes.add(mesh1);
        mesh2.init(positions, colors, texture_coords, indices);
        meshes.add(mesh2);
        System.out.println("Meshes: " + meshes.size());
        // mesh.init(positions, colors, indices);
        // setup texture:
        texture = new Texture("/Users/jareemhoff/dev/java/banter/res/textures/brickwall.png", shader_id);
        // Activate the shader to set the uniform
        // shader.use();

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
        // Use this to render in wireframe mode:
        if (wired) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        } else {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        };

        // Shader Uniforms:
        shader.use();
        uniformsMap.setUniform("projection_matrix", projection_class.getProjMatrix());
        uniformsMap.setUniform("view_matrix", view_matrix);
        uniformsMap.setUniform("texture_sampler_0", 0);
        texture.bind(0);
        // for(int i = 0; i < meshes.size(); i++) {
        //     projection_class.updateProjMatrix(window.getWidth(), window.getHeight());
        //     uniformsMap.setUniform("model_matrix" , models.get(i));
        //     models.get(i).rotate((float) Math.toRadians(1), new Vector3f(0.0f, 0.0f, 1.0f));
        //     glBindVertexArray(meshes.get(i).getVaoId());
        //     glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        // }
        

        // bind textures slot:

        // Use the shader program
        // shader.use();

        // completely optional to unbind the vao:
        texture.unbind();
        glBindVertexArray(0);
        glDisableVertexAttribArray(0);
    }

    public void cleanup() {
        shader.clean();
        mesh1.clean();
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
        System.out.println("key");
    }
}
