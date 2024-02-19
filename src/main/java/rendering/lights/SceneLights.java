package rendering.lights;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

public class SceneLights {
    private AmbientLight ambientLight;

    public SceneLights() {
        ambientLight = new AmbientLight();
    }

    public AmbientLight getAmbientLight() {
        return ambientLight;
    }

}
