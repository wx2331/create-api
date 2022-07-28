/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ConstructorDoc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.Parameter;
/*     */ import com.sun.javadoc.ParameterizedType;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.javadoc.TypeVariable;
/*     */ import com.sun.javadoc.WildcardType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassUseMapper
/*     */ {
/*     */   private final ClassTree classtree;
/*  51 */   public Map<String, Set<PackageDoc>> classToPackage = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   public Map<String, List<PackageDoc>> classToPackageAnnotations = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public Map<String, Set<ClassDoc>> classToClass = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   public Map<String, List<ClassDoc>> classToSubclass = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public Map<String, List<ClassDoc>> classToSubinterface = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   public Map<String, List<ClassDoc>> classToImplementingClass = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   public Map<String, List<FieldDoc>> classToField = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public Map<String, List<MethodDoc>> classToMethodReturn = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   public Map<String, List<ExecutableMemberDoc>> classToMethodArgs = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public Map<String, List<ExecutableMemberDoc>> classToMethodThrows = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public Map<String, List<ExecutableMemberDoc>> classToConstructorArgs = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public Map<String, List<ExecutableMemberDoc>> classToConstructorThrows = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 126 */   public Map<String, List<ConstructorDoc>> classToConstructorAnnotations = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   public Map<String, List<ExecutableMemberDoc>> classToConstructorParamAnnotation = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 136 */   public Map<String, List<ExecutableMemberDoc>> classToConstructorDocArgTypeParam = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   public Map<String, List<ClassDoc>> classToClassTypeParam = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   public Map<String, List<ClassDoc>> classToClassAnnotations = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   public Map<String, List<MethodDoc>> classToExecMemberDocTypeParam = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 156 */   public Map<String, List<ExecutableMemberDoc>> classToExecMemberDocArgTypeParam = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   public Map<String, List<MethodDoc>> classToExecMemberDocAnnotations = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 167 */   public Map<String, List<MethodDoc>> classToExecMemberDocReturnTypeParam = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 172 */   public Map<String, List<ExecutableMemberDoc>> classToExecMemberDocParamAnnotation = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 177 */   public Map<String, List<FieldDoc>> classToFieldDocTypeParam = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 182 */   public Map<String, List<FieldDoc>> annotationToFieldDoc = new HashMap<>();
/*     */ 
/*     */   
/*     */   public ClassUseMapper(RootDoc paramRootDoc, ClassTree paramClassTree) {
/* 186 */     this.classtree = paramClassTree;
/*     */     
/*     */     Iterator<ClassDoc> iterator;
/* 189 */     for (iterator = paramClassTree.baseclasses().iterator(); iterator.hasNext();) {
/* 190 */       subclasses(iterator.next());
/*     */     }
/* 192 */     for (iterator = paramClassTree.baseinterfaces().iterator(); iterator.hasNext();)
/*     */     {
/* 194 */       implementingClasses(iterator.next());
/*     */     }
/*     */     
/* 197 */     ClassDoc[] arrayOfClassDoc = paramRootDoc.classes();
/* 198 */     for (byte b = 0; b < arrayOfClassDoc.length; b++) {
/* 199 */       PackageDoc packageDoc = arrayOfClassDoc[b].containingPackage();
/* 200 */       mapAnnotations(this.classToPackageAnnotations, packageDoc, packageDoc);
/* 201 */       ClassDoc classDoc = arrayOfClassDoc[b];
/* 202 */       mapTypeParameters(this.classToClassTypeParam, classDoc, classDoc);
/* 203 */       mapAnnotations(this.classToClassAnnotations, classDoc, classDoc);
/* 204 */       FieldDoc[] arrayOfFieldDoc = classDoc.fields();
/* 205 */       for (byte b1 = 0; b1 < arrayOfFieldDoc.length; b1++) {
/* 206 */         FieldDoc fieldDoc = arrayOfFieldDoc[b1];
/* 207 */         mapTypeParameters(this.classToFieldDocTypeParam, fieldDoc, fieldDoc);
/* 208 */         mapAnnotations(this.annotationToFieldDoc, fieldDoc, fieldDoc);
/* 209 */         if (!fieldDoc.type().isPrimitive()) {
/* 210 */           add(this.classToField, fieldDoc.type().asClassDoc(), fieldDoc);
/*     */         }
/*     */       } 
/* 213 */       ConstructorDoc[] arrayOfConstructorDoc = classDoc.constructors();
/* 214 */       for (byte b2 = 0; b2 < arrayOfConstructorDoc.length; b2++) {
/* 215 */         mapAnnotations(this.classToConstructorAnnotations, arrayOfConstructorDoc[b2], arrayOfConstructorDoc[b2]);
/* 216 */         mapExecutable((ExecutableMemberDoc)arrayOfConstructorDoc[b2]);
/*     */       } 
/* 218 */       MethodDoc[] arrayOfMethodDoc = classDoc.methods();
/* 219 */       for (byte b3 = 0; b3 < arrayOfMethodDoc.length; b3++) {
/* 220 */         MethodDoc methodDoc = arrayOfMethodDoc[b3];
/* 221 */         mapExecutable((ExecutableMemberDoc)methodDoc);
/* 222 */         mapTypeParameters(this.classToExecMemberDocTypeParam, methodDoc, methodDoc);
/* 223 */         mapAnnotations(this.classToExecMemberDocAnnotations, methodDoc, methodDoc);
/* 224 */         if (!methodDoc.returnType().isPrimitive() && !(methodDoc.returnType() instanceof TypeVariable)) {
/* 225 */           mapTypeParameters(this.classToExecMemberDocReturnTypeParam, methodDoc
/* 226 */               .returnType(), methodDoc);
/* 227 */           add(this.classToMethodReturn, methodDoc.returnType().asClassDoc(), methodDoc);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Collection<ClassDoc> subclasses(ClassDoc paramClassDoc) {
/* 237 */     Collection<ClassDoc> collection = this.classToSubclass.get(paramClassDoc.qualifiedName());
/* 238 */     if (collection == null) {
/* 239 */       collection = new TreeSet();
/* 240 */       List<ClassDoc> list = this.classtree.subclasses(paramClassDoc);
/* 241 */       if (list != null) {
/* 242 */         collection.addAll(list);
/* 243 */         for (Iterator<ClassDoc> iterator = list.iterator(); iterator.hasNext();) {
/* 244 */           collection.addAll(subclasses(iterator.next()));
/*     */         }
/*     */       } 
/* 247 */       addAll(this.classToSubclass, paramClassDoc, collection);
/*     */     } 
/* 249 */     return collection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Collection<ClassDoc> subinterfaces(ClassDoc paramClassDoc) {
/* 256 */     Collection<ClassDoc> collection = this.classToSubinterface.get(paramClassDoc.qualifiedName());
/* 257 */     if (collection == null) {
/* 258 */       collection = new TreeSet();
/* 259 */       List<ClassDoc> list = this.classtree.subinterfaces(paramClassDoc);
/* 260 */       if (list != null) {
/* 261 */         collection.addAll(list);
/* 262 */         for (Iterator<ClassDoc> iterator = list.iterator(); iterator.hasNext();) {
/* 263 */           collection.addAll(subinterfaces(iterator.next()));
/*     */         }
/*     */       } 
/* 266 */       addAll(this.classToSubinterface, paramClassDoc, collection);
/*     */     } 
/* 268 */     return collection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Collection<ClassDoc> implementingClasses(ClassDoc paramClassDoc) {
/* 278 */     Collection<ClassDoc> collection = this.classToImplementingClass.get(paramClassDoc.qualifiedName());
/* 279 */     if (collection == null) {
/* 280 */       collection = new TreeSet();
/* 281 */       List<ClassDoc> list = this.classtree.implementingclasses(paramClassDoc);
/* 282 */       if (list != null) {
/* 283 */         collection.addAll(list);
/* 284 */         for (Iterator<ClassDoc> iterator1 = list.iterator(); iterator1.hasNext();) {
/* 285 */           collection.addAll(subclasses(iterator1.next()));
/*     */         }
/*     */       } 
/* 288 */       for (Iterator<ClassDoc> iterator = subinterfaces(paramClassDoc).iterator(); iterator.hasNext();) {
/* 289 */         collection.addAll(implementingClasses(iterator.next()));
/*     */       }
/* 291 */       addAll(this.classToImplementingClass, paramClassDoc, collection);
/*     */     } 
/* 293 */     return collection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void mapExecutable(ExecutableMemberDoc paramExecutableMemberDoc) {
/* 301 */     Parameter[] arrayOfParameter = paramExecutableMemberDoc.parameters();
/* 302 */     boolean bool = paramExecutableMemberDoc.isConstructor();
/* 303 */     ArrayList<Type> arrayList = new ArrayList();
/* 304 */     for (byte b1 = 0; b1 < arrayOfParameter.length; b1++) {
/* 305 */       Type type = arrayOfParameter[b1].type();
/*     */       
/* 307 */       if (!arrayOfParameter[b1].type().isPrimitive() && 
/* 308 */         !arrayList.contains(type) && !(type instanceof TypeVariable)) {
/*     */         
/* 310 */         add(bool ? this.classToConstructorArgs : this.classToMethodArgs, type
/* 311 */             .asClassDoc(), paramExecutableMemberDoc);
/* 312 */         arrayList.add(type);
/* 313 */         mapTypeParameters(bool ? this.classToConstructorDocArgTypeParam : this.classToExecMemberDocArgTypeParam, type, paramExecutableMemberDoc);
/*     */       } 
/*     */ 
/*     */       
/* 317 */       mapAnnotations(bool ? this.classToConstructorParamAnnotation : this.classToExecMemberDocParamAnnotation, arrayOfParameter[b1], paramExecutableMemberDoc);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 323 */     ClassDoc[] arrayOfClassDoc = paramExecutableMemberDoc.thrownExceptions();
/* 324 */     for (byte b2 = 0; b2 < arrayOfClassDoc.length; b2++) {
/* 325 */       add(bool ? this.classToConstructorThrows : this.classToMethodThrows, arrayOfClassDoc[b2], paramExecutableMemberDoc);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private <T> List<T> refList(Map<String, List<T>> paramMap, ClassDoc paramClassDoc) {
/* 331 */     List<T> list = paramMap.get(paramClassDoc.qualifiedName());
/* 332 */     if (list == null) {
/* 333 */       ArrayList<T> arrayList = new ArrayList();
/* 334 */       list = arrayList;
/* 335 */       paramMap.put(paramClassDoc.qualifiedName(), list);
/*     */     } 
/* 337 */     return list;
/*     */   }
/*     */   
/*     */   private Set<PackageDoc> packageSet(ClassDoc paramClassDoc) {
/* 341 */     Set<PackageDoc> set = this.classToPackage.get(paramClassDoc.qualifiedName());
/* 342 */     if (set == null) {
/* 343 */       set = new TreeSet();
/* 344 */       this.classToPackage.put(paramClassDoc.qualifiedName(), set);
/*     */     } 
/* 346 */     return set;
/*     */   }
/*     */   
/*     */   private Set<ClassDoc> classSet(ClassDoc paramClassDoc) {
/* 350 */     Set<ClassDoc> set = this.classToClass.get(paramClassDoc.qualifiedName());
/* 351 */     if (set == null) {
/* 352 */       TreeSet<ClassDoc> treeSet = new TreeSet();
/* 353 */       set = treeSet;
/* 354 */       this.classToClass.put(paramClassDoc.qualifiedName(), set);
/*     */     } 
/* 356 */     return set;
/*     */   }
/*     */ 
/*     */   
/*     */   private <T extends ProgramElementDoc> void add(Map<String, List<T>> paramMap, ClassDoc paramClassDoc, T paramT) {
/* 361 */     refList(paramMap, paramClassDoc).add(paramT);
/*     */ 
/*     */     
/* 364 */     packageSet(paramClassDoc).add(paramT.containingPackage());
/*     */     
/* 366 */     classSet(paramClassDoc).add((paramT instanceof MemberDoc) ? ((MemberDoc)paramT)
/* 367 */         .containingClass() : (ClassDoc)paramT);
/*     */   }
/*     */ 
/*     */   
/*     */   private void addAll(Map<String, List<ClassDoc>> paramMap, ClassDoc paramClassDoc, Collection<ClassDoc> paramCollection) {
/* 372 */     if (paramCollection == null) {
/*     */       return;
/*     */     }
/*     */     
/* 376 */     refList(paramMap, paramClassDoc).addAll(paramCollection);
/*     */     
/* 378 */     Set<PackageDoc> set = packageSet(paramClassDoc);
/* 379 */     Set<ClassDoc> set1 = classSet(paramClassDoc);
/*     */     
/* 381 */     for (ClassDoc classDoc : paramCollection) {
/*     */       
/* 383 */       set.add(classDoc.containingPackage());
/* 384 */       set1.add(classDoc);
/*     */     } 
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
/*     */   private <T extends ProgramElementDoc> void mapTypeParameters(Map<String, List<T>> paramMap, Object paramObject, T paramT) {
/*     */     TypeVariable[] arrayOfTypeVariable;
/* 400 */     if (paramObject instanceof ClassDoc)
/* 401 */     { arrayOfTypeVariable = ((ClassDoc)paramObject).typeParameters(); }
/* 402 */     else { if (paramObject instanceof WildcardType) {
/* 403 */         Type[] arrayOfType1 = ((WildcardType)paramObject).extendsBounds();
/* 404 */         for (byte b1 = 0; b1 < arrayOfType1.length; b1++) {
/* 405 */           addTypeParameterToMap(paramMap, arrayOfType1[b1], paramT);
/*     */         }
/* 407 */         Type[] arrayOfType2 = ((WildcardType)paramObject).superBounds();
/* 408 */         for (byte b2 = 0; b2 < arrayOfType2.length; b2++)
/* 409 */           addTypeParameterToMap(paramMap, arrayOfType2[b2], paramT); 
/*     */         return;
/*     */       } 
/* 412 */       if (paramObject instanceof ParameterizedType) {
/* 413 */         Type[] arrayOfType = ((ParameterizedType)paramObject).typeArguments();
/* 414 */         for (byte b1 = 0; b1 < arrayOfType.length; b1++)
/* 415 */           addTypeParameterToMap(paramMap, arrayOfType[b1], paramT); 
/*     */         return;
/*     */       } 
/* 418 */       if (paramObject instanceof ExecutableMemberDoc)
/* 419 */       { arrayOfTypeVariable = ((ExecutableMemberDoc)paramObject).typeParameters(); }
/* 420 */       else { if (paramObject instanceof FieldDoc) {
/* 421 */           Type type = ((FieldDoc)paramObject).type();
/* 422 */           mapTypeParameters(paramMap, type, paramT); return;
/*     */         } 
/*     */         return; }
/*     */        }
/*     */     
/* 427 */     for (byte b = 0; b < arrayOfTypeVariable.length; b++) {
/* 428 */       Type[] arrayOfType = arrayOfTypeVariable[b].bounds();
/* 429 */       for (byte b1 = 0; b1 < arrayOfType.length; b1++) {
/* 430 */         addTypeParameterToMap(paramMap, arrayOfType[b1], paramT);
/*     */       }
/*     */     } 
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
/*     */   private <T extends ProgramElementDoc> void mapAnnotations(Map<String, List<T>> paramMap, Object paramObject, T paramT) {
/*     */     AnnotationDesc[] arrayOfAnnotationDesc;
/* 446 */     boolean bool = false;
/* 447 */     if (paramObject instanceof ProgramElementDoc) {
/* 448 */       arrayOfAnnotationDesc = ((ProgramElementDoc)paramObject).annotations();
/* 449 */     } else if (paramObject instanceof PackageDoc) {
/* 450 */       arrayOfAnnotationDesc = ((PackageDoc)paramObject).annotations();
/* 451 */       bool = true;
/* 452 */     } else if (paramObject instanceof Parameter) {
/* 453 */       arrayOfAnnotationDesc = ((Parameter)paramObject).annotations();
/*     */     } else {
/* 455 */       throw new DocletAbortException("should not happen");
/*     */     } 
/* 457 */     for (byte b = 0; b < arrayOfAnnotationDesc.length; b++) {
/* 458 */       AnnotationTypeDoc annotationTypeDoc = arrayOfAnnotationDesc[b].annotationType();
/* 459 */       if (bool) {
/* 460 */         refList(paramMap, (ClassDoc)annotationTypeDoc).add(paramT);
/*     */       } else {
/* 462 */         add(paramMap, (ClassDoc)annotationTypeDoc, paramT);
/*     */       } 
/*     */     } 
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
/*     */   private <T extends PackageDoc> void mapAnnotations(Map<String, List<T>> paramMap, PackageDoc paramPackageDoc, T paramT) {
/* 478 */     AnnotationDesc[] arrayOfAnnotationDesc = paramPackageDoc.annotations();
/* 479 */     for (byte b = 0; b < arrayOfAnnotationDesc.length; b++) {
/* 480 */       AnnotationTypeDoc annotationTypeDoc = arrayOfAnnotationDesc[b].annotationType();
/* 481 */       refList(paramMap, (ClassDoc)annotationTypeDoc).add(paramT);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private <T extends ProgramElementDoc> void addTypeParameterToMap(Map<String, List<T>> paramMap, Type paramType, T paramT) {
/* 487 */     if (paramType instanceof ClassDoc) {
/* 488 */       add(paramMap, (ClassDoc)paramType, paramT);
/* 489 */     } else if (paramType instanceof ParameterizedType) {
/* 490 */       add(paramMap, ((ParameterizedType)paramType).asClassDoc(), paramT);
/*     */     } 
/* 492 */     mapTypeParameters(paramMap, paramType, paramT);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\ClassUseMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */