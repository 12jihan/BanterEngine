package utils;

import org.joml.Matrix4f;

public class Projection {

    private static final float FOV = (float) Math.toRadians(90.0f);
    private static final float Z_FAR = 100.0f;
    private static final float Z_NEAR = 0.1f;
    private int width;
    private int height;

    private Matrix4f projMatrix;

    public Projection(int width, int height) {
        projMatrix = new Matrix4f();
        updateProjMatrix(width, height);
        this.width = width;
        this.height = height;
    }

    public Matrix4f getProjMatrix() {
        return projMatrix;
    }

    public void updateProjMatrix(int width, int height) {
        projMatrix.setPerspective(FOV, (float) width / height, Z_NEAR, Z_FAR);
        System.out.println("width: " + width + " " + "height: " + height);
    }

    public String getView() {
        return "width: " + this.width + " " + "height: " + this.height;
    }
}
