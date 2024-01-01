package shader_handling;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

import utils.Utils;

public class ShaderProgram {

    private int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    public void init(String vertexFile, String fragmentFile) {
        // Create an array of "shader modules" and add - {shaderCode: String,
        // shaderType: int} for each filepath in shaderModuleDataList:
        try {
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Shader");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            vertexShaderId = loadShader(vertexFile, GL_VERTEX_SHADER);
            fragmentShaderId = loadShader(fragmentFile, GL_FRAGMENT_SHADER);
            System.out.println(
                    "Vertex Shader ID:\t" +
                            vertexShaderId +
                            "\nFragment Shader ID:\t" +
                            fragmentShaderId);
        } catch (Exception e) {
            System.err.println("Could not load shader:\n\t -" + e.getMessage());
        }
        programId = glCreateProgram();
        // System.out.println("Shader program created:\t" + programId);
        System.out.println("Shader program created:\t" + programId);
        if (programId == 0) {
            System.out.print("Could not create Shader");
        }
        // Attach the shaders to the program:
        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);
        System.out.println("- program attached:\t");

        // Link the shader modules to the program:
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("Could not link shader program!");
            System.exit(-1);
        }
        System.out.println("- program linked\t");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

        // Still doesn't work on mac?? or do i just need to do more research?
        // Validate the program:
        // glValidateProgram(programId);
        // if (glGetProgrami(programId, GL_VALIDATE_STATUS) == GL_FALSE) {
        // System.err.println("Could not validate shader program!");
        // System.exit(-1);
        // }
    }

    // start the use of the program:
    public void start() {
        glUseProgram(programId);
        System.out.println("Used program:\t" + programId);
    };

    // Stop the program:
    public void stop() {
        glUseProgram(0);
    };

    // stop use of program and detach shaders and delete program:
    public void cleanup() {
        stop();
        glDetachShader(programId, vertexShaderId);
        glDetachShader(programId, fragmentShaderId);
        glDeleteShader(vertexShaderId);
        glDeleteShader(fragmentShaderId);
        // Delete the program:
        glDeleteProgram(programId);
    }

    protected void bindAttribute(int attribute, String variableName) {
        glBindAttribLocation(programId, attribute, variableName);

    }

    private static int loadShader(String file, int type) throws Exception {
        String shaderSource;
        try {
            shaderSource = Utils.readFile(file);
        } catch (Exception e) {
            System.err.println("Could not read file!\n\t- " + file);
            e.printStackTrace();
            System.exit(-1);
            return -1;
        }

        // Set shader type:
        int shaderId = glCreateShader(type);
        // Set the shader source code to the id:
        glShaderSource(shaderId, shaderSource);
        // Compile the shader:
        glCompileShader(shaderId);
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println(glGetShaderInfoLog(shaderId, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        System.out.println("Shader compiled:\t" + shaderId);
        // creates a shader and returns its ID:
        // return createShader(shaderSource, type);
        return shaderId;
    }

    protected static int createShader(String shaderCode, int shaderType) throws Exception {
        // Create shader and return its ID:
        int shaderId = glCreateShader(shaderType);
        System.out.println("Shader created 2nd time??:\t" + shaderId);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        // sets the source code to the specified shader id and then attempt to compile
        // it:
        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        return shaderId;
    }
}
