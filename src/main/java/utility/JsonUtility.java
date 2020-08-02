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

import static utility.StorageUtility.getConnFile;
import static utility.StorageUtility.writeFile;

/**
 * Created by Sandeep Singh on 05-04-2019.
 */
public class JsonUtility {
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

    /*public static JSONObject readJsons(String connId) throws ParseException, JSONException, IOException {
        JSONObject connections = JsonUtility.readJson();
        if (connections != null) {
            JSONArray connArr = connections.getJSONArray("Connections");
            for (int i = 0; i < connArr.length(); i++) {
                JSONObject dbObject = connArr.getJSONObject(i);
                String dbId = dbObject.getString("connectionId");
                if (dbId.equals(connId)) {
                    return dbObject;
                }
            }
        }
        return null;

    }*/


    public static JSONObject createJson(ConnectionModel data) throws Exception {

        org.json.simple.JSONObject tempObject;
        JSONObject object;

        JSONArray jsonArray;

        File connFile = StorageUtility.getConnFile();

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
        dataObj.put("Schemas", "");
        jsonArray.put(dataObj);
        object.put("Connections", jsonArray);

        writeFile(object, connFile);
        return dataObj;
    }

    public static JSONObject getConnectionData(String databaseId) throws ParseException, JSONException, IOException {
        JSONObject connections = JsonUtility.readJson();
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

    public static JSONObject jsonForSchema(List<String> schemas, String connId) throws JSONException, IOException, ParseException {

        File connFile = getConnFile();
        JSONObject schemaObj;
        JSONArray schemaArr = new JSONArray();

        for (String schema : schemas) {
            UUID uuid = randomBasedGenerator.generate();
            schemaObj = new JSONObject();
            schemaObj.put("Id", uuid.toString());
            schemaObj.put("Label", schema);
            schemaObj.put("Tables", "");
            schemaArr.put(schemaObj);
        }

        JSONObject rootObject = new JSONObject();
        rootObject.put("Schemas", schemaArr);

        JSONObject dbObject;
        JSONObject connObj = readJson();
        if (connObj != null) {
            JSONArray connArr = connObj.getJSONArray("Connections");
            for (int i = 0; i < connArr.length(); i++) {
                dbObject = connArr.getJSONObject(i);
                String dbId = dbObject.getString("connectionId");
                if (dbId.equals(connId)) {
                    dbObject.put("Schemas", schemaArr);
                }
            }
        }

        writeFile(connObj, connFile);

        return rootObject;
    }

    public static JSONObject getSchemaData(String schemaId) throws ParseException, JSONException, IOException {
        JSONArray schemaArr;
        JSONObject dbObject;
        JSONObject schemas = readJson();
        if (schemas != null) {
            JSONArray connArr = schemas.getJSONArray("Connections");
            for (int i = 0; i < connArr.length(); i++) {
                JSONObject object = connArr.getJSONObject(i);
                schemaArr = object.getJSONArray("Schemas");
                String id;
                for (int j = 0; j < schemaArr.length(); j++) {
                    dbObject = schemaArr.getJSONObject(j);
                    id = dbObject.getString("Id");
                    if (id.equals(schemaId)) {
                        return dbObject;
                    }
                }
            }
        }
        return null;
    }


    public static JSONObject jsonForTables(List<String> tables, String schemaId) throws JSONException, IOException, ParseException {

        File connFile = getConnFile();

        JSONArray schemaArr;
        JSONObject tableObj;
        JSONArray tableArr = new JSONArray();

        for (String table : tables) {
            tableObj = new JSONObject();
            UUID uuid = randomBasedGenerator.generate();
            tableObj.put("Id", uuid.toString());
            tableObj.put("tableName", table);
            tableArr.put(tableObj);
        }

        JSONObject rootObject = new JSONObject();
        rootObject.put("Tables", tableArr);

        JSONObject dbObject = null;
        JSONObject connObj = readJson();
        if (connObj != null) {
            JSONArray connArr = connObj.getJSONArray("Connections");
            for (int i = 0; i < connArr.length(); i++) {
                JSONObject object = connArr.getJSONObject(i);
                schemaArr = object.getJSONArray("Schemas");
                String id;
                for (int j = 0; j < schemaArr.length(); j++) {
                    dbObject = schemaArr.getJSONObject(j);
                    id = dbObject.getString("Id");
                    if (id.equals(schemaId)) {
                        dbObject.put("Tables", tableArr);
                    }
                }

            }
        }

        writeFile(connObj, connFile);

        return rootObject;



        /*File tableFile = getTableFile();
        JSONObject tableObj;
        JSONObject rootObject = new JSONObject();
        JSONArray tableArr = new JSONArray();


        for (String table : tables) {
            tableObj = new JSONObject();
            UUID uuid = randomBasedGenerator.generate();
            tableObj.put("Id", uuid.toString());
            tableObj.put("tableName", table);
            tableArr.put(tableObj);
        }

        rootObject.put("Table", tableArr);     //DbService.schemaNameObj.getString("Label")

        writeFile(rootObject, tableFile);

        return rootObject;*/
    }

    public static JSONObject schemaName(String schemaId) throws ParseException, IOException, JSONException {
        JSONObject schemaJsonObject = JsonUtility.getSchemaData(schemaId);
        JSONObject object = new JSONObject();
        if (schemaJsonObject != null) {
            object.put("Label", schemaJsonObject.getString("Label"));
        }
        return object;
    }

}
