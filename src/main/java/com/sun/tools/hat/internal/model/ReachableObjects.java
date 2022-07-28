/*     */ package com.sun.tools.hat.internal.model;
/*     */ 
/*     */ import com.sun.tools.hat.internal.util.ArraySorter;
/*     */ import com.sun.tools.hat.internal.util.Comparer;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReachableObjects
/*     */ {
/*     */   private JavaHeapObject root;
/*     */   private JavaThing[] reachables;
/*     */   private String[] excludedFields;
/*     */   private String[] usedFields;
/*     */   private long totalSize;
/*     */   
/*     */   public ReachableObjects(JavaHeapObject paramJavaHeapObject, final ReachableExcludes excludes) {
/*  49 */     this.root = paramJavaHeapObject;
/*     */     
/*  51 */     final Hashtable<Object, Object> bag = new Hashtable<>();
/*  52 */     final Hashtable<Object, Object> fieldsExcluded = new Hashtable<>();
/*  53 */     final Hashtable<Object, Object> fieldsUsed = new Hashtable<>();
/*  54 */     AbstractJavaHeapObjectVisitor abstractJavaHeapObjectVisitor = new AbstractJavaHeapObjectVisitor()
/*     */       {
/*     */         public void visit(JavaHeapObject param1JavaHeapObject) {
/*  57 */           if (param1JavaHeapObject != null && param1JavaHeapObject.getSize() > 0 && bag.get(param1JavaHeapObject) == null) {
/*  58 */             bag.put(param1JavaHeapObject, param1JavaHeapObject);
/*  59 */             param1JavaHeapObject.visitReferencedObjects(this);
/*     */           } 
/*     */         }
/*     */         
/*     */         public boolean mightExclude() {
/*  64 */           return (excludes != null);
/*     */         }
/*     */         
/*     */         public boolean exclude(JavaClass param1JavaClass, JavaField param1JavaField) {
/*  68 */           if (excludes == null) {
/*  69 */             return false;
/*     */           }
/*  71 */           String str = param1JavaClass.getName() + "." + param1JavaField.getName();
/*  72 */           if (excludes.isExcluded(str)) {
/*  73 */             fieldsExcluded.put(str, str);
/*  74 */             return true;
/*     */           } 
/*  76 */           fieldsUsed.put(str, str);
/*  77 */           return false;
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */     
/*  83 */     abstractJavaHeapObjectVisitor.visit(paramJavaHeapObject);
/*  84 */     hashtable1.remove(paramJavaHeapObject);
/*     */ 
/*     */     
/*  87 */     JavaThing[] arrayOfJavaThing = new JavaThing[hashtable1.size()];
/*  88 */     byte b = 0;
/*  89 */     for (Enumeration<JavaThing> enumeration = hashtable1.elements(); enumeration.hasMoreElements();) {
/*  90 */       arrayOfJavaThing[b++] = enumeration.nextElement();
/*     */     }
/*  92 */     ArraySorter.sort((Object[])arrayOfJavaThing, new Comparer() {
/*     */           public int compare(Object param1Object1, Object param1Object2) {
/*  94 */             JavaThing javaThing1 = (JavaThing)param1Object1;
/*  95 */             JavaThing javaThing2 = (JavaThing)param1Object2;
/*  96 */             int i = javaThing2.getSize() - javaThing1.getSize();
/*  97 */             if (i != 0) {
/*  98 */               return i;
/*     */             }
/* 100 */             return javaThing1.compareTo(javaThing2);
/*     */           }
/*     */         });
/* 103 */     this.reachables = arrayOfJavaThing;
/*     */     
/* 105 */     this.totalSize = paramJavaHeapObject.getSize();
/* 106 */     for (b = 0; b < arrayOfJavaThing.length; b++) {
/* 107 */       this.totalSize += arrayOfJavaThing[b].getSize();
/*     */     }
/*     */     
/* 110 */     this.excludedFields = getElements(hashtable2);
/* 111 */     this.usedFields = getElements(hashtable3);
/*     */   }
/*     */   
/*     */   public JavaHeapObject getRoot() {
/* 115 */     return this.root;
/*     */   }
/*     */   
/*     */   public JavaThing[] getReachables() {
/* 119 */     return this.reachables;
/*     */   }
/*     */   
/*     */   public long getTotalSize() {
/* 123 */     return this.totalSize;
/*     */   }
/*     */   
/*     */   public String[] getExcludedFields() {
/* 127 */     return this.excludedFields;
/*     */   }
/*     */   
/*     */   public String[] getUsedFields() {
/* 131 */     return this.usedFields;
/*     */   }
/*     */   
/*     */   private String[] getElements(Hashtable paramHashtable) {
/* 135 */     Object[] arrayOfObject = paramHashtable.keySet().toArray();
/* 136 */     int i = arrayOfObject.length;
/* 137 */     String[] arrayOfString = new String[i];
/* 138 */     System.arraycopy(arrayOfObject, 0, arrayOfString, 0, i);
/* 139 */     ArraySorter.sortArrayOfStrings((Object[])arrayOfString);
/* 140 */     return arrayOfString;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\model\ReachableObjects.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */