package com.great.topnote.data;

public class Group {
    String Id , Name ;

    public Group(String id, String name) {
        Id = id;
        Name = name;
    }

    public Group() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
