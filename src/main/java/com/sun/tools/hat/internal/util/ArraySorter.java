/*     */ package com.sun.tools.hat.internal.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArraySorter
/*     */ {
/*     */   public static void sort(Object[] paramArrayOfObject, Comparer paramComparer) {
/*  59 */     quickSort(paramArrayOfObject, paramComparer, 0, paramArrayOfObject.length - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortArrayOfStrings(Object[] paramArrayOfObject) {
/*  67 */     sort(paramArrayOfObject, new Comparer() {
/*     */           public int compare(Object param1Object1, Object param1Object2) {
/*  69 */             return ((String)param1Object1).compareTo((String)param1Object2);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static void swap(Object[] paramArrayOfObject, int paramInt1, int paramInt2) {
/*  76 */     Object object = paramArrayOfObject[paramInt1];
/*  77 */     paramArrayOfObject[paramInt1] = paramArrayOfObject[paramInt2];
/*  78 */     paramArrayOfObject[paramInt2] = object;
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
/*     */   private static void quickSort(Object[] paramArrayOfObject, Comparer paramComparer, int paramInt1, int paramInt2) {
/*  90 */     if (paramInt2 <= paramInt1)
/*     */       return; 
/*  92 */     int i = (paramInt1 + paramInt2) / 2;
/*  93 */     if (i != paramInt1)
/*  94 */       swap(paramArrayOfObject, i, paramInt1); 
/*  95 */     Object object = paramArrayOfObject[paramInt1];
/*  96 */     int j = paramInt1 - 1;
/*  97 */     int k = paramInt1 + 1;
/*  98 */     int m = paramInt2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     while (k <= m) {
/* 106 */       int i1, n = paramComparer.compare(paramArrayOfObject[k], object);
/* 107 */       if (n <= 0) {
/* 108 */         if (n < 0) {
/* 109 */           j = k;
/*     */         }
/* 111 */         k++;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       while (true) {
/* 116 */         i1 = paramComparer.compare(paramArrayOfObject[m], object);
/* 117 */         if (i1 > 0) {
/* 118 */           m--;
/* 119 */           if (k > m) {
/*     */             break;
/*     */           }
/*     */           
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/* 127 */       if (k <= m) {
/* 128 */         swap(paramArrayOfObject, k, m);
/* 129 */         if (i1 < 0) {
/* 130 */           j = k;
/*     */         }
/* 132 */         k++;
/* 133 */         m--;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     if (j > paramInt1) {
/*     */       
/* 142 */       swap(paramArrayOfObject, paramInt1, j);
/* 143 */       quickSort(paramArrayOfObject, paramComparer, paramInt1, j - 1);
/*     */     } 
/* 145 */     quickSort(paramArrayOfObject, paramComparer, m + 1, paramInt2);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\interna\\util\ArraySorter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */