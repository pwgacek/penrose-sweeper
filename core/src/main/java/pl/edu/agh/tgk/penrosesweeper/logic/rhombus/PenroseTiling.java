package pl.edu.agh.tgk.penrosesweeper.logic.rhombus;

import java.util.*;
import java.util.stream.Collectors;

public class PenroseTiling {

    public static final float TOL = 1e-5F;
    public static final float PSI = (float) ((Math.sqrt(5) - 1) / 2);
    public static final float PSI2 = 1 - PSI;

    static class Complex implements Comparable<Complex >{
        float real, imag;

        Complex(float real, float imag) {
            this.real = real;
            this.imag = imag;
        }

        Complex add(Complex other) {
            return new Complex(this.real + other.real, this.imag + other.imag);
        }

        Complex subtract(Complex other) {
            return new Complex(this.real - other.real, this.imag - other.imag);
        }

        Complex multiply(float scalar) {
            return new Complex(this.real * scalar, this.imag * scalar);
        }

        Complex multiply(Complex other) {
            float realPart = this.real * other.real - this.imag * other.imag;
            float imagPart = this.real * other.imag + this.imag * other.real;
            return new Complex(realPart, imagPart);
        }

        Complex conjugate() {
            return new Complex(real, -imag);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Complex complex = (Complex) obj;
            return Math.abs(real - complex.real) < TOL && Math.abs(imag - complex.imag) < TOL;
        }

        @Override
        public int compareTo(Complex other) {
            int realComparison = Float.compare(this.real, other.real);
            if (realComparison != 0) {
                return realComparison;
            }
            return Float.compare(this.imag, other.imag);
        }

        @Override
        public int hashCode() {
            return Objects.hash(real, imag);
        }

        @Override
        public String toString() {
            return String.format("Complex{real=%.2f, imag=%.2f}", real, imag);
        }
    }

    static abstract class RobinsonTriangle {
        Complex A, B, C;

        RobinsonTriangle(Complex A, Complex B, Complex C) {
            this.A = A;
            this.B = B;
            this.C = C;
        }

        Complex centre() {
            return A.add(C).multiply(0.5f);
        }


        RobinsonTriangle conjugate() {
            return createInstance(A.conjugate(), B.conjugate(), C.conjugate());
        }

        abstract List<RobinsonTriangle> inflate();

        protected abstract RobinsonTriangle createInstance(Complex A, Complex B, Complex C);
    }

    static class BtileL extends RobinsonTriangle {
        BtileL(Complex A, Complex B, Complex C) {
            super(A, B, C);
        }

        @Override
        List<RobinsonTriangle> inflate() {
            Complex D = A.multiply(PSI2).add(C.multiply(PSI));
            Complex E = A.multiply(PSI2).add(B.multiply(PSI));
            return List.of(
                new BtileL(D, E, A),
                new BtileS(E, D, B),
                new BtileL(C, D, B)
            );
        }

        @Override
        protected RobinsonTriangle createInstance(Complex A, Complex B, Complex C) {
            return new BtileL(A, B, C);
        }
    }

    static class BtileS extends RobinsonTriangle {
        BtileS(Complex A, Complex B, Complex C) {
            super(A, B, C);
        }

        @Override
        List<RobinsonTriangle> inflate() {
            Complex D = A.multiply(PSI).add(B.multiply(PSI2));
            return List.of(
                new BtileS(D, C, A),
                new BtileL(C, D, B)
            );
        }

        @Override
        protected RobinsonTriangle createInstance(Complex A, Complex B, Complex C) {
            return new BtileS(A, B, C);
        }
    }

    static class PenroseP3 {
        private final int ngen;
        private List<RobinsonTriangle> elements;

        PenroseP3(int ngen) {
            this.ngen = ngen;
            this.elements = new ArrayList<>();
        }

        void setInitialTiles(List<RobinsonTriangle> tiles) {
            this.elements = tiles;
        }

        void inflate() {
            elements = elements.stream()
                .flatMap(element -> element.inflate().stream())
                .collect(Collectors.toList());
        }

        void removeDuplicates() {
            List<RobinsonTriangle> sortedElements = elements.stream()
                .sorted(Comparator.comparing(RobinsonTriangle::centre))
                .toList();
            elements = new ArrayList<>(List.of(sortedElements.getFirst()));
            for(int i = 0; i < sortedElements.size() - 1; i++) {
                if (!sortedElements.get(i).centre().equals(sortedElements.get(i + 1).centre())) {
                    elements.add(sortedElements.get(i + 1));
                }
            }

        }

        void addConjugateElements() {
            List<RobinsonTriangle> conjugates = elements.stream()
                .map(RobinsonTriangle::conjugate)
                .toList();
            elements.addAll(conjugates);
        }


        void makeTiling() {

            for (int i = 0; i < ngen; i++) {
                inflate();
            }
            removeDuplicates();
            addConjugateElements();
            removeDuplicates();
        }

        public List<RobinsonTriangle> getElements() {
            return elements;
        }
    }

}
