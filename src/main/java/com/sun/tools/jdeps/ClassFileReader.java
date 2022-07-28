/*     */ package com.sun.tools.jdeps;
/*     */
/*     */ import com.sun.tools.classfile.ClassFile;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.Dependencies;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class ClassFileReader
/*     */ {
/*     */   protected final Path path;
/*     */   protected final String baseFileName;
/*     */
/*     */   public static ClassFileReader newInstance(Path paramPath) throws IOException {
/*  51 */     if (!Files.exists(paramPath, new java.nio.file.LinkOption[0])) {
/*  52 */       throw new FileNotFoundException(paramPath.toString());
/*     */     }
/*     */
/*  55 */     if (Files.isDirectory(paramPath, new java.nio.file.LinkOption[0]))
/*  56 */       return new DirectoryReader(paramPath);
/*  57 */     if (paramPath.getFileName().toString().endsWith(".jar")) {
/*  58 */       return new JarFileReader(paramPath);
/*     */     }
/*  60 */     return new ClassFileReader(paramPath);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static ClassFileReader newInstance(Path paramPath, JarFile paramJarFile) throws IOException {
/*  68 */     return new JarFileReader(paramPath, paramJarFile);
/*     */   }
/*     */
/*     */
/*     */
/*  73 */   protected final List<String> skippedEntries = new ArrayList<>();
/*     */   protected ClassFileReader(Path paramPath) {
/*  75 */     this.path = paramPath;
/*  76 */     this
/*     */
/*  78 */       .baseFileName = (paramPath.getFileName() != null) ? paramPath.getFileName().toString() : paramPath.toString();
/*     */   }
/*     */
/*     */   public String getFileName() {
/*  82 */     return this.baseFileName;
/*     */   }
/*     */
/*     */   public List<String> skippedEntries() {
/*  86 */     return this.skippedEntries;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public ClassFile getClassFile(String paramString) throws IOException {
/*  94 */     if (paramString.indexOf('.') > 0) {
/*  95 */       int i = paramString.lastIndexOf('.');
/*  96 */       String str = paramString.replace('.', File.separatorChar) + ".class";
/*  97 */       if (!this.baseFileName.equals(str)) { if (this.baseFileName
/*  98 */           .equals(str.substring(0, i) + "$" + str
/*  99 */             .substring(i + 1, str.length())))
/* 100 */           return readClassFile(this.path);  } else { return readClassFile(this.path); }
/*     */
/*     */
/* 103 */     } else if (this.baseFileName.equals(paramString.replace('/', File.separatorChar) + ".class")) {
/* 104 */       return readClassFile(this.path);
/*     */     }
/*     */
/* 107 */     return null;
/*     */   }
/*     */
/*     */   public Iterable<ClassFile> getClassFiles() throws IOException {
/* 111 */     return new Iterable<ClassFile>() {
/*     */         public Iterator<ClassFile> iterator() {
/* 113 */           return new FileIterator();
/*     */         }
/*     */       };
/*     */   }
/*     */
/*     */   protected ClassFile readClassFile(Path paramPath) throws IOException {
/* 119 */     InputStream inputStream = null;
/*     */     try {
/* 121 */       inputStream = Files.newInputStream(paramPath, new java.nio.file.OpenOption[0]);
/* 122 */       return ClassFile.read(inputStream);
/* 123 */     } catch (ConstantPoolException constantPoolException) {
/* 124 */       throw new Dependencies.ClassFileError(constantPoolException);
/*     */     } finally {
/* 126 */       if (inputStream != null) {
/* 127 */         inputStream.close();
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   class FileIterator
/*     */     implements Iterator<ClassFile>
/*     */   {
/* 135 */     int count = 0;
/*     */
/*     */     public boolean hasNext() {
/* 138 */       return (this.count == 0 && ClassFileReader.this.baseFileName.endsWith(".class"));
/*     */     }
/*     */
/*     */     public ClassFile next() {
/* 142 */       if (!hasNext()) {
/* 143 */         throw new NoSuchElementException();
/*     */       }
/*     */       try {
/* 146 */         ClassFile classFile = ClassFileReader.this.readClassFile(ClassFileReader.this.path);
/* 147 */         this.count++;
/* 148 */         return classFile;
/* 149 */       } catch (IOException iOException) {
/* 150 */         throw new Dependencies.ClassFileError(iOException);
/*     */       }
/*     */     }
/*     */
/*     */     public void remove() {
/* 155 */       throw new UnsupportedOperationException("Not supported yet.");
/*     */     } }
/*     */
/*     */   public boolean isMultiReleaseJar() throws IOException {
/* 159 */     return false;
/*     */   }
/*     */   public String toString() {
/* 162 */     return this.path.toString();
/*     */   }
/*     */
/*     */   private static class DirectoryReader extends ClassFileReader {
/*     */     DirectoryReader(Path param1Path) throws IOException {
/* 167 */       super(param1Path);
/*     */     }
/*     */
/*     */     public ClassFile getClassFile(String param1String) throws IOException {
/* 171 */       if (param1String.indexOf('.') > 0) {
/* 172 */         int i = param1String.lastIndexOf('.');
/* 173 */         String str = param1String.replace('.', File.separatorChar) + ".class";
/* 174 */         Path path = this.path.resolve(str);
/* 175 */         if (!Files.exists(path, new java.nio.file.LinkOption[0])) {
/* 176 */           path = this.path.resolve(str.substring(0, i) + "$" + str
/* 177 */               .substring(i + 1, str.length()));
/*     */         }
/* 179 */         if (Files.exists(path, new java.nio.file.LinkOption[0])) {
/* 180 */           return readClassFile(path);
/*     */         }
/*     */       } else {
/* 183 */         Path path = this.path.resolve(param1String + ".class");
/* 184 */         if (Files.exists(path, new java.nio.file.LinkOption[0])) {
/* 185 */           return readClassFile(path);
/*     */         }
/*     */       }
/* 188 */       return null;
/*     */     }
/*     */
/*     */     public Iterable<ClassFile> getClassFiles() throws IOException {
/* 192 */       final DirectoryIterator iter = new DirectoryIterator();
/* 193 */       return new Iterable<ClassFile>() {
/*     */           public Iterator<ClassFile> iterator() {
/* 195 */             return iter;
/*     */           }
/*     */         };
/*     */     }
/*     */
/*     */     private List<Path> walkTree(Path param1Path) throws IOException {
/* 201 */       final ArrayList<Path> files = new ArrayList();
/* 202 */       Files.walkFileTree(param1Path, new SimpleFileVisitor<Path>()
/*     */           {
/*     */             public FileVisitResult visitFile(Path param2Path, BasicFileAttributes param2BasicFileAttributes) throws IOException {
/* 205 */               if (param2Path.getFileName().toString().endsWith(".class")) {
/* 206 */                 files.add(param2Path);
/*     */               }
/* 208 */               return FileVisitResult.CONTINUE;
/*     */             }
/*     */           });
/* 211 */       return arrayList;
/*     */     }
/*     */
/*     */     class DirectoryIterator implements Iterator<ClassFile> {
/*     */       private List<Path> entries;
/* 216 */       private int index = 0;
/*     */       DirectoryIterator() throws IOException {
/* 218 */         this.entries = DirectoryReader.this.walkTree(DirectoryReader.this.path);
/* 219 */         this.index = 0;
/*     */       }
/*     */
/*     */       public boolean hasNext() {
/* 223 */         return (this.index != this.entries.size());
/*     */       }
/*     */
/*     */       public ClassFile next() {
/* 227 */         if (!hasNext()) {
/* 228 */           throw new NoSuchElementException();
/*     */         }
/* 230 */         Path path = this.entries.get(this.index++);
/*     */         try {
/* 232 */           return DirectoryReader.this.readClassFile(path);
/* 233 */         } catch (IOException iOException) {
/* 234 */           throw new Dependencies.ClassFileError(iOException);
/*     */         }
/*     */       }
/*     */
/*     */       public void remove() {
/* 239 */         throw new UnsupportedOperationException("Not supported yet.");
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   static class JarFileReader extends ClassFileReader { private final JarFile jarfile;
/*     */
/*     */     JarFileReader(Path param1Path) throws IOException {
/* 247 */       this(param1Path, new JarFile(param1Path.toFile(), false));
/*     */     }
/*     */
/*     */     JarFileReader(Path param1Path, JarFile param1JarFile) throws IOException {
/* 251 */       super(param1Path);
/* 252 */       this.jarfile = param1JarFile;
/*     */     }
/*     */
/*     */     public ClassFile getClassFile(String param1String) throws IOException {
/* 256 */       if (param1String.indexOf('.') > 0) {
/* 257 */         int i = param1String.lastIndexOf('.');
/* 258 */         String str = param1String.replace('.', '/') + ".class";
/* 259 */         JarEntry jarEntry = this.jarfile.getJarEntry(str);
/* 260 */         if (jarEntry == null) {
/* 261 */           jarEntry = this.jarfile.getJarEntry(str.substring(0, i) + "$" + str
/* 262 */               .substring(i + 1, str.length()));
/*     */         }
/* 264 */         if (jarEntry != null) {
/* 265 */           return readClassFile(this.jarfile, jarEntry);
/*     */         }
/*     */       } else {
/* 268 */         JarEntry jarEntry = this.jarfile.getJarEntry(param1String + ".class");
/* 269 */         if (jarEntry != null) {
/* 270 */           return readClassFile(this.jarfile, jarEntry);
/*     */         }
/*     */       }
/* 273 */       return null;
/*     */     }
/*     */
/*     */     protected ClassFile readClassFile(JarFile param1JarFile, JarEntry param1JarEntry) throws IOException {
/* 277 */       InputStream inputStream = null;
/*     */       try {
/* 279 */         inputStream = param1JarFile.getInputStream(param1JarEntry);
/* 280 */         return ClassFile.read(inputStream);
/* 281 */       } catch (ConstantPoolException constantPoolException) {
/* 282 */         throw new Dependencies.ClassFileError(constantPoolException);
/*     */       } finally {
/* 284 */         if (inputStream != null)
/* 285 */           inputStream.close();
/*     */       }
/*     */     }
/*     */
/*     */     public Iterable<ClassFile> getClassFiles() throws IOException {
/* 290 */       final JarFileIterator iter = new JarFileIterator(this, this.jarfile);
/* 291 */       return new Iterable<ClassFile>() {
/*     */           public Iterator<ClassFile> iterator() {
/* 293 */             return iter;
/*     */           }
/*     */         };
/*     */     }
/*     */
/*     */
/*     */     public boolean isMultiReleaseJar() throws IOException {
/* 300 */       Manifest manifest = this.jarfile.getManifest();
/* 301 */       if (manifest != null) {
/* 302 */         Attributes attributes = manifest.getMainAttributes();
/* 303 */         return "true".equalsIgnoreCase(attributes.getValue("Multi-Release"));
/*     */       }
/* 305 */       return false;
/*     */     } }
/*     */
/*     */   class JarFileIterator implements Iterator<ClassFile> {
/*     */     protected final JarFileReader reader;
/*     */     protected Enumeration<JarEntry> entries;
/*     */     protected JarFile jf;
/*     */     protected JarEntry nextEntry;
/*     */     protected ClassFile cf;
/*     */
/*     */     JarFileIterator(JarFileReader param1JarFileReader) {
/* 316 */       this(param1JarFileReader, null);
/*     */     }
/*     */     JarFileIterator(JarFileReader param1JarFileReader, JarFile param1JarFile) {
/* 319 */       this.reader = param1JarFileReader;
/* 320 */       setJarFile(param1JarFile);
/*     */     }
/*     */
/*     */     void setJarFile(JarFile param1JarFile) {
/* 324 */       if (param1JarFile == null)
/*     */         return;
/* 326 */       this.jf = param1JarFile;
/* 327 */       this.entries = this.jf.entries();
/* 328 */       this.nextEntry = nextEntry();
/*     */     }
/*     */
/*     */     public boolean hasNext() {
/* 332 */       if (this.nextEntry != null && this.cf != null) {
/* 333 */         return true;
/*     */       }
/* 335 */       while (this.nextEntry != null) {
/*     */         try {
/* 337 */           this.cf = this.reader.readClassFile(this.jf, this.nextEntry);
/* 338 */           return true;
/* 339 */         } catch (Dependencies.ClassFileError|IOException classFileError) {
/* 340 */           ClassFileReader.this.skippedEntries.add(this.nextEntry.getName());
/*     */
/* 342 */           this.nextEntry = nextEntry();
/*     */         }
/* 344 */       }  return false;
/*     */     }
/*     */
/*     */     public ClassFile next() {
/* 348 */       if (!hasNext()) {
/* 349 */         throw new NoSuchElementException();
/*     */       }
/* 351 */       ClassFile classFile = this.cf;
/* 352 */       this.cf = null;
/* 353 */       this.nextEntry = nextEntry();
/* 354 */       return classFile;
/*     */     }
/*     */
/*     */     protected JarEntry nextEntry() {
/* 358 */       while (this.entries.hasMoreElements()) {
/* 359 */         JarEntry jarEntry = this.entries.nextElement();
/* 360 */         String str = jarEntry.getName();
/* 361 */         if (str.endsWith(".class")) {
/* 362 */           return jarEntry;
/*     */         }
/*     */       }
/* 365 */       return null;
/*     */     }
/*     */
/*     */     public void remove() {
/* 369 */       throw new UnsupportedOperationException("Not supported yet.");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdeps\ClassFileReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
