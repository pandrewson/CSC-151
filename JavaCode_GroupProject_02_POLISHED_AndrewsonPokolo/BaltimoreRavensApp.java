// GROUP PROJECT CONTRIBUTIONS:
// Shameerah Dixon - File cleanup and code polishing.
// Tannequa Whitehead - Updated the GUI/main menu to read and display CSV data.
// Pokolo Andrewson - Created and organized the players, coaches, and support staff CSV files.
// Parker Behagg - Added the program's CSV writing/activity log functionality.

// Individual contribution by Tannequa Whitehead:
// Created the welcome screen, name input, personalized greeting,
// main navigation menu, and repeating menu loop.

import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaltimoreRavensApp {

    // Individual contribution by Pokolo Andrewson:
    // Created and organized the CSV files used for players, coaches, and support staff.
    private static final String PLAYERS_FILE = "csv_files/ravens_players.csv";
    private static final String COACHES_FILE = "csv_files/ravens_coaches.csv";
    private static final String SUPPORT_STAFF_FILE = "csv_files/ravens_support_staff.csv";
    private static final String LOG_FILE = "csv_files/ravens_log.csv";

    public static void main(String[] args) {
        printToLog("Program", "Application started");

        JOptionPane.showMessageDialog(
                null,
                "Welcome to the Baltimore Ravens 2026 Team Application!"
        );

        String userName = JOptionPane.showInputDialog(
                null,
                "Please enter your name:"
        );

        if (userName == null || userName.trim().isEmpty()) {
            userName = "Guest";
        }

        printToLog(userName, "User entered application");

        JOptionPane.showMessageDialog(
                null,
                "Hello, " + userName + "! Welcome to Ravens Nation!"
        );

        String[] menuOptions = {"Players", "Coaches", "Support Staff", "Exit"};
        int selection;

        do {
            selection = JOptionPane.showOptionDialog(
                    null,
                    "What would you like to explore?",
                    "Baltimore Ravens 2026 Main Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    menuOptions,
                    menuOptions[0]
            );

            switch (selection) {
                case 0:
                    printToLog(userName, "Opened Players");
                    showCsvData(PLAYERS_FILE);
                    break;
                case 1:
                    printToLog(userName, "Opened Coaches");
                    showCsvData(COACHES_FILE);
                    break;
                case 2:
                    printToLog(userName, "Opened Support Staff");
                    showCsvData(SUPPORT_STAFF_FILE);
                    break;
                case 3:
                    printToLog(userName, "Exited application");
                    JOptionPane.showMessageDialog(
                            null,
                            "Thank you for visiting Ravens Nation!"
                    );
                    break;
                default:
                    // Ignore a closed dialog or an invalid selection.
            }
        } while (selection != 3 && selection != JOptionPane.CLOSED_OPTION);

        if (selection == JOptionPane.CLOSED_OPTION) {
            printToLog(userName, "Application closed");
        }
    }

    // Individual contribution by Tannequa Whitehead:
    // Updated the GUI data display so team information is read from the CSV files.
    public static String readCSVFile(String fileName) {
        StringBuilder list = new StringBuilder();
        File dataFile = findFile(fileName);

        if (dataFile == null) {
            return "Error reading file: " + fileName
                    + "\n\nMake sure the csv_files folder is in your project folder.";
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                line = line.replace("\"", "");
                String[] data = line.split(",", 2);

                if (data.length == 2) {
                    list.append(data[0].trim())
                            .append(" - ")
                            .append(data[1].trim())
                            .append("\n");
                }
            }
        } catch (IOException e) {
            return "Error reading file: " + dataFile.getPath();
        }

        return list.toString();
    }

    // Individual contribution by Shameerah Dixon:
    // Cleaned up and polished the file-handling code so the final program
    // is organized, readable, and able to locate the CSV files reliably.
    private static File findFile(String fileName) {
        Path currentFolder = Paths.get("").toAbsolutePath();

        for (Path folder = currentFolder; folder != null; folder = folder.getParent()) {
            File file = folder.resolve(fileName).toFile();
            if (file.isFile()) {
                return file;
            }
        }

        try {
            Path programLocation = Paths.get(
                    BaltimoreRavensApp.class.getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI()
            );

            if (!programLocation.toFile().isDirectory()) {
                programLocation = programLocation.getParent();
            }

            for (Path folder = programLocation; folder != null; folder = folder.getParent()) {
                File file = folder.resolve(fileName).toFile();
                if (file.isFile()) {
                    return file;
                }
            }
        } catch (URISyntaxException | NullPointerException e) {
            // The current working directory search above is used if the class location cannot be read.
        }

        return null;
    }

    private static void showCsvData(String fileName) {
        JOptionPane.showMessageDialog(null, readCSVFile(fileName));
    }

    // Individual contribution by Parker Behagg:
    // Makes the program write activity information to the CSV log file.
    public static void printToLog(String source, String action) {
        File logFile = findFile(LOG_FILE);

        if (logFile == null) {
            Path projectFolder = Paths.get("").toAbsolutePath();
            logFile = projectFolder.resolve(LOG_FILE).toFile();
        }

        File parentFolder = logFile.getParentFile();
        if (parentFolder != null && !parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        try {
            if (!logFile.exists()) {
                try (FileWriter writer = new FileWriter(logFile)) {
                    writer.write("Date,Time,Source,Action\n");
                }
            }

            try (FileWriter writer = new FileWriter(logFile, true)) {
                writer.write(
                        now.format(dateFormatter) + ","
                                + now.format(timeFormatter) + ","
                                + source + ","
                                + action + "\n"
                );
            }
        } catch (IOException e) {
            System.out.println("Error writing to log file: " + e.getMessage());
        }
    }
}
