/*     */ package com.sun.xml.internal.rngom.digested;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DPatternWalker
/*     */   implements DPatternVisitor<Void>
/*     */ {
/*     */   public Void onAttribute(DAttributePattern p) {
/*  53 */     return onXmlToken(p);
/*     */   }
/*     */   
/*     */   protected Void onXmlToken(DXmlTokenPattern p) {
/*  57 */     return onUnary(p);
/*     */   }
/*     */   
/*     */   public Void onChoice(DChoicePattern p) {
/*  61 */     return onContainer(p);
/*     */   }
/*     */   
/*     */   protected Void onContainer(DContainerPattern p) {
/*  65 */     for (DPattern c = p.firstChild(); c != null; c = c.next)
/*  66 */       c.accept(this); 
/*  67 */     return null;
/*     */   }
/*     */   
/*     */   public Void onData(DDataPattern p) {
/*  71 */     return null;
/*     */   }
/*     */   
/*     */   public Void onElement(DElementPattern p) {
/*  75 */     return onXmlToken(p);
/*     */   }
/*     */   
/*     */   public Void onEmpty(DEmptyPattern p) {
/*  79 */     return null;
/*     */   }
/*     */   
/*     */   public Void onGrammar(DGrammarPattern p) {
/*  83 */     return p.getStart().<Void>accept(this);
/*     */   }
/*     */   
/*     */   public Void onGroup(DGroupPattern p) {
/*  87 */     return onContainer(p);
/*     */   }
/*     */   
/*     */   public Void onInterleave(DInterleavePattern p) {
/*  91 */     return onContainer(p);
/*     */   }
/*     */   
/*     */   public Void onList(DListPattern p) {
/*  95 */     return onUnary(p);
/*     */   }
/*     */   
/*     */   public Void onMixed(DMixedPattern p) {
/*  99 */     return onUnary(p);
/*     */   }
/*     */   
/*     */   public Void onNotAllowed(DNotAllowedPattern p) {
/* 103 */     return null;
/*     */   }
/*     */   
/*     */   public Void onOneOrMore(DOneOrMorePattern p) {
/* 107 */     return onUnary(p);
/*     */   }
/*     */   
/*     */   public Void onOptional(DOptionalPattern p) {
/* 111 */     return onUnary(p);
/*     */   }
/*     */   
/*     */   public Void onRef(DRefPattern p) {
/* 115 */     return p.getTarget().getPattern().<Void>accept(this);
/*     */   }
/*     */   
/*     */   public Void onText(DTextPattern p) {
/* 119 */     return null;
/*     */   }
/*     */   
/*     */   public Void onValue(DValuePattern p) {
/* 123 */     return null;
/*     */   }
/*     */   
/*     */   public Void onZeroOrMore(DZeroOrMorePattern p) {
/* 127 */     return onUnary(p);
/*     */   }
/*     */   
/*     */   protected Void onUnary(DUnaryPattern p) {
/* 131 */     return p.getChild().<Void>accept(this);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\digested\DPatternWalker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */