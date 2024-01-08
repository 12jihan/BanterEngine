package models;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.opengl.GL30;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Mesh {
    // IDs and List for VAO and Buffers:
    private int vao_id;
    private List<Integer> vboList = new ArrayList<>();

    public void init(float[] positions, float[] colors, float[] texture_coords, int[] indices) {
        vao_id = glGenVertexArrays();
        System.out.println("VAO ID:\t" + vao_id);
        glBindVertexArray(vao_id);
        // Create that VBO:
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // positions (location = 0):
            VBO loc0 = new VBO("positions", 0, 3, positions);
            vboList.add(loc0.getVboId());

            // colors (location = 1):
            VBO loc1 = new VBO("colors", 1, 3, colors);
            vboList.add(loc1.getVboId());
            
            // texture_coords (location = 2):
            VBO loc2 = new VBO("texture_coords", 2, 2, texture_coords);
            vboList.add(loc2.getVboId());
            
            // EBO for indices:
            EBO ebo = new EBO("indices", indices);
            vboList.add(ebo.getEboId());
        }
        System.out.println("VBO List:\n\t" + vboList);
    }

    public void clean() {
        glDeleteVertexArrays(vao_id);
        vboList.forEach(GL30::glDeleteBuffers);
    }

    public int getVaoId() {
        return vao_id;
    }

    public List<Integer> getVboList() {
        return vboList;
    }
}
