package pl.edu.agh.tgk.penrosesweeper.logic;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

public class Board {
    private final List<Tile> tiles;

    public Board() {
        List<Rhombus> rhombuses = loadRhombuses();
        List<Integer> values = generateValues(rhombuses);
        tiles = createTiles(rhombuses, values);
    }

    private List<Tile> createTiles(List<Rhombus> rhombuses, List<Integer> values) {
        List<Tile> tiles = IntStream.range(0, rhombuses.size())
            .mapToObj(i -> new Tile(rhombuses.get(i), values.get(i)))
            .toList();

        setNeighbours(tiles);

        return tiles;
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

    private List<Integer> generateValues(List<Rhombus> rhombuses) {
        int n = rhombuses.size();
        int bombCount = n / 10; // 10%
        List<Integer> values = new ArrayList<>(Collections.nCopies(n, 0));
        Random rand = new Random();


        // 1. Randomly choose bomb indexes
        Set<Integer> bombIndices = new HashSet<>();
        while (bombIndices.size() < bombCount) {
            bombIndices.add(rand.nextInt(n));
        }

        // 2. Build neighbor map (adjacency list)
        Map<Integer, List<Integer>> neighbours = new HashMap<>();
        Map<Vector2, List<Integer>> vertexMap = new HashMap<>();

        for (int i = 0; i < n; i++) {
            Rhombus rh = rhombuses.get(i);
            for (Vector2 v : List.of(rh.vA(), rh.vB(), rh.vC(), rh.vD())) {
                vertexMap.computeIfAbsent(v, k -> new ArrayList<>()).add(i);
            }
        }

        for (int i = 0; i < n; i++) {
            Set<Integer> neigh = new HashSet<>();
            Rhombus rh = rhombuses.get(i);
            for (Vector2 v : List.of(rh.vA(), rh.vB(), rh.vC(), rh.vD())) {
                for (int ni : vertexMap.get(v)) {
                    if (ni != i) neigh.add(ni);
                }
            }
            neighbours.put(i, new ArrayList<>(neigh));
        }


        // 3. Set bombs to -1
        for (int bombIndex : bombIndices) {
            values.set(bombIndex, -1);
        }

        // 4. Count bomb neighbors for each non-bomb tile
        for (int i = 0; i < n; i++) {
            if (values.get(i) == -1) continue; // skip bombs
            int count = 0;
            for (int neigh : neighbours.getOrDefault(i, Collections.emptyList())) {
                if (bombIndices.contains(neigh)) {
                    count++;
                }
            }
            values.set(i, count);
        }

        return values;
    }

    private void setNeighbours(List<Tile> tiles) {
        Map<Vector2, List<Tile>> vertexMap = new HashMap<>();

        for (Tile tile : tiles) {
            Rhombus rh = tile.getRhombus();
            for (Vector2 v : List.of(rh.vA(), rh.vB(), rh.vC(), rh.vD())) {
                vertexMap.computeIfAbsent(v, k -> new ArrayList<>()).add(tile);
            }
        }

        for (Tile tile : tiles) {
            Rhombus rh = tile.getRhombus();
            for (Vector2 v : List.of(rh.vA(), rh.vB(), rh.vC(), rh.vD())) {
                for (Tile neighbour : vertexMap.get(v)) {
                    if (tile != neighbour) tile.addNeighbour(neighbour);
                }
            }
        }
    }


    public List<Tile> getTiles() {
        return tiles;
    }

    public Optional<Tile> getTile(Vector2 point) {
        return tiles.stream().filter(t -> t.isPointInside(point)).findFirst();
    }
}
