package org.example.models;

public class Entry {
    String type;
    String heading;
    String description;
    String url;
    String imageUrl;
    String priceInCents;
    String postDate;
    String id;

    public Entry(String type, String heading, String description, String url, String imageUrl, String priceInCents) {
        this.type = type;
        this.heading = heading;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
        this.priceInCents = priceInCents;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getHeading() {
        return heading;
    }

    public String getPriceInCents() {
        return priceInCents;
    }

    public String getUrl() {
        return url;
    }
}
