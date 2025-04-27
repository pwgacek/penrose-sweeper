package pl.edu.agh.tgk.penrosesweeper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import pl.edu.agh.tgk.penrosesweeper.logic.Board;
import pl.edu.agh.tgk.penrosesweeper.logic.Tile;

import java.util.Optional;

public class GameScreen implements Screen {
    private static final int BOARD_SIZE = 1600;
    private final Board board;

    private ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;
    private BitmapFont font;
    private Texture explosionTexture;
    private Texture flagTexture;
    private OrthographicCamera camera;

    private boolean isBoardInitialized = false;
    private boolean isGameOver = false;
    public GameScreen() {
        board = new Board(BOARD_SIZE, 10, 5);
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Lato-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.genMipMaps = true;
        parameter.size = 32;
        parameter.color = Color.WHITE;
        parameter.magFilter = Texture.TextureFilter.MipMapLinearLinear;
        parameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;

        font = generator.generateFont(parameter);
        explosionTexture = new Texture(Gdx.files.internal("explosionv3.png"));
        flagTexture = new Texture(Gdx.files.internal("flag4.png"));
        generator.dispose();


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
            board.getTiles().stream().filter(it -> !it.isMine()).forEach(tile -> tile.render(shapeRenderer, spriteBatch, font, flagTexture ,!isGameOver && tile.equals(optionalTile.orElse(null))));
            board.getTiles().stream().filter(Tile::isMine).forEach(tile -> tile.renderGameOver(shapeRenderer ,spriteBatch, explosionTexture));
        } else {
            for (Tile tile : board.getTiles()) {
                tile.render(shapeRenderer, spriteBatch, font, flagTexture ,!isGameOver && tile.equals(optionalTile.orElse(null)));
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
            viewportHeight = BOARD_SIZE;
            aspectRatio = (float) width / height;
            viewportWidth = viewportHeight * aspectRatio;
        } else {
            viewportWidth = BOARD_SIZE;
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
