package main.model;

public class Feedback {
    private int id;
    private String studentName;
    private String counselorName;
    private String date;
    private int rating;
    private String comments;

    public Feedback(int id, String studentName, String counselorName, String date, int rating, String comments) {
        this.id = id;
        this.studentName = studentName;
        this.counselorName = counselorName;
        this.date = date;
        this.rating = rating;
        this.comments = comments;
    }

    public int getId() { return id; }
    public String getStudentName() { return studentName; }
    public String getCounselorName() { return counselorName; }
    public String getDate() { return date; }
    public int getRating() { return rating; }
    public String getComments() { return comments; }
} 