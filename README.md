# CE-216 Project

Welcome to the **CE-216 Project** repository!  
This project is developed using **Gradle** and **JavaFX**, with additional libraries for handling JSON files. Below, you will find detailed instructions on installation, usage, and building the project from source.

---

## Table of Contents

- [Installation Using MSI Installer](#installation-using-msi-installer)  
- [Executable (.exe) Information](#executable-exe-information)  
- [Running the Project from Source Code](#running-the-project-from-source-code)  
- [Building the Project](#building-the-project)  

---

## Installation Using MSI Installer

A Windows MSI installer file is provided to simplify the installation process.

### Installation Steps

1. Locate the `CE-216-Project.msi` file in the repository.
2. Double-click the MSI file to launch the installer.
3. Follow the on-screen instructions by clicking **Next** through each step.
4. During installation, a shortcut will automatically be created on your **Desktop** for easy access.
5. Complete the installation by clicking **Finish**.

> **Note:** After installation, you can launch the application using the desktop shortcut.

---

## Executable (.exe) Information

The project includes a standalone executable file (`CE-216-Project.exe`) bundled with necessary resource folders.

- The `.exe` file is located inside the repository.
- **Important:** For the application to function correctly, the `.exe` must be used together with its accompanying folders. Running the `.exe` alone (without these folders) may result in errors or unexpected behavior.
- The MSI installer packages this executable along with all required files to ensure proper functionality.

---

## Running the Project from Source Code

If you prefer to compile and run the project manually from source:

### Prerequisites

- Java Development Kit (JDK) 11 or higher installed.
- JavaFX SDK (compatible with your JDK version) downloaded.

### Steps to Compile and Run

1. Open a terminal or command prompt.
2. Navigate to the `src` directory containing the Java source files.
3. Compile the source code with the following command:

   ```bash
   javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml *.java
   ```

4. Run the application using:

   ```bash
   java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml YourMainClass
   ```

Alternatively, you can run the pre-built JAR file included in the repository by executing:

```bash
java -jar CE-216-Project.jar
```

---

## Building the Project

This project uses **Gradle** as its build tool.

### Build Instructions

1. Ensure Gradle and JDK are properly installed and configured.
2. Open a terminal or command prompt at the root of the project.
3. Run the build command:

   ```bash
   ./gradlew build
   ```

This will:

- Compile the source code.
- Package the application into a JAR file.
- Prepare the MSI installer for Windows.
- Include all necessary dependencies such as JavaFX and external JSON libraries.

---

## Summary

| Feature               | Details                                                                            |
|-----------------------|------------------------------------------------------------------------------------|
| Installer             | MSI installer with easy, step-by-step setup and Desktop shortcut creation          |
| Executable (.exe)     | Must be run together with its resource folders for proper operation                |
| Running from Source   | Compile with `javac` using JavaFX modules, or run included JAR with `java -jar`    |
| Build Tool            | Gradle managing dependencies and building the project including JavaFX and JSON libs |

---

If you encounter any issues or have questions, please feel free to open an issue in this repository.

Thank you for using the CE-216 Project!

---

*Last updated: 2025-05-20*
