package com.data_base.entities;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role  implements GrantedAuthority{
    @Id
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "russian_name", nullable = false)
    private String russianName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthority() {
        return name;
    }

    public void setAuthority(String name) {
        this.name = name;
    }

    public String getRussianName() {
        return russianName;
    }

    public void setRussianName(String russianName) {
        this.russianName = russianName;
    }
}
