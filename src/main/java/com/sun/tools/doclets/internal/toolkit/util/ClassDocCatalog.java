/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassDocCatalog
/*     */ {
/*     */   private Set<String> packageSet;
/*     */   private Map<String, Set<ClassDoc>> allClasses;
/*     */   private Map<String, Set<ClassDoc>> ordinaryClasses;
/*     */   private Map<String, Set<ClassDoc>> exceptions;
/*     */   private Map<String, Set<ClassDoc>> enums;
/*     */   private Map<String, Set<ClassDoc>> annotationTypes;
/*     */   private Map<String, Set<ClassDoc>> errors;
/*     */   private Map<String, Set<ClassDoc>> interfaces;
/*     */   private Configuration configuration;
/*     */   
/*     */   public ClassDocCatalog(ClassDoc[] paramArrayOfClassDoc, Configuration paramConfiguration) {
/* 101 */     init();
/* 102 */     this.configuration = paramConfiguration;
/* 103 */     for (byte b = 0; b < paramArrayOfClassDoc.length; b++) {
/* 104 */       addClassDoc(paramArrayOfClassDoc[b]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDocCatalog() {
/* 113 */     init();
/*     */   }
/*     */   
/*     */   private void init() {
/* 117 */     this.allClasses = new HashMap<>();
/* 118 */     this.ordinaryClasses = new HashMap<>();
/* 119 */     this.exceptions = new HashMap<>();
/* 120 */     this.enums = new HashMap<>();
/* 121 */     this.annotationTypes = new HashMap<>();
/* 122 */     this.errors = new HashMap<>();
/* 123 */     this.interfaces = new HashMap<>();
/* 124 */     this.packageSet = new HashSet<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addClassDoc(ClassDoc paramClassDoc) {
/* 132 */     if (paramClassDoc == null) {
/*     */       return;
/*     */     }
/* 135 */     addClass(paramClassDoc, this.allClasses);
/* 136 */     if (paramClassDoc.isOrdinaryClass()) {
/* 137 */       addClass(paramClassDoc, this.ordinaryClasses);
/* 138 */     } else if (paramClassDoc.isException()) {
/* 139 */       addClass(paramClassDoc, this.exceptions);
/* 140 */     } else if (paramClassDoc.isEnum()) {
/* 141 */       addClass(paramClassDoc, this.enums);
/* 142 */     } else if (paramClassDoc.isAnnotationType()) {
/* 143 */       addClass(paramClassDoc, this.annotationTypes);
/* 144 */     } else if (paramClassDoc.isError()) {
/* 145 */       addClass(paramClassDoc, this.errors);
/* 146 */     } else if (paramClassDoc.isInterface()) {
/* 147 */       addClass(paramClassDoc, this.interfaces);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addClass(ClassDoc paramClassDoc, Map<String, Set<ClassDoc>> paramMap) {
/* 158 */     PackageDoc packageDoc = paramClassDoc.containingPackage();
/* 159 */     if (packageDoc.isIncluded() || (this.configuration.nodeprecated && Util.isDeprecated((Doc)packageDoc))) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 165 */     String str = Util.getPackageName(packageDoc);
/* 166 */     Set<ClassDoc> set = paramMap.get(str);
/* 167 */     if (set == null) {
/* 168 */       this.packageSet.add(str);
/* 169 */       set = new HashSet();
/*     */     } 
/* 171 */     set.add(paramClassDoc);
/* 172 */     paramMap.put(str, set);
/*     */   }
/*     */ 
/*     */   
/*     */   private ClassDoc[] getArray(Map<String, Set<ClassDoc>> paramMap, String paramString) {
/* 177 */     Set set = paramMap.get(paramString);
/* 178 */     if (set == null) {
/* 179 */       return new ClassDoc[0];
/*     */     }
/* 181 */     return (ClassDoc[])set.toArray((Object[])new ClassDoc[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDoc[] allClasses(PackageDoc paramPackageDoc) {
/* 191 */     return paramPackageDoc.isIncluded() ? paramPackageDoc
/* 192 */       .allClasses() : 
/* 193 */       getArray(this.allClasses, Util.getPackageName(paramPackageDoc));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDoc[] allClasses(String paramString) {
/* 203 */     return getArray(this.allClasses, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] packageNames() {
/* 211 */     return this.packageSet.<String>toArray(new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isKnownPackage(String paramString) {
/* 221 */     return this.packageSet.contains(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDoc[] errors(String paramString) {
/* 232 */     return getArray(this.errors, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDoc[] exceptions(String paramString) {
/* 242 */     return getArray(this.exceptions, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDoc[] enums(String paramString) {
/* 252 */     return getArray(this.enums, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDoc[] annotationTypes(String paramString) {
/* 262 */     return getArray(this.annotationTypes, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDoc[] interfaces(String paramString) {
/* 272 */     return getArray(this.interfaces, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDoc[] ordinaryClasses(String paramString) {
/* 282 */     return getArray(this.ordinaryClasses, paramString);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\ClassDocCatalog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */