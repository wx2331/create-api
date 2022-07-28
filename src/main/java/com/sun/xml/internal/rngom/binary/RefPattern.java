/*     */ package com.sun.xml.internal.rngom.binary;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.binary.visitor.PatternFunction;
/*     */ import com.sun.xml.internal.rngom.binary.visitor.PatternVisitor;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RefPattern
/*     */   extends Pattern
/*     */ {
/*     */   private Pattern p;
/*     */   private Locator refLoc;
/*     */   private String name;
/*  58 */   private int checkRecursionDepth = -1;
/*     */   private boolean combineImplicit = false;
/*  60 */   private byte combineType = 0;
/*  61 */   private byte replacementStatus = 0;
/*     */   
/*     */   private boolean expanded = false;
/*     */   
/*     */   static final byte REPLACEMENT_KEEP = 0;
/*     */   static final byte REPLACEMENT_REQUIRE = 1;
/*     */   static final byte REPLACEMENT_IGNORE = 2;
/*     */   static final byte COMBINE_NONE = 0;
/*     */   static final byte COMBINE_CHOICE = 1;
/*     */   static final byte COMBINE_INTERLEAVE = 2;
/*     */   
/*     */   RefPattern(String name) {
/*  73 */     this.name = name;
/*     */   }
/*     */   
/*     */   Pattern getPattern() {
/*  77 */     return this.p;
/*     */   }
/*     */   
/*     */   void setPattern(Pattern p) {
/*  81 */     this.p = p;
/*     */   }
/*     */   
/*     */   Locator getRefLocator() {
/*  85 */     return this.refLoc;
/*     */   }
/*     */   
/*     */   void setRefLocator(Locator loc) {
/*  89 */     this.refLoc = loc;
/*     */   }
/*     */ 
/*     */   
/*     */   void checkRecursion(int depth) throws SAXException {
/*  94 */     if (this.checkRecursionDepth == -1) {
/*  95 */       this.checkRecursionDepth = depth;
/*  96 */       this.p.checkRecursion(depth);
/*  97 */       this.checkRecursionDepth = -2;
/*     */     }
/*  99 */     else if (depth == this.checkRecursionDepth) {
/*     */       
/* 101 */       throw new SAXParseException(SchemaBuilderImpl.localizer.message("recursive_reference", this.name), this.refLoc);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   Pattern expand(SchemaPatternBuilder b) {
/* 107 */     if (!this.expanded) {
/* 108 */       this.p = this.p.expand(b);
/* 109 */       this.expanded = true;
/*     */     } 
/* 111 */     return this.p;
/*     */   }
/*     */   
/*     */   boolean samePattern(Pattern other) {
/* 115 */     return false;
/*     */   }
/*     */   
/*     */   public void accept(PatternVisitor visitor) {
/* 119 */     this.p.accept(visitor);
/*     */   }
/*     */   
/*     */   public Object apply(PatternFunction f) {
/* 123 */     return f.caseRef(this);
/*     */   }
/*     */   
/*     */   byte getReplacementStatus() {
/* 127 */     return this.replacementStatus;
/*     */   }
/*     */   
/*     */   void setReplacementStatus(byte replacementStatus) {
/* 131 */     this.replacementStatus = replacementStatus;
/*     */   }
/*     */   
/*     */   boolean isCombineImplicit() {
/* 135 */     return this.combineImplicit;
/*     */   }
/*     */   
/*     */   void setCombineImplicit() {
/* 139 */     this.combineImplicit = true;
/*     */   }
/*     */   
/*     */   byte getCombineType() {
/* 143 */     return this.combineType;
/*     */   }
/*     */   
/*     */   void setCombineType(byte combineType) {
/* 147 */     this.combineType = combineType;
/*     */   }
/*     */   
/*     */   String getName() {
/* 151 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\RefPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */