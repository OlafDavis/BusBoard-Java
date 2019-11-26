package training.busboard;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.jvnet.hk2.internal.SystemDescriptor;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    public static String busBoard(String postcode) {
        Client client = createClient();
        Location postcodeCoordinates = getCoordinates(client, postcode);
        StopPointDetails[] nearestTwoStops = getNearbyStops(client, postcodeCoordinates); 
        String boardString = Stream.of(nearestTwoStops).map(x -> displayArrivalsFor(client, x)).reduce("", (x,y) -> x.concat(y));
        return boardString;
    }

    private static String displayArrivalsFor(Client client, StopPointDetails stop) {
        Stream<Arrival> arrivalStream = getFiveArrivals(client, stop.naptanId);
        String displayString = "\nArrivals for stop " + stop.commonName + ":\n";
        displayString = arrivalStream.map(x -> displayOneArrival(x)).reduce(displayString,(x,y) -> x.concat(y));
        return displayString;
    }

    private static Location getCoordinates(Client client, String postcode) {
        String targetURL = "http://api.postcodes.io/postcodes/" + postcode;
        Location location = client.target(targetURL).request(MediaType.APPLICATION_JSON).get(PostcodeDetails.class).result;
        return location;
    }

    private static StopPointDetails[] getNearbyStops(Client client, Location location) {
        String targetURL = "https://api.tfl.gov.uk/StopPoint?stopTypes=NaptanPublicBusCoachTram&radius=1000&lat=" + location.latitude + "&lon=" + location.longitude;
        //NaptanBusCoachStation,NaptanBusWayPoint,NaptanOnstreetBusCoachStopCluster,NaptanOnstreetBusCoachStopPair,NaptanPrivateBusCoachTram,NaptanPublicBusCoachTram
        return Stream.of(client.target(targetURL).request(MediaType.APPLICATION_JSON)
                .get(NearbyStopPoints.class).stopPoints)
                .sorted((x, y) -> x.distance.compareTo(y.distance))
                .limit(2)
                .toArray(StopPointDetails[]::new);
    }

    private static Client createClient() {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        return client;
    }

    private static Stream<Arrival> getFiveArrivals(Client client, String naptanID) {
        String targetURL = "https://api.tfl.gov.uk/StopPoint/" + naptanID + "/Arrivals?app_key=c296999420f792c8d77672286948bd54&app_id=45d0999f";
        Stream<Arrival> arrivals = Stream.of(client.target(targetURL).request(MediaType.APPLICATION_JSON).get(Arrival[].class));
        return arrivals.sorted((x,y) -> x.expectedArrival.compareTo(y.expectedArrival)).limit(5);
    }

    private static String displayOneArrival(Arrival arrival) {
        String lineName = arrival.lineName;
        String expectedArrival = arrival.expectedArrival;
        String destinationName = arrival.destinationName;
        return "Bus number " + lineName + " to " + destinationName + " expected at " + expectedArrival + "\n";
    }
}
