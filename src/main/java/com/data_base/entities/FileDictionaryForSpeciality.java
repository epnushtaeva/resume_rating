package com.data_base.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "files__and_specialities_to_dictionaries")
public class FileDictionaryForSpeciality {

    @Id
    @GenericGenerator(name = "files_generator", strategy = "increment")
    @GeneratedValue(generator = "files_generator")
    private long id;

    @Column(name = "file_id", insertable = false, updatable = false)
    private long fileId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    private File file;

    @Column(name = "speciality_id", insertable = false, updatable = false)
    private Long specialityId;

    @Column(name = "dictionary_id", insertable = false, updatable = false)
    private Long dictionaryId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "speciality_id", referencedColumnName = "id")
    private Speciality speciality;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "dictionary_id", referencedColumnName = "id")
    private Dictionary dictionary;

    @Column(name = "coded_word")
    private String codedWord;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public File getFile() {
        return file;
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

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public String getCodedWord() {
        return codedWord;
    }

    public void setCodedWord(String codedWord) {
        this.codedWord = codedWord;
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public Long getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(Long specialityId) {
        this.specialityId = specialityId;
    }

    public Long getDictionaryId() {
        return dictionaryId;
    }

    public void setDictionaryId(Long dictionaryId) {
        this.dictionaryId = dictionaryId;
    }
}
