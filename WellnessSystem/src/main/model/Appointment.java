package main.model;

public class Appointment {
    private int id;
    private String studentName;
    private String counselorName;
    private String date;
    private String time;
    private String status;
    private String comments;

    public Appointment(int id, String studentName, String counselorName, String date, String time, String status, String comments) {
        this.id = id;
        this.studentName = studentName;
        this.counselorName = counselorName;
        this.date = date;
        this.time = time;
        this.status = status;
        this.comments = comments;
    }

    public int getId() { return id; }
    public String getStudentName() { return studentName; }
    public String getCounselorName() { return counselorName; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getComments() { return comments; }
} 