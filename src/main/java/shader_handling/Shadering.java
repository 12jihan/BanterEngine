package shader_handling;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.ArrayList;
import java.util.List;
import utils.Utils;

public class Shadering {
    private String vertSrc;
    private String fragSrc;
    private int shaderProgramId;
    // Idea for mapping shaders to programs:
    // Still not sure how this is going to work but it will come in time:
    // private Map<Integer, Set<Integer>> programToShadersMap = new HashMap<>();
    // private Set<Integer> programIds = new HashSet<>();
    List<Integer> shaderList = new ArrayList<>();
    List<Integer> programList = new ArrayList<>();

    public void init() {
        // Read src files:
        vertSrc = Utils.readFile("/Users/jareemhoff/dev/java/banter/src/resources/vertex.glsl");
        fragSrc = Utils.readFile("/Users/jareemhoff/dev/java/banter/src/resources/fragment.glsl");
        
        create_shader(vertSrc, GL_VERTEX_SHADER);
        create_shader(fragSrc, GL_FRAGMENT_SHADER);
        create_program();
        System.out.println("Shader List:\n\t" + shaderList);
        System.out.println("Program, List:\n\t" + programList);
        // programToShadersMap.put(shaderProgramId, new HashSet<>(shaderList));
        // System.out.println("Program to Shaders Map:\n\t" + programList);
    }

    public void create_shader(String shaderSrc, int type) {
        int shaderId = glCreateShader(type);
        System.out.println("shader created:\t" + shaderId + " | " + type);
        glShaderSource(shaderId, shaderSrc);
        glCompileShader(shaderId);
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println(glGetShaderInfoLog(shaderId, 500));
            System.err.println("Could not compile vert shader!");
            System.exit(-1);
        }
        System.out.println("Shader compiled succesfully:\t" + shaderId);
        shaderList.add(shaderId);

    }

    public void create_program() {
        shaderProgramId = glCreateProgram();
        if (shaderProgramId == 0) {
            System.err.println("Shader did not create shader!");
            System.exit(-1);
        }
        System.out.println("Shader program created:\t" + shaderProgramId);

        for (int item : shaderList) {
            glAttachShader(shaderProgramId, item);
            System.out.println("Shader attached:\t" + item);
        }

        glLinkProgram(shaderProgramId);
        if (glGetProgrami(shaderProgramId, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("Could not link shader program!");
            System.exit(-1);
        }

        /** 
         * There's supposed to be a validation layer but I don't think it works well with mac m1 machines.
        */
    }

    public void render(int vao) {
        // use the program
        glUseProgram(shaderProgramId);
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

        //completely optional to unbind the vao:
        glBindVertexArray(0);
        glDisableVertexAttribArray(0);
    }


    public void clean() {
        glUseProgram(0);
        for (int item : shaderList) {
            System.out.println("Shader detached and deleted:\t" + item);
            glDetachShader(shaderProgramId, item);
            glDeleteShader(item);
        }

        for (int item : programList) {
            System.out.println("Shader program deleted:\t" + item);
            glDeleteProgram(item);
        }
        System.out.println("Shader program wiped clean!");
    }
}
