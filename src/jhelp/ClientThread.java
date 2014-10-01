/*
 * Class ClientThread
 */
package jhelp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class provides a network connection between end client of
 * {@link jhelp.Client} type and {@link jhelp.Server} object. Every object of
 * this class may work in separate thread.
 *
 * @author <strong >S.N. Frantsuzov, 2013</strong>
 * @version 1.0
 * @see jhelp.Client
 * @see jhelp.Server
 */
public class ClientThread implements JHelp, Runnable {

    private final Server server;
    private final Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    /**
     * Creates a new instance of Client
     *
     * @param server reference to {@link Server} object.
     * @param socket reference to {@link java.net.Socket} object for connection
     * with client application.
     */
    public ClientThread(Server server, Socket socket) {
        System.out.println("MClient: constructor");
        this.server = server;
        clientSocket = socket;
    }

    /**
     * The method defines main job cycle for the object.
     *
     */
    @Override
    public void run() {
        System.out.println("MClient: run");
        Object obj = null;
        Data d = null;
        if (connect() == JHelp.OK) {
            while (true) {
                try {
                    try {
                        obj = input.readObject();
                        try {
                            if (server.getoutput() != null) {
                                server.getoutput().writeObject(obj);
                            } else {
                                output.writeObject(new Data(new Item("@@@notserverdb@@@")));
                                break;
                            }
                        } catch (IOException ex) {
                            throw ex;
                            //System.err.println(ex.getMessage() + " //cl1");
                        }
                        d = serverdbMsg();
                    } catch (ClassNotFoundException ex) {
                        //??????????????????????????????????????????????
                        System.err.println(ex.getMessage() + " //cl2");
                    } catch (Exception ex) {
                        //??????????????????????????????????????????????
                        System.err.println(ex.getMessage() + " //cl21");
                    }
                    System.out.println("Question: " + (((Data) obj).getKey().getItem()));
                    if (((Data) obj).getKey().getItem().equals("@@@exit@@@")) {
                        output.writeObject(new Data(new Item("@@@disconnect@@@")));
                        clientSocket.close();
                        break;
                    } else {
                        output.writeObject(d);
                    }
                } catch (IOException ex) {
                    //?????????????????????????????????????????????????????
                    System.err.println(ex.getMessage() + " //cl3");
                    break;
                }
            }
        }
    }

    /**
     * Opens input and output streams for data interchanging with client
     * application. The method uses default parameters.
     *
     * @return error code. The method returns {@link JHelp#OK} if streams are
     * successfully opened, otherwise the method returns {@link JHelp#ERROR}.
     */
    @Override
    public int connect() {
        System.out.println("MClient: connect");
        try {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            input = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException ex) {
            //?????????????????????????????????????????????????????
            System.err.println(ex.getMessage() + " //cl4");
            return JHelp.ERROR;
        }
        return JHelp.OK;
    }

    /**
     * Opens input and output streams for data interchanging with client
     * application. This method uses parameters specified by parameter
     * <code>args</code>.
     *
     * @param args defines properties for input and output streams.
     * @return error code. The method returns {@link JHelp#OK} if streams are
     * successfully opened, otherwise the method returns {@link JHelp#ERROR}.
     */
    @Override
    public int connect(String[] args) {
        System.out.println("MClient: connect");
        return JHelp.OK;
    }

    /**
     * Transports {@link Data} object from client application to {@link Server}
     * and returns modified {@link Data} object to client application.
     *
     * @param data {@link Data} object which was obtained from client
     * application.
     * @return modified {@link Data} object
     */
    @Override
    public Data getData(Data data) {
        System.out.println("Client: getData");
        return null;
    }

    /**
     * The method closes connection with client application.
     *
     * @return error code. The method returns {@link JHelp#OK} if input/output
     * streams and connection with client application was closed successfully,
     * otherwise the method returns {@link JHelp#ERROR}.
     */
    @Override
    public int disconnect() {
        System.out.println("Client: disconnect");
        return JHelp.OK;
    }

    public Data serverdbMsg() throws Exception {
        Object obj = null;
        try {
            try {
                if (server.getinput() != null) {
                    obj = server.getinput().readObject();
                } else {
                    output.writeObject(new Data(new Item("@@@notserverdb@@@")));
                    //break;
                }
            } catch (ClassNotFoundException ex) {
                throw ex;
                //System.err.println(ex.getMessage() + " //cl5");
                //return null;
            }
        } catch (IOException ex) {
            throw ex;
//            System.err.println(ex.getMessage() + " //cl6");
//            return null;
        }
        if (obj != null) {
            System.out.println("Question: " + ((Data) obj).getKey().getItem());
        }
        return (Data) obj;
    }
}
