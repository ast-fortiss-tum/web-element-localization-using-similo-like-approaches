package de.tum.ls4.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DockerUtils {
    private static boolean databaseSetup = false;

    public static void setupDatabase() {
        if (!databaseSetup) {

            // start mariadb
            executeCommand("docker", "run", "--rm", "--name", "mariadb", "-e", "MARIADB_ROOT_PASSWORD=my-secret-pw",
                    "-p", "3306:3306", "-d", "mariadb:latest");
            databaseSetup = true;

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // create similo database
            cleanDatabase();

            // hook shutdown database teardown
            Runtime.getRuntime().addShutdownHook(new Thread(DockerUtils::teardownDatabase));
        }
    }

    private static void teardownDatabase() {
        executeCommand("docker", "stop", "mariadb");
    }

    private static void executeCommand(String... command) {
        Process process = null;
        BufferedReader reader = null;
        BufferedReader errorReader = null;

        try {
            process = Runtime.getRuntime().exec(command);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
                if (errorReader != null) errorReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void cleanDatabase() {
        // drop database if exists
        executeCommand("docker", "exec", "-i", "mariadb", "mariadb", "-uroot", "-pmy-secret-pw",
                "-e", "DROP DATABASE IF EXISTS similo;");
        executeCommand("docker", "exec", "-i", "mariadb", "mariadb", "-uroot", "-pmy-secret-pw",
                "-e", "DROP DATABASE IF EXISTS von_similo;");

        // create similo database
        executeCommand("docker", "exec", "-i", "mariadb", "mariadb", "-uroot", "-pmy-secret-pw",
                "-e", "CREATE DATABASE similo;");
        executeCommand("docker", "exec", "-i", "mariadb", "mariadb", "-uroot", "-pmy-secret-pw",
                "-e", "CREATE DATABASE von_similo;");
    }
}
