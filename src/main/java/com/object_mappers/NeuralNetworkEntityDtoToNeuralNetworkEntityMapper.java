package com.object_mappers;

import com.data_base.entities.NeuralNetworkEntity;
import com.dto.NeuralNetworkEntityDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NeuralNetworkEntityDtoToNeuralNetworkEntityMapper{
    NeuralNetworkEntity neuralNetworkEntitiesDtoToNeuralNetworkEntity(NeuralNetworkEntityDto neuralNetworkEntityDto);
}
