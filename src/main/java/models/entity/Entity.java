package models.entity;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

@SuppressWarnings("unused")
public class Entity {
    // identification for entity id and model id
    private final String entity_id;
    // model positioning
    private Matrix4f model_matrix;
    // positioning in the world
    private Vector3f position;
    private Quaternionf rotation;
    private float angle;
    private float scale;

    RawModel model;

    public Entity(String id) {
        // set the id of the entity
        entity_id = id;
        // set transformation of the entity
        position = new Vector3f();
        rotation = new Quaternionf();
        model_matrix = new Matrix4f();
        angle = 0.0f;
        scale = 1;
    }

    // Update the entity's state (e.g., apply physics, handle user input)

    // Get the transformation matrix of this entity
    public Matrix4f getTransformationMatrix() {
        return new Matrix4f().translationRotateScale(position, rotation, scale);
    }

    // Getters and setters
    public String getId() {
        return entity_id;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setRotation(float x, float y, float z, float angle) {
        this.angle = angle;
        this.rotation.fromAxisAngleRad(x, y, z, this.angle);
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getAngle() {
        return angle;
    }

    public void addModel(RawModel model) {
        this.model = model;
    }

    public RawModel getModel() {
        return model;
    }

    public Matrix4f getModelMatrix() {
        return model_matrix;
    }

    public void updateModelMatrix() {
        model_matrix.translationRotateScale(this.position, this.rotation, this.scale);
    }
}