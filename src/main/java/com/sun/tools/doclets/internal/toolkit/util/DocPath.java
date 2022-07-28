/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DocPath
/*     */ {
/*     */   private final String path;
/*  44 */   public static final DocPath empty = new DocPath("");
/*     */ 
/*     */   
/*  47 */   public static final DocPath parent = new DocPath("..");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DocPath create(String paramString) {
/*  53 */     return (paramString == null || paramString.isEmpty()) ? empty : new DocPath(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DocPath forClass(ClassDoc paramClassDoc) {
/*  62 */     return (paramClassDoc == null) ? empty : 
/*  63 */       forPackage(paramClassDoc.containingPackage()).resolve(forName(paramClassDoc));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DocPath forName(ClassDoc paramClassDoc) {
/*  72 */     return (paramClassDoc == null) ? empty : new DocPath(paramClassDoc.name() + ".html");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DocPath forPackage(ClassDoc paramClassDoc) {
/*  81 */     return (paramClassDoc == null) ? empty : forPackage(paramClassDoc.containingPackage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DocPath forPackage(PackageDoc paramPackageDoc) {
/*  90 */     return (paramPackageDoc == null) ? empty : create(paramPackageDoc.name().replace('.', '/'));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DocPath forRoot(PackageDoc paramPackageDoc) {
/*  99 */     String str = (paramPackageDoc == null) ? "" : paramPackageDoc.name();
/* 100 */     if (str.isEmpty())
/* 101 */       return empty; 
/* 102 */     return new DocPath(str.replace('.', '/').replaceAll("[^/]+", ".."));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DocPath relativePath(PackageDoc paramPackageDoc1, PackageDoc paramPackageDoc2) {
/* 109 */     return forRoot(paramPackageDoc1).resolve(forPackage(paramPackageDoc2));
/*     */   }
/*     */   
/*     */   protected DocPath(String paramString) {
/* 113 */     this.path = paramString.endsWith("/") ? paramString.substring(0, paramString.length() - 1) : paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 119 */     return (paramObject instanceof DocPath && this.path.equals(((DocPath)paramObject).path));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 125 */     return this.path.hashCode();
/*     */   }
/*     */   
/*     */   public DocPath basename() {
/* 129 */     int i = this.path.lastIndexOf("/");
/* 130 */     return (i == -1) ? this : new DocPath(this.path.substring(i + 1));
/*     */   }
/*     */   
/*     */   public DocPath parent() {
/* 134 */     int i = this.path.lastIndexOf("/");
/* 135 */     return (i == -1) ? empty : new DocPath(this.path.substring(0, i));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DocPath resolve(String paramString) {
/* 142 */     if (paramString == null || paramString.isEmpty())
/* 143 */       return this; 
/* 144 */     if (this.path.isEmpty())
/* 145 */       return new DocPath(paramString); 
/* 146 */     return new DocPath(this.path + "/" + paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DocPath resolve(DocPath paramDocPath) {
/* 153 */     if (paramDocPath == null || paramDocPath.isEmpty())
/* 154 */       return this; 
/* 155 */     if (this.path.isEmpty())
/* 156 */       return paramDocPath; 
/* 157 */     return new DocPath(this.path + "/" + paramDocPath.getPath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DocPath invert() {
/* 165 */     return new DocPath(this.path.replaceAll("[^/]+", ".."));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 172 */     return this.path.isEmpty();
/*     */   }
/*     */   
/*     */   public DocLink fragment(String paramString) {
/* 176 */     return new DocLink(this.path, null, paramString);
/*     */   }
/*     */   
/*     */   public DocLink query(String paramString) {
/* 180 */     return new DocLink(this.path, paramString, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPath() {
/* 189 */     return this.path;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\DocPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */