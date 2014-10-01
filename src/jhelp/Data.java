/*
 * Class Data
 */
package jhelp;

import java.io.Serializable;

/**
 * This class presents data for client form: term and it's definitions. Class
 * has three private attributes. Attribute <code>operation</code> defines action
 * execute with the data in database. Attribute <code>key</code> presents a term
 * or key value as a {@link jhelp.Item} object. Attribute <code>values</code> is
 * array of {@link jhelp.Item} objects. The attribute presents a set of
 * definitions for the <code>key</code>.
 *
 * @author <strong >S.N. Frantsuzov, 2013</strong>
 */
public class Data implements Serializable {

    /**
     * Static constant for serialization
     */
    public static final long serialVersionUID = 234;
    private int operation;
    private Item key;
    private Item[] values;

    /**
     * Default constructor defines all class attributes with default values.
     * Defualt value for <code>operation</code> is {@link jhelp.JHelp#SELECT}.
     * Default value for <code>key</code> is default {@link jhelp.Item} object.
     * Default value for <code>values</code> is <code>null</code>.
     */
    public Data() {
        this(JHelp.SELECT, new Item(), JHelp.DEFAULT_VALUES);
    }

    /**
     * This constructor defines key value for <code>Data</code> object.
     *
     * @param key {@link jhelp.Item} object presents key value.
     */
    public Data(Item key) {
        this(JHelp.SELECT, key, null);
    }

    /**
     * Creates a new instance of <code>Data</code>
     *
     * @param operation defines value for attribute <code>operation</code>.
     * Legal values for this attribute are {@link jhelp.JHelp#SELECT},
     * {@link jhelp.JHelp#INSERT}, {@link jhelp.JHelp#UPDATE} and
     * {@link jhelp.JHelp#DELETE}.
     * @param key defines initial value for <code>key</code> object.
     * @param values defines initial value for <code>values</code>.
     */
    public Data(int operation, Item key, Item[] values) {
        this.operation = operation;
        this.key = key;
        this.values = values;
    }

    /**
     * Method returns current value of <code>operation</code> attribute.
     *
     * @return current value of <code>operation</code> attribute.
     */
    public int getOperation() {
        return operation;
    }

    /**
     * Method sets current value of <code>operation</code> attribute.
     *
     * @param operation new value of <code>operation</code> attribute.
     */
    public void setOperation(int operation) {
        this.operation = operation;
    }

    /**
     * Method returns current value of <code>key</code> attribute.
     *
     * @return current value of <code>key</code> attribute.
     */
    public Item getKey() {
        return key;
    }

    /**
     * Method sets current value of <code>key</code> attribute.
     *
     * @param key new value of <code>key</code> attribute.
     */
    public void setKey(Item key) {
        this.key = key;
    }

    /**
     * Method returns current value of item in <code>values</code> array with
     * <code>index</code> index.
     *
     * @param index index in <code>values</code> array.
     * @return current value of item in <code>values</code> array.
     */
    public Item getValue(int index) {
        return values[index];
    }

    /**
     * Method sets current value of item in <code>values</code> array with
     * <code>index</code> index as <code>value</code>.
     *
     * @param index index index in <code>values</code> array.
     * @param value new value for {@link jhelp.Item} object in
     * <code>values</code> array.
     */
    public void setValue(int index, Item value) {
        if (index > -1 && index < values.length) {
            values[index] = value;
        }
    }

    /**
     * Method returns all values of <code>values</code> attribute as array.
     *
     * @return array of {@link jhelp.Item} type.
     */
    public Item[] getValues() {
        return values;
    }

    /**
     * Method sets array <code>values</code> as class attribute
     * <code>values</code>.
     *
     * @param values array of {@link jhelp.Item} type.
     */
    public void setValues(Item[] values) {
        this.values = values;
    }
}
