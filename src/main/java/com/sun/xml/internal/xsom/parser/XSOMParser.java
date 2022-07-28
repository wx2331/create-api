/*     */ package com.sun.xml.internal.xsom.parser;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.internal.xsom.impl.parser.ParserContext;
/*     */ import com.sun.xml.internal.xsom.impl.parser.state.NGCCHandler;
/*     */ import com.sun.xml.internal.xsom.impl.parser.state.Schema;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.net.URL;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.xml.sax.ContentHandler;
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
/*     */ public final class XSOMParser
/*     */ {
/*     */   private EntityResolver entityResolver;
/*     */   private ErrorHandler userErrorHandler;
/*     */   private AnnotationParserFactory apFactory;
/*     */   private final ParserContext context;
/*     */   
/*     */   public XSOMParser() {
/*  72 */     this(new JAXPParser());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XSOMParser(SAXParserFactory factory) {
/*  84 */     this(new JAXPParser(factory));
/*     */   }
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
/*     */   public XSOMParser(XMLParser parser) {
/*  98 */     this.context = new ParserContext(this, parser);
/*     */   }
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
/*     */   public void parse(InputStream is) throws SAXException {
/* 111 */     parse(new InputSource(is));
/*     */   }
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
/*     */   public void parse(Reader reader) throws SAXException {
/* 124 */     parse(new InputSource(reader));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(File schema) throws SAXException, IOException {
/* 131 */     parse(schema.toURL());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(URL url) throws SAXException {
/* 138 */     parse(url.toExternalForm());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(String systemId) throws SAXException {
/* 145 */     parse(new InputSource(systemId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(InputSource source) throws SAXException {
/* 156 */     this.context.parse(source);
/*     */   }
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
/*     */   public ContentHandler getParserHandler() {
/* 180 */     NGCCRuntimeEx runtime = this.context.newNGCCRuntime();
/* 181 */     Schema s = new Schema(runtime, false, null);
/* 182 */     runtime.setRootHandler((NGCCHandler)s);
/* 183 */     return (ContentHandler)runtime;
/*     */   }
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
/*     */   public XSSchemaSet getResult() throws SAXException {
/* 199 */     return this.context.getResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<SchemaDocument> getDocuments() {
/* 210 */     return new HashSet<>(this.context.parsedDocuments.keySet());
/*     */   }
/*     */   
/*     */   public EntityResolver getEntityResolver() {
/* 214 */     return this.entityResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityResolver(EntityResolver resolver) {
/* 221 */     this.entityResolver = resolver;
/*     */   }
/*     */   public ErrorHandler getErrorHandler() {
/* 224 */     return this.userErrorHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorHandler(ErrorHandler errorHandler) {
/* 231 */     this.userErrorHandler = errorHandler;
/*     */   }
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
/*     */   public void setAnnotationParser(final Class annParser) {
/* 245 */     setAnnotationParser(new AnnotationParserFactory() {
/*     */           public AnnotationParser create() {
/*     */             try {
/* 248 */               return annParser.newInstance();
/* 249 */             } catch (InstantiationException e) {
/* 250 */               throw new InstantiationError(e.getMessage());
/* 251 */             } catch (IllegalAccessException e) {
/* 252 */               throw new IllegalAccessError(e.getMessage());
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAnnotationParser(AnnotationParserFactory factory) {
/* 265 */     this.apFactory = factory;
/*     */   }
/*     */   
/*     */   public AnnotationParserFactory getAnnotationParserFactory() {
/* 269 */     return this.apFactory;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\parser\XSOMParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */