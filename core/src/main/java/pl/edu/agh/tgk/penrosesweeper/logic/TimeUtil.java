package pl.edu.agh.tgk.penrosesweeper.logic;

public class TimeUtil {

    private TimeUtil() {
    }

    public static String formatTime(long time) {
        long hours = (time / 1000) / 3600;
        long minutes = ((time / 1000) % 3600) / 60;
        long seconds = (time / 1000) % 60;
        long milliseconds = time % 1000;

        String timeText;
        if (hours > 0) {
            timeText = String.format("%d:%02d:%02d:%03d", hours, minutes, seconds, milliseconds);
        } else if (minutes > 0) {
            timeText = String.format("%02d:%02d:%03d", minutes, seconds, milliseconds);
        } else {
            timeText = String.format("%02d:%03d", seconds, milliseconds);
        }

        return timeText;
    }
}
