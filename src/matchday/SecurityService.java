package matchday;

import java.util.HashMap;
import java.util.Map;

public class SecurityService {

    public enum RiskLevel { LOW, MEDIUM, HIGH }

    private static final int MEDIUM_RISK_ATTENDANCE = 15_000;
    private static final int FANS_PER_STEWARD = 100;

    private final int stewardPool;
    private final Map<String, Integer> deployedByMatch = new HashMap<>();

    public SecurityService(int stewardPool) {
        this.stewardPool = stewardPool;
    }

    public RiskLevel assessRisk(int expectedAttendance, boolean derby) {
        RiskLevel risk;
        if (derby) {
            risk = RiskLevel.HIGH;
        } else if (expectedAttendance >= MEDIUM_RISK_ATTENDANCE) {
            risk = RiskLevel.MEDIUM;
        } else {
            risk = RiskLevel.LOW;
        }
        log("Risk assessed as " + risk);
        return risk;
    }

    public int stewardsNeeded(int expectedAttendance, RiskLevel risk) {
        int base = (int) Math.ceil(expectedAttendance / (double) FANS_PER_STEWARD);
        return switch (risk) {
            case LOW -> base;
            case MEDIUM -> (int) Math.ceil(base * 1.5);
            case HIGH -> base * 2;
        };
    }

    public void deployStewards(String matchId, int count) throws MatchDayException {
        int available = stewardPool - deployedByMatch.values().stream().mapToInt(Integer::intValue).sum();
        if (count > available) {
            throw new MatchDayException("Need " + count + " stewards but only " + available + " are available");
        }
        deployedByMatch.put(matchId, count);
        log(count + " stewards deployed");
    }

    public void recallStewards(String matchId) {
        Integer count = deployedByMatch.remove(matchId);
        if (count != null) {
            log(count + " stewards recalled");
        }
    }

    private void log(String message) {
        System.out.println("   [Security] " + message);
    }
}
