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
/*     */ public class GreaterExpression
/*     */   extends BinaryCompareExpression
/*     */ {
/*     */   public GreaterExpression(long paramLong, Expression paramExpression1, Expression paramExpression2) {
/*  43 */     super(22, paramLong, paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Expression eval(int paramInt1, int paramInt2) {
/*  50 */     return new BooleanExpression(this.where, (paramInt1 > paramInt2));
/*     */   }
/*     */   Expression eval(long paramLong1, long paramLong2) {
/*  53 */     return new BooleanExpression(this.where, (paramLong1 > paramLong2));
/*     */   }
/*     */   Expression eval(float paramFloat1, float paramFloat2) {
/*  56 */     return new BooleanExpression(this.where, (paramFloat1 > paramFloat2));
/*     */   }
/*     */   Expression eval(double paramDouble1, double paramDouble2) {
/*  59 */     return new BooleanExpression(this.where, (paramDouble1 > paramDouble2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Expression simplify() {
/*  66 */     if (this.left.isConstant() && !this.right.isConstant()) {
/*  67 */       return new LessExpression(this.where, this.right, this.left);
/*     */     }
/*  69 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void codeBranch(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, Label paramLabel, boolean paramBoolean) {
/*  76 */     this.left.codeValue(paramEnvironment, paramContext, paramAssembler);
/*  77 */     switch (this.left.type.getTypeCode()) {
/*     */       case 4:
/*  79 */         if (!this.right.equals(0)) {
/*  80 */           this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/*  81 */           paramAssembler.add(this.where, paramBoolean ? 163 : 164, paramLabel, paramBoolean);
/*     */           return;
/*     */         } 
/*     */         break;
/*     */       case 5:
/*  86 */         this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/*  87 */         paramAssembler.add(this.where, 148);
/*     */         break;
/*     */       case 6:
/*  90 */         this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/*  91 */         paramAssembler.add(this.where, 149);
/*     */         break;
/*     */       case 7:
/*  94 */         this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/*  95 */         paramAssembler.add(this.where, 151);
/*     */         break;
/*     */       default:
/*  98 */         throw new CompilerError("Unexpected Type");
/*     */     } 
/* 100 */     paramAssembler.add(this.where, paramBoolean ? 157 : 158, paramLabel, paramBoolean);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\GreaterExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */