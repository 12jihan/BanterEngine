package shader;

import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL30;

import utils.Utils;

public class bu_ShaderProgram {
    private final int programId;

    public bu_ShaderProgram(List<ShaderModuleData> shaderModuleDataList) throws Exception {
        // Creates a shader program and returns its ID:
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }

        // Create an array  of "shader modules" and add - {shaderCode: String, shaderType: int} for each filepath in shaderModuleDataList:
        List<Integer> shaderModules = new ArrayList<>();
        shaderModuleDataList.forEach(s -> {
            try {
                // reads each file and creates a shader module for each file:
                shaderModules.add(createShader(Utils.readFile(s.shaderFile), s.shaderType));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Link the shader modules to the program:
        link(shaderModules);

    }
    
    // called when the shader program is needed:
    public void bind() {
        glUseProgram(programId);
    }

    // called when the shader program is no longer needed:
    public void unbind() {
        // just unbinds the shader program:
        glUseProgram(0);
    }
    
    // cleanup unbinds and deletes the shader program:
    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }

    // internal method to create a shader:
    protected int createShader(String shaderCode, int shaderType) throws Exception {
        // Create shader and return its ID:
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        // sets the source code to the specified shader id and then attempt to compile it:
        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        // Attach the shader to the program:
        glAttachShader(programId, shaderId);

        return shaderId;
    }

    public int getProgramId() {
        return programId;
    }

    private void link(List<Integer> shaderModules) {
        // Link the shader modules to the program and check for any errors with linking:
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Error linking Shader code:\n" + glGetProgramInfoLog(programId, 1024));
        }

        // Detach the shader modules from the program and delete them:
        shaderModules.forEach(s -> glDetachShader(programId, s));
        shaderModules.forEach(GL30::glDeleteShader);
    }

    public void validate() {
        // Validate the shader program and check for any errors with validation:
        // Though this doesn't really work with mac so I kinda ignored this bit.
        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }
    // Used to store the shader file path and the shader type:
    public record ShaderModuleData(String shaderFile, int shaderType) {
    }
}
