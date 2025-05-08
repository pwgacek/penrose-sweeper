package pl.edu.agh.tgk.penrosesweeper.gui.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import pl.edu.agh.tgk.penrosesweeper.gui.GameScreen;
import pl.edu.agh.tgk.penrosesweeper.gui.Main;

public class GameWonDialog extends Dialog {

    public GameWonDialog() {
        super("Congratulations!", new Skin(Gdx.files.internal("uiskin.json")));

        this.button("Restart", true);
        this.button("Exit", false);
        this.setMovable(false);
    }

    @Override
    protected void result(Object object) {
        if ((boolean) object) {
            restartGame();
        } else {
            Gdx.app.exit();
        }
    }

    public void setTimeString(String timeString) {
        this.text("You cleared the board!\n Your time: " + timeString);
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
