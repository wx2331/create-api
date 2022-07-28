/*     */ package com.sun.source.util;
/*     */ 
/*     */ import com.sun.source.tree.CompilationUnitTree;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.tools.JavaFileObject;
/*     */ import jdk.Exported;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Exported
/*     */ public final class TaskEvent
/*     */ {
/*     */   private Kind kind;
/*     */   private JavaFileObject file;
/*     */   private CompilationUnitTree unit;
/*     */   private TypeElement clazz;
/*     */   
/*     */   @Exported
/*     */   public enum Kind
/*     */   {
/*  51 */     PARSE,
/*     */ 
/*     */ 
/*     */     
/*  55 */     ENTER,
/*     */ 
/*     */ 
/*     */     
/*  59 */     ANALYZE,
/*     */ 
/*     */ 
/*     */     
/*  63 */     GENERATE,
/*     */ 
/*     */ 
/*     */     
/*  67 */     ANNOTATION_PROCESSING,
/*     */ 
/*     */ 
/*     */     
/*  71 */     ANNOTATION_PROCESSING_ROUND;
/*     */   }
/*     */   
/*     */   public TaskEvent(Kind paramKind) {
/*  75 */     this(paramKind, null, null, null);
/*     */   }
/*     */   
/*     */   public TaskEvent(Kind paramKind, JavaFileObject paramJavaFileObject) {
/*  79 */     this(paramKind, paramJavaFileObject, null, null);
/*     */   }
/*     */   
/*     */   public TaskEvent(Kind paramKind, CompilationUnitTree paramCompilationUnitTree) {
/*  83 */     this(paramKind, paramCompilationUnitTree.getSourceFile(), paramCompilationUnitTree, null);
/*     */   }
/*     */   
/*     */   public TaskEvent(Kind paramKind, CompilationUnitTree paramCompilationUnitTree, TypeElement paramTypeElement) {
/*  87 */     this(paramKind, paramCompilationUnitTree.getSourceFile(), paramCompilationUnitTree, paramTypeElement);
/*     */   }
/*     */   
/*     */   private TaskEvent(Kind paramKind, JavaFileObject paramJavaFileObject, CompilationUnitTree paramCompilationUnitTree, TypeElement paramTypeElement) {
/*  91 */     this.kind = paramKind;
/*  92 */     this.file = paramJavaFileObject;
/*  93 */     this.unit = paramCompilationUnitTree;
/*  94 */     this.clazz = paramTypeElement;
/*     */   }
/*     */   
/*     */   public Kind getKind() {
/*  98 */     return this.kind;
/*     */   }
/*     */   
/*     */   public JavaFileObject getSourceFile() {
/* 102 */     return this.file;
/*     */   }
/*     */   
/*     */   public CompilationUnitTree getCompilationUnit() {
/* 106 */     return this.unit;
/*     */   }
/*     */   
/*     */   public TypeElement getTypeElement() {
/* 110 */     return this.clazz;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 114 */     return "TaskEvent[" + this.kind + "," + this.file + "," + this.clazz + "]";
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\sourc\\util\TaskEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */