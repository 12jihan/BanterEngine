package loader;

import static org.lwjgl.opengl.GL30.*;

import java.util.List;

public class Loader {
    // Not entirely sure if this is what I'll need but we'll see:
    // This includes the VAO, VBO, and EBO:
    // Still don't know what the fuck a EBO is.
    private int numVertices;
    private int vaoId;
    private List<Integer> vboIdList;
    private List<Integer> eboIdList;

    Loader() {
        //TODO: Nothing here just yet... still working towards making this section less shitty... fingers crossed...
    }

    // Creating the Vertex Array Object to load vbos in:
    // Most likely what I'mg planning on doing is just having every single vector in it's own VBO|EBO so that I don't have to worry about bullshit that I don't need in this fucking application:
    int createVao() {
        int vao_id = glGenVertexArrays();
        glBindVertexArray(vao_id);
        return vao_id;
    }
}
