/*     */ package com.sun.xml.internal.rngom.digested;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
/*     */ import com.sun.xml.internal.rngom.parse.Parseable;
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
/*     */ public abstract class DPattern
/*     */   implements ParsedPattern
/*     */ {
/*     */   Locator location;
/*     */   DAnnotation annotation;
/*     */   DPattern next;
/*     */   DPattern prev;
/*     */   
/*     */   public Locator getLocation() {
/*  71 */     return this.location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DAnnotation getAnnotation() {
/*  80 */     if (this.annotation == null) {
/*  81 */       return DAnnotation.EMPTY;
/*     */     }
/*  83 */     return this.annotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isNullable();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract <V> V accept(DPatternVisitor<V> paramDPatternVisitor);
/*     */ 
/*     */ 
/*     */   
/*     */   public Parseable createParseable() {
/*  99 */     return new PatternParseable(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isElement() {
/* 106 */     return this instanceof DElementPattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isAttribute() {
/* 113 */     return this instanceof DAttributePattern;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\digested\DPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */