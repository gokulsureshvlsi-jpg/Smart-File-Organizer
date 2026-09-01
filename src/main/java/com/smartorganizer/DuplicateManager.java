package com.smartorganizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class DuplicateManager {

    public void deleteDuplicate(Path file) throws IOException {

        if (file == null || !Files.exists(file)) {
            throw new IOException("File does not exist.");
        }

        Files.delete(file);
    }

    public Path moveDuplicate(
            Path file,
            Path destinationFolder
    ) throws IOException {

        if (file == null || !Files.exists(file)) {
            throw new IOException("File does not exist.");
        }

        Files.createDirectories(destinationFolder);

        Path destination =
                destinationFolder.resolve(
                        file.getFileName()
                );

        int counter = 1;

        while (Files.exists(destination)) {

            String fileName =
                    file.getFileName().toString();

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

            destination =
                    destinationFolder.resolve(
                            name
                                    + "_duplicate_"
                                    + counter
                                    + extension
                    );

            counter++;
        }

        return Files.move(
                file,
                destination,
                StandardCopyOption.REPLACE_EXISTING
        );
    }
}