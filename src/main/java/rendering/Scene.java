package rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL14.glBlendEquation;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.List;

import models.Mesh;
import models.Texture;
import models.Texture2D;

@SuppressWarnings("unused")
public class Scene {
    private static Shader shader;
    private static Mesh mesh;
    private static Texture texture;
    private boolean wired = false;
    private int shader_id;
    private int texture_uni_0;

    public Scene() {
        this.wired = false;
        shader = new Shader();
        mesh = new Mesh();
    }

    public void init() {
        // Vertices for testing:
        float[] positions = new float[] {
                -0.5f,  -0.5f, 0.0f,
                 0.5f,  -0.5f, 0.0f,
                 0.5f,   0.5f, 0.0f,
                -0.5f,   0.5f, 0.0f
        };

        float[] colors = new float[] {
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };

        int[] indices = new int[] {
                0, 1, 3,
                1, 2, 3
        };

        float[] texture_coords = new float[] {
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f,
        };

        // setting up shader:
        shader.init();
        shader_id = shader.getShaderProgramId();

        // setting up mesh info:
        mesh.init(positions, colors, texture_coords, indices);

        // setup texture:
        texture = new Texture("/Users/jareemhoff/dev/java/banter/res/textures/checkermap.png", shader_id);

        // Activate the shader to set the uniform
        shader.use();

        // uniform in texture:
        texture_uni_0 = glGetUniformLocation(shader_id, "texture0");
        if(texture_uni_0 == -1) {
            System.err.println("Could not find uniform!");
            System.exit(-1);
        }

    };
    
    public void render() {
        glClearColor(0.2f, 0.25f, 0f, 0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        // Use this to render in wireframe mode:
        if (wired) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        } else {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }
        shader.use();
        // Set the texture uniform to use texture unit 0
        glUniform1i(texture_uni_0, 0);
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

    public void cleanup() {
        shader.clean();
    }

    public void wired() {
        wired = !wired;
    }
}
