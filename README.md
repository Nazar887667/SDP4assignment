# Match Day Organization: Facade Pattern (Java)

Assignment #4, ShP-2216 Software Design Patterns (Option C, free topic).
Author: Baktybay Nazar, group SE-2530.

Preparing a football match means coordinating independent services in the right
order. `MatchDayFacade` hides that orchestration behind a small API and undoes
partial work when a step fails.

## Structure

```
src/matchday/
  MatchDayFacade.java     Facade: prepareMatchDay, buyTicket, startMatch, finishMatchDay
  TicketingService.java   Subsystem 1: ticket sales within stadium capacity
  SecurityService.java    Subsystem 2: risk assessment and steward deployment
  PitchService.java       Subsystem 3: pitch preparation (wind limit, no double booking)
  Match.java              Data record
  MatchDayException.java  Single checked exception
  Main.java               Client that uses only the Facade
  AdvancedClient.java     Client that bypasses the Facade on purpose
docs/                     UML class diagram (PNG and Graphviz source)
```

## Build and run (JDK 17+)

```
mkdir out
javac --release 17 -d out src/matchday/*.java
java -cp out matchday.Main
java -cp out matchday.AdvancedClient
```

In IntelliJ IDEA: open the folder, mark `src` as Sources Root, run `Main`.

## Demo scenarios (`Main`)

1. Normal match day: prepare, sell tickets, play, finish.
2. Derby needs 440 stewards but only 400 exist: earlier steps are rolled back.
3. Storm (75 km/h wind): the pitch refuses and ticket sales are cancelled again.

## Pattern rules respected

* Subsystems never reference the Facade.
* Subsystems stay usable without the Facade (`AdvancedClient`).
* `Main` never creates or calls a subsystem class.
