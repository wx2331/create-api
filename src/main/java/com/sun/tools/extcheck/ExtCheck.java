/*     */ package com.sun.tools.extcheck;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.net.JarURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import sun.net.www.ParseUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtCheck
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private String targetSpecTitle;
/*     */   private String targetSpecVersion;
/*     */   private String targetSpecVendor;
/*     */   private String targetImplTitle;
/*     */   private String targetImplVersion;
/*     */   private String targetImplVendor;
/*     */   private String targetsealed;
/*     */   private boolean verboseFlag;
/*     */   
/*     */   static ExtCheck create(File paramFile, boolean paramBoolean) {
/*  79 */     return new ExtCheck(paramFile, paramBoolean);
/*     */   }
/*     */   
/*     */   private ExtCheck(File paramFile, boolean paramBoolean) {
/*  83 */     this.verboseFlag = paramBoolean;
/*  84 */     investigateTarget(paramFile);
/*     */   }
/*     */ 
/*     */   
/*     */   private void investigateTarget(File paramFile) {
/*  89 */     verboseMessage("Target file:" + paramFile);
/*  90 */     Manifest manifest = null;
/*     */     try {
/*  92 */       File file = new File(paramFile.getCanonicalPath());
/*  93 */       URL uRL = ParseUtil.fileToEncodedURL(file);
/*  94 */       if (uRL != null) {
/*  95 */         JarLoader jarLoader = new JarLoader(uRL);
/*  96 */         JarFile jarFile = jarLoader.getJarFile();
/*  97 */         manifest = jarFile.getManifest();
/*     */       } 
/*  99 */     } catch (MalformedURLException malformedURLException) {
/* 100 */       error("Malformed URL ");
/* 101 */     } catch (IOException iOException) {
/* 102 */       error("IO Exception ");
/*     */     } 
/* 104 */     if (manifest == null)
/* 105 */       error("No manifest available in " + paramFile); 
/* 106 */     Attributes attributes = manifest.getMainAttributes();
/* 107 */     if (attributes != null) {
/* 108 */       this.targetSpecTitle = attributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
/* 109 */       this.targetSpecVersion = attributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
/* 110 */       this.targetSpecVendor = attributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
/* 111 */       this.targetImplTitle = attributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
/* 112 */       this.targetImplVersion = attributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
/* 113 */       this.targetImplVendor = attributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
/* 114 */       this.targetsealed = attributes.getValue(Attributes.Name.SEALED);
/*     */     } else {
/* 116 */       error("No attributes available in the manifest");
/*     */     } 
/* 118 */     if (this.targetSpecTitle == null)
/* 119 */       error("The target file does not have a specification title"); 
/* 120 */     if (this.targetSpecVersion == null)
/* 121 */       error("The target file does not have a specification version"); 
/* 122 */     verboseMessage("Specification title:" + this.targetSpecTitle);
/* 123 */     verboseMessage("Specification version:" + this.targetSpecVersion);
/* 124 */     if (this.targetSpecVendor != null)
/* 125 */       verboseMessage("Specification vendor:" + this.targetSpecVendor); 
/* 126 */     if (this.targetImplVersion != null)
/* 127 */       verboseMessage("Implementation version:" + this.targetImplVersion); 
/* 128 */     if (this.targetImplVendor != null)
/* 129 */       verboseMessage("Implementation vendor:" + this.targetImplVendor); 
/* 130 */     verboseMessage("");
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
/*     */   boolean checkInstalledAgainstTarget() {
/*     */     File[] arrayOfFile;
/* 143 */     String str = System.getProperty("java.ext.dirs");
/*     */     
/* 145 */     if (str != null) {
/* 146 */       StringTokenizer stringTokenizer = new StringTokenizer(str, File.pathSeparator);
/*     */       
/* 148 */       int i = stringTokenizer.countTokens();
/* 149 */       arrayOfFile = new File[i];
/* 150 */       for (byte b1 = 0; b1 < i; b1++) {
/* 151 */         arrayOfFile[b1] = new File(stringTokenizer.nextToken());
/*     */       }
/*     */     } else {
/* 154 */       arrayOfFile = new File[0];
/*     */     } 
/*     */     
/* 157 */     boolean bool = true;
/* 158 */     for (byte b = 0; b < arrayOfFile.length; b++) {
/* 159 */       String[] arrayOfString = arrayOfFile[b].list();
/* 160 */       if (arrayOfString != null) {
/* 161 */         for (byte b1 = 0; b1 < arrayOfString.length; b1++) {
/*     */           try {
/* 163 */             File file1 = new File(arrayOfFile[b], arrayOfString[b1]);
/* 164 */             File file2 = new File(file1.getCanonicalPath());
/* 165 */             URL uRL = ParseUtil.fileToEncodedURL(file2);
/* 166 */             if (uRL != null) {
/* 167 */               bool = (bool && checkURLRecursively(1, uRL)) ? true : false;
/*     */             }
/* 169 */           } catch (MalformedURLException malformedURLException) {
/* 170 */             error("Malformed URL");
/* 171 */           } catch (IOException iOException) {
/* 172 */             error("IO Exception");
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 177 */     if (bool) {
/* 178 */       generalMessage("No conflicting installed jar found.");
/*     */     } else {
/* 180 */       generalMessage("Conflicting installed jar found.  Use -verbose for more information.");
/*     */     } 
/*     */     
/* 183 */     return bool;
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
/*     */   private boolean checkURLRecursively(int paramInt, URL paramURL) throws IOException {
/* 197 */     verboseMessage("Comparing with " + paramURL);
/* 198 */     JarLoader jarLoader = new JarLoader(paramURL);
/* 199 */     JarFile jarFile = jarLoader.getJarFile();
/* 200 */     Manifest manifest = jarFile.getManifest();
/* 201 */     if (manifest != null) {
/* 202 */       Attributes attributes = manifest.getMainAttributes();
/* 203 */       if (attributes != null) {
/* 204 */         String str1 = attributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
/* 205 */         String str2 = attributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
/* 206 */         String str3 = attributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
/* 207 */         String str4 = attributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
/*     */         
/* 209 */         String str5 = attributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
/* 210 */         String str6 = attributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
/* 211 */         String str7 = attributes.getValue(Attributes.Name.SEALED);
/* 212 */         if (str1 != null && 
/* 213 */           str1.equals(this.targetSpecTitle) && 
/* 214 */           str2 != null && (
/* 215 */           str2.equals(this.targetSpecVersion) || 
/* 216 */           isNotOlderThan(str2, this.targetSpecVersion))) {
/* 217 */           verboseMessage("");
/* 218 */           verboseMessage("CONFLICT DETECTED ");
/* 219 */           verboseMessage("Conflicting file:" + paramURL);
/* 220 */           verboseMessage("Installed Version:" + str2);
/*     */           
/* 222 */           if (str4 != null) {
/* 223 */             verboseMessage("Implementation Title:" + str4);
/*     */           }
/* 225 */           if (str5 != null) {
/* 226 */             verboseMessage("Implementation Version:" + str5);
/*     */           }
/* 228 */           if (str6 != null) {
/* 229 */             verboseMessage("Implementation Vendor:" + str6);
/*     */           }
/* 231 */           return false;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 238 */     boolean bool = true;
/* 239 */     URL[] arrayOfURL = jarLoader.getClassPath();
/* 240 */     if (arrayOfURL != null) {
/* 241 */       for (byte b = 0; b < arrayOfURL.length; b++) {
/* 242 */         if (paramURL != null) {
/* 243 */           boolean bool1 = checkURLRecursively(paramInt + 1, arrayOfURL[b]);
/* 244 */           bool = (bool1 && bool) ? true : false;
/*     */         } 
/*     */       } 
/*     */     }
/* 248 */     return bool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isNotOlderThan(String paramString1, String paramString2) throws NumberFormatException {
/* 259 */     if (paramString1 == null || paramString1.length() < 1) {
/* 260 */       throw new NumberFormatException("Empty version string");
/*     */     }
/*     */ 
/*     */     
/* 264 */     StringTokenizer stringTokenizer1 = new StringTokenizer(paramString2, ".", true);
/* 265 */     StringTokenizer stringTokenizer2 = new StringTokenizer(paramString1, ".", true);
/* 266 */     while (stringTokenizer1.hasMoreTokens() || stringTokenizer2.hasMoreTokens()) {
/*     */       byte b1;
/*     */       byte b2;
/* 269 */       if (stringTokenizer1.hasMoreTokens()) {
/* 270 */         b1 = Integer.parseInt(stringTokenizer1.nextToken());
/*     */       } else {
/* 272 */         b1 = 0;
/*     */       } 
/* 274 */       if (stringTokenizer2.hasMoreTokens()) {
/* 275 */         b2 = Integer.parseInt(stringTokenizer2.nextToken());
/*     */       } else {
/* 277 */         b2 = 0;
/*     */       } 
/* 279 */       if (b2 < b1)
/* 280 */         return false; 
/* 281 */       if (b2 > b1) {
/* 282 */         return true;
/*     */       }
/*     */       
/* 285 */       if (stringTokenizer1.hasMoreTokens())
/* 286 */         stringTokenizer1.nextToken(); 
/* 287 */       if (stringTokenizer2.hasMoreTokens()) {
/* 288 */         stringTokenizer2.nextToken();
/*     */       }
/*     */     } 
/*     */     
/* 292 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void verboseMessage(String paramString) {
/* 300 */     if (this.verboseFlag) {
/* 301 */       System.err.println(paramString);
/*     */     }
/*     */   }
/*     */   
/*     */   void generalMessage(String paramString) {
/* 306 */     System.err.println(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void error(String paramString) throws RuntimeException {
/* 313 */     throw new RuntimeException(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class JarLoader
/*     */   {
/*     */     private final URL base;
/*     */ 
/*     */     
/*     */     private JarFile jar;
/*     */ 
/*     */     
/*     */     private URL csu;
/*     */ 
/*     */ 
/*     */     
/*     */     JarLoader(URL param1URL) {
/* 331 */       String str = param1URL + "!/";
/* 332 */       URL uRL = null;
/*     */       try {
/* 334 */         uRL = new URL("jar", "", str);
/* 335 */         this.jar = findJarFile(param1URL);
/* 336 */         this.csu = param1URL;
/* 337 */       } catch (MalformedURLException malformedURLException) {
/* 338 */         ExtCheck.error("Malformed url " + str);
/* 339 */       } catch (IOException iOException) {
/* 340 */         ExtCheck.error("IO Exception occurred");
/*     */       } 
/* 342 */       this.base = uRL;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     URL getBaseURL() {
/* 350 */       return this.base;
/*     */     }
/*     */     
/*     */     JarFile getJarFile() {
/* 354 */       return this.jar;
/*     */     }
/*     */ 
/*     */     
/*     */     private JarFile findJarFile(URL param1URL) throws IOException {
/* 359 */       if ("file".equals(param1URL.getProtocol())) {
/* 360 */         String str = param1URL.getFile().replace('/', File.separatorChar);
/* 361 */         File file = new File(str);
/* 362 */         if (!file.exists()) {
/* 363 */           throw new FileNotFoundException(str);
/*     */         }
/* 365 */         return new JarFile(str);
/*     */       } 
/* 367 */       URLConnection uRLConnection = getBaseURL().openConnection();
/*     */       
/* 369 */       return ((JarURLConnection)uRLConnection).getJarFile();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     URL[] getClassPath() throws IOException {
/* 377 */       Manifest manifest = this.jar.getManifest();
/* 378 */       if (manifest != null) {
/* 379 */         Attributes attributes = manifest.getMainAttributes();
/* 380 */         if (attributes != null) {
/* 381 */           String str = attributes.getValue(Attributes.Name.CLASS_PATH);
/* 382 */           if (str != null) {
/* 383 */             return parseClassPath(this.csu, str);
/*     */           }
/*     */         } 
/*     */       } 
/* 387 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private URL[] parseClassPath(URL param1URL, String param1String) throws MalformedURLException {
/* 397 */       StringTokenizer stringTokenizer = new StringTokenizer(param1String);
/* 398 */       URL[] arrayOfURL = new URL[stringTokenizer.countTokens()];
/* 399 */       byte b = 0;
/* 400 */       while (stringTokenizer.hasMoreTokens()) {
/* 401 */         String str = stringTokenizer.nextToken();
/* 402 */         arrayOfURL[b] = new URL(param1URL, str);
/* 403 */         b++;
/*     */       } 
/* 405 */       return arrayOfURL;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\extcheck\ExtCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */