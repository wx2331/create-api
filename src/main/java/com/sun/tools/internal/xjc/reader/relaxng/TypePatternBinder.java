/*     */ package com.sun.tools.internal.xjc.reader.relaxng;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.digested.DAttributePattern;
/*     */ import com.sun.xml.internal.rngom.digested.DChoicePattern;
/*     */ import com.sun.xml.internal.rngom.digested.DDefine;
/*     */ import com.sun.xml.internal.rngom.digested.DListPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DMixedPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DOneOrMorePattern;
/*     */ import com.sun.xml.internal.rngom.digested.DOptionalPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DPatternWalker;
/*     */ import com.sun.xml.internal.rngom.digested.DRefPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DZeroOrMorePattern;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class TypePatternBinder
/*     */   extends DPatternWalker
/*     */ {
/*     */   private boolean canInherit;
/*  50 */   private final Stack<Boolean> stack = new Stack<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   private final Set<DDefine> cannotBeInherited = new HashSet<>();
/*     */ 
/*     */   
/*     */   void reset() {
/*  59 */     this.canInherit = true;
/*  60 */     this.stack.clear();
/*     */   }
/*     */   
/*     */   public Void onRef(DRefPattern p) {
/*  64 */     if (!this.canInherit) {
/*  65 */       this.cannotBeInherited.add(p.getTarget());
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/*  72 */       this.canInherit = false;
/*     */     } 
/*  74 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Void onChoice(DChoicePattern p) {
/*  84 */     push(false);
/*  85 */     super.onChoice(p);
/*  86 */     pop();
/*  87 */     return null;
/*     */   }
/*     */   
/*     */   public Void onAttribute(DAttributePattern p) {
/*  91 */     push(false);
/*  92 */     super.onAttribute(p);
/*  93 */     pop();
/*  94 */     return null;
/*     */   }
/*     */   
/*     */   public Void onList(DListPattern p) {
/*  98 */     push(false);
/*  99 */     super.onList(p);
/* 100 */     pop();
/* 101 */     return null;
/*     */   }
/*     */   
/*     */   public Void onMixed(DMixedPattern p) {
/* 105 */     push(false);
/* 106 */     super.onMixed(p);
/* 107 */     pop();
/* 108 */     return null;
/*     */   }
/*     */   
/*     */   public Void onOneOrMore(DOneOrMorePattern p) {
/* 112 */     push(false);
/* 113 */     super.onOneOrMore(p);
/* 114 */     pop();
/* 115 */     return null;
/*     */   }
/*     */   
/*     */   public Void onZeroOrMore(DZeroOrMorePattern p) {
/* 119 */     push(false);
/* 120 */     super.onZeroOrMore(p);
/* 121 */     pop();
/* 122 */     return null;
/*     */   }
/*     */   
/*     */   public Void onOptional(DOptionalPattern p) {
/* 126 */     push(false);
/* 127 */     super.onOptional(p);
/* 128 */     pop();
/* 129 */     return null;
/*     */   }
/*     */   
/*     */   private void push(boolean v) {
/* 133 */     this.stack.push(Boolean.valueOf(this.canInherit));
/* 134 */     this.canInherit = v;
/*     */   }
/*     */   
/*     */   private void pop() {
/* 138 */     this.canInherit = ((Boolean)this.stack.pop()).booleanValue();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\relaxng\TypePatternBinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */