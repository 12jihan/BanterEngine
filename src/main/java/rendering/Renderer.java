package rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import loader.RawModel;

import static org.lwjgl.opengl.GL11.glClear;

public class Renderer {

    public void prepare() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0f, 0.2f, 0f, 0f);
    }

    public void render(RawModel model) {
        System.out.println("Binding VAO:\t" + model.getVaoID());
        glBindVertexArray(model.getVaoID());
        // glEnableVertexAttribArray(0);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        // glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        // glDisableVertexAttribArray(0);
        // glBindVertexArray(0);
    }
}
