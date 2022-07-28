/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassTree
/*     */ {
/*  54 */   private List<ClassDoc> baseclasses = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   private Map<ClassDoc, List<ClassDoc>> subclasses = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   private List<ClassDoc> baseinterfaces = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   private Map<ClassDoc, List<ClassDoc>> subinterfaces = new HashMap<>();
/*     */   
/*  73 */   private List<ClassDoc> baseEnums = new ArrayList<>();
/*  74 */   private Map<ClassDoc, List<ClassDoc>> subEnums = new HashMap<>();
/*     */   
/*  76 */   private List<ClassDoc> baseAnnotationTypes = new ArrayList<>();
/*  77 */   private Map<ClassDoc, List<ClassDoc>> subAnnotationTypes = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   private Map<ClassDoc, List<ClassDoc>> implementingclasses = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassTree(Configuration paramConfiguration, boolean paramBoolean) {
/*  92 */     paramConfiguration.message.notice("doclet.Building_Tree", new Object[0]);
/*  93 */     buildTree(paramConfiguration.root.classes(), paramConfiguration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassTree(RootDoc paramRootDoc, Configuration paramConfiguration) {
/* 103 */     buildTree(paramRootDoc.classes(), paramConfiguration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassTree(ClassDoc[] paramArrayOfClassDoc, Configuration paramConfiguration) {
/* 113 */     buildTree(paramArrayOfClassDoc, paramConfiguration);
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
/*     */   private void buildTree(ClassDoc[] paramArrayOfClassDoc, Configuration paramConfiguration) {
/* 126 */     for (byte b = 0; b < paramArrayOfClassDoc.length; b++) {
/*     */ 
/*     */ 
/*     */       
/* 130 */       if (!paramConfiguration.nodeprecated || (
/* 131 */         !Util.isDeprecated((Doc)paramArrayOfClassDoc[b]) && 
/* 132 */         !Util.isDeprecated((Doc)paramArrayOfClassDoc[b].containingPackage())))
/*     */       {
/*     */ 
/*     */         
/* 136 */         if (!paramConfiguration.javafx || (paramArrayOfClassDoc[b]
/* 137 */           .tags("treatAsPrivate")).length <= 0)
/*     */         {
/*     */ 
/*     */           
/* 141 */           if (paramArrayOfClassDoc[b].isEnum()) {
/* 142 */             processType(paramArrayOfClassDoc[b], paramConfiguration, this.baseEnums, this.subEnums);
/* 143 */           } else if (paramArrayOfClassDoc[b].isClass()) {
/* 144 */             processType(paramArrayOfClassDoc[b], paramConfiguration, this.baseclasses, this.subclasses);
/* 145 */           } else if (paramArrayOfClassDoc[b].isInterface()) {
/* 146 */             processInterface(paramArrayOfClassDoc[b]);
/* 147 */             List<Comparable> list = (List)this.implementingclasses.get(paramArrayOfClassDoc[b]);
/* 148 */             if (list != null) {
/* 149 */               Collections.sort(list);
/*     */             }
/* 151 */           } else if (paramArrayOfClassDoc[b].isAnnotationType()) {
/* 152 */             processType(paramArrayOfClassDoc[b], paramConfiguration, this.baseAnnotationTypes, this.subAnnotationTypes);
/*     */           } 
/*     */         }
/*     */       }
/*     */     } 
/* 157 */     Collections.sort(this.baseinterfaces); Iterator<List<Comparable>> iterator;
/* 158 */     for (iterator = this.subinterfaces.values().iterator(); iterator.hasNext();) {
/* 159 */       Collections.sort(iterator.next());
/*     */     }
/* 161 */     for (iterator = this.subclasses.values().iterator(); iterator.hasNext();) {
/* 162 */       Collections.sort(iterator.next());
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
/*     */   private void processType(ClassDoc paramClassDoc, Configuration paramConfiguration, List<ClassDoc> paramList, Map<ClassDoc, List<ClassDoc>> paramMap) {
/* 180 */     ClassDoc classDoc = Util.getFirstVisibleSuperClassCD(paramClassDoc, paramConfiguration);
/* 181 */     if (classDoc != null) {
/* 182 */       if (!add(paramMap, classDoc, paramClassDoc)) {
/*     */         return;
/*     */       }
/* 185 */       processType(classDoc, paramConfiguration, paramList, paramMap);
/*     */     
/*     */     }
/* 188 */     else if (!paramList.contains(paramClassDoc)) {
/* 189 */       paramList.add(paramClassDoc);
/*     */     } 
/*     */     
/* 192 */     List<Type> list = Util.getAllInterfaces((Type)paramClassDoc, paramConfiguration);
/* 193 */     for (Iterator<Type> iterator = list.iterator(); iterator.hasNext();) {
/* 194 */       add(this.implementingclasses, ((Type)iterator.next()).asClassDoc(), paramClassDoc);
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
/*     */   private void processInterface(ClassDoc paramClassDoc) {
/* 207 */     ClassDoc[] arrayOfClassDoc = paramClassDoc.interfaces();
/* 208 */     if (arrayOfClassDoc.length > 0) {
/* 209 */       for (byte b = 0; b < arrayOfClassDoc.length; b++) {
/* 210 */         if (!add(this.subinterfaces, arrayOfClassDoc[b], paramClassDoc)) {
/*     */           return;
/*     */         }
/* 213 */         processInterface(arrayOfClassDoc[b]);
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/* 219 */     else if (!this.baseinterfaces.contains(paramClassDoc)) {
/* 220 */       this.baseinterfaces.add(paramClassDoc);
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
/*     */   private boolean add(Map<ClassDoc, List<ClassDoc>> paramMap, ClassDoc paramClassDoc1, ClassDoc paramClassDoc2) {
/* 235 */     List<ClassDoc> list = paramMap.get(paramClassDoc1);
/* 236 */     if (list == null) {
/* 237 */       list = new ArrayList();
/* 238 */       paramMap.put(paramClassDoc1, list);
/*     */     } 
/* 240 */     if (list.contains(paramClassDoc2)) {
/* 241 */       return false;
/*     */     }
/* 243 */     list.add(paramClassDoc2);
/*     */     
/* 245 */     return true;
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
/*     */   private List<ClassDoc> get(Map<ClassDoc, List<ClassDoc>> paramMap, ClassDoc paramClassDoc) {
/* 257 */     List<ClassDoc> list = paramMap.get(paramClassDoc);
/* 258 */     if (list == null) {
/* 259 */       return new ArrayList<>();
/*     */     }
/* 261 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ClassDoc> subclasses(ClassDoc paramClassDoc) {
/* 270 */     return get(this.subclasses, paramClassDoc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ClassDoc> subinterfaces(ClassDoc paramClassDoc) {
/* 279 */     return get(this.subinterfaces, paramClassDoc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ClassDoc> implementingclasses(ClassDoc paramClassDoc) {
/* 288 */     List<ClassDoc> list1 = get(this.implementingclasses, paramClassDoc);
/* 289 */     List<ClassDoc> list2 = allSubs(paramClassDoc, false);
/*     */ 
/*     */ 
/*     */     
/* 293 */     ListIterator<ClassDoc> listIterator = list2.listIterator();
/*     */     
/* 295 */     while (listIterator.hasNext()) {
/*     */       
/* 297 */       ListIterator<ClassDoc> listIterator1 = implementingclasses(listIterator.next()).listIterator();
/* 298 */       while (listIterator1.hasNext()) {
/* 299 */         ClassDoc classDoc = listIterator1.next();
/* 300 */         if (!list1.contains(classDoc)) {
/* 301 */           list1.add(classDoc);
/*     */         }
/*     */       } 
/*     */     } 
/* 305 */     Collections.sort(list1);
/* 306 */     return list1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ClassDoc> subs(ClassDoc paramClassDoc, boolean paramBoolean) {
/* 317 */     if (paramBoolean)
/* 318 */       return get(this.subEnums, paramClassDoc); 
/* 319 */     if (paramClassDoc.isAnnotationType())
/* 320 */       return get(this.subAnnotationTypes, paramClassDoc); 
/* 321 */     if (paramClassDoc.isInterface())
/* 322 */       return get(this.subinterfaces, paramClassDoc); 
/* 323 */     if (paramClassDoc.isClass()) {
/* 324 */       return get(this.subclasses, paramClassDoc);
/*     */     }
/* 326 */     return null;
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
/*     */   public List<ClassDoc> allSubs(ClassDoc paramClassDoc, boolean paramBoolean) {
/* 340 */     List<ClassDoc> list = subs(paramClassDoc, paramBoolean);
/* 341 */     for (byte b = 0; b < list.size(); b++) {
/* 342 */       paramClassDoc = list.get(b);
/* 343 */       List<ClassDoc> list1 = subs(paramClassDoc, paramBoolean);
/* 344 */       for (byte b1 = 0; b1 < list1.size(); b1++) {
/* 345 */         ClassDoc classDoc = list1.get(b1);
/* 346 */         if (!list.contains(classDoc)) {
/* 347 */           list.add(classDoc);
/*     */         }
/*     */       } 
/*     */     } 
/* 351 */     Collections.sort(list);
/* 352 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ClassDoc> baseclasses() {
/* 361 */     return this.baseclasses;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ClassDoc> baseinterfaces() {
/* 369 */     return this.baseinterfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ClassDoc> baseEnums() {
/* 377 */     return this.baseEnums;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ClassDoc> baseAnnotationTypes() {
/* 385 */     return this.baseAnnotationTypes;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\ClassTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */