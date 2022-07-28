/*    */ package com.sun.xml.internal.xsom.impl.util;
/*    */ 
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.InputSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResourceEntityResolver
/*    */   implements EntityResolver
/*    */ {
/*    */   private final Class base;
/*    */   
/*    */   public ResourceEntityResolver(Class _base) {
/* 33 */     this.base = _base;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public InputSource resolveEntity(String publicId, String systemId) {
/* 39 */     return new InputSource(this.base.getResourceAsStream(systemId));
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\imp\\util\ResourceEntityResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */