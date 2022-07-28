/*     */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*     */
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSTerm;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.visitor.XSTermVisitor;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ final class DefaultParticleBinder
/*     */   extends ParticleBinder
/*     */ {
/*     */   public void build(XSParticle p, Collection<XSParticle> forcedProps) {
/*  58 */     Checker checker = checkCollision(p, forcedProps);
/*     */
/*  60 */     if (checker.hasNameCollision()) {
/*     */
/*     */
/*     */
/*     */
/*  65 */       CReferencePropertyInfo prop = new CReferencePropertyInfo((getCurrentBean().getBaseClass() == null) ? "Content" : "Rest", true, false, false, (XSComponent)p, this.builder.getBindInfo((XSComponent)p).toCustomizationList(), p.getLocator(), false, false, false);
/*  66 */       RawTypeSetBuilder.build(p, false).addTo(prop);
/*  67 */       prop.javadoc = Messages.format("DefaultParticleBinder.FallbackJavadoc", new Object[] { checker
/*  68 */             .getCollisionInfo().toString() });
/*     */
/*  70 */       getCurrentBean().addProperty((CPropertyInfo)prop);
/*     */     } else {
/*  72 */       (new Builder(checker.markedParticles)).particle(p);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public boolean checkFallback(XSParticle p) {
/*  78 */     return checkCollision(p, Collections.emptyList()).hasNameCollision();
/*     */   }
/*     */
/*     */
/*     */   private Checker checkCollision(XSParticle p, Collection<XSParticle> forcedProps) {
/*  83 */     Checker checker = new Checker(forcedProps);
/*     */
/*  85 */     CClassInfo superClass = getCurrentBean().getBaseClass();
/*     */
/*  87 */     if (superClass != null)
/*  88 */       checker.readSuperClass(superClass);
/*  89 */     checker.particle(p);
/*     */
/*  91 */     return checker;
/*     */   }
/*     */   private final class Checker implements XSTermVisitor { private CollisionInfo collisionInfo; private final NameCollisionChecker cchecker; private final Collection<XSParticle> forcedProps; private XSParticle outerParticle;
/*     */     public final Map<XSParticle, String> markedParticles;
/*     */     private final Map<XSParticle, String> labelCache;
/*     */
/*     */     boolean hasNameCollision() {
/*     */       return (this.collisionInfo != null);
/*     */     }
/*     */
/*     */     CollisionInfo getCollisionInfo() {
/*     */       return this.collisionInfo;
/*     */     }
/*     */
/*     */     public void particle(XSParticle p) {
/*     */       if (DefaultParticleBinder.this.getLocalPropCustomization(p) != null || DefaultParticleBinder.this.builder.getLocalDomCustomization(p) != null) {
/*     */         check(p);
/*     */         mark(p);
/*     */         return;
/*     */       }
/*     */       XSTerm t = p.getTerm();
/*     */       if (p.isRepeated() && (t.isModelGroup() || t.isModelGroupDecl())) {
/*     */         mark(p);
/*     */         return;
/*     */       }
/*     */       if (this.forcedProps.contains(p)) {
/*     */         mark(p);
/*     */         return;
/*     */       }
/*     */       this.outerParticle = p;
/*     */       t.visit(this);
/*     */     }
/*     */
/* 124 */     Checker(Collection<XSParticle> forcedProps) { this.collisionInfo = null;
/*     */
/*     */
/* 127 */       this.cchecker = new NameCollisionChecker();
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 220 */       this.markedParticles = new HashMap<>();
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 293 */       this.labelCache = new Hashtable<>(); this.forcedProps = forcedProps; }
/*     */     public void elementDecl(XSElementDecl decl) { check(this.outerParticle); mark(this.outerParticle); }
/*     */     public void modelGroup(XSModelGroup mg) { if (mg.getCompositor() == XSModelGroup.Compositor.CHOICE && DefaultParticleBinder.this.builder.getGlobalBinding().isChoiceContentPropertyEnabled()) { mark(this.outerParticle); return; }
/*     */        for (XSParticle child : mg.getChildren())
/*     */         particle(child);  }
/*     */     public void modelGroupDecl(XSModelGroupDecl decl) { modelGroup(decl.getModelGroup()); }
/*     */     public void wildcard(XSWildcard wc) { mark(this.outerParticle); }
/* 300 */     void readSuperClass(CClassInfo ci) { this.cchecker.readSuperClass(ci); } private String computeLabel(XSParticle p) { String label = this.labelCache.get(p);
/* 301 */       if (label == null)
/* 302 */         this.labelCache.put(p, label = DefaultParticleBinder.this.computeLabel(p));
/* 303 */       return label; }
/*     */     private void check(XSParticle p) { if (this.collisionInfo == null)
/*     */         this.collisionInfo = this.cchecker.check(p);  } private void mark(XSParticle p) { this.markedParticles.put(p, computeLabel(p)); } private final class NameCollisionChecker {
/*     */       CollisionInfo check(XSParticle p) { String label = Checker.this.computeLabel(p);
/*     */         if (this.occupiedLabels.containsKey(label))
/*     */           return new CollisionInfo(label, p.getLocator(), ((CPropertyInfo)this.occupiedLabels.get(label)).locator);
/*     */         for (XSParticle jp : this.particles) {
/*     */           if (!check(p, jp))
/*     */             return new CollisionInfo(label, p.getLocator(), jp.getLocator());
/*     */         }
/*     */         this.particles.add(p);
/*     */         return null; } private final List<XSParticle> particles = new ArrayList<>(); private final Map<String, CPropertyInfo> occupiedLabels = new HashMap<>(); private boolean check(XSParticle p1, XSParticle p2) { return !Checker.this.computeLabel(p1).equals(Checker.this.computeLabel(p2)); } void readSuperClass(CClassInfo base) {
/*     */         for (; base != null; base = base.getBaseClass()) {
/*     */           for (CPropertyInfo p : base.getProperties())
/*     */             this.occupiedLabels.put(p.getName(true), p);
/*     */         }
/*     */       } private NameCollisionChecker() {}
/*     */     } }
/*     */    private final class Builder implements XSTermVisitor {
/*     */     Builder(Map<XSParticle, String> markedParticles) {
/* 323 */       this.markedParticles = markedParticles;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private final Map<XSParticle, String> markedParticles;
/*     */
/*     */
/*     */
/*     */
/*     */     private boolean insideOptionalParticle;
/*     */
/*     */
/*     */
/*     */
/*     */     private boolean marked(XSParticle p) {
/* 341 */       return this.markedParticles.containsKey(p);
/*     */     }
/*     */
/*     */     private String getLabel(XSParticle p) {
/* 345 */       return this.markedParticles.get(p);
/*     */     }
/*     */
/*     */     public void particle(XSParticle p) {
/* 349 */       XSTerm t = p.getTerm();
/*     */
/* 351 */       if (marked(p)) {
/* 352 */         BIProperty cust = BIProperty.getCustomization((XSComponent)p);
/* 353 */         CPropertyInfo prop = cust.createElementOrReferenceProperty(
/* 354 */             getLabel(p), false, p, RawTypeSetBuilder.build(p, this.insideOptionalParticle));
/* 355 */         DefaultParticleBinder.this.getCurrentBean().addProperty(prop);
/*     */       } else {
/*     */
/* 358 */         assert !p.isRepeated();
/*     */
/* 360 */         boolean oldIOP = this.insideOptionalParticle;
/* 361 */         this.insideOptionalParticle |= BigInteger.ZERO.equals(p.getMinOccurs());
/*     */
/* 363 */         t.visit(this);
/* 364 */         this.insideOptionalParticle = oldIOP;
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public void elementDecl(XSElementDecl e) {
/*     */       assert false;
/*     */     }
/*     */
/*     */
/*     */     public void wildcard(XSWildcard wc) {
/*     */       assert false;
/*     */     }
/*     */
/*     */     public void modelGroupDecl(XSModelGroupDecl decl) {
/* 379 */       modelGroup(decl.getModelGroup());
/*     */     }
/*     */
/*     */     public void modelGroup(XSModelGroup mg) {
/* 383 */       boolean oldIOP = this.insideOptionalParticle;
/* 384 */       this.insideOptionalParticle |= (mg.getCompositor() == XSModelGroup.CHOICE) ? 1 : 0;
/*     */
/* 386 */       for (XSParticle p : mg.getChildren()) {
/* 387 */         particle(p);
/*     */       }
/* 389 */       this.insideOptionalParticle = oldIOP;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\DefaultParticleBinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
