package pl.edu.agh.tgk.penrosesweeper.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import pl.edu.agh.tgk.penrosesweeper.gui.font.NumbersFont;
import pl.edu.agh.tgk.penrosesweeper.gui.label.TimerLabel;
import pl.edu.agh.tgk.penrosesweeper.gui.texture.ExplosionTexture;
import pl.edu.agh.tgk.penrosesweeper.gui.texture.FlagTexture;
import pl.edu.agh.tgk.penrosesweeper.logic.Difficulty;
import pl.edu.agh.tgk.penrosesweeper.logic.board.Board;
import pl.edu.agh.tgk.penrosesweeper.logic.board.BoardSize;
import pl.edu.agh.tgk.penrosesweeper.logic.board.Tile;

import java.util.Optional;

public class GameScreen implements Screen {
    private static final int SCREEN_SIZE = 1600;
    private static final BoardSize BOARD_SIZE = BoardSize.BIG;
    private static final Difficulty DIFFICULTY = Difficulty.EASY;
    private final Board board;

    private Stage stage;
    private Stage timerStage;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;
    private NumbersFont font;
    private ExplosionTexture explosionTexture;
    private FlagTexture flagTexture;
    private TimerLabel timerLabel;
    private long timeStart;
    private OrthographicCamera camera;

    private boolean isBoardInitialized = false;
    private boolean isGameOver = false;

    public GameScreen() {
        board = new Board(SCREEN_SIZE, DIFFICULTY, BOARD_SIZE);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        stage = new Stage(new FitViewport(SCREEN_SIZE, SCREEN_SIZE, camera));
        timerStage = new Stage(new ScreenViewport());
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();

        font = new NumbersFont(spriteBatch, BOARD_SIZE);
        explosionTexture = new ExplosionTexture(spriteBatch, BOARD_SIZE);
        flagTexture = new FlagTexture(spriteBatch, BOARD_SIZE);
        timerLabel = new TimerLabel(timerStage);
    }



    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        timerStage.act(delta);
        timerStage.getViewport().apply();
        timerStage.draw();

        stage.getViewport().apply();

        shapeRenderer.setProjectionMatrix(camera.combined);
        spriteBatch.setProjectionMatrix(camera.combined);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        Vector2 coordinates = getMouseCoordinates();
        Optional<Tile> optionalTile = board.getTile(coordinates);


        if (!isBoardInitialized) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                optionalTile.ifPresent(tile -> {
                    board.initialize(tile);
                    isBoardInitialized = true;
                    tile.uncover();
                    timeStart = System.currentTimeMillis();
                });
            }
        }
        else if (!isGameOver) {
            updateTimer();
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                 optionalTile.ifPresent(tile -> {
                    if (tile.uncover()) {
                        if (tile.isMine()) {
                            isGameOver = true;
                        }
                    }
                });
            }

            if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                board.getTile(getMouseCoordinates()).ifPresent(Tile::toggleMarked);
            }
        }
        shapeRenderer.setAutoShapeType(true);

        if (isGameOver) {
            board.getTiles().stream().filter(it -> !it.isMine()).forEach(tile -> tile.render(shapeRenderer, font, flagTexture ,!isGameOver && tile.equals(optionalTile.orElse(null))));
            board.getTiles().stream().filter(Tile::isMine).forEach(tile -> tile.renderGameOver(shapeRenderer, explosionTexture));
        } else {
            for (Tile tile : board.getTiles()) {
                tile.render(shapeRenderer, font, flagTexture ,!isGameOver && tile.equals(optionalTile.orElse(null)));
            }
        }

    }

    private void updateTimer() {
        long elapsedTime = System.currentTimeMillis() - timeStart;
        timerLabel.setTime(elapsedTime);
    }

    private Vector2 getMouseCoordinates() {
        Vector2 screenCords = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        return stage.getViewport().unproject(screenCords);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        timerStage.getViewport().update(width, height, true);
        timerStage.getViewport().setScreenPosition(0, 0);
        timerLabel.setPosition(0, timerStage.getViewport().getWorldHeight() - timerLabel.getHeight());

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
        stage.dispose();
        timerStage.dispose();
        font.dispose();
        spriteBatch.dispose();
        shapeRenderer.dispose();
        explosionTexture.dispose();
        flagTexture.dispose();
    }
}
