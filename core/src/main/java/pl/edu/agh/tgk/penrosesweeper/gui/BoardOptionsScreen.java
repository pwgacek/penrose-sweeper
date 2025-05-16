package pl.edu.agh.tgk.penrosesweeper.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import pl.edu.agh.tgk.penrosesweeper.gui.font.FontGenerator;
import pl.edu.agh.tgk.penrosesweeper.logic.Difficulty;
import pl.edu.agh.tgk.penrosesweeper.logic.board.BoardSize;

public class BoardOptionsScreen implements Screen {

    private final Stage stage;
    private final Skin skin;

    public BoardOptionsScreen(Game game) {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.defaults().padTop(50).padBottom(50).padLeft(0).padRight(0);
        table.setFillParent(true);
        stage.addActor(table);

        BitmapFont font = FontGenerator.generate(30, Color.WHITE);

        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        tbs.font = font;
        skin.add("default", tbs);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(skin.get(TextField.TextFieldStyle.class));
        textFieldStyle.font = font;

        Label nameLabel = new Label("Nick:", labelStyle);
        TextField nameInput = new TextField("", textFieldStyle);
        nameInput.setAlignment(Align.center);
        nameInput.setText("player1");



        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle(skin.get(SelectBox.SelectBoxStyle.class));
        selectBoxStyle.font = font;
        List.ListStyle listStyle = new List.ListStyle(skin.get(List.ListStyle.class));
        listStyle.font = font;
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.DARK_GRAY);
        pixmap.fill();
        listStyle.selection = new Image(new Texture(pixmap)).getDrawable();
        pixmap.dispose();
        selectBoxStyle.listStyle = listStyle;

        Label boardSizeLabel = new Label("Board Size:", labelStyle);
        SelectBox<BoardSize> boardSizeSelect = new SelectBox<>(selectBoxStyle);
        boardSizeSelect.setAlignment(Align.center);
        boardSizeSelect.setItems(BoardSize.values());
        boardSizeSelect.setSelected(BoardSize.SMALL);

        Label difficultyLabel = new Label("Difficulty:", labelStyle);
        SelectBox<Difficulty> difficultySelect = new SelectBox<>(selectBoxStyle);
        difficultySelect.setAlignment(Align.center);
        difficultySelect.setItems(Difficulty.values());
        difficultySelect.setSelected(Difficulty.EASY);

        TextButton playButton = new TextButton("Play", skin);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String playerName = nameInput.getText();
                BoardSize boardSize = boardSizeSelect.getSelected();
                Difficulty difficulty = difficultySelect.getSelected();

                game.setScreen(new BoardScreen(game, boardSize, difficulty));
            }
        });

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
            }
        });

        nameInput.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playButton.setDisabled(nameInput.getText().trim().isEmpty());
            }
        });

        table.row();
        table.add(nameLabel).padRight(50).align(Align.left);
        table.add(nameInput).width(220);

        table.row();
        table.add(boardSizeLabel).padRight(50).align(Align.left);
        table.add(boardSizeSelect).width(220);

        table.row();
        table.add(difficultyLabel).padRight(50).align(Align.left);
        table.add(difficultySelect).width(220);

        table.row();
        table.defaults().uniformX();
        Table buttonTable = new Table();
        buttonTable.row();

        buttonTable.add(playButton).width(270).height(70).align(Align.left).padRight(40);
        buttonTable.add(backButton).width(120).height(70).align(Align.right);
        table.add(buttonTable).colspan(2).height(70).align(Align.left);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(Color.DARK_GRAY.r, Color.DARK_GRAY.g, Color.DARK_GRAY.b, 1);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
