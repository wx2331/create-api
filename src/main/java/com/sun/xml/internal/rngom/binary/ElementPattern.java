/*     */ package com.sun.xml.internal.rngom.binary;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.binary.visitor.PatternFunction;
/*     */ import com.sun.xml.internal.rngom.binary.visitor.PatternVisitor;
/*     */ import com.sun.xml.internal.rngom.nc.NameClass;
/*     */ import org.xml.sax.Locator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ElementPattern
/*     */   extends Pattern
/*     */ {
/*     */   private Pattern p;
/*     */   private NameClass origNameClass;
/*     */   private NameClass nameClass;
/*     */   private boolean expanded = false;
/*     */   private boolean checkedRestrictions = false;
/*     */   private Locator loc;
/*     */   
/*     */   ElementPattern(NameClass nameClass, Pattern p, Locator loc) {
/*  63 */     super(false, 1, 
/*     */         
/*  65 */         combineHashCode(23, nameClass
/*  66 */           .hashCode(), p
/*  67 */           .hashCode()));
/*  68 */     this.nameClass = nameClass;
/*  69 */     this.origNameClass = nameClass;
/*  70 */     this.p = p;
/*  71 */     this.loc = loc;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/*  77 */     if (alpha != null)
/*  78 */       alpha.addElement(this.origNameClass); 
/*  79 */     if (this.checkedRestrictions)
/*     */       return; 
/*  81 */     switch (context) {
/*     */       case 7:
/*  83 */         throw new RestrictionViolationException("data_except_contains_element");
/*     */       case 6:
/*  85 */         throw new RestrictionViolationException("list_contains_element");
/*     */       case 5:
/*  87 */         throw new RestrictionViolationException("attribute_contains_element");
/*     */     } 
/*  89 */     this.checkedRestrictions = true;
/*     */     try {
/*  91 */       this.p.checkRestrictions(1, new DuplicateAttributeDetector(), null);
/*     */     }
/*  93 */     catch (RestrictionViolationException e) {
/*  94 */       this.checkedRestrictions = false;
/*  95 */       e.maybeSetLocator(this.loc);
/*  96 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   Pattern expand(SchemaPatternBuilder b) {
/* 102 */     if (!this.expanded) {
/* 103 */       this.expanded = true;
/* 104 */       this.p = this.p.expand(b);
/* 105 */       if (this.p.isNotAllowed())
/* 106 */         this.nameClass = NameClass.NULL; 
/*     */     } 
/* 108 */     return this;
/*     */   }
/*     */   
/*     */   boolean samePattern(Pattern other) {
/* 112 */     if (!(other instanceof ElementPattern))
/* 113 */       return false; 
/* 114 */     ElementPattern ep = (ElementPattern)other;
/* 115 */     return (this.nameClass.equals(ep.nameClass) && this.p == ep.p);
/*     */   }
/*     */ 
/*     */   
/*     */   void checkRecursion(int depth) throws SAXException {
/* 120 */     this.p.checkRecursion(depth + 1);
/*     */   }
/*     */   
/*     */   public void accept(PatternVisitor visitor) {
/* 124 */     visitor.visitElement(this.nameClass, this.p);
/*     */   }
/*     */   
/*     */   public Object apply(PatternFunction f) {
/* 128 */     return f.caseElement(this);
/*     */   }
/*     */   
/*     */   void setContent(Pattern p) {
/* 132 */     this.p = p;
/*     */   }
/*     */   
/*     */   public Pattern getContent() {
/* 136 */     return this.p;
/*     */   }
/*     */   
/*     */   public NameClass getNameClass() {
/* 140 */     return this.nameClass;
/*     */   }
/*     */   
/*     */   public Locator getLocator() {
/* 144 */     return this.loc;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\ElementPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */