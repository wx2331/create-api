/*     */ package com.sun.xml.internal.xsom.impl.parser;
/*     */
/*     */ import com.sun.xml.internal.xsom.parser.XMLParser;
/*     */ import java.io.IOException;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Parser;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ import org.xml.sax.helpers.XMLReaderAdapter;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class SAXParserFactoryAdaptor
/*     */   extends SAXParserFactory
/*     */ {
/*     */   private final XMLParser parser;
/*     */
/*     */   public SAXParserFactoryAdaptor(XMLParser _parser) {
/*  55 */     this.parser = _parser;
/*     */   }
/*     */
/*     */   public SAXParser newSAXParser() throws ParserConfigurationException, SAXException {
/*  59 */     return new SAXParserImpl();
/*     */   }
/*     */
/*     */   public void setFeature(String name, boolean value) {
/*  63 */     throw new UnsupportedOperationException("XSOM parser does not support JAXP features.");
/*     */   }
/*     */
/*     */   public boolean getFeature(String name) {
/*  67 */     return false;
/*     */   }
/*     */
/*     */   private class SAXParserImpl
/*     */     extends SAXParser {
/*  72 */     private final XMLReaderImpl reader = new XMLReaderImpl();
/*     */
/*     */
/*     */
/*     */
/*     */     public Parser getParser() throws SAXException {
/*  78 */       return new XMLReaderAdapter(this.reader);
/*     */     }
/*     */
/*     */     public XMLReader getXMLReader() throws SAXException {
/*  82 */       return this.reader;
/*     */     }
/*     */
/*     */     public boolean isNamespaceAware() {
/*  86 */       return true;
/*     */     }
/*     */
/*     */     public boolean isValidating() {
/*  90 */       return false;
/*     */     }
/*     */
/*     */
/*     */     public void setProperty(String name, Object value) {}
/*     */
/*     */     public Object getProperty(String name) {
/*  97 */       return null;
/*     */     }
/*     */
/*     */     private SAXParserImpl() {} }
/*     */
/*     */   private class XMLReaderImpl extends XMLFilterImpl {
/*     */     public void parse(InputSource input) throws IOException, SAXException {
/* 104 */       SAXParserFactoryAdaptor.this.parser.parse(input, this, this, this);
/*     */     }
/*     */     private XMLReaderImpl() {}
/*     */     public void parse(String systemId) throws IOException, SAXException {
/* 108 */       SAXParserFactoryAdaptor.this.parser.parse(new InputSource(systemId), this, this, this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\SAXParserFactoryAdaptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
