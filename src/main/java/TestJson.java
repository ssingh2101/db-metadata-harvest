import org.hornetq.utils.json.JSONArray;
import org.hornetq.utils.json.JSONException;
import org.hornetq.utils.json.JSONObject;


/**
 * Created by Sandeep Singh on 16-04-2019.
 */
public class TestJson {
    public static void main(String[] args) throws JSONException {
        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        object.put("Name","sandeep");
        object.put("id","45");
        jsonArray.put(object);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Name","Nilesh");
        jsonObject.put("Id","46");

        jsonArray.put(jsonObject);
        JSONObject roootObj = new JSONObject();
        roootObj.put("Record",jsonArray);
        System.out.println(roootObj.toString(4));

        JSONArray connArr = roootObj.getJSONArray("Record");
        for (int i = 0; i < connArr.length(); i++) {
            JSONObject dbObject = connArr.getJSONObject(i);
            String Name = dbObject.getString("Name");
            if (Name.equals("sandeep")) {
                System.out.println(dbObject);
            }
        }

    }
}

/*
    package utility;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.RandomBasedGenerator;
import model.ConnectionModel;
import org.hornetq.utils.json.JSONArray;
import org.hornetq.utils.json.JSONException;
import org.hornetq.utils.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static utility.StorageUtility.writeFile;*/

/**
 * Created by Sandeep Singh on 05-04-2019.
 */
/*
class JsonUtility {
    private static RandomBasedGenerator randomBasedGenerator = Generators.randomBasedGenerator();

    public static JSONObject readJson() throws IOException, ParseException, JSONException {
        File connFile = StorageUtility.getConnFile();
        if (connFile.exists()) {
            org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
            org.json.simple.JSONObject tempObject = (org.json.simple.JSONObject) parser.parse(new FileReader(connFile));
            String jsonString = tempObject.toJSONString();
            JSONObject object = new JSONObject(jsonString);
            JSONArray jsonArray = object.getJSONArray("Connections");
            object.put("Connections", jsonArray);
            return object;
        }
        return null;
    }

    public static JSONObject createJson(ConnectionModel data) throws Exception {

        File connFile = StorageUtility.getConnFile();
        org.json.simple.JSONObject tempObject;
        JSONObject object;
        JSONArray jsonArray;


        if (connFile.exists()) {
            org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
            tempObject = (org.json.simple.JSONObject) parser.parse(new FileReader(connFile));
            String jsonString = tempObject.toJSONString();
            object = new JSONObject(jsonString);
            jsonArray = object.getJSONArray("Connections");
        } else {
            connFile.createNewFile();
            jsonArray = new JSONArray();
            object = new JSONObject();
        }

        UUID uuid = randomBasedGenerator.generate();

        JSONObject dataObj = new JSONObject();
        dataObj.put("connectionId", uuid.toString());
        dataObj.put("connName", data.getConnectionName());
        dataObj.put("host", data.getHost());
        dataObj.put("dbType", data.getDbType());
        dataObj.put("port", data.getPort());
        dataObj.put("instance", data.getInstance());
        dataObj.put("userName", data.getUserName());
        dataObj.put("password", data.getPassword());
        jsonArray.put(dataObj);
        object.put("Connections", jsonArray);

        System.out.println(object);

        writeFile(object, connFile);

        return dataObj;
    }

    public static JSONObject getConnectionData(String databaseId) throws ParseException, JSONException, IOException {
        JSONObject connections = utility.JsonUtility.readJson();
        if (connections != null) {
            JSONArray connArr = connections.getJSONArray("Connections");
            for (int i = 0; i < connArr.length(); i++) {
                JSONObject dbObject = connArr.getJSONObject(i);
                String dbId = dbObject.getString("connectionId");
                if (dbId.equals(databaseId)) {
                    return dbObject;
                }
            }
        }
        return null;
    }

    public static JSONObject jsonForSchema(List<String> schemas) throws JSONException, IOException, ParseException {

        File schemaFile = StorageUtility.getSchemaFile();
        JSONObject rootObject = new JSONObject();
        JSONArray schemaArr = new JSONArray();
        JSONObject schemaObj=null;
        for (String schema : schemas) {
            UUID uuid = randomBasedGenerator.generate();
            schemaObj = new JSONObject();
            schemaObj.put("Id", uuid.toString());
            schemaObj.put("Label", schema);
            schemaArr.put(schemaObj);
        }
        rootObject.put("Schemas", schemaArr);

        if (schemaFile.exists()) {
            org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
            org.json.simple.JSONObject tempObject = (org.json.simple.JSONObject) parser.parse(new FileReader(schemaFile));
            String jsonString = tempObject.toJSONString();
            JSONObject object = new JSONObject(jsonString);
            JSONArray jsonArray = object.getJSONArray("Schemas");
            String label=null;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject dbObject = jsonArray.getJSONObject(i);
                label = dbObject.getString("Label");
            }
            if (label.equals(schemaObj.getString("Label"))) {
                System.out.println("entry present");
            } else {
                writeFile(rootObject, schemaFile);
            }
        }


        //writeFile(rootObject, schemaFile);

        return rootObject;
    }


}

 */
