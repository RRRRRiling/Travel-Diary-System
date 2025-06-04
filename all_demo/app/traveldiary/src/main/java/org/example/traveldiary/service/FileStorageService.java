package org.example.traveldiary.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileStorageService {
    private final ObjectMapper objectMapper;

    public FileStorageService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public synchronized <T> void writeJsonFile(String filePath, T data) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(path.toFile(), data);
    }

    public <T> T readJsonFile(String filePath, Class<T> clazz) throws IOException {
        if (!Files.exists(Paths.get(filePath))) return null;
        return objectMapper.readValue(new File(filePath), clazz);
    }

    public <T> List<T> readAllFromDirectory(String dirPath, Class<T> clazz) throws IOException {
        if (!Files.exists(Paths.get(dirPath))) return Collections.emptyList();

        return Files.list(Paths.get(dirPath))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".json"))
                .map(path -> {
                    try {
                        return objectMapper.readValue(path.toFile(), clazz);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    public synchronized boolean deleteFile(String filePath) throws IOException {
        return Files.deleteIfExists(Paths.get(filePath));
    }
}