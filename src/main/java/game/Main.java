package game;

import display.DisplaySettings;
import display.WindowManager;
import interfaces.GameLogic;

/**
 * Hello world!
 *
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Game engine = new Game();
        engine.run();
    }
}