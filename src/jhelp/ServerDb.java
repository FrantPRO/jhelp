/*
 * Class ServerDb
 *
 */
package jhelp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class presents server directly working with database. The complete
 * connection string should take the form of:<br>
 * <code><pre>
 *     jdbc:subprotocol://servername:port/datasource:user=username:password=password
 * </pre></code> Sample for using MS Access data source:<br>
 * <code><pre>
 *  private static final String accessDBURLPrefix
 *      = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
 *  private static final String accessDBURLSuffix
 *      = ";DriverID=22;READONLY=false}";
 *  // Initialize the JdbcOdbc Bridge Driver
 *  try {
 *         Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
 *      } catch(ClassNotFoundException e) {
 *         System.err.println("JdbcOdbc Bridge Driver not found!");
 *      }
 *
 *  // Example: method for connection to a Access Database
 *  public Connection getAccessDBConnection(String filename)
 *                           throws SQLException {
 *       String databaseURL = accessDBURLPrefix + filename + accessDBURLSuffix;
 *       return DriverManager.getConnection(databaseURL, "", "");
 *   }
 * </pre></code>
 *
 * @author <strong >S.N. Frantsuzov, 2013</strong>
 */
public class ServerDb implements JHelp {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Connection connect;
    private PreparedStatement psSelect, psInsertTerm, psInsertDef, psMaxTerm,
            psMaxDef, psUpdateTerm, psUpdateDef, psDeleteTerm, psDeleteDef,
            psFindTerm, psFindDef;

    /**
     * Creates a new instance of <code>ServerDb</code> with default parameters.
     * Default parameters are:<br> <ol> <li><code>ServerDb</code> host is
     * &laquo;localhost&raquo;;</li> <li>{@link java.net.ServerSocket} is opened
     * on {@link jhelp.JHelp#DEFAULT_DATABASE_PORT};</li> </ol>
     *
     * @throws java.io.IOException
     */
    public ServerDb() throws Exception {
        this(DEFAULT_DATABASE_PORT);
        System.out.println("SERVERDb: default constructor");
    }

    /**
     * Constructor creates new instance of <code>ServerDb</code>.
     *
     * @param port defines port for {@link java.net.ServerSocket} object.
     * @throws java.io.IOException
     */
    public ServerDb(int port) throws Exception {
        System.out.println("SERVERDb: constructor int");
        if (port < 0 || port > 65535) {
            throw new IOException("Port value out of range");
        }
        serverSocket = new ServerSocket(port);
    }

    /**
     * Constructor creates new instance of <code>ServerDb</code>.
     *
     * @param args array of {@link java.lang.String} type contains connection
     * parameters.
     */
    public ServerDb(String[] args) {
        System.out.println("SERVERDb: constructor string");
    }

    /**
     * Start method for <code>ServerDb</code> application.
     *
     * @param args array of {@link java.lang.String} type contains connection
     * parameters.
     */
    public static void main(String[] args) {
        System.out.println("SERVERDb: main");
        ServerDb serverDb;
        try {
            serverDb = new ServerDb();
            args = new String[3];
            args[0] = "jdbc:derby://localhost:1527/JHelpDB";
            args[1] = "adm";
            args[2] = "adm";
            if (serverDb.connect(args) != JHelp.READY) {
                throw new IOException("Check connection with data base");
            }
            serverDb.run();
            serverDb.disconnect();
        } catch (IOException ex) {
            System.err.println(ex.getMessage() + "---main1");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage() + "---main2");
        } catch (Exception ex) {
            System.err.println(ex.getMessage() + "---main3");
        }
    }

    /**
     * Method defines job cycle for client request processing.
     */
    private void run() throws Exception {
        System.out.println("SERVERDb: run");
        Data d = null;
        String Select
                = "select\n"
                + "t.ID as idt,term,\n"
                + "d.ID as idd,definition\n"
                + "from tblterms t\n"
                + "left join tbldefinitions d on d.TERM_ID=t.ID\n"
                + "where t.TERM=?\n"
                + "order by t.TERM,d.DEFINITION";
        String InsertTerm
                = "insert into tblterms (id,term)\n"
                + "values (?,?)";
        String InsertDef
                = "insert into tbldefinitions (id,definition,term_id)\n"
                + "values (?,?,?)";
        String MaxTerm
                = "select max(id) idmax from tblterms";
        String MaxDef
                = "select max(id) idmax from tbldefinitions";
        String UpdateTerm
                = "update tblterms set term=? where id=?";
        String UpdateDef
                = "update tbldefinitions set definition=? where id=?";
        String DeleteTerm
                = "delete from tblterms where id=?";
        String DeleteDef
                = "delete from tbldefinitions where id=?";
        String FindTerm
                = "select count(*) as count from tblterms where term=?";
        String FindDef
                = "select count(*) as count from tbldefinitions d left join tblterms t on t.ID=d.TERM_ID where term=?";

        psSelect = connect.prepareStatement(Select);
        psInsertTerm = connect.prepareStatement(InsertTerm);
        psInsertDef = connect.prepareStatement(InsertDef);
        psMaxTerm = connect.prepareStatement(MaxTerm);
        psMaxDef = connect.prepareStatement(MaxDef);
        psUpdateTerm = connect.prepareStatement(UpdateTerm);
        psUpdateDef = connect.prepareStatement(UpdateDef);
        psDeleteTerm = connect.prepareStatement(DeleteTerm);
        psDeleteDef = connect.prepareStatement(DeleteDef);
        psFindTerm = connect.prepareStatement(FindTerm);
        psFindDef = connect.prepareStatement(FindDef);

        while (true) {
            try {
                clientSocket = serverSocket.accept();
            } catch (UnknownHostException ex) {
                throw ex;
            }
            if (connect() != JHelp.OK) {
                break;
            }
            Object obj = null;
            while (true) {
                try {
                    try {
                        obj = input.readObject();
                        d = ((Data) obj);
                    } catch (ClassNotFoundException ex) {
                        throw ex;
                    }
                    System.out.println("Question: " + (((Data) obj).getKey().getItem()));
                    if (d.getKey().getItem().equals("@@@exit@@@")) {

                        output.writeObject(new Data(new Item("@@@disconnect@@@")));
                    } else {
                        if (d.getOperation() == JHelp.INSERT) {
                            try {
                                psFindTerm.setString(1, d.getKey().getItem());
                                ResultSet rs;
                                rs = psFindTerm.executeQuery();
                                rs.next();
                                if (rs.getInt("count") == 0) {
                                    insertTerm(d);
                                }
                                insertDef(d);
                            } catch (SQLException ex) {
                                throw ex;
                            }
                        } else if (d.getOperation() == JHelp.DELETE) {
                            deleteDef(d);
                            try {
                                psFindDef.setString(1, d.getKey().getItem());
                                ResultSet rs;
                                rs = psFindDef.executeQuery();
                                rs.next();
                                if (rs.getInt("count") == 0) {
                                    deleteTerm(d);
                                }
                            } catch (SQLException ex) {
                                throw ex;
                            }
                        } else if (d.getOperation() == JHelp.UPDATE) {
                            updateDef(d);
                            updateTerm(d);
                        }
                        output.writeObject(getData(d));
                    }
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
    }

    /**
     *
     * @return error code. The method returns {@link JHelp#OK} if streams are
     * opened successfully, otherwise the method returns {@link JHelp#ERROR}.
     */
    @Override
    public int connect() throws Exception {
        System.out.println("SERVERDb: connect1");
        try {
            input = new ObjectInputStream(clientSocket.getInputStream());
            output = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            throw ex;
        }
        return JHelp.OK;
    }

    /**
     * Method sets connection to database and create
     * {@link java.net.ServerSocket} object for waiting of client's connection
     * requests.
     *
     * @return error code. Method returns {@link jhelp.JHelp#READY} in success
     * case. Otherwise method return {@link jhelp.JHelp#ERROR} or error code.
     */
    @Override
    public int connect(String[] args) throws Exception {
        System.out.println("SERVERDb: connect2");
        String url = args[0];
        String user = args[1];
        String password = args[2];
        try {
            connect = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            throw ex;
        }
        return JHelp.READY;
    }

    /**
     * Method returns result of client request to a database.
     *
     * @param data object of {@link jhelp.Data} type with request to database.
     * @return object of {@link jhelp.Data} type with results of request to a
     * database.
     * @see Data
     * @since 1.0
     */
    @Override
    public Data getData(Data data) throws Exception {
        ResultSet rs;
        ArrayList<Item> tmp = new ArrayList();
        try {
            psSelect.setString(1, data.getKey().getItem());
            rs = psSelect.executeQuery();
            while (rs.next()) {
                data.setKey(new Item(rs.getInt("idt"), data.getKey().getItem(), JHelp.ORIGIN));
                tmp.add(new Item(rs.getInt("idd"), rs.getString("definition"), JHelp.ORIGIN));
            }
            data.setOperation(JHelp.SELECT);
            data.setValues(tmp.toArray(new Item[0]));
        } catch (SQLException ex) {
            throw ex;
        }
        System.out.println("Answer: " + data.getOperation());
        return data;
    }

    /**
     * Method disconnects <code>ServerDb</code> object from a database and
     * closes {@link java.net.ServerSocket} object.
     *
     * @return disconnect result. Method returns {@link #DISCONNECT} value, if
     * the process ends successfully. Othewise the method returns error code,
     * for example {@link #ERROR}.
     * @throws java.lang.Exception
     * @see jhelp.JHelp#DISCONNECT
     * @since 1.0
     */
    @Override
    public int disconnect() throws Exception {
        System.out.println("SERVERDb: disconnect");
        try {
            serverSocket.close();
        } catch (IOException ex) {
            throw ex;
        }
        try {
            connect.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return JHelp.DISCONNECT;
    }

    private boolean insertTerm(Data data) throws Exception {
        try {
            ResultSet rs;
            rs = psMaxTerm.executeQuery();
            rs.next();
            psInsertTerm.setInt(1, rs.getInt("idmax") + 1);
            psInsertTerm.setString(2, data.getKey().getItem());
            return psInsertTerm.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw ex;
        }
    }

    private boolean insertDef(Data data) throws Exception {
        try {
            ResultSet rs;
            rs = psMaxDef.executeQuery();
            rs.next();
            psInsertDef.setInt(1, rs.getInt("idmax") + 1);
            psInsertDef.setString(2, data.getValue(0).getItem());
            psSelect.setString(1, data.getKey().getItem());
            rs = psSelect.executeQuery();
            rs.next();
            psInsertDef.setInt(3, rs.getInt("idt"));
            return psInsertDef.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw ex;
        }
    }

    private boolean deleteDef(Data data) throws Exception {
        try {
            psDeleteDef.setInt(1, data.getValues()[0].getId());
            return psDeleteDef.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw ex;
        }
    }

    private boolean deleteTerm(Data data) throws Exception {
        try {
            psDeleteTerm.setInt(1, data.getKey().getId());
            return psDeleteTerm.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw ex;
        }
    }

    private boolean updateDef(Data data) throws Exception {
        try {
            psUpdateDef.setString(1, data.getValue(0).getItem());
            psUpdateDef.setInt(2, data.getValue(0).getId());
            return psUpdateDef.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw ex;
        }
    }

    private boolean updateTerm(Data data) throws Exception {
        try {
            psUpdateTerm.setString(1, data.getKey().getItem());
            psUpdateTerm.setInt(2, data.getKey().getId());
            return psUpdateTerm.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw ex;
        }
    }
}
