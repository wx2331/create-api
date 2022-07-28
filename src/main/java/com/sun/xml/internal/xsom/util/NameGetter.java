/*     */ package com.sun.xml.internal.xsom.util;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSAnnotation;
/*     */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeUse;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSContentType;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSFacet;
/*     */ import com.sun.xml.internal.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSNotation;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSSchema;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.XSXPath;
/*     */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*     */ import java.util.Locale;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NameGetter
/*     */   implements XSFunction<String>
/*     */ {
/*     */   private final Locale locale;
/*     */   
/*     */   public NameGetter(Locale _locale) {
/*  66 */     this.locale = _locale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   public static final XSFunction theInstance = new NameGetter(null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String get(XSComponent comp) {
/*  82 */     return (String)comp.apply(theInstance);
/*     */   }
/*     */ 
/*     */   
/*     */   public String annotation(XSAnnotation ann) {
/*  87 */     return localize("annotation");
/*     */   }
/*     */   
/*     */   public String attGroupDecl(XSAttGroupDecl decl) {
/*  91 */     return localize("attGroupDecl");
/*     */   }
/*     */   
/*     */   public String attributeUse(XSAttributeUse use) {
/*  95 */     return localize("attributeUse");
/*     */   }
/*     */   
/*     */   public String attributeDecl(XSAttributeDecl decl) {
/*  99 */     return localize("attributeDecl");
/*     */   }
/*     */   
/*     */   public String complexType(XSComplexType type) {
/* 103 */     return localize("complexType");
/*     */   }
/*     */   
/*     */   public String schema(XSSchema schema) {
/* 107 */     return localize("schema");
/*     */   }
/*     */   
/*     */   public String facet(XSFacet facet) {
/* 111 */     return localize("facet");
/*     */   }
/*     */   
/*     */   public String simpleType(XSSimpleType simpleType) {
/* 115 */     return localize("simpleType");
/*     */   }
/*     */   
/*     */   public String particle(XSParticle particle) {
/* 119 */     return localize("particle");
/*     */   }
/*     */   
/*     */   public String empty(XSContentType empty) {
/* 123 */     return localize("empty");
/*     */   }
/*     */   
/*     */   public String wildcard(XSWildcard wc) {
/* 127 */     return localize("wildcard");
/*     */   }
/*     */   
/*     */   public String modelGroupDecl(XSModelGroupDecl decl) {
/* 131 */     return localize("modelGroupDecl");
/*     */   }
/*     */   
/*     */   public String modelGroup(XSModelGroup group) {
/* 135 */     return localize("modelGroup");
/*     */   }
/*     */   
/*     */   public String elementDecl(XSElementDecl decl) {
/* 139 */     return localize("elementDecl");
/*     */   }
/*     */   
/*     */   public String notation(XSNotation n) {
/* 143 */     return localize("notation");
/*     */   }
/*     */   
/*     */   public String identityConstraint(XSIdentityConstraint decl) {
/* 147 */     return localize("idConstraint");
/*     */   }
/*     */   
/*     */   public String xpath(XSXPath xpath) {
/* 151 */     return localize("xpath");
/*     */   }
/*     */ 
/*     */   
/*     */   private String localize(String key) {
/*     */     ResourceBundle rb;
/* 157 */     if (this.locale == null) {
/* 158 */       rb = ResourceBundle.getBundle(NameGetter.class.getName());
/*     */     } else {
/* 160 */       rb = ResourceBundle.getBundle(NameGetter.class.getName(), this.locale);
/*     */     } 
/* 162 */     return rb.getString(key);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xso\\util\NameGetter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */