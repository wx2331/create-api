/*     */ package com.sun.xml.internal.xsom.parser;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.impl.parser.Messages;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
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
/*     */ public class JAXPParser
/*     */   implements XMLParser
/*     */ {
/*     */   private static final String ACCESS_EXTERNAL_SCHEMA = "http://javax.xml.XMLConstants/property/accessExternalSchema";
/*  53 */   private static final Logger LOGGER = Logger.getLogger(JAXPParser.class.getName());
/*     */   
/*     */   private final SAXParserFactory factory;
/*     */   
/*     */   public JAXPParser(SAXParserFactory factory) {
/*  58 */     factory.setNamespaceAware(true);
/*  59 */     this.factory = factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAXPParser() {
/*  68 */     this(SAXParserFactory.newInstance());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(InputSource source, ContentHandler handler, ErrorHandler errorHandler, EntityResolver entityResolver) throws SAXException, IOException {
/*     */     try {
/*  77 */       SAXParser saxParser = allowFileAccess(this.factory.newSAXParser(), false);
/*  78 */       XMLReader reader = new XMLReaderEx(saxParser.getXMLReader());
/*     */       
/*  80 */       reader.setContentHandler(handler);
/*  81 */       if (errorHandler != null)
/*  82 */         reader.setErrorHandler(errorHandler); 
/*  83 */       if (entityResolver != null)
/*  84 */         reader.setEntityResolver(entityResolver); 
/*  85 */       reader.parse(source);
/*  86 */     } catch (ParserConfigurationException e) {
/*     */       
/*  88 */       SAXParseException spe = new SAXParseException(e.getMessage(), null, e);
/*  89 */       errorHandler.fatalError(spe);
/*  90 */       throw spe;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static SAXParser allowFileAccess(SAXParser saxParser, boolean disableSecureProcessing) throws SAXException {
/*  98 */     if (disableSecureProcessing) {
/*  99 */       return saxParser;
/*     */     }
/*     */     
/*     */     try {
/* 103 */       saxParser.setProperty("http://javax.xml.XMLConstants/property/accessExternalSchema", "file");
/* 104 */       LOGGER.log(Level.FINE, Messages.format("JAXPSupportedProperty", new Object[] { "http://javax.xml.XMLConstants/property/accessExternalSchema" }));
/* 105 */     } catch (SAXException ignored) {
/*     */       
/* 107 */       LOGGER.log(Level.CONFIG, Messages.format("JAXPUnsupportedProperty", new Object[] { "http://javax.xml.XMLConstants/property/accessExternalSchema" }), ignored);
/*     */     } 
/* 109 */     return saxParser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class XMLReaderEx
/*     */     extends XMLFilterImpl
/*     */   {
/*     */     private Locator locator;
/*     */ 
/*     */ 
/*     */     
/*     */     XMLReaderEx(XMLReader parent) {
/* 123 */       setParent(parent);
/*     */     }
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
/*     */     public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
/*     */       try {
/* 151 */         InputSource is = null;
/*     */ 
/*     */         
/* 154 */         if (getEntityResolver() != null)
/* 155 */           is = getEntityResolver().resolveEntity(publicId, systemId); 
/* 156 */         if (is != null) return is;
/*     */ 
/*     */ 
/*     */         
/* 160 */         is = new InputSource((new URL(systemId)).openStream());
/* 161 */         is.setSystemId(systemId);
/* 162 */         is.setPublicId(publicId);
/* 163 */         return is;
/* 164 */       } catch (IOException e) {
/*     */ 
/*     */ 
/*     */         
/* 168 */         SAXParseException spe = new SAXParseException(Messages.format("EntityResolutionFailure", new Object[] {
/* 169 */                 systemId, e.toString()
/*     */               }), this.locator, e);
/* 171 */         if (getErrorHandler() != null)
/* 172 */           getErrorHandler().fatalError(spe); 
/* 173 */         throw spe;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void setDocumentLocator(Locator locator) {
/* 179 */       super.setDocumentLocator(locator);
/* 180 */       this.locator = locator;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\parser\JAXPParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */