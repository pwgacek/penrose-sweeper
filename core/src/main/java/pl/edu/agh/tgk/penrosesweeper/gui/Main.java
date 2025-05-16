package pl.edu.agh.tgk.penrosesweeper.gui;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }
    @Override
    public void render () {
        super.render();
    }

    @Override
    public void dispose () {
    }
}
