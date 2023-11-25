package com.apcpdcl.departmentapp.models;

public class DashBoard {

   private String title;

   private String imageUrl;

   public DashBoard(String title, String imageUrl) {
      this.imageUrl = imageUrl;
      this.title = title;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getImageUrl() {
      return imageUrl;
   }

   public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
   }
}
