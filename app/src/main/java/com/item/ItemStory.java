package com.item;

public class ItemStory {

    private int id;
    private String catId;
    private String storyTitle;
    private String storyDescription;
    private String storyDate;
    private String storyViews;
    private String storyImage;

    public ItemStory() {
        // TODO Auto-generated constructor stub
    }

    public int getId() {
        return id; }
    public void setId(int id) {
        this.id = id; }

    public String getStoryTitle() {
        return storyTitle; }
    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle; }

    public String getStoryDescription() {
        return storyDescription; }
    public void setStoryDescription(String storyDescription) {
        this.storyDescription = storyDescription; }

    public String getStoryDate() {
        return storyDate; }
    public void setStoryDate(String storyDate) {
        this.storyDate = storyDate; }

    public String getStoryViews() {
        return storyViews; }
    public void setStoryViews(String storyViews) {
        this.storyViews = storyViews; }

    public String getstoryImage() {
        return storyImage; }
    public void setstoryImage(String storyImage) {
        this.storyImage = storyImage; }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }
}
