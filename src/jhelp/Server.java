/*
 * Class Server
 *
 */
package jhelp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class sets a network connection between end client's objects of
 * {@link jhelp.Client} type and single {@link jhelp.ServerDb} object.
 *
 * @author <strong >S.N. Frantsuzov, 2013</strong>
 * @version 1.0
 * @see jhelp.Client
 * @see jhelp.ClientThread
 * @see jhelp.ServerDb
 */
public class Server implements JHelp {

    /**
     * This method creat connection ServerSocket at the class
     * {@link jhelp.Server}
     *
     * @see java.net.ServerSocket
     */
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket dbSocket;

    /**
     * Creates a new instance of Server
     *
     * @throws java.io.IOException
     */
    public Server() throws IOException {
        this(DEFAULT_SERVER_PORT, DEFAULT_DATABASE_PORT);
        System.out.println("SERVER: Default Server Constructed");
    }

    /**
     *
     * @param port
     * @param dbPort
     * @throws java.io.IOException
     */
    public Server(int port, int dbPort) throws IOException {
        System.out.println("SERVER: Server Constructed");
        if (port < 0 || port > 65535 || dbPort < 0 || dbPort > 65535) {
            throw new IOException("Port value out of range");
        }
        serverSocket = new ServerSocket(port);
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("SERVER: main");
        Server server;
        try {
            server = new Server();
            if (server.connect(args) != JHelp.OK) {
                throw new IOException("Check connection with ServerDb");
            }
            server.run();
            server.disconnect();
        } catch (IOException ex) {
            System.err.println(ex.getMessage() + "---main1");
        } catch (Exception ex) {
            System.err.println(ex.getMessage() + "---main2");
        }
    }

    /**
     * This method creat object of class {@link jhelp.ClientThread} and listen
     * port of connection
     */
    private synchronized void run() throws Exception {
        System.out.println("SERVER: run");
        while (true) {
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException ex) {
                throw ex;
            }
            ClientThread clT = new ClientThread(this, clientSocket);
            Thread t = new Thread(clT);
            t.start();
        }
    }

    /**
     * The method sets connection to database ({@link jhelp.ServerDb} object)
     * and create {@link java.net.ServerSocket} object for waiting of client's
     * connection requests. This method uses default parameters for connection.
     *
     * @return error code. The method returns {@link JHelp#OK} if streams are
     * successfully opened, otherwise the method returns {@link JHelp#ERROR}.
     */
    @Override
    public int connect() throws Exception {
        System.out.println("SERVER: connect1");
        return OK;
    }

    /**
     * The method sets connection to database ({@link jhelp.ServerDb} object)
     * and create {@link java.net.ServerSocket} object for waiting of client's
     * connection requests.
     *
     * @param args specifies properties of connection.
     * @return error code. The method returns {@link JHelp#OK} if connection are
     * openeds uccessfully, otherwise the method returns {@link JHelp#ERROR}.
     */
    @Override
    public int connect(String[] args) throws Exception {
        System.out.println("SERVER: connect2");
        try {
            dbSocket = new Socket("localhost", DEFAULT_DATABASE_PORT);
            try {
                output = new ObjectOutputStream(dbSocket.getOutputStream());
                input = new ObjectInputStream(dbSocket.getInputStream());
            } catch (IOException ex) {
                throw ex;
            }
        } catch (IOException ex) {
            throw ex;
        }
        return OK;
    }

    /**
     * Transports initial {@link Data} object from {@link ClientThread} object
     * to {@link ServerDb} object and returns modified {@link Data} object to
     * {@link ClientThread} object.
     *
     * @param data Initial {@link Data} object which was obtained from client
     * application.
     * @return modified {@link Data} object
     */
    @Override
    public Data getData(Data data) {
        System.out.println("SERVER:getData");
        return null;
    }

    /**
     * The method closes connection with database.
     *
     * @return error code. The method returns {@link JHelp#OK} if a connection
     * with database ({@link ServerDb} object) closed successfully, otherwise
     * the method returns {@link JHelp#ERROR} or any error code.
     */
    @Override
    public int disconnect() {
        System.out.println("SERVER: disconnect");
        return OK;
    }

    public ObjectInputStream getinput() {
        return input;
    }

    public ObjectOutputStream getoutput() {
        return output;
    }
}
