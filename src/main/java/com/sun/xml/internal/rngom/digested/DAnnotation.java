/*     */ package com.sun.xml.internal.rngom.digested;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DAnnotation
/*     */ {
/*  68 */   static final DAnnotation EMPTY = new DAnnotation();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   final Map<QName, Attribute> attributes = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   final List<Element> contents = new ArrayList<>();
/*     */ 
/*     */   
/*     */   public static class Attribute
/*     */   {
/*     */     private final String ns;
/*     */     
/*     */     private final String localName;
/*     */     
/*     */     private final String prefix;
/*     */     private String value;
/*     */     private Locator loc;
/*     */     
/*     */     public Attribute(String ns, String localName, String prefix) {
/*  92 */       this.ns = ns;
/*  93 */       this.localName = localName;
/*  94 */       this.prefix = prefix;
/*     */     }
/*     */     
/*     */     public Attribute(String ns, String localName, String prefix, String value, Locator loc) {
/*  98 */       this.ns = ns;
/*  99 */       this.localName = localName;
/* 100 */       this.prefix = prefix;
/* 101 */       this.value = value;
/* 102 */       this.loc = loc;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getNs() {
/* 112 */       return this.ns;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getLocalName() {
/* 122 */       return this.localName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getPrefix() {
/* 132 */       return this.prefix;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getValue() {
/* 142 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Locator getLoc() {
/* 152 */       return this.loc;
/*     */     }
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
/*     */   public Attribute getAttribute(String nsUri, String localName) {
/* 165 */     return getAttribute(new QName(nsUri, localName));
/*     */   }
/*     */   
/*     */   public Attribute getAttribute(QName n) {
/* 169 */     return this.attributes.get(n);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<QName, Attribute> getAttributes() {
/* 180 */     return Collections.unmodifiableMap(this.attributes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Element> getChildren() {
/* 191 */     return Collections.unmodifiableList(this.contents);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\digested\DAnnotation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */