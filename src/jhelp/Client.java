/*
 * Class Client
 *
 */
package jhelp;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Client class provides users's interface of the application.
 *
 * @author <strong >S.N. Frantsuzov, 2013</strong>
 * @version 1.0
 */
public class Client extends JFrame implements JHelp {

    private JTextField txtTerm;
    private JTextArea txtDef;
    private final JButton btnNext;
    private final JButton btnPrevous;
    private final JButton btnDelete;
    private final JButton btnEdit;
    private final JButton btnAdd;
    private final JButton btnFind;
    private final JMenuItem mExit;
    private final JMenuItem mFind;
    private final JMenuItem mAdd;
    private final JMenuItem mEdit;
    private final JMenuItem mDelete;
    private final JMenuItem mNext;
    private final JMenuItem mPrevous;
    private final JMenuItem mAbout;
    private final Container cp;
    private final JTabbedPane jtp;
    private final JPanel panel1 = new JPanel();
    /**
     * Static constant for serialization
     */
    public static final long serialVersionUID = 1234;
    /**
     * Private Data object presents informational data.
     */
    private Data data;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    /**
     * Constructor with parameters.
     *
     * @param args Array of {@link String} objects. Each item of this array can
     * define any client's property.
     */
    public Client(String[] args) {
        System.out.println("Client: constructor");
        setTitle("JHelp Client");
        setSize(640, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        addWindowListener(new ClientListener(this));
        ClientListener m = new ClientListener(this);
        panel1.setLayout(null);

        ///panel # 1
        JLabel lblTerm = new JLabel("Term:");
        lblTerm.setBounds(10, 20, 40, 25);
        panel1.add(lblTerm);

        txtTerm = new JTextField();
        txtTerm.setBounds(60, 20, 420, 25);
        panel1.add(txtTerm);

        btnFind = new JButton("Find");
        btnFind.setActionCommand("1");
        btnFind.addActionListener(m);
        btnFind.setBounds(500, 20, 100, 25);
        btnFind.setEnabled(false);
        panel1.add(btnFind);

        JLabel lblDef = new JLabel("Definitions:");
        lblDef.setBounds(10, 50, 80, 25);
        panel1.add(lblDef);

        txtDef = new JTextArea();
        txtDef.setEditable(false);
        txtDef.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        txtDef.setBounds(10, 80, 470, 310);
        txtDef.setLineWrap(rootPaneCheckingEnabled);
        txtDef.setWrapStyleWord(rootPaneCheckingEnabled);
        panel1.add(txtDef);

        btnAdd = new JButton("Add");
        btnAdd.setActionCommand("2");
        btnAdd.addActionListener(m);
        btnAdd.setBounds(500, 80, 100, 25);
        btnAdd.setEnabled(false);
        panel1.add(btnAdd);

        btnEdit = new JButton("Edit");
        btnEdit.setActionCommand("3");
        btnEdit.addActionListener(m);
        btnEdit.setBounds(500, 115, 100, 25);
        btnEdit.setEnabled(false);
        panel1.add(btnEdit);

        btnDelete = new JButton("Delete");
        btnDelete.setActionCommand("4");
        btnDelete.addActionListener(m);
        btnDelete.setBounds(500, 150, 100, 25);
        btnDelete.setEnabled(false);
        panel1.add(btnDelete);

        btnNext = new JButton("Next");
        btnNext.setActionCommand("5");
        btnNext.addActionListener(m);
        btnNext.setBounds(500, 225, 100, 25);
        btnNext.setEnabled(false);
        panel1.add(btnNext);

        btnPrevous = new JButton("Prevous");
        btnPrevous.setActionCommand("6");
        btnPrevous.addActionListener(m);
        btnPrevous.setBounds(500, 260, 100, 25);
        btnPrevous.setEnabled(false);
        panel1.add(btnPrevous);

        JButton btnExit = new JButton("Exit");
        btnExit.setActionCommand("7");
        btnExit.addActionListener(m);
        btnExit.setBounds(500, 365, 100, 25);
        panel1.add(btnExit);

        cp = getContentPane();
        jtp = new JTabbedPane();
        jtp.add("Main", panel1);
        jtp.setBounds(5, 0, 625, 425);
        cp.add(jtp);

        //------------1. JMenuItem      
        mExit = new JMenuItem("Exit");
        mExit.setActionCommand("7");
        mExit.addActionListener(m);

        mFind = new JMenuItem("Find");
        mFind.setActionCommand("1");
        mFind.addActionListener(m);
        mFind.setEnabled(false);

        mAdd = new JMenuItem("Add");
        mAdd.setActionCommand("2");
        mAdd.addActionListener(m);
        mAdd.setEnabled(false);

        mEdit = new JMenuItem("Edit");
        mEdit.setActionCommand("3");
        mEdit.addActionListener(m);
        mEdit.setEnabled(false);

        mDelete = new JMenuItem("Delete");
        mDelete.setActionCommand("4");
        mDelete.addActionListener(m);
        mDelete.setEnabled(false);

        mNext = new JMenuItem("Next");
        mNext.setActionCommand("5");
        mNext.addActionListener(m);
        mNext.setEnabled(false);

        mPrevous = new JMenuItem("Prevous");
        mPrevous.setActionCommand("6");
        mPrevous.addActionListener(m);
        mPrevous.setEnabled(false);

        mAbout = new JMenuItem("About...");
        mAbout.setActionCommand("8");
        mAbout.addActionListener(m);

        //-----------2. JMenu
        JMenu mFile = new JMenu("File");
        mFile.add(mFind);
        mFile.addSeparator();
        mFile.add(mNext);
        mFile.add(mPrevous);
        mFile.addSeparator();
        mFile.add(mExit);
        JMenu meEdit = new JMenu("Edit");
        meEdit.add(mAdd);
        meEdit.add(mEdit);
        meEdit.add(mDelete);
        JMenu mHelp = new JMenu("Help");
        mHelp.add(mAbout);

        //----------3. JMenuBar
        JMenuBar mBar = new JMenuBar();
        mBar.add(mFile);
        mBar.add(meEdit);
        mBar.add(mHelp);

        //----------4. setJMenuBar(bar);
        setJMenuBar(mBar);

        setVisible(true);
    }

    /**
     * Method for application start
     *
     * @param args agrgument of command string
     */
    static public void main(String[] args) {
        Client client = new Client(args);
        if (client.connect(args) == JHelp.OK) {
            client.setFindEnabled();
            client.setAddEnabled();
            client.run();
            client.disconnect();
        } else {
            client.setbtns(false);
            client.setDef("No connection to the server! Don't run Server.java", 1);
        }
    }

    /**
     * Method define main job cycle
     */
    public void run() {
        System.out.println("Client: run");
        while (true) {
            try {
                data = serverMsg();
                if (data.getKey().getItem().equals("@@@disconnect@@@")) {
                    break;
                }
            } catch (NullPointerException ex) {
                setDef("No connection to the server! " + ex.getMessage(), 1);
                setbtns(false);
                break;
            }
            if (data != null) {
                if (data.getOperation() == JHelp.SELECT) {
                    setbtns(true);
                    setPrevous(false);
                    setNext(false);
                    if (data.getValues() != null) {
                        if (data.getValues().length > 0) {
                            setDef(data.getValues()[0].getItem(), 0);
                            if (data.getValues().length > 1) {
                                setNext(true);
                            }
                        } else {
                            setbtns(false);
                            setFindEnabled();
                            setAddEnabled();
                            setDef("", 0);
                            setDef("The term is not found...", 2);
                        }
                    } else {
                        setbtns(false);
                        setFindEnabled();
                        setAddEnabled();
                        setDef("", 0);
                        setDef("The term is not found...", 2);
                        if (data.getKey().getItem().equals("@@@notserverdb@@@")) {
                            setDef("Lost connection to the database!", 1);
                            setbtns(false);
                        }
                    }
                }
            }
        }
    }

    /**
     * Method set connection to default server with default parameters
     *
     * @return error code
     */
    @Override
    public int connect() {
        return JHelp.ERROR;
    }

    /**
     * Method set connection to server with parameters defines by argument
     * <code>args</code>
     *
     * @return error code
     */
    @Override
    public int connect(String[] args) {
        System.out.println("Client: connect");
        try {
            socket = new Socket("localhost", DEFAULT_SERVER_PORT);
            try {
                input = new ObjectInputStream(socket.getInputStream());
                output = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
                setDef("No connection to the server! " + ex.getMessage(), 1);
                return JHelp.ERROR;
            }
        } catch (IOException ex) {
            setDef("No connection to the server! " + ex.getMessage(), 1);
            return JHelp.ERROR;
        }
        return JHelp.OK;
    }

    /**
     * Method gets data from data source
     *
     * @param data initial object (template)
     * @return new object
     */
    @Override
    public Data getData(Data data) {
        System.out.println("Client: getData");
        return null;
    }

    /**
     * Method disconnects client and server
     *
     * @return error code
     */
    @Override
    public int disconnect() {
        System.out.println("Client: disconnect");
        try {
            socket.close();
        } catch (IOException ex) {
            setDef("No connection to the server! " + ex.getMessage(), 1);
            return JHelp.ERROR;
        }
        return JHelp.OK;
    }

    /**
     * Method read input sream from server
     *
     * @return object type {@link jhelp.Data}
     */
    public Data serverMsg() {
        Object obj;
        try {
            try {
                obj = input.readObject();
            } catch (ClassNotFoundException ex) {
                setDef("No connection to the server! Don't run Server.java" + ex.getMessage(), 1);
                return null;
            }
        } catch (IOException ex) {
            setDef("No connection to the server! " + ex.getMessage(), 1);
            return null;
        }
        if (((Data) obj).getKey().getItem().equals("@@@notserverdb@@@")) {
            try {
                socket.close();
            } catch (IOException ex) {
                setDef("No connection to the server! " + ex.getMessage(), 1);
            }
        }
        System.out.println("Term = " + ((Data) obj).getKey().getItem());
        return (Data) obj;
    }

    /**
     * Method set text on JTextArea
     *
     * @param msg
     * @param stat
     */
    public void setDef(String msg, int stat) {
        switch (stat) {
            case 0://normal message
                msg += "\n";
                txtDef.setForeground(null);
                txtDef.setFont(null);
                break;
            case 1://connection error
                msg = "Msg: " + msg + "\n";
                txtDef.setForeground(Color.red);
                txtDef.setFont(new Font(null, Font.ITALIC, 14));
                break;
            case 2://find error
                msg = "Msg: " + msg + "\n";
                txtDef.setForeground(Color.BLUE);
                txtDef.setFont(new Font(null, Font.ITALIC, 14));
                break;
        }
//        txtDef.append(msg);
        txtDef.setText(msg);
    }

    /**
     * Method set enabled or disable buttons and menu
     *
     * @param flg boolean flag
     */
    public void setbtns(boolean flg) {
        btnFind.setEnabled(flg);
        btnAdd.setEnabled(flg);
        btnEdit.setEnabled(flg);
        btnDelete.setEnabled(flg);
        btnNext.setEnabled(flg);
        btnPrevous.setEnabled(flg);
        mFind.setEnabled(flg);
        mAdd.setEnabled(flg);
        mEdit.setEnabled(flg);
        mDelete.setEnabled(flg);
        mNext.setEnabled(flg);
        mPrevous.setEnabled(flg);
    }

    /**
     * Method set enabled or disable button Prevous
     *
     * @param flg boolean flag
     */
    public void setPrevous(boolean flg) {
        btnPrevous.setEnabled(flg);
        mPrevous.setEnabled(flg);
    }

    /**
     * Method set enabled or disable button Next
     *
     * @param flag boolean
     */
    public void setNext(boolean flag) {
        btnNext.setEnabled(flag);
        mNext.setEnabled(flag);
    }

    /**
     * Method set enabled or disable button Find
     */
    public void setFindEnabled() {
        btnFind.setEnabled(true);
        mFind.setEnabled(true);
    }

    /**
     * Method set enabled or disable button Add
     */
    public void setAddEnabled() {
        btnAdd.setEnabled(true);
        mAdd.setEnabled(true);
        txtDef.setEditable(true);
    }

    /**
     * Method show Dialog "About"
     */
    public void showAbout() {
        JOptionPane.showMessageDialog(null, "This is version 2.0 of"
                + " JHelp from Frantsuzov S.", "About",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     *
     * @return {@link java.net.Socket}
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Method send object {@link jhelp.Data} to server
     *
     * @param data type {@link jhelp.Data}
     * @return true if write to output stream is ok and otherwise false
     */
    public boolean clientMsg(Data data) {
        try {
            output.writeObject(data);
        } catch (IOException ex) {
            setDef("No connection to the server! " + ex.getMessage(), 1);
            return false;
        }
        return true;
    }

    /**
     * Method return object type {@link jhelp.Data}
     *
     * @return data of type {@link jhelp.Data}
     */
    public Data getData() {
        return data;
    }

    /**
     * Method returns the definition
     *
     * @return text {@code String}
     */
    public String getDef() {
        return txtDef.getText();
    }

    /**
     * Method returns the term
     *
     * @return text {@link java.lang.String}
     */
    public String getTerm() {
        return txtTerm.getText();
    }
}
