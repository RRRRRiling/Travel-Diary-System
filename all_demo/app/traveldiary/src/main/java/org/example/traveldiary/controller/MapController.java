package org.example.traveldiary.controller;

import org.example.traveldiary.service.MapPathFinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map")
public class MapController {

    @GetMapping("/path")
    public List<Integer> findPath(
            @RequestParam int scenicId,
            @RequestParam int start,
            @RequestParam int end) {
        return MapPathFinder.findPath(scenicId, start, end);
    }
}