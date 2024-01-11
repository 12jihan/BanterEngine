package models.entity;

import models.mesh.Mesh;

public class Model {

    private int vaoID;
    private int vertexCount;

    public Model(Mesh mesh) {
        this.vaoID = mesh.getVaoId();
        this.vertexCount = 0;
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
