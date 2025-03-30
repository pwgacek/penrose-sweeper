package pl.edu.agh.tgk.penrosesweeper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Rhombus {
    private final Vector2 vA, vB, vC, vD;
    private boolean selected = false;


    public Rhombus(Vector2 vA, Vector2 vB, Vector2 vC, Vector2 vD) {
        this.vA = vA;
        this.vB = vB;
        this.vC = vC;
        this.vD = vD;
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

    public void click() {
        selected = !selected;
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (selected) {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
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
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
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
}
