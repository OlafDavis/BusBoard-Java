package training.busboard;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.jvnet.hk2.internal.SystemDescriptor;
import org.thymeleaf.util.StringUtils;

import javax.swing.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Main {
    public static Stream<StopDisplay> busBoard(String postcode) {
        Client client = createClient();
        Location postcodeCoordinates = getCoordinates(client, postcode);
        StopPointDetails[] nearestTwoStops = getNearbyStops(client, postcodeCoordinates);
        System.out.println(nearestTwoStops[0].naptanId);
        System.out.println(nearestTwoStops[1].naptanId);
        return Stream.of(nearestTwoStops).map(x -> new StopDisplay(client,x));
    }

//    private static Stream<String> displayArrivalsFor(Client client, StopPointDetails stop) {
//        Stream<Arrival> arrivalStream = getFiveArrivals(client, stop.naptanId);
//        Stream<String> displayString = "\nArrivals for stop " + stop.commonName + ":\n";
//        displayString = arrivalStream.map(x -> displayOneArrival(x)).reduce(displayString,(x,y) -> x.concat(y));
//        return displayString;
//    }

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

    public static ProcessedArrival[] getFiveArrivals(Client client, String naptanID) {
        String targetURL = "https://api.tfl.gov.uk/StopPoint/" + naptanID + "/Arrivals?app_key=c296999420f792c8d77672286948bd54&app_id=45d0999f";
        Stream<Arrival> arrivals = Stream.of(client.target(targetURL).request(MediaType.APPLICATION_JSON).get(Arrival[].class));
        return arrivals.sorted((x,y) -> x.expectedArrival.compareTo(y.expectedArrival)).limit(5).map(x -> processArrival(x)).toArray(ProcessedArrival[]::new);
    }

    public static ProcessedArrival processArrival(Arrival arrival) {
        Matcher matcher = Pattern.compile("T(.*)Z").matcher(arrival.expectedArrival);
        String formattedTime = "";
        matcher.find();
        formattedTime = matcher.group(1);
        return new ProcessedArrival(arrival.lineName, arrival.destinationName, arrival.timeToStation, formattedTime);
    }

//    private static String displayOneArrival(Arrival arrival) {
//        String lineName = arrival.lineName;
//        String expectedArrival = arrival.expectedArrival;
//        String destinationName = arrival.destinationName;
//        return "Bus number " + lineName + " to " + destinationName + " expected at " + expectedArrival;
//    }
    public static String makeProgressBar (Integer[] timesToStation) {
//        Stream<Integer> busPositions = Stream.of(timesToStation).map(x -> 60 - x/30);
//        String progressString = "____________________________________________________________\uD83D\uDE8F";
//        busPositions.forEach((x) -> progressString = progressString + ;

        String progressString = "";
        String separator = "_";
        for (Integer t : timesToStation) {
            progressString += StringUtils.repeat(separator,t/30-progressString.length()) + "\uD83D\uDE8C";
        }
        progressString="\uD83E\uDDCD\uD83D\uDE8F"+ progressString;
        return progressString;
    }

    public static String processDirection(String compassPoint) {
        String direction;
        switch (compassPoint) {
            case "N": direction = "Northbound"; break;
            case "E": direction = "Eastbound"; break;
            case "S": direction = "Southbound"; break;
            case "W": direction = "Westbound"; break;
            case "NE": direction = "Northeastbound"; break;
            case "NW": direction = "Northwestbound"; break;
            case "SE": direction = "Southeastbound"; break;
            case "SW": direction = "Southwestbound"; break;
            default: direction = "";
        }
        return direction;
    }

    public static Boolean ValidatePostcode(String postcode) {
        Client client = createClient();
        String targetURL = "https://api.postcodes.io/postcodes/" + postcode + "/validate";
        System.out.println(targetURL);
        System.out.println(client.target(targetURL).request(MediaType.APPLICATION_JSON).get(PostcodeValidator.class).Validated());
        System.out.println(client.target(targetURL).request(MediaType.APPLICATION_JSON).get(PostcodeValidator.class).result);
        return client.target(targetURL).request(MediaType.APPLICATION_JSON).get(PostcodeValidator.class).Validated();
    }

}
