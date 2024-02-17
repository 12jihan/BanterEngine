package models;

public class TerrainGenerator {

    private int width = 100; // Width of the grid
    private int depth = 100; // Depth of the grid
    private float[][] heights = new float[width][depth]; // Placeholder for heights

    public TerrainGenerator() {

    }

    public void generate() {

        for (int z = 0; z < depth; z++) {
            for (int x = 0; x < width; x++) {
                float height = getHeight(x, z); // Implement getHeight to determine the height at (x, z)
                heights[x][z] = height;
                // Store the vertex (x, height, z) in your mesh data structure
            }
        }

    }

    public float[][] getHeight() {
        return heights;
    }

    private float getHeight(int x, int z) {
        return 0;
    }
}
