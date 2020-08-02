package model;

/**
 * Created by Sandeep Singh on 05-04-2019.
 */

public class ConnectionModel {
    private String connectionName;
    private String host;
    private String dbType;
    private String port;
    private String instance;
    private String userName;
    private String password;

    /**
     * @param connectionName
     * @param host
     * @param dbType
     * @param port
     * @param instance
     * @param userName
     * @param password
     */

    public ConnectionModel(String connectionName, String host, String dbType, String port, String instance, String userName, String password) {

        if (host != null && !host.isEmpty()
                && dbType != null && !dbType.isEmpty()
                && port != null && !port.isEmpty()
                && instance != null && !instance.isEmpty()
                && userName != null && !userName.isEmpty()
                && password != null && !password.isEmpty()
        ) {
            this.connectionName = connectionName;
            this.host = host;
            this.dbType = dbType;
            this.port = port;
            this.instance = instance;
            this.userName = userName;
            this.password = password;
        } else {
            throw new IllegalArgumentException("Required parameters missing");
        }
    }

    public String getConnectionName() throws Exception {
        return connectionName;
    }


    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
