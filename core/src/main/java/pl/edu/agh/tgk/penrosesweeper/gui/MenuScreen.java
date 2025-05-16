package pl.edu.agh.tgk.penrosesweeper.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import pl.edu.agh.tgk.penrosesweeper.gui.font.FontGenerator;

public class MenuScreen implements Screen {


    private final Stage stage;
    private final Skin skin;

    public MenuScreen(Game game) {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        tbs.font = FontGenerator.generate(30, Color.WHITE);
        skin.add("default", tbs);

        TextButton playButton = new TextButton("Play", skin);
        TextButton leaderboardButton = new TextButton("Leaderboard", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new BoardOptionsScreen(game));
            }
        });

        leaderboardButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Leaderboard button clicked");
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (exitButton.isPressed()) {
                    Gdx.app.exit();
                }
            }
        });

        table.row();
        table.add(playButton).width(300).height(70).pad(50);
        table.row();
        table.add(leaderboardButton).width(300).height(70).pad(50);
        table.row();
        table.add(exitButton).width(300).height(70).pad(50);
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
