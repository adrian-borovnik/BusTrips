import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CSV {
    final private String fileName;

    public CSV(String fileName) {
        this.fileName = fileName;
    }

    public ArrayList<String> getColumn(String key) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(fileName), "UTF-8");
//        scanner.useDelimiter("[^a-zA-Z0-9]+");

        String[] columns = scanner.nextLine().replace("\uFEFF", "").split(",", -1);

        int index = -1;
        for(int i = 0; i < columns.length; ++i){
            if(Objects.equals(key, columns[i])){
                index = i;
                break;
            }
        }

        if(index < 0) return null;

        ArrayList<String> column = new ArrayList<String>();

        while(scanner.hasNextLine()){
            String[] row = scanner.nextLine().split(",", -1);
            column.add(row[index]);
        }

        return column;
    }
}
