/*
 * Class Item for JHelp project
 */
package jhelp;

import java.io.Serializable;

/**
 * This class presents string data from database. Objects of this type are used
 * by {@link jhelp.Data} class.<br> The class has three private attributes.
 * Attribute <code>id</code> of integer type presents record's identificator or
 * it's primary key. Attribute <code>item</code> is {@link String} object which
 * presents any string data, for example term or definition. Attribute  <code>
 * state</code> is current state of item. All legal values for
 * <code>state</code> are defined in {@link jhelp.JHelp} interface.
 *
 * @author <strong >S.N. Frantsuzov, 2013</strong>
 * @version 1.0
 * @see jhelp.Data
 */
public class Item implements Serializable {

    /**
     * Static identificator of the class for serialization purpose.
     */
    public static final long serialVersionUID = 12341L;
    /**
     * Record's number (primary key) in original database. Defrault value is 0.
     */
    private int id;
    /**
     * String object presents any data. Default values is {@link
     * jhelp.JHelp#DEFAULT_KEY} or {@link jhelp.JHelp#DEFAULT_VALUE}.
     * </code>
     */
    private String item;
    /**
     * Current state of item. All possible values are defined in
     * {@link jhelp.JHelp} interface. Default values is      <code>jhelp.JHelp.ORIGIN
     * </code>
     */
    private int state;

    /**
     * Default constructor. The constructor defines class's attributes      <code>id
     * </code>, <code>item</code> and <code>state</code> with default values.
     */
    public Item() {
        this(-1, JHelp.DEFAULT_VALUE, JHelp.ORIGIN);
    }

    /**
     * This constructor defines attribute <code>id</code> with parameter
     * <code>id</code>. Attributes <code>item</code> and <code>state</code> are
     * defined as default values.
     *
     * @param item presents String value for attribute <code>item</code>
     */
    public Item(String item) {
        this(-1, item, JHelp.ORIGIN);
    }

    /**
     * This constructor defines attribute <code>id</code> with parameter
     * <code>id</code>, attribute <code>item</code> with parameter
     * <code>item</code> and attribute <code>state</code> according with
     * parameter <code>state</code>.
     *
     * @param id record's identificator or primary key.
     * @param item string value.
     * @param state current state.
     */
    public Item(int id, String item, int state) {
        this.id = id;
        this.item = item;
        this.state = state;
    }

    /**
     * Method returns current record's identificator or primary key.
     *
     * @return record's identificator or primary key. Method return 0 (zero) if
     * record's identificator not set or unknown.
     * @see JHelp
     */
    public int getId() {
        return id;
    }

    /**
     * Method returns current item's value as {@link java.lang.String} object.
     *
     * @return current item's value. Method return
     * {@link jhelp.JHelp#DEFAULT_VALUE} or {@link jhelp.JHelp#DEFAULT_KEY} if
     * item not set or unknown.
     * @see JHelp
     */
    public String getItem() {
        return item;
    }

    /**
     * Method retrun current state of item.
     *
     * @return current state of item. Possible values of the state are defined
     * in {@link jhelp.JHelp} interface. Default value is {@link JHelp#ORIGIN}.
     * @see JHelp
     */
    public int getState() {
        return state;
    }

    /**
     * Method sets new value for <code>id</code> attribute.
     *
     * @param id defines new value of <code>id</code> attribute.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Method sets new value for <code>item</code> attribute.
     *
     * @param item {@link java.lang.String} object defines new value of
     * <code>item</code> attribute.
     */
    public void setItem(String item) {
        this.item = item;
    }

    /**
     * Method sets new value for <code>state</code> attribute. All legal values
     * for this attribute are defined in {@link jhelp.JHelp} interface.
     *
     * @param state integer number defines new value of <code>state</code>
     * attribute. All legal values for this parameter are defined in
     * {@link jhelp.JHelp} interface.
     * @see jhelp.JHelp
     */
    public void setState(int state) {
        this.state = state;
    }
}
