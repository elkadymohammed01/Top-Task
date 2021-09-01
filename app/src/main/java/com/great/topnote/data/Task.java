package com.great.topnote.data;

public class Task {
    String Name , Title , details, email ,email_resever;

    public Task(String name, String title, String details, String email, String email_resever) {
        Name = name;
        Title = title;
        this.details = details;
        this.email = email;
        this.email_resever = email_resever;
    }

    public Task() {
    }

    public String getEmail_resever() {
        return email_resever;
    }
    public void setEmail_resever(String email_resever) {
        this.email_resever = email_resever;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
