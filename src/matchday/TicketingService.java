package matchday;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TicketingService {

    private final int stadiumCapacity;
    private final Map<String, List<String>> buyersByMatch = new HashMap<>();
    private final Set<String> closedSales = new HashSet<>();

    public TicketingService(int stadiumCapacity) {
        this.stadiumCapacity = stadiumCapacity;
    }

    public void openSales(String matchId) throws MatchDayException {
        if (buyersByMatch.containsKey(matchId)) {
            throw new MatchDayException("Ticket sales are already open for " + matchId);
        }
        buyersByMatch.put(matchId, new ArrayList<>());
        log("Sales opened for " + matchId + " (capacity " + stadiumCapacity + ")");
    }

    public String sellTicket(String matchId, String fanName) throws MatchDayException {
        List<String> buyers = requireOpenSales(matchId);
        if (buyers.size() >= stadiumCapacity) {
            throw new MatchDayException("Stadium is sold out for " + matchId);
        }
        buyers.add(fanName);
        String ticketId = String.format("%s-%05d", matchId, buyers.size());
        log("Ticket " + ticketId + " issued to " + fanName);
        return ticketId;
    }

    public int closeSales(String matchId) throws MatchDayException {
        int sold = requireOpenSales(matchId).size();
        closedSales.add(matchId);
        log("Sales closed for " + matchId + ", " + sold + " tickets sold");
        return sold;
    }

    public int cancelSales(String matchId) {
        List<String> buyers = buyersByMatch.remove(matchId);
        closedSales.remove(matchId);
        if (buyers == null) {
            return 0;
        }
        log("Sales cancelled for " + matchId + ", " + buyers.size() + " tickets refunded");
        return buyers.size();
    }

    public void archiveSales(String matchId) {
        buyersByMatch.remove(matchId);
        closedSales.remove(matchId);
        log("Sales archived for " + matchId);
    }

    private List<String> requireOpenSales(String matchId) throws MatchDayException {
        List<String> buyers = buyersByMatch.get(matchId);
        if (buyers == null || closedSales.contains(matchId)) {
            throw new MatchDayException("Ticket sales are not open for " + matchId);
        }
        return buyers;
    }

    private void log(String message) {
        System.out.println("   [Ticketing] " + message);
    }
}
