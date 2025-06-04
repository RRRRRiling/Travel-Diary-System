import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ScenicSpotService {
    private static final String SCENIC_SPOT_FILE = "D:\\all_demo\\data\\maps\\overview\\view.json";

    public List<ScenicSpot> getAllScenicSpots() {
        try {
            String json = FileUtil.readFromFile(SCENIC_SPOT_FILE);
            return JsonUtil.fromJsonList(json, new TypeReference<List<ScenicSpot>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 原有搜索方法
    public List<ScenicSpot> searchScenicSpots(String keyword) {
        List<ScenicSpot> spots = getAllScenicSpots();
        if (spots == null) {
            return null;
        }

        return spots.stream()
                .filter(spot -> spot.getName().contains(keyword) ||
                        spot.getDescription().contains(keyword) ||
                        spot.getLocation().contains(keyword))
                .sorted(Comparator.comparing(ScenicSpot::getName)) // 按名称排序
                .collect(Collectors.toList());
    }

    // 新增按标签搜索方法
    public List<ScenicSpot> searchByTag(String tag) {
        List<ScenicSpot> spots = getAllScenicSpots();
        if (spots == null) {
            return null;
        }

        return spots.stream()
                .filter(spot -> spot.getTags() != null && spot.getTags().contains(tag))
                .sorted(Comparator.comparing(ScenicSpot::getName)) // 按名称排序
                .collect(Collectors.toList());
    }

    // 综合搜索方法（关键词+标签）
    public List<ScenicSpot> advancedSearch(String keyword, String tag) {
        List<ScenicSpot> spots = getAllScenicSpots();
        if (spots == null) {
            return null;
        }

        return spots.stream()
                .filter(spot ->
                        (keyword == null ||
                                spot.getName().contains(keyword) ||
                                spot.getDescription().contains(keyword) ||
                                spot.getLocation().contains(keyword)) &&
                                (tag == null ||
                                        (spot.getTags() != null && spot.getTags().contains(tag)))
                )
                .sorted(Comparator.comparing(ScenicSpot::getName)) // 按名称排序
                .collect(Collectors.toList());
    }

    // 获取所有可用标签
    public Set<String> getAllTags() {
        List<ScenicSpot> spots = getAllScenicSpots();
        if (spots == null) {
            return new HashSet<>();
        }

        return spots.stream()
                .filter(spot -> spot.getTags() != null)
                .flatMap(spot -> spot.getTags().stream())
                .collect(Collectors.toSet());
    }
}