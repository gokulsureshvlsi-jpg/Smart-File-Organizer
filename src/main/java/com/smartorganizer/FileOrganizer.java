package com.smartorganizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileOrganizer {

    public int organize(Path root) throws IOException {

        int movedFiles = 0;

        // Find duplicate files first
        Map<String, List<Path>> duplicates =
                new DuplicateDetector().find(root);

        // Create a set of duplicate files
        Map<Path, Boolean> duplicateFiles =
                new HashMap<>();

        for (List<Path> group : duplicates.values()) {

            // Keep the first file as the original
            // and treat the remaining files as duplicates.
            for (int i = 1; i < group.size(); i++) {
                duplicateFiles.put(group.get(i), true);
            }
        }

        try (var files = Files.walk(root, 1)) {

            for (Path file : files
                    .filter(Files::isRegularFile)
                    .toList()) {

                String fileName =
                        file.getFileName().toString();

                // =====================================
                // DUPLICATE FILE
                // =====================================

                if (duplicateFiles.containsKey(file)) {

                    Path duplicateFolder =
                            root.resolve("Duplicates");

                    Files.createDirectories(
                            duplicateFolder
                    );

                    Path destination =
                            getUniqueDestination(
                                    duplicateFolder,
                                    fileName
                            );

                    Files.move(
                            file,
                            destination,
                            StandardCopyOption.REPLACE_EXISTING
                    );

                    movedFiles++;

                    continue;
                }

                // =====================================
                // NORMAL FILE
                // =====================================

                FileCategory category =
                        FileCategory.from(fileName);

                String folderName =
                        switch (category) {

                            case IMAGES ->
                                    "Images";

                            case DOCUMENTS ->
                                    "Documents";

                            case VIDEOS ->
                                    "Videos";

                            case MUSIC ->
                                    "Music";

                            case ARCHIVES ->
                                    "Archives";

                            case OTHERS ->
                                    "Others";
                        };

                Path categoryFolder =
                        root.resolve(folderName);

                Files.createDirectories(
                        categoryFolder
                );

                Path destination =
                        getUniqueDestination(
                                categoryFolder,
                                fileName
                        );

                Files.move(
                        file,
                        destination,
                        StandardCopyOption.REPLACE_EXISTING
                );

                movedFiles++;
            }
        }

        return movedFiles;
    }

    // ==========================================
    // CREATE UNIQUE FILE NAME
    // ==========================================

    private Path getUniqueDestination(
            Path folder,
            String fileName
    ) {

        Path destination =
                folder.resolve(fileName);

        if (!Files.exists(destination)) {
            return destination;
        }

        int dot =
                fileName.lastIndexOf('.');

        String name =
                dot > 0
                        ? fileName.substring(0, dot)
                        : fileName;

        String extension =
                dot > 0
                        ? fileName.substring(dot)
                        : "";

        int counter = 1;

        while (Files.exists(destination)) {

            destination =
                    folder.resolve(
                            name
                                    + "_"
                                    + counter
                                    + extension
                    );

            counter++;
        }

        return destination;
    }
}