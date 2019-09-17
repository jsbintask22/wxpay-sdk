package cn.jsbintask.wxpay.utils;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jsbintask@gmail.com
 * @date 2019/7/23 9:54
 */
@UtilityClass
public class FileUtils {

    /**
     * @param path 文件路径
     *             如果当前文件的父目录不存在将创建。
     */
    public File createFile(String path) throws IOException {
        File file = new File(path);
        if (file.isDirectory()) {
            throw new UnsupportedOperationException("should you need a file path? instead of directory path.");
        }

        if (!file.exists()) {
            File parentFile = file.getParentFile();

            if (parentFile != null && !parentFile.exists()) {
                if (isWinOs()) {
                    Files.createDirectories(parentFile.toPath());
                } else {
                    FileAttribute<Set<PosixFilePermission>> fileAttribute = PosixFilePermissions.asFileAttribute(Arrays.stream(PosixFilePermission.values()).collect(Collectors.toSet()));
                    Files.createDirectories(parentFile.toPath(), fileAttribute);
                }
            }

            if (isWinOs()) {
                Files.createFile(file.toPath());
            } else {
                FileAttribute<Set<PosixFilePermission>> fileAttribute = PosixFilePermissions.asFileAttribute(Arrays.stream(PosixFilePermission.values()).collect(Collectors.toSet()));
                Files.createFile(file.toPath(), fileAttribute);
            }
        }
        return file;
    }

    public boolean isWinOs() {
        return System.getProperty("os.name").toLowerCase().startsWith("win");
    }
}
