/*     */ package com.sun.tools.hat.internal.util;
/*     */ 
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VectorSorter
/*     */ {
/*     */   public static void sort(Vector<Object> paramVector, Comparer paramComparer) {
/*  60 */     quickSort(paramVector, paramComparer, 0, paramVector.size() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortVectorOfStrings(Vector<Object> paramVector) {
/*  68 */     sort(paramVector, new Comparer() {
/*     */           public int compare(Object param1Object1, Object param1Object2) {
/*  70 */             return ((String)param1Object1).compareTo((String)param1Object2);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static void swap(Vector<Object> paramVector, int paramInt1, int paramInt2) {
/*  77 */     Object object = paramVector.elementAt(paramInt1);
/*  78 */     paramVector.setElementAt(paramVector.elementAt(paramInt2), paramInt1);
/*  79 */     paramVector.setElementAt(object, paramInt2);
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
/*     */   private static void quickSort(Vector<Object> paramVector, Comparer paramComparer, int paramInt1, int paramInt2) {
/*  91 */     if (paramInt2 <= paramInt1)
/*     */       return; 
/*  93 */     int i = (paramInt1 + paramInt2) / 2;
/*  94 */     if (i != paramInt1)
/*  95 */       swap(paramVector, i, paramInt1); 
/*  96 */     Object object = paramVector.elementAt(paramInt1);
/*     */     
/*  98 */     int j = paramInt1 - 1;
/*  99 */     int k = paramInt1 + 1;
/* 100 */     int m = paramInt2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     while (k <= m) {
/* 108 */       int i1, n = paramComparer.compare(paramVector.elementAt(k), object);
/* 109 */       if (n <= 0) {
/* 110 */         if (n < 0) {
/* 111 */           j = k;
/*     */         }
/* 113 */         k++;
/*     */         continue;
/*     */       } 
/*     */       while (true) {
/* 117 */         i1 = paramComparer.compare(paramVector.elementAt(m), object);
/*     */         
/* 119 */         if (i1 > 0) {
/* 120 */           m--;
/* 121 */           if (k > m) {
/*     */             break;
/*     */           }
/*     */           
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/* 129 */       if (k <= m) {
/* 130 */         swap(paramVector, k, m);
/* 131 */         if (i1 < 0) {
/* 132 */           j = k;
/*     */         }
/* 134 */         k++;
/* 135 */         m--;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 141 */     if (j > paramInt1) {
/*     */       
/* 143 */       swap(paramVector, paramInt1, j);
/* 144 */       quickSort(paramVector, paramComparer, paramInt1, j - 1);
/*     */     } 
/* 146 */     quickSort(paramVector, paramComparer, m + 1, paramInt2);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\interna\\util\VectorSorter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */