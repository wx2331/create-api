/*    */ package com.sun.tools.internal.xjc.reader;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ import org.xml.sax.InputSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Util
/*    */ {
/*    */   public static Object getFileOrURL(String fileOrURL) throws IOException {
/*    */     try {
/* 45 */       return new URL(fileOrURL);
/* 46 */     } catch (MalformedURLException e) {
/* 47 */       return (new File(fileOrURL)).getCanonicalFile();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static InputSource getInputSource(String fileOrURL) {
/*    */     try {
/* 56 */       Object o = getFileOrURL(fileOrURL);
/* 57 */       if (o instanceof URL) {
/* 58 */         return new InputSource(escapeSpace(((URL)o).toExternalForm()));
/*    */       }
/* 60 */       String url = ((File)o).toURL().toExternalForm();
/* 61 */       return new InputSource(escapeSpace(url));
/*    */     }
/* 63 */     catch (IOException e) {
/* 64 */       return new InputSource(fileOrURL);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static String escapeSpace(String url) {
/* 70 */     StringBuffer buf = new StringBuffer();
/* 71 */     for (int i = 0; i < url.length(); i++) {
/*    */       
/* 73 */       if (url.charAt(i) == ' ') {
/* 74 */         buf.append("%20");
/*    */       } else {
/* 76 */         buf.append(url.charAt(i));
/*    */       } 
/* 78 */     }  return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */