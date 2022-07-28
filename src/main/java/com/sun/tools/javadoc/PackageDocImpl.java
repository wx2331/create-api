/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.javac.code.Attribute;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import javax.tools.FileObject;
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
/*     */ public class PackageDocImpl
/*     */   extends DocImpl
/*     */   implements PackageDoc
/*     */ {
/*     */   protected Symbol.PackageSymbol sym;
/*  66 */   private JCTree.JCCompilationUnit tree = null;
/*     */   
/*  68 */   public FileObject docPath = null; private boolean foundDoc; boolean isIncluded = false; public boolean setDocPath = false; private List<ClassDocImpl> allClassesFiltered; private List<ClassDocImpl> allClasses; private String qualifiedName; private boolean checkDocWarningEmitted; void setTree(JCTree paramJCTree) { this.tree = (JCTree.JCCompilationUnit)paramJCTree; }
/*     */   public void setTreePath(TreePath paramTreePath) { super.setTreePath(paramTreePath); checkDoc(); }
/*     */   protected String documentation() { if (this.documentation != null)
/*     */       return this.documentation;  if (this.docPath != null) { try {
/*     */         InputStream inputStream = this.docPath.openInputStream(); this.documentation = readHTMLDocumentation(inputStream, this.docPath);
/*     */       } catch (IOException iOException) {
/*     */         this.documentation = ""; this.env.error(null, "javadoc.File_Read_Error", this.docPath.getName());
/*     */       }  }
/*     */     else
/*     */     { this.documentation = ""; }
/*     */      return this.documentation; }
/*  79 */   public PackageDocImpl(DocEnv paramDocEnv, Symbol.PackageSymbol paramPackageSymbol) { this(paramDocEnv, paramPackageSymbol, (TreePath)null); } private List<ClassDocImpl> getClasses(boolean paramBoolean) { if (this.allClasses != null && !paramBoolean)
/*     */       return this.allClasses;  if (this.allClassesFiltered != null && paramBoolean)
/*     */       return this.allClassesFiltered;  ListBuffer<ClassDocImpl> listBuffer = new ListBuffer(); for (Scope.Entry entry = (this.sym.members()).elems; entry != null; entry = entry.sibling) { if (entry.sym != null) { Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)entry.sym; ClassDocImpl classDocImpl = this.env.getClassDoc(classSymbol); if (classDocImpl != null && !classDocImpl.isSynthetic())
/*     */           classDocImpl.addAllClasses(listBuffer, paramBoolean);  }  }
/*     */      if (paramBoolean)
/*     */       return this.allClassesFiltered = listBuffer.toList();  return this.allClasses = listBuffer.toList(); }
/*     */   public void addAllClassesTo(ListBuffer<ClassDocImpl> paramListBuffer) { paramListBuffer.appendList(getClasses(true)); }
/*  86 */   public PackageDocImpl(DocEnv paramDocEnv, Symbol.PackageSymbol paramPackageSymbol, TreePath paramTreePath) { super(paramDocEnv, paramTreePath);
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
/* 129 */     this.allClassesFiltered = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 135 */     this.allClasses = null;
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
/* 362 */     this.checkDocWarningEmitted = false;
/*     */     this.sym = paramPackageSymbol;
/*     */     this.tree = (paramTreePath == null) ? null : (JCTree.JCCompilationUnit)paramTreePath.getCompilationUnit();
/*     */     this.foundDoc = (this.documentation != null); }
/*     */   public ClassDoc[] allClasses(boolean paramBoolean) { List<ClassDocImpl> list = getClasses(paramBoolean);
/*     */     return (ClassDoc[])list.toArray((Object[])new ClassDocImpl[list.length()]); }
/*     */   public ClassDoc[] allClasses() { return allClasses(true); }
/* 369 */   private void checkDoc() { if (this.foundDoc)
/* 370 */     { if (!this.checkDocWarningEmitted) {
/* 371 */         this.env.warning(null, "javadoc.Multiple_package_comments", name());
/* 372 */         this.checkDocWarningEmitted = true;
/*     */       }  }
/*     */     else
/* 375 */     { this.foundDoc = true; }  }
/*     */   public ClassDoc[] ordinaryClasses() { ListBuffer listBuffer = new ListBuffer(); for (ClassDocImpl classDocImpl : getClasses(true)) { if (classDocImpl.isOrdinaryClass())
/*     */         listBuffer.append(classDocImpl);  }  return (ClassDoc[])listBuffer.toArray((Object[])new ClassDocImpl[listBuffer.length()]); }
/*     */   public ClassDoc[] exceptions() { ListBuffer listBuffer = new ListBuffer(); for (ClassDocImpl classDocImpl : getClasses(true)) { if (classDocImpl.isException())
/*     */         listBuffer.append(classDocImpl);  }  return (ClassDoc[])listBuffer.toArray((Object[])new ClassDocImpl[listBuffer.length()]); }
/*     */   public ClassDoc[] errors() { ListBuffer listBuffer = new ListBuffer(); for (ClassDocImpl classDocImpl : getClasses(true)) { if (classDocImpl.isError())
/*     */         listBuffer.append(classDocImpl);  }  return (ClassDoc[])listBuffer.toArray((Object[])new ClassDocImpl[listBuffer.length()]); }
/*     */   public ClassDoc[] enums() { ListBuffer listBuffer = new ListBuffer(); for (ClassDocImpl classDocImpl : getClasses(true)) { if (classDocImpl.isEnum())
/*     */         listBuffer.append(classDocImpl);  }
/* 384 */      return (ClassDoc[])listBuffer.toArray((Object[])new ClassDocImpl[listBuffer.length()]); } public SourcePosition position() { return (this.tree != null) ? 
/* 385 */       SourcePositionImpl.make(this.tree.sourcefile, this.tree.pos, this.tree.lineMap) : 
/* 386 */       SourcePositionImpl.make(this.docPath, -1, null); }
/*     */ 
/*     */   
/*     */   public ClassDoc[] interfaces() {
/*     */     ListBuffer listBuffer = new ListBuffer();
/*     */     for (ClassDocImpl classDocImpl : getClasses(true)) {
/*     */       if (classDocImpl.isInterface())
/*     */         listBuffer.append(classDocImpl); 
/*     */     } 
/*     */     return (ClassDoc[])listBuffer.toArray((Object[])new ClassDocImpl[listBuffer.length()]);
/*     */   }
/*     */   
/*     */   public AnnotationTypeDoc[] annotationTypes() {
/*     */     ListBuffer listBuffer = new ListBuffer();
/*     */     for (ClassDocImpl classDocImpl : getClasses(true)) {
/*     */       if (classDocImpl.isAnnotationType())
/*     */         listBuffer.append(classDocImpl); 
/*     */     } 
/*     */     return (AnnotationTypeDoc[])listBuffer.toArray((Object[])new AnnotationTypeDocImpl[listBuffer.length()]);
/*     */   }
/*     */   
/*     */   public AnnotationDesc[] annotations() {
/*     */     AnnotationDesc[] arrayOfAnnotationDesc = new AnnotationDesc[this.sym.getRawAttributes().length()];
/*     */     byte b = 0;
/*     */     for (Attribute.Compound compound : this.sym.getRawAttributes())
/*     */       arrayOfAnnotationDesc[b++] = new AnnotationDescImpl(this.env, compound); 
/*     */     return arrayOfAnnotationDesc;
/*     */   }
/*     */   
/*     */   public ClassDoc findClass(String paramString) {
/*     */     for (ClassDocImpl classDocImpl : getClasses(true)) {
/*     */       if (classDocImpl.name().equals(paramString))
/*     */         return classDocImpl; 
/*     */     } 
/*     */     return null;
/*     */   }
/*     */   
/*     */   public boolean isIncluded() {
/*     */     return this.isIncluded;
/*     */   }
/*     */   
/*     */   public String name() {
/*     */     return qualifiedName();
/*     */   }
/*     */   
/*     */   public String qualifiedName() {
/*     */     if (this.qualifiedName == null) {
/*     */       Name name = this.sym.getQualifiedName();
/*     */       this.qualifiedName = name.isEmpty() ? "" : name.toString();
/*     */     } 
/*     */     return this.qualifiedName;
/*     */   }
/*     */   
/*     */   public void setDocPath(FileObject paramFileObject) {
/*     */     this.setDocPath = true;
/*     */     if (paramFileObject == null)
/*     */       return; 
/*     */     if (!paramFileObject.equals(this.docPath)) {
/*     */       this.docPath = paramFileObject;
/*     */       checkDoc();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\PackageDocImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */