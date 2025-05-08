package pl.edu.agh.tgk.penrosesweeper.gui.label;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import pl.edu.agh.tgk.penrosesweeper.gui.font.FontGenerator;


public class TimerLabel extends Label {
    private final Stage stage;

    public TimerLabel(Stage stage) {
        super("00:000", new LabelStyle(FontGenerator.generate(32, Color.WHITE), Color.WHITE));
        this.stage = stage;
        setAlignment(Align.topLeft);
        stage.addActor(this);
    }

    public void setTime(long time) {
        long hours = (time / 1000) / 3600;
        long minutes = ((time / 1000) % 3600) / 60;
        long seconds = (time / 1000) % 60;
        long milliseconds = time % 1000;

        String timeText;
        if (hours > 0) {
            timeText = String.format("%d:%02d:%02d:%03d", hours, minutes, seconds, milliseconds);
        } else if (minutes > 0) {
            timeText = String.format("%02d:%02d:%03d", minutes, seconds, milliseconds);
        } else {
            timeText = String.format("%02d:%03d", seconds, milliseconds);
        }

        setText(timeText);
    }

    public void setPosition() {
        setPosition(10,stage.getViewport().getWorldHeight() - getHeight());
    }
}
