package com.data_base.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "files_to_specialities_estimates")
public class FileMarkForSpeciality{

    @Id
    @GenericGenerator(name = "files_to_specialities_estimates_generator", strategy = "increment")
    @GeneratedValue(generator = "files_to_specialities_estimates_generator")
    private long id;

    @Column(name = "file_id", insertable = false, updatable = false)
    private long fileId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    private File file;

    @Column(name = "speciality_id", insertable = false, updatable = false)
    private long specialityId;

    @Column(name = "speciality_id", insertable = false, updatable = false)
    private String specialityName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "speciality_id", referencedColumnName = "id")
    private Speciality speciality;

    @Column(name = "mark")
    private double mark;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    public String getFileName(){
        return this.file.getName();
    }

    public String getSpecialityName(){
        return this.speciality.getName();
    }

    public long getSpecialityId() {
        return specialityId;
    }
}
