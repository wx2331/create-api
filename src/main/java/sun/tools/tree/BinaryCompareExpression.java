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
/*    */ public class BinaryCompareExpression
/*    */   extends BinaryExpression
/*    */ {
/*    */   public BinaryCompareExpression(int paramInt, long paramLong, Expression paramExpression1, Expression paramExpression2) {
/* 41 */     super(paramInt, paramLong, Type.tBoolean, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void selectType(Environment paramEnvironment, Context paramContext, int paramInt) {
/* 48 */     Type type = Type.tInt;
/* 49 */     if ((paramInt & 0x80) != 0) {
/* 50 */       type = Type.tDouble;
/* 51 */     } else if ((paramInt & 0x40) != 0) {
/* 52 */       type = Type.tFloat;
/* 53 */     } else if ((paramInt & 0x20) != 0) {
/* 54 */       type = Type.tLong;
/*    */     } 
/* 56 */     this.left = convert(paramEnvironment, paramContext, type, this.left);
/* 57 */     this.right = convert(paramEnvironment, paramContext, type, this.right);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\BinaryCompareExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */