package pl.edu.agh.tgk.penrosesweeper.logic;

public enum Difficulty {
    EASY(7),
    MEDIUM(10),
    HARD(15);

    public final int minePercentage;

    Difficulty(int minePercentage) {
        this.minePercentage = minePercentage;
    }
}
