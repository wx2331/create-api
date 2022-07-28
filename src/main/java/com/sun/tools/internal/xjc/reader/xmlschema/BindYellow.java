/*    */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*    */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*    */ import com.sun.xml.internal.xsom.XSAttributeUse;
/*    */ import com.sun.xml.internal.xsom.XSComplexType;
/*    */ import com.sun.xml.internal.xsom.XSContentType;
/*    */ import com.sun.xml.internal.xsom.XSElementDecl;
/*    */ import com.sun.xml.internal.xsom.XSModelGroup;
/*    */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*    */ import com.sun.xml.internal.xsom.XSParticle;
/*    */ import com.sun.xml.internal.xsom.XSSimpleType;
/*    */ import com.sun.xml.internal.xsom.XSWildcard;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class BindYellow
/*    */   extends ColorBinder
/*    */ {
/*    */   public void complexType(XSComplexType ct) {}
/*    */   
/*    */   public void wildcard(XSWildcard xsWildcard) {
/* 49 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   public void elementDecl(XSElementDecl xsElementDecl) {
/* 54 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   public void simpleType(XSSimpleType xsSimpleType) {
/* 59 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   public void attributeDecl(XSAttributeDecl xsAttributeDecl) {
/* 64 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void attGroupDecl(XSAttGroupDecl xsAttGroupDecl) {
/* 74 */     throw new IllegalStateException();
/*    */   }
/*    */   
/*    */   public void attributeUse(XSAttributeUse use) {
/* 78 */     throw new IllegalStateException();
/*    */   }
/*    */   
/*    */   public void modelGroupDecl(XSModelGroupDecl xsModelGroupDecl) {
/* 82 */     throw new IllegalStateException();
/*    */   }
/*    */   
/*    */   public void modelGroup(XSModelGroup xsModelGroup) {
/* 86 */     throw new IllegalStateException();
/*    */   }
/*    */   
/*    */   public void particle(XSParticle xsParticle) {
/* 90 */     throw new IllegalStateException();
/*    */   }
/*    */   
/*    */   public void empty(XSContentType xsContentType) {
/* 94 */     throw new IllegalStateException();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\BindYellow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */