package wellnesssystem.model;

/**
 * Simple Appointment class for in-memory storage
 * No database dependencies
 */
public class Appointment {
    private int id;
    private String studentName;
    private CounselorStaff counselor;
    private String date;
    private String time;
    private String status;
    private Staff createdBy;
    private String comments;

    public Appointment(int id, String studentName, CounselorStaff counselor, 
                      String date, String time, String status, Staff createdBy) {
        this.id = id;
        this.studentName = studentName;
        this.counselor = counselor;
        this.date = date;
        this.time = time;
        this.status = status;
        this.createdBy = createdBy;
        this.comments = "";
    }

    public Appointment(int id, String studentName, CounselorStaff counselor, 
                      String date, String time, String status, Staff createdBy, String comments) {
        this.id = id;
        this.studentName = studentName;
        this.counselor = counselor;
        this.date = date;
        this.time = time;
        this.status = status;
        this.createdBy = createdBy;
        this.comments = comments != null ? comments : "";
    }

    // Getters
    public int getId() { return id; }
    public String getStudentName() { return studentName; }
    public CounselorStaff getCounselor() { return counselor; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getStatus() { return status; }
    public Staff getCreatedBy() { return createdBy; }
    public String getComments() { return comments; }

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
    public void setTime(String time) { this.time = time; }
    public void setStatus(String status) { this.status = status; }
    public void setComments(String comments) { this.comments = comments != null ? comments : ""; }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", studentName='" + studentName + '\'' +
                ", counselor=" + getCounselorName() +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}