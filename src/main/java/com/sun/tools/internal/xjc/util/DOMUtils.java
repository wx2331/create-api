/*     */ package com.sun.tools.internal.xjc.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DOMUtils
/*     */ {
/*     */   public static Element getFirstChildElement(Element parent, String nsUri, String localPart) {
/*  54 */     NodeList children = parent.getChildNodes();
/*  55 */     for (int i = 0; i < children.getLength(); i++) {
/*  56 */       Node item = children.item(i);
/*  57 */       if (item instanceof Element)
/*     */       {
/*  59 */         if (nsUri.equals(item.getNamespaceURI()) && localPart
/*  60 */           .equals(item.getLocalName()))
/*  61 */           return (Element)item;  } 
/*     */     } 
/*  63 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Element[] getChildElements(Element parent, String nsUri, String localPart) {
/*  68 */     ArrayList<Node> a = new ArrayList();
/*  69 */     NodeList children = parent.getChildNodes();
/*  70 */     for (int i = 0; i < children.getLength(); i++) {
/*  71 */       Node item = children.item(i);
/*  72 */       if (item instanceof Element)
/*     */       {
/*  74 */         if (nsUri.equals(item.getNamespaceURI()) && localPart
/*  75 */           .equals(item.getLocalName()))
/*  76 */           a.add(item);  } 
/*     */     } 
/*  78 */     return a.<Element>toArray(new Element[a.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Element[] getChildElements(Element parent) {
/*  83 */     ArrayList<Node> a = new ArrayList();
/*  84 */     NodeList children = parent.getChildNodes();
/*  85 */     for (int i = 0; i < children.getLength(); i++) {
/*  86 */       Node item = children.item(i);
/*  87 */       if (item instanceof Element)
/*     */       {
/*  89 */         a.add(item); } 
/*     */     } 
/*  91 */     return a.<Element>toArray(new Element[a.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getElementText(Element element) throws DOMException {
/*  96 */     for (Node child = element.getFirstChild(); child != null; 
/*  97 */       child = child.getNextSibling()) {
/*  98 */       if (child.getNodeType() == 3)
/*  99 */         return child.getNodeValue(); 
/*     */     } 
/* 101 */     return element.getNodeValue();
/*     */   }
/*     */   
/*     */   public static Element getElement(Document parent, String name) {
/* 105 */     NodeList children = parent.getElementsByTagName(name);
/* 106 */     if (children.getLength() >= 1)
/* 107 */       return (Element)children.item(0); 
/* 108 */     return null;
/*     */   }
/*     */   
/*     */   public static Element getElement(Document parent, QName qname) {
/* 112 */     NodeList children = parent.getElementsByTagNameNS(qname.getNamespaceURI(), qname.getLocalPart());
/* 113 */     if (children.getLength() >= 1)
/* 114 */       return (Element)children.item(0); 
/* 115 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Element getElement(Document parent, String namespaceURI, String localName) {
/* 120 */     NodeList children = parent.getElementsByTagNameNS(namespaceURI, localName);
/* 121 */     if (children.getLength() >= 1)
/* 122 */       return (Element)children.item(0); 
/* 123 */     return null;
/*     */   }
/*     */   
/*     */   public static Element[] getElements(NodeList children) {
/* 127 */     Element[] elements = null;
/* 128 */     int len = 0;
/* 129 */     for (int i = 0; i < children.getLength(); i++) {
/* 130 */       if (elements == null)
/* 131 */         elements = new Element[1]; 
/* 132 */       if (elements.length == len) {
/* 133 */         Element[] buf = new Element[elements.length + 1];
/* 134 */         System.arraycopy(elements, 0, buf, 0, elements.length);
/* 135 */         elements = buf;
/*     */       } 
/* 137 */       elements[len++] = (Element)children.item(i);
/*     */     } 
/* 139 */     return elements;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xj\\util\DOMUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */