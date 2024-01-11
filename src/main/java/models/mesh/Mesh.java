package models.mesh;

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

@SuppressWarnings("unused")
public class Mesh {
    // IDs and List for VAO and Buffers:
    private int vao_id;
    private int vertexCount;
    private List<Integer> vboList = new ArrayList<>();

    public void init(float[] positions, float[] colors, float[] texture_coords, int[] indices) {
        vao_id = glGenVertexArrays();
        vertexCount = positions.length / 3;
        System.out.println("\n|----------------|");
        System.out.println("| VAO Created:\t" + vao_id + "|");
        System.out.println("|----------------|");
        System.out.println("| Vertex Count:\t" + vertexCount + "|");
        System.out.println("|----------------|");
        glBindVertexArray(vao_id);
        // Create that VBO:
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // positions (location = 0):
            VBO loc0 = new VBO("positions", 0, 3, positions);
            vboList.add(loc0.getVboId());

            // colors (location = 1):
            if(colors != null) {
                VBO loc1 = new VBO("colors", 1, 3, colors);
                vboList.add(loc1.getVboId());
            }
            
            // texture_coords (location = 2):
            if(texture_coords != null){
                VBO loc2 = new VBO("texture_coords", 2, 2, texture_coords);
                vboList.add(loc2.getVboId());
            }
            
            // EBO for indices:
            EBO ebo = new EBO("indices", indices);
            vboList.add(ebo.getEboId());

        }
        System.out.println("|----------------|\n");
        glBindBuffer(GL_ARRAY_BUFFER , 0);
        glBindVertexArray(0);
    }

    public void clean() {
        vboList.forEach(GL30::glDeleteBuffers);
        glDeleteVertexArrays(vao_id);
    }

    public int getVaoId() {
        return vao_id;
    }

    public List<Integer> getVboList() {
        return vboList;
    }
}
