/*     */ package com.sun.xml.internal.rngom.binary;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.nc.NameClass;
/*     */ import org.relaxng.datatype.Datatype;
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
/*     */ public class SchemaPatternBuilder
/*     */   extends PatternBuilder
/*     */ {
/*     */   private boolean idTypes;
/*     */   
/*  54 */   private final Pattern unexpandedNotAllowed = new NotAllowedPattern()
/*     */     {
/*     */       boolean isNotAllowed()
/*     */       {
/*  58 */         return false;
/*     */       }
/*     */       
/*     */       Pattern expand(SchemaPatternBuilder b) {
/*  62 */         return b.makeNotAllowed();
/*     */       }
/*     */     };
/*     */   
/*  66 */   private final TextPattern text = new TextPattern();
/*  67 */   private final PatternInterner schemaInterner = new PatternInterner();
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasIdTypes() {
/*  72 */     return this.idTypes;
/*     */   }
/*     */   
/*     */   Pattern makeElement(NameClass nameClass, Pattern content, Locator loc) {
/*  76 */     Pattern p = new ElementPattern(nameClass, content, loc);
/*  77 */     return this.schemaInterner.intern(p);
/*     */   }
/*     */   
/*     */   Pattern makeAttribute(NameClass nameClass, Pattern value, Locator loc) {
/*  81 */     if (value == this.notAllowed)
/*  82 */       return value; 
/*  83 */     Pattern p = new AttributePattern(nameClass, value, loc);
/*  84 */     return this.schemaInterner.intern(p);
/*     */   }
/*     */   
/*     */   Pattern makeData(Datatype dt) {
/*  88 */     noteDatatype(dt);
/*  89 */     Pattern p = new DataPattern(dt);
/*  90 */     return this.schemaInterner.intern(p);
/*     */   }
/*     */   
/*     */   Pattern makeDataExcept(Datatype dt, Pattern except, Locator loc) {
/*  94 */     noteDatatype(dt);
/*  95 */     Pattern p = new DataExceptPattern(dt, except, loc);
/*  96 */     return this.schemaInterner.intern(p);
/*     */   }
/*     */   
/*     */   Pattern makeValue(Datatype dt, Object obj) {
/* 100 */     noteDatatype(dt);
/* 101 */     Pattern p = new ValuePattern(dt, obj);
/* 102 */     return this.schemaInterner.intern(p);
/*     */   }
/*     */   
/*     */   Pattern makeText() {
/* 106 */     return this.text;
/*     */   }
/*     */ 
/*     */   
/*     */   Pattern makeOneOrMore(Pattern p) {
/* 111 */     if (p == this.text)
/* 112 */       return p; 
/* 113 */     return super.makeOneOrMore(p);
/*     */   }
/*     */   
/*     */   Pattern makeUnexpandedNotAllowed() {
/* 117 */     return this.unexpandedNotAllowed;
/*     */   }
/*     */   
/*     */   Pattern makeError() {
/* 121 */     Pattern p = new ErrorPattern();
/* 122 */     return this.schemaInterner.intern(p);
/*     */   }
/*     */ 
/*     */   
/*     */   Pattern makeChoice(Pattern p1, Pattern p2) {
/* 127 */     if (p1 == this.notAllowed || p1 == p2)
/* 128 */       return p2; 
/* 129 */     if (p2 == this.notAllowed)
/* 130 */       return p1; 
/* 131 */     return super.makeChoice(p1, p2);
/*     */   }
/*     */   
/*     */   Pattern makeList(Pattern p, Locator loc) {
/* 135 */     if (p == this.notAllowed)
/* 136 */       return p; 
/* 137 */     Pattern p1 = new ListPattern(p, loc);
/* 138 */     return this.schemaInterner.intern(p1);
/*     */   }
/*     */   
/*     */   Pattern makeMixed(Pattern p) {
/* 142 */     return makeInterleave(this.text, p);
/*     */   }
/*     */   
/*     */   private void noteDatatype(Datatype dt) {
/* 146 */     if (dt.getIdType() != 0)
/* 147 */       this.idTypes = true; 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\SchemaPatternBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */