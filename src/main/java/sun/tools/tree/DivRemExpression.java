/*    */ package sun.tools.tree;
/*    */ 
/*    */ import sun.tools.java.Environment;
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
/*    */ public abstract class DivRemExpression
/*    */   extends BinaryArithmeticExpression
/*    */ {
/*    */   public DivRemExpression(int paramInt, long paramLong, Expression paramExpression1, Expression paramExpression2) {
/* 43 */     super(paramInt, paramLong, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/* 52 */     if (this.type.inMask(62)) {
/* 53 */       this.right = this.right.inlineValue(paramEnvironment, paramContext);
/* 54 */       if (this.right.isConstant() && !this.right.equals(0)) {
/*    */         
/* 56 */         this.left = this.left.inline(paramEnvironment, paramContext);
/* 57 */         return this.left;
/*    */       } 
/* 59 */       this.left = this.left.inlineValue(paramEnvironment, paramContext);
/*    */       try {
/* 61 */         return eval().simplify();
/* 62 */       } catch (ArithmeticException arithmeticException) {
/* 63 */         paramEnvironment.error(this.where, "arithmetic.exception");
/* 64 */         return this;
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 69 */     return super.inline(paramEnvironment, paramContext);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\DivRemExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */