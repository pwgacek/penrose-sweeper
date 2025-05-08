package pl.edu.agh.tgk.penrosesweeper.logic.board;

import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import pl.edu.agh.tgk.penrosesweeper.gui.font.NumbersFont;
import pl.edu.agh.tgk.penrosesweeper.gui.texture.ExplosionTexture;
import pl.edu.agh.tgk.penrosesweeper.gui.texture.FlagTexture;
import pl.edu.agh.tgk.penrosesweeper.logic.rhombus.Rhombus;

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

    public boolean isCovered() {
        return isCovered;
    }

    public boolean isMarkedAsMine() {
        return isMarkedAsMine;
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

    public void render(ShapeRenderer shapeRenderer, NumbersFont font, FlagTexture flagTexture, ExplosionTexture explosionTexture, boolean isHovered) {
        if (isMarkedAsMine) {
            this.rhombus.renderMarked(flagTexture, shapeRenderer, isHovered ? Color.GRAY : Color.DARK_GRAY);
        } else if (isCovered) {
            this.rhombus.renderCovered(shapeRenderer, isHovered ? Color.GRAY : Color.DARK_GRAY);
        } else {
            if (value > 0) {
                this.rhombus.renderValue(shapeRenderer, font, value);
            } else if (value == 0) {
                this.rhombus.renderBorders(shapeRenderer);
            } else {
                this.rhombus.renderExplosion(shapeRenderer, explosionTexture);
            }
        }



    }
}
