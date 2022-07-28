/*     */ package sun.jvmstat.perfdata.monitor.protocol.local;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocalVmManager
/*     */ {
/*     */   private String userName;
/*     */   private File tmpdir;
/*     */   private Pattern userPattern;
/*     */   private Matcher userMatcher;
/*     */   private FilenameFilter userFilter;
/*     */   private Pattern filePattern;
/*     */   private Matcher fileMatcher;
/*     */   private FilenameFilter fileFilter;
/*     */   private Pattern tmpFilePattern;
/*     */   private Matcher tmpFileMatcher;
/*     */   private FilenameFilter tmpFileFilter;
/*     */   
/*     */   public LocalVmManager() {
/*  66 */     this(null);
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
/*     */   public LocalVmManager(String paramString) {
/*  78 */     this.userName = paramString;
/*     */     
/*  80 */     if (this.userName == null) {
/*  81 */       this.tmpdir = new File(PerfDataFile.getTempDirectory());
/*  82 */       this.userPattern = Pattern.compile("hsperfdata_\\S*");
/*  83 */       this.userMatcher = this.userPattern.matcher("");
/*     */       
/*  85 */       this.userFilter = new FilenameFilter() {
/*     */           public boolean accept(File param1File, String param1String) {
/*  87 */             LocalVmManager.this.userMatcher.reset(param1String);
/*  88 */             return LocalVmManager.this.userMatcher.lookingAt();
/*     */           }
/*     */         };
/*     */     } else {
/*  92 */       this.tmpdir = new File(PerfDataFile.getTempDirectory(this.userName));
/*     */     } 
/*     */     
/*  95 */     this.filePattern = Pattern.compile("^[0-9]+$");
/*  96 */     this.fileMatcher = this.filePattern.matcher("");
/*     */     
/*  98 */     this.fileFilter = new FilenameFilter() {
/*     */         public boolean accept(File param1File, String param1String) {
/* 100 */           LocalVmManager.this.fileMatcher.reset(param1String);
/* 101 */           return LocalVmManager.this.fileMatcher.matches();
/*     */         }
/*     */       };
/*     */     
/* 105 */     this.tmpFilePattern = Pattern.compile("^hsperfdata_[0-9]+(_[1-2]+)?$");
/* 106 */     this.tmpFileMatcher = this.tmpFilePattern.matcher("");
/*     */     
/* 108 */     this.tmpFileFilter = new FilenameFilter() {
/*     */         public boolean accept(File param1File, String param1String) {
/* 110 */           LocalVmManager.this.tmpFileMatcher.reset(param1String);
/* 111 */           return LocalVmManager.this.tmpFileMatcher.matches();
/*     */         }
/*     */       };
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
/*     */   public synchronized Set<Integer> activeVms() {
/* 135 */     HashSet<Integer> hashSet = new HashSet();
/*     */     
/* 137 */     if (!this.tmpdir.isDirectory()) {
/* 138 */       return hashSet;
/*     */     }
/*     */     
/* 141 */     if (this.userName == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 146 */       File[] arrayOfFile1 = this.tmpdir.listFiles(this.userFilter);
/*     */       
/* 148 */       for (byte b = 0; b < arrayOfFile1.length; b++) {
/* 149 */         if (arrayOfFile1[b].isDirectory())
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 154 */           File[] arrayOfFile2 = arrayOfFile1[b].listFiles(this.fileFilter);
/*     */           
/* 156 */           if (arrayOfFile2 != null) {
/* 157 */             for (byte b1 = 0; b1 < arrayOfFile2.length; b1++) {
/* 158 */               if (arrayOfFile2[b1].isFile() && arrayOfFile2[b1].canRead()) {
/* 159 */                 hashSet.add(new Integer(
/* 160 */                       PerfDataFile.getLocalVmId(arrayOfFile2[b1])));
/*     */               
/*     */               }
/*     */             }
/*     */           
/*     */           }
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 173 */       File[] arrayOfFile1 = this.tmpdir.listFiles(this.fileFilter);
/*     */       
/* 175 */       if (arrayOfFile1 != null) {
/* 176 */         for (byte b = 0; b < arrayOfFile1.length; b++) {
/* 177 */           if (arrayOfFile1[b].isFile() && arrayOfFile1[b].canRead()) {
/* 178 */             hashSet.add(new Integer(
/* 179 */                   PerfDataFile.getLocalVmId(arrayOfFile1[b])));
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 186 */     File[] arrayOfFile = this.tmpdir.listFiles(this.tmpFileFilter);
/* 187 */     if (arrayOfFile != null) {
/* 188 */       for (byte b = 0; b < arrayOfFile.length; b++) {
/* 189 */         if (arrayOfFile[b].isFile() && arrayOfFile[b].canRead()) {
/* 190 */           hashSet.add(new Integer(
/* 191 */                 PerfDataFile.getLocalVmId(arrayOfFile[b])));
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 196 */     return hashSet;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\protocol\local\LocalVmManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */