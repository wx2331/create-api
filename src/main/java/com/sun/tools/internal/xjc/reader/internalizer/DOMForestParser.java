/*    */ package com.sun.tools.internal.xjc.reader.internalizer;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.parser.XMLParser;
/*    */ import java.io.IOException;
/*    */ import org.w3c.dom.Document;
/*    */ import org.xml.sax.ContentHandler;
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.ErrorHandler;
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
/*    */ class DOMForestParser
/*    */   implements XMLParser
/*    */ {
/*    */   private final DOMForest forest;
/*    */   private final DOMForestScanner scanner;
/*    */   private final XMLParser fallbackParser;
/*    */   
/*    */   DOMForestParser(DOMForest forest, XMLParser fallbackParser) {
/* 64 */     this.forest = forest;
/* 65 */     this.scanner = new DOMForestScanner(forest);
/* 66 */     this.fallbackParser = fallbackParser;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void parse(InputSource source, ContentHandler contentHandler, ErrorHandler errorHandler, EntityResolver entityResolver) throws SAXException, IOException {
/* 76 */     String systemId = source.getSystemId();
/* 77 */     Document dom = this.forest.get(systemId);
/*    */     
/* 79 */     if (dom == null) {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 85 */       this.fallbackParser.parse(source, contentHandler, errorHandler, entityResolver);
/*    */       
/*    */       return;
/*    */     } 
/* 89 */     this.scanner.scan(dom, contentHandler);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\internalizer\DOMForestParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */