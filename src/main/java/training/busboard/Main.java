package training.busboard;

import com.google.gson.Gson;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class Main {
    public static void main(String args[]) {
        Client client = createClient();
        String resource = getResource(client, "490008660N");
        jsonParser(resource);
        System.out.println(resource);
    }

    public static Client createClient() {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        return client;
    }

    public static String getResource(Client client, String naptanID) {
        String targetURL = "https://api.tfl.gov.uk/StopPoint/" + naptanID + "/Arrivals?app_key=c296999420f792c8d77672286948bd54&app_id=45d0999f";
        String resource = client.target(targetURL).request(MediaType.APPLICATION_JSON).get(String.class);
        return resource;
    }

    public static void jsonParser(String jsonString) {
        Gson gson = new Gson();
        Arrival arrivals[] = gson.fromJson(jsonString,Arrival[].class);

    }
    public class Arrival {

    }

}	
