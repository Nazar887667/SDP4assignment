package matchday;

public class PitchService {

    private static final int MAX_SAFE_WIND_KMH = 60;

    private String reservedFor;

    public void prepare(String matchId, int windKmh) throws MatchDayException {
        if (reservedFor != null && !reservedFor.equals(matchId)) {
            throw new MatchDayException("Pitch is already reserved for " + reservedFor);
        }
        if (windKmh > MAX_SAFE_WIND_KMH) {
            throw new MatchDayException("Wind of " + windKmh + " km/h is too strong to play");
        }
        reservedFor = matchId;
        log("Grass cut and lines marked for " + matchId);
    }

    public void release(String matchId) {
        if (matchId.equals(reservedFor)) {
            reservedFor = null;
            log("Pitch released by " + matchId);
        }
    }

    private void log(String message) {
        System.out.println("   [Pitch] " + message);
    }
}
