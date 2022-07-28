/*     */ package com.sun.xml.internal.rngom.binary;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PatternBuilder
/*     */ {
/*     */   private final EmptyPattern empty;
/*     */   protected final NotAllowedPattern notAllowed;
/*     */   protected final PatternInterner interner;
/*     */   
/*     */   public PatternBuilder() {
/*  54 */     this.empty = new EmptyPattern();
/*  55 */     this.notAllowed = new NotAllowedPattern();
/*  56 */     this.interner = new PatternInterner();
/*     */   }
/*     */   
/*     */   public PatternBuilder(PatternBuilder parent) {
/*  60 */     this.empty = parent.empty;
/*  61 */     this.notAllowed = parent.notAllowed;
/*  62 */     this.interner = new PatternInterner(parent.interner);
/*     */   }
/*     */   
/*     */   Pattern makeEmpty() {
/*  66 */     return this.empty;
/*     */   }
/*     */   
/*     */   Pattern makeNotAllowed() {
/*  70 */     return this.notAllowed;
/*     */   }
/*     */   
/*     */   Pattern makeGroup(Pattern p1, Pattern p2) {
/*  74 */     if (p1 == this.empty)
/*  75 */       return p2; 
/*  76 */     if (p2 == this.empty)
/*  77 */       return p1; 
/*  78 */     if (p1 == this.notAllowed || p2 == this.notAllowed) {
/*  79 */       return this.notAllowed;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  84 */     Pattern p = new GroupPattern(p1, p2);
/*  85 */     return this.interner.intern(p);
/*     */   }
/*     */   
/*     */   Pattern makeInterleave(Pattern p1, Pattern p2) {
/*  89 */     if (p1 == this.empty)
/*  90 */       return p2; 
/*  91 */     if (p2 == this.empty)
/*  92 */       return p1; 
/*  93 */     if (p1 == this.notAllowed || p2 == this.notAllowed) {
/*  94 */       return this.notAllowed;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     Pattern p = new InterleavePattern(p1, p2);
/* 109 */     return this.interner.intern(p);
/*     */   }
/*     */   
/*     */   Pattern makeChoice(Pattern p1, Pattern p2) {
/* 113 */     if (p1 == this.empty && p2.isNullable())
/* 114 */       return p2; 
/* 115 */     if (p2 == this.empty && p1.isNullable())
/* 116 */       return p1; 
/* 117 */     Pattern p = new ChoicePattern(p1, p2);
/* 118 */     return this.interner.intern(p);
/*     */   }
/*     */   
/*     */   Pattern makeOneOrMore(Pattern p) {
/* 122 */     if (p == this.empty || p == this.notAllowed || p instanceof OneOrMorePattern)
/*     */     {
/*     */       
/* 125 */       return p; } 
/* 126 */     Pattern p1 = new OneOrMorePattern(p);
/* 127 */     return this.interner.intern(p1);
/*     */   }
/*     */   
/*     */   Pattern makeOptional(Pattern p) {
/* 131 */     return makeChoice(p, this.empty);
/*     */   }
/*     */   
/*     */   Pattern makeZeroOrMore(Pattern p) {
/* 135 */     return makeOptional(makeOneOrMore(p));
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\PatternBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */