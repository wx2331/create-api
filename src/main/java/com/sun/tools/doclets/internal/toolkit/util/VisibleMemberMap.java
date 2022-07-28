/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.AnnotationTypeElementDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ConstructorDoc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class VisibleMemberMap
/*     */ {
/*     */   private boolean noVisibleMembers = true;
/*     */   public static final int INNERCLASSES = 0;
/*     */   public static final int ENUM_CONSTANTS = 1;
/*     */   public static final int FIELDS = 2;
/*     */   public static final int CONSTRUCTORS = 3;
/*     */   public static final int METHODS = 4;
/*     */   public static final int ANNOTATION_TYPE_FIELDS = 5;
/*     */   public static final int ANNOTATION_TYPE_MEMBER_OPTIONAL = 6;
/*     */   public static final int ANNOTATION_TYPE_MEMBER_REQUIRED = 7;
/*     */   public static final int PROPERTIES = 8;
/*     */   public static final int NUM_MEMBER_TYPES = 9;
/*     */   public static final String STARTLEVEL = "start";
/*  73 */   private final List<ClassDoc> visibleClasses = new ArrayList<>();
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*  80 */   private final Map<Object, Map<ProgramElementDoc, String>> memberNameMap = new HashMap<>();
/*     */
/*     */
/*     */
/*     */
/*  85 */   private final Map<ClassDoc, ClassMembers> classMap = new HashMap<>();
/*     */
/*     */
/*     */
/*     */
/*     */   private final ClassDoc classdoc;
/*     */
/*     */
/*     */
/*     */
/*     */   private final int kind;
/*     */
/*     */
/*     */
/*     */   private final Configuration configuration;
/*     */
/*     */
/*     */
/* 103 */   private static final Map<ClassDoc, ProgramElementDoc[]> propertiesCache = (Map)new HashMap<>();
/*     */
/* 105 */   private static final Map<ProgramElementDoc, ProgramElementDoc> classPropertiesMap = new HashMap<>();
/*     */
/* 107 */   private static final Map<ProgramElementDoc, GetterSetter> getterSetterMap = new HashMap<>();
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public VisibleMemberMap(ClassDoc paramClassDoc, int paramInt, Configuration paramConfiguration) {
/* 124 */     this.classdoc = paramClassDoc;
/* 125 */     this.kind = paramInt;
/* 126 */     this.configuration = paramConfiguration;
/* 127 */     (new ClassMembers(paramClassDoc, "start")).build();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public List<ClassDoc> getVisibleClassesList() {
/* 136 */     sort(this.visibleClasses);
/* 137 */     return this.visibleClasses;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public ProgramElementDoc getPropertyMemberDoc(ProgramElementDoc paramProgramElementDoc) {
/* 146 */     return classPropertiesMap.get(paramProgramElementDoc);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public ProgramElementDoc getGetterForProperty(ProgramElementDoc paramProgramElementDoc) {
/* 155 */     return ((GetterSetter)getterSetterMap.get(paramProgramElementDoc)).getGetter();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public ProgramElementDoc getSetterForProperty(ProgramElementDoc paramProgramElementDoc) {
/* 164 */     return ((GetterSetter)getterSetterMap.get(paramProgramElementDoc)).getSetter();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private List<ProgramElementDoc> getInheritedPackagePrivateMethods(Configuration paramConfiguration) {
/* 175 */     ArrayList<ProgramElementDoc> arrayList = new ArrayList();
/* 176 */     for (ClassDoc classDoc : this.visibleClasses) {
/*     */
/* 178 */       if (classDoc != this.classdoc && classDoc
/* 179 */         .isPackagePrivate() &&
/* 180 */         !Util.isLinkable(classDoc, paramConfiguration))
/*     */       {
/*     */
/* 183 */         arrayList.addAll(getMembersFor(classDoc));
/*     */       }
/*     */     }
/* 186 */     return arrayList;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public List<ProgramElementDoc> getLeafClassMembers(Configuration paramConfiguration) {
/* 197 */     List<ProgramElementDoc> list = getMembersFor(this.classdoc);
/* 198 */     list.addAll(getInheritedPackagePrivateMethods(paramConfiguration));
/* 199 */     return list;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public List<ProgramElementDoc> getMembersFor(ClassDoc paramClassDoc) {
/* 210 */     ClassMembers classMembers = this.classMap.get(paramClassDoc);
/* 211 */     if (classMembers == null) {
/* 212 */       return new ArrayList<>();
/*     */     }
/* 214 */     return classMembers.getMembers();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private void sort(List<ClassDoc> paramList) {
/* 222 */     ArrayList<ClassDoc> arrayList1 = new ArrayList();
/* 223 */     ArrayList<ClassDoc> arrayList2 = new ArrayList();
/* 224 */     for (byte b = 0; b < paramList.size(); b++) {
/* 225 */       ClassDoc classDoc = paramList.get(b);
/* 226 */       if (classDoc.isClass()) {
/* 227 */         arrayList1.add(classDoc);
/*     */       } else {
/* 229 */         arrayList2.add(classDoc);
/*     */       }
/*     */     }
/* 232 */     paramList.clear();
/* 233 */     paramList.addAll(arrayList1);
/* 234 */     paramList.addAll(arrayList2);
/*     */   }
/*     */
/*     */   private void fillMemberLevelMap(List<ProgramElementDoc> paramList, String paramString) {
/* 238 */     for (byte b = 0; b < paramList.size(); b++) {
/* 239 */       Object object = getMemberKey(paramList.get(b));
/* 240 */       Map<Object, Object> map = (Map)this.memberNameMap.get(object);
/* 241 */       if (map == null) {
/* 242 */         map = new HashMap<>();
/* 243 */         this.memberNameMap.put(object, map);
/*     */       }
/* 245 */       map.put(paramList.get(b), paramString);
/*     */     }
/*     */   }
/*     */
/*     */   private void purgeMemberLevelMap(List<ProgramElementDoc> paramList, String paramString) {
/* 250 */     for (byte b = 0; b < paramList.size(); b++) {
/* 251 */       Object object = getMemberKey(paramList.get(b));
/* 252 */       Map map = this.memberNameMap.get(object);
/* 253 */       if (map != null && paramString.equals(map.get(paramList.get(b)))) {
/* 254 */         map.remove(paramList.get(b));
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   private class ClassMember
/*     */   {
/*     */     private Set<ProgramElementDoc> members;
/*     */
/*     */
/*     */     public ClassMember(ProgramElementDoc param1ProgramElementDoc) {
/* 267 */       this.members = new HashSet<>();
/* 268 */       this.members.add(param1ProgramElementDoc);
/*     */     }
/*     */
/*     */     public void addMember(ProgramElementDoc param1ProgramElementDoc) {
/* 272 */       this.members.add(param1ProgramElementDoc);
/*     */     }
/*     */
/*     */     public boolean isEqual(MethodDoc param1MethodDoc) {
/* 276 */       for (MethodDoc methodDoc : this.members) {
/*     */
/* 278 */         if (Util.executableMembersEqual((ExecutableMemberDoc)param1MethodDoc, (ExecutableMemberDoc)methodDoc)) {
/* 279 */           this.members.add(param1MethodDoc);
/* 280 */           return true;
/*     */         }
/*     */       }
/* 283 */       return false;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private class ClassMembers
/*     */   {
/*     */     private ClassDoc mappingClass;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 302 */     private List<ProgramElementDoc> members = new ArrayList<>();
/*     */
/*     */
/*     */
/*     */     private String level;
/*     */
/*     */
/*     */
/*     */     private final Pattern pattern;
/*     */
/*     */
/*     */
/*     */     public List<ProgramElementDoc> getMembers() {
/* 315 */       return this.members;
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private void build() {
/* 338 */       if (VisibleMemberMap.this.kind == 3) {
/* 339 */         addMembers(this.mappingClass);
/*     */       } else {
/* 341 */         mapClass();
/*     */       }
/*     */     }
/*     */
/*     */     private void mapClass() {
/* 346 */       addMembers(this.mappingClass);
/* 347 */       ClassDoc[] arrayOfClassDoc = this.mappingClass.interfaces();
/* 348 */       for (byte b = 0; b < arrayOfClassDoc.length; b++) {
/* 349 */         String str = this.level + '\001';
/* 350 */         ClassMembers classMembers = new ClassMembers(arrayOfClassDoc[b], str);
/* 351 */         classMembers.mapClass();
/*     */       }
/* 353 */       if (this.mappingClass.isClass()) {
/* 354 */         ClassDoc classDoc = this.mappingClass.superclass();
/* 355 */         if (classDoc != null && !this.mappingClass.equals(classDoc)) {
/* 356 */           ClassMembers classMembers = new ClassMembers(classDoc, this.level + "c");
/*     */
/* 358 */           classMembers.mapClass();
/*     */         }
/*     */       }
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
/*     */     private void addMembers(ClassDoc param1ClassDoc) {
/* 372 */       List<ProgramElementDoc> list = getClassMembers(param1ClassDoc, true);
/* 373 */       ArrayList<ProgramElementDoc> arrayList = new ArrayList();
/* 374 */       for (byte b = 0; b < list.size(); b++) {
/* 375 */         ProgramElementDoc programElementDoc = list.get(b);
/* 376 */         if (!found(this.members, programElementDoc) &&
/* 377 */           memberIsVisible(programElementDoc) &&
/* 378 */           !isOverridden(programElementDoc, this.level) &&
/* 379 */           !isTreatedAsPrivate(programElementDoc)) {
/* 380 */           arrayList.add(programElementDoc);
/*     */         }
/*     */       }
/* 383 */       if (arrayList.size() > 0) {
/* 384 */         VisibleMemberMap.this.noVisibleMembers = false;
/*     */       }
/* 386 */       this.members.addAll(arrayList);
/* 387 */       VisibleMemberMap.this.fillMemberLevelMap(getClassMembers(param1ClassDoc, false), this.level);
/*     */     }
/*     */
/*     */     private boolean isTreatedAsPrivate(ProgramElementDoc param1ProgramElementDoc) {
/* 391 */       if (!VisibleMemberMap.this.configuration.javafx) {
/* 392 */         return false;
/*     */       }
/*     */
/* 395 */       Tag[] arrayOfTag = param1ProgramElementDoc.tags("@treatAsPrivate");
/* 396 */       return (arrayOfTag != null && arrayOfTag.length > 0);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private boolean memberIsVisible(ProgramElementDoc param1ProgramElementDoc) {
/* 407 */       if (param1ProgramElementDoc.containingClass().equals(VisibleMemberMap.this.classdoc))
/*     */       {
/*     */
/* 410 */         return true; }
/* 411 */       if (param1ProgramElementDoc.isPrivate())
/*     */       {
/*     */
/* 414 */         return false; }
/* 415 */       if (param1ProgramElementDoc.isPackagePrivate())
/*     */       {
/*     */
/* 418 */         return param1ProgramElementDoc.containingClass().containingPackage().equals(VisibleMemberMap.this
/* 419 */             .classdoc.containingPackage());
/*     */       }
/*     */
/* 422 */       return true;
/*     */     } private List<ProgramElementDoc> getClassMembers(ClassDoc param1ClassDoc, boolean param1Boolean) {
/*     */       AnnotationTypeElementDoc[] arrayOfAnnotationTypeElementDoc;
/*     */       ClassDoc[] arrayOfClassDoc;
/*     */       FieldDoc[] arrayOfFieldDoc1;
/*     */       ConstructorDoc[] arrayOfConstructorDoc;
/*     */       MethodDoc[] arrayOfMethodDoc;
/*     */       ProgramElementDoc[] arrayOfProgramElementDoc;
/* 430 */       if (param1ClassDoc.isEnum() && VisibleMemberMap.this.kind == 3)
/*     */       {
/*     */
/* 433 */         return Arrays.asList(new ProgramElementDoc[0]);
/*     */       }
/* 435 */       FieldDoc[] arrayOfFieldDoc2 = null;
/* 436 */       switch (VisibleMemberMap.this.kind) {
/*     */         case 5:
/* 438 */           arrayOfFieldDoc2 = param1ClassDoc.fields(param1Boolean);
/*     */           break;
/*     */
/*     */         case 6:
/* 442 */           arrayOfAnnotationTypeElementDoc = param1ClassDoc.isAnnotationType() ? filter((AnnotationTypeDoc)param1ClassDoc, false) : new AnnotationTypeElementDoc[0];
/*     */           break;
/*     */
/*     */
/*     */         case 7:
/* 447 */           arrayOfAnnotationTypeElementDoc = param1ClassDoc.isAnnotationType() ? filter((AnnotationTypeDoc)param1ClassDoc, true) : new AnnotationTypeElementDoc[0];
/*     */           break;
/*     */
/*     */         case 0:
/* 451 */           arrayOfClassDoc = param1ClassDoc.innerClasses(param1Boolean);
/*     */           break;
/*     */         case 1:
/* 454 */           arrayOfFieldDoc1 = param1ClassDoc.enumConstants();
/*     */           break;
/*     */         case 2:
/* 457 */           arrayOfFieldDoc1 = param1ClassDoc.fields(param1Boolean);
/*     */           break;
/*     */         case 3:
/* 460 */           arrayOfConstructorDoc = param1ClassDoc.constructors();
/*     */           break;
/*     */         case 4:
/* 463 */           arrayOfMethodDoc = param1ClassDoc.methods(param1Boolean);
/* 464 */           checkOnPropertiesTags(arrayOfMethodDoc);
/*     */           break;
/*     */         case 8:
/* 467 */           arrayOfProgramElementDoc = properties(param1ClassDoc, param1Boolean);
/*     */           break;
/*     */         default:
/* 470 */           arrayOfProgramElementDoc = new ProgramElementDoc[0];
/*     */           break;
/*     */       }
/* 473 */       if (VisibleMemberMap.this.configuration.nodeprecated) {
/* 474 */         return Util.excludeDeprecatedMembersAsList(arrayOfProgramElementDoc);
/*     */       }
/* 476 */       return Arrays.asList(arrayOfProgramElementDoc);
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
/*     */
/*     */     private AnnotationTypeElementDoc[] filter(AnnotationTypeDoc param1AnnotationTypeDoc, boolean param1Boolean) {
/* 492 */       AnnotationTypeElementDoc[] arrayOfAnnotationTypeElementDoc = param1AnnotationTypeDoc.elements();
/* 493 */       ArrayList<AnnotationTypeElementDoc> arrayList = new ArrayList();
/* 494 */       for (byte b = 0; b < arrayOfAnnotationTypeElementDoc.length; b++) {
/* 495 */         if ((param1Boolean && arrayOfAnnotationTypeElementDoc[b].defaultValue() == null) || (!param1Boolean && arrayOfAnnotationTypeElementDoc[b]
/* 496 */           .defaultValue() != null)) {
/* 497 */           arrayList.add(arrayOfAnnotationTypeElementDoc[b]);
/*     */         }
/*     */       }
/* 500 */       return arrayList.<AnnotationTypeElementDoc>toArray(new AnnotationTypeElementDoc[0]);
/*     */     }
/*     */
/*     */     private boolean found(List<ProgramElementDoc> param1List, ProgramElementDoc param1ProgramElementDoc) {
/* 504 */       for (byte b = 0; b < param1List.size(); b++) {
/* 505 */         ProgramElementDoc programElementDoc = param1List.get(b);
/* 506 */         if (Util.matches(programElementDoc, param1ProgramElementDoc)) {
/* 507 */           return true;
/*     */         }
/*     */       }
/* 510 */       return false;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private boolean isOverridden(ProgramElementDoc param1ProgramElementDoc, String param1String) {
/* 520 */       Map map = (Map)VisibleMemberMap.this.memberNameMap.get(VisibleMemberMap.this.getMemberKey(param1ProgramElementDoc));
/* 521 */       if (map == null)
/* 522 */         return false;
/* 523 */       String str = null;
/* 524 */       Iterator<String> iterator = map.values().iterator();
/* 525 */       while (iterator.hasNext()) {
/* 526 */         str = iterator.next();
/* 527 */         if (str.equals("start") || (param1String
/* 528 */           .startsWith(str) &&
/* 529 */           !param1String.equals(str))) {
/* 530 */           return true;
/*     */         }
/*     */       }
/* 533 */       return false;
/*     */     }
/*     */
/*     */     private ProgramElementDoc[] properties(ClassDoc param1ClassDoc, boolean param1Boolean) {
/* 537 */       MethodDoc[] arrayOfMethodDoc = param1ClassDoc.methods(param1Boolean);
/* 538 */       FieldDoc[] arrayOfFieldDoc = param1ClassDoc.fields(false);
/*     */
/* 540 */       if (VisibleMemberMap.propertiesCache.containsKey(param1ClassDoc)) {
/* 541 */         return (ProgramElementDoc[])VisibleMemberMap.propertiesCache.get(param1ClassDoc);
/*     */       }
/*     */
/* 544 */       ArrayList<MethodDoc> arrayList = new ArrayList();
/*     */
/* 546 */       for (MethodDoc methodDoc : arrayOfMethodDoc) {
/*     */
/* 548 */         if (isPropertyMethod(methodDoc)) {
/*     */
/*     */
/*     */
/* 552 */           MethodDoc methodDoc1 = getterForField(arrayOfMethodDoc, methodDoc);
/* 553 */           MethodDoc methodDoc2 = setterForField(arrayOfMethodDoc, methodDoc);
/* 554 */           FieldDoc fieldDoc = fieldForProperty(arrayOfFieldDoc, methodDoc);
/*     */
/* 556 */           addToPropertiesMap(methodDoc2, methodDoc1, methodDoc, fieldDoc);
/* 557 */           VisibleMemberMap.getterSetterMap.put(methodDoc, new GetterSetter((ProgramElementDoc)methodDoc1, (ProgramElementDoc)methodDoc2));
/* 558 */           arrayList.add(methodDoc);
/*     */         }
/*     */       }
/* 561 */       ProgramElementDoc[] arrayOfProgramElementDoc = arrayList.<ProgramElementDoc>toArray(new ProgramElementDoc[arrayList.size()]);
/* 562 */       VisibleMemberMap.propertiesCache.put(param1ClassDoc, arrayOfProgramElementDoc);
/* 563 */       return arrayOfProgramElementDoc;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     private void addToPropertiesMap(MethodDoc param1MethodDoc1, MethodDoc param1MethodDoc2, MethodDoc param1MethodDoc3, FieldDoc param1FieldDoc) {
/* 570 */       if (param1FieldDoc == null || param1FieldDoc
/* 571 */         .getRawCommentText() == null || param1FieldDoc
/* 572 */         .getRawCommentText().length() == 0) {
/* 573 */         addToPropertiesMap((ProgramElementDoc)param1MethodDoc1, (ProgramElementDoc)param1MethodDoc3);
/* 574 */         addToPropertiesMap((ProgramElementDoc)param1MethodDoc2, (ProgramElementDoc)param1MethodDoc3);
/* 575 */         addToPropertiesMap((ProgramElementDoc)param1MethodDoc3, (ProgramElementDoc)param1MethodDoc3);
/*     */       } else {
/* 577 */         addToPropertiesMap((ProgramElementDoc)param1MethodDoc2, (ProgramElementDoc)param1FieldDoc);
/* 578 */         addToPropertiesMap((ProgramElementDoc)param1MethodDoc1, (ProgramElementDoc)param1FieldDoc);
/* 579 */         addToPropertiesMap((ProgramElementDoc)param1MethodDoc3, (ProgramElementDoc)param1FieldDoc);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     private void addToPropertiesMap(ProgramElementDoc param1ProgramElementDoc1, ProgramElementDoc param1ProgramElementDoc2) {
/* 585 */       if (null == param1ProgramElementDoc1 || null == param1ProgramElementDoc2) {
/*     */         return;
/*     */       }
/* 588 */       String str = param1ProgramElementDoc1.getRawCommentText();
/*     */
/*     */
/*     */
/*     */
/*     */
/* 594 */       if (null == str || 0 == str.length() || param1ProgramElementDoc1
/* 595 */         .equals(param1ProgramElementDoc2)) {
/* 596 */         VisibleMemberMap.classPropertiesMap.put(param1ProgramElementDoc1, param1ProgramElementDoc2);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */     private MethodDoc getterForField(MethodDoc[] param1ArrayOfMethodDoc, MethodDoc param1MethodDoc) {
/* 602 */       String str4, str1 = param1MethodDoc.name();
/*     */
/* 604 */       String str2 = str1.substring(0, str1
/* 605 */           .lastIndexOf("Property"));
/*     */
/*     */
/* 608 */       String str3 = "" + Character.toUpperCase(str2.charAt(0)) + str2.substring(1);
/*     */
/* 610 */       String str5 = param1MethodDoc.returnType().toString();
/* 611 */       if ("boolean".equals(str5) || str5
/* 612 */         .endsWith("BooleanProperty")) {
/* 613 */         str4 = "(is|get)" + str3;
/*     */       } else {
/* 615 */         str4 = "get" + str3;
/*     */       }
/*     */
/* 618 */       for (MethodDoc methodDoc : param1ArrayOfMethodDoc) {
/* 619 */         if (Pattern.matches(str4, methodDoc.name()) &&
/* 620 */           0 == (methodDoc.parameters()).length && (methodDoc
/* 621 */           .isPublic() || methodDoc.isProtected())) {
/* 622 */           return methodDoc;
/*     */         }
/*     */       }
/*     */
/* 626 */       return null;
/*     */     }
/*     */
/*     */
/*     */     private MethodDoc setterForField(MethodDoc[] param1ArrayOfMethodDoc, MethodDoc param1MethodDoc) {
/* 631 */       String str1 = param1MethodDoc.name();
/*     */
/* 633 */       String str2 = str1.substring(0, str1
/* 634 */           .lastIndexOf("Property"));
/*     */
/*     */
/* 637 */       String str3 = "" + Character.toUpperCase(str2.charAt(0)) + str2.substring(1);
/* 638 */       String str4 = "set" + str3;
/*     */
/* 640 */       for (MethodDoc methodDoc : param1ArrayOfMethodDoc) {
/* 641 */         if (str4.equals(methodDoc.name()) &&
/* 642 */           1 == (methodDoc.parameters()).length && "void"
/* 643 */           .equals(methodDoc.returnType().simpleTypeName()) && (methodDoc
/* 644 */           .isPublic() || methodDoc.isProtected())) {
/* 645 */           return methodDoc;
/*     */         }
/*     */       }
/*     */
/* 649 */       return null;
/*     */     }
/*     */
/*     */
/*     */     private FieldDoc fieldForProperty(FieldDoc[] param1ArrayOfFieldDoc, MethodDoc param1MethodDoc) {
/* 654 */       for (FieldDoc fieldDoc : param1ArrayOfFieldDoc) {
/* 655 */         String str1 = fieldDoc.name();
/* 656 */         String str2 = str1 + "Property";
/* 657 */         if (str2.equals(param1MethodDoc.name())) {
/* 658 */           return fieldDoc;
/*     */         }
/*     */       }
/* 661 */       return null;
/*     */     }
/*     */
/*     */     private ClassMembers(ClassDoc param1ClassDoc, String param1String) {
/* 665 */       this.pattern = Pattern.compile("[sg]et\\p{Upper}.*"); this.mappingClass = param1ClassDoc; this.level = param1String; if (VisibleMemberMap.this.classMap.containsKey(param1ClassDoc) && param1String.startsWith((VisibleMemberMap.this.classMap.get(param1ClassDoc)).level)) { VisibleMemberMap.this.purgeMemberLevelMap(getClassMembers(param1ClassDoc, false), (VisibleMemberMap.this.classMap.get(param1ClassDoc)).level); VisibleMemberMap.this.classMap.remove(param1ClassDoc); VisibleMemberMap.this.visibleClasses.remove(param1ClassDoc); }
/*     */        if (!VisibleMemberMap.this.classMap.containsKey(param1ClassDoc)) { VisibleMemberMap.this.classMap.put(param1ClassDoc, this); VisibleMemberMap.this.visibleClasses.add(param1ClassDoc); }
/* 667 */        } private boolean isPropertyMethod(MethodDoc param1MethodDoc) { if (!VisibleMemberMap.this.configuration.javafx) {
/* 668 */         return false;
/*     */       }
/* 670 */       if (!param1MethodDoc.name().endsWith("Property")) {
/* 671 */         return false;
/*     */       }
/*     */
/* 674 */       if (!memberIsVisible((ProgramElementDoc)param1MethodDoc)) {
/* 675 */         return false;
/*     */       }
/*     */
/* 678 */       if (this.pattern.matcher(param1MethodDoc.name()).matches()) {
/* 679 */         return false;
/*     */       }
/* 681 */       if ((param1MethodDoc.typeParameters()).length > 0) {
/* 682 */         return false;
/*     */       }
/* 684 */       return (0 == (param1MethodDoc.parameters()).length &&
/* 685 */         !"void".equals(param1MethodDoc.returnType().simpleTypeName())); }
/*     */
/*     */
/*     */     private void checkOnPropertiesTags(MethodDoc[] param1ArrayOfMethodDoc) {
/* 689 */       for (MethodDoc methodDoc : param1ArrayOfMethodDoc) {
/* 690 */         if (methodDoc.isIncluded()) {
/* 691 */           for (Tag tag : methodDoc.tags()) {
/* 692 */             String str = tag.name();
/* 693 */             if (str.equals("@propertySetter") || str
/* 694 */               .equals("@propertyGetter") || str
/* 695 */               .equals("@propertyDescription")) {
/* 696 */               if (!isPropertyGetterOrSetter(param1ArrayOfMethodDoc, methodDoc)) {
/* 697 */                 VisibleMemberMap.this.configuration.message.warning(tag.position(), "doclet.javafx_tag_misuse", new Object[0]);
/*     */               }
/*     */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/*     */     private boolean isPropertyGetterOrSetter(MethodDoc[] param1ArrayOfMethodDoc, MethodDoc param1MethodDoc) {
/* 709 */       boolean bool = false;
/* 710 */       String str = Util.propertyNameFromMethodName(VisibleMemberMap.this.configuration, param1MethodDoc.name());
/* 711 */       if (!str.isEmpty()) {
/* 712 */         String str1 = str + "Property";
/* 713 */         for (MethodDoc methodDoc : param1ArrayOfMethodDoc) {
/* 714 */           if (methodDoc.name().equals(str1)) {
/* 715 */             bool = true;
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/* 720 */       return bool;
/*     */     }
/*     */   }
/*     */
/*     */   private class GetterSetter {
/*     */     private final ProgramElementDoc getter;
/*     */     private final ProgramElementDoc setter;
/*     */
/*     */     public GetterSetter(ProgramElementDoc param1ProgramElementDoc1, ProgramElementDoc param1ProgramElementDoc2) {
/* 729 */       this.getter = param1ProgramElementDoc1;
/* 730 */       this.setter = param1ProgramElementDoc2;
/*     */     }
/*     */
/*     */     public ProgramElementDoc getGetter() {
/* 734 */       return this.getter;
/*     */     }
/*     */
/*     */     public ProgramElementDoc getSetter() {
/* 738 */       return this.setter;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean noVisibleMembers() {
/* 748 */     return this.noVisibleMembers;
/*     */   }
/*     */
/*     */   private ClassMember getClassMember(MethodDoc paramMethodDoc) {
/* 752 */     for (ClassMember classMember : this.memberNameMap.keySet()) {
/*     */
/* 754 */       if (classMember instanceof String)
/*     */         continue;
/* 756 */       if (((ClassMember)classMember).isEqual(paramMethodDoc)) {
/* 757 */         return classMember;
/*     */       }
/*     */     }
/* 760 */     return new ClassMember((ProgramElementDoc)paramMethodDoc);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   private Object getMemberKey(ProgramElementDoc paramProgramElementDoc) {
/* 767 */     if (paramProgramElementDoc.isConstructor())
/* 768 */       return paramProgramElementDoc.name() + ((ExecutableMemberDoc)paramProgramElementDoc).signature();
/* 769 */     if (paramProgramElementDoc.isMethod())
/* 770 */       return getClassMember((MethodDoc)paramProgramElementDoc);
/* 771 */     if (paramProgramElementDoc.isField() || paramProgramElementDoc.isEnumConstant() || paramProgramElementDoc.isAnnotationTypeElement()) {
/* 772 */       return paramProgramElementDoc.name();
/*     */     }
/* 774 */     String str = paramProgramElementDoc.name();
/*     */
/* 776 */     str = (str.indexOf('.') != 0) ? str.substring(str.lastIndexOf('.'), str.length()) : str;
/* 777 */     return "clint" + str;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\VisibleMemberMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
