package cn.edu.zju.temp.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.LocalDate;
import java.util.Set;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties("file")
public class LocalEnv {

    /**
     * 文件保存路径
     */
    private String savePath;

    private String zipPath;

    private String unzipPath;

    private String imgPath;

    private String ossimgPath;

    private String videoFramePath;

    private String videoPath;

    private String staticPath;

    /**
     * 文件临时保存路径
     */
    public String createDateTempDir() {
        LocalDate now = LocalDate.now();
        String day = String.valueOf(now.getYear()) +
                now.getMonthValue() +
                now.getDayOfMonth();
        File dir = new File(savePath + File.separator + day);
        try {
            FileUtils.forceMkdir(dir);
            to777(dir.toPath());
        } catch (IOException e) {
            log.error("创建目录失败", e);
            return null;
        } catch (Exception e) {
            log.error("赋予权限失败", e);
            return null;
        }
        return day;
    }

    public static void to777(Path path) throws IOException {
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");
        Files.setPosixFilePermissions(path, perms);
    }

    /**
     * 获取数据集保存数据的根目录
     *
     * @param isTemp 如果已存在则删除
     */
    public File getDatasetDir(Integer datasetId, boolean isTemp) {
        File file = new File(savePath, datasetId.toString());
        try {
            if (isTemp) {
                if (file.exists()) {
                    FileUtils.forceDelete(file);
                }
            }
            FileUtils.forceMkdir(file);

            to777(file.toPath());
        } catch (IOException e) {
            log.error("创建目录失败", e);
            return null;
        }
        return file;
    }

    public String getFileSavePath(String saveUri) {
        String path = savePath;
        if (path.endsWith(File.separator)) {
            path = path + saveUri;
        } else {
            path = path + File.separator + saveUri;
        }
        return path;
    }


}
