/*     */ package com.sun.xml.internal.rngom.binary;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.binary.visitor.PatternFunction;
/*     */ import com.sun.xml.internal.rngom.binary.visitor.PatternVisitor;
/*     */ import com.sun.xml.internal.rngom.nc.NameClass;
/*     */ import com.sun.xml.internal.rngom.nc.SimpleNameClass;
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
/*     */ public final class AttributePattern
/*     */   extends Pattern
/*     */ {
/*     */   private NameClass nameClass;
/*     */   private Pattern p;
/*     */   private Locator loc;
/*     */   
/*     */   AttributePattern(NameClass nameClass, Pattern value, Locator loc) {
/*  61 */     super(false, 0, 
/*     */         
/*  63 */         combineHashCode(29, nameClass
/*  64 */           .hashCode(), value
/*  65 */           .hashCode()));
/*  66 */     this.nameClass = nameClass;
/*  67 */     this.p = value;
/*  68 */     this.loc = loc;
/*     */   }
/*     */   
/*     */   Pattern expand(SchemaPatternBuilder b) {
/*  72 */     Pattern ep = this.p.expand(b);
/*  73 */     if (ep != this.p) {
/*  74 */       return b.makeAttribute(this.nameClass, ep, this.loc);
/*     */     }
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException {
/*  81 */     switch (context) {
/*     */       case 0:
/*  83 */         throw new RestrictionViolationException("start_contains_attribute");
/*     */       case 1:
/*  85 */         if (this.nameClass.isOpen())
/*  86 */           throw new RestrictionViolationException("open_name_class_not_repeated"); 
/*     */         break;
/*     */       case 3:
/*  89 */         throw new RestrictionViolationException("one_or_more_contains_group_contains_attribute");
/*     */       case 4:
/*  91 */         throw new RestrictionViolationException("one_or_more_contains_interleave_contains_attribute");
/*     */       case 6:
/*  93 */         throw new RestrictionViolationException("list_contains_attribute");
/*     */       case 5:
/*  95 */         throw new RestrictionViolationException("attribute_contains_attribute");
/*     */       case 7:
/*  97 */         throw new RestrictionViolationException("data_except_contains_attribute");
/*     */     } 
/*  99 */     if (!dad.addAttribute(this.nameClass)) {
/* 100 */       if (this.nameClass instanceof SimpleNameClass) {
/* 101 */         throw new RestrictionViolationException("duplicate_attribute_detail", ((SimpleNameClass)this.nameClass).name);
/*     */       }
/* 103 */       throw new RestrictionViolationException("duplicate_attribute");
/*     */     } 
/*     */     try {
/* 106 */       this.p.checkRestrictions(5, null, null);
/*     */     }
/* 108 */     catch (RestrictionViolationException e) {
/* 109 */       e.maybeSetLocator(this.loc);
/* 110 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   boolean samePattern(Pattern other) {
/* 115 */     if (!(other instanceof AttributePattern))
/* 116 */       return false; 
/* 117 */     AttributePattern ap = (AttributePattern)other;
/* 118 */     return (this.nameClass.equals(ap.nameClass) && this.p == ap.p);
/*     */   }
/*     */   
/*     */   void checkRecursion(int depth) throws SAXException {
/* 122 */     this.p.checkRecursion(depth);
/*     */   }
/*     */   
/*     */   public void accept(PatternVisitor visitor) {
/* 126 */     visitor.visitAttribute(this.nameClass, this.p);
/*     */   }
/*     */   
/*     */   public Object apply(PatternFunction f) {
/* 130 */     return f.caseAttribute(this);
/*     */   }
/*     */   
/*     */   public Pattern getContent() {
/* 134 */     return this.p;
/*     */   }
/*     */   
/*     */   public NameClass getNameClass() {
/* 138 */     return this.nameClass;
/*     */   }
/*     */   
/*     */   public Locator getLocator() {
/* 142 */     return this.loc;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\AttributePattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */