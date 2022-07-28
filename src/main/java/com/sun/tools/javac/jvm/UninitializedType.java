/*    */ package com.sun.tools.javac.jvm;
/*    */ 
/*    */ import com.sun.tools.javac.code.Type;
/*    */ import com.sun.tools.javac.code.TypeTag;
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
/*    */ class UninitializedType
/*    */   extends Type.DelegatedType
/*    */ {
/*    */   public final int offset;
/*    */   
/*    */   public static UninitializedType uninitializedThis(Type paramType) {
/* 44 */     return new UninitializedType(TypeTag.UNINITIALIZED_THIS, paramType, -1);
/*    */   }
/*    */   
/*    */   public static UninitializedType uninitializedObject(Type paramType, int paramInt) {
/* 48 */     return new UninitializedType(TypeTag.UNINITIALIZED_OBJECT, paramType, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   private UninitializedType(TypeTag paramTypeTag, Type paramType, int paramInt) {
/* 53 */     super(paramTypeTag, paramType);
/* 54 */     this.offset = paramInt;
/*    */   }
/*    */   
/*    */   Type initializedType() {
/* 58 */     return this.qtype;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\jvm\UninitializedType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */