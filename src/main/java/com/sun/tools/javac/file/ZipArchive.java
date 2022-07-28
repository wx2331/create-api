/*     */ package com.sun.tools.javac.file;
/*     */
/*     */ import com.sun.tools.javac.util.List;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.net.URI;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class ZipArchive
/*     */   implements JavacFileManager.Archive
/*     */ {
/*     */   protected JavacFileManager fileManager;
/*     */   protected final Map<RelativePath.RelativeDirectory, List<String>> map;
/*     */   protected final ZipFile zfile;
/*     */   protected Reference<File> absFileRef;
/*     */
/*     */   public ZipArchive(JavacFileManager paramJavacFileManager, ZipFile paramZipFile) throws IOException {
/*  62 */     this(paramJavacFileManager, paramZipFile, true);
/*     */   }
/*     */
/*     */   protected ZipArchive(JavacFileManager paramJavacFileManager, ZipFile paramZipFile, boolean paramBoolean) throws IOException {
/*  66 */     this.fileManager = paramJavacFileManager;
/*  67 */     this.zfile = paramZipFile;
/*  68 */     this.map = new HashMap<>();
/*  69 */     if (paramBoolean)
/*  70 */       initMap();
/*     */   }
/*     */
/*     */   protected void initMap() throws IOException {
/*  74 */     for (Enumeration<? extends ZipEntry> enumeration = this.zfile.entries(); enumeration.hasMoreElements(); ) {
/*     */       ZipEntry zipEntry;
/*     */       try {
/*  77 */         zipEntry = enumeration.nextElement();
/*  78 */       } catch (InternalError internalError) {
/*  79 */         IOException iOException = new IOException();
/*  80 */         iOException.initCause(internalError);
/*  81 */         throw iOException;
/*     */       }
/*  83 */       addZipEntry(zipEntry);
/*     */     }
/*     */   }
/*     */
/*     */   void addZipEntry(ZipEntry paramZipEntry) {
/*  88 */     String str1 = paramZipEntry.getName();
/*  89 */     int i = str1.lastIndexOf('/');
/*  90 */     RelativePath.RelativeDirectory relativeDirectory = new RelativePath.RelativeDirectory(str1.substring(0, i + 1));
/*  91 */     String str2 = str1.substring(i + 1);
/*  92 */     if (str2.length() == 0)
/*     */       return;
/*  94 */     List<String> list = this.map.get(relativeDirectory);
/*  95 */     if (list == null)
/*  96 */       list = List.nil();
/*  97 */     list = list.prepend(str2);
/*  98 */     this.map.put(relativeDirectory, list);
/*     */   }
/*     */
/*     */   public boolean contains(RelativePath paramRelativePath) {
/* 102 */     RelativePath.RelativeDirectory relativeDirectory = paramRelativePath.dirname();
/* 103 */     String str = paramRelativePath.basename();
/* 104 */     if (str.length() == 0)
/* 105 */       return false;
/* 106 */     List list = this.map.get(relativeDirectory);
/* 107 */     return (list != null && list.contains(str));
/*     */   }
/*     */
/*     */   public List<String> getFiles(RelativePath.RelativeDirectory paramRelativeDirectory) {
/* 111 */     return this.map.get(paramRelativeDirectory);
/*     */   }
/*     */
/*     */   public JavaFileObject getFileObject(RelativePath.RelativeDirectory paramRelativeDirectory, String paramString) {
/* 115 */     ZipEntry zipEntry = (new RelativePath.RelativeFile(paramRelativeDirectory, paramString)).getZipEntry(this.zfile);
/* 116 */     return new ZipFileObject(this, paramString, zipEntry);
/*     */   }
/*     */
/*     */   public Set<RelativePath.RelativeDirectory> getSubdirectories() {
/* 120 */     return this.map.keySet();
/*     */   }
/*     */
/*     */   public void close() throws IOException {
/* 124 */     this.zfile.close();
/*     */   }
/*     */
/*     */
/*     */   public String toString() {
/* 129 */     return "ZipArchive[" + this.zfile.getName() + "]";
/*     */   }
/*     */
/*     */   private File getAbsoluteFile() {
/* 133 */     File file = (this.absFileRef == null) ? null : this.absFileRef.get();
/* 134 */     if (file == null) {
/* 135 */       file = (new File(this.zfile.getName())).getAbsoluteFile();
/* 136 */       this.absFileRef = new SoftReference<>(file);
/*     */     }
/* 138 */     return file;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static class ZipFileObject
/*     */     extends BaseFileObject
/*     */   {
/*     */     private String name;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     ZipArchive zarch;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     ZipEntry entry;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     protected ZipFileObject(ZipArchive param1ZipArchive, String param1String, ZipEntry param1ZipEntry) {
/* 168 */       super(param1ZipArchive.fileManager);
/* 169 */       this.zarch = param1ZipArchive;
/* 170 */       this.name = param1String;
/* 171 */       this.entry = param1ZipEntry;
/*     */     }
/*     */
/*     */     public URI toUri() {
/* 175 */       File file = new File(this.zarch.zfile.getName());
/* 176 */       return createJarUri(file, this.entry.getName());
/*     */     }
/*     */
/*     */
/*     */     public String getName() {
/* 181 */       return this.zarch.zfile.getName() + "(" + this.entry.getName() + ")";
/*     */     }
/*     */
/*     */
/*     */     public String getShortName() {
/* 186 */       return (new File(this.zarch.zfile.getName())).getName() + "(" + this.entry + ")";
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 191 */       return getKind(this.entry.getName());
/*     */     }
/*     */
/*     */
/*     */     public InputStream openInputStream() throws IOException {
/* 196 */       return this.zarch.zfile.getInputStream(this.entry);
/*     */     }
/*     */
/*     */
/*     */     public OutputStream openOutputStream() throws IOException {
/* 201 */       throw new UnsupportedOperationException();
/*     */     }
/*     */
/*     */
/*     */     public CharBuffer getCharContent(boolean param1Boolean) throws IOException {
/* 206 */       CharBuffer charBuffer = this.fileManager.getCachedContent(this);
/* 207 */       if (charBuffer == null) {
/* 208 */         InputStream inputStream = this.zarch.zfile.getInputStream(this.entry);
/*     */         try {
/* 210 */           ByteBuffer byteBuffer = this.fileManager.makeByteBuffer(inputStream);
/* 211 */           JavaFileObject javaFileObject = this.fileManager.log.useSource(this);
/*     */           try {
/* 213 */             charBuffer = this.fileManager.decode(byteBuffer, param1Boolean);
/*     */           } finally {
/* 215 */             this.fileManager.log.useSource(javaFileObject);
/*     */           }
/* 217 */           this.fileManager.recycleByteBuffer(byteBuffer);
/* 218 */           if (!param1Boolean) {
/* 219 */             this.fileManager.cache(this, charBuffer);
/*     */           }
/*     */         } finally {
/* 222 */           inputStream.close();
/*     */         }
/*     */       }
/* 225 */       return charBuffer;
/*     */     }
/*     */
/*     */
/*     */     public Writer openWriter() throws IOException {
/* 230 */       throw new UnsupportedOperationException();
/*     */     }
/*     */
/*     */
/*     */     public long getLastModified() {
/* 235 */       return this.entry.getTime();
/*     */     }
/*     */
/*     */
/*     */     public boolean delete() {
/* 240 */       throw new UnsupportedOperationException();
/*     */     }
/*     */
/*     */
/*     */     protected CharsetDecoder getDecoder(boolean param1Boolean) {
/* 245 */       return this.fileManager.getDecoder(this.fileManager.getEncodingName(), param1Boolean);
/*     */     }
/*     */
/*     */
/*     */     protected String inferBinaryName(Iterable<? extends File> param1Iterable) {
/* 250 */       String str = this.entry.getName();
/* 251 */       return removeExtension(str).replace('/', '.');
/*     */     }
/*     */
/*     */
/*     */     public boolean isNameCompatible(String param1String, Kind param1Kind) {
/* 256 */       param1String.getClass();
/*     */
/* 258 */       if (param1Kind == Kind.OTHER && getKind() != param1Kind) {
/* 259 */         return false;
/*     */       }
/* 261 */       return this.name.equals(param1String + param1Kind.extension);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public boolean equals(Object param1Object) {
/* 271 */       if (this == param1Object) {
/* 272 */         return true;
/*     */       }
/* 274 */       if (!(param1Object instanceof ZipFileObject)) {
/* 275 */         return false;
/*     */       }
/* 277 */       ZipFileObject zipFileObject = (ZipFileObject)param1Object;
/* 278 */       return (this.zarch.getAbsoluteFile().equals(zipFileObject.zarch.getAbsoluteFile()) && this.name
/* 279 */         .equals(zipFileObject.name));
/*     */     }
/*     */
/*     */
/*     */     public int hashCode() {
/* 284 */       return this.zarch.getAbsoluteFile().hashCode() + this.name.hashCode();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\file\ZipArchive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
