# 📁 Smart File Organizer

A JavaFX desktop application that automatically organizes files into categories and detects duplicate files using SHA-256 hashing.

[![Download for Windows](https://img.shields.io/badge/Download-Windows%20EXE-blue?logo=windows)](../../releases/latest/download/Smart.File.Organizer-1.0.exe)
[![Release](https://img.shields.io/badge/Release-v1.0.0-green)](../../releases/latest)

---

## 📸 Screenshots

### Main Application

![Smart File Organizer Main UI](screenshots/main-ui.png)

### File Organization

![File Organization](screenshots/organize-files.png)

### Duplicate Detection

![Duplicate Detection](screenshots/duplicates.png)

---

## ✨ Features

- 📂 Automatically organizes files into categories
- 🖼️ Organizes images
- 📄 Organizes documents
- 🎬 Organizes videos
- 🎵 Organizes music
- 📦 Organizes archive files
- 🔍 Detects duplicate files
- 🔐 Uses SHA-256 hashing for duplicate detection
- 🗂️ Provides duplicate file management
- 🖥️ Simple JavaFX desktop interface
- 💾 Works directly with the local file system
- 🗄️ No database required

---

## 📁 File Categories

| Category | Examples |
|---|---|
| 🖼️ Images | JPG, PNG, GIF, SVG |
| 📄 Documents | PDF, DOCX, TXT, XLSX, PPTX |
| 🎬 Videos | MP4, AVI, MKV, MOV |
| 🎵 Music | MP3, WAV, FLAC, AAC |
| 📦 Archives | ZIP, RAR, 7Z, TAR |
| 📁 Others | Other file types |

---

## 🔍 Duplicate File Detection

The application uses the **SHA-256 hashing algorithm** to identify files with the same content.

When duplicate files are detected, the application can help manage the duplicate copies.

This helps reduce unnecessary storage usage and keeps folders organized.

---

## ▶️ How to Use

### 1. Download

[⬇️ Download Smart File Organizer for Windows](../../releases/latest/download/Smart.File.Organizer-1.0.exe)

### 2. Launch the application

Run:

`Smart.File.Organizer-1.0.exe`

### 3. Select a folder

Choose the folder containing the files you want to organize.

### 4. Organize files

Click **Organize Files**.

The application categorizes files based on their file extensions.

### 5. Find duplicates

Click **Find Duplicates** to scan for duplicate files.

### 6. Manage duplicates

Select the duplicates and use **Move Selected** or **Delete Selected** as needed.

---

## 🛠️ Technologies Used

- ☕ Java 21
- 🎨 JavaFX
- 📦 Maven
- 🔐 SHA-256
- 🖥️ Windows

---

## 📋 System Requirements

- Windows 10 or later
- 64-bit Windows recommended
- No database required

---

## 📦 Project Structure

```text
Smart-File-Organizer
│
├── pom.xml
├── README.md
│
└── src
    └── main
        └── java
            └── com
                └── smartorganizer
                    ├── FileCategory.java
                    ├── FileOrganizer.java
                    ├── DuplicateDetector.java
                    ├── DuplicateManager.java
                    ├── Main.java
                    └── FileHasher.java
