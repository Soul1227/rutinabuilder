package org.arc.rutinabuilder.Entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Counter {
    private long nextId;

    public Counter(long nextId) {
        this.nextId = nextId;
    }

    public Counter() {
    }

    public long getNextId() {
        return nextId;
    }

    public void setNextId(long nextId) {
        this.nextId = nextId;
    }
}
