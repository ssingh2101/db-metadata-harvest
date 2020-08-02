package utility;


import model.ConnectionModel;
import org.hornetq.utils.json.JSONException;
import org.hornetq.utils.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sandeep Singh on 05-04-2019.
 */

public class DbUtility {
    public static JSONObject json;
    //public static List<String> schemas;
    public static List<String> tableSchema = new ArrayList<>();

    /**
     * @param connData
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection createConnection(ConnectionModel connData) throws ClassNotFoundException, SQLException {
        if (connData.getDbType().equalsIgnoreCase("oracle")) {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String dbUrl = "jdbc:" + connData.getDbType().toLowerCase() + ":" + "thin" + ":@" + connData.getHost() + ":" + connData.getPort() + ":" + connData.getInstance();
            return DriverManager.getConnection(dbUrl, connData.getUserName(), connData.getPassword());
        } else if (connData.getDbType().equalsIgnoreCase("derby")) {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String dbUrl = ("jdbc:" + connData.getDbType().toLowerCase() + "://" + connData.getHost() + ":" + connData.getPort() + "/" + connData.getInstance() + ";" + "ssl=basic");
            return DriverManager.getConnection(dbUrl);
        }
        return null;
    }

    /**
     * @param databaseId
     * @return
     * @throws ParseException
     * @throws JSONException
     * @throws IOException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static Connection createConnection(String databaseId) throws ParseException, JSONException, IOException, SQLException, ClassNotFoundException {
        JSONObject dbJsonObject = JsonUtility.getConnectionData(databaseId);

        if (dbJsonObject != null) {
            ConnectionModel connData = new ConnectionModel(
                    dbJsonObject.getString("connName"),
                    dbJsonObject.getString("host"),
                    dbJsonObject.getString("dbType"),
                    dbJsonObject.getString("port"),
                    dbJsonObject.getString("instance"),
                    dbJsonObject.getString("userName"),
                    dbJsonObject.getString("password"));

            return createConnection(connData);
        }
        return null;
    }

    /**
     * @param conn
     * @return
     */
    public static List<String> getSchema(Connection conn) throws SQLException, ParseException, IOException, JSONException {

        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getSchemas("null", "%");
        List<String> schemaList = new ArrayList<>();

        while (rs.next()) {
            schemaList.add(rs.getString("TABLE_SCHEM"));
        }
        rs.close();
        return schemaList;

    }

    /**
     * @param conn
     * @return
     * @throws SQLException
     */

    public static List<String> getTable(Connection conn, String schemaId) throws SQLException, ParseException, IOException, JSONException {

        String schemaName = null;
        JSONObject schemaData = JsonUtility.getSchemaData(schemaId);
        if (schemaData != null) {
            schemaName = schemaData.getString("Label");
        }

        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getTables(null, schemaName, "%", null);
        List<String> tables = new ArrayList<>();

        while (rs.next()) {
            tables.add(rs.getString("TABLE_NAME"));
            tableSchema.add(rs.getString(2));
                 /*1: none
                   2: schema
                   3: table name
                   4: table type (TABLE, VIEW)*/
        }
        rs.close();
        return tables;
    }

}
