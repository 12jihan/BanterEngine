package loader;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.system.MemoryStack;

public class Loader {
    // Not entirely sure if this is what I'll need but we'll see:
    // This includes the VAO, VBO, and EBO:
    // Still don't know what the fuck a EBO is.
    private int numVertices;
    private int vao_id;
    private boolean weDebug = false;
    private List<Integer> vboIdList = new ArrayList<>();

    Loader(boolean weDebug) {
        // Just to debug AKA console log some more shit i don't care about:
    }

    Loader() {
        // Boring old class if there's nothing passed in:
    }

    // Creating the Vertex Array Object to load vbos in:
    // Most likely what I'mg planning on doing is just having every single vector in
    // it's own VBO|EBO so that I don't have to worry about bullshit that I don't
    // need in this fucking application:
    void createVAO() {
        vao_id = glGenVertexArrays();
        glBindVertexArray(vao_id);
        if (weDebug) {
            System.out.println("VAO created:\n" + "\tVAO_ID: \t" + vao_id + "\n");
        }

        // For now we aren't going to return this value..
        // what we'll do instead is set the instance value and use getters:
        // return vao_id;
    }

    /** 
     * 
     * @param data : Float Array - for creating and binding to a vbo and adding to a list.
     */
    void createVBO(float[] data) {
        // get the id of the vbo:
        int vbo_id = glGenBuffers();
        // Check if there is indeed a VAO ID - otherwise it will just fail and throw an exception:
        if(vao_id > 0) {
            // Create memory stack to access for the buffers being created:
            try (MemoryStack stack = MemoryStack.stackPush()) {
                // Add the buffer's id to a list so that we can keep track:
                vboIdList.add(vbo_id);
                // create the buffer so that we can bind it:
                FloatBuffer dataBuffer = stack.callocFloat(data.length);
                dataBuffer.put(0, data);
                // Bind buffer and tell it what to do with the data buffer?
                glBindBuffer(GL_ARRAY_BUFFER, vbo_id);
                glBufferData(vbo_id, dataBuffer, GL_STATIC_DRAW);
                // Index in the vertex array will decide entirely where to place the vbo:
                // Hoping this doesn't shit on me at all.
                glEnableVertexAttribArray(vboIdList.size() - 1);
            }
        }

        // taking this out for now because i'm unsure if I will truely need to re-return the vbo_id
        // return vbo_id;
    }

    // Gotta figure out the best way to handl the ebos:
    // I still don't fucking understanding how to create it or what makes it
    // completely diff from VBOs. But I know exactly what it is. Jeeez....
    int createEBO() {
        int ebo_id = 0;
        // Nothing happening here just yet:
        return ebo_id;
    }

    /**
     * Getters/ Setters:
     */
    // Get current VAO ID:
    public int getVao_id() {
        return vao_id;
    }

    // Get list of VBOS:
    public List<Integer> getVboIdList() {
        return vboIdList;
    }

}
