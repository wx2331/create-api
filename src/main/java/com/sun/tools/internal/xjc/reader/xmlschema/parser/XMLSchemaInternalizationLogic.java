/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.parser;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.AbstractReferenceFinderImpl;
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.DOMForest;
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.InternalizationLogic;
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
/*     */ 
/*     */ 
/*     */ public class XMLSchemaInternalizationLogic
/*     */   implements InternalizationLogic
/*     */ {
/*     */   private static final class ReferenceFinder
/*     */     extends AbstractReferenceFinderImpl
/*     */   {
/*     */     ReferenceFinder(DOMForest parent) {
/*  53 */       super(parent);
/*     */     }
/*     */     
/*     */     protected String findExternalResource(String nsURI, String localName, Attributes atts) {
/*  57 */       if ("http://www.w3.org/2001/XMLSchema".equals(nsURI) && ("import"
/*  58 */         .equals(localName) || "include".equals(localName))) {
/*  59 */         return atts.getValue("schemaLocation");
/*     */       }
/*  61 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public XMLFilterImpl createExternalReferenceFinder(DOMForest parent) {
/*  66 */     return (XMLFilterImpl)new ReferenceFinder(parent);
/*     */   }
/*     */   
/*     */   public boolean checkIfValidTargetNode(DOMForest parent, Element bindings, Element target) {
/*  70 */     return "http://www.w3.org/2001/XMLSchema".equals(target.getNamespaceURI());
/*     */   }
/*     */ 
/*     */   
/*     */   public Element refineTarget(Element target) {
/*  75 */     Element annotation = DOMUtils.getFirstChildElement(target, "http://www.w3.org/2001/XMLSchema", "annotation");
/*  76 */     if (annotation == null)
/*     */     {
/*  78 */       annotation = insertXMLSchemaElement(target, "annotation");
/*     */     }
/*     */     
/*  81 */     Element appinfo = DOMUtils.getFirstChildElement(annotation, "http://www.w3.org/2001/XMLSchema", "appinfo");
/*  82 */     if (appinfo == null)
/*     */     {
/*  84 */       appinfo = insertXMLSchemaElement(annotation, "appinfo");
/*     */     }
/*  86 */     return appinfo;
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
/*     */   private Element insertXMLSchemaElement(Element parent, String localName) {
/*  99 */     String qname = parent.getTagName();
/* 100 */     int idx = qname.indexOf(':');
/* 101 */     if (idx == -1) { qname = localName; }
/* 102 */     else { qname = qname.substring(0, idx + 1) + localName; }
/*     */     
/* 104 */     Element child = parent.getOwnerDocument().createElementNS("http://www.w3.org/2001/XMLSchema", qname);
/*     */     
/* 106 */     NodeList children = parent.getChildNodes();
/*     */     
/* 108 */     if (children.getLength() == 0) {
/* 109 */       parent.appendChild(child);
/*     */     } else {
/* 111 */       parent.insertBefore(child, children.item(0));
/*     */     } 
/* 113 */     return child;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\parser\XMLSchemaInternalizationLogic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */