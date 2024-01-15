package de.tum.ls4.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.stream.Stream;

public class FileUtils {
    public static void cleanDir(Path path) {
        try {
            try (Stream<Path> pathStream = Files.walk(path)) {
                pathStream.sorted(Comparator.reverseOrder())
                        .forEach(p -> {
                            try {
                                Files.deleteIfExists(p);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyContentOfTo(Path sourceDir, Path destinationDir) {
        try (var walk = Files.walk(sourceDir)) {
            walk.forEach(source -> {
                Path destination = destinationDir.resolve(sourceDir.relativize(source));
                try {
                    Files.copy(source, destination);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Path getResourceFolderPath(String folder) {
        URL res = FileUtils.class.getClassLoader().getResource(folder);
        Path path = null;
        try {
            path = Paths.get(res.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return path;
    }

    public static Path copySiteToLiveFolder(String site, LocalDate date) {
        Path sourceDir = getResourceFolderPath("sites").resolve(site).resolve(date.toString());
        Path destinationDir = getResourceFolderPath("sites").resolve(site + "-live");
        cleanDir(destinationDir);
        copyContentOfTo(sourceDir, destinationDir);
        return destinationDir;
    }
}
