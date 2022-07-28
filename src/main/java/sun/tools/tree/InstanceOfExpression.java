/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Label;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InstanceOfExpression
/*     */   extends BinaryExpression
/*     */ {
/*     */   public InstanceOfExpression(long paramLong, Expression paramExpression1, Expression paramExpression2) {
/*  45 */     super(25, paramLong, Type.tBoolean, paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  52 */     paramVset = this.left.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*  53 */     this.right = new TypeExpression(this.right.where, this.right.toType(paramEnvironment, paramContext));
/*     */     
/*  55 */     if (this.right.type.isType(13) || this.left.type.isType(13))
/*     */     {
/*  57 */       return paramVset;
/*     */     }
/*     */     
/*  60 */     if (!this.right.type.inMask(1536)) {
/*  61 */       paramEnvironment.error(this.right.where, "invalid.arg.type", this.right.type, opNames[this.op]);
/*  62 */       return paramVset;
/*     */     } 
/*     */     try {
/*  65 */       if (!paramEnvironment.explicitCast(this.left.type, this.right.type)) {
/*  66 */         paramEnvironment.error(this.where, "invalid.instanceof", this.left.type, this.right.type);
/*     */       }
/*  68 */     } catch (ClassNotFound classNotFound) {
/*  69 */       paramEnvironment.error(this.where, "class.not.found", classNotFound.name, opNames[this.op]);
/*     */     } 
/*  71 */     return paramVset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/*  78 */     return this.left.inline(paramEnvironment, paramContext);
/*     */   }
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/*  81 */     this.left = this.left.inlineValue(paramEnvironment, paramContext);
/*  82 */     return this;
/*     */   }
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/*  86 */     if (paramContext == null) {
/*  87 */       return 1 + this.left.costInline(paramInt, paramEnvironment, paramContext);
/*     */     }
/*     */     
/*  90 */     ClassDefinition classDefinition = paramContext.field.getClassDefinition();
/*     */ 
/*     */     
/*     */     try {
/*  94 */       if (this.right.type.isType(9) || classDefinition
/*  95 */         .permitInlinedAccess(paramEnvironment, paramEnvironment.getClassDeclaration(this.right.type)))
/*  96 */         return 1 + this.left.costInline(paramInt, paramEnvironment, paramContext); 
/*  97 */     } catch (ClassNotFound classNotFound) {}
/*     */     
/*  99 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 109 */     this.left.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 110 */     if (this.right.type.isType(10)) {
/* 111 */       paramAssembler.add(this.where, 193, paramEnvironment.getClassDeclaration(this.right.type));
/*     */     } else {
/* 113 */       paramAssembler.add(this.where, 193, this.right.type);
/*     */     } 
/*     */   }
/*     */   void codeBranch(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, Label paramLabel, boolean paramBoolean) {
/* 117 */     codeValue(paramEnvironment, paramContext, paramAssembler);
/* 118 */     paramAssembler.add(this.where, paramBoolean ? 154 : 153, paramLabel, paramBoolean);
/*     */   }
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 121 */     this.left.code(paramEnvironment, paramContext, paramAssembler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream) {
/* 128 */     paramPrintStream.print("(" + opNames[this.op] + " ");
/* 129 */     this.left.print(paramPrintStream);
/* 130 */     paramPrintStream.print(" ");
/* 131 */     if (this.right.op == 147) {
/* 132 */       paramPrintStream.print(this.right.type.toString());
/*     */     } else {
/* 134 */       this.right.print(paramPrintStream);
/*     */     } 
/* 136 */     paramPrintStream.print(")");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\InstanceOfExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */