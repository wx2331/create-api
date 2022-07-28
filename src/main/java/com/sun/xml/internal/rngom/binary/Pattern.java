/*     */ package com.sun.xml.internal.rngom.binary;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
/*     */ import com.sun.xml.internal.rngom.binary.visitor.PatternFunction;
/*     */ import com.sun.xml.internal.rngom.binary.visitor.PatternVisitor;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Pattern
/*     */   implements ParsedPattern
/*     */ {
/*     */   private boolean nullable;
/*     */   private int hc;
/*     */   private int contentType;
/*     */   static final int TEXT_HASH_CODE = 1;
/*     */   static final int ERROR_HASH_CODE = 3;
/*     */   static final int EMPTY_HASH_CODE = 5;
/*     */   static final int NOT_ALLOWED_HASH_CODE = 7;
/*     */   static final int CHOICE_HASH_CODE = 11;
/*     */   static final int GROUP_HASH_CODE = 13;
/*     */   static final int INTERLEAVE_HASH_CODE = 17;
/*     */   static final int ONE_OR_MORE_HASH_CODE = 19;
/*     */   static final int ELEMENT_HASH_CODE = 23;
/*     */   static final int VALUE_HASH_CODE = 27;
/*     */   static final int ATTRIBUTE_HASH_CODE = 29;
/*     */   static final int DATA_HASH_CODE = 31;
/*     */   static final int LIST_HASH_CODE = 37;
/*     */   static final int AFTER_HASH_CODE = 41;
/*     */   static final int EMPTY_CONTENT_TYPE = 0;
/*     */   static final int ELEMENT_CONTENT_TYPE = 1;
/*     */   static final int MIXED_CONTENT_TYPE = 2;
/*     */   static final int DATA_CONTENT_TYPE = 3;
/*     */   static final int START_CONTEXT = 0;
/*     */   static final int ELEMENT_CONTEXT = 1;
/*     */   static final int ELEMENT_REPEAT_CONTEXT = 2;
/*     */   static final int ELEMENT_REPEAT_GROUP_CONTEXT = 3;
/*     */   static final int ELEMENT_REPEAT_INTERLEAVE_CONTEXT = 4;
/*     */   static final int ATTRIBUTE_CONTEXT = 5;
/*     */   static final int LIST_CONTEXT = 6;
/*     */   static final int DATA_EXCEPT_CONTEXT = 7;
/*     */   
/*     */   static int combineHashCode(int hc1, int hc2, int hc3) {
/*  74 */     return hc1 * hc2 * hc3;
/*     */   }
/*     */   
/*     */   static int combineHashCode(int hc1, int hc2) {
/*  78 */     return hc1 * hc2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Pattern(boolean nullable, int contentType, int hc) {
/*  87 */     this.nullable = nullable;
/*  88 */     this.contentType = contentType;
/*  89 */     this.hc = hc;
/*     */   }
/*     */   
/*     */   Pattern() {
/*  93 */     this.nullable = false;
/*  94 */     this.hc = hashCode();
/*  95 */     this.contentType = 0;
/*     */   }
/*     */   
/*     */   void checkRecursion(int depth) throws SAXException {}
/*     */   
/*     */   Pattern expand(SchemaPatternBuilder b) {
/* 101 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isNullable() {
/* 111 */     return this.nullable;
/*     */   }
/*     */   
/*     */   boolean isNotAllowed() {
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract boolean samePattern(Pattern paramPattern);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final int patternHashCode() {
/* 135 */     return this.hc;
/*     */   }
/*     */   
/*     */   final int getContentType() {
/* 139 */     return this.contentType;
/*     */   }
/*     */   
/*     */   boolean containsChoice(Pattern p) {
/* 143 */     return (this == p);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void accept(PatternVisitor paramPatternVisitor);
/*     */ 
/*     */   
/*     */   public abstract Object apply(PatternFunction paramPatternFunction);
/*     */ 
/*     */   
/*     */   static boolean contentTypeGroupable(int ct1, int ct2) {
/* 154 */     if (ct1 == 0 || ct2 == 0)
/* 155 */       return true; 
/* 156 */     if (ct1 == 3 || ct2 == 3)
/* 157 */       return false; 
/* 158 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\Pattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */