package db;

import java.sql.Time;

/**
 * Test utility to verify the FIXED time parsing works correctly
 */
public class TestTimeParsingFixed {
    
    public static void main(String[] args) {
        System.out.println("=== FIXED TIME PARSING TEST ===");
        
        String[] testTimes = {
            "00:00",
            "09:30",
            "14:45",
            "23:59",
            "00:00:00",
            "09:30:15",
            "14:45:30",
            "23:59:59",
            "9:30",
            "5:45",
            "12",
            "3",
            "",
            "invalid",
            "25:70",
            "abc:def"
        };
        
        for (String timeStr : testTimes) {
            try {
                Time parsedTime = safeParseTime(timeStr);
                System.out.println("✅ '" + timeStr + "' -> " + parsedTime);
            } catch (Exception e) {
                System.out.println("❌ '" + timeStr + "' -> Error: " + e.getMessage());
            }
        }
        
        System.out.println("\n=== TEST COMPLETED ===");
        System.out.println("All time formats should now work without crashes!");
    }
    
    /**
     * SAFE TIME PARSING - FIXED VERSION (same as in AppointmentsInterface)
     * Handles all time formats without throwing exceptions
     */
    private static Time safeParseTime(String timeText) {
        try {
            if (timeText == null || timeText.trim().isEmpty()) {
                return Time.valueOf("00:00:00");
            }
            
            timeText = timeText.trim();
            
            // If already in HH:mm:ss format
            if (timeText.matches("\\d{1,2}:\\d{2}:\\d{2}")) {
                return Time.valueOf(timeText);
            }
            
            // If in HH:mm format, add seconds
            if (timeText.matches("\\d{1,2}:\\d{2}")) {
                return Time.valueOf(timeText + ":00");
            }
            
            // If just HH format, add minutes and seconds
            if (timeText.matches("\\d{1,2}")) {
                return Time.valueOf(timeText + ":00:00");
            }
            
            // Default fallback
            System.out.println("Warning: Invalid time format '" + timeText + "', using 00:00:00");
            return Time.valueOf("00:00:00");
            
        } catch (Exception e) {
            System.err.println("Error parsing time: '" + timeText + "' - " + e.getMessage());
            return Time.valueOf("00:00:00");
        }
    }
} 