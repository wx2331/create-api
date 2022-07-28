/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
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
/*     */ 
/*     */ 
/*     */ public class CommaExpression
/*     */   extends BinaryExpression
/*     */ {
/*     */   public CommaExpression(long paramLong, Expression paramExpression1, Expression paramExpression2) {
/*  43 */     super(0, paramLong, (paramExpression2 != null) ? paramExpression2.type : Type.tVoid, paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  50 */     paramVset = this.left.check(paramEnvironment, paramContext, paramVset, paramHashtable);
/*  51 */     paramVset = this.right.check(paramEnvironment, paramContext, paramVset, paramHashtable);
/*  52 */     return paramVset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void selectType(Environment paramEnvironment, Context paramContext, int paramInt) {
/*  59 */     this.type = this.right.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Expression simplify() {
/*  66 */     if (this.left == null) {
/*  67 */       return this.right;
/*     */     }
/*  69 */     if (this.right == null) {
/*  70 */       return this.left;
/*     */     }
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/*  79 */     if (this.left != null) {
/*  80 */       this.left = this.left.inline(paramEnvironment, paramContext);
/*     */     }
/*  82 */     if (this.right != null) {
/*  83 */       this.right = this.right.inline(paramEnvironment, paramContext);
/*     */     }
/*  85 */     return simplify();
/*     */   }
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/*  88 */     if (this.left != null) {
/*  89 */       this.left = this.left.inline(paramEnvironment, paramContext);
/*     */     }
/*  91 */     if (this.right != null) {
/*  92 */       this.right = this.right.inlineValue(paramEnvironment, paramContext);
/*     */     }
/*  94 */     return simplify();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int codeLValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 101 */     if (this.right == null)
/*     */     {
/* 103 */       return super.codeLValue(paramEnvironment, paramContext, paramAssembler);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 108 */     if (this.left != null) {
/* 109 */       this.left.code(paramEnvironment, paramContext, paramAssembler);
/*     */     }
/* 111 */     return this.right.codeLValue(paramEnvironment, paramContext, paramAssembler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void codeLoad(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 118 */     if (this.right == null) {
/*     */       
/* 120 */       super.codeLoad(paramEnvironment, paramContext, paramAssembler);
/*     */     } else {
/* 122 */       this.right.codeLoad(paramEnvironment, paramContext, paramAssembler);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void codeStore(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 128 */     if (this.right == null) {
/*     */       
/* 130 */       super.codeStore(paramEnvironment, paramContext, paramAssembler);
/*     */     } else {
/* 132 */       this.right.codeStore(paramEnvironment, paramContext, paramAssembler);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 137 */     if (this.left != null) {
/* 138 */       this.left.code(paramEnvironment, paramContext, paramAssembler);
/*     */     }
/* 140 */     this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/*     */   }
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 143 */     if (this.left != null) {
/* 144 */       this.left.code(paramEnvironment, paramContext, paramAssembler);
/*     */     }
/* 146 */     if (this.right != null)
/* 147 */       this.right.code(paramEnvironment, paramContext, paramAssembler); 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\CommaExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */