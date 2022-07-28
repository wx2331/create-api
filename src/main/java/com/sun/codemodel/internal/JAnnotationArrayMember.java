/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JAnnotationArrayMember
/*     */   extends JAnnotationValue
/*     */   implements JAnnotatable
/*     */ {
/*  46 */   private final List<JAnnotationValue> values = new ArrayList<>();
/*     */   private final JCodeModel owner;
/*     */   
/*     */   JAnnotationArrayMember(JCodeModel owner) {
/*  50 */     this.owner = owner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationArrayMember param(String value) {
/*  61 */     JAnnotationValue annotationValue = new JAnnotationStringValue(JExpr.lit(value));
/*  62 */     this.values.add(annotationValue);
/*  63 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationArrayMember param(boolean value) {
/*  74 */     JAnnotationValue annotationValue = new JAnnotationStringValue(JExpr.lit(value));
/*  75 */     this.values.add(annotationValue);
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationArrayMember param(byte value) {
/*  87 */     JAnnotationValue annotationValue = new JAnnotationStringValue(JExpr.lit(value));
/*  88 */     this.values.add(annotationValue);
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationArrayMember param(char value) {
/* 100 */     JAnnotationValue annotationValue = new JAnnotationStringValue(JExpr.lit(value));
/* 101 */     this.values.add(annotationValue);
/* 102 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationArrayMember param(double value) {
/* 113 */     JAnnotationValue annotationValue = new JAnnotationStringValue(JExpr.lit(value));
/* 114 */     this.values.add(annotationValue);
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationArrayMember param(long value) {
/* 126 */     JAnnotationValue annotationValue = new JAnnotationStringValue(JExpr.lit(value));
/* 127 */     this.values.add(annotationValue);
/* 128 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationArrayMember param(short value) {
/* 139 */     JAnnotationValue annotationValue = new JAnnotationStringValue(JExpr.lit(value));
/* 140 */     this.values.add(annotationValue);
/* 141 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationArrayMember param(int value) {
/* 152 */     JAnnotationValue annotationValue = new JAnnotationStringValue(JExpr.lit(value));
/* 153 */     this.values.add(annotationValue);
/* 154 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationArrayMember param(float value) {
/* 165 */     JAnnotationValue annotationValue = new JAnnotationStringValue(JExpr.lit(value));
/* 166 */     this.values.add(annotationValue);
/* 167 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationArrayMember param(final Enum<?> value) {
/* 178 */     JAnnotationValue annotationValue = new JAnnotationValue() {
/*     */         public void generate(JFormatter f) {
/* 180 */           f.t(JAnnotationArrayMember.this.owner.ref(value.getDeclaringClass())).p('.').p(value.name());
/*     */         }
/*     */       };
/* 183 */     this.values.add(annotationValue);
/* 184 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationArrayMember param(JEnumConstant value) {
/* 195 */     JAnnotationValue annotationValue = new JAnnotationStringValue(value);
/* 196 */     this.values.add(annotationValue);
/* 197 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationArrayMember param(JExpression value) {
/* 208 */     JAnnotationValue annotationValue = new JAnnotationStringValue(value);
/* 209 */     this.values.add(annotationValue);
/* 210 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationArrayMember param(final Class<?> value) {
/* 221 */     JAnnotationValue annotationValue = new JAnnotationStringValue(new JExpressionImpl()
/*     */         {
/*     */           public void generate(JFormatter f) {
/* 224 */             f.p(value.getName().replace('$', '.'));
/* 225 */             f.p(".class");
/*     */           }
/*     */         });
/* 228 */     this.values.add(annotationValue);
/* 229 */     return this;
/*     */   }
/*     */   
/*     */   public JAnnotationArrayMember param(JType type) {
/* 233 */     JClass clazz = type.boxify();
/* 234 */     JAnnotationValue annotationValue = new JAnnotationStringValue(clazz.dotclass());
/* 235 */     this.values.add(annotationValue);
/* 236 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationUse annotate(Class<? extends Annotation> clazz) {
/* 243 */     return annotate(this.owner.ref(clazz));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationUse annotate(JClass clazz) {
/* 250 */     JAnnotationUse a = new JAnnotationUse(clazz);
/* 251 */     this.values.add(a);
/* 252 */     return a;
/*     */   }
/*     */   
/*     */   public <W extends JAnnotationWriter> W annotate2(Class<W> clazz) {
/* 256 */     return (W)TypedAnnotationWriter.create(clazz, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<JAnnotationUse> annotations() {
/* 266 */     return (Collection)Collections.unmodifiableList(this.values);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationArrayMember param(JAnnotationUse value) {
/* 282 */     this.values.add(value);
/* 283 */     return this;
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 287 */     f.p('{').nl().i();
/*     */     
/* 289 */     boolean first = true;
/* 290 */     for (JAnnotationValue aValue : this.values) {
/* 291 */       if (!first)
/* 292 */         f.p(',').nl(); 
/* 293 */       f.g(aValue);
/* 294 */       first = false;
/*     */     } 
/* 296 */     f.nl().o().p('}');
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JAnnotationArrayMember.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */