package loader;

public class RawModel {

    private int vaoID;
    private int vertexCount;

    public RawModel(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
        System.out.println("RawModel created:\t" + vaoID + "\t" + vertexCount);
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
