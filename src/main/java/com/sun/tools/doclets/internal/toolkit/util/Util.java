/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.AnnotatedType;
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.AnnotationValue;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.Parameter;
/*     */ import com.sun.javadoc.ParameterizedType;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Documented;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.annotation.Target;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import javax.tools.StandardLocation;
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
/*     */ public class Util
/*     */ {
/*     */   public static ProgramElementDoc[] excludeDeprecatedMembers(ProgramElementDoc[] paramArrayOfProgramElementDoc) {
/*  62 */     return 
/*  63 */       toProgramElementDocArray(excludeDeprecatedMembersAsList(paramArrayOfProgramElementDoc));
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
/*     */   public static List<ProgramElementDoc> excludeDeprecatedMembersAsList(ProgramElementDoc[] paramArrayOfProgramElementDoc) {
/*  77 */     ArrayList<ProgramElementDoc> arrayList = new ArrayList();
/*  78 */     for (byte b = 0; b < paramArrayOfProgramElementDoc.length; b++) {
/*  79 */       if ((paramArrayOfProgramElementDoc[b].tags("deprecated")).length == 0) {
/*  80 */         arrayList.add(paramArrayOfProgramElementDoc[b]);
/*     */       }
/*     */     } 
/*  83 */     Collections.sort(arrayList);
/*  84 */     return arrayList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ProgramElementDoc[] toProgramElementDocArray(List<ProgramElementDoc> paramList) {
/*  91 */     ProgramElementDoc[] arrayOfProgramElementDoc = new ProgramElementDoc[paramList.size()];
/*  92 */     for (byte b = 0; b < paramList.size(); b++) {
/*  93 */       arrayOfProgramElementDoc[b] = paramList.get(b);
/*     */     }
/*  95 */     return arrayOfProgramElementDoc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean nonPublicMemberFound(ProgramElementDoc[] paramArrayOfProgramElementDoc) {
/* 105 */     for (byte b = 0; b < paramArrayOfProgramElementDoc.length; b++) {
/* 106 */       if (!paramArrayOfProgramElementDoc[b].isPublic()) {
/* 107 */         return true;
/*     */       }
/*     */     } 
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MethodDoc findMethod(ClassDoc paramClassDoc, MethodDoc paramMethodDoc) {
/* 121 */     MethodDoc[] arrayOfMethodDoc = paramClassDoc.methods();
/* 122 */     for (byte b = 0; b < arrayOfMethodDoc.length; b++) {
/* 123 */       if (executableMembersEqual((ExecutableMemberDoc)paramMethodDoc, (ExecutableMemberDoc)arrayOfMethodDoc[b])) {
/* 124 */         return arrayOfMethodDoc[b];
/*     */       }
/*     */     } 
/*     */     
/* 128 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean executableMembersEqual(ExecutableMemberDoc paramExecutableMemberDoc1, ExecutableMemberDoc paramExecutableMemberDoc2) {
/* 138 */     if (!(paramExecutableMemberDoc1 instanceof MethodDoc) || !(paramExecutableMemberDoc2 instanceof MethodDoc)) {
/* 139 */       return false;
/*     */     }
/* 141 */     MethodDoc methodDoc1 = (MethodDoc)paramExecutableMemberDoc1;
/* 142 */     MethodDoc methodDoc2 = (MethodDoc)paramExecutableMemberDoc2;
/* 143 */     if (methodDoc1.isStatic() && methodDoc2.isStatic()) {
/* 144 */       Parameter[] arrayOfParameter1 = methodDoc1.parameters();
/*     */       Parameter[] arrayOfParameter2;
/* 146 */       if (methodDoc1.name().equals(methodDoc2.name()) && (
/* 147 */         arrayOfParameter2 = methodDoc2.parameters()).length == arrayOfParameter1.length) {
/*     */         byte b;
/*     */         
/* 150 */         for (b = 0; b < arrayOfParameter1.length && (
/* 151 */           arrayOfParameter1[b].typeName().equals(arrayOfParameter2[b]
/* 152 */             .typeName()) || arrayOfParameter2[b]
/* 153 */           .type() instanceof com.sun.javadoc.TypeVariable || arrayOfParameter1[b]
/* 154 */           .type() instanceof com.sun.javadoc.TypeVariable); b++);
/*     */ 
/*     */ 
/*     */         
/* 158 */         if (b == arrayOfParameter1.length) {
/* 159 */           return true;
/*     */         }
/*     */       } 
/* 162 */       return false;
/*     */     } 
/* 164 */     return (methodDoc1.overrides(methodDoc2) || methodDoc2
/* 165 */       .overrides(methodDoc1) || paramExecutableMemberDoc1 == paramExecutableMemberDoc2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isCoreClass(ClassDoc paramClassDoc) {
/* 176 */     return (paramClassDoc.containingClass() == null || paramClassDoc.isStatic());
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean matches(ProgramElementDoc paramProgramElementDoc1, ProgramElementDoc paramProgramElementDoc2) {
/* 181 */     if (paramProgramElementDoc1 instanceof ExecutableMemberDoc && paramProgramElementDoc2 instanceof ExecutableMemberDoc) {
/*     */       
/* 183 */       ExecutableMemberDoc executableMemberDoc1 = (ExecutableMemberDoc)paramProgramElementDoc1;
/* 184 */       ExecutableMemberDoc executableMemberDoc2 = (ExecutableMemberDoc)paramProgramElementDoc2;
/* 185 */       return executableMembersEqual(executableMemberDoc1, executableMemberDoc2);
/*     */     } 
/* 187 */     return paramProgramElementDoc1.name().equals(paramProgramElementDoc2.name());
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
/*     */   public static void copyDocFiles(Configuration paramConfiguration, PackageDoc paramPackageDoc) {
/* 205 */     copyDocFiles(paramConfiguration, DocPath.forPackage(paramPackageDoc).resolve(DocPaths.DOC_FILES));
/*     */   }
/*     */   
/*     */   public static void copyDocFiles(Configuration paramConfiguration, DocPath paramDocPath) {
/*     */     try {
/* 210 */       boolean bool = true;
/* 211 */       for (DocFile docFile1 : DocFile.list(paramConfiguration, StandardLocation.SOURCE_PATH, paramDocPath)) {
/* 212 */         if (!docFile1.isDirectory()) {
/*     */           continue;
/*     */         }
/* 215 */         DocFile docFile2 = docFile1;
/* 216 */         DocFile docFile3 = DocFile.createFileForOutput(paramConfiguration, paramDocPath);
/* 217 */         if (docFile2.isSameFile(docFile3)) {
/*     */           continue;
/*     */         }
/*     */         
/* 221 */         for (DocFile docFile4 : docFile2.list()) {
/* 222 */           DocFile docFile5 = docFile3.resolve(docFile4.getName());
/* 223 */           if (docFile4.isFile()) {
/* 224 */             if (docFile5.exists() && !bool) {
/* 225 */               paramConfiguration.message.warning((SourcePosition)null, "doclet.Copy_Overwrite_warning", new Object[] { docFile4
/*     */                     
/* 227 */                     .getPath(), docFile3.getPath() }); continue;
/*     */             } 
/* 229 */             paramConfiguration.message.notice("doclet.Copying_File_0_To_Dir_1", new Object[] { docFile4
/*     */                   
/* 231 */                   .getPath(), docFile3.getPath() });
/* 232 */             docFile5.copyFile(docFile4); continue;
/*     */           } 
/* 234 */           if (docFile4.isDirectory() && 
/* 235 */             paramConfiguration.copydocfilesubdirs && 
/* 236 */             !paramConfiguration.shouldExcludeDocFileDir(docFile4.getName())) {
/* 237 */             copyDocFiles(paramConfiguration, paramDocPath.resolve(docFile4.getName()));
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 242 */         bool = false;
/*     */       } 
/* 244 */     } catch (SecurityException securityException) {
/* 245 */       throw new DocletAbortException(securityException);
/* 246 */     } catch (IOException iOException) {
/* 247 */       throw new DocletAbortException(iOException);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class TypeComparator
/*     */     implements Comparator<Type>
/*     */   {
/*     */     private TypeComparator() {}
/*     */     
/*     */     public int compare(Type param1Type1, Type param1Type2) {
/* 257 */       return param1Type1.qualifiedTypeName().compareToIgnoreCase(param1Type2
/* 258 */           .qualifiedTypeName());
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Type> getAllInterfaces(Type paramType, Configuration paramConfiguration, boolean paramBoolean) {
/* 276 */     Map<ClassDoc, Type> map = (Map<ClassDoc, Type>)(paramBoolean ? new TreeMap<>() : new LinkedHashMap<>());
/* 277 */     Type[] arrayOfType = null;
/* 278 */     Type type = null;
/* 279 */     if (paramType instanceof ParameterizedType) {
/* 280 */       arrayOfType = ((ParameterizedType)paramType).interfaceTypes();
/* 281 */       type = ((ParameterizedType)paramType).superclassType();
/* 282 */     } else if (paramType instanceof ClassDoc) {
/* 283 */       arrayOfType = ((ClassDoc)paramType).interfaceTypes();
/* 284 */       type = ((ClassDoc)paramType).superclassType();
/*     */     } else {
/* 286 */       arrayOfType = paramType.asClassDoc().interfaceTypes();
/* 287 */       type = paramType.asClassDoc().superclassType();
/*     */     } 
/*     */     
/* 290 */     for (byte b = 0; b < arrayOfType.length; b++) {
/* 291 */       Type type1 = arrayOfType[b];
/* 292 */       ClassDoc classDoc = type1.asClassDoc();
/* 293 */       if (classDoc.isPublic() || paramConfiguration == null || 
/*     */         
/* 295 */         isLinkable(classDoc, paramConfiguration)) {
/*     */ 
/*     */         
/* 298 */         map.put(classDoc, type1);
/* 299 */         List<Type> list = getAllInterfaces(type1, paramConfiguration, paramBoolean);
/* 300 */         for (Type type2 : list)
/*     */         {
/* 302 */           map.put(type2.asClassDoc(), type2); } 
/*     */       } 
/*     */     } 
/* 305 */     if (type == null) {
/* 306 */       return new ArrayList<>(map.values());
/*     */     }
/* 308 */     addAllInterfaceTypes(map, type, 
/*     */         
/* 310 */         interfaceTypesOf(type), false, paramConfiguration);
/*     */     
/* 312 */     ArrayList<?> arrayList = new ArrayList(map.values());
/* 313 */     if (paramBoolean) {
/* 314 */       Collections.sort(arrayList, new TypeComparator());
/*     */     }
/* 316 */     return (List)arrayList;
/*     */   }
/*     */   
/*     */   private static Type[] interfaceTypesOf(Type paramType) {
/* 320 */     if (paramType instanceof AnnotatedType)
/* 321 */       paramType = ((AnnotatedType)paramType).underlyingType(); 
/* 322 */     return (paramType instanceof ClassDoc) ? ((ClassDoc)paramType)
/* 323 */       .interfaceTypes() : ((ParameterizedType)paramType)
/* 324 */       .interfaceTypes();
/*     */   }
/*     */   
/*     */   public static List<Type> getAllInterfaces(Type paramType, Configuration paramConfiguration) {
/* 328 */     return getAllInterfaces(paramType, paramConfiguration, true);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void findAllInterfaceTypes(Map<ClassDoc, Type> paramMap, ClassDoc paramClassDoc, boolean paramBoolean, Configuration paramConfiguration) {
/* 333 */     Type type = paramClassDoc.superclassType();
/* 334 */     if (type == null)
/*     */       return; 
/* 336 */     addAllInterfaceTypes(paramMap, type, 
/* 337 */         interfaceTypesOf(type), paramBoolean, paramConfiguration);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void findAllInterfaceTypes(Map<ClassDoc, Type> paramMap, ParameterizedType paramParameterizedType, Configuration paramConfiguration) {
/* 343 */     Type type = paramParameterizedType.superclassType();
/* 344 */     if (type == null)
/*     */       return; 
/* 346 */     addAllInterfaceTypes(paramMap, type, 
/* 347 */         interfaceTypesOf(type), false, paramConfiguration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addAllInterfaceTypes(Map<ClassDoc, Type> paramMap, Type paramType, Type[] paramArrayOfType, boolean paramBoolean, Configuration paramConfiguration) {
/* 354 */     for (byte b = 0; b < paramArrayOfType.length; b++) {
/* 355 */       Type type = paramArrayOfType[b];
/* 356 */       ClassDoc classDoc = type.asClassDoc();
/* 357 */       if (classDoc.isPublic() || (paramConfiguration != null && 
/*     */         
/* 359 */         isLinkable(classDoc, paramConfiguration))) {
/*     */         ClassDoc classDoc1;
/*     */         
/* 362 */         if (paramBoolean)
/* 363 */           classDoc1 = type.asClassDoc(); 
/* 364 */         paramMap.put(classDoc, classDoc1);
/* 365 */         List<Type> list = getAllInterfaces((Type)classDoc1, paramConfiguration);
/* 366 */         for (Type type1 : list)
/*     */         {
/* 368 */           paramMap.put(type1.asClassDoc(), type1); } 
/*     */       } 
/*     */     } 
/* 371 */     if (paramType instanceof AnnotatedType) {
/* 372 */       paramType = ((AnnotatedType)paramType).underlyingType();
/*     */     }
/* 374 */     if (paramType instanceof ParameterizedType) {
/* 375 */       findAllInterfaceTypes(paramMap, (ParameterizedType)paramType, paramConfiguration);
/* 376 */     } else if ((((ClassDoc)paramType).typeParameters()).length == 0) {
/* 377 */       findAllInterfaceTypes(paramMap, (ClassDoc)paramType, paramBoolean, paramConfiguration);
/*     */     } else {
/* 379 */       findAllInterfaceTypes(paramMap, (ClassDoc)paramType, true, paramConfiguration);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String quote(String paramString) {
/* 386 */     return "\"" + paramString + "\"";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getPackageName(PackageDoc paramPackageDoc) {
/* 395 */     return (paramPackageDoc == null || paramPackageDoc.name().length() == 0) ? "<Unnamed>" : paramPackageDoc
/* 396 */       .name();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getPackageFileHeadName(PackageDoc paramPackageDoc) {
/* 405 */     return (paramPackageDoc == null || paramPackageDoc.name().length() == 0) ? "default" : paramPackageDoc
/* 406 */       .name();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String replaceText(String paramString1, String paramString2, String paramString3) {
/* 417 */     if (paramString2 == null || paramString3 == null || paramString2.equals(paramString3)) {
/* 418 */       return paramString1;
/*     */     }
/* 420 */     return paramString1.replace(paramString2, paramString3);
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
/*     */   public static boolean isDocumentedAnnotation(AnnotationTypeDoc paramAnnotationTypeDoc) {
/* 432 */     AnnotationDesc[] arrayOfAnnotationDesc = paramAnnotationTypeDoc.annotations();
/* 433 */     for (byte b = 0; b < arrayOfAnnotationDesc.length; b++) {
/* 434 */       if (arrayOfAnnotationDesc[b].annotationType().qualifiedName().equals(Documented.class
/* 435 */           .getName())) {
/* 436 */         return true;
/*     */       }
/*     */     } 
/* 439 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isDeclarationTarget(AnnotationDesc paramAnnotationDesc) {
/* 444 */     AnnotationDesc.ElementValuePair[] arrayOfElementValuePair = paramAnnotationDesc.elementValues();
/* 445 */     if (arrayOfElementValuePair == null || arrayOfElementValuePair.length != 1 || 
/*     */       
/* 447 */       !"value".equals(arrayOfElementValuePair[0].element().name()) || 
/* 448 */       !(arrayOfElementValuePair[0].value().value() instanceof AnnotationValue[])) {
/* 449 */       return true;
/*     */     }
/* 451 */     AnnotationValue[] arrayOfAnnotationValue = (AnnotationValue[])arrayOfElementValuePair[0].value().value();
/* 452 */     for (byte b = 0; b < arrayOfAnnotationValue.length; b++) {
/* 453 */       Object object = arrayOfAnnotationValue[b].value();
/* 454 */       if (!(object instanceof FieldDoc)) {
/* 455 */         return true;
/*     */       }
/* 457 */       FieldDoc fieldDoc = (FieldDoc)object;
/* 458 */       if (isJava5DeclarationElementType(fieldDoc)) {
/* 459 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 463 */     return false;
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
/*     */   public static boolean isDeclarationAnnotation(AnnotationTypeDoc paramAnnotationTypeDoc, boolean paramBoolean) {
/* 477 */     if (!paramBoolean)
/* 478 */       return false; 
/* 479 */     AnnotationDesc[] arrayOfAnnotationDesc = paramAnnotationTypeDoc.annotations();
/*     */     
/* 481 */     if (arrayOfAnnotationDesc.length == 0)
/* 482 */       return true; 
/* 483 */     for (byte b = 0; b < arrayOfAnnotationDesc.length; b++) {
/* 484 */       if (arrayOfAnnotationDesc[b].annotationType().qualifiedName().equals(Target.class
/* 485 */           .getName()))
/* 486 */         if (isDeclarationTarget(arrayOfAnnotationDesc[b])) {
/* 487 */           return true;
/*     */         } 
/*     */     } 
/* 490 */     return false;
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
/*     */   public static boolean isLinkable(ClassDoc paramClassDoc, Configuration paramConfiguration) {
/* 508 */     return ((paramClassDoc
/* 509 */       .isIncluded() && paramConfiguration.isGeneratedDoc(paramClassDoc)) || (paramConfiguration.extern
/* 510 */       .isExternal((ProgramElementDoc)paramClassDoc) && (paramClassDoc
/* 511 */       .isPublic() || paramClassDoc.isProtected())));
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
/*     */   public static Type getFirstVisibleSuperClass(ClassDoc paramClassDoc, Configuration paramConfiguration) {
/* 524 */     if (paramClassDoc == null) {
/* 525 */       return null;
/*     */     }
/* 527 */     Type type = paramClassDoc.superclassType();
/* 528 */     ClassDoc classDoc = paramClassDoc.superclass();
/* 529 */     while (type != null && 
/* 530 */       !classDoc.isPublic() && 
/* 531 */       !isLinkable(classDoc, paramConfiguration) && 
/* 532 */       !classDoc.superclass().qualifiedName().equals(classDoc.qualifiedName())) {
/*     */       
/* 534 */       type = classDoc.superclassType();
/* 535 */       classDoc = classDoc.superclass();
/*     */     } 
/* 537 */     if (paramClassDoc.equals(classDoc)) {
/* 538 */       return null;
/*     */     }
/* 540 */     return type;
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
/*     */   public static ClassDoc getFirstVisibleSuperClassCD(ClassDoc paramClassDoc, Configuration paramConfiguration) {
/* 553 */     if (paramClassDoc == null) {
/* 554 */       return null;
/*     */     }
/* 556 */     ClassDoc classDoc = paramClassDoc.superclass();
/* 557 */     while (classDoc != null && 
/* 558 */       !classDoc.isPublic() && 
/* 559 */       !isLinkable(classDoc, paramConfiguration)) {
/* 560 */       classDoc = classDoc.superclass();
/*     */     }
/* 562 */     if (paramClassDoc.equals(classDoc)) {
/* 563 */       return null;
/*     */     }
/* 565 */     return classDoc;
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
/*     */   public static String getTypeName(Configuration paramConfiguration, ClassDoc paramClassDoc, boolean paramBoolean) {
/* 578 */     String str = "";
/* 579 */     if (paramClassDoc.isOrdinaryClass()) {
/* 580 */       str = "doclet.Class";
/* 581 */     } else if (paramClassDoc.isInterface()) {
/* 582 */       str = "doclet.Interface";
/* 583 */     } else if (paramClassDoc.isException()) {
/* 584 */       str = "doclet.Exception";
/* 585 */     } else if (paramClassDoc.isError()) {
/* 586 */       str = "doclet.Error";
/* 587 */     } else if (paramClassDoc.isAnnotationType()) {
/* 588 */       str = "doclet.AnnotationType";
/* 589 */     } else if (paramClassDoc.isEnum()) {
/* 590 */       str = "doclet.Enum";
/*     */     } 
/* 592 */     return paramConfiguration.getText(paramBoolean ? 
/* 593 */         StringUtils.toLowerCase(str) : str);
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
/*     */   public static String replaceTabs(Configuration paramConfiguration, String paramString) {
/* 605 */     if (paramString.indexOf("\t") == -1) {
/* 606 */       return paramString;
/*     */     }
/* 608 */     int i = paramConfiguration.sourcetab;
/* 609 */     String str = paramConfiguration.tabSpaces;
/* 610 */     int j = paramString.length();
/* 611 */     StringBuilder stringBuilder = new StringBuilder(j);
/* 612 */     int k = 0;
/* 613 */     int m = 0;
/* 614 */     for (byte b = 0; b < j; b++) {
/* 615 */       int n; char c = paramString.charAt(b);
/* 616 */       switch (c) { case '\n':
/*     */         case '\r':
/* 618 */           m = 0;
/*     */           break;
/*     */         case '\t':
/* 621 */           stringBuilder.append(paramString, k, b);
/* 622 */           n = i - m % i;
/* 623 */           stringBuilder.append(str, 0, n);
/* 624 */           m += n;
/* 625 */           k = b + 1;
/*     */           break;
/*     */         default:
/* 628 */           m++; break; }
/*     */     
/*     */     } 
/* 631 */     stringBuilder.append(paramString, k, j);
/* 632 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String normalizeNewlines(String paramString) {
/* 636 */     StringBuilder stringBuilder = new StringBuilder();
/* 637 */     int i = paramString.length();
/* 638 */     String str = DocletConstants.NL;
/* 639 */     int j = 0;
/* 640 */     for (byte b = 0; b < i; b++) {
/* 641 */       char c = paramString.charAt(b);
/* 642 */       switch (c) {
/*     */         case '\n':
/* 644 */           stringBuilder.append(paramString, j, b);
/* 645 */           stringBuilder.append(str);
/* 646 */           j = b + 1;
/*     */           break;
/*     */         case '\r':
/* 649 */           stringBuilder.append(paramString, j, b);
/* 650 */           stringBuilder.append(str);
/* 651 */           if (b + 1 < i && paramString.charAt(b + 1) == '\n')
/* 652 */             b++; 
/* 653 */           j = b + 1;
/*     */           break;
/*     */       } 
/*     */     } 
/* 657 */     stringBuilder.append(paramString, j, i);
/* 658 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setEnumDocumentation(Configuration paramConfiguration, ClassDoc paramClassDoc) {
/* 667 */     MethodDoc[] arrayOfMethodDoc = paramClassDoc.methods();
/* 668 */     for (byte b = 0; b < arrayOfMethodDoc.length; b++) {
/* 669 */       MethodDoc methodDoc = arrayOfMethodDoc[b];
/* 670 */       if (methodDoc.name().equals("values") && (methodDoc
/* 671 */         .parameters()).length == 0) {
/* 672 */         StringBuilder stringBuilder = new StringBuilder();
/* 673 */         stringBuilder.append(paramConfiguration.getText("doclet.enum_values_doc.main", paramClassDoc.name()));
/* 674 */         stringBuilder.append("\n@return ");
/* 675 */         stringBuilder.append(paramConfiguration.getText("doclet.enum_values_doc.return"));
/* 676 */         methodDoc.setRawCommentText(stringBuilder.toString());
/* 677 */       } else if (methodDoc.name().equals("valueOf") && (methodDoc
/* 678 */         .parameters()).length == 1) {
/* 679 */         Type type = methodDoc.parameters()[0].type();
/* 680 */         if (type != null && type
/* 681 */           .qualifiedTypeName().equals(String.class.getName())) {
/* 682 */           StringBuilder stringBuilder = new StringBuilder();
/* 683 */           stringBuilder.append(paramConfiguration.getText("doclet.enum_valueof_doc.main", paramClassDoc.name()));
/* 684 */           stringBuilder.append("\n@param name ");
/* 685 */           stringBuilder.append(paramConfiguration.getText("doclet.enum_valueof_doc.param_name"));
/* 686 */           stringBuilder.append("\n@return ");
/* 687 */           stringBuilder.append(paramConfiguration.getText("doclet.enum_valueof_doc.return"));
/* 688 */           stringBuilder.append("\n@throws IllegalArgumentException ");
/* 689 */           stringBuilder.append(paramConfiguration.getText("doclet.enum_valueof_doc.throws_ila"));
/* 690 */           stringBuilder.append("\n@throws NullPointerException ");
/* 691 */           stringBuilder.append(paramConfiguration.getText("doclet.enum_valueof_doc.throws_npe"));
/* 692 */           methodDoc.setRawCommentText(stringBuilder.toString());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isDeprecated(Doc paramDoc) {
/*     */     AnnotationDesc[] arrayOfAnnotationDesc;
/* 705 */     if ((paramDoc.tags("deprecated")).length > 0) {
/* 706 */       return true;
/*     */     }
/*     */     
/* 709 */     if (paramDoc instanceof PackageDoc) {
/* 710 */       arrayOfAnnotationDesc = ((PackageDoc)paramDoc).annotations();
/*     */     } else {
/* 712 */       arrayOfAnnotationDesc = ((ProgramElementDoc)paramDoc).annotations();
/* 713 */     }  for (byte b = 0; b < arrayOfAnnotationDesc.length; b++) {
/* 714 */       if (arrayOfAnnotationDesc[b].annotationType().qualifiedName().equals(Deprecated.class
/* 715 */           .getName())) {
/* 716 */         return true;
/*     */       }
/*     */     } 
/* 719 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String propertyNameFromMethodName(Configuration paramConfiguration, String paramString) {
/* 729 */     String str = null;
/* 730 */     if (paramString.startsWith("get") || paramString.startsWith("set")) {
/* 731 */       str = paramString.substring(3);
/* 732 */     } else if (paramString.startsWith("is")) {
/* 733 */       str = paramString.substring(2);
/*     */     } 
/* 735 */     if (str == null || str.isEmpty()) {
/* 736 */       return "";
/*     */     }
/* 738 */     return str.substring(0, 1).toLowerCase(paramConfiguration.getLocale()) + str
/* 739 */       .substring(1);
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
/*     */   public static ClassDoc[] filterOutPrivateClasses(ClassDoc[] paramArrayOfClassDoc, boolean paramBoolean) {
/* 753 */     if (!paramBoolean) {
/* 754 */       return paramArrayOfClassDoc;
/*     */     }
/* 756 */     ArrayList<ClassDoc> arrayList = new ArrayList(paramArrayOfClassDoc.length);
/*     */     
/* 758 */     for (ClassDoc classDoc : paramArrayOfClassDoc) {
/* 759 */       if (!classDoc.isPrivate() && !classDoc.isPackagePrivate()) {
/*     */ 
/*     */         
/* 762 */         Tag[] arrayOfTag = classDoc.tags("treatAsPrivate");
/* 763 */         if (arrayOfTag == null || arrayOfTag.length <= 0)
/*     */         {
/*     */           
/* 766 */           arrayList.add(classDoc); } 
/*     */       } 
/*     */     } 
/* 769 */     return arrayList.<ClassDoc>toArray(new ClassDoc[0]);
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
/*     */   public static boolean isJava5DeclarationElementType(FieldDoc paramFieldDoc) {
/* 783 */     return (paramFieldDoc.name().contentEquals(ElementType.ANNOTATION_TYPE.name()) || paramFieldDoc
/* 784 */       .name().contentEquals(ElementType.CONSTRUCTOR.name()) || paramFieldDoc
/* 785 */       .name().contentEquals(ElementType.FIELD.name()) || paramFieldDoc
/* 786 */       .name().contentEquals(ElementType.LOCAL_VARIABLE.name()) || paramFieldDoc
/* 787 */       .name().contentEquals(ElementType.METHOD.name()) || paramFieldDoc
/* 788 */       .name().contentEquals(ElementType.PACKAGE.name()) || paramFieldDoc
/* 789 */       .name().contentEquals(ElementType.PARAMETER.name()) || paramFieldDoc
/* 790 */       .name().contentEquals(ElementType.TYPE.name()));
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */