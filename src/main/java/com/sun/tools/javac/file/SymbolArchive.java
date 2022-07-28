/*     */ package com.sun.tools.javac.file;
/*     */
/*     */ import com.sun.tools.javac.util.List;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
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
/*     */ public class SymbolArchive
/*     */   extends ZipArchive
/*     */ {
/*     */   final File origFile;
/*     */   final RelativePath.RelativeDirectory prefix;
/*     */
/*     */   public SymbolArchive(JavacFileManager paramJavacFileManager, File paramFile, ZipFile paramZipFile, RelativePath.RelativeDirectory paramRelativeDirectory) throws IOException {
/*  50 */     super(paramJavacFileManager, paramZipFile, false);
/*  51 */     this.origFile = paramFile;
/*  52 */     this.prefix = paramRelativeDirectory;
/*  53 */     initMap();
/*     */   }
/*     */
/*     */
/*     */   void addZipEntry(ZipEntry paramZipEntry) {
/*  58 */     String str1 = paramZipEntry.getName();
/*  59 */     if (!str1.startsWith(this.prefix.path)) {
/*     */       return;
/*     */     }
/*  62 */     str1 = str1.substring(this.prefix.path.length());
/*  63 */     int i = str1.lastIndexOf('/');
/*  64 */     RelativePath.RelativeDirectory relativeDirectory = new RelativePath.RelativeDirectory(str1.substring(0, i + 1));
/*  65 */     String str2 = str1.substring(i + 1);
/*  66 */     if (str2.length() == 0) {
/*     */       return;
/*     */     }
/*  69 */     List<String> list = this.map.get(relativeDirectory);
/*  70 */     if (list == null)
/*  71 */       list = List.nil();
/*  72 */     list = list.prepend(str2);
/*  73 */     this.map.put(relativeDirectory, list);
/*     */   }
/*     */
/*     */
/*     */   public JavaFileObject getFileObject(RelativePath.RelativeDirectory paramRelativeDirectory, String paramString) {
/*  78 */     RelativePath.RelativeDirectory relativeDirectory = new RelativePath.RelativeDirectory(this.prefix, paramRelativeDirectory.path);
/*  79 */     ZipEntry zipEntry = (new RelativePath.RelativeFile(relativeDirectory, paramString)).getZipEntry(this.zfile);
/*  80 */     return new SymbolFileObject(this, paramString, zipEntry);
/*     */   }
/*     */
/*     */
/*     */   public String toString() {
/*  85 */     return "SymbolArchive[" + this.zfile.getName() + "]";
/*     */   }
/*     */
/*     */
/*     */   public static class SymbolFileObject
/*     */     extends ZipFileObject
/*     */   {
/*     */     protected SymbolFileObject(SymbolArchive param1SymbolArchive, String param1String, ZipEntry param1ZipEntry) {
/*  93 */       super(param1SymbolArchive, param1String, param1ZipEntry);
/*     */     }
/*     */
/*     */
/*     */     protected String inferBinaryName(Iterable<? extends File> param1Iterable) {
/*  98 */       String str1 = this.entry.getName();
/*  99 */       String str2 = ((SymbolArchive)this.zarch).prefix.path;
/* 100 */       if (str1.startsWith(str2))
/* 101 */         str1 = str1.substring(str2.length());
/* 102 */       return removeExtension(str1).replace('/', '.');
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\file\SymbolArchive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
