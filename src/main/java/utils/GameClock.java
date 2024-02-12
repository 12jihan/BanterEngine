package utils;

@SuppressWarnings("unused")
public class GameClock {
    private long lastUpdateTime;
    private long lastFrameTime;
    private double deltaTime;
    private final double nsPerUpdate;
    private final double nsPerFrame;
    private int updates;
    private int frameCount;
    private int fps;
    private long lastFpsTime;


    public GameClock(int targetFPS, int targetUPS) {
        lastUpdateTime = System.nanoTime();
        lastFrameTime = lastUpdateTime;
        nsPerUpdate = 1000000000.0 / targetUPS;
        nsPerFrame = 1000000000.0 / targetFPS;
        frameCount= 0;
        updates = 0;
        lastFpsTime = System.currentTimeMillis();
    }

    public void update() {
        long currentTime = System.nanoTime();
        deltaTime = (currentTime - lastUpdateTime) / nsPerUpdate;
        lastUpdateTime = currentTime;

        if (System.currentTimeMillis() - lastFpsTime > 1000) {
            fps = frameCount;
            frameCount = 0;
            lastFpsTime += 1000;
        }
    }

    public boolean shouldUpdate() {
        return deltaTime >= 1;
    }

    public void shouldRender() {
    }

    public void afterRender() {
    }

    public void afterUpdate() {
    }

    // Optionally, methods to get FPS and UPS
    public int getFPS() {
        return fps;
    }

    public int getUPS() {
        return updates;
    }
}
