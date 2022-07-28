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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ArrayType
/*    */   extends Type
/*    */ {
/*    */   Type elemType;
/*    */   
/*    */   ArrayType(String paramString, Type paramType) {
/* 50 */     super(9, paramString);
/* 51 */     this.elemType = paramType;
/*    */   }
/*    */   
/*    */   public Type getElementType() {
/* 55 */     return this.elemType;
/*    */   }
/*    */   
/*    */   public int getArrayDimension() {
/* 59 */     return this.elemType.getArrayDimension() + 1;
/*    */   }
/*    */   
/*    */   public String typeString(String paramString, boolean paramBoolean1, boolean paramBoolean2) {
/* 63 */     return getElementType().typeString(paramString, paramBoolean1, paramBoolean2) + "[]";
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\ArrayType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */