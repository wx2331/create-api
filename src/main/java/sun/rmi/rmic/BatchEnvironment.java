/*     */ package sun.rmi.rmic;
/*     */
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import sun.tools.java.ClassPath;
/*     */ import sun.tools.javac.BatchEnvironment;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class BatchEnvironment
/*     */   extends BatchEnvironment
/*     */ {
/*     */   private Main main;
/*     */   private Vector<File> generatedFiles;
/*     */
/*     */   public static ClassPath createClassPath(String paramString) {
/*  73 */     ClassPath[] arrayOfClassPath = classPaths(null, paramString, null, null);
/*  74 */     return arrayOfClassPath[1];
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static ClassPath createClassPath(String paramString1, String paramString2, String paramString3) {
/*  96 */     Path path = new Path();
/*     */
/*  98 */     if (paramString2 == null) {
/*  99 */       paramString2 = System.getProperty("sun.boot.class.path");
/*     */     }
/* 101 */     if (paramString2 != null) {
/* 102 */       path.addFiles(paramString2);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/* 109 */     path.expandJarClassPaths(true);
/*     */
/* 111 */     if (paramString3 == null) {
/* 112 */       paramString3 = System.getProperty("java.ext.dirs");
/*     */     }
/* 114 */     if (paramString3 != null) {
/* 115 */       path.addDirectories(paramString3);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/* 122 */     path.emptyPathDefault(".");
/*     */
/* 124 */     if (paramString1 == null) {
/*     */
/*     */
/*     */
/* 128 */       paramString1 = System.getProperty("env.class.path");
/* 129 */       if (paramString1 == null) {
/* 130 */         paramString1 = ".";
/*     */       }
/*     */     }
/* 133 */     path.addFiles(paramString1);
/*     */
/* 135 */     return new ClassPath((String[])path.toArray((Object[])new String[path.size()]));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public BatchEnvironment(OutputStream paramOutputStream, ClassPath paramClassPath, Main paramMain) {
/* 143 */     super(paramOutputStream, new ClassPath(""), paramClassPath);
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 163 */     this.generatedFiles = new Vector<>();
/*     */     this.main = paramMain;
/*     */   }
/*     */
/*     */
/*     */
/*     */   public void addGeneratedFile(File paramFile) {
/* 170 */     this.generatedFiles.addElement(paramFile);
/*     */   }
/*     */   public Main getMain() {
/*     */     return this.main;
/*     */   }
/*     */   public ClassPath getClassPath() {
/*     */     return this.binaryPath;
/*     */   }
/*     */   public void deleteGeneratedFiles() {
/* 179 */     synchronized (this.generatedFiles) {
/* 180 */       Enumeration<File> enumeration = this.generatedFiles.elements();
/* 181 */       while (enumeration.hasMoreElements()) {
/* 182 */         File file = enumeration.nextElement();
/* 183 */         file.delete();
/*     */       }
/* 185 */       this.generatedFiles.removeAllElements();
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void shutdown() {
/* 193 */     this.main = null;
/* 194 */     this.generatedFiles = null;
/* 195 */     super.shutdown();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public String errorString(String paramString, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 207 */     if (paramString.startsWith("rmic.") || paramString.startsWith("warn.rmic.")) {
/* 208 */       String str = Main.getText(paramString, (paramObject1 != null) ? paramObject1
/* 209 */           .toString() : null, (paramObject2 != null) ? paramObject2
/* 210 */           .toString() : null, (paramObject3 != null) ? paramObject3
/* 211 */           .toString() : null);
/*     */
/* 213 */       if (paramString.startsWith("warn.")) {
/* 214 */         str = "warning: " + str;
/*     */       }
/* 216 */       return str;
/*     */     }
/* 218 */     return super.errorString(paramString, paramObject1, paramObject2, paramObject3);
/*     */   }
/*     */
/*     */
/*     */   public void reset() {}
/*     */
/*     */
/*     */   private static class Path
/*     */     extends LinkedHashSet<String>
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */
/*     */     private static final boolean warn = false;
/*     */
/*     */
/*     */     private static class PathIterator
/*     */       implements Collection<String>
/*     */     {
/* 236 */       private int pos = 0;
/*     */       private final String path;
/*     */       private final String emptyPathDefault;
/*     */
/*     */       public PathIterator(String param2String1, String param2String2) {
/* 241 */         this.path = param2String1;
/* 242 */         this.emptyPathDefault = param2String2;
/*     */       } public PathIterator(String param2String) {
/* 244 */         this(param2String, null);
/*     */       } public Iterator<String> iterator() {
/* 246 */         return new Iterator<String>() {
/*     */             public boolean hasNext() {
/* 248 */               return (PathIterator.this.pos <= PathIterator.this.path.length());
/*     */             }
/*     */             public String next() {
/* 251 */               int i = PathIterator.this.pos;
/* 252 */               int j = PathIterator.this.path.indexOf(File.pathSeparator, i);
/* 253 */               if (j == -1)
/* 254 */                 j = PathIterator.this.path.length();
/* 255 */               PathIterator.this.pos = j + 1;
/*     */
/* 257 */               if (i == j && PathIterator.this.emptyPathDefault != null) {
/* 258 */                 return PathIterator.this.emptyPathDefault;
/*     */               }
/* 260 */               return PathIterator.this.path.substring(i, j);
/*     */             }
/*     */             public void remove() {
/* 263 */               throw new UnsupportedOperationException();
/*     */             }
/*     */           };
/*     */       }
/*     */
/*     */
/*     */       public int size() {
/* 270 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public boolean isEmpty() {
/* 273 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public boolean contains(Object param2Object) {
/* 276 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public Object[] toArray() {
/* 279 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public <T> T[] toArray(T[] param2ArrayOfT) {
/* 282 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public boolean add(String param2String) {
/* 285 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public boolean remove(Object param2Object) {
/* 288 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public boolean containsAll(Collection<?> param2Collection) {
/* 291 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public boolean addAll(Collection<? extends String> param2Collection) {
/* 294 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public boolean removeAll(Collection<?> param2Collection) {
/* 297 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public boolean retainAll(Collection<?> param2Collection) {
/* 300 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public void clear() {
/* 303 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public boolean equals(Object param2Object) {
/* 306 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public int hashCode() {
/* 309 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     private static boolean isZip(String param1String) {
/* 315 */       return (new File(param1String)).isFile();
/*     */     }
/*     */
/*     */     private boolean expandJarClassPaths = false;
/*     */
/*     */     public Path expandJarClassPaths(boolean param1Boolean) {
/* 321 */       this.expandJarClassPaths = param1Boolean;
/* 322 */       return this;
/*     */     }
/*     */
/*     */
/* 326 */     private String emptyPathDefault = null;
/*     */
/*     */     public Path emptyPathDefault(String param1String) {
/* 329 */       this.emptyPathDefault = param1String;
/* 330 */       return this;
/*     */     }
/*     */
/*     */
/*     */
/*     */     public Path addDirectories(String param1String, boolean param1Boolean) {
/* 336 */       if (param1String != null)
/* 337 */         for (String str : new PathIterator(param1String))
/* 338 */           addDirectory(str, param1Boolean);
/* 339 */       return this;
/*     */     }
/*     */
/*     */     public Path addDirectories(String param1String) {
/* 343 */       return addDirectories(param1String, false);
/*     */     }
/*     */
/*     */     private void addDirectory(String param1String, boolean param1Boolean) {
/* 347 */       if (!(new File(param1String)).isDirectory()) {
/*     */         return;
/*     */       }
/*     */
/*     */
/*     */
/*     */
/* 354 */       for (String str1 : (new File(param1String)).list()) {
/* 355 */         String str2 = str1.toLowerCase();
/* 356 */         if (str2.endsWith(".jar") || str2
/* 357 */           .endsWith(".zip"))
/* 358 */           addFile(param1String + File.separator + str1, param1Boolean);
/*     */       }
/*     */     }
/*     */
/*     */     public Path addFiles(String param1String, boolean param1Boolean) {
/* 363 */       if (param1String != null)
/* 364 */         for (String str : new PathIterator(param1String, this.emptyPathDefault))
/* 365 */           addFile(str, param1Boolean);
/* 366 */       return this;
/*     */     }
/*     */
/*     */     public Path addFiles(String param1String) {
/* 370 */       return addFiles(param1String, false);
/*     */     }
/*     */
/*     */     private void addFile(String param1String, boolean param1Boolean) {
/* 374 */       if (contains(param1String)) {
/*     */         return;
/*     */       }
/*     */
/*     */
/* 379 */       File file = new File(param1String);
/* 380 */       if (!file.exists())
/*     */       {
/* 382 */         if (param1Boolean) {
/*     */           return;
/*     */         }
/*     */       }
/*     */
/*     */
/* 388 */       if (file.isFile()) {
/*     */
/* 390 */         String str = param1String.toLowerCase();
/* 391 */         if (!str.endsWith(".zip") &&
/* 392 */           !str.endsWith(".jar")) {
/*     */           return;
/*     */         }
/*     */       }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 404 */       add((E)param1String);
/* 405 */       if (this.expandJarClassPaths && isZip(param1String)) {
/* 406 */         addJarClassPath(param1String, param1Boolean);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     private void addJarClassPath(String param1String, boolean param1Boolean) {
/*     */       try {
/* 415 */         String str = (new File(param1String)).getParent();
/* 416 */         JarFile jarFile = new JarFile(param1String);
/*     */
/*     */         try {
/* 419 */           Manifest manifest = jarFile.getManifest();
/* 420 */           if (manifest == null)
/*     */             return;
/* 422 */           Attributes attributes = manifest.getMainAttributes();
/* 423 */           if (attributes == null)
/*     */             return;
/* 425 */           String str1 = attributes.getValue(Attributes.Name.CLASS_PATH);
/* 426 */           if (str1 == null)
/*     */             return;
/* 428 */           StringTokenizer stringTokenizer = new StringTokenizer(str1);
/* 429 */           while (stringTokenizer.hasMoreTokens()) {
/* 430 */             String str2 = stringTokenizer.nextToken();
/* 431 */             if (str != null)
/* 432 */               str2 = (new File(str, str2)).getCanonicalPath();
/* 433 */             addFile(str2, param1Boolean);
/*     */           }
/*     */         } finally {
/* 436 */           jarFile.close();
/*     */         }
/* 438 */       } catch (IOException iOException) {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\BatchEnvironment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
