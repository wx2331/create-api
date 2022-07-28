/*    */ package com.sun.source.util;
/*    */ 
/*    */ import com.sun.source.tree.CompilationUnitTree;
/*    */ import com.sun.source.tree.Tree;
/*    */ import com.sun.tools.javac.api.BasicJavacTask;
/*    */ import com.sun.tools.javac.processing.JavacProcessingEnvironment;
/*    */ import com.sun.tools.javac.util.Context;
/*    */ import java.io.IOException;
/*    */ import javax.annotation.processing.ProcessingEnvironment;
/*    */ import javax.lang.model.element.Element;
/*    */ import javax.lang.model.type.TypeMirror;
/*    */ import javax.lang.model.util.Elements;
/*    */ import javax.lang.model.util.Types;
/*    */ import javax.tools.JavaCompiler;
/*    */ import javax.tools.JavaFileObject;
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
/*    */ public abstract class JavacTask
/*    */   implements JavaCompiler.CompilationTask
/*    */ {
/*    */   public static JavacTask instance(ProcessingEnvironment paramProcessingEnvironment) {
/* 64 */     if (!paramProcessingEnvironment.getClass().getName().equals("com.sun.tools.javac.processing.JavacProcessingEnvironment"))
/*    */     {
/* 66 */       throw new IllegalArgumentException(); } 
/* 67 */     Context context = ((JavacProcessingEnvironment)paramProcessingEnvironment).getContext();
/* 68 */     JavacTask javacTask = (JavacTask)context.get(JavacTask.class);
/* 69 */     return (javacTask != null) ? javacTask : (JavacTask)new BasicJavacTask(context, true);
/*    */   }
/*    */   
/*    */   public abstract Iterable<? extends CompilationUnitTree> parse() throws IOException;
/*    */   
/*    */   public abstract Iterable<? extends Element> analyze() throws IOException;
/*    */   
/*    */   public abstract Iterable<? extends JavaFileObject> generate() throws IOException;
/*    */   
/*    */   public abstract void setTaskListener(TaskListener paramTaskListener);
/*    */   
/*    */   public abstract void addTaskListener(TaskListener paramTaskListener);
/*    */   
/*    */   public abstract void removeTaskListener(TaskListener paramTaskListener);
/*    */   
/*    */   public abstract TypeMirror getTypeMirror(Iterable<? extends Tree> paramIterable);
/*    */   
/*    */   public abstract Elements getElements();
/*    */   
/*    */   public abstract Types getTypes();
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\sourc\\util\JavacTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */