/*     */ package com.sun.tools.javac.api;
/*     */ 
/*     */ import com.sun.source.tree.CompilationUnitTree;
/*     */ import com.sun.source.tree.Tree;
/*     */ import com.sun.source.util.JavacTask;
/*     */ import com.sun.source.util.TaskListener;
/*     */ import com.sun.tools.javac.model.JavacElements;
/*     */ import com.sun.tools.javac.model.JavacTypes;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Locale;
/*     */ import javax.annotation.processing.Processor;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.lang.model.util.Types;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicJavacTask
/*     */   extends JavacTask
/*     */ {
/*     */   protected Context context;
/*     */   private TaskListener taskListener;
/*     */   
/*     */   public static JavacTask instance(Context paramContext) {
/*  61 */     JavacTask javacTask = (JavacTask)paramContext.get(JavacTask.class);
/*  62 */     if (javacTask == null)
/*  63 */       javacTask = new BasicJavacTask(paramContext, true); 
/*  64 */     return javacTask;
/*     */   }
/*     */   
/*     */   public BasicJavacTask(Context paramContext, boolean paramBoolean) {
/*  68 */     this.context = paramContext;
/*  69 */     if (paramBoolean) {
/*  70 */       this.context.put(JavacTask.class, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public Iterable<? extends CompilationUnitTree> parse() throws IOException {
/*  75 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterable<? extends Element> analyze() throws IOException {
/*  80 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterable<? extends JavaFileObject> generate() throws IOException {
/*  85 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTaskListener(TaskListener paramTaskListener) {
/*  90 */     MultiTaskListener multiTaskListener = MultiTaskListener.instance(this.context);
/*  91 */     if (this.taskListener != null)
/*  92 */       multiTaskListener.remove(this.taskListener); 
/*  93 */     if (paramTaskListener != null)
/*  94 */       multiTaskListener.add(paramTaskListener); 
/*  95 */     this.taskListener = paramTaskListener;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addTaskListener(TaskListener paramTaskListener) {
/* 100 */     MultiTaskListener multiTaskListener = MultiTaskListener.instance(this.context);
/* 101 */     multiTaskListener.add(paramTaskListener);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeTaskListener(TaskListener paramTaskListener) {
/* 106 */     MultiTaskListener multiTaskListener = MultiTaskListener.instance(this.context);
/* 107 */     multiTaskListener.remove(paramTaskListener);
/*     */   }
/*     */   
/*     */   public Collection<TaskListener> getTaskListeners() {
/* 111 */     MultiTaskListener multiTaskListener = MultiTaskListener.instance(this.context);
/* 112 */     return multiTaskListener.getTaskListeners();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeMirror getTypeMirror(Iterable<? extends Tree> paramIterable) {
/* 118 */     Tree tree = null;
/* 119 */     for (Tree tree1 : paramIterable)
/* 120 */       tree = tree1; 
/* 121 */     return (TypeMirror)((JCTree)tree).type;
/*     */   }
/*     */ 
/*     */   
/*     */   public Elements getElements() {
/* 126 */     return (Elements)JavacElements.instance(this.context);
/*     */   }
/*     */ 
/*     */   
/*     */   public Types getTypes() {
/* 131 */     return (Types)JavacTypes.instance(this.context);
/*     */   }
/*     */   
/*     */   public void setProcessors(Iterable<? extends Processor> paramIterable) {
/* 135 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public void setLocale(Locale paramLocale) {
/* 139 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public Boolean call() {
/* 143 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Context getContext() {
/* 151 */     return this.context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateContext(Context paramContext) {
/* 159 */     this.context = paramContext;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\api\BasicJavacTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */