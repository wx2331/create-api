/*     */ package com.sun.tools.internal.xjc.reader;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import java.util.StringTokenizer;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ErrorHandler;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ExtensionBindingChecker
/*     */   extends AbstractExtensionBindingChecker
/*     */ {
/*  63 */   private int count = 0;
/*     */   
/*     */   public ExtensionBindingChecker(String schemaLanguage, Options options, ErrorHandler handler) {
/*  66 */     super(schemaLanguage, options, handler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean needsToBePruned(String uri) {
/*  74 */     if (uri.equals(this.schemaLanguage))
/*  75 */       return false; 
/*  76 */     if (uri.equals("http://java.sun.com/xml/ns/jaxb"))
/*  77 */       return false; 
/*  78 */     if (this.enabledExtensions.contains(uri)) {
/*  79 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  85 */     return isRecognizableExtension(uri);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startDocument() throws SAXException {
/*  91 */     super.startDocument();
/*  92 */     this.count = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/*  99 */     if (!isCutting()) {
/* 100 */       String v = atts.getValue("http://java.sun.com/xml/ns/jaxb", "extensionBindingPrefixes");
/* 101 */       if (v != null) {
/* 102 */         if (this.count != 0)
/*     */         {
/* 104 */           error(Messages.ERR_UNEXPECTED_EXTENSION_BINDING_PREFIXES.format(new Object[0]));
/*     */         }
/* 106 */         if (!this.allowExtensions) {
/* 107 */           error(Messages.ERR_VENDOR_EXTENSION_DISALLOWED_IN_STRICT_MODE.format(new Object[0]));
/*     */         }
/*     */         
/* 110 */         StringTokenizer tokens = new StringTokenizer(v);
/* 111 */         while (tokens.hasMoreTokens()) {
/* 112 */           String prefix = tokens.nextToken();
/* 113 */           String uri = this.nsSupport.getURI(prefix);
/* 114 */           if (uri == null) {
/*     */             
/* 116 */             error(Messages.ERR_UNDECLARED_PREFIX.format(new Object[] { prefix })); continue;
/*     */           } 
/* 118 */           checkAndEnable(uri);
/*     */         } 
/*     */       } 
/*     */       
/* 122 */       if (needsToBePruned(namespaceURI)) {
/*     */         
/* 124 */         if (isRecognizableExtension(namespaceURI))
/*     */         {
/*     */           
/* 127 */           warning(Messages.ERR_SUPPORTED_EXTENSION_IGNORED.format(new Object[] { namespaceURI }));
/*     */         }
/* 129 */         startCutting();
/*     */       } else {
/* 131 */         verifyTagName(namespaceURI, localName, qName);
/*     */       } 
/*     */     } 
/* 134 */     this.count++;
/* 135 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\ExtensionBindingChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */