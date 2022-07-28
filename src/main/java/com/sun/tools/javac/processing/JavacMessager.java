/*     */ package com.sun.tools.javac.processing;
/*     */ 
/*     */ import com.sun.tools.javac.model.JavacElements;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.JCDiagnostic;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Pair;
/*     */ import javax.annotation.processing.Messager;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.AnnotationValue;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.tools.JavaFileObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavacMessager
/*     */   implements Messager
/*     */ {
/*     */   Log log;
/*     */   JavacProcessingEnvironment processingEnv;
/*  48 */   int errorCount = 0;
/*  49 */   int warningCount = 0;
/*     */   
/*     */   JavacMessager(Context paramContext, JavacProcessingEnvironment paramJavacProcessingEnvironment) {
/*  52 */     this.log = Log.instance(paramContext);
/*  53 */     this.processingEnv = paramJavacProcessingEnvironment;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence) {
/*  59 */     printMessage(paramKind, paramCharSequence, null, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence, Element paramElement) {
/*  64 */     printMessage(paramKind, paramCharSequence, paramElement, null, null);
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
/*     */   public void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence, Element paramElement, AnnotationMirror paramAnnotationMirror) {
/*  78 */     printMessage(paramKind, paramCharSequence, paramElement, paramAnnotationMirror, null);
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
/*     */   public void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence, Element paramElement, AnnotationMirror paramAnnotationMirror, AnnotationValue paramAnnotationValue) {
/*  94 */     JavaFileObject javaFileObject1 = null;
/*  95 */     JavaFileObject javaFileObject2 = null;
/*  96 */     JCDiagnostic.DiagnosticPosition diagnosticPosition = null;
/*  97 */     JavacElements javacElements = this.processingEnv.getElementUtils();
/*  98 */     Pair pair = javacElements.getTreeAndTopLevel(paramElement, paramAnnotationMirror, paramAnnotationValue);
/*  99 */     if (pair != null) {
/* 100 */       javaFileObject2 = ((JCTree.JCCompilationUnit)pair.snd).sourcefile;
/* 101 */       if (javaFileObject2 != null) {
/*     */         
/* 103 */         javaFileObject1 = this.log.useSource(javaFileObject2);
/* 104 */         diagnosticPosition = ((JCTree)pair.fst).pos();
/*     */       } 
/*     */     }  try {
/*     */       boolean bool;
/* 108 */       switch (paramKind) {
/*     */         case ERROR:
/* 110 */           this.errorCount++;
/* 111 */           bool = this.log.multipleErrors;
/* 112 */           this.log.multipleErrors = true;
/*     */           try {
/* 114 */             this.log.error(diagnosticPosition, "proc.messager", new Object[] { paramCharSequence.toString() });
/*     */           } finally {
/* 116 */             this.log.multipleErrors = bool;
/*     */           } 
/*     */           break;
/*     */         
/*     */         case WARNING:
/* 121 */           this.warningCount++;
/* 122 */           this.log.warning(diagnosticPosition, "proc.messager", new Object[] { paramCharSequence.toString() });
/*     */           break;
/*     */         
/*     */         case MANDATORY_WARNING:
/* 126 */           this.warningCount++;
/* 127 */           this.log.mandatoryWarning(diagnosticPosition, "proc.messager", new Object[] { paramCharSequence.toString() });
/*     */           break;
/*     */         
/*     */         default:
/* 131 */           this.log.note(diagnosticPosition, "proc.messager", new Object[] { paramCharSequence.toString() });
/*     */           break;
/*     */       } 
/*     */     
/*     */     } finally {
/* 136 */       if (javaFileObject2 != null) {
/* 137 */         this.log.useSource(javaFileObject1);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printError(String paramString) {
/* 147 */     printMessage(Diagnostic.Kind.ERROR, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printWarning(String paramString) {
/* 156 */     printMessage(Diagnostic.Kind.WARNING, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printNotice(String paramString) {
/* 164 */     printMessage(Diagnostic.Kind.NOTE, paramString);
/*     */   }
/*     */   
/*     */   public boolean errorRaised() {
/* 168 */     return (this.errorCount > 0);
/*     */   }
/*     */   
/*     */   public int errorCount() {
/* 172 */     return this.errorCount;
/*     */   }
/*     */   
/*     */   public int warningCount() {
/* 176 */     return this.warningCount;
/*     */   }
/*     */   
/*     */   public void newRound(Context paramContext) {
/* 180 */     this.log = Log.instance(paramContext);
/* 181 */     this.errorCount = 0;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 185 */     return "javac Messager";
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\processing\JavacMessager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */