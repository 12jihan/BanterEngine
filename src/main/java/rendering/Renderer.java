package rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import models.entity.RawModel;

@SuppressWarnings("unused")
public class Renderer {

    private Scene scene;

    public Renderer(Scene scene) {
        this.scene = scene;
    }

    public void init() {
        scene.init();
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0.5f, 0.2f, 0f, 0f);
        // Use this to render in wireframe mode:
        // if (wired) {
        // glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        // } else {
        // glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        // }
        // ;

        scene.render();
        // Maybe this should be use instead but i'm not sure yet
        // glBindVertexArray(0);
        // glDisableVertexAttribArray(0);
    }

    public void cleanup() {
        scene.cleanup();
    }
}
