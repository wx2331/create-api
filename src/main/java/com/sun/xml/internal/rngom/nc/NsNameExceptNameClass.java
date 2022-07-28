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
/*    */ public class NsNameExceptNameClass
/*    */   extends NameClass
/*    */ {
/*    */   private final NameClass nameClass;
/*    */   private final String namespaceURI;
/*    */   
/*    */   public NsNameExceptNameClass(String namespaceURI, NameClass nameClass) {
/* 56 */     this.namespaceURI = namespaceURI;
/* 57 */     this.nameClass = nameClass;
/*    */   }
/*    */   
/*    */   public boolean contains(QName name) {
/* 61 */     return (this.namespaceURI.equals(name.getNamespaceURI()) && 
/* 62 */       !this.nameClass.contains(name));
/*    */   }
/*    */   
/*    */   public int containsSpecificity(QName name) {
/* 66 */     return contains(name) ? 1 : -1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 71 */     if (obj == null || !(obj instanceof NsNameExceptNameClass))
/* 72 */       return false; 
/* 73 */     NsNameExceptNameClass other = (NsNameExceptNameClass)obj;
/* 74 */     return (this.namespaceURI.equals(other.namespaceURI) && this.nameClass
/* 75 */       .equals(other.nameClass));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 80 */     return this.namespaceURI.hashCode() ^ this.nameClass.hashCode();
/*    */   }
/*    */   
/*    */   public <V> V accept(NameClassVisitor<V> visitor) {
/* 84 */     return visitor.visitNsNameExcept(this.namespaceURI, this.nameClass);
/*    */   }
/*    */   
/*    */   public boolean isOpen() {
/* 88 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\nc\NsNameExceptNameClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */