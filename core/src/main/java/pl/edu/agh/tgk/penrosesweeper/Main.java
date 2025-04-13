package pl.edu.agh.tgk.penrosesweeper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    @Override
    public void create() {
        int height = Gdx.graphics.getDisplayMode().height;

        Gdx.graphics.setWindowedMode(height, height);
        setScreen(new GameScreen());
    }
}
