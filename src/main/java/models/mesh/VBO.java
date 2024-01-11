package models.mesh;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.lang.reflect.Array;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.system.MemoryStack;

public class VBO {
    private String descriptor;
    private int vboId;

    public VBO(String descriptor, int index, int size, float[] vertices) {
        this.descriptor = descriptor.substring(0, 3);
        create();
        bind();
        create_and_fill_buffer(vertices);
        glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(index);
        unbind();
    }
    
    public VBO(String descriptor, int index, int size, int[] vertices) {
        this.descriptor = descriptor;
        create();
        bind();
        create_and_fill_buffer(vertices);
        glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(index);
        unbind();
    }

    private void create() {
        vboId = glGenBuffers();
        System.out.println("| VBO " + descriptor + ":\t" + vboId + "|");
    }
    private void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
    }

    private void create_and_fill_buffer(float[] vertices) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buf = stack.callocFloat(vertices.length);
            buf.put(vertices);
            buf.flip();
            glBufferData(GL_ARRAY_BUFFER, buf, GL_STATIC_DRAW);
        }
    }

    private void create_and_fill_buffer(int[] vertices) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer buf = stack.callocInt(vertices.length);
            buf.put(vertices);
            buf.flip();
            glBufferData(GL_ARRAY_BUFFER, buf, GL_STATIC_DRAW);
        }
    }


    // Unbind the VBO after use:
    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void delete(int vbo_id) {
        glDeleteBuffers(vbo_id);
    }

    public int getVboId() {
        return vboId;
    }

    public String getDescriptor() {
        return descriptor;
    }
}
