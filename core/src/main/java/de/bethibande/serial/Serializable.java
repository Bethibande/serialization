package de.bethibande.serial;

public interface Serializable {

    void write(final Writer writer);

    int size();

}
