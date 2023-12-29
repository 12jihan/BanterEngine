package loader;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

public class Loader {
    // Not entirely sure if this is what I'll need but we'll see:
    // This includes the VAO, VBO, and EBO:
    // Still don't know what the fuck a EBO is.
    private int numVertices;
    // Random debu setting:
    private boolean weDebug = false;

    // For now I have this here instead because i'm not entirely sure if i need a
    // list or something else:
    // private int vao_id;

    // List for VAOs and VBOs:
    private List<Integer> vaoList = new ArrayList<>();
    private List<Integer> vboList = new ArrayList<>();

    // Loader(boolean weDebug) {
    //     // Just to debug AKA console log some more shit i don't care about:
    // }

    // Loader() {
    //     // Boring old class if there's nothing passed in:
    // }

    // I guess it's better to name parameters for the generic shaders since they are static?
    public RawModel loadToVAO(float[] positions, int[] indices) {
        // Create and bind VAO:
        int vao_id = createVAO();
        // creates a vbo and ebo, then add it to the bound vao at specified position:
        createEBO(indices);
        createVBO(0, 3, positions);
        // unbind and return the rawmodel:
        unbindVAO();
        // TODO: figure out what positions.length/3 is supposed to be
        // I'm still not really understanding what the point of it is.
        // udpate: I think it's because it's a 3d vector so we want to say how many positions there are.
        return new RawModel(vao_id, indices.length);
    }

    // Creating the Vertex Array Object to load vbos in:
    // Most likely what I'mg planning on doing is just having every single vector in
    // it's own VBO|EBO so that I don't have to worry about bullshit that I don't
    // need in this fucking application:
    private int createVAO() {
        // Create a vao then push it into the Vao List and bind the Vao:
        int vao_id = glGenVertexArrays();
        vaoList.add(vao_id);
        glBindVertexArray(vao_id);
        // Some random debug shit:
        weDebug = true;
        if (weDebug) {
            System.out.println(
                    "VAO created:\n" +
                            "\tVAO_ID: \t" +
                            vao_id +
                            "\n");
        }

        // Returning the vao_id of the vao created:
        return vao_id;
    }

    // unbind method for VAO:
    private void unbindVAO() {
        glBindVertexArray(0);
    }

    /**
     * 
     * @param data      : Float Array - for creating and binding to a vbo and adding
     *                  to a list.
     * @param data_size : Int that states the size of the data stride wise.
     */
    private void createVBO(int attrib_num, int vector_size, float[] data) {
        // get the id of the vbo:
        int vbo_id = glGenBuffers();
        vboList.add(vbo_id);
        glBindBuffer(GL_ARRAY_BUFFER, vbo_id);
        FloatBuffer dataBuffer = storeDataInBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, dataBuffer, GL_STATIC_DRAW);
        // Settings for describing and loading vbo into vao:
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        // I'm not sure the purpose of glEnableVertexAttribArray yet:
        // glEnableVertexAttribArray(vboList.size() - 1);
        // Done using the vbo so unbind it:
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    // Gotta figure out the best way to handl the ebos:
    // I still don't fucking understanding how to create it or what makes it
    // completely diff from VBOs. But I know exactly what it is. Jeeez....
    private void createEBO(int[] indices) {
        int ebo_id = glGenBuffers();
        vboList.add(ebo_id);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo_id);
        IntBuffer dataBuffer = storeDataInBuffer(indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, dataBuffer, GL_STATIC_DRAW);
    }

    // converts out int array into a int buffer for loading into a vbo:
    private IntBuffer storeDataInBuffer(int[] data) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer dataBuffer = stack.callocInt(data.length);
            // puts the dat into the beginning of the Int buffer:
            dataBuffer.put(0, data);
            // Flip buffer to prepare it to be read from (common practice with buffers):
            dataBuffer.flip();

            // return the buffer for use:
            return dataBuffer;
        }
    }

    private FloatBuffer storeDataInBuffer(float[] data) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer dataBuffer = stack.callocFloat(data.length);
            // puts the dat into the beginning of the float buffer:
            dataBuffer.put(0, data);
            // Flip buffer to prepare it to be read from (common practice with buffers):
            dataBuffer.flip();

            // return the buffer for use:
            return dataBuffer;
        }
    }

    public void cleanup() {
        // Delete lists and buffers created:
        vaoList.forEach(GL30::glDeleteVertexArrays);
        vboList.forEach(GL30::glDeleteBuffers);
    }

    /**
     * Getters/ Setters:
     */
    // Doing this instead of the list for now:
    // public int getVao_id() {
    // return vao_id;
    // }
    // Get list of VAOS:
    public List<Integer> getVaoList() {
        return vaoList;
    }

    // Get list of VBOS:
    public List<Integer> getVboList() {
        return vboList;
    }

}
