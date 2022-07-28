/*     */ package com.sun.xml.internal.xsom.util;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSAnnotation;
/*     */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeUse;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ComponentNameFunction
/*     */   implements XSFunction<String>
/*     */ {
/*  55 */   private NameGetter nameGetter = new NameGetter(null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String annotation(XSAnnotation ann) {
/*  62 */     return this.nameGetter.annotation(ann);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String attGroupDecl(XSAttGroupDecl decl) {
/*  69 */     String name = decl.getName();
/*  70 */     if (name == null) name = ""; 
/*  71 */     return name + " " + this.nameGetter.attGroupDecl(decl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String attributeDecl(XSAttributeDecl decl) {
/*  78 */     String name = decl.getName();
/*  79 */     if (name == null) name = ""; 
/*  80 */     return name + " " + this.nameGetter.attributeDecl(decl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String attributeUse(XSAttributeUse use) {
/*  88 */     return this.nameGetter.attributeUse(use);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String complexType(XSComplexType type) {
/*  95 */     String name = type.getName();
/*  96 */     if (name == null) name = "anonymous"; 
/*  97 */     return name + " " + this.nameGetter.complexType(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String schema(XSSchema schema) {
/* 104 */     return this.nameGetter.schema(schema) + " \"" + schema.getTargetNamespace() + "\"";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String facet(XSFacet facet) {
/* 111 */     String name = facet.getName();
/* 112 */     if (name == null) name = ""; 
/* 113 */     return name + " " + this.nameGetter.facet(facet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String notation(XSNotation notation) {
/* 120 */     String name = notation.getName();
/* 121 */     if (name == null) name = ""; 
/* 122 */     return name + " " + this.nameGetter.notation(notation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String simpleType(XSSimpleType simpleType) {
/* 129 */     String name = simpleType.getName();
/* 130 */     if (name == null) name = "anonymous"; 
/* 131 */     return name + " " + this.nameGetter.simpleType(simpleType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String particle(XSParticle particle) {
/* 139 */     return this.nameGetter.particle(particle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String empty(XSContentType empty) {
/* 147 */     return this.nameGetter.empty(empty);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String wildcard(XSWildcard wc) {
/* 155 */     return this.nameGetter.wildcard(wc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String modelGroupDecl(XSModelGroupDecl decl) {
/* 162 */     String name = decl.getName();
/* 163 */     if (name == null) name = ""; 
/* 164 */     return name + " " + this.nameGetter.modelGroupDecl(decl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String modelGroup(XSModelGroup group) {
/* 172 */     return this.nameGetter.modelGroup(group);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String elementDecl(XSElementDecl decl) {
/* 179 */     String name = decl.getName();
/* 180 */     if (name == null) name = ""; 
/* 181 */     return name + " " + this.nameGetter.elementDecl(decl);
/*     */   }
/*     */   
/*     */   public String identityConstraint(XSIdentityConstraint decl) {
/* 185 */     return decl.getName() + " " + this.nameGetter.identityConstraint(decl);
/*     */   }
/*     */   
/*     */   public String xpath(XSXPath xpath) {
/* 189 */     return this.nameGetter.xpath(xpath);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xso\\util\ComponentNameFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */