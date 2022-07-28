/*     */ package com.sun.tools.internal.ws.wsdl.framework;
/*     */ 
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLParserContext;
/*     */ import com.sun.tools.internal.ws.resources.WsdlMessages;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wsdl.parser.DOMForest;
/*     */ import com.sun.xml.internal.ws.util.NamespaceSupport;
/*     */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TWSDLParserContextImpl
/*     */   implements TWSDLParserContext
/*     */ {
/*     */   private static final String PREFIX_XMLNS = "xmlns";
/*     */   private boolean _followImports;
/*     */   private final AbstractDocument _document;
/*     */   private final NamespaceSupport _nsSupport;
/*     */   private final ArrayList<ParserListener> _listeners;
/*     */   private final WSDLLocation _wsdlLocation;
/*     */   private final DOMForest forest;
/*     */   private final ErrorReceiver errorReceiver;
/*     */   
/*     */   public TWSDLParserContextImpl(DOMForest forest, AbstractDocument doc, ArrayList<ParserListener> listeners, ErrorReceiver errReceiver) {
/*  60 */     this._document = doc;
/*  61 */     this._listeners = listeners;
/*  62 */     this._nsSupport = new NamespaceSupport();
/*  63 */     this._wsdlLocation = new WSDLLocation();
/*  64 */     this.forest = forest;
/*  65 */     this.errorReceiver = errReceiver;
/*     */   }
/*     */   
/*     */   public AbstractDocument getDocument() {
/*  69 */     return this._document;
/*     */   }
/*     */   
/*     */   public boolean getFollowImports() {
/*  73 */     return this._followImports;
/*     */   }
/*     */   
/*     */   public void setFollowImports(boolean b) {
/*  77 */     this._followImports = b;
/*     */   }
/*     */   
/*     */   public void push() {
/*  81 */     this._nsSupport.pushContext();
/*     */   }
/*     */   
/*     */   public void pop() {
/*  85 */     this._nsSupport.popContext();
/*     */   }
/*     */   
/*     */   public String getNamespaceURI(String prefix) {
/*  89 */     return this._nsSupport.getURI(prefix);
/*     */   }
/*     */   
/*     */   public Iterable<String> getPrefixes() {
/*  93 */     return this._nsSupport.getPrefixes();
/*     */   }
/*     */   
/*     */   public String getDefaultNamespaceURI() {
/*  97 */     return getNamespaceURI("");
/*     */   }
/*     */   
/*     */   public void registerNamespaces(Element e) {
/* 101 */     for (Iterator<Attr> iter = XmlUtil.getAllAttributes(e); iter.hasNext(); ) {
/* 102 */       Attr a = iter.next();
/* 103 */       if (a.getName().equals("xmlns")) {
/*     */         
/* 105 */         this._nsSupport.declarePrefix("", a.getValue()); continue;
/*     */       } 
/* 107 */       String prefix = XmlUtil.getPrefix(a.getName());
/* 108 */       if (prefix != null && prefix.equals("xmlns")) {
/* 109 */         String nsPrefix = XmlUtil.getLocalPart(a.getName());
/* 110 */         String uri = a.getValue();
/* 111 */         this._nsSupport.declarePrefix(nsPrefix, uri);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Locator getLocation(Element e) {
/* 118 */     return this.forest.locatorTable.getStartLocation(e);
/*     */   }
/*     */   
/*     */   public QName translateQualifiedName(Locator locator, String s) {
/* 122 */     if (s == null) {
/* 123 */       return null;
/*     */     }
/* 125 */     String prefix = XmlUtil.getPrefix(s);
/* 126 */     String uri = null;
/*     */     
/* 128 */     if (prefix == null) {
/* 129 */       uri = getDefaultNamespaceURI();
/*     */     } else {
/* 131 */       uri = getNamespaceURI(prefix);
/* 132 */       if (uri == null) {
/* 133 */         this.errorReceiver.error(locator, WsdlMessages.PARSING_UNKNOWN_NAMESPACE_PREFIX(prefix));
/*     */       }
/*     */     } 
/*     */     
/* 137 */     return new QName(uri, XmlUtil.getLocalPart(s));
/*     */   }
/*     */   
/*     */   public void fireIgnoringExtension(Element e, Entity entity) {
/* 141 */     QName name = new QName(e.getNamespaceURI(), e.getLocalName());
/* 142 */     QName parent = entity.getElementName();
/* 143 */     List _targets = null;
/*     */     
/* 145 */     synchronized (this) {
/* 146 */       if (this._listeners != null) {
/* 147 */         _targets = (List)this._listeners.clone();
/*     */       }
/*     */     } 
/*     */     
/* 151 */     if (_targets != null) {
/* 152 */       for (Iterator<ParserListener> iter = _targets.iterator(); iter.hasNext(); ) {
/* 153 */         ParserListener l = iter.next();
/* 154 */         l.ignoringExtension(entity, name, parent);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void fireDoneParsingEntity(QName element, Entity entity) {
/* 160 */     List _targets = null;
/*     */     
/* 162 */     synchronized (this) {
/* 163 */       if (this._listeners != null) {
/* 164 */         _targets = (List)this._listeners.clone();
/*     */       }
/*     */     } 
/*     */     
/* 168 */     if (_targets != null) {
/* 169 */       for (Iterator<ParserListener> iter = _targets.iterator(); iter.hasNext(); ) {
/* 170 */         ParserListener l = iter.next();
/* 171 */         l.doneParsingEntity(element, entity);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void pushWSDLLocation() {
/* 179 */     this._wsdlLocation.push();
/*     */   }
/*     */   
/*     */   public void popWSDLLocation() {
/* 183 */     this._wsdlLocation.pop();
/*     */   }
/*     */   
/*     */   public void setWSDLLocation(String loc) {
/* 187 */     this._wsdlLocation.setLocation(loc);
/*     */   }
/*     */   
/*     */   public String getWSDLLocation() {
/* 191 */     return this._wsdlLocation.getLocation();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\framework\TWSDLParserContextImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */