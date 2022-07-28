/*     */ package com.sun.tools.javac.nio;
/*     */
/*     */ import com.sun.tools.javac.util.BaseFileManager;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.net.URI;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.NestingKind;
/*     */ import javax.tools.JavaFileObject;
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
/*     */
/*     */
/*     */
/*     */
/*     */ abstract class PathFileObject
/*     */   implements JavaFileObject
/*     */ {
/*     */   private JavacPathFileManager fileManager;
/*     */   private Path path;
/*     */
/*     */   static PathFileObject createDirectoryPathFileObject(JavacPathFileManager paramJavacPathFileManager, final Path path, final Path dir) {
/*  73 */     return new PathFileObject(paramJavacPathFileManager, path)
/*     */       {
/*     */         String inferBinaryName(Iterable<? extends Path> param1Iterable) {
/*  76 */           return toBinaryName(dir.relativize(path));
/*     */         }
/*     */       };
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   static PathFileObject createJarPathFileObject(JavacPathFileManager paramJavacPathFileManager, final Path path) {
/*  87 */     return new PathFileObject(paramJavacPathFileManager, path)
/*     */       {
/*     */         String inferBinaryName(Iterable<? extends Path> param1Iterable) {
/*  90 */           return toBinaryName(path);
/*     */         }
/*     */       };
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   static PathFileObject createSiblingPathFileObject(JavacPathFileManager paramJavacPathFileManager, Path paramPath, final String relativePath) {
/* 101 */     return new PathFileObject(paramJavacPathFileManager, paramPath)
/*     */       {
/*     */         String inferBinaryName(Iterable<? extends Path> param1Iterable) {
/* 104 */           return toBinaryName(relativePath, "/");
/*     */         }
/*     */       };
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   static PathFileObject createSimplePathFileObject(JavacPathFileManager paramJavacPathFileManager, final Path path) {
/* 115 */     return new PathFileObject(paramJavacPathFileManager, path)
/*     */       {
/*     */         String inferBinaryName(Iterable<? extends Path> param1Iterable) {
/* 118 */           Path path = path.toAbsolutePath();
/* 119 */           for (Path path1 : param1Iterable) {
/* 120 */             Path path2 = path1.toAbsolutePath();
/* 121 */             if (path.startsWith(path2)) {
/*     */               try {
/* 123 */                 Path path3 = path2.relativize(path);
/* 124 */                 if (path3 != null)
/* 125 */                   return toBinaryName(path3);
/* 126 */               } catch (IllegalArgumentException illegalArgumentException) {}
/*     */             }
/*     */           }
/*     */
/*     */
/* 131 */           return null;
/*     */         }
/*     */       };
/*     */   }
/*     */
/*     */   protected PathFileObject(JavacPathFileManager paramJavacPathFileManager, Path paramPath) {
/* 137 */     paramJavacPathFileManager.getClass();
/* 138 */     paramPath.getClass();
/* 139 */     this.fileManager = paramJavacPathFileManager;
/* 140 */     this.path = paramPath;
/*     */   }
/*     */
/*     */
/*     */
/*     */   abstract String inferBinaryName(Iterable<? extends Path> paramIterable);
/*     */
/*     */
/*     */
/*     */   Path getPath() {
/* 150 */     return this.path;
/*     */   }
/*     */
/*     */
/*     */   public Kind getKind() {
/* 155 */     return BaseFileManager.getKind(this.path.getFileName().toString());
/*     */   }
/*     */
/*     */
/*     */   public boolean isNameCompatible(String paramString, Kind paramKind) {
/* 160 */     paramString.getClass();
/*     */
/* 162 */     if (paramKind == Kind.OTHER && getKind() != paramKind) {
/* 163 */       return false;
/*     */     }
/* 165 */     String str1 = paramString + paramKind.extension;
/* 166 */     String str2 = this.path.getFileName().toString();
/* 167 */     if (str2.equals(str1)) {
/* 168 */       return true;
/*     */     }
/* 170 */     if (str2.equalsIgnoreCase(str1)) {
/*     */
/*     */       try {
/* 173 */         return this.path.toRealPath(new LinkOption[] { LinkOption.NOFOLLOW_LINKS }).getFileName().toString().equals(str1);
/* 174 */       } catch (IOException iOException) {}
/*     */     }
/*     */
/* 177 */     return false;
/*     */   }
/*     */
/*     */
/*     */   public NestingKind getNestingKind() {
/* 182 */     return null;
/*     */   }
/*     */
/*     */
/*     */   public Modifier getAccessLevel() {
/* 187 */     return null;
/*     */   }
/*     */
/*     */
/*     */   public URI toUri() {
/* 192 */     return this.path.toUri();
/*     */   }
/*     */
/*     */
/*     */   public String getName() {
/* 197 */     return this.path.toString();
/*     */   }
/*     */
/*     */
/*     */   public InputStream openInputStream() throws IOException {
/* 202 */     return Files.newInputStream(this.path, new java.nio.file.OpenOption[0]);
/*     */   }
/*     */
/*     */
/*     */   public OutputStream openOutputStream() throws IOException {
/* 207 */     this.fileManager.flushCache(this);
/* 208 */     ensureParentDirectoriesExist();
/* 209 */     return Files.newOutputStream(this.path, new java.nio.file.OpenOption[0]);
/*     */   }
/*     */
/*     */
/*     */   public Reader openReader(boolean paramBoolean) throws IOException {
/* 214 */     CharsetDecoder charsetDecoder = this.fileManager.getDecoder(this.fileManager.getEncodingName(), paramBoolean);
/* 215 */     return new InputStreamReader(openInputStream(), charsetDecoder);
/*     */   }
/*     */
/*     */
/*     */   public CharSequence getCharContent(boolean paramBoolean) throws IOException {
/* 220 */     CharBuffer charBuffer = this.fileManager.getCachedContent(this);
/* 221 */     if (charBuffer == null) {
/* 222 */       InputStream inputStream = openInputStream();
/*     */       try {
/* 224 */         ByteBuffer byteBuffer = this.fileManager.makeByteBuffer(inputStream);
/* 225 */         JavaFileObject javaFileObject = this.fileManager.log.useSource(this);
/*     */         try {
/* 227 */           charBuffer = this.fileManager.decode(byteBuffer, paramBoolean);
/*     */         } finally {
/* 229 */           this.fileManager.log.useSource(javaFileObject);
/*     */         }
/* 231 */         this.fileManager.recycleByteBuffer(byteBuffer);
/* 232 */         if (!paramBoolean) {
/* 233 */           this.fileManager.cache(this, charBuffer);
/*     */         }
/*     */       } finally {
/* 236 */         inputStream.close();
/*     */       }
/*     */     }
/* 239 */     return charBuffer;
/*     */   }
/*     */
/*     */
/*     */   public Writer openWriter() throws IOException {
/* 244 */     this.fileManager.flushCache(this);
/* 245 */     ensureParentDirectoriesExist();
/* 246 */     return new OutputStreamWriter(Files.newOutputStream(this.path, new java.nio.file.OpenOption[0]), this.fileManager.getEncodingName());
/*     */   }
/*     */
/*     */
/*     */   public long getLastModified() {
/*     */     try {
/* 252 */       return Files.getLastModifiedTime(this.path, new LinkOption[0]).toMillis();
/* 253 */     } catch (IOException iOException) {
/* 254 */       return -1L;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public boolean delete() {
/*     */     try {
/* 261 */       Files.delete(this.path);
/* 262 */       return true;
/* 263 */     } catch (IOException iOException) {
/* 264 */       return false;
/*     */     }
/*     */   }
/*     */
/*     */   public boolean isSameFile(PathFileObject paramPathFileObject) {
/*     */     try {
/* 270 */       return Files.isSameFile(this.path, paramPathFileObject.path);
/* 271 */     } catch (IOException iOException) {
/* 272 */       return false;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public boolean equals(Object paramObject) {
/* 278 */     return (paramObject instanceof PathFileObject && this.path.equals(((PathFileObject)paramObject).path));
/*     */   }
/*     */
/*     */
/*     */   public int hashCode() {
/* 283 */     return this.path.hashCode();
/*     */   }
/*     */
/*     */
/*     */   public String toString() {
/* 288 */     return getClass().getSimpleName() + "[" + this.path + "]";
/*     */   }
/*     */
/*     */   private void ensureParentDirectoriesExist() throws IOException {
/* 292 */     Path path = this.path.getParent();
/* 293 */     if (path != null)
/* 294 */       Files.createDirectories(path, (FileAttribute<?>[])new FileAttribute[0]);
/*     */   }
/*     */
/*     */   private long size() {
/*     */     try {
/* 299 */       return Files.size(this.path);
/* 300 */     } catch (IOException iOException) {
/* 301 */       return -1L;
/*     */     }
/*     */   }
/*     */
/*     */   protected static String toBinaryName(Path paramPath) {
/* 306 */     return toBinaryName(paramPath.toString(), paramPath
/* 307 */         .getFileSystem().getSeparator());
/*     */   }
/*     */
/*     */   protected static String toBinaryName(String paramString1, String paramString2) {
/* 311 */     return removeExtension(paramString1).replace(paramString2, ".");
/*     */   }
/*     */
/*     */   protected static String removeExtension(String paramString) {
/* 315 */     int i = paramString.lastIndexOf(".");
/* 316 */     return (i == -1) ? paramString : paramString.substring(0, i);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\nio\PathFileObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
