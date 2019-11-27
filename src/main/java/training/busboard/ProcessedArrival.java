package training.busboard;

public class ProcessedArrival {
    private String lineName;
    private String destinationName;
    private Integer timeToStation;
    private String formattedTime;

    public ProcessedArrival(String lineName, String destinationName, Integer timeToStation, String formattedTime) {
        this.lineName = lineName;
        this.destinationName = destinationName;
        this.timeToStation = timeToStation;
        this.formattedTime = formattedTime;
    }

    public Integer getTimeToStation() {
        return timeToStation;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public String getLineName() {
        return lineName;
    }
}