/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.ArrayReference;
/*     */ import com.sun.jdi.ArrayType;
/*     */ import com.sun.jdi.ClassLoaderReference;
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.InterfaceType;
/*     */ import com.sun.jdi.Method;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.Type;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArrayTypeImpl
/*     */   extends ReferenceTypeImpl
/*     */   implements ArrayType
/*     */ {
/*     */   protected ArrayTypeImpl(VirtualMachine paramVirtualMachine, long paramLong) {
/*  40 */     super(paramVirtualMachine, paramLong);
/*     */   }
/*     */   
/*     */   public ArrayReference newInstance(int paramInt) {
/*     */     try {
/*  45 */       return 
/*  46 */         (ArrayReference)(JDWP.ArrayType.NewInstance.process(this.vm, this, paramInt)).newArray;
/*  47 */     } catch (JDWPException jDWPException) {
/*  48 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */   }
/*     */   
/*     */   public String componentSignature() {
/*  53 */     return signature().substring(1);
/*     */   }
/*     */   
/*     */   public String componentTypeName() {
/*  57 */     JNITypeParser jNITypeParser = new JNITypeParser(componentSignature());
/*  58 */     return jNITypeParser.typeName();
/*     */   }
/*     */   
/*     */   Type type() throws ClassNotLoadedException {
/*  62 */     return findType(componentSignature());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void addVisibleMethods(Map<String, Method> paramMap, Set<InterfaceType> paramSet) {}
/*     */ 
/*     */   
/*     */   public List<Method> allMethods() {
/*  71 */     return new ArrayList<>(0);
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
/*     */   Type findComponentType(String paramString) throws ClassNotLoadedException {
/*  84 */     byte b = (byte)paramString.charAt(0);
/*  85 */     if (PacketStream.isObjectTag(b)) {
/*     */       
/*  87 */       JNITypeParser jNITypeParser = new JNITypeParser(componentSignature());
/*  88 */       List<ReferenceType> list = this.vm.classesByName(jNITypeParser.typeName());
/*  89 */       Iterator<ReferenceType> iterator = list.iterator();
/*  90 */       while (iterator.hasNext()) {
/*  91 */         ReferenceType referenceType = iterator.next();
/*  92 */         ClassLoaderReference classLoaderReference = referenceType.classLoader();
/*  93 */         if ((classLoaderReference == null) ? (
/*  94 */           classLoader() == null) : classLoaderReference
/*  95 */           .equals(classLoader())) {
/*  96 */           return (Type)referenceType;
/*     */         }
/*     */       } 
/*     */       
/* 100 */       throw new ClassNotLoadedException(componentTypeName());
/*     */     } 
/*     */     
/* 103 */     return (Type)this.vm.primitiveTypeMirror(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public Type componentType() throws ClassNotLoadedException {
/* 108 */     return findComponentType(componentSignature());
/*     */   }
/*     */   
/*     */   static boolean isComponentAssignable(Type paramType1, Type paramType2) {
/* 112 */     if (paramType2 instanceof com.sun.jdi.PrimitiveType)
/*     */     {
/*     */       
/* 115 */       return paramType2.equals(paramType1);
/*     */     }
/* 117 */     if (paramType1 instanceof com.sun.jdi.PrimitiveType) {
/* 118 */       return false;
/*     */     }
/*     */     
/* 121 */     ReferenceTypeImpl referenceTypeImpl1 = (ReferenceTypeImpl)paramType2;
/* 122 */     ReferenceTypeImpl referenceTypeImpl2 = (ReferenceTypeImpl)paramType1;
/*     */ 
/*     */     
/* 125 */     return referenceTypeImpl1.isAssignableTo(referenceTypeImpl2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isAssignableTo(ReferenceType paramReferenceType) {
/* 134 */     if (paramReferenceType instanceof ArrayType)
/*     */       try {
/* 136 */         Type type = ((ArrayType)paramReferenceType).componentType();
/* 137 */         return isComponentAssignable(type, componentType());
/* 138 */       } catch (ClassNotLoadedException classNotLoadedException) {
/*     */ 
/*     */         
/* 141 */         return false;
/*     */       }  
/* 143 */     if (paramReferenceType instanceof InterfaceType)
/*     */     {
/* 145 */       return paramReferenceType.name().equals("java.lang.Cloneable");
/*     */     }
/*     */     
/* 148 */     return paramReferenceType.name().equals("java.lang.Object");
/*     */   }
/*     */ 
/*     */   
/*     */   List<ReferenceType> inheritedTypes() {
/* 153 */     return new ArrayList<>(0);
/*     */   }
/*     */   
/*     */   void getModifiers() {
/* 157 */     if (this.modifiers != -1) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 173 */       Type type = componentType();
/* 174 */       if (type instanceof com.sun.jdi.PrimitiveType) {
/* 175 */         this.modifiers = 17;
/*     */       } else {
/* 177 */         ReferenceType referenceType = (ReferenceType)type;
/* 178 */         this.modifiers = referenceType.modifiers();
/*     */       } 
/* 180 */     } catch (ClassNotLoadedException classNotLoadedException) {
/* 181 */       classNotLoadedException.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public String toString() {
/* 186 */     return "array class " + name() + " (" + loaderString() + ")";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrepared() {
/* 193 */     return true;
/* 194 */   } public boolean isVerified() { return true; }
/* 195 */   public boolean isInitialized() { return true; }
/* 196 */   public boolean failedToInitialize() { return false; } public boolean isAbstract() {
/* 197 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/* 202 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 207 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\ArrayTypeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */