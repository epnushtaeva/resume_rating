package com.data_base.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "neural_network")
public class NeuralNetworkEntity {

    @Id
    @GenericGenerator(name = "neural_network_generator", strategy = "increment")
    @GeneratedValue(generator = "neural_network_generator")
    private long id;

    @Column(name = "layer_number")
    private int layerNumber;

    @Column(name = "neuron_number")
    private int neuronNumber;

    @Column(name = "weight")
    private double weight;

    @Column(name = "bias")
    private double bias;

    @Column(name = "speciality_id", insertable = false, updatable = false)
    private long specialityId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "speciality_id", referencedColumnName = "id")
    private Speciality speciality;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getLayerNumber() {
        return layerNumber;
    }

    public void setLayerNumber(int layerNumber) {
        this.layerNumber = layerNumber;
    }

    public int getNeuronNumber() {
        return neuronNumber;
    }

    public void setNeuronNumber(int neuronNumber) {
        this.neuronNumber = neuronNumber;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }
}
