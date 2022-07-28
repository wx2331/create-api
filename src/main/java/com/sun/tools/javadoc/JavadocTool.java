/*     */ package com.sun.tools.javadoc;
/*     */
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.main.JavaCompiler;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.util.Abort;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class JavadocTool
/*     */   extends JavaCompiler
/*     */ {
/*     */   DocEnv docenv;
/*     */   final Messager messager;
/*     */   final JavadocClassReader javadocReader;
/*     */   final JavadocEnter javadocEnter;
/*     */   final Set<JavaFileObject> uniquefiles;
/*     */
/*     */   protected JavadocTool(Context paramContext) {
/*  78 */     super(paramContext);
/*  79 */     this.messager = Messager.instance0(paramContext);
/*  80 */     this.javadocReader = JavadocClassReader.instance0(paramContext);
/*  81 */     this.javadocEnter = JavadocEnter.instance0(paramContext);
/*  82 */     this.uniquefiles = new HashSet<>();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   protected boolean keepComments() {
/*  89 */     return true;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public static JavadocTool make0(Context paramContext) {
/*  96 */     Messager messager = null;
/*     */
/*     */     try {
/*  99 */       JavadocClassReader.preRegister(paramContext);
/*     */
/*     */
/* 102 */       JavadocEnter.preRegister(paramContext);
/*     */
/*     */
/* 105 */       JavadocMemberEnter.preRegister(paramContext);
/*     */
/*     */
/* 108 */       JavadocTodo.preRegister(paramContext);
/*     */
/*     */
/* 111 */       messager = Messager.instance0(paramContext);
/*     */
/* 113 */       return new JavadocTool(paramContext);
/* 114 */     } catch (Symbol.CompletionFailure completionFailure) {
/* 115 */       messager.error(-1, completionFailure.getMessage(), new Object[0]);
/* 116 */       return null;
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
/*     */   public RootDocImpl getRootDocImpl(String paramString1, String paramString2, ModifierFilter paramModifierFilter, List<String> paramList1, List<String[]> paramList, Iterable<? extends JavaFileObject> paramIterable, boolean paramBoolean1, List<String> paramList2, List<String> paramList3, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) throws IOException {
/* 132 */     this.docenv = DocEnv.instance(this.context);
/* 133 */     this.docenv.showAccess = paramModifierFilter;
/* 134 */     this.docenv.quiet = paramBoolean4;
/* 135 */     this.docenv.breakiterator = paramBoolean1;
/* 136 */     this.docenv.setLocale(paramString1);
/* 137 */     this.docenv.setEncoding(paramString2);
/* 138 */     this.docenv.docClasses = paramBoolean2;
/* 139 */     this.docenv.legacyDoclet = paramBoolean3;
/* 140 */     this.javadocReader.sourceCompleter = paramBoolean2 ? null : this.thisCompleter;
/*     */
/* 142 */     ListBuffer<String> listBuffer = new ListBuffer();
/* 143 */     ListBuffer<JCTree.JCCompilationUnit> listBuffer1 = new ListBuffer();
/* 144 */     ListBuffer<JCTree.JCCompilationUnit> listBuffer2 = new ListBuffer();
/*     */
/*     */     try {
/* 147 */       StandardJavaFileManager standardJavaFileManager = (this.docenv.fileManager instanceof StandardJavaFileManager) ? (StandardJavaFileManager)this.docenv.fileManager : null;
/*     */
/* 149 */       for (List<String> list = paramList1; list.nonEmpty(); list = list.tail) {
/* 150 */         String str = (String)list.head;
/* 151 */         if (!paramBoolean2 && standardJavaFileManager != null && str.endsWith(".java") && (new File(str)).exists()) {
/* 152 */           JavaFileObject javaFileObject = standardJavaFileManager.getJavaFileObjects(new String[] { str }).iterator().next();
/* 153 */           parse(javaFileObject, listBuffer1, true);
/* 154 */         } else if (isValidPackageName(str)) {
/* 155 */           listBuffer = listBuffer.append(str);
/* 156 */         } else if (str.endsWith(".java")) {
/* 157 */           if (standardJavaFileManager == null) {
/* 158 */             throw new IllegalArgumentException();
/*     */           }
/* 160 */           this.docenv.error(null, "main.file_not_found", str);
/*     */         } else {
/* 162 */           this.docenv.error(null, "main.illegal_package_name", str);
/*     */         }
/*     */       }
/* 165 */       for (JavaFileObject javaFileObject : paramIterable) {
/* 166 */         parse(javaFileObject, listBuffer1, true);
/*     */       }
/*     */
/* 169 */       if (!paramBoolean2) {
/*     */
/*     */
/*     */
/* 173 */         Map<String, List<JavaFileObject>> map = searchSubPackages(paramList2, listBuffer, paramList3);
/*     */
/*     */
/* 176 */         for (List list1 = listBuffer.toList(); list1.nonEmpty(); list1 = list1.tail) {
/*     */
/* 178 */           String str = (String)list1.head;
/* 179 */           parsePackageClasses(str, map.get(str), listBuffer2, paramList3);
/*     */         }
/*     */
/* 182 */         if (this.messager.nerrors() != 0) return null;
/*     */
/*     */
/* 185 */         this.docenv.notice("main.Building_tree");
/* 186 */         this.javadocEnter.main(listBuffer1.toList().appendList(listBuffer2.toList()));
/*     */       }
/* 188 */     } catch (Abort abort) {}
/*     */
/* 190 */     if (this.messager.nerrors() != 0) {
/* 191 */       return null;
/*     */     }
/* 193 */     if (paramBoolean2) {
/* 194 */       return new RootDocImpl(this.docenv, paramList1, paramList);
/*     */     }
/* 196 */     return new RootDocImpl(this.docenv, listClasses(listBuffer1.toList()), listBuffer.toList(), paramList);
/*     */   }
/*     */
/*     */
/*     */   boolean isValidPackageName(String paramString) {
/*     */     int i;
/* 202 */     while ((i = paramString.indexOf('.')) != -1) {
/* 203 */       if (!isValidClassName(paramString.substring(0, i))) return false;
/* 204 */       paramString = paramString.substring(i + 1);
/*     */     }
/* 206 */     return isValidClassName(paramString);
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
/*     */   private void parsePackageClasses(String paramString, List<JavaFileObject> paramList, ListBuffer<JCTree.JCCompilationUnit> paramListBuffer, List<String> paramList1) throws IOException {
/* 218 */     if (paramList1.contains(paramString)) {
/*     */       return;
/*     */     }
/*     */
/* 222 */     this.docenv.notice("main.Loading_source_files_for_package", paramString);
/*     */
/* 224 */     if (paramList == null) {
/* 225 */       StandardLocation standardLocation = this.docenv.fileManager.hasLocation(StandardLocation.SOURCE_PATH) ? StandardLocation.SOURCE_PATH : StandardLocation.CLASS_PATH;
/*     */
/* 227 */       ListBuffer listBuffer = new ListBuffer();
/* 228 */       for (JavaFileObject javaFileObject : this.docenv.fileManager.list(standardLocation, paramString,
/* 229 */           EnumSet.of(JavaFileObject.Kind.SOURCE), false)) {
/* 230 */         String str1 = this.docenv.fileManager.inferBinaryName(standardLocation, javaFileObject);
/* 231 */         String str2 = getSimpleName(str1);
/* 232 */         if (isValidClassName(str2)) {
/* 233 */           listBuffer.append(javaFileObject);
/*     */         }
/*     */       }
/* 236 */       paramList = listBuffer.toList();
/*     */     }
/* 238 */     if (paramList.nonEmpty()) {
/* 239 */       for (JavaFileObject javaFileObject : paramList) {
/* 240 */         parse(javaFileObject, paramListBuffer, false);
/*     */       }
/*     */     } else {
/* 243 */       this.messager.warning(Messager.NOPOS, "main.no_source_files_for_package", new Object[] { paramString
/* 244 */             .replace(File.separatorChar, '.') });
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   private void parse(JavaFileObject paramJavaFileObject, ListBuffer<JCTree.JCCompilationUnit> paramListBuffer, boolean paramBoolean) {
/* 250 */     if (this.uniquefiles.add(paramJavaFileObject)) {
/* 251 */       if (paramBoolean)
/* 252 */         this.docenv.notice("main.Loading_source_file", paramJavaFileObject.getName());
/* 253 */       paramListBuffer.append(parse(paramJavaFileObject));
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
/*     */   private Map<String, List<JavaFileObject>> searchSubPackages(List<String> paramList1, ListBuffer<String> paramListBuffer, List<String> paramList2) throws IOException {
/* 266 */     HashMap<Object, Object> hashMap1 = new HashMap<>();
/*     */
/*     */
/* 269 */     HashMap<Object, Object> hashMap2 = new HashMap<>();
/* 270 */     hashMap2.put("", Boolean.valueOf(true));
/* 271 */     for (String str : paramList2) {
/* 272 */       hashMap2.put(str, Boolean.valueOf(false));
/*     */     }
/* 274 */     StandardLocation standardLocation = this.docenv.fileManager.hasLocation(StandardLocation.SOURCE_PATH) ? StandardLocation.SOURCE_PATH : StandardLocation.CLASS_PATH;
/*     */
/*     */
/* 277 */     searchSubPackages(paramList1, (Map)hashMap2, paramListBuffer, (Map)hashMap1, standardLocation,
/*     */
/*     */
/*     */
/* 281 */         EnumSet.of(JavaFileObject.Kind.SOURCE));
/*     */
/* 283 */     return (Map)hashMap1;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private void searchSubPackages(List<String> paramList, Map<String, Boolean> paramMap, ListBuffer<String> paramListBuffer, Map<String, List<JavaFileObject>> paramMap1, StandardLocation paramStandardLocation, Set<JavaFileObject.Kind> paramSet) throws IOException {
/* 292 */     for (String str : paramList) {
/* 293 */       if (!isIncluded(str, paramMap)) {
/*     */         continue;
/*     */       }
/* 296 */       for (JavaFileObject javaFileObject : this.docenv.fileManager.list(paramStandardLocation, str, paramSet, true)) {
/* 297 */         String str1 = this.docenv.fileManager.inferBinaryName(paramStandardLocation, javaFileObject);
/* 298 */         String str2 = getPackageName(str1);
/* 299 */         String str3 = getSimpleName(str1);
/* 300 */         if (isIncluded(str2, paramMap) && isValidClassName(str3)) {
/* 301 */           List<JavaFileObject> list = paramMap1.get(str2);
/* 302 */           list = (list == null) ? List.of(javaFileObject) : list.prepend(javaFileObject);
/* 303 */           paramMap1.put(str2, list);
/* 304 */           if (!paramListBuffer.contains(str2))
/* 305 */             paramListBuffer.add(str2);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   private String getPackageName(String paramString) {
/* 312 */     int i = paramString.lastIndexOf(".");
/* 313 */     return (i == -1) ? "" : paramString.substring(0, i);
/*     */   }
/*     */
/*     */   private String getSimpleName(String paramString) {
/* 317 */     int i = paramString.lastIndexOf(".");
/* 318 */     return (i == -1) ? paramString : paramString.substring(i + 1);
/*     */   }
/*     */
/*     */   private boolean isIncluded(String paramString, Map<String, Boolean> paramMap) {
/* 322 */     Boolean bool = paramMap.get(paramString);
/* 323 */     if (bool == null) {
/* 324 */       bool = Boolean.valueOf(isIncluded(getPackageName(paramString), paramMap));
/* 325 */       paramMap.put(paramString, bool);
/*     */     }
/* 327 */     return bool.booleanValue();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private void searchSubPackage(String paramString, ListBuffer<String> paramListBuffer, List<String> paramList, Collection<File> paramCollection) {
/* 338 */     if (paramList.contains(paramString)) {
/*     */       return;
/*     */     }
/* 341 */     String str = paramString.replace('.', File.separatorChar);
/* 342 */     boolean bool = false;
/* 343 */     for (File file1 : paramCollection) {
/* 344 */       File file2 = new File(file1, str);
/* 345 */       String[] arrayOfString = file2.list();
/*     */
/* 347 */       if (arrayOfString != null) {
/* 348 */         for (String str1 : arrayOfString) {
/* 349 */           if (!bool && (
/* 350 */             isValidJavaSourceFile(str1) ||
/* 351 */             isValidJavaClassFile(str1)) &&
/* 352 */             !paramListBuffer.contains(paramString)) {
/* 353 */             paramListBuffer.append(paramString);
/* 354 */             bool = true;
/* 355 */           } else if (isValidClassName(str1) && (new File(file2, str1))
/* 356 */             .isDirectory()) {
/* 357 */             searchSubPackage(paramString + "." + str1, paramListBuffer, paramList, paramCollection);
/*     */           }
/*     */         }
/*     */       }
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
/*     */   private static boolean isValidJavaClassFile(String paramString) {
/* 372 */     if (!paramString.endsWith(".class")) return false;
/* 373 */     String str = paramString.substring(0, paramString.length() - ".class".length());
/* 374 */     return isValidClassName(str);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private static boolean isValidJavaSourceFile(String paramString) {
/* 384 */     if (!paramString.endsWith(".java")) return false;
/* 385 */     String str = paramString.substring(0, paramString.length() - ".java".length());
/* 386 */     return isValidClassName(str);
/*     */   }
/*     */
/*     */
/*     */
/* 391 */   static final boolean surrogatesSupported = surrogatesSupported();
/*     */   private static boolean surrogatesSupported() {
/*     */     try {
/* 394 */       boolean bool = Character.isHighSurrogate('a');
/* 395 */       return true;
/* 396 */     } catch (NoSuchMethodError noSuchMethodError) {
/* 397 */       return false;
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
/*     */   public static boolean isValidClassName(String paramString) {
/* 409 */     if (paramString.length() < 1) return false;
/* 410 */     if (paramString.equals("package-info")) return true;
/* 411 */     if (surrogatesSupported) {
/* 412 */       int i = paramString.codePointAt(0);
/* 413 */       if (!Character.isJavaIdentifierStart(i))
/* 414 */         return false;
/* 415 */       for (int j = Character.charCount(i); j < paramString.length(); j += Character.charCount(i)) {
/* 416 */         i = paramString.codePointAt(j);
/* 417 */         if (!Character.isJavaIdentifierPart(i))
/* 418 */           return false;
/*     */       }
/*     */     } else {
/* 421 */       if (!Character.isJavaIdentifierStart(paramString.charAt(0)))
/* 422 */         return false;
/* 423 */       for (byte b = 1; b < paramString.length(); b++) {
/* 424 */         if (!Character.isJavaIdentifierPart(paramString.charAt(b)))
/* 425 */           return false;
/*     */       }
/* 427 */     }  return true;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   List<JCTree.JCClassDecl> listClasses(List<JCTree.JCCompilationUnit> paramList) {
/* 434 */     ListBuffer listBuffer = new ListBuffer();
/* 435 */     for (JCTree.JCCompilationUnit jCCompilationUnit : paramList) {
/* 436 */       for (JCTree jCTree : jCCompilationUnit.defs) {
/* 437 */         if (jCTree.hasTag(JCTree.Tag.CLASSDEF))
/* 438 */           listBuffer.append(jCTree);
/*     */       }
/*     */     }
/* 441 */     return listBuffer.toList();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\JavadocTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
