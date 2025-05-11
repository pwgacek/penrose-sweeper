package pl.edu.agh.tgk.penrosesweeper.gui.horizontalgroup;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import pl.edu.agh.tgk.penrosesweeper.gui.font.FontGenerator;
import pl.edu.agh.tgk.penrosesweeper.gui.texture.FlagTexture;

public class FlagsLeftHorizontalGroup extends HorizontalGroup {

    private final Stage stage;
    private final Label label;
    private int flagsLeftCounter;

    public FlagsLeftHorizontalGroup(Stage stage, SpriteBatch spriteBatch, int bombCounter) {
        int size = 32;
        this.label = new Label(String.valueOf(bombCounter), new Label.LabelStyle(FontGenerator.generate(size, Color.WHITE), Color.WHITE));
        this.stage = stage;
        this.flagsLeftCounter = bombCounter;


        addActor(new Image(new FlagTexture(spriteBatch, size)));
        addActor(label);

        padRight(10);
        pack();
        setHeight(label.getHeight());


        stage.addActor(this);
    }

    public void setValue(int value) {
        flagsLeftCounter = value;
        label.setText(String.format("%02d", flagsLeftCounter));
    }

    public void setPosition() {
        setPosition(stage.getViewport().getWorldWidth() - getWidth(),stage.getViewport().getWorldHeight() - getHeight());
    }


}
