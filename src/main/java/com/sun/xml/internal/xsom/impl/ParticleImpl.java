/*     */ package com.sun.xml.internal.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSContentType;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSTerm;
/*     */ import com.sun.xml.internal.xsom.impl.parser.DelayedRef;
/*     */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.internal.xsom.visitor.XSContentTypeFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSContentTypeVisitor;
/*     */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*     */ import java.math.BigInteger;
/*     */ import java.util.List;
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
/*     */ public class ParticleImpl
/*     */   extends ComponentImpl
/*     */   implements XSParticle, ContentTypeImpl
/*     */ {
/*     */   private Ref.Term term;
/*     */   private BigInteger maxOccurs;
/*     */   private BigInteger minOccurs;
/*     */   
/*     */   public ParticleImpl(SchemaDocumentImpl owner, AnnotationImpl _ann, Ref.Term _term, Locator _loc, BigInteger _maxOccurs, BigInteger _minOccurs) {
/*  48 */     super(owner, _ann, _loc, null);
/*  49 */     this.term = _term;
/*  50 */     this.maxOccurs = _maxOccurs;
/*  51 */     this.minOccurs = _minOccurs;
/*     */   }
/*     */ 
/*     */   
/*     */   public ParticleImpl(SchemaDocumentImpl owner, AnnotationImpl _ann, Ref.Term _term, Locator _loc, int _maxOccurs, int _minOccurs) {
/*  56 */     super(owner, _ann, _loc, null);
/*  57 */     this.term = _term;
/*  58 */     this.maxOccurs = BigInteger.valueOf(_maxOccurs);
/*  59 */     this.minOccurs = BigInteger.valueOf(_minOccurs);
/*     */   }
/*     */   public ParticleImpl(SchemaDocumentImpl owner, AnnotationImpl _ann, Ref.Term _term, Locator _loc) {
/*  62 */     this(owner, _ann, _term, _loc, 1, 1);
/*     */   }
/*     */   
/*     */   public XSTerm getTerm() {
/*  66 */     return this.term.getTerm();
/*     */   }
/*     */   public BigInteger getMaxOccurs() {
/*  69 */     return this.maxOccurs;
/*     */   }
/*     */   public boolean isRepeated() {
/*  72 */     return (!this.maxOccurs.equals(BigInteger.ZERO) && !this.maxOccurs.equals(BigInteger.ONE));
/*     */   }
/*     */   
/*     */   public BigInteger getMinOccurs() {
/*  76 */     return this.minOccurs;
/*     */   }
/*     */   
/*     */   public void redefine(ModelGroupDeclImpl oldMG) {
/*  80 */     if (this.term instanceof ModelGroupImpl) {
/*  81 */       ((ModelGroupImpl)this.term).redefine(oldMG);
/*     */       return;
/*     */     } 
/*  84 */     if (this.term instanceof DelayedRef.ModelGroup) {
/*  85 */       ((DelayedRef)this.term).redefine(oldMG);
/*     */     }
/*     */   }
/*     */   
/*     */   public XSSimpleType asSimpleType() {
/*  90 */     return null; }
/*  91 */   public XSParticle asParticle() { return this; } public XSContentType asEmpty() {
/*  92 */     return null;
/*     */   }
/*     */   
/*     */   public final Object apply(XSFunction function) {
/*  96 */     return function.particle(this);
/*     */   }
/*     */   public final Object apply(XSContentTypeFunction function) {
/*  99 */     return function.particle(this);
/*     */   }
/*     */   public final void visit(XSVisitor visitor) {
/* 102 */     visitor.particle(this);
/*     */   }
/*     */   public final void visit(XSContentTypeVisitor visitor) {
/* 105 */     visitor.particle(this);
/*     */   }
/*     */   
/*     */   public XSContentType getContentType() {
/* 109 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getForeignAttributes() {
/* 117 */     return getTerm().getForeignAttributes();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\ParticleImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */