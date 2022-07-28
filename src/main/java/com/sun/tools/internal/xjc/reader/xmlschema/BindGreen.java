/*     */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.ct.ComplexTypeFieldBuilder;
/*     */ import com.sun.xml.internal.xsom.XSAttContainer;
/*     */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeUse;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSContentType;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BindGreen
/*     */   extends ColorBinder
/*     */ {
/*  50 */   private final ComplexTypeFieldBuilder ctBuilder = (ComplexTypeFieldBuilder)Ring.get(ComplexTypeFieldBuilder.class);
/*     */   
/*     */   public void attGroupDecl(XSAttGroupDecl ag) {
/*  53 */     attContainer((XSAttContainer)ag);
/*     */   }
/*     */ 
/*     */   
/*     */   public void attContainer(XSAttContainer cont) {
/*  58 */     Iterator<XSAttributeUse> itr = cont.iterateDeclaredAttributeUses();
/*  59 */     while (itr.hasNext())
/*  60 */       this.builder.ying((XSComponent)itr.next(), (XSComponent)cont); 
/*  61 */     itr = cont.iterateAttGroups();
/*  62 */     while (itr.hasNext()) {
/*  63 */       this.builder.ying((XSComponent)itr.next(), (XSComponent)cont);
/*     */     }
/*  65 */     XSWildcard w = cont.getAttributeWildcard();
/*  66 */     if (w != null)
/*  67 */       this.builder.ying((XSComponent)w, (XSComponent)cont); 
/*     */   }
/*     */   
/*     */   public void complexType(XSComplexType ct) {
/*  71 */     this.ctBuilder.build(ct);
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
/*     */   public void attributeDecl(XSAttributeDecl xsAttributeDecl) {
/*  83 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void wildcard(XSWildcard xsWildcard) {
/*  88 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void modelGroupDecl(XSModelGroupDecl xsModelGroupDecl) {
/*  93 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void modelGroup(XSModelGroup xsModelGroup) {
/*  98 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void elementDecl(XSElementDecl xsElementDecl) {
/* 103 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void particle(XSParticle xsParticle) {
/* 108 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void empty(XSContentType xsContentType) {
/* 113 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void simpleType(XSSimpleType xsSimpleType) {
/* 124 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void attributeUse(XSAttributeUse use) {
/* 129 */     throw new IllegalStateException();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\BindGreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */