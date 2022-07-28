/*     */ package com.sun.tools.internal.xjc.reader.xmlschema;
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
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.XSXPath;
/*     */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class RefererFinder
/*     */   implements XSVisitor
/*     */ {
/*  62 */   private final Set<Object> visited = new HashSet();
/*     */   
/*  64 */   private final Map<XSComponent, Set<XSComponent>> referers = new HashMap<>();
/*     */   
/*     */   public Set<XSComponent> getReferer(XSComponent src) {
/*  67 */     Set<XSComponent> r = this.referers.get(src);
/*  68 */     if (r == null) return Collections.emptySet(); 
/*  69 */     return r;
/*     */   }
/*     */ 
/*     */   
/*     */   public void schemaSet(XSSchemaSet xss) {
/*  74 */     if (!this.visited.add(xss))
/*     */       return; 
/*  76 */     for (XSSchema xs : xss.getSchemas()) {
/*  77 */       schema(xs);
/*     */     }
/*     */   }
/*     */   
/*     */   public void schema(XSSchema xs) {
/*  82 */     if (!this.visited.add(xs))
/*     */       return; 
/*  84 */     for (XSComplexType ct : xs.getComplexTypes().values()) {
/*  85 */       complexType(ct);
/*     */     }
/*     */     
/*  88 */     for (XSElementDecl e : xs.getElementDecls().values()) {
/*  89 */       elementDecl(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void elementDecl(XSElementDecl e) {
/*  94 */     if (!this.visited.add(e))
/*     */       return; 
/*  96 */     refer((XSComponent)e, e.getType());
/*  97 */     e.getType().visit(this);
/*     */   }
/*     */   
/*     */   public void complexType(XSComplexType ct) {
/* 101 */     if (!this.visited.add(ct))
/*     */       return; 
/* 103 */     refer((XSComponent)ct, ct.getBaseType());
/* 104 */     ct.getBaseType().visit(this);
/* 105 */     ct.getContentType().visit(this);
/*     */   }
/*     */   
/*     */   public void modelGroupDecl(XSModelGroupDecl decl) {
/* 109 */     if (!this.visited.add(decl))
/*     */       return; 
/* 111 */     modelGroup(decl.getModelGroup());
/*     */   }
/*     */   
/*     */   public void modelGroup(XSModelGroup group) {
/* 115 */     if (!this.visited.add(group))
/*     */       return; 
/* 117 */     for (XSParticle p : group.getChildren()) {
/* 118 */       particle(p);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void particle(XSParticle particle) {
/* 124 */     particle.getTerm().visit(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void simpleType(XSSimpleType simpleType) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void annotation(XSAnnotation ann) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void attGroupDecl(XSAttGroupDecl decl) {}
/*     */ 
/*     */   
/*     */   public void attributeDecl(XSAttributeDecl decl) {}
/*     */ 
/*     */   
/*     */   public void attributeUse(XSAttributeUse use) {}
/*     */ 
/*     */   
/*     */   private void refer(XSComponent source, XSType target) {
/* 147 */     Set<XSComponent> r = this.referers.get(target);
/* 148 */     if (r == null) {
/* 149 */       r = new HashSet<>();
/* 150 */       this.referers.put(target, r);
/*     */     } 
/* 152 */     r.add(source);
/*     */   }
/*     */   
/*     */   public void facet(XSFacet facet) {}
/*     */   
/*     */   public void notation(XSNotation notation) {}
/*     */   
/*     */   public void identityConstraint(XSIdentityConstraint decl) {}
/*     */   
/*     */   public void xpath(XSXPath xp) {}
/*     */   
/*     */   public void wildcard(XSWildcard wc) {}
/*     */   
/*     */   public void empty(XSContentType empty) {}
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\RefererFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */