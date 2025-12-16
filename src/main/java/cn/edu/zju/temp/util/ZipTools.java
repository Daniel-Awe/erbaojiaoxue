package cn.edu.zju.temp.util;

import cn.edu.zju.temp.config.LocalEnv;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Slf4j
public class ZipTools {

    public static boolean zip(File fileToZip, File targetFile) {
        try {
            FileOutputStream fos = new FileOutputStream(targetFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            File[] fileList = fileToZip.listFiles();
            if (fileList == null) {
                log.error("压缩文件夹不存在");
                return false;
            }

            String fileName;
            FileInputStream fis;
            for (File file : fileList) {
                if (file.isFile()) {
                    fileName = file.getName();
                    fis = new FileInputStream(file);

                    ZipEntry zipEntry = new ZipEntry(fileName);
                    zos.putNextEntry(zipEntry);
                    IOUtils.copy(fis, zos);

                    fis.close();
                } else if (file.isDirectory()) {
                    zipSubFolder(file, file.getName(), zos);
                }
            }

            zos.closeEntry();
            zos.close();
            fos.close();
        } catch (IOException e) {
            log.error("压缩文件失败", e);
            return false;
        }
        return true;
    }

    private static void zipSubFolder(File folderToZip, String folderName, ZipOutputStream zos) throws IOException {
        File[] fileList = folderToZip.listFiles();
        if (fileList == null) {
            return;
        }

        if (folderName.endsWith("/")) {
            folderName = folderName.substring(0, folderName.length() - 1);
        }

        FileInputStream fis;

        for (File file : fileList) {
            String fileName = file.getName();

            if (file.isFile()) {
                fileName = folderName + "/" + file.getName();
                fis = new FileInputStream(file);

                ZipEntry zipEntry = new ZipEntry(fileName);
                zos.putNextEntry(zipEntry);
                IOUtils.copy(fis, zos);
                fis.close();
            } else if (file.isDirectory()) {
                zipSubFolder(file, fileName, zos);
            }
        }
    }

    public static boolean unzip(File zipFile, File targetDir) {
        if (!targetDir.exists()) {
            try {
                FileUtils.forceMkdir(targetDir);
//                LocalEnv.to777(targetDir.toPath());
            } catch (IOException e) {
                log.error("Failed to create directory: {}", targetDir, e);
                return false;
            }
        }

        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                // 解压路径
                String filePath = targetDir.getPath() + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zipIn, filePath);
                } else {
//                    File dir = new File(filePath);
//                    if (!dir.mkdirs()) {
//                        throw new IOException("Failed to create directory: " + filePath);
//                    } else {
//                        LocalEnv.to777(dir.toPath());
//                    }
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        } catch (IOException e) {
            log.error("文件解压缩失败", e);
            return false;
        }
        return true;
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }


}
