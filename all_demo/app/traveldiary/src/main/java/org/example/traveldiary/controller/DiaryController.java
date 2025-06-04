package org.example.traveldiary.controller;

import org.example.traveldiary.model.Diary;
import org.example.traveldiary.service.DiaryService;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/diaries")
public class DiaryController {
    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping
    public Diary createDiary(@RequestBody Diary diary) throws IOException {
        return diaryService.createDiary(diary);
    }

    @GetMapping("/{id}")
    public Diary getDiary(@PathVariable String id) throws IOException {
        return diaryService.getDiaryById(id);
    }

    @GetMapping
    public List<Diary> getUserDiaries(@RequestParam String author) throws IOException {
        return diaryService.getDiariesByAuthor(author);
    }

    @PutMapping("/{id}")
    public Diary updateDiary(
            @PathVariable String id,
            @RequestBody Diary updates) throws IOException {
        return diaryService.updateDiary(id, updates);
    }

    @DeleteMapping("/{id}")
    public void deleteDiary(@PathVariable String id) throws IOException {
        diaryService.deleteDiary(id);
    }
}
