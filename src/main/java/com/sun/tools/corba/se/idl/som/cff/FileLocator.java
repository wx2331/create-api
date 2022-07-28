/*     */ package com.sun.tools.corba.se.idl.som.cff;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Locale;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipException;
/*     */ import java.util.zip.ZipFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class FileLocator
/*     */ {
/*  62 */   static final Properties pp = System.getProperties();
/*  63 */   static final String classPath = pp.getProperty("java.class.path", ".");
/*  64 */   static final String pathSeparator = pp.getProperty("path.separator", ";");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataInputStream locateClassFile(String paramString) throws FileNotFoundException, IOException {
/*  90 */     boolean bool = true;
/*     */     
/*  92 */     String str1 = "";
/*     */     
/*  94 */     File file = null;
/*     */ 
/*     */     
/*  97 */     StringTokenizer stringTokenizer = new StringTokenizer(classPath, pathSeparator, false);
/*  98 */     String str2 = paramString.replace('.', File.separatorChar) + ".class";
/*     */ 
/*     */     
/* 101 */     while (stringTokenizer.hasMoreTokens() && bool) {
/*     */       
/* 103 */       try { str1 = stringTokenizer.nextToken(); }
/* 104 */       catch (NoSuchElementException noSuchElementException) { break; }
/* 105 */        int i = str1.length();
/* 106 */       String str = (i > 3) ? str1.substring(i - 4) : "";
/* 107 */       if (str.equalsIgnoreCase(".zip") || str
/* 108 */         .equalsIgnoreCase(".jar")) {
/*     */         
/*     */         try {
/*     */           
/* 112 */           NamedDataInputStream namedDataInputStream = locateInZipFile(str1, paramString, true, true);
/* 113 */           if (namedDataInputStream == null)
/*     */             continue; 
/* 115 */           return namedDataInputStream;
/*     */         }
/* 117 */         catch (ZipException zipException) {
/*     */           continue;
/* 119 */         } catch (IOException iOException) {
/*     */           continue;
/*     */         } 
/*     */       }
/*     */       
/* 124 */       try { file = new File(str1 + File.separator + str2); }
/* 125 */       catch (NullPointerException nullPointerException) { continue; }
/* 126 */        if (file != null && file.exists()) {
/* 127 */         bool = false;
/*     */       }
/*     */     } 
/*     */     
/* 131 */     if (bool) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 137 */       int i = paramString.lastIndexOf('.');
/*     */       
/* 139 */       String str = (i >= 0) ? paramString.substring(i + 1) : paramString;
/*     */ 
/*     */       
/* 142 */       return new NamedDataInputStream(new BufferedInputStream(new FileInputStream(str + ".class")), str + ".class", false);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 148 */     return new NamedDataInputStream(new BufferedInputStream(new FileInputStream(file)), str1 + File.separator + str2, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataInputStream locateLocaleSpecificFileInClassPath(String paramString) throws FileNotFoundException, IOException {
/* 190 */     String str2, str3, str1 = "_" + Locale.getDefault().toString();
/* 191 */     int i = paramString.lastIndexOf('/');
/* 192 */     int j = paramString.lastIndexOf('.');
/*     */     
/* 194 */     DataInputStream dataInputStream = null;
/* 195 */     boolean bool = false;
/*     */     
/* 197 */     if (j > 0 && j > i) {
/* 198 */       str2 = paramString.substring(0, j);
/* 199 */       str3 = paramString.substring(j);
/*     */     } else {
/* 201 */       str2 = paramString;
/* 202 */       str3 = "";
/*     */     } 
/*     */     
/*     */     while (true) {
/* 206 */       if (bool)
/* 207 */       { dataInputStream = locateFileInClassPath(paramString); }
/*     */       else { try {
/* 209 */           dataInputStream = locateFileInClassPath(str2 + str1 + str3);
/* 210 */         } catch (Exception exception) {} }
/* 211 */        if (dataInputStream != null || bool)
/*     */         break; 
/* 213 */       int k = str1.lastIndexOf('_');
/* 214 */       if (k > 0) {
/* 215 */         str1 = str1.substring(0, k); continue;
/*     */       } 
/* 217 */       bool = true;
/*     */     } 
/* 219 */     return dataInputStream;
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
/*     */   public static DataInputStream locateFileInClassPath(String paramString) throws FileNotFoundException, IOException {
/* 241 */     boolean bool = true;
/*     */     
/* 243 */     String str1 = "";
/* 244 */     File file = null;
/*     */ 
/*     */ 
/*     */     
/* 248 */     String str2 = (File.separatorChar == '/') ? paramString : paramString.replace(File.separatorChar, '/');
/*     */ 
/*     */     
/* 251 */     String str3 = (File.separatorChar == '/') ? paramString : paramString.replace('/', File.separatorChar);
/*     */     
/* 253 */     StringTokenizer stringTokenizer = new StringTokenizer(classPath, pathSeparator, false);
/*     */     
/* 255 */     while (stringTokenizer.hasMoreTokens() && bool) {
/*     */       
/* 257 */       try { str1 = stringTokenizer.nextToken(); }
/* 258 */       catch (NoSuchElementException noSuchElementException) { break; }
/* 259 */        int i = str1.length();
/* 260 */       String str = (i > 3) ? str1.substring(i - 4) : "";
/* 261 */       if (str.equalsIgnoreCase(".zip") || str
/* 262 */         .equalsIgnoreCase(".jar")) {
/*     */         
/*     */         try {
/*     */           
/* 266 */           NamedDataInputStream namedDataInputStream = locateInZipFile(str1, str2, false, false);
/* 267 */           if (namedDataInputStream == null)
/*     */             continue; 
/* 269 */           return namedDataInputStream;
/*     */         }
/* 271 */         catch (ZipException zipException) {
/*     */           continue;
/* 273 */         } catch (IOException iOException) {
/*     */           continue;
/*     */         } 
/*     */       }
/*     */       
/* 278 */       try { file = new File(str1 + File.separator + str3); }
/* 279 */       catch (NullPointerException nullPointerException) { continue; }
/* 280 */        if (file != null && file.exists()) {
/* 281 */         bool = false;
/*     */       }
/*     */     } 
/*     */     
/* 285 */     if (bool) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 291 */       int i = str3.lastIndexOf(File.separator);
/*     */       
/* 293 */       String str = (i >= 0) ? str3.substring(i + 1) : str3;
/*     */ 
/*     */       
/* 296 */       return new NamedDataInputStream(new BufferedInputStream(new FileInputStream(str)), str, false);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 301 */     return new NamedDataInputStream(new BufferedInputStream(new FileInputStream(file)), str1 + File.separator + str3, false);
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
/*     */   public static String getFileNameFromStream(DataInputStream paramDataInputStream) {
/* 316 */     if (paramDataInputStream instanceof NamedDataInputStream)
/* 317 */       return ((NamedDataInputStream)paramDataInputStream).fullyQualifiedFileName; 
/* 318 */     return "";
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
/*     */   public static boolean isZipFileAssociatedWithStream(DataInputStream paramDataInputStream) {
/* 330 */     if (paramDataInputStream instanceof NamedDataInputStream)
/* 331 */       return ((NamedDataInputStream)paramDataInputStream).inZipFile; 
/* 332 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static NamedDataInputStream locateInZipFile(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2) throws ZipException, IOException {
/* 342 */     ZipFile zipFile = new ZipFile(paramString1);
/*     */     
/* 344 */     if (zipFile == null) {
/* 345 */       return null;
/*     */     }
/* 347 */     String str = paramBoolean1 ? (paramString2.replace('.', '/') + ".class") : paramString2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 363 */     ZipEntry zipEntry = zipFile.getEntry(str);
/* 364 */     if (zipEntry == null) {
/* 365 */       zipFile.close();
/* 366 */       zipFile = null;
/* 367 */       return null;
/*     */     } 
/* 369 */     InputStream inputStream = zipFile.getInputStream(zipEntry);
/* 370 */     if (paramBoolean2)
/* 371 */       inputStream = new BufferedInputStream(inputStream); 
/* 372 */     return new NamedDataInputStream(inputStream, paramString1 + '(' + str + ')', true);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\som\cff\FileLocator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */