package models;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

@SuppressWarnings("unused")
public class Texture {
    private int textureId;
    private int texture0Uni;
    private String texturePath;
    private int shaderProgramId;

    public Texture(String texture_path, int shaderProgramId) {
        this.texturePath = texture_path;
        this.shaderProgramId = shaderProgramId;
        init(texturePath);
    }

    public void init(String texturePath) {
        this.texturePath = texturePath;
        System.out.println("Texture path:\t" + texturePath);
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        // Flip the photo vertically because of the way stbi works:
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(texturePath, w, h, channels, 4);
        if (image == null) {
            throw new RuntimeException(
                    "Image file [\n" + texturePath + "\n] not loaded: \n" + stbi_failure_reason());
        }
        image.flip();
        int width = w.get();
        int height = h.get();

        generateTexture(width, height, image);
        stbi_image_free(image);
    }

    private void generateTexture(int width, int height, ByteBuffer image) {
        this.textureId = glGenTextures();
        System.out.println("Texture ID:\t" + this.textureId);
        System.out.println("Texture size:\t" + width + " x " + height + " px\n" );
        glBindTexture(GL_TEXTURE_2D, this.textureId);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGBA,
                width,
                height,
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                image);
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
