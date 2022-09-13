package com.opus.opus_version1.Part_Home;

import android.graphics.drawable.Drawable;

public class categoriesHelperClasses {
    Drawable Gradient;
    int image;
    String Title;

    public categoriesHelperClasses(Drawable gradient, int image, String title) {
        Gradient = gradient;
        this.image = image;
        Title = title;
    }

    public Drawable getGradient() {
        return Gradient;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
