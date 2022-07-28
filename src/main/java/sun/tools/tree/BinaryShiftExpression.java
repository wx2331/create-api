/*    */ package sun.tools.tree;
/*    */ 
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
/*    */ public class BinaryShiftExpression
/*    */   extends BinaryExpression
/*    */ {
/*    */   public BinaryShiftExpression(int paramInt, long paramLong, Expression paramExpression1, Expression paramExpression2) {
/* 41 */     super(paramInt, paramLong, paramExpression1.type, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Expression eval() {
/* 52 */     if (this.left.op == 66 && this.right.op == 65) {
/* 53 */       return eval(((LongExpression)this.left).value, ((IntExpression)this.right).value);
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 59 */     return super.eval();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void selectType(Environment paramEnvironment, Context paramContext, int paramInt) {
/* 66 */     if (this.left.type == Type.tLong) {
/* 67 */       this.type = Type.tLong;
/* 68 */     } else if (this.left.type.inMask(62)) {
/* 69 */       this.type = Type.tInt;
/* 70 */       this.left = convert(paramEnvironment, paramContext, this.type, this.left);
/*    */     } else {
/* 72 */       this.type = Type.tError;
/*    */     } 
/* 74 */     if (this.right.type.inMask(62)) {
/* 75 */       this.right = new ConvertExpression(this.where, Type.tInt, this.right);
/*    */     } else {
/* 77 */       this.right = convert(paramEnvironment, paramContext, Type.tInt, this.right);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\BinaryShiftExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */