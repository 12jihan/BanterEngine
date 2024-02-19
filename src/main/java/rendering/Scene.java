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
import rendering.lights.SceneLights;
import utils.Projection;
import utils.UniformsMap;

@SuppressWarnings("unused")
public class Scene {
    private static Shader main_shader;
    private static Texture texture;
    private static SceneLights lights;
    private static Camera camera;
    private static Projection projection;
    
    private UniformsMap uniformsMap;
    
    private int width, height;
    private int[] indices;
    
    private Matrix4f model_matrix;
    private Matrix4f view_matrix;
    private Matrix4f projection_matrix;

    // for testing purposes only:
    private static final float FOV = (float) Math.toRadians(180.0f);
    private static final float Z_FAR = 1000.0f;
    private static final float Z_NEAR = 0.01f;



    // Model map:
    private List<Entity> entities = new ArrayList<Entity>();

    Window window;
    private static float scale;
    // ------

    private int main_shader_id;
    private int texture_uni_0;

    public Scene(Window window) {
        this.window = window;
        this.width = window.getWidth();
        this.height = window.getHeight();

        camera = new Camera();
        projection = new Projection(width, height);

        main_shader = new Shader();
        scale = 1;
    }

    public void init() {
        System.out.println("entities list: " + entities.size());
        // setting up shader:
        main_shader.init("/Users/jareemhoff/dev/java/banter/res/shaders/core/vertex.glsl", "/Users/jareemhoff/dev/java/banter/res/shaders/core/fragment.glsl");
        main_shader_id = main_shader.getShaderProgramId();
        uniformsMap = new UniformsMap(main_shader_id);

        // Camera and projection of camera:
        view_matrix = camera.getViewMatrix();
        projection_matrix = projection.getProjMatrix();

        // TODO: Camera control needs to be moved into an actual input handler device.
        // view_matrix.translate(new Vector3f(0.0f, 0.0f, -5.0f));
        // view_matrix.rotate((float) Math.toRadians(0.0f), new Vector3f(0.0f, 1.0f, 0.0f));

        // mesh.init(positions, colors, indices);
        // setup texture:
        texture = new Texture("/Users/jareemhoff/dev/java/banter/res/textures/hexagon-pavers/hexagon-pavers1_albedo.png", main_shader_id);
        // Activate the shader to set the uniform
        // shader.use();

        // uniform in texture0:
        uniformsMap.createUniform("texture_sampler_0");
        // uniform in projection_matrix:
        uniformsMap.createUniform("view_matrix");
        uniformsMap.createUniform("model_matrix");
        uniformsMap.createUniform("projection_matrix");
        // glEnable(GL_Cull)
    };

    public void render() {

        // Shader Uniforms:
        main_shader.use();
        uniformsMap.setUniform("projection_matrix", projection.getProjMatrix());
        uniformsMap.setUniform("view_matrix", view_matrix);
        uniformsMap.setUniform("texture_sampler_0", 0);
        texture.bind(0);

        for (Entity entity : entities) {
            // matrices and uniforms:
            projection.updateProjMatrix(window.getWidth(), window.getHeight());
            uniformsMap.setUniform("model_matrix", entity.getTransformationMatrix());

            // Do the binding and stuff:
            glBindVertexArray(entity.getModel().getVaoID());
            glDrawElements(GL_TRIANGLES, entity.getModel().getIndexCount(), GL_UNSIGNED_INT, 0);
        }

        // bind textures slot:

        // Use the shader program
        // main_shader.use();

        // completely optional to unbind the vao:
        texture.unbind();
        // glBindVertexArray(0);
        // glDisableVertexAttribArray(0);
    }

    public void cleanup() {
        main_shader.clean();
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

    /**
     * @param entity - add and entity to the scene
     **/
    public void add_entity(Entity entity) {
        entities.add(entity);
        System.out.println("Entity added: " + entity.getId());
    }

    public List<Entity> get_entities() {
        return entities;
    }

    /**
     * @return Camera from the scene.
     **/
    public Camera get_camera() {
        return camera;
    }
}
