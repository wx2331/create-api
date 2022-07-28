/*     */ package com.sun.tools.javac.file;
/*     */
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ import java.net.URI;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.util.Set;
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
/*     */ public class ZipFileIndexArchive
/*     */   implements JavacFileManager.Archive
/*     */ {
/*     */   private final ZipFileIndex zfIndex;
/*     */   private JavacFileManager fileManager;
/*     */
/*     */   public ZipFileIndexArchive(JavacFileManager paramJavacFileManager, ZipFileIndex paramZipFileIndex) throws IOException {
/*  61 */     this.fileManager = paramJavacFileManager;
/*  62 */     this.zfIndex = paramZipFileIndex;
/*     */   }
/*     */
/*     */   public boolean contains(RelativePath paramRelativePath) {
/*  66 */     return this.zfIndex.contains(paramRelativePath);
/*     */   }
/*     */
/*     */   public List<String> getFiles(RelativePath.RelativeDirectory paramRelativeDirectory) {
/*  70 */     return this.zfIndex.getFiles(paramRelativeDirectory);
/*     */   }
/*     */
/*     */   public JavaFileObject getFileObject(RelativePath.RelativeDirectory paramRelativeDirectory, String paramString) {
/*  74 */     RelativePath.RelativeFile relativeFile = new RelativePath.RelativeFile(paramRelativeDirectory, paramString);
/*  75 */     ZipFileIndex.Entry entry = this.zfIndex.getZipIndexEntry(relativeFile);
/*  76 */     return new ZipFileIndexFileObject(this.fileManager, this.zfIndex, entry, this.zfIndex.getZipFile());
/*     */   }
/*     */
/*     */
/*     */   public Set<RelativePath.RelativeDirectory> getSubdirectories() {
/*  81 */     return this.zfIndex.getAllDirectories();
/*     */   }
/*     */
/*     */   public void close() throws IOException {
/*  85 */     this.zfIndex.close();
/*     */   }
/*     */
/*     */
/*     */   public String toString() {
/*  90 */     return "ZipFileIndexArchive[" + this.zfIndex + "]";
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static class ZipFileIndexFileObject
/*     */     extends BaseFileObject
/*     */   {
/*     */     private String name;
/*     */
/*     */
/*     */
/*     */     ZipFileIndex zfIndex;
/*     */
/*     */
/*     */
/*     */     ZipFileIndex.Entry entry;
/*     */
/*     */
/*     */
/* 112 */     InputStream inputStream = null;
/*     */
/*     */
/*     */     File zipName;
/*     */
/*     */
/*     */
/*     */     ZipFileIndexFileObject(JavacFileManager param1JavacFileManager, ZipFileIndex param1ZipFileIndex, ZipFileIndex.Entry param1Entry, File param1File) {
/* 120 */       super(param1JavacFileManager);
/* 121 */       this.name = param1Entry.getFileName();
/* 122 */       this.zfIndex = param1ZipFileIndex;
/* 123 */       this.entry = param1Entry;
/* 124 */       this.zipName = param1File;
/*     */     }
/*     */
/*     */
/*     */     public URI toUri() {
/* 129 */       return createJarUri(this.zipName, getPrefixedEntryName());
/*     */     }
/*     */
/*     */
/*     */     public String getName() {
/* 134 */       return this.zipName + "(" + getPrefixedEntryName() + ")";
/*     */     }
/*     */
/*     */
/*     */     public String getShortName() {
/* 139 */       return this.zipName.getName() + "(" + this.entry.getName() + ")";
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 144 */       return getKind(this.entry.getName());
/*     */     }
/*     */
/*     */
/*     */     public InputStream openInputStream() throws IOException {
/* 149 */       if (this.inputStream == null) {
/* 150 */         Assert.checkNonNull(this.entry);
/* 151 */         this.inputStream = new ByteArrayInputStream(this.zfIndex.read(this.entry));
/*     */       }
/* 153 */       return this.inputStream;
/*     */     }
/*     */
/*     */
/*     */     public OutputStream openOutputStream() throws IOException {
/* 158 */       throw new UnsupportedOperationException();
/*     */     }
/*     */
/*     */
/*     */     public CharBuffer getCharContent(boolean param1Boolean) throws IOException {
/* 163 */       CharBuffer charBuffer = this.fileManager.getCachedContent(this);
/* 164 */       if (charBuffer == null) {
/* 165 */         ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.zfIndex.read(this.entry));
/*     */         try {
/* 167 */           ByteBuffer byteBuffer = this.fileManager.makeByteBuffer(byteArrayInputStream);
/* 168 */           JavaFileObject javaFileObject = this.fileManager.log.useSource(this);
/*     */           try {
/* 170 */             charBuffer = this.fileManager.decode(byteBuffer, param1Boolean);
/*     */           } finally {
/* 172 */             this.fileManager.log.useSource(javaFileObject);
/*     */           }
/* 174 */           this.fileManager.recycleByteBuffer(byteBuffer);
/* 175 */           if (!param1Boolean)
/* 176 */             this.fileManager.cache(this, charBuffer);
/*     */         } finally {
/* 178 */           byteArrayInputStream.close();
/*     */         }
/*     */       }
/* 181 */       return charBuffer;
/*     */     }
/*     */
/*     */
/*     */     public Writer openWriter() throws IOException {
/* 186 */       throw new UnsupportedOperationException();
/*     */     }
/*     */
/*     */
/*     */     public long getLastModified() {
/* 191 */       return this.entry.getLastModified();
/*     */     }
/*     */
/*     */
/*     */     public boolean delete() {
/* 196 */       throw new UnsupportedOperationException();
/*     */     }
/*     */
/*     */
/*     */     protected CharsetDecoder getDecoder(boolean param1Boolean) {
/* 201 */       return this.fileManager.getDecoder(this.fileManager.getEncodingName(), param1Boolean);
/*     */     }
/*     */
/*     */
/*     */     protected String inferBinaryName(Iterable<? extends File> param1Iterable) {
/* 206 */       String str = this.entry.getName();
/* 207 */       if (this.zfIndex.symbolFilePrefix != null) {
/* 208 */         String str1 = this.zfIndex.symbolFilePrefix.path;
/* 209 */         if (str.startsWith(str1))
/* 210 */           str = str.substring(str1.length());
/*     */       }
/* 212 */       return removeExtension(str).replace('/', '.');
/*     */     }
/*     */
/*     */
/*     */     public boolean isNameCompatible(String param1String, Kind param1Kind) {
/* 217 */       param1String.getClass();
/* 218 */       if (param1Kind == Kind.OTHER && getKind() != param1Kind)
/* 219 */         return false;
/* 220 */       return this.name.equals(param1String + param1Kind.extension);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public boolean equals(Object param1Object) {
/* 230 */       if (this == param1Object) {
/* 231 */         return true;
/*     */       }
/* 233 */       if (!(param1Object instanceof ZipFileIndexFileObject)) {
/* 234 */         return false;
/*     */       }
/* 236 */       ZipFileIndexFileObject zipFileIndexFileObject = (ZipFileIndexFileObject)param1Object;
/* 237 */       return (this.zfIndex.getAbsoluteFile().equals(zipFileIndexFileObject.zfIndex.getAbsoluteFile()) && this.name
/* 238 */         .equals(zipFileIndexFileObject.name));
/*     */     }
/*     */
/*     */
/*     */     public int hashCode() {
/* 243 */       return this.zfIndex.getAbsoluteFile().hashCode() + this.name.hashCode();
/*     */     }
/*     */
/*     */     private String getPrefixedEntryName() {
/* 247 */       if (this.zfIndex.symbolFilePrefix != null) {
/* 248 */         return this.zfIndex.symbolFilePrefix.path + this.entry.getName();
/*     */       }
/* 250 */       return this.entry.getName();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\file\ZipFileIndexArchive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
