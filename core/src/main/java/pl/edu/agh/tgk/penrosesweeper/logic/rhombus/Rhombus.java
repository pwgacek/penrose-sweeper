package pl.edu.agh.tgk.penrosesweeper.logic.rhombus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import pl.edu.agh.tgk.penrosesweeper.gui.font.NumbersFont;
import pl.edu.agh.tgk.penrosesweeper.gui.texture.ExplosionTexture;
import pl.edu.agh.tgk.penrosesweeper.gui.texture.FlagTexture;

public record Rhombus(Vector2 vA, Vector2 vB, Vector2 vC, Vector2 vD) {

    public Rhombus(Rhombus rhombus, float scale) {
        this(scaleVector(rhombus.vA, scale), scaleVector(rhombus.vB, scale), scaleVector(rhombus.vC, scale), scaleVector(rhombus.vD, scale));
    }

    private static Vector2 scaleVector(Vector2 vector, float scale) {
        return new Vector2(vector.x * scale, vector.y * scale);
    }

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

    public void renderValue(NumbersFont font, int value) {
        font.draw(getCenter(), value);
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

    public void renderMarked(FlagTexture flagTexture) {
        flagTexture.draw(getCenter());

    }

    public void renderExplosion(ShapeRenderer shapeRenderer, ExplosionTexture explosionTexture) {
        renderBorders(shapeRenderer);
        explosionTexture.draw(getCenter());

    }
}
