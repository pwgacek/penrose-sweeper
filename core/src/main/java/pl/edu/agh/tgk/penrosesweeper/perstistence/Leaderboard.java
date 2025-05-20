package pl.edu.agh.tgk.penrosesweeper.perstistence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import pl.edu.agh.tgk.penrosesweeper.logic.Difficulty;
import pl.edu.agh.tgk.penrosesweeper.logic.board.BoardSize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Leaderboard {
    private static final int MAX_ENTRIES = 10;

    private static Leaderboard instance;

    private final Preferences prefs = Gdx.app.getPreferences("Leaderboard");
    private final Map<String, PriorityQueue<LeaderboardEntry>> leaderboards = new HashMap<>();


    public static Leaderboard getInstance() {
        if (instance == null) {
            instance = new Leaderboard();
        }
        return instance;
    }

    private PriorityQueue<LeaderboardEntry> buildLeaderBoard(String leaderboardKey) {
        PriorityQueue<LeaderboardEntry> leaderboard = new PriorityQueue<>();
        for (int i = 0; i < MAX_ENTRIES; i++) {
            String nick = getNick(leaderboardKey, i);
            long time = getTime(leaderboardKey, i);
            if (nick != null && time != -1) {
                leaderboard.add(new LeaderboardEntry(nick, time));
            }
        }
        return leaderboard;

    }

    private String getNick(String leaderboardKey, int position) {
        return prefs.getString(getNickKey(leaderboardKey, position), null);
    }

    private String getNickKey(String leaderboardKey, int position) {
        return leaderboardKey + "_" + position + "_NICK";
    }

    private long getTime(String leaderboardId, int position) {
        return prefs.getLong(getTimeKey(leaderboardId, position), -1);
    }

    private String getTimeKey(String leaderboardId, int position) {
        return leaderboardId + "_" + position + "_TIME";
    }



    private Leaderboard() {
        initializeLeaderboards();
    }

    private void initializeLeaderboards() {
        for (Difficulty difficulty: Difficulty.values()) {
            for (BoardSize size: BoardSize.values()) {
                String key = getKey(difficulty, size);
                leaderboards.put(key, buildLeaderBoard(key));
            }
        }
    }

    private String getKey(Difficulty difficulty, BoardSize boardSize) {
        return difficulty.name() + "_" + boardSize.name();
    }

    public void addEntry(Difficulty difficulty, BoardSize boardSize, LeaderboardEntry entry) {
        String key = getKey(difficulty, boardSize);
        PriorityQueue<LeaderboardEntry> leaderboard = leaderboards.get(key);

        leaderboard.add(entry);
        if (leaderboard.size() > MAX_ENTRIES) {
            leaderboard.poll();
        }

        saveLeaderboard(key, leaderboard);
    }

    public void saveLeaderboard(String leaderBoardKey, PriorityQueue<LeaderboardEntry> leaderboard) {
        List<LeaderboardEntry> entries = leaderboard.stream()
            .sorted()
            .toList()
            .reversed();

        for (int i = 0; i < entries.size(); i++) {
            LeaderboardEntry entry = entries.get(i);
            prefs.putString(getNickKey(leaderBoardKey, i), entry.nick());
            prefs.putLong(getTimeKey(leaderBoardKey, i), entry.time());
        }

        prefs.flush();
    }

    public List<LeaderboardEntry> getOrderedEntries(Difficulty difficulty, BoardSize boardSize) {
        return leaderboards.get(getKey(difficulty, boardSize)).stream().sorted().toList().reversed();
    }
}
