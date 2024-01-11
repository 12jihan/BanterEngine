package models.mesh;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

@SuppressWarnings("unused")
public class EBO {
    private String descriptor;
    private int eboId;

    public EBO(String descriptor, int[] indices) {
        this.descriptor = descriptor.substring(0, 3);
        create();
        bind();
        create_and_fill_buffer(indices);
        // Can't unbind ???
        // unbind();
    }

    private void create() {
        eboId = glGenBuffers();
        System.out.println("| EBO " + this.descriptor + ":\t" + eboId + "|");
    }
    private void bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
    }

    private void create_and_fill_buffer(int[] indices) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer buf = stack.callocInt(indices.length);
            buf.put(indices).flip();
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, buf, GL_STATIC_DRAW);
        }
    }

    // Unbind the EBO after use:
    public void unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void delete(int vbo_id) {
        glDeleteBuffers(vbo_id);
    }

    public int getEboId() {
        return eboId;
    }

    public String getDescriptor() {
        return descriptor;
    }
}
