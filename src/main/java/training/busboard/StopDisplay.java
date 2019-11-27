package training.busboard;

import training.busboard.Main;

import javax.ws.rs.client.Client;
import java.util.Arrays;
import java.util.stream.Stream;

public class StopDisplay {
    private String stopName;
    private ProcessedArrival[] arrivals;
    private String progressBar;
    private String compassPoint;
    private String towards;

    public StopDisplay(Client client, StopPointDetails stopPointDetails) {
        this.stopName = stopPointDetails.commonName;
        this.arrivals = Main.getFiveArrivals(client ,stopPointDetails.naptanId);
        this.progressBar = Main.makeProgressBar(Stream.of(arrivals).map(x -> x.getTimeToStation()).toArray(Integer[]::new));
        AdditionalProperty possibleDirection = Arrays.stream(stopPointDetails.additionalProperties).filter((x) -> x.getKey().equals("CompassPoint")).findFirst().orElse(null);
        this.compassPoint = possibleDirection == null ? null : possibleDirection.getValue();
        AdditionalProperty possibleTowards = Arrays.stream(stopPointDetails.additionalProperties).filter((x) -> x.getKey().equals("Towards")).findFirst().orElse(null);
        this.towards = possibleTowards == null ? null : possibleTowards.getValue();
    }

    public String getStopName() {
        return stopName;
    }

    public ProcessedArrival[] getArrivals() {
        return arrivals;
    }

    public String getProgressBar() {
        return progressBar;
    }

    public String getCompassPoint() {return compassPoint; }

    public String getDirection() {return Main.processDirection(compassPoint);}

    public String getTowards() {
        return towards;
    }
}
