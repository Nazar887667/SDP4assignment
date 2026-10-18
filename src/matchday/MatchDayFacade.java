package matchday;

import matchday.SecurityService.RiskLevel;

public class MatchDayFacade {

    public record Report(String fixture, String riskLevel, int stewards) {
        public String summary() {
            return fixture + " | risk " + riskLevel + " | " + stewards + " stewards";
        }
    }

    private final TicketingService ticketing;
    private final SecurityService security;
    private final PitchService pitch;

    public MatchDayFacade() {
        this(new TicketingService(23_804), new SecurityService(400), new PitchService());
    }

    public MatchDayFacade(TicketingService ticketing, SecurityService security, PitchService pitch) {
        this.ticketing = ticketing;
        this.security = security;
        this.pitch = pitch;
    }

    public Report prepareMatchDay(Match match) throws MatchDayException {
        String id = match.id();
        try {
            ticketing.openSales(id);
            pitch.prepare(id, match.windKmh());
            RiskLevel risk = security.assessRisk(match.expectedAttendance(), match.derby());
            int stewards = security.stewardsNeeded(match.expectedAttendance(), risk);
            security.deployStewards(id, stewards);
            return new Report(match.title(), risk.name(), stewards);
        } catch (MatchDayException failure) {
            rollback(id);
            throw new MatchDayException("Could not prepare " + match.title() + ": " + failure.getMessage(), failure);
        }
    }

    public String buyTicket(String matchId, String fanName) throws MatchDayException {
        return ticketing.sellTicket(matchId, fanName);
    }

    public int startMatch(String matchId) throws MatchDayException {
        return ticketing.closeSales(matchId);
    }

    public void finishMatchDay(String matchId) {
        security.recallStewards(matchId);
        pitch.release(matchId);
        ticketing.archiveSales(matchId);
    }

    private void rollback(String matchId) {
        security.recallStewards(matchId);
        pitch.release(matchId);
        ticketing.cancelSales(matchId);
    }
}
