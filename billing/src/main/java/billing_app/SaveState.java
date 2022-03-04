package billing_app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Date;

/*This class aims to provide methods for saving and opening the state of the javaprogram  */

public class SaveState {
    
    Path currentPath;
    Date today = new Date();

    public SaveState() {
        currentPath = FileSystems.getDefault().getPath(System.getProperty("user.dir") + "/billing/store/instance");
    }

    public SaveState(Path optionalPath) {
        currentPath = optionalPath;
    }

    public void saveCurrentState(Path currentPath) {
        File currentState = new File(currentPath.toString() + "/" + today + ".txt");
        try {
            currentState.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            FileWriter writeCurrentState = new FileWriter(currentPath.toString() + "/" + today + ".txt");
            writeCurrentState.write("START,ITEM\n");
            /*Save item data*/
            
            writeCurrentState.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        SaveState ny = new SaveState();
        ny.saveCurrentState(ny.currentPath);
    }
}
