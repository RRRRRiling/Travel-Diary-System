package org.example.traveldiary.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MapPathFinder {
    private static String mapDir;
    private static final Map<Integer, int[][]> matrixCache = new ConcurrentHashMap<>();

    @Value("${app.data.map-dir}")
    public void setMapDir(String dir) {
        mapDir = dir.replace("\\", "/"); // 统一路径格式
        validateMapDir();
    }

    @Cacheable(value = "pathCache", key = "{#scenicId, #start, #end}")
    public static List<Integer> findPath(int scenicId, int start, int end) {
        validateInput(scenicId, start, end);

        int[][] matrix = matrixCache.computeIfAbsent(scenicId, id -> {
            try {
                return loadMatrix(id);
            } catch (Exception e) {
                throw new RuntimeException("地图加载失败: " + e.getMessage());
            }
        });

        List<Integer> path = findShortestPath(matrix, start - 1, end - 1);
        if (path == null) {
            throw new RuntimeException("未找到有效路径");
        }
        return path.stream().map(node -> node + 1).toList();
    }

    private static void validateInput(int scenicId, int start, int end) {
        if (scenicId < 1 || scenicId > 200) throw new IllegalArgumentException("景区ID必须为1-200");
        if (start < 1 || start > 70) throw new IllegalArgumentException("起点编号必须为1-70");
        if (end < 1 || end > 70) throw new IllegalArgumentException("终点编号必须为1-70");
    }

    private static int[][] loadMatrix(int scenicId) throws IOException {
        String filePath = mapDir + "/" + scenicId + ".xlsx";
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int[][] matrix = new int[70][70];

            for (int i = 0; i < 70; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < 70; j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    matrix[i][j] = cell.getCellType() == CellType.NUMERIC ?
                            (int) cell.getNumericCellValue() : 0;
                }
            }
            return matrix;
        }
    }

    private static List<Integer> findShortestPath(int[][] matrix, int start, int end) {
        if (start == end) return Collections.singletonList(start);

        int n = matrix.length;
        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> visited = new HashMap<>();
        queue.add(start);
        visited.put(start, null);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (int neighbor = 0; neighbor < n; neighbor++) {
                if (matrix[current][neighbor] == 1 && !visited.containsKey(neighbor)) {
                    visited.put(neighbor, current);
                    if (neighbor == end) {
                        return buildPath(visited, start, end);
                    }
                    queue.add(neighbor);
                }
            }
        }
        return null;
    }

    private static List<Integer> buildPath(Map<Integer, Integer> visited, int start, int end) {
        LinkedList<Integer> path = new LinkedList<>();
        int node = end;
        while (node != start) {
            path.addFirst(node);
            node = visited.get(node);
        }
        path.addFirst(start);
        return path;
    }

    private static void validateMapDir() {
        Path path = Paths.get(mapDir);
        if (!path.toFile().exists()) {
            throw new IllegalStateException("地图目录不存在: " + path);
        }
        if (!path.toFile().canRead()) {
            throw new IllegalStateException("无权限读取地图目录: " + path);
        }
    }
}