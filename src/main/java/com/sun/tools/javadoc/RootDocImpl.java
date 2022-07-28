/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Locale;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.StandardJavaFileManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RootDocImpl
/*     */   extends DocImpl
/*     */   implements RootDoc
/*     */ {
/*     */   private List<ClassDocImpl> cmdLineClasses;
/*     */   private List<PackageDocImpl> cmdLinePackages;
/*     */   private List<String[]> options;
/*     */   
/*     */   public RootDocImpl(DocEnv paramDocEnv, List<JCTree.JCClassDecl> paramList, List<String> paramList1, List<String[]> paramList2) {
/*  83 */     super(paramDocEnv, null);
/*  84 */     this.options = paramList2;
/*  85 */     setPackages(paramDocEnv, paramList1);
/*  86 */     setClasses(paramDocEnv, paramList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RootDocImpl(DocEnv paramDocEnv, List<String> paramList, List<String[]> paramList1) {
/*  97 */     super(paramDocEnv, null);
/*  98 */     this.options = paramList1;
/*  99 */     this.cmdLinePackages = List.nil();
/* 100 */     ListBuffer listBuffer = new ListBuffer();
/* 101 */     for (String str : paramList) {
/* 102 */       ClassDocImpl classDocImpl = paramDocEnv.loadClass(str);
/* 103 */       if (classDocImpl == null) {
/* 104 */         paramDocEnv.error(null, "javadoc.class_not_found", str); continue;
/*     */       } 
/* 106 */       listBuffer = listBuffer.append(classDocImpl);
/*     */     } 
/* 108 */     this.cmdLineClasses = listBuffer.toList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setClasses(DocEnv paramDocEnv, List<JCTree.JCClassDecl> paramList) {
/* 119 */     ListBuffer listBuffer = new ListBuffer();
/* 120 */     for (JCTree.JCClassDecl jCClassDecl : paramList) {
/*     */       
/* 122 */       if (paramDocEnv.shouldDocument(jCClassDecl.sym)) {
/* 123 */         ClassDocImpl classDocImpl = paramDocEnv.getClassDoc(jCClassDecl.sym);
/* 124 */         if (classDocImpl != null) {
/* 125 */           classDocImpl.isIncluded = true;
/* 126 */           listBuffer.append(classDocImpl);
/*     */         } 
/*     */       } 
/*     */     } 
/* 130 */     this.cmdLineClasses = listBuffer.toList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setPackages(DocEnv paramDocEnv, List<String> paramList) {
/* 140 */     ListBuffer listBuffer = new ListBuffer();
/* 141 */     for (String str : paramList) {
/* 142 */       PackageDocImpl packageDocImpl = paramDocEnv.lookupPackage(str);
/* 143 */       if (packageDocImpl != null) {
/* 144 */         packageDocImpl.isIncluded = true;
/* 145 */         listBuffer.append(packageDocImpl); continue;
/*     */       } 
/* 147 */       paramDocEnv.warning(null, "main.no_source_files_for_package", str);
/*     */     } 
/*     */     
/* 150 */     this.cmdLinePackages = listBuffer.toList();
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
/*     */   public String[][] options() {
/* 171 */     return (String[][])this.options.toArray((Object[])new String[this.options.length()][]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PackageDoc[] specifiedPackages() {
/* 178 */     return (PackageDoc[])this.cmdLinePackages
/* 179 */       .toArray((Object[])new PackageDocImpl[this.cmdLinePackages.length()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDoc[] specifiedClasses() {
/* 186 */     ListBuffer<ClassDocImpl> listBuffer = new ListBuffer();
/* 187 */     for (ClassDocImpl classDocImpl : this.cmdLineClasses) {
/* 188 */       classDocImpl.addAllClasses(listBuffer, true);
/*     */     }
/* 190 */     return (ClassDoc[])listBuffer.toArray((Object[])new ClassDocImpl[listBuffer.length()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDoc[] classes() {
/* 198 */     ListBuffer<ClassDocImpl> listBuffer = new ListBuffer();
/* 199 */     for (ClassDocImpl classDocImpl : this.cmdLineClasses) {
/* 200 */       classDocImpl.addAllClasses(listBuffer, true);
/*     */     }
/* 202 */     for (PackageDocImpl packageDocImpl : this.cmdLinePackages) {
/* 203 */       packageDocImpl.addAllClassesTo(listBuffer);
/*     */     }
/* 205 */     return (ClassDoc[])listBuffer.toArray((Object[])new ClassDocImpl[listBuffer.length()]);
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
/*     */   public ClassDoc classNamed(String paramString) {
/* 218 */     return this.env.lookupClass(paramString);
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
/*     */   public PackageDoc packageNamed(String paramString) {
/* 230 */     return this.env.lookupPackage(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/* 239 */     return "*RootDocImpl*";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String qualifiedName() {
/* 248 */     return "*RootDocImpl*";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIncluded() {
/* 256 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printError(String paramString) {
/* 265 */     this.env.printError(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printError(SourcePosition paramSourcePosition, String paramString) {
/* 274 */     this.env.printError(paramSourcePosition, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printWarning(String paramString) {
/* 283 */     this.env.printWarning(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printWarning(SourcePosition paramSourcePosition, String paramString) {
/* 292 */     this.env.printWarning(paramSourcePosition, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printNotice(String paramString) {
/* 301 */     this.env.printNotice(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printNotice(SourcePosition paramSourcePosition, String paramString) {
/* 310 */     this.env.printNotice(paramSourcePosition, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JavaFileObject getOverviewPath() {
/* 318 */     for (String[] arrayOfString : this.options) {
/* 319 */       if (arrayOfString[0].equals("-overview") && 
/* 320 */         this.env.fileManager instanceof StandardJavaFileManager) {
/* 321 */         StandardJavaFileManager standardJavaFileManager = (StandardJavaFileManager)this.env.fileManager;
/* 322 */         return standardJavaFileManager.getJavaFileObjects(new String[] { arrayOfString[1] }).iterator().next();
/*     */       } 
/*     */     } 
/*     */     
/* 326 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String documentation() {
/* 334 */     if (this.documentation == null) {
/* 335 */       JavaFileObject javaFileObject = getOverviewPath();
/* 336 */       if (javaFileObject == null) {
/*     */         
/* 338 */         this.documentation = "";
/*     */       } else {
/*     */         
/*     */         try {
/* 342 */           this.documentation = readHTMLDocumentation(javaFileObject
/* 343 */               .openInputStream(), javaFileObject);
/*     */         }
/* 345 */         catch (IOException iOException) {
/* 346 */           this.documentation = "";
/* 347 */           this.env.error(null, "javadoc.File_Read_Error", javaFileObject.getName());
/*     */         } 
/*     */       } 
/*     */     } 
/* 351 */     return this.documentation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SourcePosition position() {
/*     */     JavaFileObject javaFileObject;
/* 361 */     return ((javaFileObject = getOverviewPath()) == null) ? null : 
/*     */       
/* 363 */       SourcePositionImpl.make(javaFileObject, -1, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 370 */     return this.env.doclocale.locale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaFileManager getFileManager() {
/* 377 */     return this.env.fileManager;
/*     */   }
/*     */   
/*     */   public void initDocLint(Collection<String> paramCollection1, Collection<String> paramCollection2) {
/* 381 */     this.env.initDoclint(paramCollection1, paramCollection2);
/*     */   }
/*     */   
/*     */   public JavaScriptScanner initJavaScriptScanner(boolean paramBoolean) {
/* 385 */     return this.env.initJavaScriptScanner(paramBoolean);
/*     */   }
/*     */   
/*     */   public boolean isFunctionalInterface(AnnotationDesc paramAnnotationDesc) {
/* 389 */     return (paramAnnotationDesc.annotationType().qualifiedName().equals(this.env.syms.functionalInterfaceType
/* 390 */         .toString()) && this.env.source.allowLambda());
/*     */   }
/*     */   
/*     */   public boolean showTagMessages() {
/* 394 */     return this.env.showTagMessages();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\RootDocImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */