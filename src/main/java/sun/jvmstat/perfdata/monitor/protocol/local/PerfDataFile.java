/*     */ package sun.jvmstat.perfdata.monitor.protocol.local;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import sun.misc.VMSupport;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PerfDataFile
/*     */ {
/*     */   public static final String tmpDirName;
/*     */   public static final String dirNamePrefix = "hsperfdata_";
/*     */   public static final String userDirNamePattern = "hsperfdata_\\S*";
/*     */   public static final String fileNamePattern = "^[0-9]+$";
/*     */   public static final String tmpFileNamePattern = "^hsperfdata_[0-9]+(_[1-2]+)?$";
/*     */   
/*     */   public static File getFile(int paramInt) {
/*  99 */     if (paramInt == 0)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 106 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     File file1 = new File(tmpDirName);
/* 114 */     String[] arrayOfString = file1.list(new FilenameFilter() {
/*     */           public boolean accept(File param1File, String param1String) {
/* 116 */             if (!param1String.startsWith("hsperfdata_")) {
/* 117 */               return false;
/*     */             }
/* 119 */             File file = new File(param1File, param1String);
/* 120 */             return ((file.isDirectory() || file.isFile()) && file
/* 121 */               .canRead());
/*     */           }
/*     */         });
/*     */     
/* 125 */     long l = 0L;
/* 126 */     File file2 = null;
/*     */     
/* 128 */     for (byte b = 0; b < arrayOfString.length; b++) {
/* 129 */       File file3 = new File(tmpDirName + arrayOfString[b]);
/* 130 */       File file4 = null;
/*     */       
/* 132 */       if (file3.exists() && file3.isDirectory()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 138 */         String str = Integer.toString(paramInt);
/* 139 */         file4 = new File(file3.getName(), str);
/*     */       }
/* 141 */       else if (file3.exists() && file3.isFile()) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 146 */         file4 = file3;
/*     */       }
/*     */       else {
/*     */         
/* 150 */         file4 = file3;
/*     */       } 
/*     */       
/* 153 */       if (file4.exists() && file4.isFile() && file4
/* 154 */         .canRead()) {
/* 155 */         long l1 = file4.lastModified();
/* 156 */         if (l1 >= l) {
/* 157 */           l = l1;
/* 158 */           file2 = file4;
/*     */         } 
/*     */       } 
/*     */     } 
/* 162 */     return file2;
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
/*     */   public static File getFile(String paramString, int paramInt) {
/* 182 */     if (paramInt == 0)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 189 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 193 */     String str = getTempDirectory(paramString) + Integer.toString(paramInt);
/* 194 */     File file1 = new File(str);
/*     */     
/* 196 */     if (file1.exists() && file1.isFile() && file1.canRead()) {
/* 197 */       return file1;
/*     */     }
/*     */ 
/*     */     
/* 201 */     long l = 0L;
/* 202 */     File file2 = null;
/* 203 */     for (byte b = 0; b < 2; b++) {
/* 204 */       if (b == 0) {
/* 205 */         str = getTempDirectory() + Integer.toString(paramInt);
/*     */       } else {
/*     */         
/* 208 */         str = getTempDirectory() + Integer.toString(paramInt) + Integer.toString(b);
/*     */       } 
/*     */       
/* 211 */       file1 = new File(str);
/*     */       
/* 213 */       if (file1.exists() && file1.isFile() && file1.canRead()) {
/* 214 */         long l1 = file1.lastModified();
/* 215 */         if (l1 >= l) {
/* 216 */           l = l1;
/* 217 */           file2 = file1;
/*     */         } 
/*     */       } 
/*     */     } 
/* 221 */     return file2;
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
/*     */   public static int getLocalVmId(File paramFile) {
/*     */     try {
/* 238 */       return Integer.parseInt(paramFile.getName());
/* 239 */     } catch (NumberFormatException numberFormatException) {
/*     */ 
/*     */       
/* 242 */       String str = paramFile.getName();
/* 243 */       if (str.startsWith("hsperfdata_")) {
/* 244 */         int i = str.indexOf('_');
/* 245 */         int j = str.lastIndexOf('_');
/*     */         try {
/* 247 */           if (i == j) {
/* 248 */             return Integer.parseInt(str.substring(i + 1));
/*     */           }
/* 250 */           return Integer.parseInt(str.substring(i + 1, j));
/*     */         }
/* 252 */         catch (NumberFormatException numberFormatException1) {}
/*     */       } 
/* 254 */       throw new IllegalArgumentException("file name does not match pattern");
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
/*     */   public static String getTempDirectory() {
/* 269 */     return tmpDirName;
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
/*     */   public static String getTempDirectory(String paramString) {
/* 285 */     return tmpDirName + "hsperfdata_" + paramString + File.separator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 294 */     String str = VMSupport.getVMTemporaryDirectory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 302 */     if (str.lastIndexOf(File.separator) != str.length() - 1) {
/* 303 */       str = str + File.separator;
/*     */     }
/* 305 */     tmpDirName = str;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\protocol\local\PerfDataFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */