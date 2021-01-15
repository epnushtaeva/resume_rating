package com.object_mappers;

import com.data_base.entities.NeuralNetworkEntity;
import com.dto.NeuralNetworkEntityDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NeuralNetworkEntityToNeuralNetworkEntityDtoMapper {
    NeuralNetworkEntityDto neuralNetworkEntityToNeuralNetworkEntityDto(NeuralNetworkEntity neuralNetworkEntityDto);
}
