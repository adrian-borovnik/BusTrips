import javafx.scene.control.TableCell;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CSV {
    private ArrayList<HashMap<String, String>> table;

    private static boolean isInArray(String[] array, String item) {
        for (String element : array) {
            if (element.equals(item)) return true;
        }
        return false;
    }

    public CSV(String fileName, String[] keys) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(fileName), "UTF-8");
//        scanner.useDelimiter("[^a-zA-Z0-9]+");

        String[] columns = scanner.nextLine().replace("\uFEFF", "").split(",", -1);

        table = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String[] row = scanner.nextLine().replace("\uFEFF", "").split(",", -1);
            HashMap<String, String> map = new HashMap<>();
            for (int i = 0; i < columns.length; ++i) {
                if(!isInArray(keys, columns[i])) continue;
                map.put(columns[i], row[i]);
            }
            table.add(map);
        }
    }

    public ArrayList<HashMap<String, String>> getTable() {
        return table;
    }
}
