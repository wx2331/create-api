/*     */ package com.sun.tools.internal.xjc.reader;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import com.sun.tools.internal.xjc.Plugin;
/*     */ import com.sun.tools.internal.xjc.util.SubtreeCutter;
/*     */ import com.sun.xml.internal.bind.v2.util.EditDistance;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.NamespaceSupport;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractExtensionBindingChecker
/*     */   extends SubtreeCutter
/*     */ {
/*  51 */   protected final NamespaceSupport nsSupport = new NamespaceSupport();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   protected final Set<String> enabledExtensions = new HashSet<>();
/*     */   
/*  58 */   private final Set<String> recognizableExtensions = new HashSet<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private Locator locator;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String schemaLanguage;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean allowExtensions;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Options options;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractExtensionBindingChecker(String schemaLanguage, Options options, ErrorHandler handler) {
/*  80 */     this.schemaLanguage = schemaLanguage;
/*  81 */     this.allowExtensions = (options.compatibilityMode != 1);
/*  82 */     this.options = options;
/*  83 */     setErrorHandler(handler);
/*     */     
/*  85 */     for (Plugin plugin : options.getAllPlugins())
/*  86 */       this.recognizableExtensions.addAll(plugin.getCustomizationURIs()); 
/*  87 */     this.recognizableExtensions.add("http://java.sun.com/xml/ns/jaxb/xjc");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void checkAndEnable(String uri) throws SAXException {
/*  97 */     if (!isRecognizableExtension(uri)) {
/*  98 */       String nearest = EditDistance.findNearest(uri, this.recognizableExtensions);
/*     */       
/* 100 */       error(Messages.ERR_UNSUPPORTED_EXTENSION.format(new Object[] { uri, nearest }));
/*     */     }
/* 102 */     else if (!isSupportedExtension(uri)) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 107 */       Plugin owner = null;
/* 108 */       for (Plugin p : this.options.getAllPlugins()) {
/* 109 */         if (p.getCustomizationURIs().contains(uri)) {
/* 110 */           owner = p;
/*     */           break;
/*     */         } 
/*     */       } 
/* 114 */       if (owner != null) {
/*     */         
/* 116 */         error(Messages.ERR_PLUGIN_NOT_ENABLED.format(new Object[] { owner.getOptionName(), uri }));
/*     */       } else {
/*     */         
/* 119 */         error(Messages.ERR_UNSUPPORTED_EXTENSION.format(new Object[] { uri }));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 124 */     this.enabledExtensions.add(uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void verifyTagName(String namespaceURI, String localName, String qName) throws SAXException {
/* 132 */     if (this.options.pluginURIs.contains(namespaceURI)) {
/*     */       
/* 134 */       boolean correct = false;
/* 135 */       for (Plugin p : this.options.activePlugins) {
/* 136 */         if (p.isCustomizationTagName(namespaceURI, localName)) {
/* 137 */           correct = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 141 */       if (!correct) {
/* 142 */         error(Messages.ERR_ILLEGAL_CUSTOMIZATION_TAGNAME.format(new Object[] { qName }));
/* 143 */         startCutting();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean isSupportedExtension(String namespaceUri) {
/* 153 */     return (namespaceUri.equals("http://java.sun.com/xml/ns/jaxb/xjc") || this.options.pluginURIs.contains(namespaceUri));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean isRecognizableExtension(String namespaceUri) {
/* 161 */     return this.recognizableExtensions.contains(namespaceUri);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {
/* 166 */     super.setDocumentLocator(locator);
/* 167 */     this.locator = locator;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startDocument() throws SAXException {
/* 172 */     super.startDocument();
/*     */     
/* 174 */     this.nsSupport.reset();
/* 175 */     this.enabledExtensions.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException {
/* 180 */     if ("http://www.w3.org/XML/1998/namespace".equals(uri))
/* 181 */       return;  super.startPrefixMapping(prefix, uri);
/* 182 */     this.nsSupport.pushContext();
/* 183 */     this.nsSupport.declarePrefix(prefix, uri);
/*     */   }
/*     */ 
/*     */   
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/* 188 */     if ("xml".equals(prefix))
/* 189 */       return;  super.endPrefixMapping(prefix);
/* 190 */     this.nsSupport.popContext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final SAXParseException error(String msg) throws SAXException {
/* 198 */     SAXParseException spe = new SAXParseException(msg, this.locator);
/* 199 */     getErrorHandler().error(spe);
/* 200 */     return spe;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void warning(String msg) throws SAXException {
/* 207 */     SAXParseException spe = new SAXParseException(msg, this.locator);
/* 208 */     getErrorHandler().warning(spe);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\AbstractExtensionBindingChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */