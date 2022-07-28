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
/*    */ public class SimpleNameClass
/*    */   extends NameClass
/*    */ {
/*    */   public final QName name;
/*    */   
/*    */   public SimpleNameClass(QName name) {
/* 55 */     this.name = name;
/*    */   }
/*    */   
/*    */   public SimpleNameClass(String nsUri, String localPart) {
/* 59 */     this(new QName(nsUri, localPart));
/*    */   }
/*    */   
/*    */   public SimpleNameClass(String nsUri, String localPart, String prefix) {
/* 63 */     this(new QName(nsUri, localPart, prefix));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(QName name) {
/* 68 */     return this.name.equals(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public int containsSpecificity(QName name) {
/* 73 */     return contains(name) ? 2 : -1;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 78 */     return this.name.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 83 */     if (obj == null || !(obj instanceof SimpleNameClass)) {
/* 84 */       return false;
/*    */     }
/* 86 */     SimpleNameClass other = (SimpleNameClass)obj;
/* 87 */     return this.name.equals(other.name);
/*    */   }
/*    */ 
/*    */   
/*    */   public <V> V accept(NameClassVisitor<V> visitor) {
/* 92 */     return visitor.visitName(this.name);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isOpen() {
/* 97 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\nc\SimpleNameClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */