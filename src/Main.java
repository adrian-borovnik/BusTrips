import sun.util.resources.LocaleData;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        if(args.length != 3) {
            System.out.println("Invalid number of arguments.");
            return;
        }

        String stopID = args[0];
        int numOfBuses = Integer.parseInt(args[1]);
        String mode = args[2];

        String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.println(currentTime);

        CSV stops = new CSV("gtfs/stops.txt", new String[]{"stop_id", "stop_name"});
//        CSV stopTimes = new CSV("gtfs/stop_times.txt");
//        CSV trips = new CSV("gtfs/trips.txt");

        System.out.println(stops.getTable());


        System.out.println();

        System.out.println("Hellooo!");


    }
}