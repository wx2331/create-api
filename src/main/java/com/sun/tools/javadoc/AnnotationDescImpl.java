/*     */ package com.sun.tools.javadoc;
/*     */
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.AnnotationTypeElementDoc;
/*     */ import com.sun.javadoc.AnnotationValue;
/*     */ import com.sun.tools.javac.code.Attribute;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Pair;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class AnnotationDescImpl
/*     */   implements AnnotationDesc
/*     */ {
/*     */   private final DocEnv env;
/*     */   private final Attribute.Compound annotation;
/*     */
/*     */   AnnotationDescImpl(DocEnv paramDocEnv, Attribute.Compound paramCompound) {
/*  58 */     this.env = paramDocEnv;
/*  59 */     this.annotation = paramCompound;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public AnnotationTypeDoc annotationType() {
/*  66 */     Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)this.annotation.type.tsym;
/*  67 */     if (this.annotation.type.isErroneous()) {
/*  68 */       this.env.warning(null, "javadoc.class_not_found", this.annotation.type.toString());
/*  69 */       return new AnnotationTypeDocImpl(this.env, classSymbol);
/*     */     }
/*  71 */     return (AnnotationTypeDoc)this.env.getClassDoc(classSymbol);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public ElementValuePair[] elementValues() {
/*  82 */     List list = this.annotation.values;
/*  83 */     ElementValuePair[] arrayOfElementValuePair = new ElementValuePair[list.length()];
/*  84 */     byte b = 0;
/*  85 */     for (Pair pair : list) {
/*  86 */       arrayOfElementValuePair[b++] = new ElementValuePairImpl(this.env, (Symbol.MethodSymbol)pair.fst, (Attribute)pair.snd);
/*     */     }
/*  88 */     return arrayOfElementValuePair;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean isSynthesized() {
/*  97 */     return this.annotation.isSynthesized();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public String toString() {
/* 110 */     StringBuilder stringBuilder = new StringBuilder("@");
/* 111 */     stringBuilder.append(this.annotation.type.tsym);
/*     */
/* 113 */     ElementValuePair[] arrayOfElementValuePair = elementValues();
/* 114 */     if (arrayOfElementValuePair.length > 0) {
/* 115 */       stringBuilder.append('(');
/* 116 */       boolean bool = true;
/* 117 */       for (ElementValuePair elementValuePair : arrayOfElementValuePair) {
/* 118 */         if (!bool) {
/* 119 */           stringBuilder.append(", ");
/*     */         }
/* 121 */         bool = false;
/*     */
/* 123 */         String str = elementValuePair.element().name();
/* 124 */         if (arrayOfElementValuePair.length == 1 && str.equals("value")) {
/* 125 */           stringBuilder.append(elementValuePair.value());
/*     */         } else {
/* 127 */           stringBuilder.append(elementValuePair);
/*     */         }
/*     */       }
/* 130 */       stringBuilder.append(')');
/*     */     }
/* 132 */     return stringBuilder.toString();
/*     */   }
/*     */
/*     */
/*     */   public static class ElementValuePairImpl
/*     */     implements ElementValuePair
/*     */   {
/*     */     private final DocEnv env;
/*     */
/*     */     private final Symbol.MethodSymbol meth;
/*     */
/*     */     private final Attribute value;
/*     */
/*     */
/*     */     ElementValuePairImpl(DocEnv param1DocEnv, Symbol.MethodSymbol param1MethodSymbol, Attribute param1Attribute) {
/* 147 */       this.env = param1DocEnv;
/* 148 */       this.meth = param1MethodSymbol;
/* 149 */       this.value = param1Attribute;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     public AnnotationTypeElementDoc element() {
/* 156 */       return this.env.getAnnotationTypeElementDoc(this.meth);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     public AnnotationValue value() {
/* 163 */       return new AnnotationValueImpl(this.env, this.value);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public String toString() {
/* 172 */       return this.meth.name + "=" + value();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\AnnotationDescImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
