package com.great.topnote.data;

public class Note {
    private String Name,Id
            ,groupId,Title,Details;
    private Long Sound,Image,File;

    public Note(String name, String id, String groupId, String title, String details, Long sound, Long image, Long file) {
        Name = name;
        Id = id;
        this.groupId = groupId;
        Title = title;
        Details = details;
        Sound = sound;
        Image = image;
        File = file;
    }

    public Note() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public Long getSound() {
        return Sound;
    }

    public void setSound(Long sound) {
        Sound = sound;
    }

    public Long getImage() {
        return Image;
    }

    public void setImage(Long image) {
        Image = image;
    }

    public Long getFile() {
        return File;
    }

    public void setFile(Long file) {
        File = file;
    }
}
