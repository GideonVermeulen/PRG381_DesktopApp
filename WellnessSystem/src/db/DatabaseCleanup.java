package db;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database cleanup utility to fix Derby restart issues
 * Run this if you encounter database connection problems
 */
public class DatabaseCleanup {
    
    public static void main(String[] args) {
        System.out.println("=== WELLNESS SYSTEM DATABASE CLEANUP ===");
        System.out.println();
        
        try {
            // Step 1: Force Derby shutdown
            System.out.println("Step 1: Forcing Derby shutdown...");
            forceDerbyShutdown();
            
            // Step 2: Wait a moment
            System.out.println("Step 2: Waiting for cleanup...");
            Thread.sleep(2000);
            
            // Step 3: Test connection
            System.out.println("Step 3: Testing new connection...");
            if (testNewConnection()) {
                System.out.println("✅ Database cleanup successful!");
                System.out.println("You can now run the Wellness System application.");
            } else {
                System.out.println("❌ Database cleanup failed!");
                System.out.println("Try deleting the wellnessDB folder and restarting.");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error during cleanup: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Force Derby shutdown
     */
    private static void forceDerbyShutdown() {
        try {
            // Try to connect with shutdown=true to force Derby shutdown
            String shutdownUrl = "jdbc:derby:wellnessDB;shutdown=true";
            DriverManager.getConnection(shutdownUrl);
        } catch (SQLException e) {
            // Expected exception when Derby shuts down
            if (e.getMessage().contains("Derby system shutdown")) {
                System.out.println("✅ Derby shutdown successful");
            } else {
                System.out.println("⚠️ Derby shutdown attempt: " + e.getMessage());
            }
        }
    }
    
    /**
     * Test new connection
     */
    private static boolean testNewConnection() {
        try {
            // Try to establish a new connection
            String url = "jdbc:derby:wellnessDB;create=true;user=admin;password=admin";
            var conn = DriverManager.getConnection(url);
            if (conn != null && !conn.isClosed()) {
                conn.close();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Check if wellnessDB folder exists
     */
    private static boolean checkDatabaseFolder() {
        File dbFolder = new File("wellnessDB");
        return dbFolder.exists() && dbFolder.isDirectory();
    }
    
    /**
     * Delete database folder (use with caution!)
     */
    public static void deleteDatabaseFolder() {
        File dbFolder = new File("wellnessDB");
        if (dbFolder.exists()) {
            deleteFolder(dbFolder);
            System.out.println("✅ Database folder deleted");
        } else {
            System.out.println("ℹ️ Database folder not found");
        }
    }
    
    /**
     * Recursively delete a folder
     */
    private static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolder(file);
                } else {
                    file.delete();
                }
            }
        }
        folder.delete();
    }
} 