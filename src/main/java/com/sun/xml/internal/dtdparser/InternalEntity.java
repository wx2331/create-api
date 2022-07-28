/*    */ package com.sun.xml.internal.dtdparser;
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
/*    */ final class InternalEntity
/*    */   extends EntityDecl
/*    */ {
/*    */   char[] buf;
/*    */   
/*    */   InternalEntity(String name, char[] value) {
/* 31 */     this.name = name;
/* 32 */     this.buf = value;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\dtdparser\InternalEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */