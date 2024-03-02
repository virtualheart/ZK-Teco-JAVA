package com.gac.employee.attendance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
@EnableScheduling
public class AttendanceApplication {

    private static final Logger log = LoggerFactory.getLogger(AttendanceApplication.class);
    public static void main(String[] args) throws IOException {
//        startDatabaseServer();
        SpringApplication.run(AttendanceApplication.class, args);
    }
    private static void startDatabaseServer() {
        String osName = System.getProperty("os.name").toLowerCase();

        try {
            if (osName.contains("linux")) {
                if (!isMariaDBRunningOnLinux()) {
                    startMySQLOnLinux();
                } else {
                    log.info("MariaDB is already running on Linux.");
                }
            } else if (osName.contains("win")) {
                if (!isMariaDBRunningOnWindows()) {
                    startMariaDBOnWindows();
                } else {
                    log.info("MariaDB is already running on Windows.");
                }
            } else {
                log.error("Unsupported operating system: " + osName);
            }
        } catch (IOException e) {
            log.error("Error starting database server: " + e.getMessage());
        }
    }

    private static void startMySQLOnLinux() throws IOException {
        // Execute commands to start MySQL on Linux
        Process process = Runtime.getRuntime().exec("service mysql start");
        int exitCode;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            throw new IOException("Error waiting for process completion", e);
        }

        if (exitCode == 0) {
            log.info("MySQL started successfully on Linux");
        } else {
            log.error("Failed to start MySQL on Linux. Exit code: " + exitCode);
        }
    }

    private static void startMariaDBOnWindows() throws IOException {
        if (!isMariaDBRunningOnWindows()) {
            String mariadbCommand = "mariadb\\bin\\mariadb.exe";
            Process process = Runtime.getRuntime().exec(mariadbCommand);
        } else {
            log.info("MariaDB is already running on Windows.");
        }
    }

    private static boolean isMariaDBRunningOnLinux() {
        try {
            // Execute a command to check if MariaDB process is running on Linux
            Process process = Runtime.getRuntime().exec("pgrep mariadb");

            // Check the exit code of the process
            int exitCode = process.waitFor();
            return exitCode == 0; // 0 means process is running

        } catch (IOException | InterruptedException e) {
            log.error("Error checking if MariaDB is running on Linux: " + e.getMessage());
        }

        return false; // Unable to determine if MariaDB is running
    }

    private static boolean isMariaDBRunningOnWindows() {
        try {
            // Execute a command to check if MariaDB process is running on Windows
            Process process = Runtime.getRuntime().exec("tasklist /FI \"IMAGENAME eq mysqld.exe\"");

            // Read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("mysqld.exe")) {
                    return true; // MariaDB process is running
                }
            }

            reader.close();
            process.waitFor();

        } catch (IOException | InterruptedException e) {
            log.error("Error checking if MariaDB is running on Windows: " + e.getMessage());
        }

        return false;
    }

}
