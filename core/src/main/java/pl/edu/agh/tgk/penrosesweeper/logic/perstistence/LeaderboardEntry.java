package pl.edu.agh.tgk.penrosesweeper.logic.perstistence;

public record LeaderboardEntry(String nick, long time) implements Comparable<LeaderboardEntry> {

    @Override
    public int compareTo(LeaderboardEntry o) {
        return - Long.compare(this.time, o.time);
    }
}
