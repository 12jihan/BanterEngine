package shader;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.ArrayList;
import java.util.List;

import utils.Utils;

public class ShaderProgram {

    boolean wired = false;
    private String vertSrc;
    private String fragSrc;
    private int shaderProgramId;
    private int textureID;
    // Idea for mapping shaders to programs:
    List<Integer> shaderList = new ArrayList<>();
    List<Integer> programList = new ArrayList<>();

    public void init(int textureID) {
        // Read src files:
        this.textureID = textureID;
        vertSrc = Utils.readFile("/Users/jareemhoff/dev/java/banter/src/resources/vertex.glsl");
        fragSrc = Utils.readFile("/Users/jareemhoff/dev/java/banter/src/resources/fragment.glsl");
        create_shader(vertSrc, GL_VERTEX_SHADER);
        create_shader(fragSrc, GL_FRAGMENT_SHADER);
        create_program();
    }

    public void create_shader(String shaderSrc, int type) {
        int shaderId = glCreateShader(type);
        glShaderSource(shaderId, shaderSrc);
        glCompileShader(shaderId);
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("\nCould not compile vert shader:");
            System.err.println( "\t- " + glGetShaderInfoLog(shaderId, 500));
            System.exit(-1);
        }
        shaderList.add(shaderId);
    }

    public void create_program() {
        shaderProgramId = glCreateProgram();
        if (shaderProgramId == 0) {
            System.err.println("Shader did not create shader!");
            System.exit(-1);
        }
        programList.add(shaderProgramId);
        // Attach shaders to program:
        for (int item : shaderList) {
            glAttachShader(shaderProgramId, item);
        }

        glLinkProgram(shaderProgramId);
        if (glGetProgrami(shaderProgramId, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("\nCould not link shader program:\n");
            System.err.println("\t- " + glGetProgramInfoLog(shaderProgramId, 500));
            System.exit(-1);
        }

        /**
         * There's supposed to be a validation layer here but I don't think it works well
         * with mac m1 machines.
         */
    }

    public void render(int vao) {
        glClearColor(0f, 0f, 0f, 0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        // Use this to render in wireframe mode:
        if (wired) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        } else {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }
        // use the program
        glUseProgram(shaderProgramId);
        // TODO: Texture:
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureID);

        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

        // completely optional to unbind the vao:
        glBindVertexArray(0);
        glDisableVertexAttribArray(0);
    }

    public void clean() {
        glUseProgram(0);
        for (int item : shaderList) {
            glDetachShader(shaderProgramId, item);
            glDeleteShader(item);
        }

        for (int item : programList) {
            glDeleteProgram(item);
        }
    }

    public void wired() {
        wired = !wired;
    }
}
