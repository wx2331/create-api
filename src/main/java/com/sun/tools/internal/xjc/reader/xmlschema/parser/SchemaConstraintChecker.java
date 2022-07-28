/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.parser;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.ConsoleErrorReporter;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.api.ErrorListener;
/*     */ import com.sun.tools.internal.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.xml.internal.bind.v2.util.XmlFactory;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.validation.SchemaFactory;
/*     */ import org.w3c.dom.ls.LSInput;
/*     */ import org.w3c.dom.ls.LSResourceResolver;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
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
/*     */ public class SchemaConstraintChecker
/*     */ {
/*     */   public static boolean check(InputSource[] schemas, ErrorReceiver errorHandler, final EntityResolver entityResolver, boolean disableXmlSecurity) {
/*  68 */     ErrorReceiverFilter errorFilter = new ErrorReceiverFilter((ErrorListener)errorHandler);
/*  69 */     boolean hadErrors = false;
/*     */     
/*  71 */     SchemaFactory sf = XmlFactory.createSchemaFactory("http://www.w3.org/2001/XMLSchema", disableXmlSecurity);
/*  72 */     XmlFactory.allowExternalAccess(sf, "all", disableXmlSecurity);
/*  73 */     sf.setErrorHandler((ErrorHandler)errorFilter);
/*  74 */     if (entityResolver != null) {
/*  75 */       sf.setResourceResolver(new LSResourceResolver()
/*     */           {
/*     */             public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI)
/*     */             {
/*     */               try {
/*  80 */                 InputSource is = entityResolver.resolveEntity(namespaceURI, systemId);
/*  81 */                 if (is == null) return null; 
/*  82 */                 return new LSInputSAXWrapper(is);
/*  83 */               } catch (SAXException e) {
/*     */                 
/*  85 */                 return null;
/*  86 */               } catch (IOException e) {
/*     */                 
/*  88 */                 return null;
/*     */               } 
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     try {
/*  95 */       XmlFactory.allowExternalDTDAccess(sf, "all", disableXmlSecurity);
/*  96 */       sf.newSchema(getSchemaSource(schemas, entityResolver));
/*  97 */     } catch (SAXException e) {
/*     */       
/*  99 */       hadErrors = true;
/* 100 */     } catch (OutOfMemoryError e) {
/* 101 */       errorHandler.warning(null, Messages.format("SchemaConstraintChecker.UnableToCheckCorrectness", new Object[0]));
/*     */     } 
/*     */     
/* 104 */     return (!hadErrors && !errorFilter.hadError());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Source[] getSchemaSource(InputSource[] schemas, EntityResolver entityResolver) throws SAXException {
/* 115 */     SAXSource[] sources = new SAXSource[schemas.length];
/* 116 */     for (int i = 0; i < schemas.length; i++) {
/* 117 */       sources[i] = new SAXSource(schemas[i]);
/*     */     }
/*     */     
/* 120 */     return (Source[])sources;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] args) throws IOException {
/* 125 */     InputSource[] sources = new InputSource[args.length];
/* 126 */     for (int i = 0; i < args.length; i++) {
/* 127 */       sources[i] = new InputSource((new File(args[i])).toURL().toExternalForm());
/*     */     }
/* 129 */     check(sources, (ErrorReceiver)new ConsoleErrorReporter(), null, true);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\parser\SchemaConstraintChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */