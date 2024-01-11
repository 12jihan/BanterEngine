package models.entity;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class Entity {
    private Matrix4f modelMatrix;
    private Vector3f position;
    private Quaternionf rotation;
}
