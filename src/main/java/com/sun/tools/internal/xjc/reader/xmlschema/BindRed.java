/*     */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.ct.ComplexTypeFieldBuilder;
/*     */ import com.sun.xml.internal.bind.v2.TODO;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BindRed
/*     */   extends ColorBinder
/*     */ {
/*  50 */   private final ComplexTypeFieldBuilder ctBuilder = (ComplexTypeFieldBuilder)Ring.get(ComplexTypeFieldBuilder.class);
/*     */   
/*     */   public void complexType(XSComplexType ct) {
/*  53 */     this.ctBuilder.build(ct);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void wildcard(XSWildcard xsWildcard) {
/*  60 */     TODO.checkSpec();
/*  61 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void elementDecl(XSElementDecl e) {
/*  65 */     SimpleTypeBuilder stb = (SimpleTypeBuilder)Ring.get(SimpleTypeBuilder.class);
/*  66 */     stb.refererStack.push(e);
/*  67 */     this.builder.ying((XSComponent)e.getType(), (XSComponent)e);
/*  68 */     stb.refererStack.pop();
/*     */   }
/*     */   
/*     */   public void simpleType(XSSimpleType type) {
/*  72 */     SimpleTypeBuilder stb = (SimpleTypeBuilder)Ring.get(SimpleTypeBuilder.class);
/*  73 */     stb.refererStack.push(type);
/*  74 */     createSimpleTypeProperty(type, "Value");
/*  75 */     stb.refererStack.pop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void attGroupDecl(XSAttGroupDecl ag) {
/*  82 */     throw new IllegalStateException();
/*     */   }
/*     */   public void attributeDecl(XSAttributeDecl ad) {
/*  85 */     throw new IllegalStateException();
/*     */   }
/*     */   public void attributeUse(XSAttributeUse au) {
/*  88 */     throw new IllegalStateException();
/*     */   }
/*     */   public void empty(XSContentType xsContentType) {
/*  91 */     throw new IllegalStateException();
/*     */   }
/*     */   public void modelGroupDecl(XSModelGroupDecl xsModelGroupDecl) {
/*  94 */     throw new IllegalStateException();
/*     */   }
/*     */   public void modelGroup(XSModelGroup xsModelGroup) {
/*  97 */     throw new IllegalStateException();
/*     */   }
/*     */   public void particle(XSParticle p) {
/* 100 */     throw new IllegalStateException();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\BindRed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */