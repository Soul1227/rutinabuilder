package org.arc.rutinabuilder.Entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Counter {
    private long nextId;

    /**
     * Parameterized constructor for Counter.
     *
     * @param nextId The value to set as the next ID.
     */
    public Counter(long nextId) {
        this.nextId = nextId;
    }

    /**
     * Default constructor for Counter.
     * Initializes the nextId field to zero.
     */
    public Counter() {
    }

    public long getNextId() {
        return nextId;
    }

    public void setNextId(long nextId) {
        this.nextId = nextId;
    }
}
