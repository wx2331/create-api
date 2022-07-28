/*     */ package com.sun.tools.javac.model;
/*     */
/*     */ import com.sun.tools.javac.code.Attribute;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Pair;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.AnnotationTypeMismatchException;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.lang.model.type.MirroredTypeException;
/*     */ import javax.lang.model.type.MirroredTypesException;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import sun.reflect.annotation.AnnotationParser;
/*     */ import sun.reflect.annotation.AnnotationType;
/*     */ import sun.reflect.annotation.EnumConstantNotPresentExceptionProxy;
/*     */ import sun.reflect.annotation.ExceptionProxy;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class AnnotationProxyMaker
/*     */ {
/*     */   private final Attribute.Compound anno;
/*     */   private final Class<? extends Annotation> annoType;
/*     */
/*     */   private AnnotationProxyMaker(Attribute.Compound paramCompound, Class<? extends Annotation> paramClass) {
/*  68 */     this.anno = paramCompound;
/*  69 */     this.annoType = paramClass;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static <A extends Annotation> A generateAnnotation(Attribute.Compound paramCompound, Class<A> paramClass) {
/*  78 */     AnnotationProxyMaker annotationProxyMaker = new AnnotationProxyMaker(paramCompound, paramClass);
/*  79 */     return paramClass.cast(annotationProxyMaker.generateAnnotation());
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private Annotation generateAnnotation() {
/*  87 */     return AnnotationParser.annotationForMap(this.annoType,
/*  88 */         getAllReflectedValues());
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private Map<String, Object> getAllReflectedValues() {
/*  97 */     LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
/*     */
/*     */
/* 100 */     for (Map.Entry<Symbol.MethodSymbol, Attribute> entry : getAllValues().entrySet()) {
/* 101 */       Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol)entry.getKey();
/* 102 */       Object object = generateValue(methodSymbol, (Attribute)entry.getValue());
/* 103 */       if (object != null) {
/* 104 */         linkedHashMap.put(methodSymbol.name.toString(), object);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/* 110 */     return (Map)linkedHashMap;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private Map<Symbol.MethodSymbol, Attribute> getAllValues() {
/* 118 */     LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
/*     */
/*     */
/*     */
/* 122 */     Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)this.anno.type.tsym;
/* 123 */     for (Scope.Entry entry = (classSymbol.members()).elems; entry != null; entry = entry.sibling) {
/* 124 */       if (entry.sym.kind == 16) {
/* 125 */         Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol)entry.sym;
/* 126 */         Attribute attribute = methodSymbol.getDefaultValue();
/* 127 */         if (attribute != null) {
/* 128 */           linkedHashMap.put(methodSymbol, attribute);
/*     */         }
/*     */       }
/*     */     }
/* 132 */     for (Pair pair : this.anno.values)
/* 133 */       linkedHashMap.put(pair.fst, pair.snd);
/* 134 */     return (Map)linkedHashMap;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private Object generateValue(Symbol.MethodSymbol paramMethodSymbol, Attribute paramAttribute) {
/* 143 */     ValueVisitor valueVisitor = new ValueVisitor(paramMethodSymbol);
/* 144 */     return valueVisitor.getValue(paramAttribute);
/*     */   }
/*     */
/*     */   private class ValueVisitor
/*     */     implements Attribute.Visitor
/*     */   {
/*     */     private Symbol.MethodSymbol meth;
/*     */     private Class<?> returnClass;
/*     */     private Object value;
/*     */
/*     */     ValueVisitor(Symbol.MethodSymbol param1MethodSymbol) {
/* 155 */       this.meth = param1MethodSymbol;
/*     */     }
/*     */
/*     */     Object getValue(Attribute param1Attribute) {
/*     */       Method method;
/*     */       try {
/* 161 */         method = AnnotationProxyMaker.this.annoType.getMethod(this.meth.name.toString(), new Class[0]);
/* 162 */       } catch (NoSuchMethodException noSuchMethodException) {
/* 163 */         return null;
/*     */       }
/* 165 */       this.returnClass = method.getReturnType();
/* 166 */       param1Attribute.accept(this);
/* 167 */       if (!(this.value instanceof ExceptionProxy) &&
/*     */
/* 169 */         !AnnotationType.invocationHandlerReturnType(this.returnClass).isInstance(this.value)) {
/* 170 */         typeMismatch(method, param1Attribute);
/*     */       }
/* 172 */       return this.value;
/*     */     }
/*     */
/*     */
/*     */     public void visitConstant(Attribute.Constant param1Constant) {
/* 177 */       this.value = param1Constant.getValue();
/*     */     }
/*     */
/*     */     public void visitClass(Attribute.Class param1Class) {
/* 181 */       this.value = new MirroredTypeExceptionProxy((TypeMirror)param1Class.classType);
/*     */     }
/*     */
/*     */     public void visitArray(Attribute.Array param1Array) {
/* 185 */       Name name = ((Type.ArrayType)param1Array.type).elemtype.tsym.getQualifiedName();
/*     */
/* 187 */       if (name.equals(name.table.names.java_lang_Class)) {
/*     */
/* 189 */         ListBuffer listBuffer = new ListBuffer();
/* 190 */         for (Attribute attribute : param1Array.values) {
/* 191 */           Type type = ((Attribute.Class)attribute).classType;
/* 192 */           listBuffer.append(type);
/*     */         }
/* 194 */         this.value = new MirroredTypesExceptionProxy(listBuffer.toList());
/*     */       } else {
/*     */
/* 197 */         int i = param1Array.values.length;
/* 198 */         Class<?> clazz = this.returnClass;
/* 199 */         this.returnClass = this.returnClass.getComponentType();
/*     */         try {
/* 201 */           Object object = Array.newInstance(this.returnClass, i);
/* 202 */           for (byte b = 0; b < i; b++) {
/* 203 */             param1Array.values[b].accept(this);
/* 204 */             if (this.value == null || this.value instanceof ExceptionProxy) {
/*     */               return;
/*     */             }
/*     */             try {
/* 208 */               Array.set(object, b, this.value);
/* 209 */             } catch (IllegalArgumentException illegalArgumentException) {
/* 210 */               this.value = null;
/*     */               return;
/*     */             }
/*     */           }
/* 214 */           this.value = object;
/*     */         } finally {
/* 216 */           this.returnClass = clazz;
/*     */         }
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public void visitEnum(Attribute.Enum param1Enum) {
/* 223 */       if (this.returnClass.isEnum()) {
/* 224 */         String str = param1Enum.value.toString();
/*     */         try {
/* 226 */           this.value = Enum.valueOf(this.returnClass, str);
/* 227 */         } catch (IllegalArgumentException illegalArgumentException) {
/* 228 */           this.value = new EnumConstantNotPresentExceptionProxy((Class)this.returnClass, str);
/*     */         }
/*     */       } else {
/*     */
/* 232 */         this.value = null;
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     public void visitCompound(Attribute.Compound param1Compound) {
/*     */       try {
/* 239 */         Class<? extends Annotation> clazz = this.returnClass.asSubclass(Annotation.class);
/* 240 */         this.value = AnnotationProxyMaker.generateAnnotation(param1Compound, clazz);
/* 241 */       } catch (ClassCastException classCastException) {
/* 242 */         this.value = null;
/*     */       }
/*     */     }
/*     */
/*     */     public void visitError(Attribute.Error param1Error) {
/* 247 */       if (param1Error instanceof Attribute.UnresolvedClass) {
/* 248 */         this.value = new MirroredTypeExceptionProxy((TypeMirror)((Attribute.UnresolvedClass)param1Error).classType);
/*     */       } else {
/* 250 */         this.value = null;
/*     */       }
/*     */     }
/*     */
/*     */     private void typeMismatch(Method param1Method, final Attribute attr) {
/*     */       class AnnotationTypeMismatchExceptionProxy
/*     */         extends ExceptionProxy
/*     */       {
/*     */         static final long serialVersionUID = 269L;
/*     */         final transient Method method;
/*     */
/*     */         AnnotationTypeMismatchExceptionProxy(Method param2Method1) {
/* 262 */           this.method = param2Method1;
/*     */         }
/*     */         public String toString() {
/* 265 */           return "<error>";
/*     */         }
/*     */         protected RuntimeException generateException() {
/* 268 */           return new AnnotationTypeMismatchException(this.method, attr.type
/* 269 */               .toString());
/*     */         }
/*     */       };
/* 272 */       this.value = new AnnotationTypeMismatchExceptionProxy(param1Method);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   private static final class MirroredTypeExceptionProxy
/*     */     extends ExceptionProxy
/*     */   {
/*     */     static final long serialVersionUID = 269L;
/*     */
/*     */     private transient TypeMirror type;
/*     */
/*     */     private final String typeString;
/*     */
/*     */
/*     */     MirroredTypeExceptionProxy(TypeMirror param1TypeMirror) {
/* 289 */       this.type = param1TypeMirror;
/* 290 */       this.typeString = param1TypeMirror.toString();
/*     */     }
/*     */
/*     */     public String toString() {
/* 294 */       return this.typeString;
/*     */     }
/*     */
/*     */     public int hashCode() {
/* 298 */       return ((this.type != null) ? this.type : this.typeString).hashCode();
/*     */     }
/*     */
/*     */     public boolean equals(Object param1Object) {
/* 302 */       return (this.type != null && param1Object instanceof MirroredTypeExceptionProxy && this.type
/*     */
/* 304 */         .equals(((MirroredTypeExceptionProxy)param1Object).type));
/*     */     }
/*     */
/*     */     protected RuntimeException generateException() {
/* 308 */       return new MirroredTypeException(this.type);
/*     */     }
/*     */
/*     */
/*     */
/*     */     private void readObject(ObjectInputStream param1ObjectInputStream) throws IOException, ClassNotFoundException {
/* 314 */       param1ObjectInputStream.defaultReadObject();
/* 315 */       this.type = null;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   private static final class MirroredTypesExceptionProxy
/*     */     extends ExceptionProxy
/*     */   {
/*     */     static final long serialVersionUID = 269L;
/*     */
/*     */     private transient List<TypeMirror> types;
/*     */
/*     */     private final String typeStrings;
/*     */
/*     */
/*     */     MirroredTypesExceptionProxy(List<TypeMirror> param1List) {
/* 332 */       this.types = param1List;
/* 333 */       this.typeStrings = param1List.toString();
/*     */     }
/*     */
/*     */     public String toString() {
/* 337 */       return this.typeStrings;
/*     */     }
/*     */
/*     */     public int hashCode() {
/* 341 */       return ((this.types != null) ? this.types : this.typeStrings).hashCode();
/*     */     }
/*     */
/*     */     public boolean equals(Object param1Object) {
/* 345 */       return (this.types != null && param1Object instanceof MirroredTypesExceptionProxy && this.types
/*     */
/* 347 */         .equals(((MirroredTypesExceptionProxy)param1Object).types));
/*     */     }
/*     */
/*     */
/*     */     protected RuntimeException generateException() {
/* 352 */       return new MirroredTypesException((List<? extends TypeMirror>)this.types);
/*     */     }
/*     */
/*     */
/*     */
/*     */     private void readObject(ObjectInputStream param1ObjectInputStream) throws IOException, ClassNotFoundException {
/* 358 */       param1ObjectInputStream.defaultReadObject();
/* 359 */       this.types = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\model\AnnotationProxyMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
