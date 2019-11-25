package training.busboard;

import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    public static void main(String args[]) {
        Client client = createClient();
        String postcode = getPostcode();
        Location postcodeCoordinates = getCoordinates(client, postcode);
        Stream<String> nearestTwoNaptans = getNearbyStopCodes(client, postcodeCoordinates);
        nearestTwoNaptans.forEach(System.out::println);
        //String stopCode = getStopCode();
        //Stream<Arrival> arrivals = getFiveArrivals(client, stopCode); //490008660N
        //arrivals.forEach(Main::displayOneArrival);
    }

    private static String getPostcode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your postcode");
        String postcode = scanner.nextLine();
        return postcode;
    }

    private static Location getCoordinates(Client client, String postcode) {
        String targetURL = "http://api.postcodes.io/postcodes/" + postcode;
        Location location = client.target(targetURL).request(MediaType.APPLICATION_JSON).get(PostcodeDetails.class).result;
        return location;
    }

    private static Stream<String> getNearbyStopCodes(Client client, Location location) {
        System.out.println(location.latitude);
        System.out.println(location.longitude);
        String targetURL = "https://api.tfl.gov.uk/StopPoint?stopTypes=NaptanPublicBusCoachTram&radius=1000&lat=" + location.latitude + "&lon=" + location.longitude;
        //NaptanBusCoachStation,NaptanBusWayPoint,NaptanOnstreetBusCoachStopCluster,NaptanOnstreetBusCoachStopPair,NaptanPrivateBusCoachTram,NaptanPublicBusCoachTram
        Stream<StopPointDetails> stringStream = Stream.of(client.target(targetURL).request(MediaType.APPLICATION_JSON)
                .get(NearbyStopPoints.class).stopPoints);
        return stringStream.sorted((x, y) -> x.distance.compareTo(y.distance))
                .limit(2)
                .map(x -> x.naptanId);
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

    private static void displayOneArrival(Arrival arrival) {
        String lineName = arrival.lineName;
        String expectedArrival = arrival.expectedArrival;
        String destinationName = arrival.destinationName;
        System.out.println("Bus number " + lineName + " to " + destinationName + " expected at " + expectedArrival);
    }
}
