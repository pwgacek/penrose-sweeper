package pl.edu.agh.tgk.penrosesweeper.gui.dialog;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;


public class GameOverDialog extends MyDialog {

    public GameOverDialog(Game game) {
        super(game, "Game Over");
        this.button("Go to menu", true);
        this.button("Exit", false);
        this.text("You hit a mine!");
    }





    public void showWithDelay(Stage stage, float delay) {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                show(stage);
            }
        }, delay);
    }
}
