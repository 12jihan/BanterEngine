package loader;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

public class Mesh {
    // List for VAOs and VBOs:
    private List<Integer> vaoList = new ArrayList<>();
    private List<Integer> vboList = new ArrayList<>();

    public Mesh() {
        // Create that VAO:

    }

    public void init(float[] positions, int[] indices) {
        int vao_id = glGenVertexArrays();
        vaoList.add(vao_id);
        System.out.println("VAO ID:\t" + vao_id);
        glBindVertexArray(vao_id);

        // Create that VBO:
        try (MemoryStack stack = MemoryStack.stackPush()) {
            int vbo_id = glGenBuffers();
            System.out.println("VBO ID:\t" + vbo_id);
            vboList.add(vbo_id);
            // Bind the VBO:
            glBindBuffer(GL_ARRAY_BUFFER, vbo_id);

            FloatBuffer vbobuf = stack.callocFloat(positions.length);
            vbobuf.put(positions).flip();
            glBufferData(GL_ARRAY_BUFFER, vbobuf, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(0);
            // Unbind the VBO after use:
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            int ebo = glGenBuffers();
            System.out.println("EBO ID:\t" + ebo);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
            IntBuffer intbuf = stack.callocInt(indices.length);
            intbuf.put(indices).flip();
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, intbuf, GL_STATIC_DRAW);
        }

    }

    void clean() {
        vaoList.forEach(GL30::glDeleteVertexArrays);
        vboList.forEach(GL30::glDeleteBuffers);
    }

    public List<Integer> getVaoList() {
        return vaoList;
    }

    public List<Integer> getVboList() {
        return vboList;
    }
}
