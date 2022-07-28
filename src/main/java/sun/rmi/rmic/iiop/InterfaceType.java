/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import sun.rmi.rmic.IndentingWriter;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.CompilerError;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class InterfaceType
/*     */   extends CompoundType
/*     */ {
/*     */   public void print(IndentingWriter paramIndentingWriter, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) throws IOException {
/*  64 */     if (isInner()) {
/*  65 */       paramIndentingWriter.p("// " + getTypeDescription() + " (INNER)");
/*     */     } else {
/*  67 */       paramIndentingWriter.p("// " + getTypeDescription() + "");
/*     */     } 
/*  69 */     paramIndentingWriter.pln(" (" + getRepositoryID() + ")\n");
/*  70 */     printPackageOpen(paramIndentingWriter, paramBoolean2);
/*     */     
/*  72 */     if (!paramBoolean2) {
/*  73 */       paramIndentingWriter.p("public ");
/*     */     }
/*     */     
/*  76 */     paramIndentingWriter.p("interface " + getTypeName(false, paramBoolean2, false));
/*  77 */     printImplements(paramIndentingWriter, "", paramBoolean1, paramBoolean2, paramBoolean3);
/*  78 */     paramIndentingWriter.plnI(" {");
/*  79 */     printMembers(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
/*  80 */     paramIndentingWriter.pln();
/*  81 */     printMethods(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
/*  82 */     paramIndentingWriter.pln();
/*     */     
/*  84 */     if (paramBoolean2) {
/*  85 */       paramIndentingWriter.pOln("};");
/*     */     } else {
/*  87 */       paramIndentingWriter.pOln("}");
/*     */     } 
/*  89 */     printPackageClose(paramIndentingWriter, paramBoolean2);
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
/*     */   protected InterfaceType(ContextStack paramContextStack, int paramInt, ClassDefinition paramClassDefinition) {
/* 101 */     super(paramContextStack, paramInt, paramClassDefinition);
/*     */     
/* 103 */     if ((paramInt & 0x8000000) == 0 || !paramClassDefinition.isInterface()) {
/* 104 */       throw new CompilerError("Not an interface");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InterfaceType(ContextStack paramContextStack, ClassDefinition paramClassDefinition, int paramInt) {
/* 116 */     super(paramContextStack, paramClassDefinition, paramInt);
/*     */     
/* 118 */     if ((paramInt & 0x8000000) == 0 || !paramClassDefinition.isInterface())
/* 119 */       throw new CompilerError("Not an interface"); 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\InterfaceType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */