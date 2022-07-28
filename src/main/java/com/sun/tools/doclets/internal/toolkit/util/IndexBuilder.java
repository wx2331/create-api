/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IndexBuilder
/*     */ {
/*  54 */   private Map<Character, List<Doc>> indexmap = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean noDeprecated;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean classesOnly;
/*     */ 
/*     */   
/*     */   private boolean javafx;
/*     */ 
/*     */   
/*     */   protected final Object[] elements;
/*     */ 
/*     */ 
/*     */   
/*     */   private class DocComparator
/*     */     implements Comparator<Doc>
/*     */   {
/*     */     private DocComparator() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public int compare(Doc param1Doc1, Doc param1Doc2) {
/*  80 */       String str1 = param1Doc1.name();
/*  81 */       String str2 = param1Doc2.name();
/*     */       int i;
/*  83 */       if ((i = str1.compareToIgnoreCase(str2)) != 0)
/*  84 */         return i; 
/*  85 */       if (param1Doc1 instanceof ProgramElementDoc && param1Doc2 instanceof ProgramElementDoc) {
/*  86 */         str1 = ((ProgramElementDoc)param1Doc1).qualifiedName();
/*  87 */         str2 = ((ProgramElementDoc)param1Doc2).qualifiedName();
/*  88 */         return str1.compareToIgnoreCase(str2);
/*     */       } 
/*  90 */       return 0;
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
/*     */   public IndexBuilder(Configuration paramConfiguration, boolean paramBoolean) {
/* 103 */     this(paramConfiguration, paramBoolean, false);
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
/*     */   public IndexBuilder(Configuration paramConfiguration, boolean paramBoolean1, boolean paramBoolean2) {
/* 116 */     if (paramBoolean2) {
/* 117 */       paramConfiguration.message.notice("doclet.Building_Index_For_All_Classes", new Object[0]);
/*     */     } else {
/* 119 */       paramConfiguration.message.notice("doclet.Building_Index", new Object[0]);
/*     */     } 
/* 121 */     this.noDeprecated = paramBoolean1;
/* 122 */     this.classesOnly = paramBoolean2;
/* 123 */     this.javafx = paramConfiguration.javafx;
/* 124 */     buildIndexMap(paramConfiguration.root);
/* 125 */     Set<Character> set = this.indexmap.keySet();
/* 126 */     this.elements = set.toArray();
/* 127 */     Arrays.sort(this.elements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sortIndexMap() {
/* 135 */     for (Iterator<List> iterator = this.indexmap.values().iterator(); iterator.hasNext();) {
/* 136 */       Collections.sort(iterator.next(), new DocComparator());
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
/*     */   protected void buildIndexMap(RootDoc paramRootDoc) {
/* 148 */     PackageDoc[] arrayOfPackageDoc = paramRootDoc.specifiedPackages();
/* 149 */     ClassDoc[] arrayOfClassDoc = paramRootDoc.classes();
/* 150 */     if (!this.classesOnly) {
/* 151 */       if (arrayOfPackageDoc.length == 0) {
/* 152 */         HashSet<PackageDoc> hashSet = new HashSet();
/*     */         
/* 154 */         for (byte b = 0; b < arrayOfClassDoc.length; b++) {
/* 155 */           PackageDoc packageDoc = arrayOfClassDoc[b].containingPackage();
/* 156 */           if (packageDoc != null && packageDoc.name().length() > 0) {
/* 157 */             hashSet.add(packageDoc);
/*     */           }
/*     */         } 
/* 160 */         adjustIndexMap((Doc[])hashSet.toArray((Object[])arrayOfPackageDoc));
/*     */       } else {
/* 162 */         adjustIndexMap((Doc[])arrayOfPackageDoc);
/*     */       } 
/*     */     }
/* 165 */     adjustIndexMap((Doc[])arrayOfClassDoc);
/* 166 */     if (!this.classesOnly) {
/* 167 */       for (byte b = 0; b < arrayOfClassDoc.length; b++) {
/* 168 */         if (shouldAddToIndexMap((Doc)arrayOfClassDoc[b])) {
/* 169 */           putMembersInIndexMap(arrayOfClassDoc[b]);
/*     */         }
/*     */       } 
/*     */     }
/* 173 */     sortIndexMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void putMembersInIndexMap(ClassDoc paramClassDoc) {
/* 183 */     adjustIndexMap((Doc[])paramClassDoc.fields());
/* 184 */     adjustIndexMap((Doc[])paramClassDoc.methods());
/* 185 */     adjustIndexMap((Doc[])paramClassDoc.constructors());
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
/*     */   protected void adjustIndexMap(Doc[] paramArrayOfDoc) {
/* 197 */     for (byte b = 0; b < paramArrayOfDoc.length; b++) {
/* 198 */       if (shouldAddToIndexMap(paramArrayOfDoc[b])) {
/* 199 */         String str = paramArrayOfDoc[b].name();
/*     */ 
/*     */         
/* 202 */         boolean bool = (str.length() == 0) ? true : Character.toUpperCase(str.charAt(0));
/* 203 */         Character character = new Character(bool);
/* 204 */         List<Doc> list = this.indexmap.get(character);
/* 205 */         if (list == null) {
/* 206 */           list = new ArrayList();
/* 207 */           this.indexmap.put(character, list);
/*     */         } 
/* 209 */         list.add(paramArrayOfDoc[b]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldAddToIndexMap(Doc paramDoc) {
/* 218 */     if (this.javafx && (
/* 219 */       paramDoc.tags("treatAsPrivate")).length > 0) {
/* 220 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 224 */     if (paramDoc instanceof PackageDoc)
/*     */     {
/*     */       
/* 227 */       return (!this.noDeprecated || !Util.isDeprecated(paramDoc));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 232 */     return (!this.noDeprecated || (
/* 233 */       !Util.isDeprecated(paramDoc) && 
/* 234 */       !Util.isDeprecated((Doc)((ProgramElementDoc)paramDoc).containingPackage())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Character, List<Doc>> getIndexMap() {
/* 243 */     return this.indexmap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Doc> getMemberList(Character paramCharacter) {
/* 253 */     return this.indexmap.get(paramCharacter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] elements() {
/* 260 */     return this.elements;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\IndexBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */