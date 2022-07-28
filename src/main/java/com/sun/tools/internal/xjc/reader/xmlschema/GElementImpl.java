/*    */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSElementDecl;
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
/*    */ final class GElementImpl
/*    */   extends GElement
/*    */ {
/*    */   public final QName tagName;
/*    */   public final XSElementDecl decl;
/*    */   
/*    */   public GElementImpl(QName tagName, XSElementDecl decl) {
/* 56 */     this.tagName = tagName;
/* 57 */     this.decl = decl;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 61 */     return this.tagName.toString();
/*    */   }
/*    */   
/*    */   String getPropertyNameSeed() {
/* 65 */     return this.tagName.getLocalPart();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\GElementImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */