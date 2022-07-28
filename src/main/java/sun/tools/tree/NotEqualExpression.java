/*     */ package sun.tools.tree;
/*     */ 
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Label;
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
/*     */ public class NotEqualExpression
/*     */   extends BinaryEqualityExpression
/*     */ {
/*     */   public NotEqualExpression(long paramLong, Expression paramExpression1, Expression paramExpression2) {
/*  43 */     super(19, paramLong, paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Expression eval(int paramInt1, int paramInt2) {
/*  50 */     return new BooleanExpression(this.where, (paramInt1 != paramInt2));
/*     */   }
/*     */   Expression eval(long paramLong1, long paramLong2) {
/*  53 */     return new BooleanExpression(this.where, (paramLong1 != paramLong2));
/*     */   }
/*     */   Expression eval(float paramFloat1, float paramFloat2) {
/*  56 */     return new BooleanExpression(this.where, (paramFloat1 != paramFloat2));
/*     */   }
/*     */   Expression eval(double paramDouble1, double paramDouble2) {
/*  59 */     return new BooleanExpression(this.where, (paramDouble1 != paramDouble2));
/*     */   }
/*     */   Expression eval(boolean paramBoolean1, boolean paramBoolean2) {
/*  62 */     return new BooleanExpression(this.where, (paramBoolean1 != paramBoolean2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Expression simplify() {
/*  69 */     if (this.left.isConstant() && !this.right.isConstant()) {
/*  70 */       return new NotEqualExpression(this.where, this.right, this.left);
/*     */     }
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void codeBranch(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, Label paramLabel, boolean paramBoolean) {
/*  79 */     this.left.codeValue(paramEnvironment, paramContext, paramAssembler);
/*  80 */     switch (this.left.type.getTypeCode()) {
/*     */       case 0:
/*     */       case 4:
/*  83 */         if (!this.right.equals(0)) {
/*  84 */           this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/*  85 */           paramAssembler.add(this.where, paramBoolean ? 160 : 159, paramLabel, paramBoolean);
/*     */           return;
/*     */         } 
/*     */         break;
/*     */       case 5:
/*  90 */         this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/*  91 */         paramAssembler.add(this.where, 148);
/*     */         break;
/*     */       case 6:
/*  94 */         this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/*  95 */         paramAssembler.add(this.where, 149);
/*     */         break;
/*     */       case 7:
/*  98 */         this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/*  99 */         paramAssembler.add(this.where, 151);
/*     */         break;
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/* 104 */         if (this.right.equals(0)) {
/* 105 */           paramAssembler.add(this.where, paramBoolean ? 199 : 198, paramLabel, paramBoolean);
/*     */         } else {
/* 107 */           this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 108 */           paramAssembler.add(this.where, paramBoolean ? 166 : 165, paramLabel, paramBoolean);
/*     */         } 
/*     */         return;
/*     */       default:
/* 112 */         throw new CompilerError("Unexpected Type");
/*     */     } 
/* 114 */     paramAssembler.add(this.where, paramBoolean ? 154 : 153, paramLabel, paramBoolean);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\NotEqualExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */