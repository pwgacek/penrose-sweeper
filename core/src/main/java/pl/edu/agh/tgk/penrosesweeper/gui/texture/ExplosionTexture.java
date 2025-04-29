package pl.edu.agh.tgk.penrosesweeper.gui.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import pl.edu.agh.tgk.penrosesweeper.logic.board.BoardSize;

public class ExplosionTexture extends Texture {
    private final float size;
    private final SpriteBatch spriteBatch;

    public ExplosionTexture(SpriteBatch spriteBatch, BoardSize boardSize) {
        super(Gdx.files.internal("explosionv3.png"));
        this.spriteBatch = spriteBatch;
        this.size = getSize(boardSize);
    }

    private static float getSize(BoardSize boardSize) {
        return switch (boardSize) {
            case SMALL -> 100f;
            case NORMAL -> 50f;
            case BIG -> 25f;
        };
    }


    public void draw(Vector2 position) {
        spriteBatch.begin();
        spriteBatch.draw(this, position.x - size / 2f, position.y - size / 2f, size, size);
        spriteBatch.end();
    }
}
