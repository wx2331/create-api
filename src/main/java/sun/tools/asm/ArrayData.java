/*    */ package sun.tools.asm;
/*    */ 
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
/*    */ public final class ArrayData
/*    */ {
/*    */   Type type;
/*    */   int nargs;
/*    */   
/*    */   public ArrayData(Type paramType, int paramInt) {
/* 41 */     this.type = paramType;
/* 42 */     this.nargs = paramInt;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\asm\ArrayData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */