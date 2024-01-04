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
    private List<Integer> vaoList = new ArrayList<>();
    private List<Integer> vboList = new ArrayList<>();
    private List<Integer> textureList = new ArrayList<>();

    // Texture:
    private Texture texture;

    public void init(float[] positions, float[] colors, int[] indices) {
        int vao_id = glGenVertexArrays();
        vaoList.add(vao_id);
        System.out.println("VAO ID:\t" + vao_id);
        glBindVertexArray(vao_id);
        // Create that VBO:
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // (location = 0):
            VBO loc0 = new VBO("position", 0, 3, positions);
            vboList.add(loc0.getVboId());
            loc0 = null;
            // (location = 1):
            VBO loc1 = new VBO("colors", 1, 3, colors);
            vboList.add(loc1.getVboId());
            loc1 = null;
            // (location = 1):
            // vbo_id = glGenBuffers();
            // vboList.add(vbo_id);
            // System.out.println("VBO ID:\t" + vbo_id);
            // glBindBuffer(GL_ARRAY_BUFFER, vbo_id);
            // FloatBuffer colors_buffer = stack.callocFloat(colors.length);
            // colors_buffer.put(colors).flip();
            // glBufferData(GL_ARRAY_BUFFER, colors_buffer, GL_DYNAMIC_DRAW);
            // glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
            // glEnableVertexAttribArray(1);
            // // Unbind the VBO after use:
            // glBindBuffer(GL_ARRAY_BUFFER, 0);
            

            int ebo = glGenBuffers();
            System.out.println("EBO ID:\t" + ebo);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
            IntBuffer intbuf = stack.callocInt(indices.length);
            intbuf.put(indices).flip();
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, intbuf, GL_DYNAMIC_DRAW);
        }

        // texture = new Texture("/Users/jareemhoff/dev/java/banter/src/resources/textures/grass.png");
        // textureList.add(texture.getTextureId());
        // texture.bind();
        System.out.println("VBO List:\n\t" + vboList);


    }

    // public int create_texture() {
    //     int texture_id = glGenTextures();
    //     textureList.add(texture_id);
    //     return texture_id;
    // }

    // public void create_texture(String texture_path) {
    //     String path;
    //     try (MemoryStack stack = MemoryStack.stackPush()) {
    //         path = texture_path;
    //         IntBuffer w = stack.mallocInt(1);
    //         IntBuffer h = stack.mallocInt(1);
    //         IntBuffer channels = stack.mallocInt(1);

    //         ByteBuffer buf = stbi_load(texturePath, w, h, channels, 4);
    //         if (buf == null) {
    //             throw new RuntimeException(
    //                     "Image file [\n" + texturePath + "\n] not loaded: \n" + stbi_failure_reason());
    //         }

    //         int width = w.get();
    //         int height = h.get();

    //         generateTexture(width, height, buf);

    //         stbi_image_free(buf);

    //     }
    // }

    public Object getTexture() {
        
        return null;
    }

    public void clean() {
        textureList.forEach(GL30::glDeleteTextures);
        vboList.forEach(GL30::glDeleteBuffers);
        vaoList.forEach(GL30::glDeleteVertexArrays);
    }

    public List<Integer> getVaoList() {
        return vaoList;
    }

    public List<Integer> getVboList() {
        return vboList;
    }

    public List<Integer> getTextureList() {
        return textureList;
    }
}
