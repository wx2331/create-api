/*    */ package sun.tools.tree;
/*    */ 
/*    */ import sun.tools.java.ClassNotFound;
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
/*    */ public class BinaryEqualityExpression
/*    */   extends BinaryExpression
/*    */ {
/*    */   public BinaryEqualityExpression(int paramInt, long paramLong, Expression paramExpression1, Expression paramExpression2) {
/* 41 */     super(paramInt, paramLong, Type.tBoolean, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void selectType(Environment paramEnvironment, Context paramContext, int paramInt) {
/*    */     Type type;
/* 49 */     if ((paramInt & 0x2000) != 0) {
/*    */       return;
/*    */     }
/* 52 */     if ((paramInt & 0x700) != 0) {
/*    */       try {
/* 54 */         if (paramEnvironment.explicitCast(this.left.type, this.right.type) || paramEnvironment
/* 55 */           .explicitCast(this.right.type, this.left.type)) {
/*    */           return;
/*    */         }
/* 58 */         paramEnvironment.error(this.where, "incompatible.type", this.left.type, this.left.type, this.right.type);
/*    */       }
/* 60 */       catch (ClassNotFound classNotFound) {
/* 61 */         paramEnvironment.error(this.where, "class.not.found", classNotFound.name, opNames[this.op]);
/*    */       }  return;
/*    */     } 
/* 64 */     if ((paramInt & 0x80) != 0) {
/* 65 */       type = Type.tDouble;
/* 66 */     } else if ((paramInt & 0x40) != 0) {
/* 67 */       type = Type.tFloat;
/* 68 */     } else if ((paramInt & 0x20) != 0) {
/* 69 */       type = Type.tLong;
/* 70 */     } else if ((paramInt & 0x1) != 0) {
/* 71 */       type = Type.tBoolean;
/*    */     } else {
/* 73 */       type = Type.tInt;
/*    */     } 
/* 75 */     this.left = convert(paramEnvironment, paramContext, type, this.left);
/* 76 */     this.right = convert(paramEnvironment, paramContext, type, this.right);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\BinaryEqualityExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */