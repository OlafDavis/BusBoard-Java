package training.busboard;

import training.busboard.Main;

import javax.ws.rs.client.Client;
import java.util.stream.Stream;

public class StopDisplay {
    private String stopName;
    private ProcessedArrival[] arrivals;
    private String progressBar;

    public StopDisplay(Client client, StopPointDetails stopPointDetails) {
        this.stopName = stopPointDetails.commonName;
        this.arrivals = Main.getFiveArrivals(client ,stopPointDetails.naptanId);
        this.progressBar = Main.makeProgressBar(Stream.of(arrivals).map(x -> x.getTimeToStation()).toArray(Integer[]::new));
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
}
