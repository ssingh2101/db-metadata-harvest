package services;

import model.ConnectionModel;
import org.hornetq.utils.json.JSONArray;
import org.hornetq.utils.json.JSONException;
import org.hornetq.utils.json.JSONObject;
import org.json.simple.parser.ParseException;
import utility.DbUtility;
import utility.JsonUtility;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static utility.JsonUtility.createJson;

/**
 * Created by Sandeep Singh on 05-04-2019.
 */
@Path("/v0")
public class DbService {
    public static JSONObject schemaNameObj = null;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/databases")
    public Response listDatabases() {
        System.out.println("GET /databases called");
        try {
            JSONObject connections = JsonUtility.readJson();

            if (connections != null) {
                return Response.status(200).entity(connections.toString(4)).build();
            }
            JSONObject noDataObject = new JSONObject();
            noDataObject.put("errorCode:", 500);
            noDataObject.put("errorMessage:", "No data found.");
            return Response.status(404).entity(noDataObject.toString(4)).build();

        } catch (Exception e) {
            System.out.println(e);
        }
        return Response.status(500).build();
    }

    /**
     * @param connName
     * @param host
     * @param dbType
     * @param port
     * @param instance
     * @param userName
     * @param password
     * @param testFlag
     * @return
     * @throws JSONException
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/databases/")
    public Response createDatabases(@FormParam("connName") String connName,
                                    @FormParam("host") String host,
                                    @FormParam("dbType") String dbType,
                                    @FormParam("port") String port,
                                    @FormParam("instance") String instance,
                                    @FormParam("userName") String userName,
                                    @FormParam("password") String password,
                                    @QueryParam("test") boolean testFlag) throws JSONException {

        System.out.println("POST /databases called");
        System.out.println("connName = " + connName);
        System.out.println("host = " + host);
        System.out.println("dbType = " + dbType);
        System.out.println("port = " + port);
        System.out.println("instance = " + instance);
        System.out.println("userName = " + userName);
        try {
            if (connName.isEmpty() || host.isEmpty() || dbType.isEmpty() || port.isEmpty() || instance.isEmpty() || userName.isEmpty() || password.isEmpty()) {
                JSONObject requiredParameterMissing = new JSONObject();
                requiredParameterMissing.put("errorCode", 406);
                requiredParameterMissing.put("errorMessage", "Required parameters missing.");
                return Response.status(406).entity(requiredParameterMissing.toString(4)).build();
            }

            JSONObject connections = JsonUtility.readJson();
            if (connections != null) {
                JSONArray connArr = connections.getJSONArray("Connections");
                for (int i = 0; i < connArr.length(); i++) {
                    JSONObject dbObject = connArr.getJSONObject(i);
                    String newHost = dbObject.getString("host");
                    String newDbType = dbObject.getString("dbType");
                    String newPort = dbObject.getString("port");
                    String newInstance = dbObject.getString("instance");
                    String newConnName = dbObject.getString("connName");
                    if (newHost.equals(host) && newDbType.equals(dbType) && newInstance.equals(instance) && newPort.equals(port) && newConnName.equals(connName)) {
                        JSONObject duplicateConnObject = new JSONObject();
                        duplicateConnObject.put("errorCode:", 409);
                        duplicateConnObject.put("errorMessage:", "Connection already exists.");
                        return Response.status(409).entity(duplicateConnObject.toString(4)).build();
                    }
                    if (newConnName.equals(connName) && newDbType.equals(dbType)) {
                        JSONObject duplicateConnObject = new JSONObject();
                        duplicateConnObject.put("errorCode:", 409);
                        duplicateConnObject.put("errorMessage:", "Connection with same name exists.");
                        return Response.status(409).entity(duplicateConnObject.toString(4)).build();
                    }
                }
            }
            ConnectionModel connData = new ConnectionModel(connName, host, dbType, port, instance, userName, password);

            Connection connection = DbUtility.createConnection(connData);
            if (connection != null) {
                if (testFlag) {
                    JSONObject connObject = new JSONObject();
                    connObject.put("Status", "Success");
                    connObject.put("Message", "Connection to the database is successful.");
                    return Response.status(200).entity(connObject.toString(4)).build();
                } else {
                    JSONObject json = createJson(connData);

                    JSONObject connObject = new JSONObject();
                    connObject.put("Status", 200);
                    connObject.put("Message", "Connection added to the list.");
                    return Response.status(200).entity(connObject.toString(4)).build();
                    //return Response.status(200).entity(json.toString(4)).build();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {

            JSONObject noConnObject = new JSONObject();
            noConnObject.put("errorCode:", 503);
            noConnObject.put("errorMessage:", "Connection failed to database.");
            return Response.status(503).entity(noConnObject.toString(4)).build();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Response.status(500).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("databases/{connectionId}")
    public Response getUserHistory(
            @PathParam("connectionId") String connectionId) throws ParseException, IOException, JSONException {

        JSONObject dbObject = JsonUtility.getConnectionData(connectionId);
        if (dbObject != null) {
            return Response.status(200).entity(dbObject.toString(4)).build();
        } else {
            JSONObject noDataObject = new JSONObject();
            noDataObject.put("errorCode:", 404);
            noDataObject.put("errorMessage:", "No data found.");
            return Response.status(404).entity(noDataObject.toString(4)).build();
        }
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("databases/{connectionId}/schemas")
    public Response schemaNames(
            @PathParam("connectionId") String connectionId) {

        System.out.println("GET /databases/" + connectionId + "/schemas called");
        JSONObject object = null;
        try {
            JSONObject connections = JsonUtility.readJson();
            if (connections != null) {
                JSONArray connArr = connections.getJSONArray("Connections");
                for (int i = 0; i < connArr.length(); i++) {
                    JSONObject dbObject = connArr.getJSONObject(i);
                    String dbId = dbObject.getString("connectionId");
                    if (dbId.equals(connectionId)) {
                        if (dbObject.getString("Schemas") != null && !dbObject.getString("Schemas").isEmpty()) {
                            object = dbObject;
                        } else {
                            Connection connection = DbUtility.createConnection(connectionId);

                            List<String> schema = null;
                            if (connection != null) {
                                schema = DbUtility.getSchema(connection);
                            }
                            object = JsonUtility.jsonForSchema(schema, connectionId);
                        }
                    }
                }
            } else {
                Connection connection = DbUtility.createConnection(connectionId);

                List<String> schema = null;
                if (connection != null) {
                    schema = DbUtility.getSchema(connection);
                }
                object = JsonUtility.jsonForSchema(schema, connectionId);
            }
            return Response.status(200).entity(object.toString(4)).build();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("databases/{connectionId}/schemas/{schemaId}")
    public Response getSchemaName(
            /*@PathParam("connectionId") String connectionId,*/
            @PathParam("schemaId") String schemaId
    ) throws ParseException, IOException, JSONException {
        JSONObject schemaData = JsonUtility.getSchemaData(schemaId);
        if (schemaData != null) {
            return Response.status(200).entity(schemaData.toString(4)).build();
        } else {
            JSONObject noDataObject = new JSONObject();
            noDataObject.put("errorCode:", 404);
            noDataObject.put("errorMessage:", "No schema found.");
            return Response.status(404).entity(noDataObject.toString(4)).build();
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("databases/{connectionId}/schemas/{schemaId}/tables")
    public Response getTables(
            @PathParam("connectionId") String connectionId,
            @PathParam("schemaId") String schemaId
    ) throws ParseException, JSONException, IOException, SQLException, ClassNotFoundException {
        System.out.println("GET /database/connectionId/schemas " + schemaId + "/tables called");
        JSONObject object = null;

        schemaNameObj = JsonUtility.schemaName(schemaId);
        JSONObject connections = JsonUtility.readJson();
        if (connections != null) {

            JSONArray connArr = connections.getJSONArray("Connections");
            for (int i = 0; i < connArr.length(); i++) {
                JSONObject dbObject = connArr.getJSONObject(i);
                String dbId = dbObject.getString("connectionId");
                if (dbId.equals(connectionId)) {
                    JSONArray tableArr = dbObject.getJSONArray("Schemas");
                    for (int j = 0; j < tableArr.length(); j++) {
                        JSONObject tableObject = tableArr.getJSONObject(j);
                        String tableId = tableObject.getString("Id");
                        if (tableId.equals(schemaId)) {
                            if (tableObject.getString("Tables") != null && !tableObject.getString("Tables").isEmpty()) {
                                object = tableObject;
                            } else {
                                Connection connection = DbUtility.createConnection(connectionId);

                                List<String> tables = null;
                                if (connection != null) {
                                    tables = DbUtility.getTable(connection, schemaId);
                                }
                                object = JsonUtility.jsonForTables(tables, schemaId);
                            }


                        }
                    }

                }
            }

        } else {
            Connection connection = DbUtility.createConnection(connectionId);

            List<String> tables = null;
            if (connection != null) {
                tables = DbUtility.getTable(connection, schemaId);
            }
            object = JsonUtility.jsonForTables(tables, schemaId);
        }
        return Response.status(200).entity(object.toString(4)).build();

    }
}
