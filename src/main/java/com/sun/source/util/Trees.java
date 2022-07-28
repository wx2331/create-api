/*    */ package com.sun.source.util;
/*    */ 
/*    */ import com.sun.source.tree.CatchTree;
/*    */ import com.sun.source.tree.ClassTree;
/*    */ import com.sun.source.tree.CompilationUnitTree;
/*    */ import com.sun.source.tree.MethodTree;
/*    */ import com.sun.source.tree.Scope;
/*    */ import com.sun.source.tree.Tree;
/*    */ import java.lang.reflect.Method;
/*    */ import javax.annotation.processing.ProcessingEnvironment;
/*    */ import javax.lang.model.element.AnnotationMirror;
/*    */ import javax.lang.model.element.AnnotationValue;
/*    */ import javax.lang.model.element.Element;
/*    */ import javax.lang.model.element.ExecutableElement;
/*    */ import javax.lang.model.element.TypeElement;
/*    */ import javax.lang.model.type.DeclaredType;
/*    */ import javax.lang.model.type.ErrorType;
/*    */ import javax.lang.model.type.TypeMirror;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Exported
/*    */ public abstract class Trees
/*    */ {
/*    */   public static Trees instance(JavaCompiler.CompilationTask paramCompilationTask) {
/* 62 */     String str = paramCompilationTask.getClass().getName();
/* 63 */     if (!str.equals("com.sun.tools.javac.api.JavacTaskImpl") && 
/* 64 */       !str.equals("com.sun.tools.javac.api.BasicJavacTask"))
/* 65 */       throw new IllegalArgumentException(); 
/* 66 */     return getJavacTrees(JavaCompiler.CompilationTask.class, paramCompilationTask);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Trees instance(ProcessingEnvironment paramProcessingEnvironment) {
/* 75 */     if (!paramProcessingEnvironment.getClass().getName().equals("com.sun.tools.javac.processing.JavacProcessingEnvironment"))
/* 76 */       throw new IllegalArgumentException(); 
/* 77 */     return getJavacTrees(ProcessingEnvironment.class, paramProcessingEnvironment);
/*    */   }
/*    */   
/*    */   static Trees getJavacTrees(Class<?> paramClass, Object paramObject) {
/*    */     try {
/* 82 */       ClassLoader classLoader = paramObject.getClass().getClassLoader();
/* 83 */       Class<?> clazz = Class.forName("com.sun.tools.javac.api.JavacTrees", false, classLoader);
/* 84 */       paramClass = Class.forName(paramClass.getName(), false, classLoader);
/* 85 */       Method method = clazz.getMethod("instance", new Class[] { paramClass });
/* 86 */       return (Trees)method.invoke(null, new Object[] { paramObject });
/* 87 */     } catch (Throwable throwable) {
/* 88 */       throw new AssertionError(throwable);
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract SourcePositions getSourcePositions();
/*    */   
/*    */   public abstract Tree getTree(Element paramElement);
/*    */   
/*    */   public abstract ClassTree getTree(TypeElement paramTypeElement);
/*    */   
/*    */   public abstract MethodTree getTree(ExecutableElement paramExecutableElement);
/*    */   
/*    */   public abstract Tree getTree(Element paramElement, AnnotationMirror paramAnnotationMirror);
/*    */   
/*    */   public abstract Tree getTree(Element paramElement, AnnotationMirror paramAnnotationMirror, AnnotationValue paramAnnotationValue);
/*    */   
/*    */   public abstract TreePath getPath(CompilationUnitTree paramCompilationUnitTree, Tree paramTree);
/*    */   
/*    */   public abstract TreePath getPath(Element paramElement);
/*    */   
/*    */   public abstract TreePath getPath(Element paramElement, AnnotationMirror paramAnnotationMirror);
/*    */   
/*    */   public abstract TreePath getPath(Element paramElement, AnnotationMirror paramAnnotationMirror, AnnotationValue paramAnnotationValue);
/*    */   
/*    */   public abstract Element getElement(TreePath paramTreePath);
/*    */   
/*    */   public abstract TypeMirror getTypeMirror(TreePath paramTreePath);
/*    */   
/*    */   public abstract Scope getScope(TreePath paramTreePath);
/*    */   
/*    */   public abstract String getDocComment(TreePath paramTreePath);
/*    */   
/*    */   public abstract boolean isAccessible(Scope paramScope, TypeElement paramTypeElement);
/*    */   
/*    */   public abstract boolean isAccessible(Scope paramScope, Element paramElement, DeclaredType paramDeclaredType);
/*    */   
/*    */   public abstract TypeMirror getOriginalType(ErrorType paramErrorType);
/*    */   
/*    */   public abstract void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence, Tree paramTree, CompilationUnitTree paramCompilationUnitTree);
/*    */   
/*    */   public abstract TypeMirror getLub(CatchTree paramCatchTree);
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\sourc\\util\Trees.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */