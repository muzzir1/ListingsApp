package com.example.muzzi.fullcontactapp;


public class Movie
{
    private  String title, year, rating, plot;
    private String posterimagelink;

    public Movie() {
    }

    public Movie(String Title, String PosterLink, String Year, String Rating, String Plot)
    {
        this.title = Title;
        this.posterimagelink = PosterLink;
        this.year = Year;
        this.rating = Rating;
        this.plot = Plot;
    }

    public String getPosterimage() {
        return posterimagelink;
    }

    public void setPosterimage(String posterimage) {
        this.posterimagelink = posterimage;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getYear() {
        return year;
    }

    public String getRating() {
        return rating;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }
}
