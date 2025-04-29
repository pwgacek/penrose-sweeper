package pl.edu.agh.tgk.penrosesweeper.gui.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import pl.edu.agh.tgk.penrosesweeper.logic.board.BoardSize;

public class NumbersFont extends BitmapFont {

    private final SpriteBatch spriteBatch;
    private final BitmapFont font;

    public NumbersFont(SpriteBatch spriteBatch, BoardSize boardSize) {
        this.spriteBatch = spriteBatch;
        this.font = generateFont(boardSize);
    }

    public BitmapFont generateFont(BoardSize boardSize) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Lato-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.genMipMaps = true;
        parameter.size = getFontSize(boardSize);
        parameter.color = Color.WHITE;
        parameter.magFilter = Texture.TextureFilter.MipMapLinearLinear;
        parameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    private int getFontSize(BoardSize boardSize) {
        return switch (boardSize) {
            case SMALL -> 48;
            case NORMAL -> 32;
            case BIG -> 16;
        };
    }

    public void draw(Vector2 position, int value) {
        GlyphLayout layout = new GlyphLayout(font, String.valueOf(value));
        float textWidth = layout.width;
        float textHeight = layout.height;

        float xPos = position.x - textWidth / 2;
        float yPos = position.y + textHeight / 2;

        spriteBatch.begin();
        font.draw(spriteBatch, String.valueOf(value), xPos, yPos);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
    }
}
