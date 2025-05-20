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
import pl.edu.agh.tgk.penrosesweeper.logic.TimeUtil;
import pl.edu.agh.tgk.penrosesweeper.logic.board.BoardSize;
import pl.edu.agh.tgk.penrosesweeper.logic.perstistence.Leaderboard;
import pl.edu.agh.tgk.penrosesweeper.logic.perstistence.LeaderboardEntry;


public class LeaderboardScreen implements Screen  {

    private final Stage stage;
    private final Skin skin;

    private final Leaderboard leaderboard;
    private final Table listTable;

    public LeaderboardScreen(Game game) {
        this.leaderboard = Leaderboard.getInstance();
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        BitmapFont bigFont = FontGenerator.generate(30, Color.WHITE);
        BitmapFont smallFont = FontGenerator.generate(20, Color.WHITE);
        Label.LabelStyle bigLabelStyle = new Label.LabelStyle(bigFont, Color.WHITE);
        Label.LabelStyle smallLabelStyle = new Label.LabelStyle(smallFont, Color.WHITE);

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        tbs.font = bigFont;
        skin.add("default", tbs);


        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle(skin.get(SelectBox.SelectBoxStyle.class));
        selectBoxStyle.font = smallFont;
        List.ListStyle listStyle = new List.ListStyle(skin.get(List.ListStyle.class));
        listStyle.font = smallFont;
        selectBoxStyle.listStyle = listStyle;
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.DARK_GRAY);
        pixmap.fill();
        listStyle.selection = new Image(new Texture(pixmap)).getDrawable();
        pixmap.dispose();
        Label titleLabel = new Label("Leaderboard", bigLabelStyle);

        SelectBox<BoardSize> boardSizeSelect;
        boardSizeSelect = new SelectBox<>(selectBoxStyle);
        boardSizeSelect.setItems(BoardSize.values());
        boardSizeSelect.setSelected(BoardSize.SMALL);

        SelectBox<Difficulty> difficultySelect;
        difficultySelect = new SelectBox<>(selectBoxStyle);
        difficultySelect.setItems(Difficulty.values());
        difficultySelect.setSelected(Difficulty.EASY);

        listTable = new Table();
        listTable.defaults().pad(5);

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
            }
        });

        ChangeListener updateListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Difficulty difficulty = difficultySelect.getSelected();
                BoardSize boardSize = boardSizeSelect.getSelected();
                updateLeaderboard(difficulty, boardSize, smallLabelStyle);
            }
        };
        boardSizeSelect.addListener(updateListener);
        difficultySelect.addListener(updateListener);


        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.add(titleLabel).colspan(2).padBottom(40).center().row();

        Label boardSizeLabel = new Label("Board Size:", smallLabelStyle);
        table.add(boardSizeLabel).align(Align.left).padLeft(65).padRight(60);
        table.add(boardSizeSelect).width(150).align(Align.left).row();

        Label difficultyLabel = new Label("Difficulty:", smallLabelStyle);
        table.add(difficultyLabel).align(Align.left).padLeft(65).padRight(60);
        table.add(difficultySelect).width(150).align(Align.left).row();

        table.add(listTable).colspan(2).width(500).height(380).fill().padTop(30).row();

        table.add(backButton).colspan(2).center().width(380).height(70).padTop(30);

        updateLeaderboard(difficultySelect.getSelected(), boardSizeSelect.getSelected(), smallLabelStyle);
    }

    private void updateLeaderboard(Difficulty difficulty, BoardSize boardSize, Label.LabelStyle labelStyle) {

        listTable.clear();

        java.util.List<LeaderboardEntry> orderedEntries = leaderboard.getOrderedEntries(difficulty, boardSize);

        for(int i=0; i < Leaderboard.MAX_ENTRIES; i++) {

            listTable.row();

            Label posLabel = new Label(String.valueOf(i + 1), labelStyle);
            listTable.add(posLabel).width(50);

            String nick, time;
            if (i < orderedEntries.size()) {
                LeaderboardEntry entry = orderedEntries.get(i);
                nick = entry.nick();
                time = TimeUtil.formatTime(entry.time());
            } else {
                nick = "N/A";
                time = "N/A";
            }
            Label nickLabel = new Label(nick, labelStyle);
            listTable.add(nickLabel).width(150);
            nickLabel.setAlignment(Align.right);

            Label timeLabel = new Label(time, labelStyle);
            listTable.add(timeLabel).width(150);
            timeLabel.setAlignment(Align.right);

        }

    }

    @Override
    public void show() {}

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
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
