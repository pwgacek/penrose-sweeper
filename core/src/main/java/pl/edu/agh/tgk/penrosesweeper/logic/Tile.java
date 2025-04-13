package pl.edu.agh.tgk.penrosesweeper.logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Tile {
    private final Rhombus rhombus;
    private final int value;
    private final List<Tile> neighbours;

    private boolean isCovered = true;
    private boolean isMarkedAsMine = false;

    public Tile(Rhombus rhombus, int value) {
        this.rhombus = rhombus;
        this.value = value;
        neighbours = new ArrayList<>();
    }

    void addNeighbour(Tile tile) {
        this.neighbours.add(tile);
    }

    public Rhombus getRhombus() {
        return rhombus;
    }

    public boolean uncover() {
        if (isCovered && !isMarkedAsMine) {
            this.isCovered = false;
            if (value == 0) {
                neighbours.forEach(Tile::uncover);
            }
            return true;
        }

        return false;
    }

    public boolean isMine(){
        return value == -1;
    }

    public void toggleMarked() {
        if (isCovered) {
            isMarkedAsMine = !isMarkedAsMine;
        }
    }

    public boolean isPointInside(Vector2 point) {
        return this.rhombus.isPointInside(point);
    }

    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, BitmapFont font, Texture flagTexture) {

        if (isCovered) {
            this.rhombus.renderFilled(shapeRenderer, Color.DARK_GRAY);
        } else if (value > 0) {
            this.rhombus.renderValue(spriteBatch, font, value);
        }
        if (isMarkedAsMine) {
            this.rhombus.renderMarked(spriteBatch, flagTexture);
        }
        this.rhombus.renderBorders(shapeRenderer);

    }


}
