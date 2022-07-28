/*     */ package com.sun.tools.javac.nio;
/*     */
/*     */ import com.sun.tools.javac.main.Option;
/*     */ import com.sun.tools.javac.util.BaseFileManager;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.FileVisitOption;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.SourceVersion;
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class JavacPathFileManager
/*     */   extends BaseFileManager
/*     */   implements PathFileManager
/*     */ {
/*     */   protected FileSystem defaultFileSystem;
/*     */   private boolean inited;
/*     */   private Map<Location, PathsForLocation> pathsForLocation;
/*     */   private Map<Path, FileSystem> fileSystems;
/*     */
/*     */   public JavacPathFileManager(Context paramContext, boolean paramBoolean, Charset paramCharset) {
/* 113 */     super(paramCharset);
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 269 */     this.inited = false; if (paramBoolean)
/*     */       paramContext.put(JavaFileManager.class, this);  this.pathsForLocation = new HashMap<>(); this.fileSystems = new HashMap<>(); setContext(paramContext);
/*     */   } public void setContext(Context paramContext) { super.setContext(paramContext); } public FileSystem getDefaultFileSystem() { if (this.defaultFileSystem == null)
/*     */       this.defaultFileSystem = FileSystems.getDefault();  return this.defaultFileSystem; }
/*     */   public void setDefaultFileSystem(FileSystem paramFileSystem) { this.defaultFileSystem = paramFileSystem; }
/*     */   public void flush() throws IOException { this.contentCache.clear(); }
/*     */   public void close() throws IOException { for (FileSystem fileSystem : this.fileSystems.values())
/*     */       fileSystem.close();  }
/*     */   public ClassLoader getClassLoader(Location paramLocation) { nullCheck(paramLocation); Iterable<? extends Path> iterable = getLocation(paramLocation); if (iterable == null)
/*     */       return null;  ListBuffer listBuffer = new ListBuffer(); for (Path path : iterable) { try { listBuffer.append(path.toUri().toURL()); }
/*     */       catch (MalformedURLException malformedURLException) { throw new AssertionError(malformedURLException); }
/*     */        }
/*     */      return getClassLoader((URL[])listBuffer.toArray((Object[])new URL[listBuffer.size()])); }
/*     */   public boolean isDefaultBootClassPath() { return this.locations.isDefaultBootClassPath(); }
/* 283 */   public Path getPath(FileObject paramFileObject) { nullCheck(paramFileObject);
/* 284 */     if (!(paramFileObject instanceof PathFileObject))
/* 285 */       throw new IllegalArgumentException();
/* 286 */     return ((PathFileObject)paramFileObject).getPath(); } public boolean hasLocation(Location paramLocation) { return (getLocation(paramLocation) != null); }
/*     */   public Iterable<? extends Path> getLocation(Location paramLocation) { nullCheck(paramLocation); lazyInitSearchPaths(); PathsForLocation pathsForLocation = this.pathsForLocation.get(paramLocation); if (pathsForLocation == null && !this.pathsForLocation.containsKey(paramLocation)) { setDefaultForLocation(paramLocation); pathsForLocation = this.pathsForLocation.get(paramLocation); }  return pathsForLocation; }
/*     */   private Path getOutputLocation(Location paramLocation) { Iterable<? extends Path> iterable = getLocation(paramLocation); return (iterable == null) ? null : iterable.iterator().next(); }
/*     */   public void setLocation(Location paramLocation, Iterable<? extends Path> paramIterable) throws IOException { nullCheck(paramLocation); lazyInitSearchPaths(); if (paramIterable == null) { setDefaultForLocation(paramLocation); } else { if (paramLocation.isOutputLocation()) checkOutputPath(paramIterable);  PathsForLocation pathsForLocation = new PathsForLocation(); for (Path path : paramIterable)
/*     */         pathsForLocation.add(path);  this.pathsForLocation.put(paramLocation, pathsForLocation); }  }
/* 291 */   public boolean isSameFile(FileObject paramFileObject1, FileObject paramFileObject2) { nullCheck(paramFileObject1);
/* 292 */     nullCheck(paramFileObject2);
/* 293 */     if (!(paramFileObject1 instanceof PathFileObject))
/* 294 */       throw new IllegalArgumentException("Not supported: " + paramFileObject1);
/* 295 */     if (!(paramFileObject2 instanceof PathFileObject))
/* 296 */       throw new IllegalArgumentException("Not supported: " + paramFileObject2);
/* 297 */     return ((PathFileObject)paramFileObject1).isSameFile((PathFileObject)paramFileObject2); }
/*     */   private void checkOutputPath(Iterable<? extends Path> paramIterable) throws IOException { Iterator<? extends Path> iterator = paramIterable.iterator();
/*     */     if (!iterator.hasNext())
/*     */       throw new IllegalArgumentException("empty path for directory");
/*     */     Path path = iterator.next();
/*     */     if (iterator.hasNext())
/*     */       throw new IllegalArgumentException("path too long for directory");
/*     */     if (!isDirectory(path))
/* 305 */       throw new IOException(path + ": not a directory");  } public Iterable<JavaFileObject> list(Location paramLocation, String paramString, Set<JavaFileObject.Kind> paramSet, boolean paramBoolean) throws IOException { nullCheck(paramString);
/* 306 */     nullCheck(paramSet);
/*     */
/* 308 */     Iterable<? extends Path> iterable = getLocation(paramLocation);
/* 309 */     if (iterable == null)
/* 310 */       return (Iterable<JavaFileObject>)List.nil();
/* 311 */     ListBuffer<JavaFileObject> listBuffer = new ListBuffer();
/*     */
/* 313 */     for (Path path : iterable) {
/* 314 */       list(path, paramString, paramSet, paramBoolean, listBuffer);
/*     */     }
/* 316 */     return (Iterable<JavaFileObject>)listBuffer.toList(); }
/*     */   private void setDefaultForLocation(Location paramLocation) { Collection<T> collection = null; if (paramLocation instanceof StandardLocation) { String str; switch ((StandardLocation)paramLocation) { case CLASS_PATH: collection = this.locations.userClassPath(); break;case PLATFORM_CLASS_PATH: collection = this.locations.bootClassPath(); break;case SOURCE_PATH: collection = this.locations.sourcePath(); break;case CLASS_OUTPUT: str = this.options.get(Option.D); collection = (str == null) ? null : Collections.<T>singleton((T)new File(str)); break;case SOURCE_OUTPUT: str = this.options.get(Option.S); collection = (str == null) ? null : Collections.<T>singleton((T)new File(str)); break; }  }  PathsForLocation pathsForLocation = new PathsForLocation(); if (collection != null)
/*     */       for (File file : collection)
/*     */         pathsForLocation.add(file.toPath());   if (!pathsForLocation.isEmpty())
/*     */       this.pathsForLocation.put(paramLocation, pathsForLocation);  }
/*     */   private void lazyInitSearchPaths() { if (!this.inited) { setDefaultForLocation(StandardLocation.PLATFORM_CLASS_PATH); setDefaultForLocation(StandardLocation.CLASS_PATH); setDefaultForLocation(StandardLocation.SOURCE_PATH); this.inited = true; }  } private static class PathsForLocation extends LinkedHashSet<Path> {
/* 322 */     private static final long serialVersionUID = 6788510222394486733L; private PathsForLocation() {} } private void list(Path paramPath, String paramString, final Set<JavaFileObject.Kind> kinds, boolean paramBoolean, final ListBuffer<JavaFileObject> results) throws IOException { final Path pathDir; if (!Files.exists(paramPath, new java.nio.file.LinkOption[0])) {
/*     */       return;
/*     */     }
/*     */
/* 326 */     if (isDirectory(paramPath)) {
/* 327 */       path1 = paramPath;
/*     */     } else {
/* 329 */       FileSystem fileSystem = getFileSystem(paramPath);
/* 330 */       if (fileSystem == null)
/*     */         return;
/* 332 */       path1 = fileSystem.getRootDirectories().iterator().next();
/*     */     }
/* 334 */     String str = paramPath.getFileSystem().getSeparator();
/*     */
/* 336 */     Path path2 = paramString.isEmpty() ? path1 : path1.resolve(paramString.replace(".", str));
/* 337 */     if (!Files.exists(path2, new java.nio.file.LinkOption[0])) {
/*     */       return;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 366 */     boolean bool = paramBoolean ? true : true;
/* 367 */     EnumSet<FileVisitOption> enumSet = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
/* 368 */     Files.walkFileTree(path2, enumSet, bool, new SimpleFileVisitor<Path>()
/*     */         {
/*     */           public FileVisitResult preVisitDirectory(Path param1Path, BasicFileAttributes param1BasicFileAttributes)
/*     */           {
/* 372 */             Path path = param1Path.getFileName();
/* 373 */             if (path == null || SourceVersion.isIdentifier(path.toString())) {
/* 374 */               return FileVisitResult.CONTINUE;
/*     */             }
/* 376 */             return FileVisitResult.SKIP_SUBTREE;
/*     */           }
/*     */
/*     */
/*     */           public FileVisitResult visitFile(Path param1Path, BasicFileAttributes param1BasicFileAttributes) {
/* 381 */             if (param1BasicFileAttributes.isRegularFile() && kinds.contains(BaseFileManager.getKind(param1Path.getFileName().toString()))) {
/*     */
/* 383 */               PathFileObject pathFileObject = PathFileObject.createDirectoryPathFileObject(JavacPathFileManager.this, param1Path, pathDir);
/*     */
/* 385 */               results.append(pathFileObject);
/*     */             }
/* 387 */             return FileVisitResult.CONTINUE;
/*     */           }
/*     */         }); }
/*     */
/*     */
/*     */
/*     */
/*     */   public Iterable<? extends JavaFileObject> getJavaFileObjectsFromPaths(Iterable<? extends Path> paramIterable) {
/*     */     ArrayList<PathFileObject> arrayList;
/* 396 */     if (paramIterable instanceof Collection) {
/* 397 */       arrayList = new ArrayList(((Collection)paramIterable).size());
/*     */     } else {
/* 399 */       arrayList = new ArrayList();
/* 400 */     }  for (Path path : paramIterable)
/* 401 */       arrayList.add(PathFileObject.createSimplePathFileObject(this, (Path)nullCheck(path)));
/* 402 */     return (Iterable)arrayList;
/*     */   }
/*     */
/*     */
/*     */   public Iterable<? extends JavaFileObject> getJavaFileObjects(Path... paramVarArgs) {
/* 407 */     return getJavaFileObjectsFromPaths(Arrays.asList((Object[])nullCheck(paramVarArgs)));
/*     */   }
/*     */
/*     */
/*     */
/*     */   public JavaFileObject getJavaFileForInput(Location paramLocation, String paramString, JavaFileObject.Kind paramKind) throws IOException {
/* 413 */     return getFileForInput(paramLocation, getRelativePath(paramString, paramKind));
/*     */   }
/*     */
/*     */
/*     */
/*     */   public FileObject getFileForInput(Location paramLocation, String paramString1, String paramString2) throws IOException {
/* 419 */     return getFileForInput(paramLocation, getRelativePath(paramString1, paramString2));
/*     */   }
/*     */
/*     */
/*     */   private JavaFileObject getFileForInput(Location paramLocation, String paramString) throws IOException {
/* 424 */     for (Path path : getLocation(paramLocation)) {
/* 425 */       if (isDirectory(path)) {
/* 426 */         Path path1 = resolve(path, paramString);
/* 427 */         if (Files.exists(path1, new java.nio.file.LinkOption[0]))
/* 428 */           return PathFileObject.createDirectoryPathFileObject(this, path1, path);  continue;
/*     */       }
/* 430 */       FileSystem fileSystem = getFileSystem(path);
/* 431 */       if (fileSystem != null) {
/* 432 */         Path path1 = getPath(fileSystem, paramString);
/* 433 */         if (Files.exists(path1, new java.nio.file.LinkOption[0])) {
/* 434 */           return PathFileObject.createJarPathFileObject(this, path1);
/*     */         }
/*     */       }
/*     */     }
/* 438 */     return null;
/*     */   }
/*     */
/*     */
/*     */
/*     */   public JavaFileObject getJavaFileForOutput(Location paramLocation, String paramString, JavaFileObject.Kind paramKind, FileObject paramFileObject) throws IOException {
/* 444 */     return getFileForOutput(paramLocation, getRelativePath(paramString, paramKind), paramFileObject);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public FileObject getFileForOutput(Location paramLocation, String paramString1, String paramString2, FileObject paramFileObject) throws IOException {
/* 451 */     return getFileForOutput(paramLocation, getRelativePath(paramString1, paramString2), paramFileObject);
/*     */   }
/*     */
/*     */
/*     */   private JavaFileObject getFileForOutput(Location paramLocation, String paramString, FileObject paramFileObject) {
/* 456 */     Path path1 = getOutputLocation(paramLocation);
/* 457 */     if (path1 == null) {
/* 458 */       if (paramLocation == StandardLocation.CLASS_OUTPUT) {
/* 459 */         Path path = null;
/* 460 */         if (paramFileObject != null && paramFileObject instanceof PathFileObject) {
/* 461 */           path = ((PathFileObject)paramFileObject).getPath().getParent();
/*     */         }
/* 463 */         return PathFileObject.createSiblingPathFileObject(this, path
/* 464 */             .resolve(getBaseName(paramString)), paramString);
/*     */       }
/* 466 */       if (paramLocation == StandardLocation.SOURCE_OUTPUT) {
/* 467 */         path1 = getOutputLocation(StandardLocation.CLASS_OUTPUT);
/*     */       }
/*     */     }
/*     */
/*     */
/* 472 */     if (path1 != null) {
/* 473 */       Path path = resolve(path1, paramString);
/* 474 */       return PathFileObject.createDirectoryPathFileObject(this, path, path1);
/*     */     }
/* 476 */     Path path2 = getPath(getDefaultFileSystem(), paramString);
/* 477 */     return PathFileObject.createSimplePathFileObject(this, path2);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public String inferBinaryName(Location paramLocation, JavaFileObject paramJavaFileObject) {
/* 484 */     nullCheck(paramJavaFileObject);
/*     */
/* 486 */     Iterable<? extends Path> iterable = getLocation(paramLocation);
/* 487 */     if (iterable == null) {
/* 488 */       return null;
/*     */     }
/*     */
/* 491 */     if (!(paramJavaFileObject instanceof PathFileObject)) {
/* 492 */       throw new IllegalArgumentException(paramJavaFileObject.getClass().getName());
/*     */     }
/* 494 */     return ((PathFileObject)paramJavaFileObject).inferBinaryName(iterable);
/*     */   }
/*     */
/*     */   private FileSystem getFileSystem(Path paramPath) throws IOException {
/* 498 */     FileSystem fileSystem = this.fileSystems.get(paramPath);
/* 499 */     if (fileSystem == null) {
/* 500 */       fileSystem = FileSystems.newFileSystem(paramPath, (ClassLoader)null);
/* 501 */       this.fileSystems.put(paramPath, fileSystem);
/*     */     }
/* 503 */     return fileSystem;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private static String getRelativePath(String paramString, JavaFileObject.Kind paramKind) {
/* 513 */     return paramString.replace(".", "/") + paramKind.extension;
/*     */   }
/*     */
/*     */   private static String getRelativePath(String paramString1, String paramString2) {
/* 517 */     return paramString1.isEmpty() ? paramString2 : (paramString1
/* 518 */       .replace(".", "/") + "/" + paramString2);
/*     */   }
/*     */
/*     */   private static String getBaseName(String paramString) {
/* 522 */     int i = paramString.lastIndexOf("/");
/* 523 */     return paramString.substring(i + 1);
/*     */   }
/*     */
/*     */   private static boolean isDirectory(Path paramPath) throws IOException {
/* 527 */     BasicFileAttributes basicFileAttributes = (BasicFileAttributes)Files.readAttributes(paramPath, (Class)BasicFileAttributes.class, new java.nio.file.LinkOption[0]);
/* 528 */     return basicFileAttributes.isDirectory();
/*     */   }
/*     */
/*     */   private static Path getPath(FileSystem paramFileSystem, String paramString) {
/* 532 */     return paramFileSystem.getPath(paramString.replace("/", paramFileSystem.getSeparator()), new String[0]);
/*     */   }
/*     */
/*     */   private static Path resolve(Path paramPath, String paramString) {
/* 536 */     FileSystem fileSystem = paramPath.getFileSystem();
/* 537 */     Path path = fileSystem.getPath(paramString.replace("/", fileSystem.getSeparator()), new String[0]);
/* 538 */     return paramPath.resolve(path);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\nio\JavacPathFileManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
