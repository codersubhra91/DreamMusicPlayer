package com.subhrajit.sutapamusicplayer;


import java.io.Serializable;

public class SongModel implements Serializable {
    public String path;
    public String title;
    public String artist;
    public String size;
    public String genre;
    public String album;
    public String duration;

    public SongModel(String path, String title, String artist, String size, String genre, String album, String duration) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.size = size;
        this.genre = genre;
        this.album = album;
        this.duration = duration;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
     public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
