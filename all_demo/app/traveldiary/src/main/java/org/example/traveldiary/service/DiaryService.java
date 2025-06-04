package org.example.traveldiary.service;

import org.example.traveldiary.model.Diary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final FileStorageService fileStorage;
    private final ScenicSpotService spotService;

    @Value("${app.data.diary-dir}")
    private String diaryDir;

    // 定义日期时间格式
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Diary createDiary(Diary diary) throws IOException {
        // 验证景区是否存在
        if (spotService.getSpotByName(diary.getScenicSpot()) == null) {
            throw new IllegalArgumentException("景区不存在");
        }

        // 设置ID和时间
        diary.setId(UUID.randomUUID().toString());
        diary.setCreateTime(LocalDateTime.now());
        diary.setUpdateTime(LocalDateTime.now());

        // 保存日记
        String diaryFile = Paths.get(diaryDir, diary.getId() + ".json").toString();
        fileStorage.writeJsonFile(diaryFile, diary);
        return diary;
    }

    public List<Diary> getDiariesByAuthor(String author) throws IOException {
        return fileStorage.readAllFromDirectory(diaryDir, Diary.class).stream()
                .filter(diary -> diary.getAuthor().equals(author))
                .toList();
    }

    public Diary getDiaryById(String id) throws IOException {
        Diary diary = fileStorage.readJsonFile(
                Paths.get(diaryDir, id + ".json").toString(),
                Diary.class
        );

        // 确保时间格式正确
        if (diary != null) {
            formatDiaryDateTime(diary);
        }
        return diary;
    }

    public Diary updateDiary(String id, Diary updates) throws IOException {
        Diary existing = getDiaryById(id);
        if (existing == null) {
            throw new IllegalArgumentException("日记不存在");
        }

        // 更新字段
        if (updates.getTitle() != null) {
            existing.setTitle(updates.getTitle());
        }
        if (updates.getContent() != null) {
            existing.setContent(updates.getContent());
        }
        if (updates.getScenicSpot() != null) {
            if (spotService.getSpotByName(updates.getScenicSpot()) == null) {
                throw new IllegalArgumentException("景区不存在");
            }
            existing.setScenicSpot(updates.getScenicSpot());
        }

        // 更新修改时间
        existing.setUpdateTime(LocalDateTime.now());

        // 保存更新
        fileStorage.writeJsonFile(
                Paths.get(diaryDir, id + ".json").toString(),
                existing
        );
        return existing;
    }

    public void deleteDiary(String id) throws IOException {
        if (!fileStorage.deleteFile(Paths.get(diaryDir, id + ".json").toString())) {
            throw new IllegalArgumentException("日记不存在");
        }
    }

    /**
     * 格式化日记中的时间字段，确保格式一致
     */
    private void formatDiaryDateTime(Diary diary) {
        if (diary.getCreateTime() != null) {
            String formatted = diary.getCreateTime().format(DATE_TIME_FORMATTER);
            diary.setCreateTime(LocalDateTime.parse(formatted, DATE_TIME_FORMATTER));
        }
        if (diary.getUpdateTime() != null) {
            String formatted = diary.getUpdateTime().format(DATE_TIME_FORMATTER);
            diary.setUpdateTime(LocalDateTime.parse(formatted, DATE_TIME_FORMATTER));
        }
    }
}
