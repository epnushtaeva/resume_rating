package com.data_base.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "specialities")
public class Speciality {
    @Id
    @GenericGenerator(name = "specialities_generator", strategy = "increment")
    @GeneratedValue(generator = "specialities_generator")
    private Long id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "speciality", cascade = CascadeType.ALL)
    private Set<FileMarkForSpeciality> marks;

    @Column(name = "is_employees_needed")
    private boolean isEmployeesNeeded;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<FileMarkForSpeciality> getMarks() {
        return marks;
    }

    public void setMarks(Set<FileMarkForSpeciality> marks) {
        this.marks = marks;
    }

    public boolean isEmployeesNeeded() {
        return isEmployeesNeeded;
    }

    public void setEmployeesNeeded(boolean employeesNeeded) {
        isEmployeesNeeded = employeesNeeded;
    }
}
