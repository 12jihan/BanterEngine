package display;

public class DisplaySettings {
    private static final boolean SYSCHECK = System.getProperty("os.name").contains("Mac");
    public int width, height;
    public int fps;
    public int target_ups;
    public boolean compatibleProfile;
    public boolean antiAliasing;
    public boolean vsync;

    public DisplaySettings() {
        this.width = 1280;
        this.height = 720;
        this.fps = 60;
        this.target_ups = 60;
        this.compatibleProfile = true;
        this.antiAliasing = false;
        this.vsync = false;
    }

    public boolean getSyscheck() {
        return SYSCHECK;
    }
}
