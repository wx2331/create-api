/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
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
/*     */ public class ClassPath
/*     */ {
/*  44 */   static final char dirSeparator = File.pathSeparatorChar;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String pathstr;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ClassPathEntry[] path;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String fileSeparatorChar;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassPath(String paramString)
/*     */   {
/* 188 */     this.fileSeparatorChar = "" + File.separatorChar; init(paramString); } public ClassPath(String[] paramArrayOfString) { this.fileSeparatorChar = "" + File.separatorChar; init(paramArrayOfString); } public ClassPath() { this.fileSeparatorChar = "" + File.separatorChar; String str1 = System.getProperty("sun.boot.class.path"); String str2 = System.getProperty("env.class.path"); if (str2 == null) str2 = ".";  String str3 = str1 + File.pathSeparator + str2; init(str3); }
/*     */   private void init(String paramString) { this.pathstr = paramString; if (paramString.length() == 0)
/*     */       this.path = new ClassPathEntry[0];  byte b = 0; int i = b; while ((i = paramString.indexOf(dirSeparator, i)) != -1) { b++; i++; }  ClassPathEntry[] arrayOfClassPathEntry = new ClassPathEntry[b + 1]; int j = paramString.length(); for (i = b = 0; i < j; i = k + 1) { int k; if ((k = paramString.indexOf(dirSeparator, i)) == -1)
/* 191 */         k = j;  if (i == k) { arrayOfClassPathEntry[b] = new ClassPathEntry(); (arrayOfClassPathEntry[b++]).dir = new File("."); } else { File file = new File(paramString.substring(i, k)); if (file.isFile()) { try { ZipFile zipFile = new ZipFile(file); arrayOfClassPathEntry[b] = new ClassPathEntry(); (arrayOfClassPathEntry[b++]).zip = zipFile; } catch (ZipException zipException) {  } catch (IOException iOException) {} } else { arrayOfClassPathEntry[b] = new ClassPathEntry(); (arrayOfClassPathEntry[b++]).dir = file; }  }  }  this.path = new ClassPathEntry[b]; System.arraycopy(arrayOfClassPathEntry, 0, this.path, 0, b); } private ClassFile getFile(String paramString, boolean paramBoolean) { String str1 = paramString;
/* 192 */     String str2 = "";
/* 193 */     if (!paramBoolean) {
/* 194 */       int i = paramString.lastIndexOf(File.separatorChar);
/* 195 */       str1 = paramString.substring(0, i + 1);
/* 196 */       str2 = paramString.substring(i + 1);
/* 197 */     } else if (!str1.equals("") && 
/* 198 */       !str1.endsWith(this.fileSeparatorChar)) {
/*     */ 
/*     */       
/* 201 */       str1 = str1 + File.separatorChar;
/* 202 */       paramString = str1;
/*     */     } 
/* 204 */     for (byte b = 0; b < this.path.length; b++) {
/* 205 */       if ((this.path[b]).zip != null) {
/* 206 */         String str = paramString.replace(File.separatorChar, '/');
/* 207 */         ZipEntry zipEntry = (this.path[b]).zip.getEntry(str);
/* 208 */         if (zipEntry != null) {
/* 209 */           return new ClassFile((this.path[b]).zip, zipEntry);
/*     */         }
/*     */       } else {
/* 212 */         File file = new File((this.path[b]).dir.getPath(), paramString);
/* 213 */         String[] arrayOfString = this.path[b].getFiles(str1);
/* 214 */         if (paramBoolean) {
/* 215 */           if (arrayOfString.length > 0) {
/* 216 */             return new ClassFile(file);
/*     */           }
/*     */         } else {
/* 219 */           for (byte b1 = 0; b1 < arrayOfString.length; b1++) {
/* 220 */             if (str2.equals(arrayOfString[b1]))
/*     */             {
/*     */ 
/*     */               
/* 224 */               return new ClassFile(file);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 230 */     return null; }
/*     */   private void init(String[] paramArrayOfString) { if (paramArrayOfString.length == 0) { this.pathstr = ""; } else { StringBuilder stringBuilder = new StringBuilder(paramArrayOfString[0]); for (byte b1 = 1; b1 < paramArrayOfString.length; b1++) { stringBuilder.append(File.pathSeparatorChar); stringBuilder.append(paramArrayOfString[b1]); }  this.pathstr = stringBuilder.toString(); }  ClassPathEntry[] arrayOfClassPathEntry = new ClassPathEntry[paramArrayOfString.length]; byte b = 0; for (String str : paramArrayOfString) { File file = new File(str); if (file.isFile()) { try { ZipFile zipFile = new ZipFile(file); arrayOfClassPathEntry[b] = new ClassPathEntry(); (arrayOfClassPathEntry[b++]).zip = zipFile; } catch (ZipException zipException) {  }
/*     */         catch (IOException iOException) {} }
/*     */       else { arrayOfClassPathEntry[b] = new ClassPathEntry(); (arrayOfClassPathEntry[b++]).dir = file; }
/*     */        }
/*     */      this.path = new ClassPathEntry[b]; System.arraycopy(arrayOfClassPathEntry, 0, this.path, 0, b); }
/*     */   public ClassFile getDirectory(String paramString) { return getFile(paramString, true); }
/* 237 */   public ClassFile getFile(String paramString) { return getFile(paramString, false); } public Enumeration getFiles(String paramString1, String paramString2) { Hashtable<Object, Object> hashtable = new Hashtable<>();
/* 238 */     for (int i = this.path.length; --i >= 0; ) {
/* 239 */       if ((this.path[i]).zip != null) {
/* 240 */         Enumeration<? extends ZipEntry> enumeration = (this.path[i]).zip.entries();
/* 241 */         while (enumeration.hasMoreElements()) {
/* 242 */           ZipEntry zipEntry = enumeration.nextElement();
/* 243 */           String str = zipEntry.getName();
/* 244 */           str = str.replace('/', File.separatorChar);
/* 245 */           if (str.startsWith(paramString1) && str.endsWith(paramString2))
/* 246 */             hashtable.put(str, new ClassFile((this.path[i]).zip, zipEntry)); 
/*     */         } 
/*     */         continue;
/*     */       } 
/* 250 */       String[] arrayOfString = this.path[i].getFiles(paramString1);
/* 251 */       for (byte b = 0; b < arrayOfString.length; b++) {
/* 252 */         String str = arrayOfString[b];
/* 253 */         if (str.endsWith(paramString2)) {
/* 254 */           str = paramString1 + File.separatorChar + str;
/* 255 */           File file = new File((this.path[i]).dir.getPath(), str);
/* 256 */           hashtable.put(str, new ClassFile(file));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 261 */     return hashtable.elements(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 268 */     for (int i = this.path.length; --i >= 0;) {
/* 269 */       if ((this.path[i]).zip != null) {
/* 270 */         (this.path[i]).zip.close();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 279 */     return this.pathstr;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\ClassPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */