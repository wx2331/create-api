/*    */ package com.sun.xml.internal.dtdparser;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
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
/*    */ final class ExternalEntity
/*    */   extends EntityDecl
/*    */ {
/*    */   String systemId;
/*    */   String publicId;
/*    */   String notation;
/*    */   
/*    */   public ExternalEntity(InputEntity in) {}
/*    */   
/*    */   public InputSource getInputSource(EntityResolver r) throws IOException, SAXException {
/* 48 */     InputSource retval = r.resolveEntity(this.publicId, this.systemId);
/*    */     
/* 50 */     if (retval == null)
/* 51 */       retval = Resolver.createInputSource(new URL(this.systemId), false); 
/* 52 */     return retval;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\dtdparser\ExternalEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */