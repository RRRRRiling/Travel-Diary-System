package org.example.traveldiary.service;

import org.example.traveldiary.model.ScenicSpot;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ScenicSpotService {
    private static final Logger logger = LoggerFactory.getLogger(ScenicSpotService.class);

    private final ObjectMapper objectMapper;
    private final String scenicSpotsFilePath;

    public ScenicSpotService(
            ObjectMapper objectMapper,
            @Value("${app.data.scenic-spots}") String scenicSpotsFilePath
    ) {
        this.objectMapper = objectMapper;
        this.scenicSpotsFilePath = scenicSpotsFilePath;
        configureObjectMapper();
    }

    @PostConstruct
    public void init() {
        logger.info("Initializing ScenicSpotService with data file: {}", scenicSpotsFilePath);
        validateDataFile();
    }

    private void configureObjectMapper() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private void validateDataFile() {
        if (!Files.exists(Path.of(scenicSpotsFilePath))) {
            logger.error("Scenic spots data file not found at: {}", scenicSpotsFilePath);
            throw new IllegalStateException("Scenic spots data file not found");
        }
    }

    public List<ScenicSpot> getAllScenicSpots() {
        try {
            return objectMapper.readValue(
                    new File(scenicSpotsFilePath),
                    new TypeReference<List<ScenicSpot>>() {}
            );
        } catch (IOException e) {
            logger.error("Failed to parse scenic spots data from file: {}", scenicSpotsFilePath, e);
            return Collections.emptyList();
        }
    }

    public Optional<ScenicSpot> getSpotByName(String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }
        return getAllScenicSpots().stream()
                .filter(spot -> name.equalsIgnoreCase(spot.getName()))
                .findFirst();
    }

    public List<ScenicSpot> searchByKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return Collections.emptyList();
        }

        final String lowerKeyword = keyword.toLowerCase();
        return getAllScenicSpots().stream()
                .filter(spot -> containsIgnoreCase(spot.getName(), lowerKeyword) ||
                        containsIgnoreCase(spot.getDescription(), lowerKeyword) ||
                        containsIgnoreCase(spot.getLocation(), lowerKeyword))
                .toList();
    }

    public List<ScenicSpot> searchByTag(String tag) {
        if (tag == null || tag.isBlank()) {
            return Collections.emptyList();
        }

        final String lowerTag = tag.toLowerCase();
        return getAllScenicSpots().stream()
                .filter(spot -> spot.getTags() != null &&
                        spot.getTags().stream()
                                .anyMatch(t -> t.equalsIgnoreCase(lowerTag)))
                .toList();
    }

    private boolean containsIgnoreCase(String source, String target) {
        return source != null && source.toLowerCase().contains(target);
    }
}
