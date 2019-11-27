package training.busboard.web;

import training.busboard.Main;
import training.busboard.StopDisplay;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BusInfo {
    private final String postcode;
    private final Stream<StopDisplay> stopDisplays;

    public BusInfo(String postcode) {
        this.postcode = postcode;
        this.stopDisplays = Main.busBoard(postcode);
    }

    public String getPostcode() {
        return this.postcode;
    }
    public StopDisplay[] getStopDisplays() {
        return this.stopDisplays.toArray(StopDisplay[]::new);
    }
}
