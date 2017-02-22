package com.example.bean;

import java.util.List;

/**
 * Created by wumingjun1 on 2017/2/20.
 */

public class ThemeItemBean {
    private List<Stories> stories;
    private String description;
    private String background;
    private String color;
    private String name;
    private String image;
    private List<Editor> editors;
    private String image_source;

    @Override
    public String toString() {
        return "ThemeItemBean{" +
                "background='" + background + '\'' +
                ", stories=" + stories +
                ", description='" + description + '\'' +
                ", color='" + color + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", editors=" + editors +
                ", image_source='" + image_source + '\'' +
                '}';
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Editor> getEditors() {
        return editors;
    }

    public void setEditors(List<Editor> editors) {
        this.editors = editors;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Stories> getStories() {
        return stories;
    }

    public void setStories(List<Stories> stories) {
        this.stories = stories;
    }

    static class Editor{
        private String url;
        private String bio;
        private String id;
        private String avater;
        private String name;

        @Override
        public String toString() {
            return "Editor{" +
                    "avater='" + avater + '\'' +
                    ", url='" + url + '\'' +
                    ", bio='" + bio + '\'' +
                    ", id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }

        public String getAvater() {
            return avater;
        }

        public void setAvater(String avater) {
            this.avater = avater;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
