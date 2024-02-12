package models.texture;

import java.util.*;

import models.mesh.Mesh;

public class Material {
    private List<Mesh> mesh_list;
    private String texture_path;

    public Material() {
        mesh_list = new ArrayList<>();
    }

    public void cleanup() {
        mesh_list.forEach(Mesh::clean);
    }

    public List<Mesh> getMeshList() {
        return mesh_list;
    }

    public String getTexturePath() {
        return texture_path;
    }

    public void setTexturePath(String texturePath) {
        this.texture_path = texturePath;
    }
}
