/*     */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.Multiplicity;
/*     */ import com.sun.tools.internal.xjc.reader.RawTypeSet;
/*     */ import com.sun.tools.internal.xjc.reader.gbind.ConnectedComponent;
/*     */ import com.sun.tools.internal.xjc.reader.gbind.Element;
/*     */ import com.sun.tools.internal.xjc.reader.gbind.Expression;
/*     */ import com.sun.tools.internal.xjc.reader.gbind.Graph;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.xml.internal.bind.v2.model.core.WildcardMode;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ExpressionParticleBinder
/*     */   extends ParticleBinder
/*     */ {
/*     */   public void build(XSParticle p, Collection<XSParticle> forcedProps) {
/*  55 */     Expression tree = ExpressionBuilder.createTree(p);
/*  56 */     Graph g = new Graph(tree);
/*  57 */     for (ConnectedComponent cc : g) {
/*  58 */       buildProperty(cc);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buildProperty(ConnectedComponent cc) {
/*  66 */     StringBuilder propName = new StringBuilder();
/*  67 */     int nameTokenCount = 0;
/*     */     
/*  69 */     RawTypeSetBuilder rtsb = new RawTypeSetBuilder();
/*  70 */     for (Element e : cc) {
/*  71 */       GElement ge = (GElement)e;
/*     */       
/*  73 */       if (nameTokenCount < 3) {
/*  74 */         if (nameTokenCount != 0)
/*  75 */           propName.append("And"); 
/*  76 */         propName.append(makeJavaName(cc.isCollection(), ge.getPropertyNameSeed()));
/*  77 */         nameTokenCount++;
/*     */       } 
/*     */       
/*  80 */       if (e instanceof GElementImpl) {
/*  81 */         GElementImpl ei = (GElementImpl)e;
/*  82 */         rtsb.elementDecl(ei.decl);
/*     */         continue;
/*     */       } 
/*  85 */       if (e instanceof GWildcardElement) {
/*  86 */         GWildcardElement w = (GWildcardElement)e;
/*  87 */         rtsb.getRefs().add(new RawTypeSetBuilder.WildcardRef(
/*  88 */               w.isStrict() ? WildcardMode.STRICT : WildcardMode.SKIP));
/*     */         continue;
/*     */       } 
/*  91 */       assert false : e;
/*     */     } 
/*     */     
/*  94 */     Multiplicity m = Multiplicity.ONE;
/*  95 */     if (cc.isCollection())
/*  96 */       m = m.makeRepeated(); 
/*  97 */     if (!cc.isRequired()) {
/*  98 */       m = m.makeOptional();
/*     */     }
/* 100 */     RawTypeSet rts = new RawTypeSet(rtsb.getRefs(), m);
/*     */     
/* 102 */     XSParticle p = findSourceParticle(cc);
/*     */     
/* 104 */     BIProperty cust = BIProperty.getCustomization((XSComponent)p);
/* 105 */     CPropertyInfo prop = cust.createElementOrReferenceProperty(propName
/* 106 */         .toString(), false, p, rts);
/* 107 */     getCurrentBean().addProperty(prop);
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
/*     */   private XSParticle findSourceParticle(ConnectedComponent cc) {
/* 120 */     XSParticle first = null;
/*     */     
/* 122 */     for (Element e : cc) {
/* 123 */       GElement ge = (GElement)e;
/* 124 */       for (XSParticle p : ge.particles) {
/* 125 */         if (first == null)
/* 126 */           first = p; 
/* 127 */         if (getLocalPropCustomization(p) != null) {
/* 128 */           return p;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 136 */     return first;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkFallback(XSParticle p) {
/* 141 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\ExpressionParticleBinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */