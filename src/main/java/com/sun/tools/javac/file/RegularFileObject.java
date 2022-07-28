/*     */ package com.sun.tools.javac.file;
/*     */
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.net.URI;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.text.Normalizer;
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
/*     */ class RegularFileObject
/*     */   extends BaseFileObject
/*     */ {
/*     */   private boolean hasParents = false;
/*     */   private String name;
/*     */   final File file;
/*     */   private Reference<File> absFileRef;
/*  61 */   static final boolean isMacOS = System.getProperty("os.name", "").contains("OS X");
/*     */
/*     */   public RegularFileObject(JavacFileManager paramJavacFileManager, File paramFile) {
/*  64 */     this(paramJavacFileManager, paramFile.getName(), paramFile);
/*     */   }
/*     */
/*     */   public RegularFileObject(JavacFileManager paramJavacFileManager, String paramString, File paramFile) {
/*  68 */     super(paramJavacFileManager);
/*  69 */     if (paramFile.isDirectory()) {
/*  70 */       throw new IllegalArgumentException("directories not supported");
/*     */     }
/*  72 */     this.name = paramString;
/*  73 */     this.file = paramFile;
/*     */   }
/*     */
/*     */
/*     */   public URI toUri() {
/*  78 */     return this.file.toURI().normalize();
/*     */   }
/*     */
/*     */
/*     */   public String getName() {
/*  83 */     return this.file.getPath();
/*     */   }
/*     */
/*     */
/*     */   public String getShortName() {
/*  88 */     return this.name;
/*     */   }
/*     */
/*     */
/*     */   public Kind getKind() {
/*  93 */     return getKind(this.name);
/*     */   }
/*     */
/*     */
/*     */   public InputStream openInputStream() throws IOException {
/*  98 */     return new FileInputStream(this.file);
/*     */   }
/*     */
/*     */
/*     */   public OutputStream openOutputStream() throws IOException {
/* 103 */     this.fileManager.flushCache(this);
/* 104 */     ensureParentDirectoriesExist();
/* 105 */     return new FileOutputStream(this.file);
/*     */   }
/*     */
/*     */
/*     */   public CharBuffer getCharContent(boolean paramBoolean) throws IOException {
/* 110 */     CharBuffer charBuffer = this.fileManager.getCachedContent(this);
/* 111 */     if (charBuffer == null) {
/* 112 */       FileInputStream fileInputStream = new FileInputStream(this.file);
/*     */       try {
/* 114 */         ByteBuffer byteBuffer = this.fileManager.makeByteBuffer(fileInputStream);
/* 115 */         JavaFileObject javaFileObject = this.fileManager.log.useSource(this);
/*     */         try {
/* 117 */           charBuffer = this.fileManager.decode(byteBuffer, paramBoolean);
/*     */         } finally {
/* 119 */           this.fileManager.log.useSource(javaFileObject);
/*     */         }
/* 121 */         this.fileManager.recycleByteBuffer(byteBuffer);
/* 122 */         if (!paramBoolean) {
/* 123 */           this.fileManager.cache(this, charBuffer);
/*     */         }
/*     */       } finally {
/* 126 */         fileInputStream.close();
/*     */       }
/*     */     }
/* 129 */     return charBuffer;
/*     */   }
/*     */
/*     */
/*     */   public Writer openWriter() throws IOException {
/* 134 */     this.fileManager.flushCache(this);
/* 135 */     ensureParentDirectoriesExist();
/* 136 */     return new OutputStreamWriter(new FileOutputStream(this.file), this.fileManager.getEncodingName());
/*     */   }
/*     */
/*     */
/*     */   public long getLastModified() {
/* 141 */     return this.file.lastModified();
/*     */   }
/*     */
/*     */
/*     */   public boolean delete() {
/* 146 */     return this.file.delete();
/*     */   }
/*     */
/*     */
/*     */   protected CharsetDecoder getDecoder(boolean paramBoolean) {
/* 151 */     return this.fileManager.getDecoder(this.fileManager.getEncodingName(), paramBoolean);
/*     */   }
/*     */
/*     */
/*     */   protected String inferBinaryName(Iterable<? extends File> paramIterable) {
/* 156 */     String str = this.file.getPath();
/*     */
/* 158 */     for (File file : paramIterable) {
/*     */
/* 160 */       String str1 = file.getPath();
/* 161 */       if (str1.length() == 0)
/* 162 */         str1 = System.getProperty("user.dir");
/* 163 */       if (!str1.endsWith(File.separator))
/* 164 */         str1 = str1 + File.separator;
/* 165 */       if (str.regionMatches(true, 0, str1, 0, str1.length()) && (new File(str
/* 166 */           .substring(0, str1.length()))).equals(new File(str1))) {
/* 167 */         String str2 = str.substring(str1.length());
/* 168 */         return removeExtension(str2).replace(File.separatorChar, '.');
/*     */       }
/*     */     }
/* 171 */     return null;
/*     */   }
/*     */
/*     */
/*     */   public boolean isNameCompatible(String paramString, Kind paramKind) {
/* 176 */     paramString.getClass();
/*     */
/* 178 */     if (paramKind == Kind.OTHER && getKind() != paramKind) {
/* 179 */       return false;
/*     */     }
/* 181 */     String str = paramString + paramKind.extension;
/* 182 */     if (this.name.equals(str)) {
/* 183 */       return true;
/*     */     }
/* 185 */     if (isMacOS && Normalizer.isNormalized(this.name, Normalizer.Form.NFD) &&
/* 186 */       Normalizer.isNormalized(str, Normalizer.Form.NFC)) {
/*     */
/*     */
/*     */
/* 190 */       String str1 = Normalizer.normalize(this.name, Normalizer.Form.NFC);
/* 191 */       if (str1.equals(str)) {
/* 192 */         this.name = str1;
/* 193 */         return true;
/*     */       }
/*     */     }
/*     */
/* 197 */     if (this.name.equalsIgnoreCase(str)) {
/*     */
/*     */       try {
/* 200 */         return this.file.getCanonicalFile().getName().equals(str);
/* 201 */       } catch (IOException iOException) {}
/*     */     }
/*     */
/* 204 */     return false;
/*     */   }
/*     */
/*     */   private void ensureParentDirectoriesExist() throws IOException {
/* 208 */     if (!this.hasParents) {
/* 209 */       File file = this.file.getParentFile();
/* 210 */       if (file != null && !file.exists() &&
/* 211 */         !file.mkdirs() && (
/* 212 */         !file.exists() || !file.isDirectory())) {
/* 213 */         throw new IOException("could not create parent directories");
/*     */       }
/*     */
/*     */
/* 217 */       this.hasParents = true;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean equals(Object paramObject) {
/* 228 */     if (this == paramObject) {
/* 229 */       return true;
/*     */     }
/* 231 */     if (!(paramObject instanceof RegularFileObject)) {
/* 232 */       return false;
/*     */     }
/* 234 */     RegularFileObject regularFileObject = (RegularFileObject)paramObject;
/* 235 */     return getAbsoluteFile().equals(regularFileObject.getAbsoluteFile());
/*     */   }
/*     */
/*     */
/*     */   public int hashCode() {
/* 240 */     return getAbsoluteFile().hashCode();
/*     */   }
/*     */
/*     */   private File getAbsoluteFile() {
/* 244 */     File file = (this.absFileRef == null) ? null : this.absFileRef.get();
/* 245 */     if (file == null) {
/* 246 */       file = this.file.getAbsoluteFile();
/* 247 */       this.absFileRef = new SoftReference<>(file);
/*     */     }
/* 249 */     return file;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\file\RegularFileObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
