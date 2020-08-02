package utility;

import org.hornetq.utils.json.JSONException;
import org.hornetq.utils.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Sandeep Singh on 05-04-2019.
 */

public class StorageUtility {
    private static final String CONN_FILE_PATH = "C:\\Users\\Sandeep Singh\\IdeaProjects\\MavenWebApp\\connFile.json";
    private static File connFile;


    public static File getConnFile() {
        if (connFile == null) {
            connFile = new File(CONN_FILE_PATH);
        }
        return connFile;
    }

    public static void writeFile(JSONObject object, File file) throws IOException, JSONException {
        FileWriter jsonFile = new FileWriter(file);
        jsonFile.append(object.toString(4));
        jsonFile.flush();

    }

    /*private static final String SCHEMA_FILE_PATH = "C:\\Users\\Sandeep Singh\\IdeaProjects\\MavenWebApp\\schemaFile.json";
    private static File schemaFile;
    private static final String TABLE_FILE_PATH = "C:\\Users\\Sandeep Singh\\IdeaProjects\\MavenWebApp\\tableFile.json";
    private static File tableFile;*/

    /*public static File getSchemaFile() {
        if (schemaFile == null) {
            schemaFile = new File(SCHEMA_FILE_PATH);
        }
        return schemaFile;
    }

    public static File getTableFile() {
        if (tableFile == null) {
            tableFile = new File(TABLE_FILE_PATH);
        }
        return tableFile;
    }*/
}
