package com.data_base.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "files")
public class File {

    @Id
    @GenericGenerator(name = "files_generator", strategy = "increment")
    @GeneratedValue(generator = "files_generator")
    private long id;

    @Column(name = "file_name")
    private String name;

    @Column(name = "file_path")
    private String path;

    @Column(name = "is_learn_example")
    private boolean learnExample;

    @Column(name = "is_hired")
    private boolean hired;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long hrManagerId;

    @Column(name = "learn_example_id")
    private Long learnExampleId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User hrManager;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
    private List<FileMarkForSpeciality> marks;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isLearnExample() {
        return learnExample;
    }

    public void setLearnExample(boolean learnExample) {
        this.learnExample = learnExample;
    }

    public boolean isHired() {
        return hired;
    }

    public void setHired(boolean hired) {
        this.hired = hired;
    }

    public List<FileMarkForSpeciality> getMarks() {
        return marks;
    }

    public void setMarks(List<FileMarkForSpeciality> marks) {
        this.marks = marks;
    }

    public User getHrManager() {
        return hrManager;
    }

    public void setHrManager(User hrManager) {
        this.hrManager = hrManager;
    }

    public long getHrManagerId() {
        return hrManagerId;
    }

    public void setHrManagerId(long hrManagerId) {
        this.hrManagerId = hrManagerId;
    }

    public Long getLearnExampleId() {
        return learnExampleId;
    }

    public void setLearnExampleId(Long learnExampleId) {
        this.learnExampleId = learnExampleId;
    }
}
