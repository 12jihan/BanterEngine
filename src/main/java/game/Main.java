package game;

import display.DisplaySettings;
import display.WindowManager;
import interfaces.GameLogic;

/**
 * Hello world!
 *
 */
public class Main implements GameLogic {

    // private static final float MOUSE_SENSITIVITY = 0.1f;
    // private static final float MOVEMENT_SPEED = 0.005f;
    // private float lightAngle;

    public static void main(String[] args) throws Exception {
        GameLoop gameEng = new GameLoop();
        gameEng.run();

    }

    @Override
    public void update(WindowManager window, long diffTimeMillis) {
        // TODO: nothing to put in here just yet.
        System.out.println("update in Main.java");
    }

    @Override
    public void cleanup() {
        // TODO nothing in here just yet but made for cleaning:
        System.out.println("cleanup in Main.java");
    }

    @Override
    public void init(WindowManager window) {
        // TODO: Will update init when I can:
        System.out.println("Initializeing in Main.java");
    }

    @Override
    public void input(WindowManager window) {
        // TODO Auto-generated method stub
        System.out.println("Input in Main.java");
    }
}