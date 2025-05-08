package pl.edu.agh.tgk.penrosesweeper.gui.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import pl.edu.agh.tgk.penrosesweeper.gui.GameScreen;
import pl.edu.agh.tgk.penrosesweeper.gui.Main;

public class GameOverDialog extends Dialog {

    public GameOverDialog() {
        super("Game Over", new Skin(Gdx.files.internal("uiskin.json")));

        this.button("Restart", true);
        this.button("Exit", false);
        this.text("Game Over! You hit a mine.");
    }

    @Override
    protected void result(Object object) {
        if ((boolean) object) {
            restartGame();
        } else {
            Gdx.app.exit();
        }
    }

    public void showWithDelay(Stage stage, float delay) {
        Timer.schedule(new Timer.Task()
        {
            @Override
            public void run()
            {
                show(stage);
            }
        }, delay);
    }


    private void restartGame() {
        ((Main) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
    }
}
