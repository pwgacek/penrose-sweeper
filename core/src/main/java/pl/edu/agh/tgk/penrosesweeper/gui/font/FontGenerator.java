package pl.edu.agh.tgk.penrosesweeper.gui.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontGenerator {
    private FontGenerator() {}
    public static BitmapFont generate(int size, Color color) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Lato-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.genMipMaps = true;
        parameter.size = size;
        parameter.color = color;
        parameter.magFilter = Texture.TextureFilter.MipMapLinearLinear;
        parameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }
}
