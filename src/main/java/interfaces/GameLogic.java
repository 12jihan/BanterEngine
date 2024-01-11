package interfaces;

import io.WindowManager;

public interface GameLogic {
    // void init(Window window, Scene scene, Render render) throws Exception;

    // // void input(Window window, Scene scene, long diffTimeMillis, boolean inputConsumed) throws Exception;
    // void input(Window window, Scene scene, long diffTimeMillis, boolean inputConsumed) throws Exception;

    // // void update(Window window, Scene scene, long diffTimeMillis) throws Exception;
    // void update(Window window, Scene scene, long diffTimeMillis) throws Exception;

    void cleanup();

    void init(WindowManager window);

    void input(WindowManager window);

    void update(WindowManager window, long diffTimeMillis);

}
