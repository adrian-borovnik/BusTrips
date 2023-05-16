import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Main {

    private static boolean isInArray(ArrayList<String> array, String item) {
        for (String element : array) {
            if (element.equals(item)) return true;
        }
        return false;
    }

    public static void main(String[] args) throws FileNotFoundException {

        if (args.length != 3) {
            System.out.println("Invalid number of arguments.");
            return;
        }

        String stopID = args[0];
        int numOfBuses = Integer.parseInt(args[1]);

        if(numOfBuses < 1) {
            System.out.println("Choose a positive number of buses displayed.");
            return;
        }

        String mode = args[2];

        if(!(mode.equals("absolute") || mode.equals("relative"))){
            System.out.println("Invalid mode specified. Choose between \"absolute\" and \"relative\"");
            return;
        }

        CSV stopTimes = new CSV("gtfs/stop_times.txt", new String[]{"trip_id", "stop_id", "arrival_time"});
        ArrayList<HashMap<String, String>> timeTable = stopTimes.getTable();

        boolean doesStopExist = false;
        for(HashMap<String, String> row : timeTable){
            if(row.get("stop_id").equals(stopID)){
                doesStopExist = true;
                break;
            }
        }

        if(!doesStopExist){
            System.out.println("There are no buses arriving at a stop with ID " + stopID + ".");
            return;
        }

        CSV stops = new CSV("gtfs/stops.txt", new String[]{"stop_id", "stop_name"});
        ArrayList<HashMap<String, String>> stopsTable = stops.getTable();

        CSV trips = new CSV("gtfs/trips.txt", new String[]{"trip_id", "route_id"});
        ArrayList<HashMap<String, String>> tripsTable = trips.getTable();


        timeTable.sort((o1, o2) -> {
            LocalTime time1 = LocalTime.parse(o1.get("arrival_time"));
            LocalTime time2 = LocalTime.parse(o2.get("arrival_time"));

            return time1.compareTo(time2);
        });

        LocalTime currentTime = LocalTime.now();
//        String currentTimeString = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

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

        for (HashMap<String, String> tableRow : timeTable) {
            String tripID = tableRow.get("trip_id");
            String routeID = "";

            for (HashMap<String, String> row : tripsTable) {
                if (row.get("trip_id").equals(tripID)) {
                    routeID = row.get("route_id");
                    break;
                }
            }

            tableRow.put("route_id", routeID);
//            timeTable.get(i).remove("trip_id");
        }

        for (HashMap<String, String> row : stopsTable) {
            if (row.get("stop_id").equals(stopID)) {
                System.out.println(row.get("stop_name"));
                break;
            }
        }

        ArrayList<String> routes = new ArrayList<>();
        for (int i = 0; i < timeTable.size(); ++i) {
            String routeID = timeTable.get(i).get("route_id");
            if (isInArray(routes, routeID)) continue;

            routes.add(routeID);
            System.out.print(routeID + ": ");

            for (HashMap<String, String> row : timeTable) {

                if (row.get("route_id").equals(routeID)) {
                    LocalTime arrivalTime = LocalTime.parse(row.get("arrival_time"));
                    arrivalTime = LocalTime.of(arrivalTime.getHour(), arrivalTime.getMinute());

                    if (mode.equals("absolute"))
                        System.out.print(arrivalTime + ", ");
                    else if (mode.equals("relative"))
                        System.out.print(currentTime.until(arrivalTime, ChronoUnit.MINUTES) + "min, ");

                }
            }

            System.out.println();
            if (routes.size() == numOfBuses) break;

        }

        System.out.println();
    }
}