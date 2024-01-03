package loader;

public class Model {

    private int vaoID;
    private int vertexCount;

    public Model(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
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
