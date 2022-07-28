/*     */ package com.sun.tools.doclint;
/*     */ 
/*     */ import com.sun.source.doctree.DocCommentTree;
/*     */ import com.sun.source.util.DocSourcePositions;
/*     */ import com.sun.source.util.DocTrees;
/*     */ import com.sun.source.util.JavacTask;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.javac.model.JavacTypes;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.lang.model.util.Types;
/*     */ import javax.tools.JavaCompiler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Env
/*     */ {
/*     */   final Messages messages;
/*     */   
/*     */   public enum AccessKind
/*     */   {
/*  63 */     PRIVATE,
/*  64 */     PACKAGE,
/*  65 */     PROTECTED,
/*  66 */     PUBLIC;
/*     */     
/*     */     static boolean accepts(String param1String) {
/*  69 */       for (AccessKind accessKind : values()) {
/*  70 */         if (param1String.equals(StringUtils.toLowerCase(accessKind.name()))) return true; 
/*  71 */       }  return false;
/*     */     }
/*     */     
/*     */     static AccessKind of(Set<Modifier> param1Set) {
/*  75 */       if (param1Set.contains(Modifier.PUBLIC))
/*  76 */         return PUBLIC; 
/*  77 */       if (param1Set.contains(Modifier.PROTECTED))
/*  78 */         return PROTECTED; 
/*  79 */       if (param1Set.contains(Modifier.PRIVATE)) {
/*  80 */         return PRIVATE;
/*     */       }
/*  82 */       return PACKAGE;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   int implicitHeaderLevel = 0;
/*     */ 
/*     */   
/*     */   Set<String> customTags;
/*     */ 
/*     */   
/*     */   DocTrees trees;
/*     */   
/*     */   Elements elements;
/*     */   
/*     */   Types types;
/*     */   
/*     */   TypeMirror java_lang_Error;
/*     */   
/*     */   TypeMirror java_lang_RuntimeException;
/*     */   
/*     */   TypeMirror java_lang_Throwable;
/*     */   
/*     */   TypeMirror java_lang_Void;
/*     */   
/*     */   TreePath currPath;
/*     */   
/*     */   Element currElement;
/*     */   
/*     */   DocCommentTree currDocComment;
/*     */   
/*     */   AccessKind currAccess;
/*     */   
/*     */   Set<? extends ExecutableElement> currOverriddenMethods;
/*     */ 
/*     */   
/*     */   Env() {
/* 121 */     this.messages = new Messages(this);
/*     */   }
/*     */   
/*     */   void init(JavacTask paramJavacTask) {
/* 125 */     init(DocTrees.instance((JavaCompiler.CompilationTask)paramJavacTask), paramJavacTask.getElements(), paramJavacTask.getTypes());
/*     */   }
/*     */   
/*     */   void init(DocTrees paramDocTrees, Elements paramElements, Types paramTypes) {
/* 129 */     this.trees = paramDocTrees;
/* 130 */     this.elements = paramElements;
/* 131 */     this.types = paramTypes;
/* 132 */     this.java_lang_Error = paramElements.getTypeElement("java.lang.Error").asType();
/* 133 */     this.java_lang_RuntimeException = paramElements.getTypeElement("java.lang.RuntimeException").asType();
/* 134 */     this.java_lang_Throwable = paramElements.getTypeElement("java.lang.Throwable").asType();
/* 135 */     this.java_lang_Void = paramElements.getTypeElement("java.lang.Void").asType();
/*     */   }
/*     */   
/*     */   void setImplicitHeaders(int paramInt) {
/* 139 */     this.implicitHeaderLevel = paramInt;
/*     */   }
/*     */   
/*     */   void setCustomTags(String paramString) {
/* 143 */     this.customTags = new LinkedHashSet<>();
/* 144 */     for (String str : paramString.split(",")) {
/* 145 */       if (!str.isEmpty()) {
/* 146 */         this.customTags.add(str);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   void setCurrent(TreePath paramTreePath, DocCommentTree paramDocCommentTree) {
/* 152 */     this.currPath = paramTreePath;
/* 153 */     this.currDocComment = paramDocCommentTree;
/* 154 */     this.currElement = this.trees.getElement(this.currPath);
/* 155 */     this.currOverriddenMethods = ((JavacTypes)this.types).getOverriddenMethods(this.currElement);
/*     */     
/* 157 */     AccessKind accessKind = AccessKind.PUBLIC;
/* 158 */     for (TreePath treePath = paramTreePath; treePath != null; treePath = treePath.getParentPath()) {
/* 159 */       Element element = this.trees.getElement(treePath);
/* 160 */       if (element != null && element.getKind() != ElementKind.PACKAGE) {
/* 161 */         accessKind = min(accessKind, AccessKind.of(element.getModifiers()));
/*     */       }
/*     */     } 
/* 164 */     this.currAccess = accessKind;
/*     */   }
/*     */   
/*     */   AccessKind getAccessKind() {
/* 168 */     return this.currAccess;
/*     */   }
/*     */   
/*     */   long getPos(TreePath paramTreePath) {
/* 172 */     return ((JCTree)paramTreePath.getLeaf()).pos;
/*     */   }
/*     */   
/*     */   long getStartPos(TreePath paramTreePath) {
/* 176 */     DocSourcePositions docSourcePositions = this.trees.getSourcePositions();
/* 177 */     return docSourcePositions.getStartPosition(paramTreePath.getCompilationUnit(), paramTreePath.getLeaf());
/*     */   }
/*     */   
/*     */   private <T extends Comparable<T>> T min(T paramT1, T paramT2) {
/* 181 */     return (paramT1 == null) ? paramT2 : ((paramT2 == null) ? paramT1 : (
/*     */       
/* 183 */       (paramT1.compareTo(paramT2) <= 0) ? paramT1 : paramT2));
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclint\Env.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */