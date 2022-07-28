/*    */ package com.sun.xml.internal.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSAnnotation;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.helpers.LocatorImpl;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AnnotationImpl
/*    */   implements XSAnnotation
/*    */ {
/*    */   private Object annotation;
/*    */   private final Locator locator;
/*    */   
/*    */   public Object getAnnotation() {
/* 35 */     return this.annotation;
/*    */   }
/*    */   public Object setAnnotation(Object o) {
/* 38 */     Object r = this.annotation;
/* 39 */     this.annotation = o;
/* 40 */     return r;
/*    */   }
/*    */   
/*    */   public Locator getLocator() {
/* 44 */     return this.locator;
/*    */   }
/*    */   public AnnotationImpl(Object o, Locator _loc) {
/* 47 */     this.annotation = o;
/* 48 */     this.locator = _loc;
/*    */   }
/*    */   
/*    */   public AnnotationImpl() {
/* 52 */     this.locator = NULL_LOCATION;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static class LocatorImplUnmodifiable
/*    */     extends LocatorImpl
/*    */   {
/*    */     private LocatorImplUnmodifiable() {}
/*    */ 
/*    */ 
/*    */     
/*    */     public void setColumnNumber(int columnNumber) {}
/*    */ 
/*    */ 
/*    */     
/*    */     public void setPublicId(String publicId) {}
/*    */ 
/*    */     
/*    */     public void setSystemId(String systemId) {}
/*    */ 
/*    */     
/*    */     public void setLineNumber(int lineNumber) {}
/*    */   }
/*    */ 
/*    */   
/* 78 */   private static final LocatorImplUnmodifiable NULL_LOCATION = new LocatorImplUnmodifiable();
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\AnnotationImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */