package models.entity;

import models.mesh.Mesh;

public class RawModel {

    private int vaoID;
    private int vertexCount;

    public RawModel(Mesh mesh) {
        this.vaoID = mesh.getVaoId();
        this.vertexCount = mesh.getVertexCount();
        System.out.println("Model created:\t" + vaoID + "\t" + vertexCount);
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

}
