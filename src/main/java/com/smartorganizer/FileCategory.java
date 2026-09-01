package com.smartorganizer;
import java.util.Locale;
public enum FileCategory {
 IMAGES("Images"), DOCUMENTS("Documents"), VIDEOS("Videos"), MUSIC("Music"), ARCHIVES("Archives"), OTHERS("Others");
 private final String folder; FileCategory(String folder){this.folder=folder;} public String folder(){return folder;}
 public static FileCategory from(String name){
  String n=name.toLowerCase(Locale.ROOT); int d=n.lastIndexOf('.'); if(d<0)return OTHERS;
  return switch(n.substring(d+1)){
   case "jpg","jpeg","png","gif","bmp","webp","svg"->IMAGES;
   case "pdf","doc","docx","txt","xls","xlsx","ppt","pptx","csv","odp"->DOCUMENTS;
   case "mp4","avi","mkv","mov","wmv","flv","webm"->VIDEOS;
   case "mp3","wav","flac","aac","m4a","ogg"->MUSIC;
   case "zip","rar","7z","tar","gz"->ARCHIVES; default->OTHERS;};
 }
}