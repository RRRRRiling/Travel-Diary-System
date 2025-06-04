import java.io.IOException;
import java.io.File;
import java.util.*;

public class DiaryService {
    private static final String DIARY_DIR = "D:\\all_demo\\data\\diaries";

    static {
        FileUtil.createDirectoryIfNotExists(DIARY_DIR);
    }

    public boolean createDiary(Diary diary) {
        String diaryId = UUID.randomUUID().toString();
        diary.setId(diaryId);

        String diaryFilePath = DIARY_DIR + "\\" + diaryId + ".json";

        try {
            String diaryJson = JsonUtil.toJson(diary);
            FileUtil.writeToFile(diaryFilePath, diaryJson);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDiary(String diaryId, String username, String newTitle, String newContent) {
        String diaryFilePath = DIARY_DIR + "\\" + diaryId + ".json";
        File diaryFile = new File(diaryFilePath);

        if (!diaryFile.exists()) {
            return false; // 日记不存在
        }

        try {
            String diaryJson = FileUtil.readFromFile(diaryFilePath);
            Diary diary = JsonUtil.fromJson(diaryJson, Diary.class);

            if (!diary.getAuthor().equals(username)) {
                return false; // 不是作者无法修改
            }

            diary.setTitle(newTitle);
            diary.setContent(newContent);
            diary.setUpdateTime(new Date());

            String updatedDiaryJson = JsonUtil.toJson(diary);
            FileUtil.writeToFile(diaryFilePath, updatedDiaryJson);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDiary(String diaryId, String username) {
        String diaryFilePath = DIARY_DIR + "\\" + diaryId + ".json";
        File diaryFile = new File(diaryFilePath);

        if (!diaryFile.exists()) {
            return false; // 日记不存在
        }

        try {
            String diaryJson = FileUtil.readFromFile(diaryFilePath);
            Diary diary = JsonUtil.fromJson(diaryJson, Diary.class);

            if (!diary.getAuthor().equals(username)) {
                return false; // 不是作者无法删除
            }

            return FileUtil.deleteFile(diaryFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Diary> getDiariesByAuthor(String author) {
        List<Diary> diaries = new ArrayList<>();
        List<String> files = FileUtil.getAllFilesInDirectory(DIARY_DIR);

        for (String file : files) {
            if (file.endsWith(".json")) {
                try {
                    String diaryJson = FileUtil.readFromFile(DIARY_DIR + "\\" + file);
                    Diary diary = JsonUtil.fromJson(diaryJson, Diary.class);

                    if (diary.getAuthor().equals(author)) {
                        diaries.add(diary);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return diaries;
    }

    public List<Diary> getAllDiaries() {
        List<Diary> diaries = new ArrayList<>();
        List<String> files = FileUtil.getAllFilesInDirectory(DIARY_DIR);

        for (String file : files) {
            if (file.endsWith(".json")) {
                try {
                    String diaryJson = FileUtil.readFromFile(DIARY_DIR + "\\" + file);
                    Diary diary = JsonUtil.fromJson(diaryJson, Diary.class);
                    diaries.add(diary);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return diaries;
    }

    public Diary getDiaryById(String diaryId) {
        String diaryFilePath = DIARY_DIR + "\\" + diaryId + ".json";
        File diaryFile = new File(diaryFilePath);

        if (!diaryFile.exists()) {
            return null;
        }

        try {
            String diaryJson = FileUtil.readFromFile(diaryFilePath);
            return JsonUtil.fromJson(diaryJson, Diary.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
