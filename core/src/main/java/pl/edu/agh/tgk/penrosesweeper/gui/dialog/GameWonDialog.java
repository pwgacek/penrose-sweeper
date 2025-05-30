package pl.edu.agh.tgk.penrosesweeper.gui.dialog;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;


public class GameWonDialog extends MyDialog {

    public GameWonDialog(Game game) {
        super(game, "Congratulations!");

        this.button("Go to menu", true);
        this.button("Exit", false);
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

}
