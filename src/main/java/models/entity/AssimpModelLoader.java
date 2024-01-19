package models.entity;

import org.pmw.tinylog.Logger;
import org.lwjgl.assimp.*;

import models.mesh.Mesh;
import org.lwjgl.PointerBuffer;


import static org.lwjgl.assimp.Assimp.*;

@SuppressWarnings("unused")
public class AssimpModelLoader {
    private AIScene scene;
    private Mesh[] meshes;
    private String model_id;
    
    public void load_model(String model_id, String model_path) {
        this.model_id = model_id;
    }

    public void load_model(String model_path) {
        scene = aiImportFile(model_path, aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_FlipUVs);
        if(scene == null) {
            Logger.error("Error loading model: \n" + model_path + "\n" + aiGetErrorString());
            System.exit(-1);
        };

        int numMeshes = scene.mNumMeshes();
        PointerBuffer meshPointerBuffer = scene.mMeshes();
        meshes = new Mesh[numMeshes];

        for(int i = 0; i < numMeshes; i++) {
            AIMesh mesh = AIMesh.create(meshPointerBuffer.get(i));
            // meshes[i] = process_mesh(mesh);
        }
    }

    private Mesh process_mesh() {
        System.out.println("Processin mesh in Assimp");
        
        return null;
    }
}
