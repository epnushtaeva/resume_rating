package com.data_base.repositories;

import com.data_base.entities.NeuralNetworkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NeuralNetworkRepository extends JpaRepository<NeuralNetworkEntity, Long> {

    List<NeuralNetworkEntity> findAllByOrderByIdAsc();

    boolean existsBySpecialityId(long specialityId);

    NeuralNetworkEntity findFirstBySpecialityIdOrderByLayerNumberAsc(long specialityId);

    NeuralNetworkEntity findFirstBySpecialityIdAndLayerNumberOrderByNeuronNumberAsc(long specialityId, int layerNumber);

    NeuralNetworkEntity findFirstBySpecialityIdOrderByLayerNumberDesc(long specialityId);

    NeuralNetworkEntity findFirstBySpecialityIdAndLayerNumberOrderByNeuronNumberDesc(long specialityId, int layerNumber);

    List<NeuralNetworkEntity> findAllBySpecialityIdAndLayerNumberAndNeuronNumberOrderByIdAsc(long specialityId, int layerNumber, int neuronNumber);

    void deleteAllBySpecialityId(long specialityId);
}
