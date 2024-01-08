package models;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

import org.lwjgl.system.MemoryStack;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture {
    private int textureId;
    private int texture0Uni;
    private String texturePath;
    private int shaderProgramId;

    // public Texture(int width, int height, ByteBuffer buf) {
    // this.texturePath = "";
    // generateTexture(width, height, buf);
    // }

    public Texture(String texture_path, int shaderProgramId) {
        this.texturePath = texture_path;
        this.shaderProgramId = shaderProgramId;
        init(texturePath);
    }

    public void init(String texturePath) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            this.texturePath = texturePath;
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);
            System.out.println("Texture path:\t" + texturePath);
            ByteBuffer buf = stbi_load(texturePath, w, h, channels, 4);
            if (buf == null) {
                throw new RuntimeException(
                        "Image file [\n" + texturePath + "\n] not loaded: \n" + stbi_failure_reason());
            }
            buf.flip();
            int width = w.get();
            int height = h.get();

            generateTexture(width, height, buf);

            stbi_image_free(buf);

        }
    }

    private void generateTexture(int width, int height, ByteBuffer buf) {
        textureId = glGenTextures();
        System.out.println("Texture ID:\t" + textureId);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
        glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGBA,
                width,
                height,
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                buf);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void bind(int slot) {
        glActiveTexture(GL_TEXTURE0 + slot);
        glBindTexture(GL_TEXTURE_2D, textureId);
        
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void cleanup() {
        glDeleteTextures(textureId);
    }

    public String getTexturePath() {
        return texturePath;
    }

    public int getTextureId() {
        return textureId;
    }
}
