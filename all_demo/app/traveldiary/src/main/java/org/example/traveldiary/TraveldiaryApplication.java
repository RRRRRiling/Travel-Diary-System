package org.example.traveldiary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@EnableCaching
@SpringBootApplication
public class TraveldiaryApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(TraveldiaryApplication.class);

    @Value("${app.data.map-dir}")
    private String mapDir;

    @Value("${app.data.user-dir}")
    private String userDir;

    @Value("${app.data.diary-dir}")
    private String diaryDir;

    @Value("${app.data.scenic-spots}")
    private String scenicSpotsFile;

    public static void main(String[] args) {
        SpringApplication.run(TraveldiaryApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            // 统一路径分隔符（兼容Windows/Linux）
            mapDir = mapDir.replace("\\", "/");

            // 验证地图目录
            validateDirectory(mapDir, "地图数据");

            // 验证其他数据目录
            ensureDirectoryExists(userDir);
            ensureDirectoryExists(diaryDir);
            ensureParentDirExists(scenicSpotsFile);

            logger.info("系统启动完成，数据目录已验证");
            logger.info("地图数据位置: {}", Paths.get(mapDir).toAbsolutePath());
        } catch (Exception e) {
            logger.error("系统启动失败：目录初始化错误", e);
            throw new IllegalStateException("系统启动失败", e);
        }
    }

    private void validateDirectory(String dirPath, String desc) throws Exception {
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            throw new IllegalStateException(desc + "目录不存在: " + path.toAbsolutePath());
        }
        if (!Files.isReadable(path)) {
            throw new IllegalStateException("无权限读取" + desc + "目录: " + path.toAbsolutePath());
        }
        logger.info("{}目录已验证: {}", desc, path.toAbsolutePath());
    }

    private void ensureDirectoryExists(String dirPath) throws Exception {
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            logger.info("创建目录: {}", path.toAbsolutePath());
        }
    }

    private void ensureParentDirExists(String filePath) throws Exception {
        Path parentDir = Paths.get(filePath).getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
            logger.info("创建父级目录: {}", parentDir.toAbsolutePath());
        }
    }
}