package models;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import javax.swing.text.Style;

public class Texture2D {
	
	private int id;
	private int width;
	private int height;
	
	public Texture2D(){}
	
	public Texture2D(String file)
	{
		int[] data = ImageLoader.loadImage(file);
        System.out.println("Texture ID:\t" + data[0]);
        System.out.println("Texture Width:\t" + data[1]);
        System.out.println("Texture Height:\t" + data[2]);
        
		id = data[0];
		width = data[1];
		height = data[2];
	}
	
	public void bind()
	{
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public void generate()
	{
		id = glGenTextures();
	}
	
	public void delete()
	{
		glDeleteTextures(id);
	}
	
	public void unbind()
	{
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void noFilter()
	{
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	}
	
	public void bilinearFilter()
	{
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	}
	
	public void trilinearFilter(){
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glGenerateMipmap(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
	}
	
	public int getId() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
