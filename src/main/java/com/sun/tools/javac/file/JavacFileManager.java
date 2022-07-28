/*     */ package com.sun.tools.javac.file;
/*     */
/*     */ import com.sun.tools.javac.util.BaseFileManager;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.zip.ZipFile;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.StandardJavaFileManager;
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
/*     */ public class JavacFileManager
/*     */   extends BaseFileManager
/*     */   implements StandardJavaFileManager
/*     */ {
/*     */   private FSInfo fsInfo;
/*     */   private boolean contextUseOptimizedZip;
/*     */   private ZipFileIndexCache zipFileIndexCache;
/*     */
/*     */   public static char[] toArray(CharBuffer paramCharBuffer) {
/*  78 */     if (paramCharBuffer.hasArray()) {
/*  79 */       return ((CharBuffer)paramCharBuffer.compact().flip()).array();
/*     */     }
/*  81 */     return paramCharBuffer.toString().toCharArray();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*  90 */   private final Set<JavaFileObject.Kind> sourceOrClass = EnumSet.of(JavaFileObject.Kind.SOURCE, JavaFileObject.Kind.CLASS);
/*     */   protected boolean mmappedIO;
/*     */   protected boolean symbolFileEnabled;
/*     */   protected SortFiles sortFiles;
/*     */
/*     */   protected enum SortFiles implements Comparator<File> {
/*  96 */     FORWARD {
/*     */       public int compare(File param2File1, File param2File2) {
/*  98 */         return param2File1.getName().compareTo(param2File2.getName());
/*     */       }
/*     */     },
/* 101 */     REVERSE {
/*     */       public int compare(File param2File1, File param2File2) {
/* 103 */         return -param2File1.getName().compareTo(param2File2.getName());
/*     */       }
/*     */     };
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static void preRegister(Context paramContext) {
/* 113 */     paramContext.put(JavaFileManager.class, new Context.Factory<JavaFileManager>() {
/*     */           public JavaFileManager make(Context param1Context) {
/* 115 */             return new JavacFileManager(param1Context, true, null);
/*     */           }
/*     */         });
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public JavacFileManager(Context paramContext, boolean paramBoolean, Charset paramCharset) {
/* 125 */     super(paramCharset);
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 448 */     this.archives = new HashMap<>(); if (paramBoolean) paramContext.put(JavaFileManager.class, this);  setContext(paramContext);
/*     */   }
/* 450 */   public void setContext(Context paramContext) { super.setContext(paramContext); this.fsInfo = FSInfo.instance(paramContext); this.contextUseOptimizedZip = this.options.getBoolean("useOptimizedZip", true); if (this.contextUseOptimizedZip) this.zipFileIndexCache = ZipFileIndexCache.getSharedInstance();  this.mmappedIO = this.options.isSet("mmappedIO"); this.symbolFileEnabled = !this.options.isSet("ignore.symbol.file"); String str = this.options.get("sortFiles"); if (str != null) this.sortFiles = str.equals("reverse") ? SortFiles.REVERSE : SortFiles.FORWARD;  } public void setSymbolFileEnabled(boolean paramBoolean) { this.symbolFileEnabled = paramBoolean; } public boolean isDefaultBootClassPath() { return this.locations.isDefaultBootClassPath(); } public JavaFileObject getFileForInput(String paramString) { return getRegularFile(new File(paramString)); } public JavaFileObject getRegularFile(File paramFile) { return new RegularFileObject(this, paramFile); } public JavaFileObject getFileForOutput(String paramString, JavaFileObject.Kind paramKind, JavaFileObject paramJavaFileObject) throws IOException { return getJavaFileForOutput(StandardLocation.CLASS_OUTPUT, paramString, paramKind, paramJavaFileObject); } public Iterable<? extends JavaFileObject> getJavaFileObjectsFromStrings(Iterable<String> paramIterable) { ListBuffer listBuffer = new ListBuffer(); for (String str : paramIterable) listBuffer.append(new File((String)nullCheck(str)));  return getJavaFileObjectsFromFiles((Iterable<? extends File>)listBuffer.toList()); } public Iterable<? extends JavaFileObject> getJavaFileObjects(String... paramVarArgs) { return getJavaFileObjectsFromStrings(Arrays.asList((Object[])nullCheck(paramVarArgs))); } private static boolean isValidName(String paramString) { for (String str : paramString.split("\\.", -1)) { if (!SourceVersion.isIdentifier(str)) return false;  }  return true; } private static void validateClassName(String paramString) { if (!isValidName(paramString)) throw new IllegalArgumentException("Invalid class name: " + paramString);  } private static final String[] symbolFileLocation = new String[] { "lib", "ct.sym" }; private static void validatePackageName(String paramString) { if (paramString.length() > 0 && !isValidName(paramString)) throw new IllegalArgumentException("Invalid packageName name: " + paramString);  } public static void testName(String paramString, boolean paramBoolean1, boolean paramBoolean2) { try { validatePackageName(paramString); if (!paramBoolean1) throw new AssertionError("Invalid package name accepted: " + paramString);  printAscii("Valid package name: \"%s\"", new Object[] { paramString }); } catch (IllegalArgumentException illegalArgumentException) { if (paramBoolean1) throw new AssertionError("Valid package name rejected: " + paramString);  printAscii("Invalid package name: \"%s\"", new Object[] { paramString }); }  try { validateClassName(paramString); if (!paramBoolean2) throw new AssertionError("Invalid class name accepted: " + paramString);  printAscii("Valid class name: \"%s\"", new Object[] { paramString }); } catch (IllegalArgumentException illegalArgumentException) { if (paramBoolean2) throw new AssertionError("Valid class name rejected: " + paramString);  printAscii("Invalid class name: \"%s\"", new Object[] { paramString }); }  } private static void printAscii(String paramString, Object... paramVarArgs) { String str; try { str = new String(String.format(null, paramString, paramVarArgs).getBytes("US-ASCII"), "US-ASCII"); } catch (UnsupportedEncodingException unsupportedEncodingException) { throw new AssertionError(unsupportedEncodingException); }  System.out.println(str); } private void listDirectory(File paramFile, RelativePath.RelativeDirectory paramRelativeDirectory, Set<JavaFileObject.Kind> paramSet, boolean paramBoolean, ListBuffer<JavaFileObject> paramListBuffer) { File file = paramRelativeDirectory.getFile(paramFile); if (!caseMapCheck(file, paramRelativeDirectory)) return;  File[] arrayOfFile = file.listFiles(); if (arrayOfFile == null) return;  if (this.sortFiles != null) Arrays.sort(arrayOfFile, this.sortFiles);  for (File file1 : arrayOfFile) { String str = file1.getName(); if (file1.isDirectory()) { if (paramBoolean && SourceVersion.isIdentifier(str)) listDirectory(paramFile, new RelativePath.RelativeDirectory(paramRelativeDirectory, str), paramSet, paramBoolean, paramListBuffer);  } else if (isValidFile(str, paramSet)) { RegularFileObject regularFileObject = new RegularFileObject(this, str, new File(file, str)); paramListBuffer.append(regularFileObject); }  }  } private void listArchive(Archive paramArchive, RelativePath.RelativeDirectory paramRelativeDirectory, Set<JavaFileObject.Kind> paramSet, boolean paramBoolean, ListBuffer<JavaFileObject> paramListBuffer) { List<String> list = paramArchive.getFiles(paramRelativeDirectory); if (list != null) for (; !list.isEmpty(); list = list.tail) { String str = (String)list.head; if (isValidFile(str, paramSet)) paramListBuffer.append(paramArchive.getFileObject(paramRelativeDirectory, str));  }   if (paramBoolean) for (RelativePath.RelativeDirectory relativeDirectory : paramArchive.getSubdirectories()) { if (paramRelativeDirectory.contains(relativeDirectory)) listArchive(paramArchive, relativeDirectory, paramSet, false, paramListBuffer);  }   } private void listContainer(File paramFile, RelativePath.RelativeDirectory paramRelativeDirectory, Set<JavaFileObject.Kind> paramSet, boolean paramBoolean, ListBuffer<JavaFileObject> paramListBuffer) { Archive archive = this.archives.get(paramFile); if (archive == null) { if (this.fsInfo.isDirectory(paramFile)) { listDirectory(paramFile, paramRelativeDirectory, paramSet, paramBoolean, paramListBuffer); return; }  try { archive = openArchive(paramFile); } catch (IOException iOException) { this.log.error("error.reading.file", new Object[] { paramFile, getMessage(iOException) }); return; }  }  listArchive(archive, paramRelativeDirectory, paramSet, paramBoolean, paramListBuffer); } private boolean isValidFile(String paramString, Set<JavaFileObject.Kind> paramSet) { JavaFileObject.Kind kind = getKind(paramString); return paramSet.contains(kind); } private static final boolean fileSystemIsCaseSensitive = (File.separatorChar == '/'); Map<File, Archive> archives; private boolean caseMapCheck(File paramFile, RelativePath paramRelativePath) { String str; if (fileSystemIsCaseSensitive) return true;  try { str = paramFile.getCanonicalPath(); } catch (IOException iOException) { return false; }  char[] arrayOfChar1 = str.toCharArray(); char[] arrayOfChar2 = paramRelativePath.path.toCharArray(); int i = arrayOfChar1.length - 1; int j = arrayOfChar2.length - 1; while (i >= 0 && j >= 0) { for (; i >= 0 && arrayOfChar1[i] == File.separatorChar; i--); for (; j >= 0 && arrayOfChar2[j] == '/'; j--); if (i >= 0 && j >= 0) { if (arrayOfChar1[i] != arrayOfChar2[j]) return false;  i--; j--; }  }  return (j < 0); } public class MissingArchive implements Archive {
/* 451 */     final File zipFileName; public MissingArchive(File param1File) { this.zipFileName = param1File; } public boolean contains(RelativePath param1RelativePath) { return false; } public void close() {} public JavaFileObject getFileObject(RelativePath.RelativeDirectory param1RelativeDirectory, String param1String) { return null; } public List<String> getFiles(RelativePath.RelativeDirectory param1RelativeDirectory) { return List.nil(); } public Set<RelativePath.RelativeDirectory> getSubdirectories() { return Collections.emptySet(); } public String toString() { return "MissingArchive[" + this.zipFileName + "]"; } } private static final RelativePath.RelativeDirectory symbolFilePrefix = new RelativePath.RelativeDirectory("META-INF/sym/rt.jar/");
/*     */
/*     */
/*     */
/*     */   private String defaultEncodingName;
/*     */
/*     */
/*     */
/*     */
/*     */   protected Archive openArchive(File paramFile) throws IOException {
/*     */     try {
/* 462 */       return openArchive(paramFile, this.contextUseOptimizedZip);
/* 463 */     } catch (IOException iOException) {
/* 464 */       if (iOException instanceof ZipFileIndex.ZipFormatException) {
/* 465 */         return openArchive(paramFile, false);
/*     */       }
/* 467 */       throw iOException;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   private Archive openArchive(File paramFile, boolean paramBoolean) throws IOException {
/*     */     MissingArchive missingArchive;
/* 475 */     File file = paramFile;
/* 476 */     if (this.symbolFileEnabled && this.locations.isDefaultBootClassPathRtJar(paramFile)) {
/* 477 */       File file1 = paramFile.getParentFile().getParentFile();
/* 478 */       if ((new File(file1.getName())).equals(new File("jre"))) {
/* 479 */         file1 = file1.getParentFile();
/*     */       }
/* 481 */       for (String str : symbolFileLocation) {
/* 482 */         file1 = new File(file1, str);
/*     */       }
/* 484 */       if (file1.exists()) {
/* 485 */         paramFile = file1;
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     try {
/* 491 */       ZipFile zipFile = null;
/*     */
/* 493 */       boolean bool = false;
/* 494 */       String str = null;
/*     */
/* 496 */       if (!paramBoolean) {
/* 497 */         zipFile = new ZipFile(paramFile);
/*     */       } else {
/* 499 */         bool = this.options.isSet("usezipindex");
/* 500 */         str = this.options.get("java.io.tmpdir");
/* 501 */         String str1 = this.options.get("cachezipindexdir");
/*     */
/* 503 */         if (str1 != null && str1.length() != 0) {
/* 504 */           if (str1.startsWith("\"")) {
/* 505 */             if (str1.endsWith("\"")) {
/* 506 */               str1 = str1.substring(1, str1.length() - 1);
/*     */             } else {
/*     */
/* 509 */               str1 = str1.substring(1);
/*     */             }
/*     */           }
/*     */
/* 513 */           File file1 = new File(str1);
/* 514 */           if (file1.exists() && file1.canWrite()) {
/* 515 */             str = str1;
/* 516 */             if (!str.endsWith("/") &&
/* 517 */               !str.endsWith(File.separator)) {
/* 518 */               str = str + File.separator;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */
/* 524 */       if (file == paramFile) {
/* 525 */         if (!paramBoolean) {
/* 526 */           ZipArchive zipArchive = new ZipArchive(this, zipFile);
/*     */         } else {
/*     */
/* 529 */           ZipFileIndexArchive zipFileIndexArchive = new ZipFileIndexArchive(this, this.zipFileIndexCache.getZipFileIndex(paramFile, null, bool, str, this.options
/*     */
/*     */
/*     */
/* 533 */                 .isSet("writezipindexfiles")));
/*     */         }
/*     */
/* 536 */       } else if (!paramBoolean) {
/* 537 */         SymbolArchive symbolArchive = new SymbolArchive(this, file, zipFile, symbolFilePrefix);
/*     */       } else {
/*     */
/* 540 */         ZipFileIndexArchive zipFileIndexArchive = new ZipFileIndexArchive(this, this.zipFileIndexCache.getZipFileIndex(paramFile, symbolFilePrefix, bool, str, this.options
/*     */
/*     */
/*     */
/* 544 */               .isSet("writezipindexfiles")));
/*     */       }
/*     */
/* 547 */     } catch (FileNotFoundException fileNotFoundException) {
/* 548 */       missingArchive = new MissingArchive(paramFile);
/* 549 */     } catch (ZipFormatException zipFormatException) {
/* 550 */       throw zipFormatException;
/* 551 */     } catch (IOException iOException) {
/* 552 */       if (paramFile.exists())
/* 553 */         this.log.error("error.reading.file", new Object[] { paramFile, getMessage(iOException) });
/* 554 */       missingArchive = new MissingArchive(paramFile);
/*     */     }
/*     */
/* 557 */     this.archives.put(file, missingArchive);
/* 558 */     return missingArchive;
/*     */   }
/*     */
/*     */
/*     */
/*     */   public void flush() {
/* 564 */     this.contentCache.clear();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void close() {
/* 571 */     for (Iterator<Archive> iterator = this.archives.values().iterator(); iterator.hasNext(); ) {
/* 572 */       Archive archive = iterator.next();
/* 573 */       iterator.remove();
/*     */       try {
/* 575 */         archive.close();
/* 576 */       } catch (IOException iOException) {}
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   private String getDefaultEncodingName() {
/* 583 */     if (this.defaultEncodingName == null) {
/* 584 */       this
/* 585 */         .defaultEncodingName = (new OutputStreamWriter(new ByteArrayOutputStream())).getEncoding();
/*     */     }
/* 587 */     return this.defaultEncodingName;
/*     */   }
/*     */
/*     */   public ClassLoader getClassLoader(Location paramLocation) {
/* 591 */     nullCheck(paramLocation);
/* 592 */     Iterable<? extends File> iterable = getLocation(paramLocation);
/* 593 */     if (iterable == null)
/* 594 */       return null;
/* 595 */     ListBuffer listBuffer = new ListBuffer();
/* 596 */     for (File file : iterable) {
/*     */       try {
/* 598 */         listBuffer.append(file.toURI().toURL());
/* 599 */       } catch (MalformedURLException malformedURLException) {
/* 600 */         throw new AssertionError(malformedURLException);
/*     */       }
/*     */     }
/*     */
/* 604 */     return getClassLoader((URL[])listBuffer.toArray((Object[])new URL[listBuffer.size()]));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Iterable<JavaFileObject> list(Location paramLocation, String paramString, Set<JavaFileObject.Kind> paramSet, boolean paramBoolean) throws IOException {
/* 614 */     nullCheck(paramString);
/* 615 */     nullCheck(paramSet);
/*     */
/* 617 */     Iterable<? extends File> iterable = getLocation(paramLocation);
/* 618 */     if (iterable == null)
/* 619 */       return (Iterable<JavaFileObject>)List.nil();
/* 620 */     RelativePath.RelativeDirectory relativeDirectory = RelativePath.RelativeDirectory.forPackage(paramString);
/* 621 */     ListBuffer<JavaFileObject> listBuffer = new ListBuffer();
/*     */
/* 623 */     for (File file : iterable)
/* 624 */       listContainer(file, relativeDirectory, paramSet, paramBoolean, listBuffer);
/* 625 */     return (Iterable<JavaFileObject>)listBuffer.toList();
/*     */   }
/*     */
/*     */   public String inferBinaryName(Location paramLocation, JavaFileObject paramJavaFileObject) {
/* 629 */     paramJavaFileObject.getClass();
/* 630 */     paramLocation.getClass();
/*     */
/* 632 */     Iterable<? extends File> iterable = getLocation(paramLocation);
/* 633 */     if (iterable == null) {
/* 634 */       return null;
/*     */     }
/*     */
/* 637 */     if (paramJavaFileObject instanceof BaseFileObject) {
/* 638 */       return ((BaseFileObject)paramJavaFileObject).inferBinaryName(iterable);
/*     */     }
/* 640 */     throw new IllegalArgumentException(paramJavaFileObject.getClass().getName());
/*     */   }
/*     */
/*     */   public boolean isSameFile(FileObject paramFileObject1, FileObject paramFileObject2) {
/* 644 */     nullCheck(paramFileObject1);
/* 645 */     nullCheck(paramFileObject2);
/* 646 */     if (!(paramFileObject1 instanceof BaseFileObject))
/* 647 */       throw new IllegalArgumentException("Not supported: " + paramFileObject1);
/* 648 */     if (!(paramFileObject2 instanceof BaseFileObject))
/* 649 */       throw new IllegalArgumentException("Not supported: " + paramFileObject2);
/* 650 */     return paramFileObject1.equals(paramFileObject2);
/*     */   }
/*     */
/*     */   public boolean hasLocation(Location paramLocation) {
/* 654 */     return (getLocation(paramLocation) != null);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public JavaFileObject getJavaFileForInput(Location paramLocation, String paramString, JavaFileObject.Kind paramKind) throws IOException {
/* 662 */     nullCheck(paramLocation);
/*     */
/* 664 */     nullCheck(paramString);
/* 665 */     nullCheck(paramKind);
/* 666 */     if (!this.sourceOrClass.contains(paramKind))
/* 667 */       throw new IllegalArgumentException("Invalid kind: " + paramKind);
/* 668 */     return getFileForInput(paramLocation, RelativePath.RelativeFile.forClass(paramString, paramKind));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public FileObject getFileForInput(Location paramLocation, String paramString1, String paramString2) throws IOException {
/* 676 */     nullCheck(paramLocation);
/*     */
/* 678 */     nullCheck(paramString1);
/* 679 */     if (!isRelativeUri(paramString2)) {
/* 680 */       throw new IllegalArgumentException("Invalid relative name: " + paramString2);
/*     */     }
/*     */
/* 683 */     RelativePath.RelativeFile relativeFile = (paramString1.length() == 0) ? new RelativePath.RelativeFile(paramString2) : new RelativePath.RelativeFile(RelativePath.RelativeDirectory.forPackage(paramString1), paramString2);
/* 684 */     return getFileForInput(paramLocation, relativeFile);
/*     */   }
/*     */
/*     */   private JavaFileObject getFileForInput(Location paramLocation, RelativePath.RelativeFile paramRelativeFile) throws IOException {
/* 688 */     Iterable<? extends File> iterable = getLocation(paramLocation);
/* 689 */     if (iterable == null) {
/* 690 */       return null;
/*     */     }
/* 692 */     for (File file : iterable) {
/* 693 */       Archive archive = this.archives.get(file);
/* 694 */       if (archive == null) {
/* 695 */         if (this.fsInfo.isDirectory(file)) {
/* 696 */           File file1 = paramRelativeFile.getFile(file);
/* 697 */           if (file1.exists()) {
/* 698 */             return new RegularFileObject(this, file1);
/*     */           }
/*     */           continue;
/*     */         }
/* 702 */         archive = openArchive(file);
/*     */       }
/*     */
/* 705 */       if (archive.contains(paramRelativeFile)) {
/* 706 */         return archive.getFileObject(paramRelativeFile.dirname(), paramRelativeFile.basename());
/*     */       }
/*     */     }
/* 709 */     return null;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public JavaFileObject getJavaFileForOutput(Location paramLocation, String paramString, JavaFileObject.Kind paramKind, FileObject paramFileObject) throws IOException {
/* 718 */     nullCheck(paramLocation);
/*     */
/* 720 */     nullCheck(paramString);
/* 721 */     nullCheck(paramKind);
/* 722 */     if (!this.sourceOrClass.contains(paramKind))
/* 723 */       throw new IllegalArgumentException("Invalid kind: " + paramKind);
/* 724 */     return getFileForOutput(paramLocation, RelativePath.RelativeFile.forClass(paramString, paramKind), paramFileObject);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public FileObject getFileForOutput(Location paramLocation, String paramString1, String paramString2, FileObject paramFileObject) throws IOException {
/* 733 */     nullCheck(paramLocation);
/*     */
/* 735 */     nullCheck(paramString1);
/* 736 */     if (!isRelativeUri(paramString2)) {
/* 737 */       throw new IllegalArgumentException("Invalid relative name: " + paramString2);
/*     */     }
/*     */
/* 740 */     RelativePath.RelativeFile relativeFile = (paramString1.length() == 0) ? new RelativePath.RelativeFile(paramString2) : new RelativePath.RelativeFile(RelativePath.RelativeDirectory.forPackage(paramString1), paramString2);
/* 741 */     return getFileForOutput(paramLocation, relativeFile, paramFileObject);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private JavaFileObject getFileForOutput(Location paramLocation, RelativePath.RelativeFile paramRelativeFile, FileObject paramFileObject) throws IOException {
/*     */     File file1;
/* 750 */     if (paramLocation == StandardLocation.CLASS_OUTPUT) {
/* 751 */       if (getClassOutDir() != null) {
/* 752 */         file1 = getClassOutDir();
/*     */       } else {
/* 754 */         File file = null;
/* 755 */         if (paramFileObject != null && paramFileObject instanceof RegularFileObject) {
/* 756 */           file = ((RegularFileObject)paramFileObject).file.getParentFile();
/*     */         }
/* 758 */         return new RegularFileObject(this, new File(file, paramRelativeFile.basename()));
/*     */       }
/* 760 */     } else if (paramLocation == StandardLocation.SOURCE_OUTPUT) {
/* 761 */       file1 = (getSourceOutDir() != null) ? getSourceOutDir() : getClassOutDir();
/*     */     } else {
/* 763 */       Collection<File> collection = this.locations.getLocation(paramLocation);
/* 764 */       file1 = null;
/* 765 */       Iterator<File> iterator = collection.iterator(); if (iterator.hasNext()) { File file = iterator.next();
/* 766 */         file1 = file; }
/*     */
/*     */     }
/*     */
/*     */
/* 771 */     File file2 = paramRelativeFile.getFile(file1);
/* 772 */     return new RegularFileObject(this, file2);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public Iterable<? extends JavaFileObject> getJavaFileObjectsFromFiles(Iterable<? extends File> paramIterable) {
/*     */     ArrayList<RegularFileObject> arrayList;
/* 780 */     if (paramIterable instanceof Collection) {
/* 781 */       arrayList = new ArrayList(((Collection)paramIterable).size());
/*     */     } else {
/* 783 */       arrayList = new ArrayList();
/* 784 */     }  for (File file : paramIterable)
/* 785 */       arrayList.add(new RegularFileObject(this, (File)nullCheck(file)));
/* 786 */     return (Iterable)arrayList;
/*     */   }
/*     */
/*     */   public Iterable<? extends JavaFileObject> getJavaFileObjects(File... paramVarArgs) {
/* 790 */     return getJavaFileObjectsFromFiles(Arrays.asList((Object[])nullCheck(paramVarArgs)));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void setLocation(Location paramLocation, Iterable<? extends File> paramIterable) throws IOException {
/* 797 */     nullCheck(paramLocation);
/* 798 */     this.locations.setLocation(paramLocation, paramIterable);
/*     */   }
/*     */
/*     */   public Iterable<? extends File> getLocation(Location paramLocation) {
/* 802 */     nullCheck(paramLocation);
/* 803 */     return this.locations.getLocation(paramLocation);
/*     */   }
/*     */
/*     */   private File getClassOutDir() {
/* 807 */     return this.locations.getOutputLocation(StandardLocation.CLASS_OUTPUT);
/*     */   }
/*     */
/*     */   private File getSourceOutDir() {
/* 811 */     return this.locations.getOutputLocation(StandardLocation.SOURCE_OUTPUT);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   protected static boolean isRelativeUri(URI paramURI) {
/* 822 */     if (paramURI.isAbsolute())
/* 823 */       return false;
/* 824 */     String str = paramURI.normalize().getPath();
/* 825 */     if (str.length() == 0)
/* 826 */       return false;
/* 827 */     if (!str.equals(paramURI.getPath()))
/* 828 */       return false;
/* 829 */     if (str.startsWith("/") || str.startsWith("./") || str.startsWith("../"))
/* 830 */       return false;
/* 831 */     return true;
/*     */   }
/*     */
/*     */
/*     */   protected static boolean isRelativeUri(String paramString) {
/*     */     try {
/* 837 */       return isRelativeUri(new URI(paramString));
/* 838 */     } catch (URISyntaxException uRISyntaxException) {
/* 839 */       return false;
/*     */     }
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
/*     */   public static String getRelativeName(File paramFile) {
/* 855 */     if (!paramFile.isAbsolute()) {
/* 856 */       String str = paramFile.getPath().replace(File.separatorChar, '/');
/* 857 */       if (isRelativeUri(str))
/* 858 */         return str;
/*     */     }
/* 860 */     throw new IllegalArgumentException("Invalid relative path: " + paramFile);
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
/*     */   public static String getMessage(IOException paramIOException) {
/* 873 */     String str = paramIOException.getLocalizedMessage();
/* 874 */     if (str != null)
/* 875 */       return str;
/* 876 */     str = paramIOException.getMessage();
/* 877 */     if (str != null)
/* 878 */       return str;
/* 879 */     return paramIOException.toString();
/*     */   }
/*     */
/*     */   public static interface Archive {
/*     */     void close() throws IOException;
/*     */
/*     */     boolean contains(RelativePath param1RelativePath);
/*     */
/*     */     JavaFileObject getFileObject(RelativePath.RelativeDirectory param1RelativeDirectory, String param1String);
/*     */
/*     */     List<String> getFiles(RelativePath.RelativeDirectory param1RelativeDirectory);
/*     */
/*     */     Set<RelativePath.RelativeDirectory> getSubdirectories();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\file\JavacFileManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
