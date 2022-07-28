/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*     */
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.Messages;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationParser;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationParserFactory;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.UnmarshallerHandler;
/*     */ import javax.xml.bind.helpers.DefaultValidationEventHandler;
/*     */ import javax.xml.validation.ValidatorHandler;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
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
/*     */
/*     */
/*     */ public class AnnotationParserFactoryImpl
/*     */   implements AnnotationParserFactory
/*     */ {
/*     */   private final Options options;
/*     */   private ValidatorHandler validator;
/*     */
/*     */   public AnnotationParserFactoryImpl(Options opts) {
/*  58 */     this.options = opts;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public AnnotationParser create() {
/*  69 */     return new AnnotationParser() {
/*  70 */         private Unmarshaller u = BindInfo.getCustomizationUnmarshaller();
/*     */
/*     */
/*     */
/*     */         private UnmarshallerHandler handler;
/*     */
/*     */
/*     */
/*     */
/*     */         public ContentHandler getContentHandler(AnnotationContext context, String parentElementName, final ErrorHandler errorHandler, EntityResolver entityResolver) {
/*  80 */           if (this.handler != null)
/*     */           {
/*     */
/*  83 */             throw new AssertionError();
/*     */           }
/*  85 */           if (AnnotationParserFactoryImpl.this.options.debugMode) {
/*     */             try {
/*  87 */               this.u.setEventHandler(new DefaultValidationEventHandler());
/*  88 */             } catch (JAXBException e) {
/*  89 */               throw new AssertionError(e);
/*     */             }
/*     */           }
/*  92 */           this.handler = this.u.getUnmarshallerHandler();
/*     */
/*     */
/*  95 */           return new ForkingFilter(this.handler)
/*     */             {
/*     */               public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
/*  98 */                 super.startElement(uri, localName, qName, atts);
/*  99 */                 if ((uri.equals("http://java.sun.com/xml/ns/jaxb") || uri.equals("http://java.sun.com/xml/ns/jaxb/xjc")) &&
/* 100 */                   getSideHandler() == null) {
/*     */
/* 102 */                   if (AnnotationParserFactoryImpl.this.validator == null)
/* 103 */                     AnnotationParserFactoryImpl.this.validator = BindInfo.bindingFileSchema.newValidator();
/* 104 */                   AnnotationParserFactoryImpl.this.validator.setErrorHandler(errorHandler);
/* 105 */                   startForking(uri, localName, qName, atts, new ValidatorProtecter(AnnotationParserFactoryImpl.this.validator));
/*     */                 }
/*     */
/*     */
/* 109 */                 for (int i = atts.getLength() - 1; i >= 0; i--) {
/* 110 */                   if (atts.getURI(i).equals("http://www.w3.org/2005/05/xmlmime") && atts
/* 111 */                     .getLocalName(i).equals("expectedContentTypes")) {
/* 112 */                     errorHandler.warning(new SAXParseException(
/* 113 */                           Messages.format("UnusedCustomizationChecker.WarnUnusedExpectedContentTypes", new Object[0]),
/*     */
/* 115 */                           getDocumentLocator()));
/*     */                   }
/*     */                 }
/*     */               }
/*     */             };
/*     */         }
/*     */
/*     */         public BindInfo getResult(Object existing) {
/* 123 */           if (this.handler == null)
/*     */           {
/*     */
/* 126 */             throw new AssertionError();
/*     */           }
/*     */           try {
/* 129 */             BindInfo result = (BindInfo)this.handler.getResult();
/*     */
/* 131 */             if (existing != null) {
/* 132 */               BindInfo bie = (BindInfo)existing;
/* 133 */               bie.absorb(result);
/* 134 */               return bie;
/*     */             }
/* 136 */             if (!result.isPointless()) {
/* 137 */               return result;
/*     */             }
/* 139 */             return null;
/*     */           }
/* 141 */           catch (JAXBException e) {
/* 142 */             throw new AssertionError(e);
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */
/*     */   private static final class ValidatorProtecter extends XMLFilterImpl {
/*     */     public ValidatorProtecter(ContentHandler h) {
/* 150 */       setContentHandler(h);
/*     */     }
/*     */
/*     */
/*     */
/*     */     public void startPrefixMapping(String prefix, String uri) throws SAXException {
/* 156 */       super.startPrefixMapping(prefix.intern(), uri);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\bindinfo\AnnotationParserFactoryImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
