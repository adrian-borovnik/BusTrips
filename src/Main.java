import javafx.print.Collation;
import sun.util.resources.LocaleData;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        if (args.length != 3) {
            System.out.println("Invalid number of arguments.");
            return;
        }

        String stopID = args[0];
        int numOfBuses = Integer.parseInt(args[1]);
        String mode = args[2];

        LocalTime currentTime = LocalTime.now();
        String currentTimeString = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.println(currentTime);

        CSV stops = new CSV("gtfs/stops.txt", new String[]{"stop_id", "stop_name"});
        CSV stopTimes = new CSV("gtfs/stop_times.txt", new String[]{"trip_id", "stop_id", "arrival_time"});
        CSV trips = new CSV("gtfs/trips.txt", new String[]{"trip_id", "route_id"});

//        System.out.println(stops.getTable());
//        System.out.println(trips.getTable());

        ArrayList<HashMap<String, String>> stopsTable = stops.getTable();
        ArrayList<HashMap<String, String>> timeTable = stopTimes.getTable();
        ArrayList<HashMap<String, String>> tripsTable = trips.getTable();

        timeTable.sort((o1, o2) -> {
            LocalTime time1 = LocalTime.parse(o1.get("arrival_time"));
            LocalTime time2 = LocalTime.parse(o2.get("arrival_time"));

            return time1.compareTo(time2);
        });

        for (int i = timeTable.size() - 1; i >= 0; --i) {

            if (!timeTable.get(i).get("stop_id").equals(stopID)) {
                timeTable.remove(stopTimes.getTable().get(i));
                continue;
            }

            LocalTime time = LocalTime.parse(timeTable.get(i).get("arrival_time"));
            if (time.isAfter(currentTime) && time.isBefore(currentTime.plusHours(2)))
                continue;

            timeTable.remove(stopTimes.getTable().get(i));
        }

        for (int i = 0; i < timeTable.size(); ++i) {
            String tripID = timeTable.get(i).get("trip_id");
            String routeID = "";

            for (HashMap<String, String> row : tripsTable) {
                if (row.get("trip_id").equals(tripID)) {
                    routeID = row.get("route_id");
                    break;
                }
            }

            timeTable.get(i).put("route_id", routeID);
//            timeTable.get(i).remove("trip_id");
        }

        System.out.println(timeTable);


        System.out.println();

        System.out.println("Hellooo!");


    }
}