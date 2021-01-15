package com.data_base.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "settings")
public class Settings {
    @Id
    @GenericGenerator(name = "settings_generator", strategy = "increment")
    @GeneratedValue(generator = "settings_generator")
    private long id;

    @Column(name = "email")
    private String email;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
