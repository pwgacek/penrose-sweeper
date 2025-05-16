package pl.edu.agh.tgk.penrosesweeper.gui.dialog;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import pl.edu.agh.tgk.penrosesweeper.gui.MenuScreen;
import pl.edu.agh.tgk.penrosesweeper.gui.font.FontGenerator;

public abstract class MyDialog extends Dialog {


    private final float dialogWidth = 260f;
    private final float dialogHeight = 180f;
    private final float dialogPadding = 30f;
    private final float buttonPaddding = 15f;

    private static final Skin skin;
    private final Game game;
    static {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        Window.WindowStyle ws = new Window.WindowStyle(skin.get(Window.WindowStyle.class));
        ws.titleFont = FontGenerator.generate(16, Color.WHITE);
        skin.add("default", ws, Window.WindowStyle.class);
    }

    public MyDialog(Game game, String title) {
        super(title, skin);
        this.game = game;
        this.setup();
    }

    private void setup() {
        getTitleLabel().setAlignment(Align.left);
        padBottom(dialogPadding);
        setModal(false);
        setMovable(true);
        setResizable(false);

    }

    @Override
    public Dialog button(String text, Object object){
        Skin skin = getSkin();
        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        tbs.font = FontGenerator.generate(20, Color.WHITE);
        skin.add("default", tbs);

        TextButton textButton = new TextButton(text, skin);
        textButton.padLeft(buttonPaddding);
        textButton.padRight(buttonPaddding);

        super.button(textButton, object);
        return this;
    }

    @Override
    public MyDialog text(String text) {

        Label label = new Label(text, new Label.LabelStyle(FontGenerator.generate(24, Color.WHITE), Color.WHITE));
        label.setAlignment(Align.center);
        label.setWidth(dialogWidth - dialogPadding * 2);
        text(label);
        return this;
    }

    @Override
    protected void result(Object object) {
        if ((boolean) object) {
            goToMenu();
        } else {
            Gdx.app.exit();
        }
    }

    private void goToMenu() {
        game.setScreen(new MenuScreen(game));
    }

    @Override
    public float getPrefWidth() {
        return dialogWidth;
    }

    @Override
    public float getPrefHeight() {
        return dialogHeight;
    }
}
