package training.busboard;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Arrival {
    public String $type;
    public String id;
    public Integer operationType;
    public String vehicleId;
    public String naptanId;
    public String stationName;
    public String lineId;
    public String lineName;
    public String platformName;
    public String direction;
    public String bearing;
    public String destinationNaptanId;
    public String destinationName;
    public String timestamp; //date
    public Integer timeToStation;
    public String currentLocation;
    public String towards;
    public String expectedArrival; //date
    public String timeToLive; //date
    public String modeName;
    public Timing timing;
}