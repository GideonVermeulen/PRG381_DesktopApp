package db;

import java.sql.Time;

/**
 * Test utility to verify time parsing works correctly
 */
public class TestTimeParsing {
    
    public static void main(String[] args) {
        System.out.println("=== TIME PARSING TEST ===");
        
        String[] testTimes = {
            "00:00",
            "09:30",
            "14:45",
            "23:59",
            "00:00:00",
            "09:30:15",
            "14:45:30",
            "23:59:59",
            "",
            "invalid"
        };
        
        for (String timeStr : testTimes) {
            try {
                Time parsedTime = parseTimeString(timeStr);
                System.out.println("✅ '" + timeStr + "' -> " + parsedTime);
            } catch (Exception e) {
                System.out.println("❌ '" + timeStr + "' -> Error: " + e.getMessage());
            }
        }
        
        System.out.println("\n=== TEST COMPLETED ===");
    }
    
    /**
     * Parse time string to proper Time format (same as in AppointmentsInterface)
     */
    private static Time parseTimeString(String timeText) {
        try {
            timeText = timeText.trim();
            if (timeText.isEmpty()) {
                return Time.valueOf("00:00:00");
            }
            
            // If already in HH:mm:ss format
            if (timeText.matches("\\d{2}:\\d{2}:\\d{2}")) {
                return Time.valueOf(timeText);
            }
            
            // If in HH:mm format, add seconds
            if (timeText.matches("\\d{2}:\\d{2}")) {
                return Time.valueOf(timeText + ":00");
            }
            
            // Default fallback
            return Time.valueOf("00:00:00");
        } catch (Exception e) {
            System.err.println("Error parsing time: " + timeText + " - " + e.getMessage());
            return Time.valueOf("00:00:00");
        }
    }
} 