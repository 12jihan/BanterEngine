package models;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

import org.lwjgl.system.MemoryStack;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture {
    private int textureId;
    private String texturePath;

    // public Texture(int width, int height, ByteBuffer buf) {
    // this.texturePath = "";
    // generateTexture(width, height, buf);
    // }

    public Texture(String texture_path) {
        this.texturePath = texture_path;
        init(texturePath);
    }

    public void init(String texturePath) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            this.texturePath = texturePath;
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer buf = stbi_load(texturePath, w, h, channels, 4);
            if (buf == null) {
                throw new RuntimeException(
                        "Image file [\n" + texturePath + "\n] not loaded: \n" + stbi_failure_reason());
            }

            int width = w.get();
            int height = h.get();

            generateTexture(width, height, buf);

            stbi_image_free(buf);

        }
    }

    private void generateTexture(int width, int height, ByteBuffer buf) {
        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGBA,
                width,
                height,
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                buf.flip());
        glGenerateMipmap(GL_TEXTURE_2D);

    }

    public void bind() {
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
