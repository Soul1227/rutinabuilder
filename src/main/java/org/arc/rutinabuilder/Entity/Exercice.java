package org.arc.rutinabuilder.Entity;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class Exercice {

    private Long id;
    private String name;
    private Integer rep;
    private Integer set;
    private Integer time;
    private String description;
    private Date date;
    private Integer weight;
    private String collection;

    public Exercice() {
    }

    public Exercice(Long id,String name, Integer rep, Integer set, Integer time, String description, Date date, Integer weigth, String collection) {
        this.id = id;
        this.name = name;
        this.rep = rep;
        this.set = set;
        this.time = time;
        this.description = description;
        this.date = date;
        this.weight = weigth;
        this.collection = collection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRep() {
        return rep;
    }

    public void setRep(Integer rep) {
        this.rep = rep;
    }

    public Integer getSet() {
        return set;
    }

    public void setSet(Integer set) {
        this.set = set;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weigth) {
        this.weight = weigth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    @Override
    public String toString() {
        return "Exercice{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rep=" + rep +
                ", set=" + set +
                ", time=" + time +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", weight=" + weight +
                ", collection='" + collection + '\'' +
                '}';
    }
}
