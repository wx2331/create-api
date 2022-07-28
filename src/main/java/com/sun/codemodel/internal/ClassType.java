/*    */ package com.sun.codemodel.internal;
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
/*    */ public final class ClassType
/*    */ {
/*    */   final String declarationToken;
/*    */   
/*    */   private ClassType(String token) {
/* 42 */     this.declarationToken = token;
/*    */   }
/*    */   
/* 45 */   public static final ClassType CLASS = new ClassType("class");
/* 46 */   public static final ClassType INTERFACE = new ClassType("interface");
/* 47 */   public static final ClassType ANNOTATION_TYPE_DECL = new ClassType("@interface");
/* 48 */   public static final ClassType ENUM = new ClassType("enum");
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\ClassType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */