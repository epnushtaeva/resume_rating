package com.services.impl;

import com.data_base.entities.*;
import com.data_base.repositories.NeuralNetworkRepository;
import com.dictionaries.ConstantDigits;
import com.dto.FileMarkDto;
import com.dto.NeuralNetworkEntityDto;
import com.dto.NeuralNetworkLoadFromJsonDto;
import com.google.gson.Gson;
import com.neural_network.Network;
import com.neural_network.NetworkTeachParameters;
import com.neural_network.NetworkTeacher;
import com.object_mappers.NeuralNetworkEntityDtoToNeuralNetworkEntityMapper;
import com.object_mappers.NeuralNetworkEntityToNeuralNetworkEntityDtoMapper;
import com.services.*;
import com.services.classes.DictionaryDto;
import com.utils.ArrayUtils;
import liquibase.util.file.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class NeuralNetworkServiceImpl implements NeuralNetworkService {
    @Autowired
    private NeuralNetworkRepository neuralNetworkRepository;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private SpecialityService specialityService;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private NeuralNetworkEntityDtoToNeuralNetworkEntityMapper neuralNetworkEntityDtoToNeuralNetworkEntityMapper;

    @Autowired
    private NeuralNetworkEntityToNeuralNetworkEntityDtoMapper neuralNetworkEntityToNeuralNetworkEntityDtoMapper;

    private NGramService nGramService = new UnigrammService();
    private Porter porter = new StemmingPorter();

    private double maxError = 0.05;
    private double teachScore = 0.2;
    private double maxTime = 800000;

    private Map<Long, Network> networks;

    @PostConstruct
    public void afterInit(){
        this.networks = this.buildNetworks();
    }

    @Transactional
    @Override
    public void teachNeuralNetwork(){
        List<Speciality> specialities = this.specialityService.getAllSpecialitiesOrdered();

       for(Speciality speciality: specialities){
           this.teachNeuralNetwork(speciality.getId());
       }
    }

    @Transactional
    @Override
    public void teachNeuralNetwork(long specialityId){
      this.neuralNetworkRepository.deleteAllBySpecialityId(specialityId);

      Network neuralNetwork = this.getNetworkForTeach(specialityId);

      if(ObjectUtils.isEmpty(neuralNetwork)){
          return;
      }

      double[][] trainingSets = this.getTrainingSets(specialityId);
      double[][] targetValues = this.getTargetValues(specialityId);
      
      NetworkTeacher networkTeacher = new NetworkTeacher(neuralNetwork);
      networkTeacher.teach(new NetworkTeachParameters()
                                .setTrainingSets(trainingSets)
                                .setTargetValues(targetValues)
                                .setMaxError(this.maxError)
                                .setMaxTeachTime(this.maxTime)
                                .setTeachScore(this.teachScore));
      this.saveNetworkToDataBase(neuralNetwork, specialityId);
    }

    @Override
    public List<FileMarkDto> costAndSaveFile(MultipartFile file, String fileName, Principal principal){
        String storageFilePath = this.fileService.moveMultipartToStorage(file, fileName);
        String fileExtension = FilenameUtils.getExtension(fileName);
        List<Speciality> specialities = this.specialityService.getAllSpecialitiesOrdered();
        Map <String, Double> specialitiesNamesToMarks = new HashMap<>();
        List<FileMarkDto> result = new ArrayList<>();
        User currentUser = (User)this.userDetailsService.loadUserByUsername(principal.getName());

        if (fileExtension.equals("zip")) {
            String unzippedDirectoryPath = this.fileService.unzipFile(storageFilePath);

            try {
                Path unzippedDirectory = Paths.get(unzippedDirectoryPath);
                List<Path> filesInUnzippedDirectory = Files
                        .walk(unzippedDirectory)
                        .filter(Files::isRegularFile)
                        .collect(Collectors.toList());

                for (Path fileInUnzippedDirectory : filesInUnzippedDirectory) {
                    specialitiesNamesToMarks = new HashMap<>();
                    String currentFileName = fileInUnzippedDirectory.getFileName().toFile().getName().toString();
                    String currentFilePath = this.fileService.moveFileToStorage(fileInUnzippedDirectory.toString(), currentFileName);
                    this.fillSpecialitiesNamesToMarkMap(currentFilePath, specialities, specialitiesNamesToMarks);

                    FileMarkDto fileMarkDto = new FileMarkDto();
                    fileMarkDto.setFileName(currentFileName);
                    fileMarkDto.setFilePath(currentFilePath);
                    fileMarkDto.setSpecialityNamesToMarks(specialitiesNamesToMarks);
                    fileMarkDto.setId(this.saveCoastetFileToDataBase(fileMarkDto, currentUser));
                    result.add(fileMarkDto);
                }

                this.fileService.removeUnzippedDirectory(unzippedDirectory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.fillSpecialitiesNamesToMarkMap(storageFilePath, specialities, specialitiesNamesToMarks);
            FileMarkDto fileMarkDto = new FileMarkDto();
            fileMarkDto.setFileName(fileName);
            fileMarkDto.setFilePath(storageFilePath);
            fileMarkDto.setSpecialityNamesToMarks(specialitiesNamesToMarks);
            fileMarkDto.setId(this.saveCoastetFileToDataBase(fileMarkDto, currentUser));
            result.add(fileMarkDto);
        }

        return result;
    }

    @Override
    @Transactional
    public void removeNetworkForSpeciality(long specialityId){
        this.neuralNetworkRepository.deleteAllBySpecialityId(specialityId);
    }

    private long saveCoastetFileToDataBase(FileMarkDto fileMarkDto, User currentUser){
        List<Speciality> specialities = this.specialityService.getAllSpecialitiesOrdered();
        double[] marks = new double[specialities.size()];
        int specialityIndex = 0;

        for(Speciality speciality: specialities){
            for(Map.Entry<String, Double> specialityNameToMark: fileMarkDto.getSpecialityNamesToMarks().entrySet()){
                       if(specialityNameToMark.getKey().equals(speciality.getName())){
                           marks[specialityIndex] = specialityNameToMark.getValue();
                       }
            }

            specialityIndex++;
        }

        return this.fileService.saveFileToDataBase(fileMarkDto.getFileName(), fileMarkDto.getFilePath(), marks, false, currentUser);
    }

    private void fillSpecialitiesNamesToMarkMap(String storageFilePath,
                                                List<Speciality> specialities,
                                                Map<String, Double> specialitiesNamesToMarks) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        for(Speciality speciality: specialities){
            double[] codedFile = this.getCodedFile(storageFilePath, this.dictionaryService.getDictionary(speciality.getId()));
            Network network = this.networks.get(speciality.getId());

            executorService.execute(() -> {
                double[] neuralNetworkOutputs = network.getOutputs(codedFile);
                double output = neuralNetworkOutputs.length > 0 ? neuralNetworkOutputs[0] : 0;
                String fileText = this.fileService.readTextFromFile(storageFilePath);

                if(!fileText.contains(speciality.getName().split(" ")[0])){
                    output = output/2;
                }

                if(fileText.contains(speciality.getName().split(" ")[0]) && output<0.5){
                    output = output*1.5;
                }

                specialitiesNamesToMarks.put(speciality.getName(), output);
            });
        }

        executorService.shutdown();
    }

    @Override
    @Transactional
    public void rebuildAllNetworks(){
        List<Speciality> specialities = this.specialityService.getAllSpecialitiesOrdered();

        for(Speciality speciality: specialities){
            this.rebuildNetwork(speciality.getId());
        }
    }

    @Override
    @Transactional
    public void rebuildNetwork(long specialityId){
        Network network = this.buildNetworkFromDatabase(specialityId);
        this.networks.put(specialityId, network);
    }

    @Override
    @Transactional
    public void loadNeuralNetworkEntitiesToDataBase(long specialityId, MultipartFile file){
        this.neuralNetworkRepository.deleteAllBySpecialityId(specialityId);
        this.dictionaryService.clearDictionary(specialityId);

        try {
            String fileText = Files.lines(Paths.get(file.getOriginalFilename())).collect(Collectors.joining());
            Gson gson = new Gson();
            NeuralNetworkLoadFromJsonDto neuralNetworkLoadFromJsonDto  = gson.fromJson(fileText, NeuralNetworkLoadFromJsonDto.class);
            this.dictionaryService.saveAllDictionaryWordsToDataBase(neuralNetworkLoadFromJsonDto.getDictionary(), specialityId);

            int currentQuatesIndex = 0;
            int neuralNetworkStringLength = neuralNetworkLoadFromJsonDto.getNeuralNetwork().length();

            while (currentQuatesIndex < neuralNetworkStringLength){
                currentQuatesIndex = neuralNetworkLoadFromJsonDto.getNeuralNetwork().indexOf("}", currentQuatesIndex) + 1; //ToDo: В константы

                String neuralNetworkEntityDtoString = neuralNetworkLoadFromJsonDto.getNeuralNetwork().substring(0, currentQuatesIndex);
                NeuralNetworkEntityDto neuralNetworkEntityDto = gson.fromJson(neuralNetworkEntityDtoString, NeuralNetworkEntityDto.class);
                NeuralNetworkEntity neuralNetworkEntity = this.neuralNetworkEntityDtoToNeuralNetworkEntityMapper.neuralNetworkEntitiesDtoToNeuralNetworkEntity(neuralNetworkEntityDto);
                this.neuralNetworkRepository.saveAndFlush(neuralNetworkEntity);

                currentQuatesIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String loadNeuralNetworkEntitiesFromDataBase(long specialityId){
        NeuralNetworkLoadFromJsonDto neuralNetworkLoadFromJsonDto  = new NeuralNetworkLoadFromJsonDto();
        neuralNetworkLoadFromJsonDto.setDictionary(this.dictionaryService.getDictionary(specialityId).stream().map(dictionaryDto -> dictionaryDto.getWord()).collect(Collectors.toList()));

        Gson gson = new Gson();
        StringBuilder neuralNetwork = new StringBuilder();
        NeuralNetworkEntity neuralNetworkEntityWithMinLayerNumber = this.neuralNetworkRepository.findFirstBySpecialityIdOrderByLayerNumberAsc(specialityId);
        int minLayerNumber = neuralNetworkEntityWithMinLayerNumber.getLayerNumber();
        NeuralNetworkEntity neuralNetworkEntityWithMaxLayerNumber = this.neuralNetworkRepository.findFirstBySpecialityIdOrderByLayerNumberDesc(specialityId);
        int maxLayerNumber = neuralNetworkEntityWithMaxLayerNumber.getLayerNumber();

        for(int currentLayerNumber = minLayerNumber; currentLayerNumber <= maxLayerNumber; currentLayerNumber++){
            NeuralNetworkEntity neuralNetworkEntityWithMinNeuronNumber = this.neuralNetworkRepository.findFirstBySpecialityIdAndLayerNumberOrderByNeuronNumberAsc(specialityId, currentLayerNumber);
            int minNeuronNumber = neuralNetworkEntityWithMinNeuronNumber.getNeuronNumber();
            NeuralNetworkEntity neuralNetworkEntityWithMaxNeuronNumber = this.neuralNetworkRepository.findFirstBySpecialityIdAndLayerNumberOrderByNeuronNumberDesc(specialityId, currentLayerNumber);
            int maxNeuronNumber = neuralNetworkEntityWithMaxNeuronNumber.getNeuronNumber();

            for(int currentNeuronNumber = minNeuronNumber; currentNeuronNumber<=maxNeuronNumber;currentNeuronNumber++){
                List<NeuralNetworkEntity> currentNeuronEntities = this.neuralNetworkRepository.findAllBySpecialityIdAndLayerNumberAndNeuronNumberOrderByIdAsc(specialityId, currentLayerNumber, currentNeuronNumber);

                for(NeuralNetworkEntity neuralNetworkEntity: currentNeuronEntities){
                   neuralNetwork.append(gson.toJson(this.neuralNetworkEntityToNeuralNetworkEntityDtoMapper.neuralNetworkEntityToNeuralNetworkEntityDto(neuralNetworkEntity)));
                }
            }
        }

        neuralNetworkLoadFromJsonDto.setNeuralNetwork(neuralNetwork.toString());

        String jsonFilePath = this.fileService.saveJsonToStorage(gson.toJson(neuralNetworkLoadFromJsonDto));
        return jsonFilePath;
    }

    private Map<Long, Network> buildNetworks(){
        Map<Long, Network> networks = new LinkedHashMap<>();

        List<Speciality> specialities = this.specialityService.getAllSpecialitiesOrdered();

        for(Speciality speciality: specialities){
                Network network = this.buildNetworkFromDatabase(speciality.getId());
                networks.put(speciality.getId(), network);
        }

        return networks;
    }

    private Network buildNetworkFromDatabase(long specialityId){
        if(!this.neuralNetworkRepository.existsBySpecialityId(specialityId)){
            return new Network(new int[0], new double[0][], new double[0]);
        }

        NeuralNetworkEntity neuralNetworkEntityWithMinLayerNumber = this.neuralNetworkRepository.findFirstBySpecialityIdOrderByLayerNumberAsc(specialityId);
        int minLayerNumber = neuralNetworkEntityWithMinLayerNumber.getLayerNumber();
        NeuralNetworkEntity neuralNetworkEntityWithMaxLayerNumber = this.neuralNetworkRepository.findFirstBySpecialityIdOrderByLayerNumberDesc(specialityId);
        int maxLayerNumber = neuralNetworkEntityWithMaxLayerNumber.getLayerNumber();
        List<double[]> weights = new ArrayList<>();
        List<Double> biases = new ArrayList<>();
        int[] neuronsInLayersCounts = new int[maxLayerNumber];

        for(int currentLayerNumber = minLayerNumber; currentLayerNumber <= maxLayerNumber; currentLayerNumber++){
            NeuralNetworkEntity neuralNetworkEntityWithMinNeuronNumber = this.neuralNetworkRepository.findFirstBySpecialityIdAndLayerNumberOrderByNeuronNumberAsc(specialityId, currentLayerNumber);
            int minNeuronNumber = neuralNetworkEntityWithMinNeuronNumber.getNeuronNumber();
            NeuralNetworkEntity neuralNetworkEntityWithMaxNeuronNumber = this.neuralNetworkRepository.findFirstBySpecialityIdAndLayerNumberOrderByNeuronNumberDesc(specialityId, currentLayerNumber);
            int maxNeuronNumber = neuralNetworkEntityWithMaxNeuronNumber.getNeuronNumber();
            neuronsInLayersCounts[currentLayerNumber - 1] = maxNeuronNumber;

            for(int currentNeuronNumber = minNeuronNumber; currentNeuronNumber<=maxNeuronNumber;currentNeuronNumber++){
                List<NeuralNetworkEntity> currentNeuronEntities = this.neuralNetworkRepository.findAllBySpecialityIdAndLayerNumberAndNeuronNumberOrderByIdAsc(specialityId, currentLayerNumber, currentNeuronNumber);
                double[] currentNeuronWeights = new double[currentNeuronEntities.size()];
                int weightIndex = 0;

                for(NeuralNetworkEntity neuralNetworkEntity: currentNeuronEntities){
                    currentNeuronWeights[weightIndex] = neuralNetworkEntity.getWeight();
                    weightIndex++;
                }

                biases.add(currentNeuronEntities.get(0).getBias());
                weights.add(currentNeuronWeights);
            }
        }

        return new Network(neuronsInLayersCounts, ArrayUtils.toArrayOfDoubleArrays(weights), ArrayUtils.toPrimitiveDoubleArray(biases));
    }

    private Network getNetworkForTeach(long specialityId){
        this.dictionaryService.rebuildDictionaryForSpeciality(specialityId);
        Set<DictionaryDto> specialityWords = this.dictionaryService.getDictionary(specialityId);


        int firstNetworkLayerNeuronsCount = specialityWords.size();
        int secondNetworkLayerNeuronsCount = firstNetworkLayerNeuronsCount/ConstantDigits.NEURONS_IN_HIDDEN_LAYER_COUNT_COEFFICIENT;
        int thirdNetworkLayerNeuronsCount = firstNetworkLayerNeuronsCount/10;
        int fourthNetworkLayerNeuronsCount = 1;

        int[] neuronsInLayersCounts = {firstNetworkLayerNeuronsCount, secondNetworkLayerNeuronsCount, thirdNetworkLayerNeuronsCount, fourthNetworkLayerNeuronsCount};
        double[][] weights = this.getInitialWeights(neuronsInLayersCounts);
        double[] biases = this.getInitialBiases(neuronsInLayersCounts);

        if(firstNetworkLayerNeuronsCount == 0){
            return  null;
        }

        return new Network(neuronsInLayersCounts, weights, biases);
    }

    @Transactional
    private double[][] getTrainingSets(long specialityId){
       List<File> filesForTrainingSets = this.fileService.getFilesForTrainingSets(specialityId);
       Set<DictionaryDto> specialityWords = this.dictionaryService.getDictionary(specialityId);
       List<double[]> trainingSets = new ArrayList<>();

       for(File trainingSetFile: filesForTrainingSets){
           trainingSets.add(this.getCodedFile(trainingSetFile.getPath(), specialityWords));
       }

       return ArrayUtils.toArrayOfDoubleArrays(trainingSets);
    }

    private  double[][] getTargetValues(long specialityId){
        List<File> filesForTargetValues = this.fileService.getFilesForTrainingSets(specialityId);
        List<Long> targetFilesIds = this.getFileIds(filesForTargetValues);
        List<FileMarkForSpeciality> fileMarks = this.fileService.getFilesMarks(targetFilesIds, specialityId);

        return this.getTargetValues(fileMarks);
    }

    private double[][] getInitialWeights(int[] neuronsInLayersCounts){
        int weightsInCurrentLayerCount = 1;
        int layerNumber = 1;
        List<double[]> result = new ArrayList<>();

        for(int neuronsInCurrentLayerCount: neuronsInLayersCounts){
            for(int index = 0; index < neuronsInCurrentLayerCount; index++){
                result.add(this.getInitialWeights(weightsInCurrentLayerCount, layerNumber));
            }

            weightsInCurrentLayerCount = neuronsInCurrentLayerCount;
            layerNumber++;
        }

        return ArrayUtils.toArrayOfDoubleArrays(result);
    }

    private double[] getInitialBiases(int[] neuronsInLayersCounts){
        int allNeuronsCount = Arrays.stream(neuronsInLayersCounts).sum();
        double[] result = new double[allNeuronsCount];

        for(int biasIndex = 0; biasIndex < allNeuronsCount; biasIndex++){
            result[biasIndex] = 0;
        }

        return result;
    }

    private double[] getInitialWeights(int weightsCount, int layerNumber){
        double[] weights = new double[weightsCount];

        for(int weightIndex = 0; weightIndex < weightsCount; weightIndex++){
            if(layerNumber == 1){
                weights[weightIndex ] = 1.0;
            } else {
                weights[weightIndex ] = ThreadLocalRandom.current().nextDouble(ConstantDigits.MIN_NEURONS_INITIAL_WEIGHT,
                        ConstantDigits.MAX_NEURONS_INITIAL_WEIGHTS);
            }
        }

        return weights;
    }

    private double[] getCodedFile(String filePath, Set<DictionaryDto> words){
        double[] result = new double[words.size()];

        String fileText = this.fileService.readTextFromFile(filePath);
        List<String> fileWords = this.porter.portWords(this.nGramService.getNgrams(fileText));

        int wordIndex = 0;

        for(DictionaryDto word: words){
            if(fileWords.contains(word.getWord())){
                try {
                    Files.write(Paths.get("D:\\2.txt"),(word.getWord()+" ").getBytes(), StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                result[wordIndex] = 1;
            } else {
                result[wordIndex] = 0;
            }

            wordIndex++;
        }

        return result;
    }

    private void saveNetworkToDataBase(Network network, long specialityId){
        Speciality speciality = this.specialityService.getSpecialityById(specialityId);

        for(int layerIndex = 0; layerIndex < network.getLayersCount(); layerIndex++){
            double[][] weights = network.getWeights(layerIndex);
            double[] biases = network.getBiases(layerIndex);
            List<NeuralNetworkEntity> tempEntitis = new ArrayList<>();

            for(int neuronIndex = 0; neuronIndex < weights.length; neuronIndex++){

                for(double weight: weights[neuronIndex]){
                    NeuralNetworkEntity neuralNetworkEntity = new NeuralNetworkEntity();
                    neuralNetworkEntity.setSpeciality(speciality);
                    neuralNetworkEntity.setLayerNumber(layerIndex + 1);
                    neuralNetworkEntity.setNeuronNumber(neuronIndex + 1);
                    neuralNetworkEntity.setWeight(weight);
                    neuralNetworkEntity.setBias(biases[neuronIndex]);
                    tempEntitis.add(neuralNetworkEntity);
                }

                if(tempEntitis.size()>100000){
                    this.neuralNetworkRepository.saveAll(tempEntitis);
                    tempEntitis.clear();
                }
            }

            if(tempEntitis.size()>0){
                this.neuralNetworkRepository.saveAll(tempEntitis);
                tempEntitis.clear();
            }
        }
    }

    private List<Long> getFileIds(List<File> files){
        List<Long> result = new ArrayList<>();

        for(File file: files){
           result.add(file.getId());
        }

        return  result;
    }

    private double[][] getTargetValues(List<FileMarkForSpeciality> fileMarks){
        List<double[]> result = new ArrayList<>();

        for(FileMarkForSpeciality fileMark:fileMarks){
            double[] fileMarkArray = {fileMark.getMark()};
            result.add(fileMarkArray);
        }

        return ArrayUtils.toArrayOfDoubleArrays(result);
    }

    private int[] getNeuronsInLayersCounts(List<NeuralNetworkEntity> neuralNetworkEntities){
        int layersCount = neuralNetworkEntities
                .stream()
                .mapToInt(neuralNetworkEntity -> neuralNetworkEntity.getLayerNumber())
                .max()
                .orElse(0);
        int[] result = new int[layersCount];

        for(int layerNumber = 1; layerNumber <= layersCount; layerNumber++){
            final int currentLayerNumber = layerNumber;

            result[currentLayerNumber - 1] = neuralNetworkEntities
                    .stream()
                    .filter(neuralNetworkEntity -> neuralNetworkEntity.getLayerNumber() == currentLayerNumber)
                    .mapToInt(neuralNetworkEntity -> neuralNetworkEntity.getNeuronNumber())
                    .max()
                    .orElse(0);
        }

        return result;
    }

    private double[][] getWeights(List<NeuralNetworkEntity> neuralNetworkEntities){
        List<double[]> result = new ArrayList<>();
        List<Double> currentNeuronWeights = new ArrayList<>();
        int networkEntitiesCount = neuralNetworkEntities.size();

        for(int networkEntityIndex = 0; networkEntityIndex < networkEntitiesCount; networkEntitiesCount++){
            currentNeuronWeights.add(neuralNetworkEntities.get(networkEntityIndex).getWeight());

            if(networkEntityIndex == networkEntitiesCount - 1){
                result.add(ArrayUtils.toPrimitiveDoubleArray(currentNeuronWeights));
                break;
            }

            NeuralNetworkEntity currentNetworEntity = neuralNetworkEntities.get(networkEntityIndex);
            NeuralNetworkEntity nextNeuralNetworkEntity = neuralNetworkEntities.get(networkEntityIndex + 1);

            if(currentNetworEntity.getNeuronNumber() != nextNeuralNetworkEntity.getNeuronNumber() ||
                    currentNetworEntity.getLayerNumber() != nextNeuralNetworkEntity.getLayerNumber()){
                result.add(ArrayUtils.toPrimitiveDoubleArray(currentNeuronWeights));
                currentNeuronWeights = new ArrayList<>();
            }
        }

        return ArrayUtils.toArrayOfDoubleArrays(result);
    }

    private double[] getBiases(List<NeuralNetworkEntity> neuralNetworkEntities){
        List<Double> result = new ArrayList<>();
        double currentNeuronBias = 0;
        int networkEntitiesCount = neuralNetworkEntities.size();

        for(int networkEntityIndex = 0; networkEntityIndex < networkEntitiesCount; networkEntitiesCount++){
            currentNeuronBias = neuralNetworkEntities.get(networkEntityIndex).getBias();

            if(networkEntityIndex == networkEntitiesCount - 1){
                result.add(currentNeuronBias);
                break;
            }

            NeuralNetworkEntity currentNetworEntity = neuralNetworkEntities.get(networkEntityIndex);
            NeuralNetworkEntity nextNeuralNetworkEntity = neuralNetworkEntities.get(networkEntityIndex + 1);

            if(currentNetworEntity.getNeuronNumber() != nextNeuralNetworkEntity.getNeuronNumber() ||
                    currentNetworEntity.getLayerNumber() != nextNeuralNetworkEntity.getLayerNumber()){
                result.add(currentNeuronBias);
            }
        }

        return ArrayUtils.toPrimitiveDoubleArray(result);
    }
}
