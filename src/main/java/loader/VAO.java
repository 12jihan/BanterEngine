package loader;

import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VAO {
    private int vao_id;

    public VAO() {

    }

    public void linkVBO(VBO VBO, int layout) {

    }

    public int create() {
        vao_id = glGenVertexArrays();
        return vao_id;
    }

    public void bind() {
        
    }

    public void unbind() {

    }

    public void delete() {

    }
}
