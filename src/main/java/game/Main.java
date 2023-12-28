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
        GameLoop gameEng = new GameLoop();
        gameEng.run();

    }
}