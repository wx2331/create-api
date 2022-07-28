/*     */ package com.sun.tools.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.tools.internal.ws.util.xml.XmlUtil;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wscompile.WsimportOptions;
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.LocatorTable;
/*     */ import com.sun.xml.internal.bind.marshaller.DataWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.sax.SAXResult;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
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
/*     */ public class DOMForest
/*     */ {
/*  71 */   protected final Set<String> rootDocuments = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   protected final Set<String> externalReferences = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   protected final Map<String, Document> core = new HashMap<>();
/*     */ 
/*     */   
/*     */   protected final ErrorReceiver errorReceiver;
/*     */   
/*     */   private final DocumentBuilder documentBuilder;
/*     */   
/*     */   private final SAXParserFactory parserFactory;
/*     */   
/*  90 */   protected final List<Element> inlinedSchemaElements = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   public final LocatorTable locatorTable = new LocatorTable();
/*     */ 
/*     */   
/*     */   protected final EntityResolver entityResolver;
/*     */ 
/*     */   
/* 102 */   public final Set<Element> outerMostBindings = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final InternalizationLogic logic;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final WsimportOptions options;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<String, String> resolvedCache;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Element> getInlinedSchemaElement() {
/* 128 */     return this.inlinedSchemaElements;
/*     */   }
/*     */   @NotNull
/*     */   public Document parse(InputSource source, boolean root) throws SAXException, IOException {
/* 132 */     if (source.getSystemId() == null)
/* 133 */       throw new IllegalArgumentException(); 
/* 134 */     return parse(source.getSystemId(), source, root);
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
/*     */   public Document parse(String systemId, boolean root) throws SAXException, IOException {
/* 146 */     systemId = normalizeSystemId(systemId);
/*     */     
/* 148 */     InputSource is = null;
/*     */ 
/*     */     
/* 151 */     is = this.entityResolver.resolveEntity(null, systemId);
/* 152 */     if (is == null) {
/* 153 */       is = new InputSource(systemId);
/*     */     } else {
/* 155 */       this.resolvedCache.put(systemId, is.getSystemId());
/* 156 */       systemId = is.getSystemId();
/*     */     } 
/*     */     
/* 159 */     if (this.core.containsKey(systemId))
/*     */     {
/* 161 */       return this.core.get(systemId);
/*     */     }
/*     */     
/* 164 */     if (!root) {
/* 165 */       addExternalReferences(systemId);
/*     */     }
/*     */     
/* 168 */     return parse(systemId, is, root);
/*     */   }
/* 170 */   public DOMForest(InternalizationLogic logic, @NotNull EntityResolver entityResolver, WsimportOptions options, ErrorReceiver errReceiver) { this.resolvedCache = new HashMap<>(); this.options = options; this.entityResolver = entityResolver; this.errorReceiver = errReceiver; this.logic = logic; boolean disableXmlSecurity = (options == null) ? false : options.disableXmlSecurity; DocumentBuilderFactory dbf = XmlUtil.newDocumentBuilderFactory(disableXmlSecurity); this.parserFactory = XmlUtil.newSAXParserFactory(disableXmlSecurity); try { this.documentBuilder = dbf.newDocumentBuilder(); }
/*     */     catch (ParserConfigurationException e)
/*     */     { throw new AssertionError(e); }
/* 173 */      } public Map<String, String> getReferencedEntityMap() { return this.resolvedCache; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   private Document parse(String systemId, InputSource inputSource, boolean root) throws SAXException, IOException {
/* 181 */     Document dom = this.documentBuilder.newDocument();
/*     */     
/* 183 */     systemId = normalizeSystemId(systemId);
/*     */ 
/*     */ 
/*     */     
/* 187 */     this.core.put(systemId, dom);
/*     */     
/* 189 */     dom.setDocumentURI(systemId);
/* 190 */     if (root) {
/* 191 */       this.rootDocuments.add(systemId);
/*     */     }
/*     */     try {
/* 194 */       XMLReader reader = createReader(dom);
/*     */       
/* 196 */       InputStream is = null;
/* 197 */       if (inputSource.getByteStream() == null) {
/* 198 */         inputSource = this.entityResolver.resolveEntity(null, systemId);
/*     */       }
/* 200 */       reader.parse(inputSource);
/* 201 */       Element doc = dom.getDocumentElement();
/* 202 */       if (doc == null) {
/* 203 */         return null;
/*     */       }
/* 205 */       NodeList schemas = doc.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "schema");
/* 206 */       for (int i = 0; i < schemas.getLength(); i++) {
/* 207 */         this.inlinedSchemaElements.add((Element)schemas.item(i));
/*     */       }
/* 209 */     } catch (ParserConfigurationException e) {
/* 210 */       this.errorReceiver.error(e);
/* 211 */       throw new SAXException(e.getMessage());
/*     */     } 
/* 213 */     this.resolvedCache.put(systemId, dom.getDocumentURI());
/* 214 */     return dom;
/*     */   }
/*     */   
/*     */   public void addExternalReferences(String ref) {
/* 218 */     if (!this.externalReferences.contains(ref)) {
/* 219 */       this.externalReferences.add(ref);
/*     */     }
/*     */   }
/*     */   
/*     */   public Set<String> getExternalReferences() {
/* 224 */     return this.externalReferences;
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
/*     */   private XMLReader createReader(Document dom) throws SAXException, ParserConfigurationException {
/* 243 */     XMLReader reader = this.parserFactory.newSAXParser().getXMLReader();
/* 244 */     DOMBuilder dombuilder = new DOMBuilder(dom, this.locatorTable, this.outerMostBindings);
/*     */     try {
/* 246 */       reader.setProperty("http://xml.org/sax/properties/lexical-handler", dombuilder);
/* 247 */     } catch (SAXException e) {
/* 248 */       this.errorReceiver.debug(e.getMessage());
/*     */     } 
/*     */     
/* 251 */     ContentHandler handler = new WhitespaceStripper(dombuilder, (ErrorHandler)this.errorReceiver, this.entityResolver);
/* 252 */     handler = new VersionChecker(handler, (ErrorHandler)this.errorReceiver, this.entityResolver);
/*     */ 
/*     */ 
/*     */     
/* 256 */     XMLFilterImpl f = this.logic.createExternalReferenceFinder(this);
/* 257 */     f.setContentHandler(handler);
/* 258 */     if (this.errorReceiver != null)
/* 259 */       f.setErrorHandler((ErrorHandler)this.errorReceiver); 
/* 260 */     f.setEntityResolver(this.entityResolver);
/*     */     
/* 262 */     reader.setContentHandler(f);
/* 263 */     if (this.errorReceiver != null)
/* 264 */       reader.setErrorHandler((ErrorHandler)this.errorReceiver); 
/* 265 */     reader.setEntityResolver(this.entityResolver);
/* 266 */     return reader;
/*     */   }
/*     */   
/*     */   private String normalizeSystemId(String systemId) {
/*     */     try {
/* 271 */       systemId = (new URI(systemId)).normalize().toString();
/* 272 */     } catch (URISyntaxException uRISyntaxException) {}
/*     */ 
/*     */     
/* 275 */     return systemId;
/*     */   }
/*     */   
/*     */   boolean isExtensionMode() {
/* 279 */     return this.options.isExtensionMode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Document get(String systemId) {
/* 288 */     Document doc = this.core.get(systemId);
/*     */     
/* 290 */     if (doc == null && systemId.startsWith("file:/") && !systemId.startsWith("file://"))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 297 */       doc = this.core.get("file://" + systemId.substring(5));
/*     */     }
/*     */     
/* 300 */     if (doc == null && systemId.startsWith("file:")) {
/*     */ 
/*     */       
/* 303 */       String systemPath = getPath(systemId);
/* 304 */       for (String key : this.core.keySet()) {
/* 305 */         if (key.startsWith("file:") && getPath(key).equalsIgnoreCase(systemPath)) {
/* 306 */           doc = this.core.get(key);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 312 */     return doc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getPath(String key) {
/* 319 */     key = key.substring(5);
/* 320 */     while (key.length() > 0 && key.charAt(0) == '/')
/* 321 */       key = key.substring(1); 
/* 322 */     return key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] listSystemIDs() {
/* 329 */     return (String[])this.core.keySet().toArray((Object[])new String[this.core.keySet().size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSystemId(Document dom) {
/* 338 */     for (Map.Entry<String, Document> e : this.core.entrySet()) {
/* 339 */       if (e.getValue() == dom)
/* 340 */         return e.getKey(); 
/*     */     } 
/* 342 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFirstRootDocument() {
/* 349 */     if (this.rootDocuments.isEmpty()) return null; 
/* 350 */     return this.rootDocuments.iterator().next();
/*     */   }
/*     */   
/*     */   public Set<String> getRootDocuments() {
/* 354 */     return this.rootDocuments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dump(OutputStream out) throws IOException {
/*     */     try {
/* 366 */       boolean secureProcessingEnabled = (this.options == null || !this.options.disableXmlSecurity);
/* 367 */       TransformerFactory tf = XmlUtil.newTransformerFactory(secureProcessingEnabled);
/* 368 */       Transformer it = tf.newTransformer();
/*     */       
/* 370 */       for (Map.Entry<String, Document> e : this.core.entrySet()) {
/* 371 */         out.write(("---<< " + (String)e.getKey() + '\n').getBytes());
/*     */         
/* 373 */         DataWriter dw = new DataWriter(new OutputStreamWriter(out), null);
/* 374 */         dw.setIndentStep("  ");
/* 375 */         it.transform(new DOMSource(e.getValue()), new SAXResult(dw));
/*     */ 
/*     */         
/* 378 */         out.write("\n\n\n".getBytes());
/*     */       } 
/* 380 */     } catch (TransformerException e) {
/* 381 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface Handler extends ContentHandler {
/*     */     Document getDocument();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\DOMForest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */