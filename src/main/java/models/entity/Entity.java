package models.entity;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Entity {
    private Vector3f position;
    private Quaternionf rotation;
    private Vector3f scale;

    public Entity() {
        position = new Vector3f(0.0f, 0.0f, 0.0f);
        rotation = new Quaternionf();
        scale = new Vector3f(1.0f, 1.0f, 1.0f);
    }

    // Update the entity's state (e.g., apply physics, handle user input)
    public void update() {
        // Implement logic to update the entity's state
    }

    // Get the transformation matrix of this entity
    public Matrix4f getTransformationMatrix() {
        return new Matrix4f().translate(position)
                .rotate(rotation)
                .scale(scale);
    }

    // Getters and setters for position, rotation, scale
    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public void setRotation(Quaternionf rotation) {
        this.rotation = rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }
}