/*     */ package com.sun.tools.internal.ws.processor.modeler.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.lang.model.element.Name;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.type.ArrayType;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.PrimitiveType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeMonikerFactory
/*     */ {
/*     */   public static TypeMoniker getTypeMoniker(TypeMirror typeMirror) {
/*  45 */     if (typeMirror == null) {
/*  46 */       throw new NullPointerException();
/*     */     }
/*  48 */     if (typeMirror.getKind().isPrimitive())
/*  49 */       return new PrimitiveTypeMoniker((PrimitiveType)typeMirror); 
/*  50 */     if (typeMirror.getKind().equals(TypeKind.ARRAY))
/*  51 */       return new ArrayTypeMoniker((ArrayType)typeMirror); 
/*  52 */     if (typeMirror.getKind().equals(TypeKind.DECLARED))
/*  53 */       return new DeclaredTypeMoniker((DeclaredType)typeMirror); 
/*  54 */     return getTypeMoniker(typeMirror.toString());
/*     */   }
/*     */   
/*     */   public static TypeMoniker getTypeMoniker(String typeName) {
/*  58 */     return new StringMoniker(typeName);
/*     */   }
/*     */   
/*     */   static class ArrayTypeMoniker implements TypeMoniker {
/*     */     private TypeMoniker arrayType;
/*     */     
/*     */     public ArrayTypeMoniker(ArrayType type) {
/*  65 */       this.arrayType = TypeMonikerFactory.getTypeMoniker(type.getComponentType());
/*     */     }
/*     */     
/*     */     public TypeMirror create(ProcessingEnvironment apEnv) {
/*  69 */       return apEnv.getTypeUtils().getArrayType(this.arrayType.create(apEnv));
/*     */     } }
/*     */   
/*     */   static class DeclaredTypeMoniker implements TypeMoniker {
/*     */     private Name typeDeclName;
/*  74 */     private Collection<TypeMoniker> typeArgs = new ArrayList<>();
/*     */     
/*     */     public DeclaredTypeMoniker(DeclaredType type) {
/*  77 */       this.typeDeclName = ((TypeElement)type.asElement()).getQualifiedName();
/*  78 */       for (TypeMirror arg : type.getTypeArguments())
/*  79 */         this.typeArgs.add(TypeMonikerFactory.getTypeMoniker(arg)); 
/*     */     }
/*     */     
/*     */     public TypeMirror create(ProcessingEnvironment apEnv) {
/*  83 */       TypeElement typeDecl = apEnv.getElementUtils().getTypeElement(this.typeDeclName);
/*  84 */       TypeMirror[] tmpArgs = new TypeMirror[this.typeArgs.size()];
/*  85 */       int idx = 0;
/*  86 */       for (TypeMoniker moniker : this.typeArgs) {
/*  87 */         tmpArgs[idx++] = moniker.create(apEnv);
/*     */       }
/*  89 */       return apEnv.getTypeUtils().getDeclaredType(typeDecl, tmpArgs);
/*     */     }
/*     */   }
/*     */   
/*     */   static class PrimitiveTypeMoniker
/*     */     implements TypeMoniker {
/*     */     public PrimitiveTypeMoniker(PrimitiveType type) {
/*  96 */       this.kind = type.getKind();
/*     */     }
/*     */     private TypeKind kind;
/*     */     public TypeMirror create(ProcessingEnvironment apEnv) {
/* 100 */       return apEnv.getTypeUtils().getPrimitiveType(this.kind);
/*     */     } }
/*     */   
/*     */   static class StringMoniker implements TypeMoniker {
/*     */     private String typeName;
/*     */     
/*     */     public StringMoniker(String typeName) {
/* 107 */       this.typeName = typeName;
/*     */     }
/*     */     
/*     */     public TypeMirror create(ProcessingEnvironment apEnv) {
/* 111 */       return apEnv.getTypeUtils().getDeclaredType(apEnv.getElementUtils().getTypeElement(this.typeName), new TypeMirror[0]);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\modeler\annotation\TypeMonikerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */