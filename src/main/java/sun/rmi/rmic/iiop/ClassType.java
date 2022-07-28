/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import sun.rmi.rmic.IndentingWriter;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Environment;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ClassType
/*     */   extends CompoundType
/*     */ {
/*     */   private ClassType parent;
/*     */   
/*     */   public ClassType getSuperclass() {
/*  61 */     return this.parent;
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
/*     */   public void print(IndentingWriter paramIndentingWriter, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) throws IOException {
/*  77 */     if (isInner()) {
/*  78 */       paramIndentingWriter.p("// " + getTypeDescription() + " (INNER)");
/*     */     } else {
/*  80 */       paramIndentingWriter.p("// " + getTypeDescription());
/*     */     } 
/*  82 */     paramIndentingWriter.pln(" (" + getRepositoryID() + ")\n");
/*     */     
/*  84 */     printPackageOpen(paramIndentingWriter, paramBoolean2);
/*     */     
/*  86 */     if (!paramBoolean2) {
/*  87 */       paramIndentingWriter.p("public ");
/*     */     }
/*     */     
/*  90 */     String str = "";
/*  91 */     paramIndentingWriter.p("class " + getTypeName(false, paramBoolean2, false));
/*  92 */     if (printExtends(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3)) {
/*  93 */       str = ",";
/*     */     }
/*  95 */     printImplements(paramIndentingWriter, str, paramBoolean1, paramBoolean2, paramBoolean3);
/*  96 */     paramIndentingWriter.plnI(" {");
/*  97 */     printMembers(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
/*  98 */     paramIndentingWriter.pln();
/*  99 */     printMethods(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
/*     */     
/* 101 */     if (paramBoolean2) {
/* 102 */       paramIndentingWriter.pOln("};");
/*     */     } else {
/* 104 */       paramIndentingWriter.pOln("}");
/*     */     } 
/*     */     
/* 107 */     printPackageClose(paramIndentingWriter, paramBoolean2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void destroy() {
/* 116 */     if (!this.destroyed) {
/* 117 */       super.destroy();
/* 118 */       if (this.parent != null) {
/* 119 */         this.parent.destroy();
/* 120 */         this.parent = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassType(ContextStack paramContextStack, int paramInt, ClassDefinition paramClassDefinition) {
/* 130 */     super(paramContextStack, paramInt, paramClassDefinition);
/* 131 */     if ((paramInt & 0x4000000) == 0 && paramClassDefinition.isInterface()) {
/* 132 */       throw new CompilerError("Not a class");
/*     */     }
/* 134 */     this.parent = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassType(int paramInt, ClassDefinition paramClassDefinition, ContextStack paramContextStack) {
/* 142 */     super(paramContextStack, paramClassDefinition, paramInt);
/*     */     
/* 144 */     if ((paramInt & 0x4000000) == 0 && paramClassDefinition.isInterface()) {
/* 145 */       throw new CompilerError("Not a class");
/*     */     }
/* 147 */     this.parent = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassType(ContextStack paramContextStack, ClassDefinition paramClassDefinition, int paramInt) {
/* 158 */     super(paramContextStack, paramClassDefinition, paramInt);
/* 159 */     if ((paramInt & 0x4000000) == 0 && paramClassDefinition.isInterface()) {
/* 160 */       throw new CompilerError("Not a class");
/*     */     }
/* 162 */     this.parent = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void swapInvalidTypes() {
/* 169 */     super.swapInvalidTypes();
/* 170 */     if (this.parent != null && this.parent.getStatus() != 1) {
/* 171 */       this.parent = (ClassType)getValidType(this.parent);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String addExceptionDescription(String paramString) {
/* 179 */     if (this.isException) {
/* 180 */       if (this.isCheckedException) {
/* 181 */         paramString = paramString + " - Checked Exception";
/*     */       } else {
/* 183 */         paramString = paramString + " - Unchecked Exception";
/*     */       } 
/*     */     }
/* 186 */     return paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean initParents(ContextStack paramContextStack) {
/* 192 */     paramContextStack.setNewContextCode(11);
/* 193 */     BatchEnvironment batchEnvironment = paramContextStack.getEnv();
/*     */ 
/*     */ 
/*     */     
/* 197 */     boolean bool = true;
/*     */     
/*     */     try {
/* 200 */       ClassDeclaration classDeclaration = getClassDefinition().getSuperClass((Environment)batchEnvironment);
/* 201 */       if (classDeclaration != null) {
/* 202 */         ClassDefinition classDefinition = classDeclaration.getClassDefinition((Environment)batchEnvironment);
/* 203 */         this.parent = (ClassType)makeType(classDefinition.getType(), classDefinition, paramContextStack);
/* 204 */         if (this.parent == null) {
/* 205 */           bool = false;
/*     */         }
/*     */       } 
/* 208 */     } catch (ClassNotFound classNotFound) {
/* 209 */       classNotFound(paramContextStack, classNotFound);
/* 210 */       throw new CompilerError("ClassType constructor");
/*     */     } 
/*     */     
/* 213 */     return bool;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\ClassType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */