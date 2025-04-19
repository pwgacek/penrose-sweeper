package pl.edu.agh.tgk.penrosesweeper.logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public record Rhombus(Vector2 vA, Vector2 vB, Vector2 vC, Vector2 vD) {

    public boolean isPointInside(Vector2 point) {
        return sameSide(point, vA, vB,vC) &&
            sameSide(point, vB, vC, vD) &&
            sameSide(point, vC, vD, vA) &&
            sameSide(point, vD, vA, vB);
    }

    private static boolean sameSide(Vector2 p, Vector2 a, Vector2 b, Vector2 c) {
        Vector2 ab = new Vector2(b.x - a.x, b.y - a.y);
        Vector2 ap = new Vector2(p.x - a.x, p.y - a.y);
        Vector2 ac = new Vector2(c.x - a.x, c.y - a.y);

        float cross1 = ab.x * ap.y - ab.y * ap.x;
        float cross2 = ab.x * ac.y - ab.y * ac.x;

        return cross1 * cross2 >= 0;
    }

    private Vector2 getCenter() {
        float centerX = (vA.x + vB.x + vC.x + vD.x) / 4;
        float centerY = (vA.y + vB.y + vC.y + vD.y) / 4;

        return new Vector2(centerX, centerY);
    }

    public void renderValue(SpriteBatch spriteBatch, BitmapFont font, int value) {

        Vector2 center = getCenter();

        GlyphLayout layout = new GlyphLayout(font, String.valueOf(value));
        float textWidth = layout.width;
        float textHeight = layout.height;

        float xPos = center.x - textWidth / 2;
        float yPos = center.y + textHeight / 2;
        spriteBatch.begin();
        font.draw(spriteBatch, String.valueOf(value), xPos, yPos);
        spriteBatch.end();
    }

    public void renderBorders(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin();
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.polygon(
            new float[] {
                vA.x, vA.y,
                vB.x, vB.y,
                vC.x, vC.y,
                vD.x, vD.y,
            }
        );
        shapeRenderer.end();
    }

    public void renderFilled(ShapeRenderer shapeRenderer, Color color) {
        shapeRenderer.begin();
        shapeRenderer.setColor(color);
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.triangle(
            vA.x, vA.y,
            vB.x, vB.y,
            vC.x, vC.y
        );

        shapeRenderer.triangle(
            vA.x, vA.y,
            vD.x, vD.y,
            vC.x, vC.y
        );
        shapeRenderer.end();
    }

    public void renderMarked(SpriteBatch spriteBatch, Texture flagTexture) {
        Vector2 center = getCenter();
        spriteBatch.begin();
        spriteBatch.draw(flagTexture, center.x - flagTexture.getWidth() / 2f, center.y - flagTexture.getHeight() / 2f);
        spriteBatch.end();

    }

    public void renderExplosion(SpriteBatch spriteBatch, Texture explosionTexture) {
        Vector2 center = getCenter();
        spriteBatch.begin();
        spriteBatch.draw(explosionTexture, center.x - explosionTexture.getWidth() / 2f, center.y - explosionTexture.getHeight() / 2f);
        spriteBatch.end();

    }
}
