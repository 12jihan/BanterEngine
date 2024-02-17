package rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import models.entity.RawModel;

@SuppressWarnings("unused")
public class Renderer {

    private Scene scene;
    private boolean wired = false;

    public Renderer(Scene scene) {
        this.scene = scene;
    }

    public void init() {
        scene.init();
    }

    public void render() {
        glClearColor(0.1f, 0.1f, 0f, 0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        // Use this to render in wireframe mode:
        // if (false) {
        //     glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        //     glDisable(GL_TEXTURE_2D);
        // } else {
        //     glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        // }

        scene.render();
        // Maybe this should be use instead but i'm not sure yet:
        glBindVertexArray(0);
        glDisableVertexAttribArray(0);
    }

    public void wired() {
        wired = !wired;
    }

    public void cleanup() {
        scene.cleanup();
    }
}
