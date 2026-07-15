package com.example;

import java.time.LocalDate;

public class Intern {
    private int internId;
    private String internName;
    private String internEmail;
    private LocalDate startDate;
    private int trackId;
    private Integer mentorId;

    public Intern(int internId, String internName, String internEmail, LocalDate startDate, int trackId, Integer mentorId) {
        this.internId = internId;
        this.internName = internName;
        this.internEmail = internEmail;
        this.startDate = startDate;
        this.trackId = trackId;
        this.mentorId = mentorId;
    }
    public Intern(int internId, String internName) {
        this.internId = internId;
        this.internName = internName;
    }


    public int getInternId() {
        return internId;
    }
    public String getInternName(){
        return internName;
    }
    public String getInternEmail(){
        return internEmail;
    }
    public LocalDate getStartDate(){
        return startDate;
    }
    public int getTrackId(){
        return trackId;
    }
    public Integer getMentorId(){
        return mentorId;
    }
}
