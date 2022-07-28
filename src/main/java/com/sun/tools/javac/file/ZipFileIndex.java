/*      */ package com.sun.tools.javac.file;
/*      */
/*      */ import com.sun.tools.javac.util.List;
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.io.RandomAccessFile;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.zip.DataFormatException;
/*      */ import java.util.zip.Inflater;
/*      */ import java.util.zip.ZipException;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ public class ZipFileIndex
/*      */ {
/*   80 */   private static final String MIN_CHAR = String.valueOf(false);
/*   81 */   private static final String MAX_CHAR = String.valueOf('￿');
/*      */
/*      */
/*      */   public static final long NOT_MODIFIED = -9223372036854775808L;
/*      */
/*   86 */   private static final boolean NON_BATCH_MODE = (System.getProperty("nonBatchMode") != null);
/*      */
/*      */
/*   89 */   private Map<RelativePath.RelativeDirectory, DirectoryEntry> directories = Collections.emptyMap();
/*      */
/*   91 */   private Set<RelativePath.RelativeDirectory> allDirs = Collections.emptySet();
/*      */
/*      */   final File zipFile;
/*      */
/*      */   private Reference<File> absFileRef;
/*   96 */   long zipFileLastModified = Long.MIN_VALUE;
/*      */
/*      */   private RandomAccessFile zipRandomFile;
/*      */   private Entry[] entries;
/*      */   private boolean readFromIndex = false;
/*  101 */   private File zipIndexFile = null;
/*      */   private boolean triedToReadIndex = false;
/*      */   final RelativePath.RelativeDirectory symbolFilePrefix;
/*      */   private final int symbolFilePrefixLength;
/*      */   private boolean hasPopulatedData = false;
/*  106 */   long lastReferenceTimeStamp = Long.MIN_VALUE;
/*      */
/*      */   private final boolean usePreindexedCache;
/*      */
/*      */   private final String preindexedCacheLocation;
/*      */
/*      */   private boolean writeIndex = false;
/*  113 */   private Map<String, SoftReference<RelativePath.RelativeDirectory>> relativeDirectoryCache = new HashMap<>();
/*      */
/*      */   private SoftReference<Inflater> inflaterRef;
/*      */
/*      */   public synchronized boolean isOpen() {
/*  118 */     return (this.zipRandomFile != null);
/*      */   }
/*      */
/*      */
/*      */   ZipFileIndex(File paramFile, RelativePath.RelativeDirectory paramRelativeDirectory, boolean paramBoolean1, boolean paramBoolean2, String paramString) throws IOException {
/*  123 */     this.zipFile = paramFile;
/*  124 */     this.symbolFilePrefix = paramRelativeDirectory;
/*  125 */     this
/*  126 */       .symbolFilePrefixLength = (paramRelativeDirectory == null) ? 0 : (paramRelativeDirectory.getPath().getBytes("UTF-8")).length;
/*  127 */     this.writeIndex = paramBoolean1;
/*  128 */     this.usePreindexedCache = paramBoolean2;
/*  129 */     this.preindexedCacheLocation = paramString;
/*      */
/*  131 */     if (paramFile != null) {
/*  132 */       this.zipFileLastModified = paramFile.lastModified();
/*      */     }
/*      */
/*      */
/*  136 */     checkIndex();
/*      */   }
/*      */
/*      */
/*      */   public String toString() {
/*  141 */     return "ZipFileIndex[" + this.zipFile + "]";
/*      */   }
/*      */
/*      */
/*      */
/*      */   protected void finalize() throws Throwable {
/*  147 */     closeFile();
/*  148 */     super.finalize();
/*      */   }
/*      */
/*      */   private boolean isUpToDate() {
/*  152 */     if (this.zipFile != null && (!NON_BATCH_MODE || this.zipFileLastModified == this.zipFile
/*  153 */       .lastModified()) && this.hasPopulatedData)
/*      */     {
/*  155 */       return true;
/*      */     }
/*      */
/*  158 */     return false;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private void checkIndex() throws IOException {
/*  166 */     boolean bool = true;
/*  167 */     if (!isUpToDate()) {
/*  168 */       closeFile();
/*  169 */       bool = false;
/*      */     }
/*      */
/*  172 */     if (this.zipRandomFile != null || bool) {
/*  173 */       this.lastReferenceTimeStamp = System.currentTimeMillis();
/*      */
/*      */       return;
/*      */     }
/*  177 */     this.hasPopulatedData = true;
/*      */
/*  179 */     if (readIndex()) {
/*  180 */       this.lastReferenceTimeStamp = System.currentTimeMillis();
/*      */
/*      */       return;
/*      */     }
/*  184 */     this.directories = Collections.emptyMap();
/*  185 */     this.allDirs = Collections.emptySet();
/*      */
/*      */     try {
/*  188 */       openFile();
/*  189 */       long l = this.zipRandomFile.length();
/*  190 */       ZipDirectory zipDirectory = new ZipDirectory(this.zipRandomFile, 0L, l, this);
/*  191 */       zipDirectory.buildIndex();
/*      */     } finally {
/*  193 */       if (this.zipRandomFile != null) {
/*  194 */         closeFile();
/*      */       }
/*      */     }
/*      */
/*  198 */     this.lastReferenceTimeStamp = System.currentTimeMillis();
/*      */   }
/*      */
/*      */   private void openFile() throws FileNotFoundException {
/*  202 */     if (this.zipRandomFile == null && this.zipFile != null) {
/*  203 */       this.zipRandomFile = new RandomAccessFile(this.zipFile, "r");
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   private void cleanupState() {
/*  209 */     this.entries = Entry.EMPTY_ARRAY;
/*  210 */     this.directories = Collections.emptyMap();
/*  211 */     this.zipFileLastModified = Long.MIN_VALUE;
/*  212 */     this.allDirs = Collections.emptySet();
/*      */   }
/*      */
/*      */   public synchronized void close() {
/*  216 */     writeIndex();
/*  217 */     closeFile();
/*      */   }
/*      */
/*      */   private void closeFile() {
/*  221 */     if (this.zipRandomFile != null) {
/*      */       try {
/*  223 */         this.zipRandomFile.close();
/*  224 */       } catch (IOException iOException) {}
/*      */
/*  226 */       this.zipRandomFile = null;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   synchronized Entry getZipIndexEntry(RelativePath paramRelativePath) {
/*      */     try {
/*  235 */       checkIndex();
/*  236 */       DirectoryEntry directoryEntry = this.directories.get(paramRelativePath.dirname());
/*  237 */       String str = paramRelativePath.basename();
/*  238 */       return (directoryEntry == null) ? null : directoryEntry.getEntry(str);
/*      */     }
/*  240 */     catch (IOException iOException) {
/*  241 */       return null;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public synchronized List<String> getFiles(RelativePath.RelativeDirectory paramRelativeDirectory) {
/*      */     try {
/*  250 */       checkIndex();
/*      */
/*  252 */       DirectoryEntry directoryEntry = this.directories.get(paramRelativeDirectory);
/*  253 */       List<String> list = (directoryEntry == null) ? null : directoryEntry.getFiles();
/*      */
/*  255 */       if (list == null) {
/*  256 */         return List.nil();
/*      */       }
/*  258 */       return list;
/*      */     }
/*  260 */     catch (IOException iOException) {
/*  261 */       return List.nil();
/*      */     }
/*      */   }
/*      */
/*      */   public synchronized List<String> getDirectories(RelativePath.RelativeDirectory paramRelativeDirectory) {
/*      */     try {
/*  267 */       checkIndex();
/*      */
/*  269 */       DirectoryEntry directoryEntry = this.directories.get(paramRelativeDirectory);
/*  270 */       List list = (directoryEntry == null) ? null : directoryEntry.getDirectories();
/*      */
/*  272 */       if (list == null) {
/*  273 */         return (List<String>)List.nil();
/*      */       }
/*      */
/*  276 */       return (List<String>)list;
/*      */     }
/*  278 */     catch (IOException iOException) {
/*  279 */       return (List<String>)List.nil();
/*      */     }
/*      */   }
/*      */
/*      */   public synchronized Set<RelativePath.RelativeDirectory> getAllDirectories() {
/*      */     try {
/*  285 */       checkIndex();
/*  286 */       if (this.allDirs == Collections.EMPTY_SET) {
/*  287 */         this.allDirs = new LinkedHashSet<>(this.directories.keySet());
/*      */       }
/*      */
/*  290 */       return this.allDirs;
/*      */     }
/*  292 */     catch (IOException iOException) {
/*  293 */       return Collections.emptySet();
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public synchronized boolean contains(RelativePath paramRelativePath) {
/*      */     try {
/*  306 */       checkIndex();
/*  307 */       return (getZipIndexEntry(paramRelativePath) != null);
/*      */     }
/*  309 */     catch (IOException iOException) {
/*  310 */       return false;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public synchronized boolean isDirectory(RelativePath paramRelativePath) throws IOException {
/*  316 */     if (paramRelativePath.getPath().length() == 0) {
/*  317 */       this.lastReferenceTimeStamp = System.currentTimeMillis();
/*  318 */       return true;
/*      */     }
/*      */
/*  321 */     checkIndex();
/*  322 */     return (this.directories.get(paramRelativePath) != null);
/*      */   }
/*      */
/*      */   public synchronized long getLastModified(RelativePath.RelativeFile paramRelativeFile) throws IOException {
/*  326 */     Entry entry = getZipIndexEntry(paramRelativeFile);
/*  327 */     if (entry == null)
/*  328 */       throw new FileNotFoundException();
/*  329 */     return entry.getLastModified();
/*      */   }
/*      */
/*      */   public synchronized int length(RelativePath.RelativeFile paramRelativeFile) throws IOException {
/*  333 */     Entry entry = getZipIndexEntry(paramRelativeFile);
/*  334 */     if (entry == null) {
/*  335 */       throw new FileNotFoundException();
/*      */     }
/*  337 */     if (entry.isDir) {
/*  338 */       return 0;
/*      */     }
/*      */
/*  341 */     byte[] arrayOfByte = getHeader(entry);
/*      */
/*  343 */     if (get2ByteLittleEndian(arrayOfByte, 8) == 0) {
/*  344 */       return entry.compressedSize;
/*      */     }
/*  346 */     return entry.size;
/*      */   }
/*      */
/*      */
/*      */   public synchronized byte[] read(RelativePath.RelativeFile paramRelativeFile) throws IOException {
/*  351 */     Entry entry = getZipIndexEntry(paramRelativeFile);
/*  352 */     if (entry == null)
/*  353 */       throw new FileNotFoundException("Path not found in ZIP: " + paramRelativeFile.path);
/*  354 */     return read(entry);
/*      */   }
/*      */
/*      */   synchronized byte[] read(Entry paramEntry) throws IOException {
/*  358 */     openFile();
/*  359 */     byte[] arrayOfByte = readBytes(paramEntry);
/*  360 */     closeFile();
/*  361 */     return arrayOfByte;
/*      */   }
/*      */
/*      */   public synchronized int read(RelativePath.RelativeFile paramRelativeFile, byte[] paramArrayOfbyte) throws IOException {
/*  365 */     Entry entry = getZipIndexEntry(paramRelativeFile);
/*  366 */     if (entry == null)
/*  367 */       throw new FileNotFoundException();
/*  368 */     return read(entry, paramArrayOfbyte);
/*      */   }
/*      */
/*      */
/*      */   synchronized int read(Entry paramEntry, byte[] paramArrayOfbyte) throws IOException {
/*  373 */     return readBytes(paramEntry, paramArrayOfbyte);
/*      */   }
/*      */
/*      */
/*      */   private byte[] readBytes(Entry paramEntry) throws IOException {
/*  378 */     byte[] arrayOfByte1 = getHeader(paramEntry);
/*  379 */     int i = paramEntry.compressedSize;
/*  380 */     byte[] arrayOfByte2 = new byte[i];
/*  381 */     this.zipRandomFile.skipBytes(get2ByteLittleEndian(arrayOfByte1, 26) + get2ByteLittleEndian(arrayOfByte1, 28));
/*  382 */     this.zipRandomFile.readFully(arrayOfByte2, 0, i);
/*      */
/*      */
/*  385 */     if (get2ByteLittleEndian(arrayOfByte1, 8) == 0) {
/*  386 */       return arrayOfByte2;
/*      */     }
/*  388 */     int j = paramEntry.size;
/*  389 */     byte[] arrayOfByte3 = new byte[j];
/*  390 */     if (inflate(arrayOfByte2, arrayOfByte3) != j) {
/*  391 */       throw new ZipException("corrupted zip file");
/*      */     }
/*  393 */     return arrayOfByte3;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private int readBytes(Entry paramEntry, byte[] paramArrayOfbyte) throws IOException {
/*  400 */     byte[] arrayOfByte1 = getHeader(paramEntry);
/*      */
/*      */
/*  403 */     if (get2ByteLittleEndian(arrayOfByte1, 8) == 0) {
/*  404 */       this.zipRandomFile.skipBytes(get2ByteLittleEndian(arrayOfByte1, 26) + get2ByteLittleEndian(arrayOfByte1, 28));
/*  405 */       int k = 0;
/*  406 */       int m = paramArrayOfbyte.length;
/*  407 */       while (k < m) {
/*  408 */         int n = this.zipRandomFile.read(paramArrayOfbyte, k, m - k);
/*  409 */         if (n == -1)
/*      */           break;
/*  411 */         k += n;
/*      */       }
/*  413 */       return paramEntry.size;
/*      */     }
/*      */
/*  416 */     int i = paramEntry.compressedSize;
/*  417 */     byte[] arrayOfByte2 = new byte[i];
/*  418 */     this.zipRandomFile.skipBytes(get2ByteLittleEndian(arrayOfByte1, 26) + get2ByteLittleEndian(arrayOfByte1, 28));
/*  419 */     this.zipRandomFile.readFully(arrayOfByte2, 0, i);
/*      */
/*  421 */     int j = inflate(arrayOfByte2, paramArrayOfbyte);
/*  422 */     if (j == -1) {
/*  423 */       throw new ZipException("corrupted zip file");
/*      */     }
/*  425 */     return paramEntry.size;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private byte[] getHeader(Entry paramEntry) throws IOException {
/*  433 */     this.zipRandomFile.seek(paramEntry.offset);
/*  434 */     byte[] arrayOfByte = new byte[30];
/*  435 */     this.zipRandomFile.readFully(arrayOfByte);
/*  436 */     if (get4ByteLittleEndian(arrayOfByte, 0) != 67324752)
/*  437 */       throw new ZipException("corrupted zip file");
/*  438 */     if ((get2ByteLittleEndian(arrayOfByte, 6) & 0x1) != 0)
/*  439 */       throw new ZipException("encrypted zip file");
/*  440 */     return arrayOfByte;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private int inflate(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/*  448 */     Inflater inflater = (this.inflaterRef == null) ? null : this.inflaterRef.get();
/*      */
/*      */
/*  451 */     if (inflater == null) {
/*  452 */       this.inflaterRef = new SoftReference<>(inflater = new Inflater(true));
/*      */     }
/*  454 */     inflater.reset();
/*  455 */     inflater.setInput(paramArrayOfbyte1);
/*      */     try {
/*  457 */       return inflater.inflate(paramArrayOfbyte2);
/*  458 */     } catch (DataFormatException dataFormatException) {
/*  459 */       return -1;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private static int get2ByteLittleEndian(byte[] paramArrayOfbyte, int paramInt) {
/*  468 */     return (paramArrayOfbyte[paramInt] & 0xFF) + ((paramArrayOfbyte[paramInt + 1] & 0xFF) << 8);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private static int get4ByteLittleEndian(byte[] paramArrayOfbyte, int paramInt) {
/*  475 */     return (paramArrayOfbyte[paramInt] & 0xFF) + ((paramArrayOfbyte[paramInt + 1] & 0xFF) << 8) + ((paramArrayOfbyte[paramInt + 2] & 0xFF) << 16) + ((paramArrayOfbyte[paramInt + 3] & 0xFF) << 24);
/*      */   }
/*      */
/*      */
/*      */   private class ZipDirectory
/*      */   {
/*      */     private RelativePath.RelativeDirectory lastDir;
/*      */
/*      */     private int lastStart;
/*      */
/*      */     private int lastLen;
/*      */
/*      */     byte[] zipDir;
/*      */
/*  489 */     RandomAccessFile zipRandomFile = null;
/*  490 */     ZipFileIndex zipFileIndex = null;
/*      */
/*      */     public ZipDirectory(RandomAccessFile param1RandomAccessFile, long param1Long1, long param1Long2, ZipFileIndex param1ZipFileIndex1) throws IOException {
/*  493 */       this.zipRandomFile = param1RandomAccessFile;
/*  494 */       this.zipFileIndex = param1ZipFileIndex1;
/*  495 */       hasValidHeader();
/*  496 */       findCENRecord(param1Long1, param1Long2);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     private boolean hasValidHeader() throws IOException {
/*  504 */       long l = this.zipRandomFile.getFilePointer();
/*      */       try {
/*  506 */         if (this.zipRandomFile.read() == 80 &&
/*  507 */           this.zipRandomFile.read() == 75 &&
/*  508 */           this.zipRandomFile.read() == 3 &&
/*  509 */           this.zipRandomFile.read() == 4) {
/*  510 */           return true;
/*      */
/*      */         }
/*      */       }
/*      */       finally {
/*      */
/*  516 */         this.zipRandomFile.seek(l);
/*      */       }
/*  518 */       throw new ZipFormatException("invalid zip magic");
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     private void findCENRecord(long param1Long1, long param1Long2) throws IOException {
/*  527 */       long l1 = param1Long2 - param1Long1;
/*  528 */       int i = 1024;
/*  529 */       byte[] arrayOfByte = new byte[i];
/*  530 */       long l2 = param1Long2 - param1Long1;
/*      */
/*      */
/*  533 */       while (l2 >= 22L) {
/*  534 */         if (l2 < i)
/*  535 */           i = (int)l2;
/*  536 */         long l = l2 - i;
/*  537 */         this.zipRandomFile.seek(param1Long1 + l);
/*  538 */         this.zipRandomFile.readFully(arrayOfByte, 0, i);
/*  539 */         int j = i - 22;
/*  540 */         while (j >= 0 && (arrayOfByte[j] != 80 || arrayOfByte[j + 1] != 75 || arrayOfByte[j + 2] != 5 || arrayOfByte[j + 3] != 6 || l + j + 22L + ZipFileIndex
/*      */
/*      */
/*      */
/*      */
/*      */
/*  546 */           .get2ByteLittleEndian(arrayOfByte, j + 20) != l1)) {
/*  547 */           j--;
/*      */         }
/*      */
/*  550 */         if (j >= 0) {
/*  551 */           this.zipDir = new byte[ZipFileIndex.get4ByteLittleEndian(arrayOfByte, j + 12)];
/*  552 */           int k = ZipFileIndex.get4ByteLittleEndian(arrayOfByte, j + 16);
/*      */
/*      */
/*  555 */           if (k < 0 || ZipFileIndex.get2ByteLittleEndian(arrayOfByte, j + 10) == 65535) {
/*  556 */             throw new ZipFormatException("detected a zip64 archive");
/*      */           }
/*  558 */           this.zipRandomFile.seek(param1Long1 + k);
/*  559 */           this.zipRandomFile.readFully(this.zipDir, 0, this.zipDir.length);
/*      */           return;
/*      */         }
/*  562 */         l2 = l + 21L;
/*      */       }
/*      */
/*  565 */       throw new ZipException("cannot read zip file");
/*      */     }
/*      */
/*      */     private void buildIndex() throws IOException {
/*  569 */       int i = this.zipDir.length;
/*      */
/*      */
/*  572 */       if (i > 0) {
/*  573 */         ZipFileIndex.this.directories = (Map)new LinkedHashMap<>();
/*  574 */         ArrayList<Entry> arrayList = new ArrayList();
/*  575 */         for (int j = 0; j < i;) {
/*  576 */           j = readEntry(j, arrayList, ZipFileIndex.this.directories);
/*      */         }
/*      */
/*      */
/*  580 */         for (RelativePath.RelativeDirectory relativeDirectory1 : ZipFileIndex.this.directories.keySet()) {
/*      */
/*  582 */           RelativePath.RelativeDirectory relativeDirectory2 = ZipFileIndex.this.getRelativeDirectory(relativeDirectory1.dirname().getPath());
/*  583 */           String str = relativeDirectory1.basename();
/*  584 */           Entry entry = new Entry(relativeDirectory2, str);
/*  585 */           entry.isDir = true;
/*  586 */           arrayList.add(entry);
/*      */         }
/*      */
/*  589 */         ZipFileIndex.this.entries = arrayList.<Entry>toArray(new Entry[arrayList.size()]);
/*  590 */         Arrays.sort((Object[])ZipFileIndex.this.entries);
/*      */       } else {
/*  592 */         ZipFileIndex.this.cleanupState();
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     private int readEntry(int param1Int, List<Entry> param1List, Map<RelativePath.RelativeDirectory, DirectoryEntry> param1Map) throws IOException {
/*  598 */       if (ZipFileIndex.get4ByteLittleEndian(this.zipDir, param1Int) != 33639248) {
/*  599 */         throw new ZipException("cannot read zip file entry");
/*      */       }
/*      */
/*  602 */       int i = param1Int + 46;
/*  603 */       int j = i;
/*  604 */       int k = j + ZipFileIndex.get2ByteLittleEndian(this.zipDir, param1Int + 28);
/*      */
/*  606 */       if (this.zipFileIndex.symbolFilePrefixLength != 0 && k - j >= ZipFileIndex.this
/*  607 */         .symbolFilePrefixLength) {
/*  608 */         i += this.zipFileIndex.symbolFilePrefixLength;
/*  609 */         j += this.zipFileIndex.symbolFilePrefixLength;
/*      */       }
/*      */
/*  612 */       for (int m = j; m < k; m++) {
/*  613 */         byte b = this.zipDir[m];
/*  614 */         if (b == 92) {
/*  615 */           this.zipDir[m] = 47;
/*  616 */           j = m + 1;
/*  617 */         } else if (b == 47) {
/*  618 */           j = m + 1;
/*      */         }
/*      */       }
/*      */
/*  622 */       RelativePath.RelativeDirectory relativeDirectory = null;
/*  623 */       if (j == i) {
/*  624 */         relativeDirectory = ZipFileIndex.this.getRelativeDirectory("");
/*  625 */       } else if (this.lastDir != null && this.lastLen == j - i - 1) {
/*  626 */         int n = this.lastLen - 1;
/*  627 */         while (this.zipDir[this.lastStart + n] == this.zipDir[i + n]) {
/*  628 */           if (n == 0) {
/*  629 */             relativeDirectory = this.lastDir;
/*      */             break;
/*      */           }
/*  632 */           n--;
/*      */         }
/*      */       }
/*      */
/*      */
/*  637 */       if (relativeDirectory == null) {
/*  638 */         this.lastStart = i;
/*  639 */         this.lastLen = j - i - 1;
/*      */
/*  641 */         relativeDirectory = ZipFileIndex.this.getRelativeDirectory(new String(this.zipDir, i, this.lastLen, "UTF-8"));
/*  642 */         this.lastDir = relativeDirectory;
/*      */
/*      */
/*  645 */         RelativePath.RelativeDirectory relativeDirectory1 = relativeDirectory;
/*      */
/*  647 */         while (param1Map.get(relativeDirectory1) == null) {
/*  648 */           param1Map.put(relativeDirectory1, new DirectoryEntry(relativeDirectory1, this.zipFileIndex));
/*  649 */           if (relativeDirectory1.path.indexOf("/") == relativeDirectory1.path.length() - 1) {
/*      */             break;
/*      */           }
/*      */
/*  653 */           relativeDirectory1 = ZipFileIndex.this.getRelativeDirectory(relativeDirectory1.dirname().getPath());
/*      */
/*      */         }
/*      */
/*      */       }
/*  658 */       else if (param1Map.get(relativeDirectory) == null) {
/*  659 */         param1Map.put(relativeDirectory, new DirectoryEntry(relativeDirectory, this.zipFileIndex));
/*      */       }
/*      */
/*      */
/*      */
/*  664 */       if (j != k) {
/*  665 */         Entry entry = new Entry(relativeDirectory, new String(this.zipDir, j, k - j, "UTF-8"));
/*      */
/*      */
/*  668 */         entry.setNativeTime(ZipFileIndex.get4ByteLittleEndian(this.zipDir, param1Int + 12));
/*  669 */         entry.compressedSize = ZipFileIndex.get4ByteLittleEndian(this.zipDir, param1Int + 20);
/*  670 */         entry.size = ZipFileIndex.get4ByteLittleEndian(this.zipDir, param1Int + 24);
/*  671 */         entry.offset = ZipFileIndex.get4ByteLittleEndian(this.zipDir, param1Int + 42);
/*  672 */         param1List.add(entry);
/*      */       }
/*      */
/*  675 */       return param1Int + 46 + ZipFileIndex
/*  676 */         .get2ByteLittleEndian(this.zipDir, param1Int + 28) + ZipFileIndex
/*  677 */         .get2ByteLittleEndian(this.zipDir, param1Int + 30) + ZipFileIndex
/*  678 */         .get2ByteLittleEndian(this.zipDir, param1Int + 32);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public long getZipFileLastModified() throws IOException {
/*  687 */     synchronized (this) {
/*  688 */       checkIndex();
/*  689 */       return this.zipFileLastModified;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   static class DirectoryEntry
/*      */   {
/*      */     private boolean filesInited;
/*      */
/*      */     private boolean directoriesInited;
/*      */
/*      */     private boolean zipFileEntriesInited;
/*      */
/*      */     private boolean entriesInited;
/*  703 */     private long writtenOffsetOffset = 0L;
/*      */
/*      */     private RelativePath.RelativeDirectory dirName;
/*      */
/*  707 */     private List<String> zipFileEntriesFiles = List.nil();
/*  708 */     private List<String> zipFileEntriesDirectories = List.nil();
/*  709 */     private List<Entry> zipFileEntries = List.nil();
/*      */
/*  711 */     private List<Entry> entries = new ArrayList<>();
/*      */
/*      */     private ZipFileIndex zipFileIndex;
/*      */
/*      */     private int numEntries;
/*      */
/*      */     DirectoryEntry(RelativePath.RelativeDirectory param1RelativeDirectory, ZipFileIndex param1ZipFileIndex) {
/*  718 */       this.filesInited = false;
/*  719 */       this.directoriesInited = false;
/*  720 */       this.entriesInited = false;
/*      */
/*  722 */       this.dirName = param1RelativeDirectory;
/*  723 */       this.zipFileIndex = param1ZipFileIndex;
/*      */     }
/*      */
/*      */     private List<String> getFiles() {
/*  727 */       if (!this.filesInited) {
/*  728 */         initEntries();
/*  729 */         for (Entry entry : this.entries) {
/*  730 */           if (!entry.isDir) {
/*  731 */             this.zipFileEntriesFiles = this.zipFileEntriesFiles.append(entry.name);
/*      */           }
/*      */         }
/*  734 */         this.filesInited = true;
/*      */       }
/*  736 */       return this.zipFileEntriesFiles;
/*      */     }
/*      */
/*      */     private List<String> getDirectories() {
/*  740 */       if (!this.directoriesInited) {
/*  741 */         initEntries();
/*  742 */         for (Entry entry : this.entries) {
/*  743 */           if (entry.isDir) {
/*  744 */             this.zipFileEntriesDirectories = this.zipFileEntriesDirectories.append(entry.name);
/*      */           }
/*      */         }
/*  747 */         this.directoriesInited = true;
/*      */       }
/*  749 */       return this.zipFileEntriesDirectories;
/*      */     }
/*      */
/*      */     private List<Entry> getEntries() {
/*  753 */       if (!this.zipFileEntriesInited) {
/*  754 */         initEntries();
/*  755 */         this.zipFileEntries = List.nil();
/*  756 */         for (Entry entry : this.entries) {
/*  757 */           this.zipFileEntries = this.zipFileEntries.append(entry);
/*      */         }
/*  759 */         this.zipFileEntriesInited = true;
/*      */       }
/*  761 */       return this.zipFileEntries;
/*      */     }
/*      */
/*      */     private Entry getEntry(String param1String) {
/*  765 */       initEntries();
/*  766 */       int i = Collections.binarySearch((List)this.entries, new Entry(this.dirName, param1String));
/*  767 */       if (i < 0) {
/*  768 */         return null;
/*      */       }
/*      */
/*  771 */       return this.entries.get(i);
/*      */     }
/*      */
/*      */     private void initEntries() {
/*  775 */       if (this.entriesInited) {
/*      */         return;
/*      */       }
/*      */
/*  779 */       if (!this.zipFileIndex.readFromIndex) {
/*  780 */         int i = -Arrays.binarySearch((Object[])this.zipFileIndex.entries, new Entry(this.dirName, ZipFileIndex
/*  781 */               .MIN_CHAR)) - 1;
/*  782 */         int j = -Arrays.binarySearch((Object[])this.zipFileIndex.entries, new Entry(this.dirName, ZipFileIndex
/*  783 */               .MAX_CHAR)) - 1;
/*      */
/*  785 */         for (int k = i; k < j; k++) {
/*  786 */           this.entries.add(this.zipFileIndex.entries[k]);
/*      */         }
/*      */       } else {
/*  789 */         File file = this.zipFileIndex.getIndexFile();
/*  790 */         if (file != null) {
/*  791 */           RandomAccessFile randomAccessFile = null;
/*      */
/*  793 */           try { randomAccessFile = new RandomAccessFile(file, "r");
/*  794 */             randomAccessFile.seek(this.writtenOffsetOffset);
/*      */
/*  796 */             for (byte b = 0; b < this.numEntries; b++) {
/*      */
/*  798 */               int i = randomAccessFile.readInt();
/*  799 */               byte[] arrayOfByte = new byte[i];
/*  800 */               randomAccessFile.read(arrayOfByte);
/*  801 */               String str = new String(arrayOfByte, "UTF-8");
/*      */
/*      */
/*  804 */               boolean bool = (randomAccessFile.readByte() == 0) ? false : true;
/*      */
/*      */
/*  807 */               int j = randomAccessFile.readInt();
/*      */
/*      */
/*  810 */               int k = randomAccessFile.readInt();
/*      */
/*      */
/*  813 */               int m = randomAccessFile.readInt();
/*      */
/*      */
/*  816 */               long l = randomAccessFile.readLong();
/*      */
/*  818 */               Entry entry = new Entry(this.dirName, str);
/*  819 */               entry.isDir = bool;
/*  820 */               entry.offset = j;
/*  821 */               entry.size = k;
/*  822 */               entry.compressedSize = m;
/*  823 */               entry.javatime = l;
/*  824 */               this.entries.add(entry);
/*      */             }  }
/*  826 */           catch (Throwable throwable)
/*      */
/*      */           {
/*      */             try {
/*  830 */               if (randomAccessFile != null) {
/*  831 */                 randomAccessFile.close();
/*      */               }
/*  833 */             } catch (Throwable throwable1) {} } finally { try { if (randomAccessFile != null) randomAccessFile.close();  } catch (Throwable throwable) {} }
/*      */
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*  840 */       this.entriesInited = true;
/*      */     }
/*      */
/*      */     List<Entry> getEntriesAsCollection() {
/*  844 */       initEntries();
/*      */
/*  846 */       return this.entries;
/*      */     }
/*      */   }
/*      */
/*      */   private boolean readIndex() {
/*  851 */     if (this.triedToReadIndex || !this.usePreindexedCache) {
/*  852 */       return false;
/*      */     }
/*      */
/*  855 */     boolean bool = false;
/*  856 */     synchronized (this) {
/*  857 */       this.triedToReadIndex = true;
/*  858 */       RandomAccessFile randomAccessFile = null;
/*      */       try {
/*  860 */         File file = getIndexFile();
/*  861 */         randomAccessFile = new RandomAccessFile(file, "r");
/*      */
/*  863 */         long l = randomAccessFile.readLong();
/*  864 */         if (this.zipFile.lastModified() != l) {
/*  865 */           bool = false;
/*      */         } else {
/*  867 */           this.directories = new LinkedHashMap<>();
/*  868 */           int i = randomAccessFile.readInt();
/*  869 */           for (byte b = 0; b < i; b++) {
/*  870 */             int j = randomAccessFile.readInt();
/*  871 */             byte[] arrayOfByte = new byte[j];
/*  872 */             randomAccessFile.read(arrayOfByte);
/*      */
/*  874 */             RelativePath.RelativeDirectory relativeDirectory = getRelativeDirectory(new String(arrayOfByte, "UTF-8"));
/*  875 */             DirectoryEntry directoryEntry = new DirectoryEntry(relativeDirectory, this);
/*  876 */             directoryEntry.numEntries = randomAccessFile.readInt();
/*  877 */             directoryEntry.writtenOffsetOffset = randomAccessFile.readLong();
/*  878 */             this.directories.put(relativeDirectory, directoryEntry);
/*      */           }
/*  880 */           bool = true;
/*  881 */           this.zipFileLastModified = l;
/*      */         }
/*  883 */       } catch (Throwable throwable) {
/*      */
/*      */       } finally {
/*  886 */         if (randomAccessFile != null) {
/*      */           try {
/*  888 */             randomAccessFile.close();
/*  889 */           } catch (Throwable throwable) {}
/*      */         }
/*      */       }
/*      */
/*      */
/*  894 */       if (bool == true) {
/*  895 */         this.readFromIndex = true;
/*      */       }
/*      */     }
/*      */
/*  899 */     return bool;
/*      */   }
/*      */
/*      */   private boolean writeIndex() {
/*  903 */     boolean bool = false;
/*  904 */     if (this.readFromIndex || !this.usePreindexedCache) {
/*  905 */       return true;
/*      */     }
/*      */
/*  908 */     if (!this.writeIndex) {
/*  909 */       return true;
/*      */     }
/*      */
/*  912 */     File file = getIndexFile();
/*  913 */     if (file == null) {
/*  914 */       return false;
/*      */     }
/*      */
/*  917 */     RandomAccessFile randomAccessFile = null;
/*  918 */     long l = 0L;
/*      */
/*  920 */     try { randomAccessFile = new RandomAccessFile(file, "rw");
/*      */
/*  922 */       randomAccessFile.writeLong(this.zipFileLastModified);
/*  923 */       l += 8L;
/*      */
/*  925 */       ArrayList<DirectoryEntry> arrayList = new ArrayList();
/*  926 */       HashMap<Object, Object> hashMap = new HashMap<>();
/*  927 */       randomAccessFile.writeInt(this.directories.keySet().size());
/*  928 */       l += 4L;
/*      */
/*  930 */       for (RelativePath.RelativeDirectory relativeDirectory : this.directories.keySet()) {
/*  931 */         DirectoryEntry directoryEntry = this.directories.get(relativeDirectory);
/*      */
/*  933 */         arrayList.add(directoryEntry);
/*      */
/*      */
/*  936 */         byte[] arrayOfByte = relativeDirectory.getPath().getBytes("UTF-8");
/*  937 */         int i = arrayOfByte.length;
/*  938 */         randomAccessFile.writeInt(i);
/*  939 */         l += 4L;
/*      */
/*  941 */         randomAccessFile.write(arrayOfByte);
/*  942 */         l += i;
/*      */
/*      */
/*  945 */         List<Entry> list = directoryEntry.getEntriesAsCollection();
/*  946 */         randomAccessFile.writeInt(list.size());
/*  947 */         l += 4L;
/*      */
/*  949 */         hashMap.put(relativeDirectory, new Long(l));
/*      */
/*      */
/*  952 */         directoryEntry.writtenOffsetOffset = 0L;
/*  953 */         randomAccessFile.writeLong(0L);
/*  954 */         l += 8L;
/*      */       }
/*      */
/*  957 */       for (DirectoryEntry directoryEntry : arrayList) {
/*      */
/*  959 */         long l1 = randomAccessFile.getFilePointer();
/*      */
/*  961 */         long l2 = ((Long)hashMap.get(directoryEntry.dirName)).longValue();
/*  962 */         randomAccessFile.seek(l2);
/*  963 */         randomAccessFile.writeLong(l);
/*      */
/*  965 */         randomAccessFile.seek(l1);
/*      */
/*      */
/*  968 */         List<Entry> list = directoryEntry.getEntriesAsCollection();
/*  969 */         for (Entry entry : list) {
/*      */
/*  971 */           byte[] arrayOfByte = entry.name.getBytes("UTF-8");
/*  972 */           int i = arrayOfByte.length;
/*  973 */           randomAccessFile.writeInt(i);
/*  974 */           l += 4L;
/*  975 */           randomAccessFile.write(arrayOfByte);
/*  976 */           l += i;
/*      */
/*      */
/*  979 */           randomAccessFile.writeByte(entry.isDir ? 1 : 0);
/*  980 */           l++;
/*      */
/*      */
/*  983 */           randomAccessFile.writeInt(entry.offset);
/*  984 */           l += 4L;
/*      */
/*      */
/*  987 */           randomAccessFile.writeInt(entry.size);
/*  988 */           l += 4L;
/*      */
/*      */
/*  991 */           randomAccessFile.writeInt(entry.compressedSize);
/*  992 */           l += 4L;
/*      */
/*      */
/*  995 */           randomAccessFile.writeLong(entry.getLastModified());
/*  996 */           l += 8L;
/*      */         }
/*      */       }  }
/*  999 */     catch (Throwable throwable)
/*      */
/*      */     {
/*      */       try {
/* 1003 */         if (randomAccessFile != null) {
/* 1004 */           randomAccessFile.close();
/*      */         }
/* 1006 */       } catch (IOException iOException) {} } finally { try { if (randomAccessFile != null) randomAccessFile.close();  } catch (IOException iOException) {} }
/*      */
/*      */
/*      */
/*      */
/* 1011 */     return bool;
/*      */   }
/*      */
/*      */   public boolean writeZipIndex() {
/* 1015 */     synchronized (this) {
/* 1016 */       return writeIndex();
/*      */     }
/*      */   }
/*      */
/*      */   private File getIndexFile() {
/* 1021 */     if (this.zipIndexFile == null) {
/* 1022 */       if (this.zipFile == null) {
/* 1023 */         return null;
/*      */       }
/*      */
/* 1026 */       this
/* 1027 */         .zipIndexFile = new File(((this.preindexedCacheLocation == null) ? "" : this.preindexedCacheLocation) + this.zipFile.getName() + ".index");
/*      */     }
/*      */
/* 1030 */     return this.zipIndexFile;
/*      */   }
/*      */
/*      */   public File getZipFile() {
/* 1034 */     return this.zipFile;
/*      */   }
/*      */
/*      */   File getAbsoluteFile() {
/* 1038 */     File file = (this.absFileRef == null) ? null : this.absFileRef.get();
/* 1039 */     if (file == null) {
/* 1040 */       file = this.zipFile.getAbsoluteFile();
/* 1041 */       this.absFileRef = new SoftReference<>(file);
/*      */     }
/* 1043 */     return file;
/*      */   }
/*      */
/*      */
/*      */   private RelativePath.RelativeDirectory getRelativeDirectory(String paramString) {
/* 1048 */     SoftReference<RelativePath.RelativeDirectory> softReference = this.relativeDirectoryCache.get(paramString);
/* 1049 */     if (softReference != null) {
/* 1050 */       RelativePath.RelativeDirectory relativeDirectory1 = softReference.get();
/* 1051 */       if (relativeDirectory1 != null)
/* 1052 */         return relativeDirectory1;
/*      */     }
/* 1054 */     RelativePath.RelativeDirectory relativeDirectory = new RelativePath.RelativeDirectory(paramString);
/* 1055 */     this.relativeDirectoryCache.put(paramString, new SoftReference<>(relativeDirectory));
/* 1056 */     return relativeDirectory;
/*      */   }
/*      */
/*      */   static class Entry implements Comparable<Entry> {
/* 1060 */     public static final Entry[] EMPTY_ARRAY = new Entry[0];
/*      */
/*      */     RelativePath.RelativeDirectory dir;
/*      */
/*      */     boolean isDir;
/*      */
/*      */     String name;
/*      */
/*      */     int offset;
/*      */
/*      */     int size;
/*      */
/*      */     int compressedSize;
/*      */     long javatime;
/*      */     private int nativetime;
/*      */
/*      */     public Entry(RelativePath param1RelativePath) {
/* 1077 */       this(param1RelativePath.dirname(), param1RelativePath.basename());
/*      */     }
/*      */
/*      */     public Entry(RelativePath.RelativeDirectory param1RelativeDirectory, String param1String) {
/* 1081 */       this.dir = param1RelativeDirectory;
/* 1082 */       this.name = param1String;
/*      */     }
/*      */
/*      */     public String getName() {
/* 1086 */       return (new RelativePath.RelativeFile(this.dir, this.name)).getPath();
/*      */     }
/*      */
/*      */     public String getFileName() {
/* 1090 */       return this.name;
/*      */     }
/*      */
/*      */     public long getLastModified() {
/* 1094 */       if (this.javatime == 0L) {
/* 1095 */         this.javatime = dosToJavaTime(this.nativetime);
/*      */       }
/* 1097 */       return this.javatime;
/*      */     }
/*      */
/*      */
/*      */
/*      */     private static long dosToJavaTime(int param1Int) {
/* 1103 */       Calendar calendar = Calendar.getInstance();
/* 1104 */       calendar.set(1, (param1Int >> 25 & 0x7F) + 1980);
/* 1105 */       calendar.set(2, (param1Int >> 21 & 0xF) - 1);
/* 1106 */       calendar.set(5, param1Int >> 16 & 0x1F);
/* 1107 */       calendar.set(11, param1Int >> 11 & 0x1F);
/* 1108 */       calendar.set(12, param1Int >> 5 & 0x3F);
/* 1109 */       calendar.set(13, param1Int << 1 & 0x3E);
/* 1110 */       calendar.set(14, 0);
/* 1111 */       return calendar.getTimeInMillis();
/*      */     }
/*      */
/*      */     void setNativeTime(int param1Int) {
/* 1115 */       this.nativetime = param1Int;
/*      */     }
/*      */
/*      */     public boolean isDirectory() {
/* 1119 */       return this.isDir;
/*      */     }
/*      */
/*      */     public int compareTo(Entry param1Entry) {
/* 1123 */       RelativePath.RelativeDirectory relativeDirectory = param1Entry.dir;
/* 1124 */       if (this.dir != relativeDirectory) {
/* 1125 */         int i = this.dir.compareTo(relativeDirectory);
/* 1126 */         if (i != 0)
/* 1127 */           return i;
/*      */       }
/* 1129 */       return this.name.compareTo(param1Entry.name);
/*      */     }
/*      */
/*      */
/*      */     public boolean equals(Object param1Object) {
/* 1134 */       if (!(param1Object instanceof Entry))
/* 1135 */         return false;
/* 1136 */       Entry entry = (Entry)param1Object;
/* 1137 */       return (this.dir.equals(entry.dir) && this.name.equals(entry.name));
/*      */     }
/*      */
/*      */
/*      */     public int hashCode() {
/* 1142 */       int i = 7;
/* 1143 */       i = 97 * i + ((this.dir != null) ? this.dir.hashCode() : 0);
/* 1144 */       i = 97 * i + ((this.name != null) ? this.name.hashCode() : 0);
/* 1145 */       return i;
/*      */     }
/*      */
/*      */
/*      */     public String toString() {
/* 1150 */       return this.isDir ? ("Dir:" + this.dir + " : " + this.name) : (this.dir + ":" + this.name);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   static final class ZipFormatException
/*      */     extends IOException
/*      */   {
/*      */     private static final long serialVersionUID = 8000196834066748623L;
/*      */
/*      */
/*      */     protected ZipFormatException(String param1String) {
/* 1162 */       super(param1String);
/*      */     }
/*      */
/*      */     protected ZipFormatException(String param1String, Throwable param1Throwable) {
/* 1166 */       super(param1String, param1Throwable);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\file\ZipFileIndex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
