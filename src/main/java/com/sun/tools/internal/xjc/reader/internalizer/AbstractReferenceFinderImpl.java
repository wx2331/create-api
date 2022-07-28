/*     */ package com.sun.tools.internal.xjc.reader.internalizer;
/*     */ 
/*     */ import com.sun.istack.internal.SAXParseException2;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
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
/*     */ public abstract class AbstractReferenceFinderImpl
/*     */   extends XMLFilterImpl
/*     */ {
/*     */   protected final DOMForest parent;
/*     */   private Locator locator;
/*     */   
/*     */   protected AbstractReferenceFinderImpl(DOMForest _parent) {
/*  55 */     this.parent = _parent;
/*     */   }
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
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/*  71 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */     
/*  73 */     String relativeRef = findExternalResource(namespaceURI, localName, atts);
/*  74 */     if (relativeRef == null) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/*  79 */       String ref, lsi = this.locator.getSystemId();
/*     */       
/*  81 */       URI relRefURI = new URI(relativeRef);
/*  82 */       if (relRefURI.isAbsolute()) {
/*  83 */         ref = relativeRef;
/*     */       }
/*  85 */       else if (lsi.startsWith("jar:")) {
/*  86 */         int bangIdx = lsi.indexOf('!');
/*  87 */         if (bangIdx > 0) {
/*     */           
/*  89 */           ref = lsi.substring(0, bangIdx + 1) + (new URI(lsi.substring(bangIdx + 1))).resolve(new URI(relativeRef)).toString();
/*     */         } else {
/*  91 */           ref = relativeRef;
/*     */         } 
/*     */       } else {
/*  94 */         ref = (new URI(lsi)).resolve(new URI(relativeRef)).toString();
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 100 */       if (this.parent != null) {
/* 101 */         this.parent.parse(ref, false);
/*     */       }
/* 103 */     } catch (URISyntaxException e) {
/* 104 */       String msg = e.getMessage();
/* 105 */       if ((new File(relativeRef)).exists()) {
/* 106 */         msg = Messages.format("ERR_FILENAME_IS_NOT_URI", new Object[0]) + ' ' + msg;
/*     */       }
/*     */ 
/*     */       
/* 110 */       SAXParseException spe = new SAXParseException2(Messages.format("AbstractReferenceFinderImpl.UnableToParse", new Object[] { relativeRef, msg }), this.locator, e);
/*     */ 
/*     */       
/* 113 */       fatalError(spe);
/* 114 */       throw spe;
/* 115 */     } catch (IOException e) {
/*     */       
/* 117 */       SAXParseException spe = new SAXParseException2(Messages.format("AbstractReferenceFinderImpl.UnableToParse", new Object[] { relativeRef, e.getMessage() }), this.locator, e);
/*     */ 
/*     */       
/* 120 */       fatalError(spe);
/* 121 */       throw spe;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/* 129 */     super.setDocumentLocator(locator);
/* 130 */     this.locator = locator;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\internalizer\AbstractReferenceFinderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */