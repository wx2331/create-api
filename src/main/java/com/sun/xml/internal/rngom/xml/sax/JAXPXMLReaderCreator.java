/*    */ package com.sun.xml.internal.rngom.xml.sax;
/*    */ 
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import javax.xml.parsers.ParserConfigurationException;
/*    */ import javax.xml.parsers.SAXParserFactory;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXNotRecognizedException;
/*    */ import org.xml.sax.SAXNotSupportedException;
/*    */ import org.xml.sax.XMLReader;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JAXPXMLReaderCreator
/*    */   implements XMLReaderCreator
/*    */ {
/*    */   private final SAXParserFactory spf;
/*    */   
/*    */   public JAXPXMLReaderCreator(SAXParserFactory spf) {
/* 71 */     this.spf = spf;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JAXPXMLReaderCreator() {
/* 79 */     this.spf = SAXParserFactory.newInstance();
/*    */     try {
/* 81 */       this.spf.setNamespaceAware(true);
/* 82 */       this.spf.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
/* 83 */     } catch (ParserConfigurationException ex) {
/* 84 */       Logger.getLogger(JAXPXMLReaderCreator.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 85 */     } catch (SAXNotRecognizedException ex) {
/* 86 */       Logger.getLogger(JAXPXMLReaderCreator.class.getName()).log(Level.SEVERE, (String)null, ex);
/* 87 */     } catch (SAXNotSupportedException ex) {
/* 88 */       Logger.getLogger(JAXPXMLReaderCreator.class.getName()).log(Level.SEVERE, (String)null, ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public XMLReader createXMLReader() throws SAXException {
/*    */     try {
/* 97 */       return this.spf.newSAXParser().getXMLReader();
/* 98 */     } catch (ParserConfigurationException e) {
/* 99 */       throw new SAXException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\xml\sax\JAXPXMLReaderCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */