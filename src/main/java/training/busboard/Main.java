package training.busboard;

import com.google.gson.Gson;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.stream.Stream;

public class Main {
    public static void main(String args[]) {
        Client client = createClient();
        Stream<Arrival> arrivals = getResource(client, "490008660N");
        arrivals.forEach(Main::displayOneArrival);
    }

    private static Client createClient() {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        return client;
    }

    private static Stream<Arrival> getResource(Client client, String naptanID) {
        String targetURL = "https://api.tfl.gov.uk/StopPoint/" + naptanID + "/Arrivals?app_key=c296999420f792c8d77672286948bd54&app_id=45d0999f";
        Stream<Arrival> arrivals = Stream.of(client.target(targetURL).request(MediaType.APPLICATION_JSON).get(Arrival[].class));
        return arrivals;
    }

    private static void displayArrivals() {

    }

    private static void displayOneArrival(Arrival arrival) {
        String lineId = arrival.lineId;
        String expectedArrival = arrival.expectedArrival;
        String destinationName = arrival.destinationName;
        String direction = arrival.direction;
        String $type = arrival.$type;
        System.out.println("lineId: " + lineId);
        System.out.println("expectedArrival: " + expectedArrival);
        System.out.println("destinationName: " + destinationName);
        System.out.println("direction: " + direction);
        System.out.println("$type: " + $type);
    }

}	
