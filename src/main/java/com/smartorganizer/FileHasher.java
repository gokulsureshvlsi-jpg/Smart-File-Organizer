package com.smartorganizer;
import java.io.*; import java.nio.file.*; import java.security.*;
public final class FileHasher {
 private FileHasher(){}
 public static String sha256(Path p)throws IOException{
  try{MessageDigest md=MessageDigest.getInstance("SHA-256"); try(InputStream in=new BufferedInputStream(Files.newInputStream(p))){
   byte[] b=new byte[8192]; int n; while((n=in.read(b))!=-1)md.update(b,0,n);}
   StringBuilder s=new StringBuilder(); for(byte b:md.digest())s.append(String.format("%02x",b)); return s.toString();
  }catch(NoSuchAlgorithmException e){throw new IllegalStateException(e);}
 }
}