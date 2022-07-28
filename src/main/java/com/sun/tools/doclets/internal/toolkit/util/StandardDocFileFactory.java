/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashSet;
/*     */ import javax.tools.DocumentationTool;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.StandardJavaFileManager;
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
/*     */ class StandardDocFileFactory
/*     */   extends DocFileFactory
/*     */ {
/*     */   private final StandardJavaFileManager fileManager;
/*     */   private File destDir;
/*     */   
/*     */   public StandardDocFileFactory(Configuration paramConfiguration) {
/*  69 */     super(paramConfiguration);
/*  70 */     this.fileManager = (StandardJavaFileManager)paramConfiguration.getFileManager();
/*     */   }
/*     */   
/*     */   private File getDestDir() {
/*  74 */     if (this.destDir == null) {
/*  75 */       if (!this.configuration.destDirName.isEmpty() || 
/*  76 */         !this.fileManager.hasLocation(DocumentationTool.Location.DOCUMENTATION_OUTPUT)) {
/*     */         try {
/*  78 */           String str = this.configuration.destDirName.isEmpty() ? "." : this.configuration.destDirName;
/*  79 */           File file = new File(str);
/*  80 */           this.fileManager.setLocation(DocumentationTool.Location.DOCUMENTATION_OUTPUT, Arrays.asList(new File[] { file }));
/*  81 */         } catch (IOException iOException) {
/*  82 */           throw new DocletAbortException(iOException);
/*     */         } 
/*     */       }
/*     */       
/*  86 */       this.destDir = this.fileManager.getLocation(DocumentationTool.Location.DOCUMENTATION_OUTPUT).iterator().next();
/*     */     } 
/*  88 */     return this.destDir;
/*     */   }
/*     */   
/*     */   public DocFile createFileForDirectory(String paramString) {
/*  92 */     return new StandardDocFile(new File(paramString));
/*     */   }
/*     */   
/*     */   public DocFile createFileForInput(String paramString) {
/*  96 */     return new StandardDocFile(new File(paramString));
/*     */   }
/*     */   
/*     */   public DocFile createFileForOutput(DocPath paramDocPath) {
/* 100 */     return new StandardDocFile(DocumentationTool.Location.DOCUMENTATION_OUTPUT, paramDocPath);
/*     */   }
/*     */ 
/*     */   
/*     */   Iterable<DocFile> list(JavaFileManager.Location paramLocation, DocPath paramDocPath) {
/* 105 */     if (paramLocation != StandardLocation.SOURCE_PATH) {
/* 106 */       throw new IllegalArgumentException();
/*     */     }
/* 108 */     LinkedHashSet<StandardDocFile> linkedHashSet = new LinkedHashSet();
/* 109 */     StandardLocation standardLocation = this.fileManager.hasLocation(StandardLocation.SOURCE_PATH) ? StandardLocation.SOURCE_PATH : StandardLocation.CLASS_PATH;
/*     */     
/* 111 */     for (File file : this.fileManager.getLocation(standardLocation)) {
/* 112 */       if (file.isDirectory()) {
/* 113 */         file = new File(file, paramDocPath.getPath());
/* 114 */         if (file.exists())
/* 115 */           linkedHashSet.add(new StandardDocFile(file)); 
/*     */       } 
/*     */     } 
/* 118 */     return (Iterable)linkedHashSet;
/*     */   }
/*     */   
/*     */   private static File newFile(File paramFile, String paramString) {
/* 122 */     return (paramFile == null) ? new File(paramString) : new File(paramFile, paramString);
/*     */   }
/*     */   
/*     */   class StandardDocFile
/*     */     extends DocFile
/*     */   {
/*     */     private File file;
/*     */     
/*     */     private StandardDocFile(File param1File) {
/* 131 */       super(StandardDocFileFactory.this.configuration);
/* 132 */       this.file = param1File;
/*     */     }
/*     */ 
/*     */     
/*     */     private StandardDocFile(JavaFileManager.Location param1Location, DocPath param1DocPath) {
/* 137 */       super(StandardDocFileFactory.this.configuration, param1Location, param1DocPath);
/* 138 */       Assert.check((param1Location == DocumentationTool.Location.DOCUMENTATION_OUTPUT));
/* 139 */       this.file = StandardDocFileFactory.newFile(StandardDocFileFactory.this.getDestDir(), param1DocPath.getPath());
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openInputStream() throws IOException {
/* 144 */       JavaFileObject javaFileObject = getJavaFileObjectForInput(this.file);
/* 145 */       return new BufferedInputStream(javaFileObject.openInputStream());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OutputStream openOutputStream() throws IOException, UnsupportedEncodingException {
/* 154 */       if (this.location != DocumentationTool.Location.DOCUMENTATION_OUTPUT) {
/* 155 */         throw new IllegalStateException();
/*     */       }
/* 157 */       OutputStream outputStream = getFileObjectForOutput(this.path).openOutputStream();
/* 158 */       return new BufferedOutputStream(outputStream);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Writer openWriter() throws IOException, UnsupportedEncodingException {
/* 168 */       if (this.location != DocumentationTool.Location.DOCUMENTATION_OUTPUT) {
/* 169 */         throw new IllegalStateException();
/*     */       }
/* 171 */       OutputStream outputStream = getFileObjectForOutput(this.path).openOutputStream();
/* 172 */       if (StandardDocFileFactory.this.configuration.docencoding == null) {
/* 173 */         return new BufferedWriter(new OutputStreamWriter(outputStream));
/*     */       }
/* 175 */       return new BufferedWriter(new OutputStreamWriter(outputStream, StandardDocFileFactory.this.configuration.docencoding));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canRead() {
/* 181 */       return this.file.canRead();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canWrite() {
/* 186 */       return this.file.canWrite();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean exists() {
/* 191 */       return this.file.exists();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 196 */       return this.file.getName();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getPath() {
/* 201 */       return this.file.getPath();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isAbsolute() {
/* 206 */       return this.file.isAbsolute();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDirectory() {
/* 211 */       return this.file.isDirectory();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isFile() {
/* 216 */       return this.file.isFile();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSameFile(DocFile param1DocFile) {
/* 221 */       if (!(param1DocFile instanceof StandardDocFile)) {
/* 222 */         return false;
/*     */       }
/*     */       try {
/* 225 */         return (this.file.exists() && this.file
/* 226 */           .getCanonicalFile().equals(((StandardDocFile)param1DocFile).file.getCanonicalFile()));
/* 227 */       } catch (IOException iOException) {
/* 228 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterable<DocFile> list() {
/* 234 */       ArrayList<StandardDocFile> arrayList = new ArrayList();
/* 235 */       for (File file : this.file.listFiles()) {
/* 236 */         arrayList.add(new StandardDocFile(file));
/*     */       }
/* 238 */       return (Iterable)arrayList;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean mkdirs() {
/* 243 */       return this.file.mkdirs();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public DocFile resolve(DocPath param1DocPath) {
/* 253 */       return resolve(param1DocPath.getPath());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public DocFile resolve(String param1String) {
/* 263 */       if (this.location == null && this.path == null) {
/* 264 */         return new StandardDocFile(new File(this.file, param1String));
/*     */       }
/* 266 */       return new StandardDocFile(this.location, this.path.resolve(param1String));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public DocFile resolveAgainst(JavaFileManager.Location param1Location) {
/* 276 */       if (param1Location != DocumentationTool.Location.DOCUMENTATION_OUTPUT)
/* 277 */         throw new IllegalArgumentException(); 
/* 278 */       return new StandardDocFile(StandardDocFileFactory.newFile(StandardDocFileFactory.this.getDestDir(), this.file.getPath()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 286 */       StringBuilder stringBuilder = new StringBuilder();
/* 287 */       stringBuilder.append("StandardDocFile[");
/* 288 */       if (this.location != null)
/* 289 */         stringBuilder.append("locn:").append(this.location).append(","); 
/* 290 */       if (this.path != null)
/* 291 */         stringBuilder.append("path:").append(this.path.getPath()).append(","); 
/* 292 */       stringBuilder.append("file:").append(this.file);
/* 293 */       stringBuilder.append("]");
/* 294 */       return stringBuilder.toString();
/*     */     }
/*     */     
/*     */     private JavaFileObject getJavaFileObjectForInput(File param1File) {
/* 298 */       return StandardDocFileFactory.this.fileManager.getJavaFileObjects(new File[] { param1File }).iterator().next();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private FileObject getFileObjectForOutput(DocPath param1DocPath) throws IOException {
/* 306 */       String str1 = param1DocPath.getPath();
/* 307 */       byte b = -1;
/* 308 */       for (byte b1 = 0; b1 < str1.length(); b1++) {
/* 309 */         char c = str1.charAt(b1);
/* 310 */         if (c == '/') {
/* 311 */           b = b1;
/* 312 */         } else if ((b1 == b + 1 && !Character.isJavaIdentifierStart(c)) || 
/* 313 */           !Character.isJavaIdentifierPart(c)) {
/*     */           break;
/*     */         } 
/*     */       } 
/* 317 */       String str2 = (b == -1) ? "" : str1.substring(0, b);
/* 318 */       String str3 = str1.substring(b + 1);
/* 319 */       return StandardDocFileFactory.this.fileManager.getFileForOutput(this.location, str2, str3, null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\StandardDocFileFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */