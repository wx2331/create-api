/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.javac.nio.PathFileManager;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashSet;
/*     */ import javax.tools.DocumentationTool;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
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
/*     */ class PathDocFileFactory
/*     */   extends DocFileFactory
/*     */ {
/*     */   private final PathFileManager fileManager;
/*     */   private final Path destDir;
/*     */
/*     */   public PathDocFileFactory(Configuration paramConfiguration) {
/*  71 */     super(paramConfiguration);
/*  72 */     this.fileManager = (PathFileManager)paramConfiguration.getFileManager();
/*     */
/*  74 */     if (!paramConfiguration.destDirName.isEmpty() ||
/*  75 */       !this.fileManager.hasLocation(DocumentationTool.Location.DOCUMENTATION_OUTPUT)) {
/*     */       try {
/*  77 */         String str = paramConfiguration.destDirName.isEmpty() ? "." : paramConfiguration.destDirName;
/*  78 */         Path path = this.fileManager.getDefaultFileSystem().getPath(str, new String[0]);
/*  79 */         this.fileManager.setLocation(DocumentationTool.Location.DOCUMENTATION_OUTPUT, Arrays.asList(new Path[] { path }));
/*  80 */       } catch (IOException iOException) {
/*  81 */         throw new DocletAbortException(iOException);
/*     */       }
/*     */     }
/*     */
/*  85 */     this.destDir = this.fileManager.getLocation(DocumentationTool.Location.DOCUMENTATION_OUTPUT).iterator().next();
/*     */   }
/*     */
/*     */   public DocFile createFileForDirectory(String paramString) {
/*  89 */     return new StandardDocFile(this.fileManager.getDefaultFileSystem().getPath(paramString, new String[0]));
/*     */   }
/*     */
/*     */   public DocFile createFileForInput(String paramString) {
/*  93 */     return new StandardDocFile(this.fileManager.getDefaultFileSystem().getPath(paramString, new String[0]));
/*     */   }
/*     */
/*     */   public DocFile createFileForOutput(DocPath paramDocPath) {
/*  97 */     return new StandardDocFile(DocumentationTool.Location.DOCUMENTATION_OUTPUT, paramDocPath);
/*     */   }
/*     */
/*     */
/*     */   Iterable<DocFile> list(JavaFileManager.Location paramLocation, DocPath paramDocPath) {
/* 102 */     if (paramLocation != StandardLocation.SOURCE_PATH) {
/* 103 */       throw new IllegalArgumentException();
/*     */     }
/* 105 */     LinkedHashSet<StandardDocFile> linkedHashSet = new LinkedHashSet();
/* 106 */     if (this.fileManager.hasLocation(paramLocation))
/* 107 */       for (Path path : this.fileManager.getLocation(paramLocation)) {
/* 108 */         if (Files.isDirectory(path, new java.nio.file.LinkOption[0])) {
/* 109 */           path = path.resolve(paramDocPath.getPath());
/* 110 */           if (Files.exists(path, new java.nio.file.LinkOption[0])) {
/* 111 */             linkedHashSet.add(new StandardDocFile(path));
/*     */           }
/*     */         }
/*     */       }
/* 115 */     return (Iterable)linkedHashSet;
/*     */   }
/*     */
/*     */   class StandardDocFile
/*     */     extends DocFile {
/*     */     private Path file;
/*     */
/*     */     private StandardDocFile(Path param1Path) {
/* 123 */       super(PathDocFileFactory.this.configuration);
/* 124 */       this.file = param1Path;
/*     */     }
/*     */
/*     */
/*     */     private StandardDocFile(JavaFileManager.Location param1Location, DocPath param1DocPath) {
/* 129 */       super(PathDocFileFactory.this.configuration, param1Location, param1DocPath);
/* 130 */       this.file = PathDocFileFactory.this.destDir.resolve(param1DocPath.getPath());
/*     */     }
/*     */
/*     */
/*     */     public InputStream openInputStream() throws IOException {
/* 135 */       JavaFileObject javaFileObject = getJavaFileObjectForInput(this.file);
/* 136 */       return new BufferedInputStream(javaFileObject.openInputStream());
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public OutputStream openOutputStream() throws IOException, UnsupportedEncodingException {
/* 145 */       if (this.location != DocumentationTool.Location.DOCUMENTATION_OUTPUT) {
/* 146 */         throw new IllegalStateException();
/*     */       }
/* 148 */       OutputStream outputStream = getFileObjectForOutput(this.path).openOutputStream();
/* 149 */       return new BufferedOutputStream(outputStream);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public Writer openWriter() throws IOException, UnsupportedEncodingException {
/* 159 */       if (this.location != DocumentationTool.Location.DOCUMENTATION_OUTPUT) {
/* 160 */         throw new IllegalStateException();
/*     */       }
/* 162 */       OutputStream outputStream = getFileObjectForOutput(this.path).openOutputStream();
/* 163 */       if (PathDocFileFactory.this.configuration.docencoding == null) {
/* 164 */         return new BufferedWriter(new OutputStreamWriter(outputStream));
/*     */       }
/* 166 */       return new BufferedWriter(new OutputStreamWriter(outputStream, PathDocFileFactory.this.configuration.docencoding));
/*     */     }
/*     */
/*     */
/*     */
/*     */     public boolean canRead() {
/* 172 */       return Files.isReadable(this.file);
/*     */     }
/*     */
/*     */
/*     */     public boolean canWrite() {
/* 177 */       return Files.isWritable(this.file);
/*     */     }
/*     */
/*     */
/*     */     public boolean exists() {
/* 182 */       return Files.exists(this.file, new java.nio.file.LinkOption[0]);
/*     */     }
/*     */
/*     */
/*     */     public String getName() {
/* 187 */       return this.file.getFileName().toString();
/*     */     }
/*     */
/*     */
/*     */     public String getPath() {
/* 192 */       return this.file.toString();
/*     */     }
/*     */
/*     */
/*     */     public boolean isAbsolute() {
/* 197 */       return this.file.isAbsolute();
/*     */     }
/*     */
/*     */
/*     */     public boolean isDirectory() {
/* 202 */       return Files.isDirectory(this.file, new java.nio.file.LinkOption[0]);
/*     */     }
/*     */
/*     */
/*     */     public boolean isFile() {
/* 207 */       return Files.isRegularFile(this.file, new java.nio.file.LinkOption[0]);
/*     */     }
/*     */
/*     */
/*     */     public boolean isSameFile(DocFile param1DocFile) {
/* 212 */       if (!(param1DocFile instanceof StandardDocFile)) {
/* 213 */         return false;
/*     */       }
/*     */       try {
/* 216 */         return Files.isSameFile(this.file, ((StandardDocFile)param1DocFile).file);
/* 217 */       } catch (IOException iOException) {
/* 218 */         return false;
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public Iterable<DocFile> list() throws IOException {
/* 224 */       ArrayList<StandardDocFile> arrayList = new ArrayList();
/* 225 */       try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(this.file)) {
/* 226 */         for (Path path : directoryStream) {
/* 227 */           arrayList.add(new StandardDocFile(path));
/*     */         }
/*     */       }
/* 230 */       return (Iterable)arrayList;
/*     */     }
/*     */
/*     */
/*     */     public boolean mkdirs() {
/*     */       try {
/* 236 */         Files.createDirectories(this.file, (FileAttribute<?>[])new FileAttribute[0]);
/* 237 */         return true;
/* 238 */       } catch (IOException iOException) {
/* 239 */         return false;
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public DocFile resolve(DocPath param1DocPath) {
/* 250 */       return resolve(param1DocPath.getPath());
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public DocFile resolve(String param1String) {
/* 260 */       if (this.location == null && this.path == null) {
/* 261 */         return new StandardDocFile(this.file.resolve(param1String));
/*     */       }
/* 263 */       return new StandardDocFile(this.location, this.path.resolve(param1String));
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public DocFile resolveAgainst(JavaFileManager.Location param1Location) {
/* 273 */       if (param1Location != DocumentationTool.Location.DOCUMENTATION_OUTPUT)
/* 274 */         throw new IllegalArgumentException();
/* 275 */       return new StandardDocFile(PathDocFileFactory.this.destDir.resolve(this.file));
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public String toString() {
/* 283 */       StringBuilder stringBuilder = new StringBuilder();
/* 284 */       stringBuilder.append("PathDocFile[");
/* 285 */       if (this.location != null)
/* 286 */         stringBuilder.append("locn:").append(this.location).append(",");
/* 287 */       if (this.path != null)
/* 288 */         stringBuilder.append("path:").append(this.path.getPath()).append(",");
/* 289 */       stringBuilder.append("file:").append(this.file);
/* 290 */       stringBuilder.append("]");
/* 291 */       return stringBuilder.toString();
/*     */     }
/*     */
/*     */     private JavaFileObject getJavaFileObjectForInput(Path param1Path) {
/* 295 */       return PathDocFileFactory.this.fileManager.getJavaFileObjects(new Path[] { param1Path }).iterator().next();
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private FileObject getFileObjectForOutput(DocPath param1DocPath) throws IOException {
/* 303 */       String str1 = param1DocPath.getPath();
/* 304 */       byte b = -1;
/* 305 */       for (byte b1 = 0; b1 < str1.length(); b1++) {
/* 306 */         char c = str1.charAt(b1);
/* 307 */         if (c == '/') {
/* 308 */           b = b1;
/* 309 */         } else if ((b1 == b + 1 && !Character.isJavaIdentifierStart(c)) ||
/* 310 */           !Character.isJavaIdentifierPart(c)) {
/*     */           break;
/*     */         }
/*     */       }
/* 314 */       String str2 = (b == -1) ? "" : str1.substring(0, b);
/* 315 */       String str3 = str1.substring(b + 1);
/* 316 */       return PathDocFileFactory.this.fileManager.getFileForOutput(this.location, str2, str3, null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\PathDocFileFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
