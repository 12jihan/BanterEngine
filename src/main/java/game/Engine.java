package game;

import display.WindowManager;
import interfaces.GameLogic;

public class Engine {
    private final WindowManager window;
    private final GameLogic gameLogic;
    private String windowTitle;
    // private Render render;
    // private Scene scene;
    private boolean running;
    private int targetFps;
    private int targetUps;
    
    Engine(String windowTitle, WindowManager.WindowOptions opts, GameLogic gameLogic) throws Exception {
        this.windowTitle = windowTitle;
        System.out.println("window title: \n" + this.windowTitle);
        this.window = new WindowManager(this.windowTitle, opts, () -> {
            this.resize();
            return null;
        });
        this.gameLogic = gameLogic;
        gameLogic.init(window);
    }
    
    private void run() {
        long initialTime = System.currentTimeMillis();
        float timeU = 1000.0f / targetUps;
        float timeR = targetFps > 0 ? 1000.0f / targetFps : 0;
        float deltaUpdate = 0;
        float deltaFps = 0;
        long updateTime = initialTime;

        while (running && !window.windowShouldClose()) {
            window.pollEvents();

            long now = System.currentTimeMillis();
            deltaUpdate += (now - initialTime) / timeU;
            deltaFps += (now - initialTime) / timeR;

            // Put the input in here:
            if (targetFps <= 0 || deltaFps >= 1) {
            }

            // Put something here:
            if (deltaUpdate >= 1) {
                long diffTimeMillis = now - updateTime;
                gameLogic.update(window, diffTimeMillis);
                updateTime = now;
                deltaUpdate--;
            }

            // Put the renders in here:
            if (targetFps <= 0 || deltaFps >= 1) {
                deltaFps--;
                window.update();
            }
            initialTime = now;
        }
        cleanup();
    }

    public void start() {
        System.out.println("Engine started!");
        running = true;
        run();
    }

    private void cleanup() {
        window.cleanup();
    }

    private void resize() {
        System.out.println("Resizing window...");
    }

}
