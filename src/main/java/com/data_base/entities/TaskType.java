package com.data_base.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "task_types")
public class TaskType implements Serializable {
    @Id
    @GenericGenerator(name = "task_types_generator", strategy = "increment")
    @GeneratedValue(generator = "task_types_generator")
    private long id;

    @Column(name = "name")
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
