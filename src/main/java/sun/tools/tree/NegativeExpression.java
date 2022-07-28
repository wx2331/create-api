/*    */ package sun.tools.tree;
/*    */ 
/*    */ import sun.tools.asm.Assembler;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.Type;
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
/*    */ public class NegativeExpression
/*    */   extends UnaryExpression
/*    */ {
/*    */   public NegativeExpression(long paramLong, Expression paramExpression) {
/* 42 */     super(36, paramLong, paramExpression.type, paramExpression);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void selectType(Environment paramEnvironment, Context paramContext, int paramInt) {
/* 49 */     if ((paramInt & 0x80) != 0) {
/* 50 */       this.type = Type.tDouble;
/* 51 */     } else if ((paramInt & 0x40) != 0) {
/* 52 */       this.type = Type.tFloat;
/* 53 */     } else if ((paramInt & 0x20) != 0) {
/* 54 */       this.type = Type.tLong;
/*    */     } else {
/* 56 */       this.type = Type.tInt;
/*    */     } 
/* 58 */     this.right = convert(paramEnvironment, paramContext, this.type, this.right);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Expression eval(int paramInt) {
/* 65 */     return new IntExpression(this.where, -paramInt);
/*    */   }
/*    */   Expression eval(long paramLong) {
/* 68 */     return new LongExpression(this.where, -paramLong);
/*    */   }
/*    */   Expression eval(float paramFloat) {
/* 71 */     return new FloatExpression(this.where, -paramFloat);
/*    */   }
/*    */   Expression eval(double paramDouble) {
/* 74 */     return new DoubleExpression(this.where, -paramDouble);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Expression simplify() {
/* 81 */     if (this.right.op == 36) {
/* 82 */       return ((NegativeExpression)this.right).right;
/*    */     }
/* 84 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 91 */     this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 92 */     paramAssembler.add(this.where, 116 + this.type.getTypeCodeOffset());
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\NegativeExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */