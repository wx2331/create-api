/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import javax.tools.JavaFileManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DocFile
/*     */ {
/*     */   private final Configuration configuration;
/*     */   protected final JavaFileManager.Location location;
/*     */   protected final DocPath path;
/*     */   
/*     */   public static DocFile createFileForDirectory(Configuration paramConfiguration, String paramString) {
/*  59 */     return DocFileFactory.getFactory(paramConfiguration).createFileForDirectory(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public static DocFile createFileForInput(Configuration paramConfiguration, String paramString) {
/*  64 */     return DocFileFactory.getFactory(paramConfiguration).createFileForInput(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public static DocFile createFileForOutput(Configuration paramConfiguration, DocPath paramDocPath) {
/*  69 */     return DocFileFactory.getFactory(paramConfiguration).createFileForOutput(paramDocPath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Iterable<DocFile> list(Configuration paramConfiguration, JavaFileManager.Location paramLocation, DocPath paramDocPath) {
/*  95 */     return DocFileFactory.getFactory(paramConfiguration).list(paramLocation, paramDocPath);
/*     */   }
/*     */ 
/*     */   
/*     */   protected DocFile(Configuration paramConfiguration) {
/* 100 */     this.configuration = paramConfiguration;
/* 101 */     this.location = null;
/* 102 */     this.path = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected DocFile(Configuration paramConfiguration, JavaFileManager.Location paramLocation, DocPath paramDocPath) {
/* 107 */     this.configuration = paramConfiguration;
/* 108 */     this.location = paramLocation;
/* 109 */     this.path = paramDocPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract InputStream openInputStream() throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract OutputStream openOutputStream() throws IOException, UnsupportedEncodingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Writer openWriter() throws IOException, UnsupportedEncodingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyFile(DocFile paramDocFile) throws IOException {
/* 135 */     InputStream inputStream = paramDocFile.openInputStream();
/* 136 */     OutputStream outputStream = openOutputStream();
/*     */     
/* 138 */     try { byte[] arrayOfByte = new byte[1024];
/*     */       int i;
/* 140 */       while ((i = inputStream.read(arrayOfByte)) != -1) {
/* 141 */         outputStream.write(arrayOfByte, 0, i);
/*     */       } }
/* 143 */     catch (FileNotFoundException fileNotFoundException) {  }
/* 144 */     catch (SecurityException securityException) {  }
/*     */     finally
/* 146 */     { inputStream.close();
/* 147 */       outputStream.close(); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyResource(DocPath paramDocPath, boolean paramBoolean1, boolean paramBoolean2) {
/* 160 */     if (exists() && !paramBoolean1) {
/*     */       return;
/*     */     }
/*     */     try {
/* 164 */       InputStream inputStream = Configuration.class.getResourceAsStream(paramDocPath.getPath());
/* 165 */       if (inputStream == null) {
/*     */         return;
/*     */       }
/* 168 */       OutputStream outputStream = openOutputStream();
/*     */       try {
/* 170 */         if (!paramBoolean2) {
/* 171 */           byte[] arrayOfByte = new byte[2048];
/*     */           int i;
/* 173 */           for (; (i = inputStream.read(arrayOfByte)) > 0; outputStream.write(arrayOfByte, 0, i));
/*     */         } else {
/* 175 */           BufferedWriter bufferedWriter; BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
/*     */           
/* 177 */           if (this.configuration.docencoding == null) {
/* 178 */             bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
/*     */           } else {
/* 180 */             bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, this.configuration.docencoding));
/*     */           } 
/*     */           
/*     */           String str;
/*     */           
/* 185 */           while ((str = bufferedReader.readLine()) != null) {
/* 186 */             bufferedWriter.write(str);
/* 187 */             bufferedWriter.write(DocletConstants.NL);
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */       finally {
/*     */         
/* 195 */         inputStream.close();
/* 196 */         outputStream.close();
/*     */       } 
/* 198 */     } catch (IOException iOException) {
/* 199 */       iOException.printStackTrace(System.err);
/* 200 */       throw new DocletAbortException(iOException);
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract boolean canRead();
/*     */   
/*     */   public abstract boolean canWrite();
/*     */   
/*     */   public abstract boolean exists();
/*     */   
/*     */   public abstract String getName();
/*     */   
/*     */   public abstract String getPath();
/*     */   
/*     */   public abstract boolean isAbsolute();
/*     */   
/*     */   public abstract boolean isDirectory();
/*     */   
/*     */   public abstract boolean isFile();
/*     */   
/*     */   public abstract boolean isSameFile(DocFile paramDocFile);
/*     */   
/*     */   public abstract Iterable<DocFile> list() throws IOException;
/*     */   
/*     */   public abstract boolean mkdirs();
/*     */   
/*     */   public abstract DocFile resolve(DocPath paramDocPath);
/*     */   
/*     */   public abstract DocFile resolve(String paramString);
/*     */   
/*     */   public abstract DocFile resolveAgainst(JavaFileManager.Location paramLocation);
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\DocFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */