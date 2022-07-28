/*    */ package com.sun.xml.internal.rngom.nc;
/*    */ 
/*    */ import javax.xml.namespace.QName;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class AnyNameClass
/*    */   extends NameClass
/*    */ {
/*    */   public boolean contains(QName name) {
/* 55 */     return true;
/*    */   }
/*    */   
/*    */   public int containsSpecificity(QName name) {
/* 59 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 64 */     return (obj == this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 69 */     return AnyNameClass.class.hashCode();
/*    */   }
/*    */   
/*    */   public <V> V accept(NameClassVisitor<V> visitor) {
/* 73 */     return visitor.visitAnyName();
/*    */   }
/*    */   
/*    */   public boolean isOpen() {
/* 77 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\nc\AnyNameClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */