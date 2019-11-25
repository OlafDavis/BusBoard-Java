package training.busboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StopPointDetails {
    public String naptanId;
    public Double distance;
    public String commonName;
    public String stopType;
}
