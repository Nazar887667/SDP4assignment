package matchday;

public class AdvancedClient {

    public static void main(String[] args) throws MatchDayException {
        TicketingService ticketing = new TicketingService(1_000);
        ticketing.openSales("TRAINING-01");
        System.out.println("Direct use: " + ticketing.sellTicket("TRAINING-01", "Club president"));

        SecurityService security = new SecurityService(50);
        int stewards = security.stewardsNeeded(3_000, security.assessRisk(3_000, false));
        System.out.println("Stewards needed for 3000 fans at LOW risk: " + stewards);
    }
}
