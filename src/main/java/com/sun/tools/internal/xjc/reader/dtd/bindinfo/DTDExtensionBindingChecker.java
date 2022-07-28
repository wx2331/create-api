/*    */ package com.sun.tools.internal.xjc.reader.dtd.bindinfo;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.Options;
/*    */ import com.sun.tools.internal.xjc.reader.AbstractExtensionBindingChecker;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.ErrorHandler;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class DTDExtensionBindingChecker
/*    */   extends AbstractExtensionBindingChecker
/*    */ {
/*    */   public DTDExtensionBindingChecker(String schemaLanguage, Options options, ErrorHandler handler) {
/* 49 */     super(schemaLanguage, options, handler);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean needsToBePruned(String uri) {
/* 57 */     if (uri.equals(this.schemaLanguage))
/* 58 */       return false; 
/* 59 */     if (uri.equals("http://java.sun.com/xml/ns/jaxb"))
/* 60 */       return false; 
/* 61 */     if (uri.equals("http://java.sun.com/xml/ns/jaxb/xjc")) {
/* 62 */       return false;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 67 */     return this.enabledExtensions.contains(uri);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
/* 75 */     if (!isCutting() && 
/* 76 */       !uri.equals("")) {
/*    */       
/* 78 */       checkAndEnable(uri);
/*    */       
/* 80 */       verifyTagName(uri, localName, qName);
/*    */       
/* 82 */       if (needsToBePruned(uri)) {
/* 83 */         startCutting();
/*    */       }
/*    */     } 
/*    */     
/* 87 */     super.startElement(uri, localName, qName, atts);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\dtd\bindinfo\DTDExtensionBindingChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */