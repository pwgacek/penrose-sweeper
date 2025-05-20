package pl.edu.agh.tgk.penrosesweeper.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import pl.edu.agh.tgk.penrosesweeper.gui.dialog.GameOverDialog;
import pl.edu.agh.tgk.penrosesweeper.gui.dialog.GameWonDialog;
import pl.edu.agh.tgk.penrosesweeper.gui.font.NumbersFont;
import pl.edu.agh.tgk.penrosesweeper.gui.horizontalgroup.FlagsLeftHorizontalGroup;
import pl.edu.agh.tgk.penrosesweeper.gui.label.TimerLabel;
import pl.edu.agh.tgk.penrosesweeper.gui.texture.ExplosionTexture;
import pl.edu.agh.tgk.penrosesweeper.gui.texture.FlagTexture;
import pl.edu.agh.tgk.penrosesweeper.logic.Difficulty;
import pl.edu.agh.tgk.penrosesweeper.logic.GameplayPhase;
import pl.edu.agh.tgk.penrosesweeper.logic.board.Board;
import pl.edu.agh.tgk.penrosesweeper.logic.board.BoardSize;
import pl.edu.agh.tgk.penrosesweeper.logic.board.Tile;
import pl.edu.agh.tgk.penrosesweeper.logic.perstistence.Leaderboard;
import pl.edu.agh.tgk.penrosesweeper.logic.perstistence.LeaderboardEntry;

import java.util.Optional;

import static pl.edu.agh.tgk.penrosesweeper.logic.GameplayPhase.*;

public class BoardScreen implements Screen {
    private static final int SCREEN_SIZE = 1600;
    private final Difficulty difficulty;
    private final BoardSize boardSize;
    private final String nick;
    private final Board board;
    private final Leaderboard leaderboard;
    private GameplayPhase phase = NOT_STARTED;
    private final Game game;
    private Stage mainStage;
    private Stage boardStage;
    private GameOverDialog gameOverDialog;
    private GameWonDialog gameWonDialog;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;
    private NumbersFont font;
    private ExplosionTexture explosionTexture;
    private FlagTexture flagTexture;
    private TimerLabel timerLabel;
    private FlagsLeftHorizontalGroup flagsLeftHorizontalGroup;
    private long timeStart;
    private OrthographicCamera camera;

    public BoardScreen(Game game, Difficulty difficulty, BoardSize boardSize, String nick) {
        this.game = game;
        this.difficulty = difficulty;
        this.boardSize = boardSize;
        this.board = new Board(SCREEN_SIZE, difficulty, boardSize);
        this.nick = nick;
        this.leaderboard = Leaderboard.getInstance();
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        boardStage = new Stage(new FitViewport(SCREEN_SIZE, SCREEN_SIZE, camera));
        mainStage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(mainStage);

        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();

        font = new NumbersFont(spriteBatch, boardSize);
        explosionTexture = new ExplosionTexture(spriteBatch, boardSize);
        flagTexture = new FlagTexture(spriteBatch, boardSize);
        timerLabel = new TimerLabel(mainStage);
        flagsLeftHorizontalGroup = new FlagsLeftHorizontalGroup(mainStage, spriteBatch, board.getFlagsLeft());
        gameOverDialog = new GameOverDialog(game);
        gameWonDialog = new GameWonDialog(game);
    }



    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, 1);

        boardStage.act(delta);
        boardStage.getViewport().apply();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setAutoShapeType(true);
        spriteBatch.setProjectionMatrix(camera.combined);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        Optional<Tile> optionalTile = board.getTile(getMouseCoordinates());

        switch(phase) {
            case NOT_STARTED -> {
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    optionalTile.ifPresent(tile -> {
                        board.initialize(tile);
                        phase = RUNNING;
                        tile.uncover();
                        timeStart = System.currentTimeMillis();
                    });
                }
            } case RUNNING -> {
                updateTimer();
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    optionalTile.ifPresent(tile -> {
                        if (tile.uncover()) {
                            if (tile.isMine()) {
                                phase = ENDED;
                                board.explodeAllMines();
                                gameOverDialog.showWithDelay(mainStage, 0.5f);
                            } else if (board.areAllNonMinesUncovered()) {
                                phase = ENDED;
                                board.markAllMines();
                                leaderboard.addEntry(difficulty, boardSize, new LeaderboardEntry(nick, timerLabel.getTime()));
                                flagsLeftHorizontalGroup.setValue(board.getFlagsLeft());
                                gameWonDialog.setTimeString(timerLabel.getText().toString());
                                gameWonDialog.showWithDelay(mainStage, 0.5f);
                            }
                        }
                    });
                }

                else if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                    board.getTile(getMouseCoordinates()).ifPresent(it -> {
                        if (it.isCovered() && board.toggleFlagMarked(it)) {
                            flagsLeftHorizontalGroup.setValue(board.getFlagsLeft());
                        }
                    });
                }
            }
        }
        renderBoard(optionalTile.orElse(null));

        mainStage.getViewport().apply();
        mainStage.draw();
    }

    private void renderBoard(Tile optionalTile) {
        board.getTiles().stream()
            .sorted((tile1, tile2) -> Boolean.compare(tile1.isMine(), tile2.isMine()))
            .forEach(it -> it.render(shapeRenderer, font, flagTexture, explosionTexture, phase != ENDED && it.equals(optionalTile)));


    }


    private void updateTimer() {
        long elapsedTime = System.currentTimeMillis() - timeStart;
        timerLabel.setTime(elapsedTime);
    }

    private Vector2 getMouseCoordinates() {
        Vector2 screenCords = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        return boardStage.getViewport().unproject(screenCords);
    }

    @Override
    public void resize(int width, int height) {
        boardStage.getViewport().update(width, height, true);
        mainStage.getViewport().update(width, height, true);
        mainStage.getViewport().setScreenPosition(0, 0);

        gameOverDialog.setPosition((width - gameOverDialog.getWidth())/ 2f  , (height - gameOverDialog.getHeight()) / 2f);
        gameWonDialog.setPosition((width - gameWonDialog.getWidth())/ 2f  , (height - gameWonDialog.getHeight()) / 2f);

        timerLabel.setPosition();
        flagsLeftHorizontalGroup.setPosition();

        camera.position.set(0, 0, 0);
        camera.update();
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        boardStage.dispose();
        mainStage.dispose();
        font.dispose();
        spriteBatch.dispose();
        shapeRenderer.dispose();
        explosionTexture.dispose();
        flagTexture.dispose();
    }
}
