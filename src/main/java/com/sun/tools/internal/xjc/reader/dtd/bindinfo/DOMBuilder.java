/*    */ package com.sun.tools.internal.xjc.reader.dtd.bindinfo;
/*    */ 
/*    */ import com.sun.xml.internal.bind.marshaller.SAX2DOMEx;
/*    */ import javax.xml.parsers.DocumentBuilderFactory;
/*    */ import javax.xml.parsers.ParserConfigurationException;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class DOMBuilder
/*    */   extends SAX2DOMEx
/*    */ {
/*    */   private Locator locator;
/*    */   
/*    */   public DOMBuilder(DocumentBuilderFactory f) throws ParserConfigurationException {
/* 43 */     super(f);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDocumentLocator(Locator locator) {
/* 48 */     super.setDocumentLocator(locator);
/* 49 */     this.locator = locator;
/*    */   }
/*    */ 
/*    */   
/*    */   public void startElement(String namespace, String localName, String qName, Attributes attrs) {
/* 54 */     super.startElement(namespace, localName, qName, attrs);
/* 55 */     DOMLocator.setLocationInfo(getCurrentElement(), this.locator);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\dtd\bindinfo\DOMBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */