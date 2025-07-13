package wellnesssystem.model;

/**
 * Simple Feedback class for in-memory storage
 * No database dependencies
 */
public class Feedback {
    private int id;
    private String studentName;
    private CounselorStaff counselor;
    private String date;
    private int rating;
    private String feedback;
    private Staff createdBy;

    public Feedback(int id, String studentName, CounselorStaff counselor, 
                   String date, int rating, String feedback, Staff createdBy) {
        this.id = id;
        this.studentName = studentName;
        this.counselor = counselor;
        this.date = date;
        this.rating = rating;
        this.feedback = feedback;
        this.createdBy = createdBy;
    }

    // Getters
    public int getId() { return id; }
    public String getStudentName() { return studentName; }
    public CounselorStaff getCounselor() { return counselor; }
    public String getDate() { return date; }
    public int getRating() { return rating; }
    public String getFeedback() { return feedback; }
    public Staff getCreatedBy() { return createdBy; }

    // Getters for display
    public String getCounselorName() { 
        return counselor != null ? counselor.getName() : "Unknown"; 
    }
    public String getCreatedByName() { 
        return createdBy != null ? createdBy.getName() : "Unknown"; 
    }

    // Setters
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setCounselor(CounselorStaff counselor) { this.counselor = counselor; }
    public void setDate(String date) { this.date = date; }
    public void setRating(int rating) { this.rating = rating; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", studentName='" + studentName + '\'' +
                ", counselor=" + getCounselorName() +
                ", date='" + date + '\'' +
                ", rating=" + rating +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}
