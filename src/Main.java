import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        CSV stops = new CSV("gtfs/stops.txt");
        System.out.println(stops.getColumn("stop_code"));
    }
}