package pl.edu.agh.tgk.penrosesweeper.gui.label;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;


public class TimerLabel extends Label {

    public TimerLabel(Stage stage) {
        super("00:000", new LabelStyle(generateFont(), com.badlogic.gdx.graphics.Color.WHITE));
        setAlignment(Align.topLeft);

        setPosition(0,stage.getViewport().getWorldHeight() - getHeight());
        stage.addActor(this);
    }

    private static BitmapFont generateFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Lato-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.genMipMaps = true;
        parameter.size = 32;
        parameter.color = com.badlogic.gdx.graphics.Color.WHITE;
        parameter.magFilter = Texture.TextureFilter.MipMapLinearLinear;
        parameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
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
}
