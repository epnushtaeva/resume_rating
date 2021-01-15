package com.services;

import com.dto.FileMarkDto;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;

public interface NeuralNetworkService {

    void teachNeuralNetwork();

    void teachNeuralNetwork(long specialityId);

    List<FileMarkDto> costAndSaveFile(MultipartFile multipartFile, String fileName, Principal principal);

    @Transactional
    void removeNetworkForSpeciality(long specialityId);

    @Transactional
    void rebuildAllNetworks();

    void rebuildNetwork(long specialityId);

    void loadNeuralNetworkEntitiesToDataBase(long specialityId, MultipartFile file);

    String loadNeuralNetworkEntitiesFromDataBase(long specialityId);
}
