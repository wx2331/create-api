/*    */ package com.sun.tools.internal.xjc.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ForkEntityResolver
/*    */   implements EntityResolver
/*    */ {
/*    */   private final EntityResolver lhs;
/*    */   private final EntityResolver rhs;
/*    */   
/*    */   public ForkEntityResolver(EntityResolver lhs, EntityResolver rhs) {
/* 44 */     this.lhs = lhs;
/* 45 */     this.rhs = rhs;
/*    */   }
/*    */   
/*    */   public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
/* 49 */     InputSource is = this.lhs.resolveEntity(publicId, systemId);
/* 50 */     if (is != null)
/* 51 */       return is; 
/* 52 */     return this.rhs.resolveEntity(publicId, systemId);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xj\\util\ForkEntityResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */