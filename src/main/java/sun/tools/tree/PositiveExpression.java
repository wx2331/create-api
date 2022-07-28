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
/*    */ 
/*    */ public class PositiveExpression
/*    */   extends UnaryExpression
/*    */ {
/*    */   public PositiveExpression(long paramLong, Expression paramExpression) {
/* 42 */     super(35, paramLong, paramExpression.type, paramExpression);
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
/*    */   Expression simplify() {
/* 65 */     return this.right;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\PositiveExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */