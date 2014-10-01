/*
 * Class JHelp
 *
 */
package jhelp;

/**
 * Interface for implementation by <code>Client, MClient, Server</code> and
 * <code>ServerDb</code> classes.
 *
 * @author <strong >S.N. Frantsuzov, 2013</strong>
 * @version 1.0
 * @see jhelp.Server
 * @see jhelp.ServerDb
 */
public interface JHelp {

    /**
     * Present situation when any method complets properly
     */
    public static final int OK = 0;
    /**
     * Present situation when any method make an error
     */
    public static final int ERROR = -1;
    /**
     * Present situation when any server is connected and can process requests.
     */
    public static final int READY = 1;
    /**
     * Present situation when any server is disconnected
     */
    public static final int DISCONNECT = -2;
    /**
     * Default value for <code>item</code> attribute of {@link jhelp.Item}
     * object, if this object is used as <code>key</code> attribute of
     * {@link jhelp.Data} object.
     */
    public static final String DEFAULT_KEY = "Unknown key";
    /**
     * Default value for <code>item</code> attribute of {@link jhelp.Item}
     * object, if this object is used as item of <code>values</code> array of
     * {@link jhelp.Data} object.
     */
    public static final String DEFAULT_VALUE = "Unknown value";
    /**
     * Default value for <code>values</code> attribute of {@link jhelp.Data}
     * object.
     */
    public static final Item[] DEFAULT_VALUES = {new Item()};
    /**
     * Defines SELECT operation for database
     */
    public static final int SELECT = 2;
    /**
     * Defines INSERT operation for database
     */
    public static final int INSERT = 4;
    /**
     * Defines UPDATE operation for database
     */
    public static final int UPDATE = 8;
    /**
     * Defines DELETE operation for database
     */
    public static final int DELETE = 16;
    /**
     * Defines initial state for {@link jhelp.Item} object. The state can
     * replaced by {@link #INSERT}, {@link #UPDATE} or {@link #DELETE} values.
     */
    public static final int ORIGIN = 32;
    /**
     * Defines default port for {@link jhelp.Server} object.
     */
    public static final int DEFAULT_SERVER_PORT = 12345;
    /**
     * Defines default port for {@link jhelp.ServerDb} object.
     */
    public static final int DEFAULT_DATABASE_PORT = 12346;

    /**
     * Method connect any object to a server using default values for all
     * connection parameters.
     *
     * @return connect result code. It can equals {@link #READY} or
     * {@link #ERROR}.
     * @throws java.lang.Exception
     * @since 1.0
     */
    int connect() throws Exception;

    /**
     * Method connect any object to a server
     *
     * @param args array of String objects presents connection parameters, for
     * example server host, server port, user's login and user's password
     * @return connect result code. It can equals {@link #READY} or
     * {@link #ERROR}.
     * @throws java.lang.Exception
     * @since 1.0
     */
    int connect(String[] args) throws Exception; //connect to server and initialize it;

    /**
     * Method returns result of client request to a database.
     *
     * @param data object of {@link jhelp.Data} type with request to database.
     * @return object of {@link jhelp.Data} type with results of request to a
     * database.
     * @throws java.lang.Exception
     * @see Data
     * @since 1.0
     */
    Data getData(Data data) throws Exception; // send him termin and getting manual in Data format

    /**
     * Method disconnects any object from a server
     *
     * @return disconnect result. Method returns {@link #DISCONNECT} value, if
     * the process ends successfully. Othewise the method returns error code,
     * for example {@link #ERROR}.
     * @throws java.lang.Exception
     * @see jhelp.JHelp#DISCONNECT
     * @since 1.0
     */
    int disconnect() throws Exception; //anticonnect 
}
