/*     */ package com.sun.tools.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.istack.internal.SAXParseException2;
/*     */ import com.sun.tools.internal.ws.resources.WsdlMessages;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractReferenceFinderImpl
/*     */   extends XMLFilterImpl
/*     */ {
/*     */   protected final DOMForest parent;
/*     */   private Locator locator;
/*     */   
/*     */   protected AbstractReferenceFinderImpl(DOMForest _parent) {
/*  57 */     this.parent = _parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract String findExternalResource(String paramString1, String paramString2, Attributes paramAttributes);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/*  76 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */     
/*  78 */     String relativeRef = findExternalResource(namespaceURI, localName, atts);
/*  79 */     if (relativeRef == null)
/*     */       return; 
/*     */     try {
/*     */       String ref;
/*  83 */       assert this.locator != null;
/*  84 */       String lsi = this.locator.getSystemId();
/*     */       
/*  86 */       if (lsi.startsWith("jar:"))
/*  87 */       { int bangIdx = lsi.indexOf('!');
/*  88 */         if (bangIdx > 0) {
/*  89 */           ref = (new URL(new URL(lsi), relativeRef)).toString();
/*     */         } else {
/*  91 */           ref = relativeRef;
/*     */         }  }
/*  93 */       else { ref = (new URI(lsi)).resolve(new URI(relativeRef)).toString(); }
/*     */ 
/*     */ 
/*     */       
/*  97 */       this.parent.parse(ref, false);
/*  98 */     } catch (URISyntaxException e) {
/*     */       
/* 100 */       SAXParseException spe = new SAXParseException2(WsdlMessages.ABSTRACT_REFERENCE_FINDER_IMPL_UNABLE_TO_PARSE(relativeRef, e.getMessage()), this.locator, e);
/*     */ 
/*     */       
/* 103 */       fatalError(spe);
/* 104 */       throw spe;
/* 105 */     } catch (IOException e) {
/*     */       
/* 107 */       SAXParseException spe = new SAXParseException2(WsdlMessages.ABSTRACT_REFERENCE_FINDER_IMPL_UNABLE_TO_PARSE(relativeRef, e.getMessage()), this.locator, e);
/*     */ 
/*     */       
/* 110 */       fatalError(spe);
/* 111 */       throw spe;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/* 119 */     super.setDocumentLocator(locator);
/* 120 */     this.locator = locator;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\AbstractReferenceFinderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */