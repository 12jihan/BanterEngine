package gui;

import org.lwjgl.nuklear.NkBuffer;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkDrawNullTexture;
import org.lwjgl.nuklear.NkUserFont;

public class Gui {
    private long win; // The identifier of the GLFW window
    private int width, height; // The pixel dimensions of the GLFW window
    private int display_width, display_height; // The pixel dimensions of the content inside the window, this will usually be the same size as the window.

    private NkContext ctx = NkContext.create(); // Create a Nuklear context, it is used everywhere.
    private NkUserFont default_font = NkUserFont.create(); // This is the Nuklear font object used for rendering text.

    private NkBuffer cmds = NkBuffer.create(); // Stores a list of drawing commands that will be passed to OpenGL to render the interface.
    private NkDrawNullTexture null_texture = NkDrawNullTexture.create(); // An empty texture used for drawing.

    /**
     * The following variables are used for OpenGL.
     */
    private int vbo, vao, ebo;
    private int prog;
    private int vert_shdr;
    private int frag_shdr;
    private int uniform_tex;
    private int uniform_proj;

}
