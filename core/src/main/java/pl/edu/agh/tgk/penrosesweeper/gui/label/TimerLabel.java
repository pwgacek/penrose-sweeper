package pl.edu.agh.tgk.penrosesweeper.gui.label;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import pl.edu.agh.tgk.penrosesweeper.gui.font.FontGenerator;
import pl.edu.agh.tgk.penrosesweeper.logic.TimeUtil;


public class TimerLabel extends Label {
    private final Stage stage;
    private long time;

    public TimerLabel(Stage stage) {
        super("00:000", new LabelStyle(FontGenerator.generate(32, Color.WHITE), Color.WHITE));
        this.stage = stage;
        setAlignment(Align.topLeft);
        stage.addActor(this);
    }

    public void setTime(long time) {
        this.time = time;
        setText(TimeUtil.formatTime(time));
    }

    public long getTime() {
        return time;
    }

    public void setPosition() {
        setPosition(10,stage.getViewport().getWorldHeight() - getHeight());
    }
}
