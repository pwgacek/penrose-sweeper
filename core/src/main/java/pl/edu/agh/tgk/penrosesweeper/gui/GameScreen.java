package pl.edu.agh.tgk.penrosesweeper.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import pl.edu.agh.tgk.penrosesweeper.gui.font.NumbersFont;
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

    private ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;
    private NumbersFont font;
    private ExplosionTexture explosionTexture;
    private FlagTexture flagTexture;
    private OrthographicCamera camera;

    private boolean isBoardInitialized = false;
    private boolean isGameOver = false;

    public GameScreen() {
        board = new Board(SCREEN_SIZE, DIFFICULTY, BOARD_SIZE);
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();

        font = new NumbersFont(spriteBatch, BOARD_SIZE);
        explosionTexture = new ExplosionTexture(spriteBatch, BOARD_SIZE);
        flagTexture = new FlagTexture(spriteBatch, BOARD_SIZE);

        camera = new OrthographicCamera();
    }



    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
                });
            }
        }
        else if (!isGameOver) {
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
        spriteBatch.begin();
        spriteBatch.end();

    }

    private Vector2 getMouseCoordinates() {
        float screenX = Gdx.input.getX();
        float screenY = Gdx.input.getY();
        Vector3 unprojected = camera.unproject(new Vector3(screenX, screenY, 0f));
        return new Vector2(unprojected.x, unprojected.y);
    }

    @Override
    public void resize(int width, int height) {
        float viewportHeight, viewportWidth, aspectRatio;
        if ( width > height) {
            viewportHeight = SCREEN_SIZE;
            aspectRatio = (float) width / height;
            viewportWidth = viewportHeight * aspectRatio;
        } else {
            viewportWidth = SCREEN_SIZE;
            aspectRatio = (float) height / width;
            viewportHeight = viewportWidth * aspectRatio;
        }
        camera.setToOrtho(false, viewportWidth, viewportHeight);
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
        font.dispose();
        spriteBatch.dispose();
        shapeRenderer.dispose();
        explosionTexture.dispose();
        flagTexture.dispose();
    }
}
