package pl.edu.agh.tgk.penrosesweeper.logic.rhombus;

import com.badlogic.gdx.math.Vector2;

import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static pl.edu.agh.tgk.penrosesweeper.logic.rhombus.PenroseTiling.PSI;

public class RhombusesGenerator {

    private RhombusesGenerator() {}

    public static List<Rhombus> generateRhombuses(double screenSize, int nGen) {

        // Rotation calculations
        double theta = Math.PI / 5;

        PenroseTiling.Complex rot = new PenroseTiling.Complex((float) Math.cos(theta), (float) Math.sin(theta));
        PenroseTiling.Complex bigRot = new PenroseTiling.Complex((float) Math.cos(2*theta), (float) Math.sin(2*theta));
        // Define points
        PenroseTiling.Complex A1 = new PenroseTiling.Complex(100/PSI, 0);
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

        PenroseTiling.Complex AA1 = new PenroseTiling.Complex(0, 0);
        PenroseTiling.Complex BB = bigRot.multiply(100f).add(new PenroseTiling.Complex(100f, 0));
        PenroseTiling.Complex CC1 = new PenroseTiling.Complex(100f / PSI, 0).add(new PenroseTiling.Complex(100f, 0));

        PenroseTiling.Complex DD = AA1.subtract(A2).add(C1.multiply(1/PSI));

        // Create PenroseP3 instance
        PenroseTiling.PenroseP3 tiling = new PenroseTiling.PenroseP3(nGen);
        tiling.setInitialTiles(List.of(
//            new PenroseTiling.BtileL(AA1, A2, C1.multiply(1/PSI)),
//            new PenroseTiling.BtileL(AA1, DD, C1.multiply(1/PSI)),
            new PenroseTiling.BtileS(A1, B, C1),
            new PenroseTiling.BtileS(A2, B, C2),
            new PenroseTiling.BtileS(A3, B, C3),
            new PenroseTiling.BtileS(A4, B, C4),
            new PenroseTiling.BtileS(A5, B, C5)
        ));

        // Generate tiling and write to SVG
        tiling.makeTiling();
        float scale = (float) (screenSize / (2 * getMax(tiling.getElements())));

        return tiling.getElements().stream()
            .map(element -> {
                PenroseTiling.Complex D = element.A.subtract(element.B).add(element.C);
                return new Rhombus(
                    new Vector2((float) element.A.real * scale, (float) element.A.imag * scale),
                    new Vector2((float) element.B.real * scale, (float) element.B.imag * scale),
                    new Vector2((float) element.C.real * scale, (float) element.C.imag * scale),
                    new Vector2((float) D.real  * scale, (float) D.imag  * scale)
                );
            })
            .collect(toList());

    }

    private static double getMax(List<PenroseTiling.RobinsonTriangle> triangles) {
        return triangles.stream()
            .flatMap(triangle -> Stream.of(
                triangle.A,
                triangle.B,
                triangle.C,
                triangle.A.subtract(triangle.B).add(triangle.C)
            )).flatMapToDouble(it -> DoubleStream.of(it.real, it.imag))
            .map(Math::abs)
            .max()
            .orElse(0);
    }
}
