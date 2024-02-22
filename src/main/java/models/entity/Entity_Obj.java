package models.entity;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Entity_Obj {
    private Entity entity;
    private Vector3f position;
    private Quaternionf rotation;
    private float scale;

    public Entity_Obj(Entity entity) {
        this.entity = entity;
        this.position = entity.getPosition();
        this.rotation = entity.getRotation();
        this.scale = entity.getScale();
    }

    public Entity get_entity() {
        return entity;
    }

    public float[] get_position() {
        return new float[] { position.x, position.y, position.z };
    }

    public float[] get_rotation() {
        return new float[] { rotation.x, rotation.y, rotation.z };
    }

    public float[] getScale() {
        return new float[] { scale };
    }

}
