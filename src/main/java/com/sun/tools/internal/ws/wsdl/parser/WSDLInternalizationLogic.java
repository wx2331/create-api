/*     */ package com.sun.tools.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.util.DOMUtils;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.Attributes;
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
/*     */ public class WSDLInternalizationLogic
/*     */   implements InternalizationLogic
/*     */ {
/*     */   private static final class ReferenceFinder
/*     */     extends AbstractReferenceFinderImpl
/*     */   {
/*     */     ReferenceFinder(DOMForest parent) {
/*  48 */       super(parent);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String findExternalResource(String nsURI, String localName, Attributes atts) {
/*  53 */       if ("http://schemas.xmlsoap.org/wsdl/".equals(nsURI) && "import".equals(localName))
/*     */       {
/*     */ 
/*     */         
/*  57 */         return atts.getValue("location");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  62 */       if (this.parent.options.clientjar != null && 
/*  63 */         "http://www.w3.org/2001/XMLSchema".equals(nsURI) && "import".equals(localName)) {
/*  64 */         return atts.getValue("schemaLocation");
/*     */       }
/*     */       
/*  67 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public XMLFilterImpl createExternalReferenceFinder(DOMForest parent) {
/*  72 */     return new ReferenceFinder(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkIfValidTargetNode(DOMForest parent, Element bindings, Element target) {
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Element refineSchemaTarget(Element target) {
/*  83 */     Element annotation = DOMUtils.getFirstChildElement(target, "http://www.w3.org/2001/XMLSchema", "annotation");
/*  84 */     if (annotation == null)
/*     */     {
/*  86 */       annotation = insertXMLSchemaElement(target, "annotation");
/*     */     }
/*     */     
/*  89 */     Element appinfo = DOMUtils.getFirstChildElement(annotation, "http://www.w3.org/2001/XMLSchema", "appinfo");
/*  90 */     if (appinfo == null)
/*     */     {
/*  92 */       appinfo = insertXMLSchemaElement(annotation, "appinfo");
/*     */     }
/*  94 */     return appinfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Element refineWSDLTarget(Element target) {
/* 101 */     Element JAXWSBindings = DOMUtils.getFirstChildElement(target, "http://java.sun.com/xml/ns/jaxws", "bindings");
/* 102 */     if (JAXWSBindings == null)
/*     */     {
/* 104 */       JAXWSBindings = insertJAXWSBindingsElement(target, "bindings"); } 
/* 105 */     return JAXWSBindings;
/*     */   }
/*     */   
/*     */   private Element insertJAXWSBindingsElement(Element parent, String localName) {
/* 109 */     String qname = "JAXWS:" + localName;
/*     */     
/* 111 */     Element child = parent.getOwnerDocument().createElementNS("http://java.sun.com/xml/ns/jaxws", qname);
/*     */     
/* 113 */     NodeList children = parent.getChildNodes();
/*     */     
/* 115 */     if (children.getLength() == 0) {
/* 116 */       parent.appendChild(child);
/*     */     } else {
/* 118 */       parent.insertBefore(child, children.item(0));
/*     */     } 
/* 120 */     return child;
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
/*     */   private Element insertXMLSchemaElement(Element parent, String localName) {
/* 134 */     String qname = parent.getTagName();
/* 135 */     int idx = qname.indexOf(':');
/* 136 */     if (idx == -1) { qname = localName; }
/* 137 */     else { qname = qname.substring(0, idx + 1) + localName; }
/*     */     
/* 139 */     Element child = parent.getOwnerDocument().createElementNS("http://www.w3.org/2001/XMLSchema", qname);
/*     */     
/* 141 */     NodeList children = parent.getChildNodes();
/*     */     
/* 143 */     if (children.getLength() == 0) {
/* 144 */       parent.appendChild(child);
/*     */     } else {
/* 146 */       parent.insertBefore(child, children.item(0));
/*     */     } 
/* 148 */     return child;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\WSDLInternalizationLogic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */