package rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.List;

import utils.Utils;

@SuppressWarnings("unused")
public class Shader {
    private String vertSrc;
    private String fragSrc;
    private int shaderProgramId;
    // Idea for mapping shaders to programs:
    List<Integer> shaderList = new ArrayList<>();
    List<Integer> programList = new ArrayList<>();

    public void init() {
        vertSrc = Utils.readFile("/Users/jareemhoff/dev/java/banter/res/shaders/core/vertex.glsl");
        fragSrc = Utils.readFile("/Users/jareemhoff/dev/java/banter/res/shaders/core/fragment.glsl");
        create_shader(vertSrc, GL_VERTEX_SHADER);
        create_shader(fragSrc, GL_FRAGMENT_SHADER);
        create_program();
    }

    private void create_shader(String shaderSrc, int type) {
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

    private void create_program() {
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

    public void use() {
        // use the program
        glUseProgram(shaderProgramId);
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

    public int getShaderProgramId() {
        return shaderProgramId;
    }

}
