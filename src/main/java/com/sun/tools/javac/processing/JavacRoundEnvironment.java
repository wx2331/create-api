/*     */ package com.sun.tools.javac.processing;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.annotation.processing.RoundEnvironment;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.util.ElementScanner8;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavacRoundEnvironment
/*     */   implements RoundEnvironment
/*     */ {
/*     */   private final boolean processingOver;
/*     */   private final boolean errorRaised;
/*     */   private final ProcessingEnvironment processingEnv;
/*     */   private final Set<? extends Element> rootElements;
/*     */   private static final String NOT_AN_ANNOTATION_TYPE = "The argument does not represent an annotation type: ";
/*     */   
/*     */   JavacRoundEnvironment(boolean paramBoolean1, boolean paramBoolean2, Set<? extends Element> paramSet, ProcessingEnvironment paramProcessingEnvironment) {
/*  59 */     this.processingOver = paramBoolean1;
/*  60 */     this.errorRaised = paramBoolean2;
/*  61 */     this.rootElements = paramSet;
/*  62 */     this.processingEnv = paramProcessingEnvironment;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  66 */     return String.format("[errorRaised=%b, rootElements=%s, processingOver=%b]", new Object[] {
/*  67 */           Boolean.valueOf(this.errorRaised), this.rootElements, 
/*     */           
/*  69 */           Boolean.valueOf(this.processingOver) });
/*     */   }
/*     */   
/*     */   public boolean processingOver() {
/*  73 */     return this.processingOver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean errorRaised() {
/*  84 */     return this.errorRaised;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<? extends Element> getRootElements() {
/*  94 */     return this.rootElements;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<? extends Element> getElementsAnnotatedWith(TypeElement paramTypeElement) {
/* 113 */     Set<?> set = Collections.emptySet();
/* 114 */     if (paramTypeElement.getKind() != ElementKind.ANNOTATION_TYPE) {
/* 115 */       throw new IllegalArgumentException("The argument does not represent an annotation type: " + paramTypeElement);
/*     */     }
/* 117 */     AnnotationSetScanner annotationSetScanner = new AnnotationSetScanner((Set)set);
/*     */ 
/*     */     
/* 120 */     for (Element element : this.rootElements) {
/* 121 */       set = annotationSetScanner.scan(element, paramTypeElement);
/*     */     }
/* 123 */     return (Set)set;
/*     */   }
/*     */ 
/*     */   
/*     */   private class AnnotationSetScanner
/*     */     extends ElementScanner8<Set<Element>, TypeElement>
/*     */   {
/* 130 */     Set<Element> annotatedElements = new LinkedHashSet<>();
/*     */     
/*     */     AnnotationSetScanner(Set<Element> param1Set) {
/* 133 */       super(param1Set);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Set<Element> visitType(TypeElement param1TypeElement1, TypeElement param1TypeElement2) {
/* 139 */       scan((Iterable)param1TypeElement1.getTypeParameters(), param1TypeElement2);
/* 140 */       return super.visitType(param1TypeElement1, param1TypeElement2);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Set<Element> visitExecutable(ExecutableElement param1ExecutableElement, TypeElement param1TypeElement) {
/* 146 */       scan((Iterable)param1ExecutableElement.getTypeParameters(), param1TypeElement);
/* 147 */       return super.visitExecutable(param1ExecutableElement, param1TypeElement);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Set<Element> scan(Element param1Element, TypeElement param1TypeElement) {
/* 153 */       List<? extends AnnotationMirror> list = JavacRoundEnvironment.this.processingEnv.getElementUtils().getAllAnnotationMirrors(param1Element);
/* 154 */       for (AnnotationMirror annotationMirror : list) {
/* 155 */         if (param1TypeElement.equals(annotationMirror.getAnnotationType().asElement()))
/* 156 */           this.annotatedElements.add(param1Element); 
/*     */       } 
/* 158 */       param1Element.accept(this, param1TypeElement);
/* 159 */       return this.annotatedElements;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<? extends Element> getElementsAnnotatedWith(Class<? extends Annotation> paramClass) {
/* 167 */     if (!paramClass.isAnnotation())
/* 168 */       throw new IllegalArgumentException("The argument does not represent an annotation type: " + paramClass); 
/* 169 */     String str = paramClass.getCanonicalName();
/* 170 */     if (str == null) {
/* 171 */       return Collections.emptySet();
/*     */     }
/* 173 */     TypeElement typeElement = this.processingEnv.getElementUtils().getTypeElement(str);
/* 174 */     if (typeElement == null) {
/* 175 */       return Collections.emptySet();
/*     */     }
/* 177 */     return getElementsAnnotatedWith(typeElement);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\processing\JavacRoundEnvironment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */