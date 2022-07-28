/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashSet;
/*     */ import javax.tools.DocumentationTool;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.StandardLocation;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ class SimpleDocFileFactory
/*     */   extends DocFileFactory
/*     */ {
/*     */   public SimpleDocFileFactory(Configuration paramConfiguration) {
/*  66 */     super(paramConfiguration);
/*     */   }
/*     */   
/*     */   public DocFile createFileForDirectory(String paramString) {
/*  70 */     return new SimpleDocFile(new File(paramString));
/*     */   }
/*     */   
/*     */   public DocFile createFileForInput(String paramString) {
/*  74 */     return new SimpleDocFile(new File(paramString));
/*     */   }
/*     */   
/*     */   public DocFile createFileForOutput(DocPath paramDocPath) {
/*  78 */     return new SimpleDocFile(DocumentationTool.Location.DOCUMENTATION_OUTPUT, paramDocPath);
/*     */   }
/*     */ 
/*     */   
/*     */   Iterable<DocFile> list(JavaFileManager.Location paramLocation, DocPath paramDocPath) {
/*  83 */     if (paramLocation != StandardLocation.SOURCE_PATH) {
/*  84 */       throw new IllegalArgumentException();
/*     */     }
/*  86 */     LinkedHashSet<SimpleDocFile> linkedHashSet = new LinkedHashSet();
/*  87 */     for (String str : this.configuration.sourcepath.split(File.pathSeparator)) {
/*  88 */       if (!str.isEmpty()) {
/*     */         
/*  90 */         File file = new File(str);
/*  91 */         if (file.isDirectory()) {
/*  92 */           file = new File(file, paramDocPath.getPath());
/*  93 */           if (file.exists())
/*  94 */             linkedHashSet.add(new SimpleDocFile(file)); 
/*     */         } 
/*     */       } 
/*  97 */     }  return (Iterable)linkedHashSet;
/*     */   }
/*     */   
/*     */   class SimpleDocFile
/*     */     extends DocFile {
/*     */     private File file;
/*     */     
/*     */     private SimpleDocFile(File param1File) {
/* 105 */       super(SimpleDocFileFactory.this.configuration);
/* 106 */       this.file = param1File;
/*     */     }
/*     */ 
/*     */     
/*     */     private SimpleDocFile(JavaFileManager.Location param1Location, DocPath param1DocPath) {
/* 111 */       super(SimpleDocFileFactory.this.configuration, param1Location, param1DocPath);
/* 112 */       String str = SimpleDocFileFactory.this.configuration.destDirName;
/* 113 */       this
/* 114 */         .file = str.isEmpty() ? new File(param1DocPath.getPath()) : new File(str, param1DocPath.getPath());
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openInputStream() throws FileNotFoundException {
/* 119 */       return new BufferedInputStream(new FileInputStream(this.file));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OutputStream openOutputStream() throws IOException, UnsupportedEncodingException {
/* 128 */       if (this.location != DocumentationTool.Location.DOCUMENTATION_OUTPUT) {
/* 129 */         throw new IllegalStateException();
/*     */       }
/* 131 */       createDirectoryForFile(this.file);
/* 132 */       return new BufferedOutputStream(new FileOutputStream(this.file));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Writer openWriter() throws IOException, UnsupportedEncodingException {
/* 142 */       if (this.location != DocumentationTool.Location.DOCUMENTATION_OUTPUT) {
/* 143 */         throw new IllegalStateException();
/*     */       }
/* 145 */       createDirectoryForFile(this.file);
/* 146 */       FileOutputStream fileOutputStream = new FileOutputStream(this.file);
/* 147 */       if (SimpleDocFileFactory.this.configuration.docencoding == null) {
/* 148 */         return new BufferedWriter(new OutputStreamWriter(fileOutputStream));
/*     */       }
/* 150 */       return new BufferedWriter(new OutputStreamWriter(fileOutputStream, SimpleDocFileFactory.this.configuration.docencoding));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canRead() {
/* 156 */       return this.file.canRead();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canWrite() {
/* 161 */       return this.file.canRead();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean exists() {
/* 166 */       return this.file.exists();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 171 */       return this.file.getName();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getPath() {
/* 176 */       return this.file.getPath();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isAbsolute() {
/* 181 */       return this.file.isAbsolute();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDirectory() {
/* 186 */       return this.file.isDirectory();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isFile() {
/* 191 */       return this.file.isFile();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSameFile(DocFile param1DocFile) {
/* 196 */       if (!(param1DocFile instanceof SimpleDocFile)) {
/* 197 */         return false;
/*     */       }
/*     */       try {
/* 200 */         return (this.file.exists() && this.file
/* 201 */           .getCanonicalFile().equals(((SimpleDocFile)param1DocFile).file.getCanonicalFile()));
/* 202 */       } catch (IOException iOException) {
/* 203 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterable<DocFile> list() {
/* 209 */       ArrayList<SimpleDocFile> arrayList = new ArrayList();
/* 210 */       for (File file : this.file.listFiles()) {
/* 211 */         arrayList.add(new SimpleDocFile(file));
/*     */       }
/* 213 */       return (Iterable)arrayList;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean mkdirs() {
/* 218 */       return this.file.mkdirs();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public DocFile resolve(DocPath param1DocPath) {
/* 228 */       return resolve(param1DocPath.getPath());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public DocFile resolve(String param1String) {
/* 238 */       if (this.location == null && this.path == null) {
/* 239 */         return new SimpleDocFile(new File(this.file, param1String));
/*     */       }
/* 241 */       return new SimpleDocFile(this.location, this.path.resolve(param1String));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public DocFile resolveAgainst(JavaFileManager.Location param1Location) {
/* 251 */       if (param1Location != DocumentationTool.Location.DOCUMENTATION_OUTPUT)
/* 252 */         throw new IllegalArgumentException(); 
/* 253 */       return new SimpleDocFile(new File(SimpleDocFileFactory.this.configuration.destDirName, this.file
/* 254 */             .getPath()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void createDirectoryForFile(File param1File) {
/* 266 */       File file = param1File.getParentFile();
/* 267 */       if (file == null || file.exists() || file.mkdirs()) {
/*     */         return;
/*     */       }
/* 270 */       SimpleDocFileFactory.this.configuration.message.error("doclet.Unable_to_create_directory_0", new Object[] { file
/* 271 */             .getPath() });
/* 272 */       throw new DocletAbortException("can't create directory");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 280 */       StringBuilder stringBuilder = new StringBuilder();
/* 281 */       stringBuilder.append("DocFile[");
/* 282 */       if (this.location != null)
/* 283 */         stringBuilder.append("locn:").append(this.location).append(","); 
/* 284 */       if (this.path != null)
/* 285 */         stringBuilder.append("path:").append(this.path.getPath()).append(","); 
/* 286 */       stringBuilder.append("file:").append(this.file);
/* 287 */       stringBuilder.append("]");
/* 288 */       return stringBuilder.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\SimpleDocFileFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */