package cn.edu.zju.temp.util;

import cn.edu.zju.temp.constant.DirName;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathUtil {

    // 查看数据集详情的时候展示的图片数量
    private static final int DEMO_SIZE = 4;

    private static final String[] IMAGE_EXT = new String[]{
            "jpg", "jpeg", "png", "images"
    };

    public static String pathToUrl(Path filePath, String parentDir) {
        Path parentDirPath = Paths.get(parentDir);
        String parentName = parentDirPath.getFileName().toString();

        Path relativePath = parentDirPath.relativize(filePath);
        return parentName + "/" + relativePath;
    }

    public static List<String> getUriByNames(Path imageDirPath, List<Path> fileNames, String parentDir) {
        List<String> result = new ArrayList<>();
        for (Path fileName : fileNames) {
            Path filePath = imageDirPath.resolve(fileName);
            result.add(pathToUrl(filePath, parentDir));
        }
        return result;
    }

    public static List<String> getUriByNamePrev(Path imageDirPath, List<Path> fileNames, String parentDir) {
        List<String> filePrevs = fileNames.stream()
                .map(Path::toString)
                .map(s -> s.substring(0, s.lastIndexOf(".")))
                .collect(Collectors.toList());
        try (Stream<Path> list = Files.list(imageDirPath)) {
            return list.filter(path -> {
                        String fileName = path.getFileName().toString();
                        return filePrevs.stream()
                                .anyMatch(fileName::startsWith);
                    })
                    .sorted((a, b) -> {
                        int i = 0, j = 0;
                        int idx = 0;
                        for (String filePrev : filePrevs) {
                            if (a.getFileName().toString().startsWith(filePrev)) {
                                i = idx;
                            }
                            if (b.getFileName().toString().startsWith(filePrev)) {
                                j = idx;
                            }
                            idx += 1;
                        }
                        return Integer.compare(i, j);
                    })
                    .map(path -> pathToUrl(path, parentDir))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public static List<String> getFileUri(File dir, String parentDir) {
        if (!dir.exists() || !dir.isDirectory()) {
            return Collections.emptyList();
        }
        Path dirPath = dir.toPath();
        try (Stream<Path> paths = Files.list(dirPath)) {
            return paths.limit(DEMO_SIZE)
                    .map(p -> pathToUrl(p, parentDir))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private static List<Path> readDirImages(Path parentPath) {
        if (!Files.exists(parentPath) || !Files.isDirectory(parentPath)) {
            return Collections.emptyList();
        }
        try (Stream<Path> list = Files.list(parentPath)) {
            return list.limit(DEMO_SIZE).collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private static List<Path> readDirByExts(Path parentPath) {
        List<Path> result = new ArrayList<>();
        for (String ext : IMAGE_EXT) {
            Path imageDirPath = parentPath.resolve(ext);
            List<Path> list = readDirImages(imageDirPath);
            int len = Math.min(list.size(), DEMO_SIZE - result.size());
            for (int i = 0; i < len; i++) {
                result.add(list.get(i));
            }
            if (result.size() == DEMO_SIZE) {
                break;
            }
        }
        return result;
    }

    /**
     * 获取基础的数据集指定数量的数据
     */
    public static List<Path> getBaseDatasetPaths(Path datasetParentPath) {
        Path benignPath = datasetParentPath.resolve(DirName.BENIGN_DIR_NAME);
        Path malignantPath = datasetParentPath.resolve(DirName.MALIGNANT_DIR_NAME);

        List<Path> paths = readDirByExts(benignPath);
        if (paths.size() == DEMO_SIZE) {
            return paths;
        }
        List<Path> malPaths = readDirByExts(malignantPath);
        int len = Math.min(malPaths.size(), DEMO_SIZE - paths.size());
        for (int i = 0; i < len; i++) {
            paths.add(malPaths.get(i));
        }
        return paths;
    }

    /**
     * 从文件夹中获取示例图片的uri
     */
    public static List<String> getDemoUri(File dir, String parentDir) {
        if (!dir.isDirectory()) {
            return Collections.emptyList();
        }

        for (String imageExt : IMAGE_EXT) {
            File imgDir = new File(dir, imageExt);
            if (imgDir.exists() && imgDir.isDirectory()) {
                List<String> result = getFileUri(imgDir, parentDir);
                if (!result.isEmpty()) {
                    return result;
                }
            }
        }
        return Collections.emptyList();
    }

}
