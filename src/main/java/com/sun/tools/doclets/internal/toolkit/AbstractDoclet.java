/*     */ package com.sun.tools.doclets.internal.toolkit;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.LanguageVersion;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.tools.doclets.formats.html.HtmlDoclet;
/*     */ import com.sun.tools.doclets.internal.toolkit.builders.AbstractBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.builders.BuilderFactory;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.FatalError;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.PackageListWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractDoclet
/*     */ {
/*     */   public Configuration configuration;
/*  52 */   private static final String TOOLKIT_DOCLET_NAME = HtmlDoclet.class
/*  53 */     .getName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isValidDoclet(AbstractDoclet paramAbstractDoclet) {
/*  60 */     if (!paramAbstractDoclet.getClass().getName().equals(TOOLKIT_DOCLET_NAME)) {
/*  61 */       this.configuration.message.error("doclet.Toolkit_Usage_Violation", new Object[] { TOOLKIT_DOCLET_NAME });
/*     */       
/*  63 */       return false;
/*     */     } 
/*  65 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean start(AbstractDoclet paramAbstractDoclet, RootDoc paramRootDoc) {
/*  76 */     this.configuration = configuration();
/*  77 */     this.configuration.root = paramRootDoc;
/*  78 */     if (!isValidDoclet(paramAbstractDoclet)) {
/*  79 */       return false;
/*     */     }
/*     */     try {
/*  82 */       paramAbstractDoclet.startGeneration(paramRootDoc);
/*  83 */     } catch (Fault fault) {
/*  84 */       paramRootDoc.printError(fault.getMessage());
/*  85 */       return false;
/*  86 */     } catch (FatalError fatalError) {
/*  87 */       return false;
/*  88 */     } catch (DocletAbortException docletAbortException) {
/*  89 */       Throwable throwable = docletAbortException.getCause();
/*  90 */       if (throwable != null) {
/*  91 */         if (throwable.getLocalizedMessage() != null) {
/*  92 */           paramRootDoc.printError(throwable.getLocalizedMessage());
/*     */         } else {
/*  94 */           paramRootDoc.printError(throwable.toString());
/*     */         } 
/*     */       }
/*  97 */       return false;
/*  98 */     } catch (Exception exception) {
/*  99 */       exception.printStackTrace();
/* 100 */       return false;
/*     */     } 
/* 102 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LanguageVersion languageVersion() {
/* 110 */     return LanguageVersion.JAVA_1_5;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Configuration configuration();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void startGeneration(RootDoc paramRootDoc) throws Configuration.Fault, Exception {
/* 129 */     if ((paramRootDoc.classes()).length == 0) {
/* 130 */       this.configuration.message
/* 131 */         .error("doclet.No_Public_Classes_To_Document", new Object[0]);
/*     */       return;
/*     */     } 
/* 134 */     this.configuration.setOptions();
/* 135 */     this.configuration.getDocletSpecificMsg().notice("doclet.build_version", new Object[] { this.configuration
/* 136 */           .getDocletSpecificBuildDate() });
/* 137 */     ClassTree classTree = new ClassTree(this.configuration, this.configuration.nodeprecated);
/*     */     
/* 139 */     generateClassFiles(paramRootDoc, classTree);
/* 140 */     Util.copyDocFiles(this.configuration, DocPaths.DOC_FILES);
/*     */     
/* 142 */     PackageListWriter.generate(this.configuration);
/* 143 */     generatePackageFiles(classTree);
/* 144 */     generateProfileFiles();
/*     */     
/* 146 */     generateOtherFiles(paramRootDoc, classTree);
/* 147 */     this.configuration.tagletManager.printReport();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateOtherFiles(RootDoc paramRootDoc, ClassTree paramClassTree) throws Exception {
/* 157 */     BuilderFactory builderFactory = this.configuration.getBuilderFactory();
/* 158 */     AbstractBuilder abstractBuilder1 = builderFactory.getConstantsSummaryBuider();
/* 159 */     abstractBuilder1.build();
/* 160 */     AbstractBuilder abstractBuilder2 = builderFactory.getSerializedFormBuilder();
/* 161 */     abstractBuilder2.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void generateProfileFiles() throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void generatePackageFiles(ClassTree paramClassTree) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void generateClassFiles(ClassDoc[] paramArrayOfClassDoc, ClassTree paramClassTree);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateClassFiles(RootDoc paramRootDoc, ClassTree paramClassTree) {
/* 191 */     generateClassFiles(paramClassTree);
/* 192 */     PackageDoc[] arrayOfPackageDoc = paramRootDoc.specifiedPackages();
/* 193 */     for (byte b = 0; b < arrayOfPackageDoc.length; b++) {
/* 194 */       generateClassFiles(arrayOfPackageDoc[b].allClasses(), paramClassTree);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateClassFiles(ClassTree paramClassTree) {
/* 204 */     String[] arrayOfString = this.configuration.classDocCatalog.packageNames();
/* 205 */     for (byte b = 0; b < arrayOfString.length; 
/* 206 */       b++)
/* 207 */       generateClassFiles(this.configuration.classDocCatalog.allClasses(arrayOfString[b]), paramClassTree); 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\AbstractDoclet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */