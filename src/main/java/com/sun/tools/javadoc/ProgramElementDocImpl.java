/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.javac.code.Attribute;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.util.Position;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.text.CollationKey;
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
/*     */ public abstract class ProgramElementDocImpl
/*     */   extends DocImpl
/*     */   implements ProgramElementDoc
/*     */ {
/*     */   private final Symbol sym;
/*  64 */   JCTree tree = null;
/*  65 */   Position.LineMap lineMap = null;
/*     */ 
/*     */ 
/*     */   
/*  69 */   private int modifiers = -1;
/*     */   
/*     */   protected ProgramElementDocImpl(DocEnv paramDocEnv, Symbol paramSymbol, TreePath paramTreePath) {
/*  72 */     super(paramDocEnv, paramTreePath);
/*  73 */     this.sym = paramSymbol;
/*  74 */     if (paramTreePath != null) {
/*  75 */       this.tree = (JCTree)paramTreePath.getLeaf();
/*  76 */       this.lineMap = ((JCTree.JCCompilationUnit)paramTreePath.getCompilationUnit()).lineMap;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void setTreePath(TreePath paramTreePath) {
/*  82 */     super.setTreePath(paramTreePath);
/*  83 */     this.tree = (JCTree)paramTreePath.getLeaf();
/*  84 */     this.lineMap = ((JCTree.JCCompilationUnit)paramTreePath.getCompilationUnit()).lineMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Symbol.ClassSymbol getContainingClass();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract long getFlags();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getModifiers() {
/* 101 */     if (this.modifiers == -1) {
/* 102 */       this.modifiers = DocEnv.translateModifiers(getFlags());
/*     */     }
/* 104 */     return this.modifiers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDoc containingClass() {
/* 114 */     if (getContainingClass() == null) {
/* 115 */       return null;
/*     */     }
/* 117 */     return this.env.getClassDoc(getContainingClass());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PackageDoc containingPackage() {
/* 125 */     return this.env.getPackageDoc(getContainingClass().packge());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int modifierSpecifier() {
/* 134 */     int i = getModifiers();
/* 135 */     if (isMethod() && containingClass().isInterface())
/*     */     {
/* 137 */       return i & 0xFFFFFBFF; } 
/* 138 */     return i;
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
/*     */   public String modifiers() {
/* 152 */     int i = getModifiers();
/* 153 */     if (isAnnotationTypeElement() || (
/* 154 */       isMethod() && containingClass().isInterface()))
/*     */     {
/* 156 */       return Modifier.toString(i & 0xFFFFFBFF);
/*     */     }
/* 158 */     return Modifier.toString(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationDesc[] annotations() {
/* 167 */     AnnotationDesc[] arrayOfAnnotationDesc = new AnnotationDesc[this.sym.getRawAttributes().length()];
/* 168 */     byte b = 0;
/* 169 */     for (Attribute.Compound compound : this.sym.getRawAttributes()) {
/* 170 */       arrayOfAnnotationDesc[b++] = new AnnotationDescImpl(this.env, compound);
/*     */     }
/* 172 */     return arrayOfAnnotationDesc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPublic() {
/* 179 */     int i = getModifiers();
/* 180 */     return Modifier.isPublic(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isProtected() {
/* 187 */     int i = getModifiers();
/* 188 */     return Modifier.isProtected(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrivate() {
/* 195 */     int i = getModifiers();
/* 196 */     return Modifier.isPrivate(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPackagePrivate() {
/* 203 */     return (!isPublic() && !isPrivate() && !isProtected());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 210 */     int i = getModifiers();
/* 211 */     return Modifier.isStatic(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/* 218 */     int i = getModifiers();
/* 219 */     return Modifier.isFinal(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CollationKey generateKey() {
/* 226 */     String str = name();
/*     */     
/* 228 */     return this.env.doclocale.collator.getCollationKey(str);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\ProgramElementDocImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */