/*     */ package com.sun.tools.internal.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public final class DOMUtil
/*     */ {
/*     */   static final String getAttribute(Element e, String attName) {
/*  40 */     if (e.getAttributeNode(attName) == null) return null; 
/*  41 */     return e.getAttribute(attName);
/*     */   }
/*     */   
/*     */   public static String getAttribute(Element e, String nsUri, String local) {
/*  45 */     if (e.getAttributeNodeNS(nsUri, local) == null) return null; 
/*  46 */     return e.getAttributeNS(nsUri, local);
/*     */   }
/*     */   
/*     */   public static Element getElement(Element e, String nsUri, String localName) {
/*  50 */     NodeList l = e.getChildNodes();
/*  51 */     for (int i = 0; i < l.getLength(); i++) {
/*  52 */       Node n = l.item(i);
/*  53 */       if (n.getNodeType() == 1) {
/*  54 */         Element r = (Element)n;
/*  55 */         if (equals(r.getLocalName(), localName) && equals(fixNull(r.getNamespaceURI()), nsUri))
/*  56 */           return r; 
/*     */       } 
/*     */     } 
/*  59 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean equals(String a, String b) {
/*  67 */     if (a == b) return true; 
/*  68 */     if (a == null || b == null) return false; 
/*  69 */     return a.equals(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String fixNull(String s) {
/*  76 */     if (s == null) return ""; 
/*  77 */     return s;
/*     */   }
/*     */   
/*     */   public static Element getElement(Element e, String localName) {
/*  81 */     return getElement(e, "", localName);
/*     */   }
/*     */   
/*     */   public static List<Element> getChildElements(Element e) {
/*  85 */     List<Element> r = new ArrayList<>();
/*  86 */     NodeList l = e.getChildNodes();
/*  87 */     for (int i = 0; i < l.getLength(); i++) {
/*  88 */       Node n = l.item(i);
/*  89 */       if (n.getNodeType() == 1)
/*  90 */         r.add((Element)n); 
/*     */     } 
/*  92 */     return r;
/*     */   }
/*     */   
/*     */   public static List<Element> getChildElements(Element e, String localName) {
/*  96 */     List<Element> r = new ArrayList<>();
/*  97 */     NodeList l = e.getChildNodes();
/*  98 */     for (int i = 0; i < l.getLength(); i++) {
/*  99 */       Node n = l.item(i);
/* 100 */       if (n.getNodeType() == 1) {
/* 101 */         Element c = (Element)n;
/* 102 */         if (c.getLocalName().equals(localName))
/* 103 */           r.add(c); 
/*     */       } 
/*     */     } 
/* 106 */     return r;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\dtd\bindinfo\DOMUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */