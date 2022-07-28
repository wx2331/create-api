/*     */ package com.sun.tools.javac.file;
/*     */
/*     */ import java.io.File;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
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
/*     */ public abstract class RelativePath
/*     */   implements Comparable<RelativePath>
/*     */ {
/*     */   protected final String path;
/*     */
/*     */   protected RelativePath(String paramString) {
/*  48 */     this.path = paramString;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public File getFile(File paramFile) {
/*  56 */     if (this.path.length() == 0)
/*  57 */       return paramFile;
/*  58 */     return new File(paramFile, this.path.replace('/', File.separatorChar));
/*     */   }
/*     */
/*     */   public int compareTo(RelativePath paramRelativePath) {
/*  62 */     return this.path.compareTo(paramRelativePath.path);
/*     */   }
/*     */
/*     */
/*     */   public boolean equals(Object paramObject) {
/*  67 */     if (!(paramObject instanceof RelativePath))
/*  68 */       return false;
/*  69 */     return this.path.equals(((RelativePath)paramObject).path);
/*     */   }
/*     */
/*     */
/*     */   public int hashCode() {
/*  74 */     return this.path.hashCode();
/*     */   }
/*     */
/*     */
/*     */   public String toString() {
/*  79 */     return "RelPath[" + this.path + "]";
/*     */   }
/*     */
/*     */   public String getPath() {
/*  83 */     return this.path;
/*     */   }
/*     */
/*     */
/*     */   public abstract RelativeDirectory dirname();
/*     */
/*     */
/*     */   public abstract String basename();
/*     */
/*     */
/*     */   public static class RelativeDirectory
/*     */     extends RelativePath
/*     */   {
/*     */     static RelativeDirectory forPackage(CharSequence param1CharSequence) {
/*  97 */       return new RelativeDirectory(param1CharSequence.toString().replace('.', '/'));
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     public RelativeDirectory(String param1String) {
/* 104 */       super((param1String.length() == 0 || param1String.endsWith("/")) ? param1String : (param1String + "/"));
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     public RelativeDirectory(RelativeDirectory param1RelativeDirectory, String param1String) {
/* 111 */       this(param1RelativeDirectory.path + param1String);
/*     */     }
/*     */
/*     */
/*     */     public RelativeDirectory dirname() {
/* 116 */       int i = this.path.length();
/* 117 */       if (i == 0)
/* 118 */         return this;
/* 119 */       int j = this.path.lastIndexOf('/', i - 2);
/* 120 */       return new RelativeDirectory(this.path.substring(0, j + 1));
/*     */     }
/*     */
/*     */
/*     */     public String basename() {
/* 125 */       int i = this.path.length();
/* 126 */       if (i == 0)
/* 127 */         return this.path;
/* 128 */       int j = this.path.lastIndexOf('/', i - 2);
/* 129 */       return this.path.substring(j + 1, i - 1);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     boolean contains(RelativePath param1RelativePath) {
/* 137 */       return (param1RelativePath.path.length() > this.path.length() && param1RelativePath.path.startsWith(this.path));
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 142 */       return "RelativeDirectory[" + this.path + "]";
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public static class RelativeFile
/*     */     extends RelativePath
/*     */   {
/*     */     static RelativeFile forClass(CharSequence param1CharSequence, JavaFileObject.Kind param1Kind) {
/* 153 */       return new RelativeFile(param1CharSequence.toString().replace('.', '/') + param1Kind.extension);
/*     */     }
/*     */
/*     */     public RelativeFile(String param1String) {
/* 157 */       super(param1String);
/* 158 */       if (param1String.endsWith("/")) {
/* 159 */         throw new IllegalArgumentException(param1String);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/*     */     public RelativeFile(RelativeDirectory param1RelativeDirectory, String param1String) {
/* 166 */       this(param1RelativeDirectory.path + param1String);
/*     */     }
/*     */
/*     */     RelativeFile(RelativeDirectory param1RelativeDirectory, RelativePath param1RelativePath) {
/* 170 */       this(param1RelativeDirectory, param1RelativePath.path);
/*     */     }
/*     */
/*     */
/*     */     public RelativeDirectory dirname() {
/* 175 */       int i = this.path.lastIndexOf('/');
/* 176 */       return new RelativeDirectory(this.path.substring(0, i + 1));
/*     */     }
/*     */
/*     */
/*     */     public String basename() {
/* 181 */       int i = this.path.lastIndexOf('/');
/* 182 */       return this.path.substring(i + 1);
/*     */     }
/*     */
/*     */     ZipEntry getZipEntry(ZipFile param1ZipFile) {
/* 186 */       return param1ZipFile.getEntry(this.path);
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 191 */       return "RelativeFile[" + this.path + "]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\file\RelativePath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
