package pl.edu.agh.tgk.penrosesweeper.logic.generation;

import com.badlogic.gdx.math.Vector2;
import pl.edu.agh.tgk.penrosesweeper.logic.Rhombus;

import java.util.List;
import java.util.stream.Collectors;

public class RhombusesGenerator {

    private RhombusesGenerator() {}

    public static List<Rhombus> generateRhombuses(int n) {
        float scale = 760;

        // Rotation calculations
        double theta = Math.PI / 5;
        PenroseTiling.Complex rot = new PenroseTiling.Complex((float) Math.cos(theta), (float) Math.sin(theta));
        // Define points
        PenroseTiling.Complex A1 = new PenroseTiling.Complex(scale, 0);
        PenroseTiling.Complex B = new PenroseTiling.Complex(0, 0);
        PenroseTiling.Complex C1 = A1.multiply(rot);
        PenroseTiling.Complex C2 = A1.multiply(rot);

        PenroseTiling.Complex A2 = C1.multiply(rot);
        PenroseTiling.Complex A3 = C1.multiply(rot);

        PenroseTiling.Complex C3 = A3.multiply(rot);
        PenroseTiling.Complex C4 = A3.multiply(rot);

        PenroseTiling.Complex A4 = C4.multiply(rot);
        PenroseTiling.Complex A5 = C4.multiply(rot);

        PenroseTiling.Complex C5 = A1.multiply(-1);

        // Create PenroseP3 instance
        PenroseTiling.PenroseP3 tiling = new PenroseTiling.PenroseP3(n);
        tiling.setInitialTiles(List.of(
            new PenroseTiling.BtileS(A1, B, C1),
            new PenroseTiling.BtileS(A2, B, C2),
            new PenroseTiling.BtileS(A3, B, C3),
            new PenroseTiling.BtileS(A4, B, C4),
            new PenroseTiling.BtileS(A5, B, C5)
        ));

        // Generate tiling and write to SVG
        tiling.makeTiling();

        return tiling.getElements().stream()
            .map(element -> {
                PenroseTiling.Complex D = element.A.subtract(element.B).add(element.C);
                return new Rhombus(
                    new Vector2(element.A.real, element.A.imag),
                    new Vector2(element.B.real, element.B.imag),
                    new Vector2(element.C.real, element.C.imag),
                    new Vector2(D.real, D.imag)
                );
            })
            .collect(Collectors.toList());
    }
}
