package com.data_base.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "dictionary")
public class Dictionary {

    @Id
    @GenericGenerator(name = "dictionary_generator", strategy = "increment")
    @GeneratedValue(generator = "dictionary_generator")
    private long id;

    @Column(name = "word")
    private String word;

    @OneToMany(mappedBy = "dictionary", cascade = CascadeType.MERGE)
    private List<FileDictionaryForSpeciality> specialities;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<FileDictionaryForSpeciality> getSpecialities() {
        return specialities;
    }
}
