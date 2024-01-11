package game;

import javax.annotation.processing.SupportedSourceVersion;

import interfaces.GameLogic;
import io.DisplaySettings;
import io.WindowManager;

/**
 * Hello world!
 *
 */
@SuppressWarnings("unused")
public class Main {
    public static void main(String[] args) throws Exception {
        Game engine = new Game();
        engine.run();
    }
}