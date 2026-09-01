package com.smartorganizer;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Main extends Application {

    private Path selectedFolder;

    private final Label totalFilesLabel = new Label("0");
    private final Label imageLabel = new Label("0");
    private final Label documentLabel = new Label("0");
    private final Label videoLabel = new Label("0");
    private final Label musicLabel = new Label("0");
    private final Label duplicateLabel = new Label("0");

    private final Label folderLabel =
            new Label("No folder selected");

    private final Label statusLabel =
            new Label("Ready");

    private final TextArea resultsArea =
            new TextArea();

    private final ProgressBar progressBar =
            new ProgressBar(0);

    /*
     * Stores the duplicate groups found during the scan.
     * This allows us to select a duplicate file later.
     */
    private Map<String, List<Path>> duplicateGroups;

    private final ListView<Path> duplicateList =
            new ListView<>();

    private final DuplicateManager duplicateManager =
            new DuplicateManager();

    @Override
    public void start(Stage stage) {

        Label title =
                new Label("Smart File Organizer");

        title.setStyle(
                "-fx-font-size: 30px;" +
                "-fx-font-weight: bold;"
        );

        Label subtitle =
                new Label(
                        "Organize files • Detect duplicates • Save storage"
                );

        subtitle.setStyle(
                "-fx-font-size: 15px;"
        );

        // =========================
        // FOLDER SECTION
        // =========================

        Label folderTitle =
                new Label("Selected Folder");

        folderTitle.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;"
        );

        folderLabel.setStyle(
                "-fx-font-size: 14px;"
        );

        Button selectButton =
                new Button("📁 Select Folder");

        Button organizeButton =
                new Button("🗂 Organize Files");

        Button duplicateButton =
                new Button("🔍 Find Duplicates");

        Button clearButton =
                new Button("↻ Clear");

        selectButton.setPrefWidth(150);
        organizeButton.setPrefWidth(150);
        duplicateButton.setPrefWidth(160);
        clearButton.setPrefWidth(100);

        selectButton.setOnAction(
                e -> selectFolder(stage)
        );

        organizeButton.setOnAction(
                e -> organizeFiles()
        );

        duplicateButton.setOnAction(
                e -> findDuplicates()
        );

        clearButton.setOnAction(
                e -> clearDashboard()
        );

        HBox buttonBox =
                new HBox(
                        12,
                        selectButton,
                        organizeButton,
                        duplicateButton,
                        clearButton
                );

        buttonBox.setAlignment(
                Pos.CENTER_LEFT
        );

        VBox folderBox =
                new VBox(
                        10,
                        folderTitle,
                        folderLabel,
                        buttonBox
                );

        folderBox.setPadding(
                new Insets(18)
        );

        folderBox.setStyle(
                "-fx-background-color: #f4f6f8;" +
                "-fx-background-radius: 12;"
        );

        // =========================
        // DASHBOARD
        // =========================

        VBox totalCard =
                createCard(
                        "Total Files",
                        totalFilesLabel
                );

        VBox imageCard =
                createCard(
                        "Images",
                        imageLabel
                );

        VBox documentCard =
                createCard(
                        "Documents",
                        documentLabel
                );

        VBox videoCard =
                createCard(
                        "Videos",
                        videoLabel
                );

        VBox musicCard =
                createCard(
                        "Music",
                        musicLabel
                );

        VBox duplicateCard =
                createCard(
                        "Duplicate Groups",
                        duplicateLabel
                );

        GridPane dashboard =
                new GridPane();

        dashboard.setHgap(12);
        dashboard.setVgap(12);

        dashboard.add(totalCard, 0, 0);
        dashboard.add(imageCard, 1, 0);
        dashboard.add(documentCard, 2, 0);

        dashboard.add(videoCard, 0, 1);
        dashboard.add(musicCard, 1, 1);
        dashboard.add(duplicateCard, 2, 1);

        // =========================
        // PROGRESS
        // =========================

        Label progressTitle =
                new Label("Operation Progress");

        progressTitle.setStyle(
                "-fx-font-weight: bold;"
        );

        progressBar.setPrefWidth(500);

        // =========================
        // DUPLICATE LIST
        // =========================

        Label duplicateTitle =
                new Label("Duplicate Files");

        duplicateTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;"
        );

        duplicateList.setPrefHeight(150);

        Button deleteButton =
                new Button("🗑 Delete Selected");

        Button moveButton =
                new Button("📦 Move Selected");

        deleteButton.setPrefWidth(170);
        moveButton.setPrefWidth(170);

        deleteButton.setOnAction(
                e -> deleteSelectedDuplicate()
        );

        moveButton.setOnAction(
                e -> moveSelectedDuplicate(stage)
        );

        HBox duplicateButtons =
                new HBox(
                        12,
                        deleteButton,
                        moveButton
                );

        duplicateButtons.setAlignment(
                Pos.CENTER_LEFT
        );

        VBox duplicateBox =
                new VBox(
                        10,
                        duplicateTitle,
                        duplicateList,
                        duplicateButtons
                );

        // =========================
        // RESULTS
        // =========================

        Label resultsTitle =
                new Label("Results");

        resultsTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;"
        );

        resultsArea.setEditable(false);
        resultsArea.setWrapText(true);
        resultsArea.setPrefHeight(180);

        // =========================
        // MAIN LAYOUT
        // =========================

        VBox mainLayout =
                new VBox(
                        18,
                        title,
                        subtitle,
                        folderBox,
                        dashboard,
                        progressTitle,
                        progressBar,
                        duplicateBox,
                        resultsTitle,
                        resultsArea,
                        statusLabel
                );

        mainLayout.setPadding(
                new Insets(25)
        );

        mainLayout.setStyle(
                "-fx-background-color: white;"
        );

        ScrollPane scrollPane =
                new ScrollPane(mainLayout);

        scrollPane.setFitToWidth(true);

        Scene scene =
                new Scene(
                        scrollPane,
                        1000,
                        800
                );

        stage.setTitle(
                "Smart File Organizer"
        );

        stage.setScene(scene);

        stage.show();
    }

    // ==========================================
    // CARD
    // ==========================================

    private VBox createCard(
            String title,
            Label value
    ) {

        Label titleLabel =
                new Label(title);

        titleLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;"
        );

        value.setStyle(
                "-fx-font-size: 25px;" +
                "-fx-font-weight: bold;"
        );

        VBox card =
                new VBox(
                        8,
                        titleLabel,
                        value
                );

        card.setAlignment(
                Pos.CENTER_LEFT
        );

        card.setPrefWidth(300);
        card.setPrefHeight(100);

        card.setPadding(
                new Insets(15)
        );

        card.setStyle(
                "-fx-background-color: #f4f6f8;" +
                "-fx-background-radius: 12;"
        );

        return card;
    }

    // ==========================================
    // SELECT FOLDER
    // ==========================================

    private void selectFolder(Stage stage) {

        DirectoryChooser chooser =
                new DirectoryChooser();

        chooser.setTitle(
                "Select Folder"
        );

        File selected =
                chooser.showDialog(stage);

        if (selected != null) {

            selectedFolder =
                    selected.toPath();

            folderLabel.setText(
                    selectedFolder.toString()
            );

            resultsArea.clear();

            duplicateList.getItems().clear();

            duplicateGroups = null;

            statusLabel.setText(
                    "Folder selected successfully."
            );

            scanFolder();
        }
    }

    // ==========================================
    // SCAN FOLDER
    // ==========================================

    private void scanFolder() {

        if (selectedFolder == null) {
            return;
        }

        try {

            int total = 0;
            int images = 0;
            int documents = 0;
            int videos = 0;
            int music = 0;

            try (var stream =
                         Files.walk(selectedFolder)) {

                for (Path path :
                        stream.filter(
                                Files::isRegularFile
                        ).toList()) {

                    total++;

                    switch (
                            FileCategory.from(
                                    path.getFileName()
                                            .toString()
                            )
                    ) {

                        case IMAGES ->
                                images++;

                        case DOCUMENTS ->
                                documents++;

                        case VIDEOS ->
                                videos++;

                        case MUSIC ->
                                music++;

                        default -> {
                        }
                    }
                }
            }

            totalFilesLabel.setText(
                    String.valueOf(total)
            );

            imageLabel.setText(
                    String.valueOf(images)
            );

            documentLabel.setText(
                    String.valueOf(documents)
            );

            videoLabel.setText(
                    String.valueOf(videos)
            );

            musicLabel.setText(
                    String.valueOf(music)
            );

        } catch (IOException e) {

            statusLabel.setText(
                    "Unable to scan folder."
            );
        }
    }

    // ==========================================
    // ORGANIZE
    // ==========================================

    private void organizeFiles() {

        if (selectedFolder == null) {

            showAlert(
                    "Please select a folder first."
            );

            return;
        }

        Alert confirm =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirm.setTitle(
                "Organize Files"
        );

        confirm.setHeaderText(
                "Organize files in this folder?"
        );

        confirm.setContentText(
                "Files will be moved into category folders."
        );

        if (confirm.showAndWait().orElse(
                ButtonType.CANCEL
        ) != ButtonType.OK) {

            return;
        }

        Task<Integer> task =
                new Task<>() {

                    @Override
                    protected Integer call()
                            throws Exception {

                        updateProgress(
                                0.3,
                                1.0
                        );

                        int result =
                                new FileOrganizer()
                                        .organize(
                                                selectedFolder
                                        );

                        updateProgress(
                                1.0,
                                1.0
                        );

                        return result;
                    }
                };

        progressBar
                .progressProperty()
                .bind(
                        task.progressProperty()
                );

        task.setOnSucceeded(
                e -> {

                    progressBar.progressProperty()
                            .unbind();

                    progressBar.setProgress(1);

                    int count =
                            task.getValue();

                    statusLabel.setText(
                            "Successfully organized "
                                    + count
                                    + " file(s)."
                    );

                    resultsArea.setText(
                            "File organization completed.\n\n"
                                    + "Files organized: "
                                    + count
                    );

                    scanFolder();
                }
        );

        task.setOnFailed(
                e -> {

                    progressBar.progressProperty()
                            .unbind();

                    progressBar.setProgress(0);

                    statusLabel.setText(
                            "File organization failed."
                    );
                }
        );

        new Thread(task).start();
    }

    // ==========================================
    // FIND DUPLICATES
    // ==========================================

    private void findDuplicates() {

        if (selectedFolder == null) {

            showAlert(
                    "Please select a folder first."
            );

            return;
        }

        statusLabel.setText(
                "Scanning for duplicate files..."
        );

        resultsArea.clear();
        duplicateList.getItems().clear();

        progressBar.setProgress(
                ProgressBar.INDETERMINATE_PROGRESS
        );

        Task<Map<String, List<Path>>> task =
                new Task<>() {

                    @Override
                    protected Map<String, List<Path>>
                    call() throws Exception {

                        return new DuplicateDetector()
                                .find(
                                        selectedFolder
                                );
                    }
                };

        task.setOnSucceeded(
                e -> {

                    progressBar.setProgress(1);

                    duplicateGroups =
                            task.getValue();

                    duplicateLabel.setText(
                            String.valueOf(
                                    duplicateGroups.size()
                            )
                    );

                    resultsArea.clear();

                    if (duplicateGroups.isEmpty()) {

                        resultsArea.appendText(
                                "✓ No duplicate files found.\n"
                        );

                        statusLabel.setText(
                                "Duplicate scan completed. No duplicates found."
                        );

                        return;
                    }

                    resultsArea.appendText(
                            "Duplicate files found:\n\n"
                    );

                    for (
                            Map.Entry<String,
                                    List<Path>> entry
                            : duplicateGroups.entrySet()
                    ) {

                        resultsArea.appendText(
                                "SHA-256:\n"
                                        + entry.getKey()
                                        + "\n"
                        );

                        for (
                                Path path
                                : entry.getValue()
                        ) {

                            duplicateList
                                    .getItems()
                                    .add(path);

                            resultsArea.appendText(
                                    "  • "
                                            + path
                                            + "\n"
                            );
                        }

                        resultsArea.appendText(
                                "\n"
                        );
                    }

                    statusLabel.setText(
                            "Duplicate scan completed. Select a file to delete or move it."
                    );
                }
        );

        task.setOnFailed(
                e -> {

                    progressBar.setProgress(0);

                    statusLabel.setText(
                            "Duplicate scan failed."
                    );
                }
        );

        new Thread(task).start();
    }

    // ==========================================
    // DELETE DUPLICATE
    // ==========================================

    private void deleteSelectedDuplicate() {

        Path selected =
                duplicateList
                        .getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {

            showAlert(
                    "Please select a duplicate file first."
            );

            return;
        }

        Alert confirm =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirm.setTitle(
                "Delete Duplicate"
        );

        confirm.setHeaderText(
                "Delete this file?"
        );

        confirm.setContentText(
                selected.toString()
        );

        if (confirm.showAndWait().orElse(
                ButtonType.CANCEL
        ) != ButtonType.OK) {

            return;
        }

        try {

            duplicateManager
                    .deleteDuplicate(selected);

            duplicateList
                    .getItems()
                    .remove(selected);

            resultsArea.appendText(
                    "\nDeleted:\n"
                            + selected
                            + "\n"
            );

            statusLabel.setText(
                    "Duplicate file deleted successfully."
            );

            scanFolder();

        } catch (IOException e) {

            showAlert(
                    "Could not delete the file:\n"
                            + e.getMessage()
            );
        }
    }

    // ==========================================
    // MOVE DUPLICATE
    // ==========================================

    private void moveSelectedDuplicate(
            Stage stage
    ) {

        Path selected =
                duplicateList
                        .getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {

            showAlert(
                    "Please select a duplicate file first."
            );

            return;
        }

        DirectoryChooser chooser =
                new DirectoryChooser();

        chooser.setTitle(
                "Select Destination Folder"
        );

        File destination =
                chooser.showDialog(stage);

        if (destination == null) {
            return;
        }

        try {

            Path moved =
                    duplicateManager.moveDuplicate(
                            selected,
                            destination.toPath()
                    );

            duplicateList
                    .getItems()
                    .remove(selected);

            resultsArea.appendText(
                    "\nMoved:\n"
                            + selected
                            + "\nTo:\n"
                            + moved
                            + "\n"
            );

            statusLabel.setText(
                    "Duplicate file moved successfully."
            );

            scanFolder();

        } catch (IOException e) {

            showAlert(
                    "Could not move the file:\n"
                            + e.getMessage()
            );
        }
    }

    // ==========================================
    // CLEAR
    // ==========================================

    private void clearDashboard() {

        selectedFolder = null;

        duplicateGroups = null;

        folderLabel.setText(
                "No folder selected"
        );

        totalFilesLabel.setText("0");
        imageLabel.setText("0");
        documentLabel.setText("0");
        videoLabel.setText("0");
        musicLabel.setText("0");
        duplicateLabel.setText("0");

        duplicateList
                .getItems()
                .clear();

        resultsArea.clear();

        statusLabel.setText(
                "Ready"
        );

        progressBar.setProgress(0);
    }

    // ==========================================
    // ALERT
    // ==========================================

    private void showAlert(
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.WARNING
                );

        alert.setTitle(
                "Smart File Organizer"
        );

        alert.setHeaderText(null);

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }

    // ==========================================
    // MAIN
    // ==========================================

    public static void main(
            String[] args
    ) {

        launch(args);
    }
}