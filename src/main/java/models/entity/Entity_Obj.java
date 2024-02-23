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

    public float[][] get_rotation() {
        float[] main = new float[] { rotation.x, rotation.y, rotation.z, entity.getAngle() };
        // [0] = x, [1] = y, [2] = z, [3] = angle:
        float[][] returnable = new float[][] {
            new float[] { main[0] },
            new float[] { main[1] },
            new float[] { main[2] },
            new float[] { main[3] },
        };
        return returnable;
    }

    public float[] get_scale() {
        return new float[] { scale };
    }

}
