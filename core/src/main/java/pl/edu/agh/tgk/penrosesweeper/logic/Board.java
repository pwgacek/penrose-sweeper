package pl.edu.agh.tgk.penrosesweeper.logic;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class Board {
    private final List<Tile> tiles;
    private final int minePercentage;

    public Board(int minePercentage) {
        this.tiles = loadRhombuses().stream().map(Tile::new).toList();
        this.minePercentage = minePercentage;
        setNeighbours();
    }

    private List<Rhombus> loadRhombuses() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Gdx.files.internal("figures.txt").read()))) {
            String line;
            List<Rhombus> rhombuses = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] coordinates = line.split(",");
                if (coordinates.length == 8) {
                    Vector2 vA = new Vector2(Float.parseFloat(coordinates[0]) * 4, Float.parseFloat(coordinates[1]) * 4);
                    Vector2 vB = new Vector2(Float.parseFloat(coordinates[2]) * 4, Float.parseFloat(coordinates[3]) * 4);
                    Vector2 vC = new Vector2(Float.parseFloat(coordinates[4]) * 4, Float.parseFloat(coordinates[5]) * 4);
                    Vector2 vD = new Vector2(Float.parseFloat(coordinates[6]) * 4, Float.parseFloat(coordinates[7]) * 4);

                    rhombuses.add(new Rhombus(vA, vB, vC, vD));
                }
            }
            return rhombuses;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Vector2 roundVector(Vector2 v) {
        float x = new BigDecimal(v.x).setScale(4, RoundingMode.DOWN).floatValue();
        float y = new BigDecimal(v.y).setScale(4, RoundingMode.DOWN).floatValue();
        return new Vector2(x, y);
    }

    private void setNeighbours() {
        Map<Vector2, List<Tile>> vertexMap = new HashMap<>();

        for (Tile tile : tiles) {
            Rhombus rh = tile.getRhombus();
            List.of(rh.vA(), rh.vB(), rh.vC(), rh.vD())
                .forEach(v -> vertexMap.computeIfAbsent(roundVector(v), k -> new ArrayList<>()).add(tile));

        }

        for (Tile tile : tiles) {
            Rhombus rh = tile.getRhombus();
            for (Vector2 v : List.of(rh.vA(), rh.vB(), rh.vC(), rh.vD())) {
                for (Tile neighbour : vertexMap.get(roundVector(v))) {
                    if (tile != neighbour) tile.addNeighbour(neighbour);
                }
            }
        }
    }

    public void initialize(Tile startingTile) {
        int mineCount = (int) (minePercentage * tiles.size() / 100f);

        List<Integer> possibleMineIndices = getPossibleMineIndices(startingTile);

        Random rand = new Random();
        for (int i = 0; i < mineCount; i++) {
            int mineIndex = possibleMineIndices.remove(rand.nextInt(possibleMineIndices.size()));
            tiles.get(mineIndex).setMine();
        }

        tiles.stream().filter(it -> !it.isMine()).forEach(it -> {

            for (Tile neigh : it.getNeighbours()) {
                if (neigh.isMine()) {
                    it.increaseValue();
                }
            }
        });
    }

    private List<Integer> getPossibleMineIndices(Tile startingTile) {
        Set<Tile> noMineTiles = new HashSet<>(startingTile.getNeighbours());
        noMineTiles.add(startingTile);

        List<Integer> possibleMineIndices = new ArrayList<>(noMineTiles.size());
        for (int i = 0; i < tiles.size(); i++) {
            if (!noMineTiles.contains(tiles.get(i))) {
                possibleMineIndices.add(i);
            }
        }
        return possibleMineIndices;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public Optional<Tile> getTile(Vector2 point) {
        return tiles.stream().filter(t -> t.isPointInside(point)).findFirst();
    }
}
