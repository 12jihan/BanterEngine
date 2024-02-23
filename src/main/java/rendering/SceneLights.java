package rendering;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import rendering.lights.AmbientLight;

public class SceneLights {
    private AmbientLight ambientLight;

    public SceneLights() {
        ambientLight = new AmbientLight();
    }

    public AmbientLight getAmbientLight() {
        return ambientLight;
    }

}
