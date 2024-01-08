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

public class Scene {
    private boolean wired = false;
    Shader shader;
    Mesh mesh;
    Texture texture;
    int texture_uni_0;

    public Scene() {
        this.wired = false;
        shader = new Shader();
        mesh = new Mesh();
    }

    public void init() {
        // Vertices for testing:
        float[] positions = new float[] {
                0.5f, 0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                -0.5f, 0.5f, 0.0f
        };

        float[] colors = new float[] {
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.5f, 0.5f, 0.0f,
        };

        int[] indices = new int[] {
                0, 1, 3,
                1, 2, 3
        };

        float[] texture_coords = new float[] {
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 1.0f,
        };

        shader.init();
        mesh.init(positions, colors, texture_coords, indices);
        // texture = new Texture("/Users/jareemhoff/dev/java/banter/src/res/textures/checkermap.png",
        //         shader.getShaderProgramId());
        texture = new Texture("/Users/jareemhoff/dev/java/banter/src/res/textures/checkermap.png",
        shader.getShaderProgramId());
        texture.bind(0);
        texture_uni_0 = glGetUniformLocation(shader.getShaderProgramId(), "texture0");
        if(texture_uni_0 == -1) {
            System.err.println("Could not find uniform!");
            System.exit(-1);
        }
    };

    public void render() {
        glClearColor(0f, 0f, 0f, 0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_BLEND);
        glBlendEquation(GL_FUNC_ADD);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        // Use this to render in wireframe mode:
        if (wired) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        } else {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }
        shader.use();
        glActiveTexture(GL_TEXTURE0);
        texture_uni_0 = glGetUniformLocation(shader.getShaderProgramId(), "texture0");
        // glBindTexture(GL_TEXTURE_2D, texture.getTextureId());
        // texture.bind();
        glUniform1i(texture_uni_0, 0);


        glBindVertexArray(mesh.getVaoId());
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        // completely optional to unbind the vao:
        glBindVertexArray(0);
        glDisableVertexAttribArray(0);
    }

    public void clean() {
        shader.clean();
    }

    public void wired() {
        wired = !wired;
    }
}
