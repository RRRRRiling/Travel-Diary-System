package org.example.traveldiary.controller;

import org.example.traveldiary.model.ScenicSpot;
import org.example.traveldiary.service.ScenicSpotService;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/spots")
public class ScenicSpotController {
    private final ScenicSpotService spotService;

    public ScenicSpotController(ScenicSpotService spotService) {
        this.spotService = spotService;
    }

    @GetMapping
    public List<ScenicSpot> getAllSpots() throws IOException {
        return spotService.getAllScenicSpots();
    }

    @GetMapping("/search")
    public List<ScenicSpot> searchSpots(@RequestParam String keyword) throws IOException {
        return spotService.searchByKeyword(keyword);
    }

    @GetMapping("/tags/{tag}")
    public List<ScenicSpot> getSpotsByTag(@PathVariable String tag) throws IOException {
        return spotService.searchByTag(tag);
    }
}
