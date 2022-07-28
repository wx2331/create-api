/*    */ package sun.tools.java;
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
/*    */ public class BinaryExceptionHandler
/*    */ {
/*    */   public int startPC;
/*    */   public int endPC;
/*    */   public int handlerPC;
/*    */   public ClassDeclaration exceptionClass;
/*    */   
/*    */   BinaryExceptionHandler(int paramInt1, int paramInt2, int paramInt3, ClassDeclaration paramClassDeclaration) {
/* 44 */     this.startPC = paramInt1;
/* 45 */     this.endPC = paramInt2;
/* 46 */     this.handlerPC = paramInt3;
/* 47 */     this.exceptionClass = paramClassDeclaration;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\BinaryExceptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */