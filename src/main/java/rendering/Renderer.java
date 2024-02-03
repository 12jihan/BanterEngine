package rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import models.entity.RawModel;

@SuppressWarnings("unused")
public class Renderer {

    public void prepare() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0.5f, 0.2f, 0f, 0f);
    }

    public void render(Scene scene) {
        // TODO: Take a look at this:
        // glBindVertexArray(model.getVaoID());
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
        glDisableVertexAttribArray(0);
    }
}
