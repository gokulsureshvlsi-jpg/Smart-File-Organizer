package com.smartorganizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuplicateDetector {

    public Map<String, List<Path>> find(Path root)
            throws IOException {

        Map<String, List<Path>> duplicates =
                new HashMap<>();

        try (var files = Files.walk(root)) {

            files.filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            String hash =
                                    FileHasher.sha256(path);

                            duplicates
                                    .computeIfAbsent(
                                            hash,
                                            k -> new ArrayList<>()
                                    )
                                    .add(path);

                        } catch (IOException e) {
                            System.out.println(
                                    "Could not read: " + path
                            );
                        }
                    });
        }

        duplicates.entrySet().removeIf(
                entry -> entry.getValue().size() < 2
        );

        return duplicates;
    }
}