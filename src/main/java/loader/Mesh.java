package loader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.system.MemoryStack;


import models.Texture;
import org.lwjgl.opengl.GL30;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Mesh {
    // List for VAOs and Buffers:
    private List<Integer> vboList = new ArrayList<>();
    private List<Integer> textureList = new ArrayList<>();
    private int vao_id;

    // Texture:
    private Texture texture;

    public void init(float[] positions, float[] colors, int[] indices) {
        vao_id = glGenVertexArrays();
        // vaoList.add(vao_id);
        System.out.println("VAO ID:\t" + vao_id);
        glBindVertexArray(vao_id);
        // Create that VBO:
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // (location = 0):
            VBO loc0 = new VBO("position", 0, 3, positions);
            vboList.add(loc0.getVboId());
            // (location = 1):
            VBO loc1 = new VBO("colors", 1, 3, colors);
            vboList.add(loc1.getVboId());
            
            
            
            // EBO for indices:
            // (location = xxx):
            EBO ebo = new EBO("indices", indices);
            vboList.add(ebo.getEboId());
        }
        System.out.println("VBO List:\n\t" + vboList);
    }

    public Object getTexture() {
        
        return null;
    }

    public void clean() {
        textureList.forEach(GL30::glDeleteTextures);
        vboList.forEach(GL30::glDeleteBuffers);
        glDeleteVertexArrays(vao_id);
    }

    public int getVaoId() {
        return vao_id;
    }

    public List<Integer> getVboList() {
        return vboList;
    }

    public List<Integer> getTextureList() {
        return textureList;
    }
}
