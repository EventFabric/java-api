package com.eventfabric.examples;

import java.util.LinkedHashMap;

import com.eventfabric.api.client.Response;
import com.eventfabric.api.client.EventClient;
import com.eventfabric.model.Event;

public class EventFabricExample {
    private EventClient client;

    public EventFabricExample(String username, String password) {
        this.client = new EventClient(username, password);
    }

    public void run() {
        boolean success = client.authenticate();
        System.out.println("successful? " + success);

        // create the event value to send

        LinkedHashMap<String, Object> value = new LinkedHashMap<String, Object>();
        value.put("count", 4);
        value.put("price", 12.3);
        value.put("yes", true);

        // create the event to send
        Event event = new Event("my.channel", value);

        // send the event
        Response response = client.send(event);

        // do something with the response
        System.out.println("status: " + response.getStatus());
        System.out.println("result" + response.getResult());

    }

    public static void main(String[] args) {
        String username, password;

        if (args.length != 2) {
            System.err.println("Usage: <program> username password");
        } else {
            username = args[0];
            password = args[1];

            EventFabricExample ping = new EventFabricExample(username, password);
            ping.run();
        }
    }
}
