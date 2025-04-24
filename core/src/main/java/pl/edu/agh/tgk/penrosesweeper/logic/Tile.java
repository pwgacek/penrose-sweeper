package pl.edu.agh.tgk.penrosesweeper.logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;
import java.util.Set;

public class Tile {
    private final Rhombus rhombus;
    private int value;
    private final Set<Tile> neighbours;

    private boolean isCovered = true;
    private boolean isMarkedAsMine = false;

    public Tile(Rhombus rhombus) {
        this.rhombus = rhombus;
        neighbours = new HashSet<>();
        this.value = 0;
    }

    void setMine() {
        this.value = -1;
    }

    void increaseValue() {
        this.value++;
    }

    void addNeighbour(Tile tile) {
        this.neighbours.add(tile);
    }

    public Set<Tile> getNeighbours() {
        return neighbours;
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

    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, BitmapFont font, Texture flagTexture, boolean isHovered) {

        if (isCovered) {
            this.rhombus.renderFilled(shapeRenderer, isHovered ? Color.GRAY : Color.DARK_GRAY);
        } else if (value > 0) {
            this.rhombus.renderValue(spriteBatch, font, value);
        }
        if (isMarkedAsMine) {
            this.rhombus.renderMarked(spriteBatch, flagTexture);
        }
        this.rhombus.renderBorders(shapeRenderer);

    }

    public void renderGameOver(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, Texture explosionTexture) {
            this.rhombus.renderExplosion(shapeRenderer, spriteBatch, explosionTexture);
    }


}
