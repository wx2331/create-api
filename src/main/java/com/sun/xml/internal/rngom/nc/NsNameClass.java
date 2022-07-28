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
/*    */ public final class NsNameClass
/*    */   extends NameClass
/*    */ {
/*    */   private final String namespaceUri;
/*    */   
/*    */   public NsNameClass(String namespaceUri) {
/* 55 */     this.namespaceUri = namespaceUri;
/*    */   }
/*    */   
/*    */   public boolean contains(QName name) {
/* 59 */     return this.namespaceUri.equals(name.getNamespaceURI());
/*    */   }
/*    */   
/*    */   public int containsSpecificity(QName name) {
/* 63 */     return contains(name) ? 1 : -1;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 68 */     return this.namespaceUri.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 73 */     if (obj == null || !(obj instanceof NsNameClass))
/* 74 */       return false; 
/* 75 */     return this.namespaceUri.equals(((NsNameClass)obj).namespaceUri);
/*    */   }
/*    */   
/*    */   public <V> V accept(NameClassVisitor<V> visitor) {
/* 79 */     return visitor.visitNsName(this.namespaceUri);
/*    */   }
/*    */   
/*    */   public boolean isOpen() {
/* 83 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\nc\NsNameClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */