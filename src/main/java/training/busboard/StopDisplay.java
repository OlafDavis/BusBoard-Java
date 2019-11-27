package training.busboard;

import training.busboard.Main;

import javax.ws.rs.client.Client;
import java.util.stream.Stream;

public class StopDisplay {
    private String stopName;
    private Arrival[] arrivals;

    public StopDisplay(Client client, StopPointDetails stopPointDetails) {
        this.stopName = stopPointDetails.commonName;
        this.arrivals = Main.getFiveArrivals(client ,stopPointDetails.naptanId);
    }

    public String getStopName() {
        return stopName;
    }

    public Arrival[] getArrivals() {
        return arrivals;
    }
}
