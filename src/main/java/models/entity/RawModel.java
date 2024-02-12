package models.entity;

import models.mesh.Mesh;

public class RawModel {

    private int vaoID;
    private int vertexCount;
    private int indexCount;

    public RawModel(Mesh mesh) {
        this.vaoID = mesh.getVaoId();
        this.vertexCount = mesh.getVertexCount() / 3;
        this.indexCount = mesh.getIndexCount();
    }

    /**
     * Getters:
     **/

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getIndexCount() {
        return indexCount;
    }

}
