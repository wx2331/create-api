/*     */ package com.sun.tools.internal.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BIElement
/*     */ {
/*     */   final BindInfo parent;
/*     */   private final Element e;
/*     */   public final CClassInfo clazz;
/*     */   private final List<BIContent> contents;
/*     */   private final Map<String, BIConversion> conversions;
/*     */   private BIContent rest;
/*     */   private final Map<String, BIAttribute> attributes;
/*     */   private final List<BIConstructor> constructors;
/*     */   private final String className;
/*     */   
/*     */   BIElement(BindInfo bi, Element _e) {
/* 138 */     this.contents = new ArrayList<>();
/*     */ 
/*     */     
/* 141 */     this.conversions = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     this.attributes = new HashMap<>();
/*     */ 
/*     */     
/* 154 */     this.constructors = new ArrayList<>(); this.parent = bi; this.e = _e; Element c = DOMUtil.getElement(this.e, "content"); if (c != null)
/*     */       if (DOMUtil.getAttribute(c, "property") != null) { this.rest = BIContent.create(c, this); }
/*     */       else { for (Element p : DOMUtil.getChildElements(c)) { if (p.getLocalName().equals("rest")) { this.rest = BIContent.create(p, this); continue; }
/*     */            this.contents.add(BIContent.create(p, this)); }
/*     */          }
/*     */         for (Element atr : DOMUtil.getChildElements(this.e, "attribute")) { BIAttribute a = new BIAttribute(this, atr); this.attributes.put(a.name(), a); }
/*     */      if (isClass()) { String className = DOMUtil.getAttribute(this.e, "class"); if (className == null)
/*     */         className = NameConverter.standard.toClassName(name());  this.className = className; }
/*     */     else { this.className = null; }
/*     */      for (Element conv : DOMUtil.getChildElements(this.e, "conversion")) { BIConversion bIConversion = new BIUserConversion(bi, conv); this.conversions.put(bIConversion.name(), bIConversion); }
/*     */      for (Element en : DOMUtil.getChildElements(this.e, "enumeration")) { BIConversion bIConversion = BIEnumeration.create(en, this); this.conversions.put(bIConversion.name(), bIConversion); }
/*     */      for (Element element : DOMUtil.getChildElements(this.e, "constructor"))
/* 166 */       this.constructors.add(new BIConstructor(element));  String name = name(); QName tagName = new QName("", name); this.clazz = new CClassInfo(this.parent.model, this.parent.getTargetPackage(), this.className, getLocation(), null, tagName, null, null); } public String name() { return DOMUtil.getAttribute(this.e, "name"); }
/*     */   
/*     */   public Locator getLocation() {
/*     */     return DOMLocator.getLocationInfo(this.e);
/*     */   }
/*     */   
/*     */   public boolean isClass() {
/* 173 */     return "class".equals(this.e.getAttribute("type"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRoot() {
/* 180 */     return "true".equals(this.e.getAttribute("root"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/* 191 */     return this.className;
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
/*     */   public void declareConstructors(CClassInfo src) {
/* 205 */     for (BIConstructor c : this.constructors) {
/* 206 */       c.createDeclaration(src);
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
/*     */ 
/*     */   
/*     */   public BIConversion getConversion() {
/* 221 */     String cnv = DOMUtil.getAttribute(this.e, "convert");
/* 222 */     if (cnv == null) return null;
/*     */     
/* 224 */     return conversion(cnv);
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
/*     */   public BIConversion conversion(String name) {
/* 237 */     BIConversion r = this.conversions.get(name);
/* 238 */     if (r != null) return r;
/*     */ 
/*     */     
/* 241 */     return this.parent.conversion(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BIContent> getContents() {
/* 249 */     return this.contents;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BIAttribute attribute(String name) {
/* 259 */     return this.attributes.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BIContent getRest() {
/* 267 */     return this.rest;
/*     */   }
/*     */   
/*     */   public Locator getSourceLocation() {
/* 271 */     return DOMLocator.getLocationInfo(this.e);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\dtd\bindinfo\BIElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */