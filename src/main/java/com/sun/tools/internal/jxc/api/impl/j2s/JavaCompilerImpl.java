/*     */ package com.sun.tools.internal.jxc.api.impl.j2s;
/*     */ 
/*     */ import com.sun.tools.internal.jxc.ap.InlineAnnotationReaderImpl;
/*     */ import com.sun.tools.internal.jxc.model.nav.ApNavigator;
/*     */ import com.sun.tools.internal.xjc.api.J2SJAXBModel;
/*     */ import com.sun.tools.internal.xjc.api.JavaCompiler;
/*     */ import com.sun.tools.internal.xjc.api.Reference;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ErrorHandler;
/*     */ import com.sun.xml.internal.bind.v2.model.core.Ref;
/*     */ import com.sun.xml.internal.bind.v2.model.core.TypeInfoSet;
/*     */ import com.sun.xml.internal.bind.v2.model.impl.ModelBuilder;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.annotation.processing.Messager;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.xml.bind.annotation.XmlList;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaCompilerImpl
/*     */   implements JavaCompiler
/*     */ {
/*     */   public J2SJAXBModel bind(Collection<Reference> rootClasses, Map<QName, Reference> additionalElementDecls, String defaultNamespaceRemap, ProcessingEnvironment env) {
/*  69 */     ModelBuilder<TypeMirror, TypeElement, VariableElement, ExecutableElement> builder = new ModelBuilder<>((AnnotationReader<TypeMirror, TypeElement, VariableElement, ExecutableElement>)InlineAnnotationReaderImpl.theInstance, (Navigator<TypeMirror, TypeElement, VariableElement, ExecutableElement>)new ApNavigator(env), Collections.emptyMap(), defaultNamespaceRemap);
/*     */ 
/*     */     
/*  72 */     builder.setErrorHandler(new ErrorHandlerImpl(env.getMessager()));
/*     */     
/*  74 */     for (Reference ref : rootClasses) {
/*  75 */       TypeMirror t = ref.type;
/*     */       
/*  77 */       XmlJavaTypeAdapter xjta = ref.annotations.<XmlJavaTypeAdapter>getAnnotation(XmlJavaTypeAdapter.class);
/*  78 */       XmlList xl = ref.annotations.<XmlList>getAnnotation(XmlList.class);
/*     */       
/*  80 */       builder.getTypeInfo(new Ref<>(builder, t, xjta, xl));
/*     */     } 
/*     */     
/*  83 */     TypeInfoSet<TypeMirror, TypeElement, VariableElement, ExecutableElement> r = builder.link();
/*  84 */     if (r == null) return null;
/*     */     
/*  86 */     if (additionalElementDecls == null) {
/*  87 */       additionalElementDecls = Collections.emptyMap();
/*     */     } else {
/*     */       
/*  90 */       for (Map.Entry<QName, ? extends Reference> e : additionalElementDecls.entrySet()) {
/*  91 */         if (e.getKey() == null)
/*  92 */           throw new IllegalArgumentException("nulls in additionalElementDecls"); 
/*     */       } 
/*     */     } 
/*  95 */     return new JAXBModelImpl(r, builder.reader, rootClasses, new HashMap<>(additionalElementDecls));
/*     */   }
/*     */   
/*     */   private static final class ErrorHandlerImpl implements ErrorHandler {
/*     */     private final Messager messager;
/*     */     
/*     */     public ErrorHandlerImpl(Messager messager) {
/* 102 */       this.messager = messager;
/*     */     }
/*     */     
/*     */     public void error(IllegalAnnotationException e) {
/* 106 */       String error = e.toString();
/* 107 */       this.messager.printMessage(Diagnostic.Kind.ERROR, error);
/* 108 */       System.err.println(error);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\jxc\api\impl\j2s\JavaCompilerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */