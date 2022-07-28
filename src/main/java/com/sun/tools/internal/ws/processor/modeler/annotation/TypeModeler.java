/*     */ package com.sun.tools.internal.ws.processor.modeler.annotation;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.util.ElementFilter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class TypeModeler
/*     */ {
/*     */   public static TypeElement getDeclaration(TypeMirror typeMirror) {
/*  48 */     if (typeMirror != null && typeMirror.getKind().equals(TypeKind.DECLARED))
/*  49 */       return (TypeElement)((DeclaredType)typeMirror).asElement(); 
/*  50 */     return null;
/*     */   }
/*     */   
/*     */   public static TypeElement getDeclaringClassMethod(TypeMirror theClass, String methodName, TypeMirror[] args) {
/*  54 */     return getDeclaringClassMethod(getDeclaration(theClass), methodName, args);
/*     */   }
/*     */ 
/*     */   
/*     */   public static TypeElement getDeclaringClassMethod(TypeElement theClass, String methodName, TypeMirror[] args) {
/*  59 */     TypeElement retClass = null;
/*  60 */     if (theClass.getKind().equals(ElementKind.CLASS)) {
/*  61 */       TypeMirror superClass = theClass.getSuperclass();
/*  62 */       if (!superClass.getKind().equals(TypeKind.NONE))
/*  63 */         retClass = getDeclaringClassMethod(superClass, methodName, args); 
/*     */     } 
/*  65 */     if (retClass == null) {
/*  66 */       for (TypeMirror interfaceType : theClass.getInterfaces()) {
/*  67 */         retClass = getDeclaringClassMethod(interfaceType, methodName, args);
/*     */       }
/*     */     }
/*  70 */     if (retClass == null) {
/*  71 */       Collection<? extends ExecutableElement> methods = ElementFilter.methodsIn(theClass.getEnclosedElements());
/*  72 */       for (ExecutableElement method : methods) {
/*  73 */         if (method.getSimpleName().toString().equals(methodName)) {
/*  74 */           retClass = theClass;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*  79 */     return retClass;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Collection<DeclaredType> collectInterfaces(TypeElement type) {
/*  84 */     Collection<DeclaredType> interfaces = (Collection)type.getInterfaces();
/*  85 */     for (TypeMirror interfaceType : type.getInterfaces()) {
/*  86 */       interfaces.addAll(collectInterfaces(getDeclaration(interfaceType)));
/*     */     }
/*  88 */     return interfaces;
/*     */   }
/*     */   
/*     */   public static boolean isSubclass(String subTypeName, String superTypeName, ProcessingEnvironment env) {
/*  92 */     return isSubclass(env.getElementUtils().getTypeElement(subTypeName), env.getElementUtils().getTypeElement(superTypeName), env);
/*     */   }
/*     */   
/*     */   public static boolean isSubclass(TypeElement subType, TypeElement superType, ProcessingEnvironment env) {
/*  96 */     return (!subType.equals(superType) && isSubElement(subType, superType));
/*     */   }
/*     */   
/*     */   public static TypeMirror getHolderValueType(TypeMirror type, TypeElement defHolder, ProcessingEnvironment env) {
/* 100 */     TypeElement typeElement = getDeclaration(type);
/* 101 */     if (typeElement == null) {
/* 102 */       return null;
/*     */     }
/* 104 */     if (isSubElement(typeElement, defHolder) && 
/* 105 */       type.getKind().equals(TypeKind.DECLARED)) {
/* 106 */       Collection<? extends TypeMirror> argTypes = ((DeclaredType)type).getTypeArguments();
/* 107 */       if (argTypes.size() == 1)
/* 108 */         return argTypes.iterator().next(); 
/* 109 */       if (argTypes.isEmpty()) {
/* 110 */         VariableElement member = getValueMember(typeElement);
/* 111 */         if (member != null) {
/* 112 */           return member.asType();
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 117 */     return null;
/*     */   }
/*     */   
/*     */   public static VariableElement getValueMember(TypeMirror classType) {
/* 121 */     return getValueMember(getDeclaration(classType));
/*     */   }
/*     */   
/*     */   public static VariableElement getValueMember(TypeElement type) {
/* 125 */     VariableElement member = null;
/* 126 */     for (VariableElement field : ElementFilter.fieldsIn(type.getEnclosedElements())) {
/* 127 */       if ("value".equals(field.getSimpleName().toString())) {
/* 128 */         member = field;
/*     */         break;
/*     */       } 
/*     */     } 
/* 132 */     if (member == null && type.getKind().equals(ElementKind.CLASS))
/* 133 */       member = getValueMember(type.getSuperclass()); 
/* 134 */     return member;
/*     */   }
/*     */   
/*     */   public static boolean isSubElement(TypeElement d1, TypeElement d2) {
/* 138 */     if (d1.equals(d2))
/* 139 */       return true; 
/* 140 */     TypeElement superClassDecl = null;
/* 141 */     if (d1.getKind().equals(ElementKind.CLASS)) {
/* 142 */       TypeMirror superClass = d1.getSuperclass();
/* 143 */       if (!superClass.getKind().equals(TypeKind.NONE)) {
/* 144 */         superClassDecl = (TypeElement)((DeclaredType)superClass).asElement();
/* 145 */         if (superClassDecl.equals(d2))
/* 146 */           return true; 
/*     */       } 
/*     */     } 
/* 149 */     for (TypeMirror superIntf : d1.getInterfaces()) {
/* 150 */       DeclaredType declaredSuperIntf = (DeclaredType)superIntf;
/* 151 */       if (declaredSuperIntf.asElement().equals(d2)) {
/* 152 */         return true;
/*     */       }
/* 154 */       if (isSubElement((TypeElement)declaredSuperIntf.asElement(), d2))
/* 155 */         return true; 
/* 156 */       if (superClassDecl != null && isSubElement(superClassDecl, d2)) {
/* 157 */         return true;
/*     */       }
/*     */     } 
/* 160 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\modeler\annotation\TypeModeler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */