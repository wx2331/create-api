/*    */ package com.sun.source.util;
/*    */ 
/*    */ import com.sun.source.doctree.DocCommentTree;
/*    */ import com.sun.source.doctree.DocTree;
/*    */ import com.sun.source.tree.CompilationUnitTree;
/*    */ import javax.annotation.processing.ProcessingEnvironment;
/*    */ import javax.lang.model.element.Element;
/*    */ import javax.tools.Diagnostic;
/*    */ import javax.tools.JavaCompiler;
/*    */ import jdk.Exported;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Exported
/*    */ public abstract class DocTrees
/*    */   extends Trees
/*    */ {
/*    */   public static DocTrees instance(JavaCompiler.CompilationTask paramCompilationTask) {
/* 48 */     return (DocTrees)Trees.instance(paramCompilationTask);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static DocTrees instance(ProcessingEnvironment paramProcessingEnvironment) {
/* 57 */     if (!paramProcessingEnvironment.getClass().getName().equals("com.sun.tools.javac.processing.JavacProcessingEnvironment"))
/* 58 */       throw new IllegalArgumentException(); 
/* 59 */     return (DocTrees)getJavacTrees(ProcessingEnvironment.class, paramProcessingEnvironment);
/*    */   }
/*    */   
/*    */   public abstract DocCommentTree getDocCommentTree(TreePath paramTreePath);
/*    */   
/*    */   public abstract Element getElement(DocTreePath paramDocTreePath);
/*    */   
/*    */   public abstract DocSourcePositions getSourcePositions();
/*    */   
/*    */   public abstract void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence, DocTree paramDocTree, DocCommentTree paramDocCommentTree, CompilationUnitTree paramCompilationUnitTree);
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\sourc\\util\DocTrees.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */