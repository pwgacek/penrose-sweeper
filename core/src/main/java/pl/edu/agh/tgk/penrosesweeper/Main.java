package pl.edu.agh.tgk.penrosesweeper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    @Override
    public void create() {
        // Get the display width and height
        int height = Gdx.graphics.getDisplayMode().height;

        // Set the windowed mode to fullscreen size (borderless)
        Gdx.graphics.setWindowedMode(height, height);
        setScreen(new FirstScreen());
    }
}
