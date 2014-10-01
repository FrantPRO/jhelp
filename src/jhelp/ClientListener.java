/*
 * Class ClientListener
 */
package jhelp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Class defines a process for all events what happens in client form.
 *
 * @author <strong >S.N. Frantsuzov, 2013</strong>
 * @version 1.0
 */
public class ClientListener extends WindowAdapter
        implements ActionListener, TextListener {

    /**
     * Variable to refer to an object of class {@link jhelp.Client}
     */
    private final Client client;
    /**
     * Variable to refer to an object of class {@link jhelp.Data}
     */
    private Data d;
    /**
     * Variable current position of count
     */
    private int current;

    /**
     * Single constructor of the class.
     *
     * @param client references to client form
     */
    public ClientListener(Client client) {
        this.client = client;
    }

    /**
     * Method for processing of {@link java.awt.event.ActionEvent} events.
     *
     * @param e reference to {@link java.awt.event.ActionEvent} event what
     * happens
     * @see java.awt.event.ActionEvent
     * @see java.awt.event.ActionListener
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        d = client.getData();
        switch (e.getActionCommand()) {
            case "1": //find
                System.out.println("Find");
                if (!sendData(new Data(new Item(client.getTerm())))) {
                    client.setDef("Request is not sent...", 1);
                } else {
                    current = 0;
                }
                break;
            case "2": //add
                System.out.println("add");
                if ((!client.getTerm().equals("")) && (!client.getDef().equals(""))) {
                    Item[] tmp = new Item[1];
                    tmp[0] = new Item(client.getDef());
                    System.out.println("1");
                    if (!sendData(new Data(JHelp.INSERT, new Item(client.getTerm()), tmp))) {
                        client.setDef("Request is not sent...", 1);
                    } else {
                        current = 0;
                    }
                }
                break;
            case "3": //edit
                System.out.println("edit");
                if ((!client.getTerm().equals("")) && (!client.getDef().equals(""))) {
                    Item[] tmp = new Item[1];
                    tmp[0] = new Item(d.getValues()[current].getId(), client.getDef(), JHelp.ORIGIN);
                    if (!sendData(new Data(JHelp.UPDATE, new Item(d.getKey().getId(), client.getTerm(), JHelp.ORIGIN), tmp))) {
                        client.setDef("Request is not sent...", 1);
                    } else {
                        current = 0;
                    }
                }
                break;
            case "4": //delete
                System.out.println("delete");
                if ((!client.getTerm().equals("")) && (!client.getDef().equals(""))) {
                    Item[] tmp = new Item[1];
                    tmp[0] = new Item(d.getValues()[current].getId(), client.getDef(), JHelp.ORIGIN);
                    if (!sendData(new Data(JHelp.DELETE, new Item(d.getKey().getId(), client.getTerm(), JHelp.ORIGIN), tmp))) {
                        client.setDef("Request is not sent...", 1);
                    } else {
                        current = 0;
                    }
                }
                break;
            case "5": //next
                System.out.println("next");
                if (d != null) {
                    if (current == d.getValues().length - 2) {
                        client.setNext(false);
                    }
                    client.setPrevous(true);
                    client.setDef(d.getValues()[current + 1].getItem(), 0);
                    current++;
//                    client.setDef("");
                }
                break;
            case "6": //Prevous
                System.out.println("Prevous");
                if (d != null) {
                    if (current == 1) {
                        client.setPrevous(false);
                    }
                    client.setNext(true);
                    client.setDef(d.getValues()[current - 1].getItem(), 0);
                    current--;
//                    client.setDef("");
                }
                break;
            case "7": //exit
                System.out.println("exit");
                if (client.getSocket() != null) {
                    client.clientMsg(new Data(new Item("@@@exit@@@")));
                }
                client.dispose();
                break;
            case "8": //about
                System.out.println("about");
                client.showAbout();
                break;
        }
    }

    /**
     * This method are invoked when an object's text changed.
     *
     * @param e reference to {@link java.awt.event.TextEvent} event what happens
     * @see java.awt.event.TextEvent
     * @see java.awt.event.TextListener
     */
    @Override
    public void textValueChanged(TextEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("exit window");
        client.dispose();
    }

    /**
     * Method send Data
     *
     * @param d {@link jhelp.Data}
     * @return true if the object was successful send and otherwise false
     */
    public boolean sendData(Data d) {
        client.setDef("", 0);
        String key = client.getTerm();
        if (!key.equals("")) {
            if (client.getSocket() != null) {
                client.clientMsg(d);
                return true;
            }
            return false;
        } else {
            return false;
        }
    }
}
