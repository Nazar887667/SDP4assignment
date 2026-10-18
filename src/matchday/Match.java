package matchday;

public record Match(String id, String home, String away,
                    int expectedAttendance, boolean derby, int windKmh) {

    public Match {
        if (home.equals(away)) {
            throw new IllegalArgumentException("A club cannot play against itself");
        }
        if (expectedAttendance <= 0) {
            throw new IllegalArgumentException("Expected attendance must be positive");
        }
    }

    public String title() {
        return home + " vs " + away;
    }
}
