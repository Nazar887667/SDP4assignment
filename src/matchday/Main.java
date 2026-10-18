package matchday;

/** Client: uses only MatchDayFacade, never a subsystem class. */
public class Main {

    public static void main(String[] args) {
        MatchDayFacade matchDay = new MatchDayFacade();

        title("1. Normal match day");
        Match league = new Match("KAI-AST-01", "FC Kairat", "FC Astana", 16_000, false, 20);
        try {
            System.out.println("PREPARED -> " + matchDay.prepareMatchDay(league).summary());
            matchDay.buyTicket(league.id(), "Aigerim");
            matchDay.buyTicket(league.id(), "Daniyar");
            System.out.println("KICK-OFF with " + matchDay.startMatch(league.id()) + " fans");
            matchDay.finishMatchDay(league.id());
        } catch (MatchDayException e) {
            System.out.println("!! " + e.getMessage());
        }

        title("2. Derby needs 440 stewards, only 400 exist: everything is rolled back");
        Match derby = new Match("KAI-ALM-02", "FC Kairat", "FC Almaty", 22_000, true, 15);
        try {
            matchDay.prepareMatchDay(derby);
        } catch (MatchDayException e) {
            System.out.println("!! " + e.getMessage());
        }
        try {
            matchDay.buyTicket(derby.id(), "Timur");
        } catch (MatchDayException e) {
            System.out.println("!! Proof of rollback: " + e.getMessage());
        }

        title("3. Storm: the pitch refuses, ticket sales are cancelled again");
        Match storm = new Match("KAI-AST-03", "FC Kairat", "FC Astana", 12_000, false, 75);
        try {
            matchDay.prepareMatchDay(storm);
        } catch (MatchDayException e) {
            System.out.println("!! " + e.getMessage());
        }
    }

    private static void title(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }
}
