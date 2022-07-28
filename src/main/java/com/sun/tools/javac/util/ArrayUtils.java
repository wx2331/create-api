/*    */ package com.sun.tools.javac.util;
/*    */ 
/*    */ import java.lang.reflect.Array;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ArrayUtils
/*    */ {
/*    */   private static int calculateNewLength(int paramInt1, int paramInt2) {
/* 38 */     while (paramInt1 < paramInt2 + 1)
/* 39 */       paramInt1 *= 2; 
/* 40 */     return paramInt1;
/*    */   }
/*    */   
/*    */   public static <T> T[] ensureCapacity(T[] paramArrayOfT, int paramInt) {
/* 44 */     if (paramInt < paramArrayOfT.length) {
/* 45 */       return paramArrayOfT;
/*    */     }
/* 47 */     int i = calculateNewLength(paramArrayOfT.length, paramInt);
/*    */     
/* 49 */     Object[] arrayOfObject = (Object[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), i);
/* 50 */     System.arraycopy(paramArrayOfT, 0, arrayOfObject, 0, paramArrayOfT.length);
/* 51 */     return (T[])arrayOfObject;
/*    */   }
/*    */ 
/*    */   
/*    */   public static byte[] ensureCapacity(byte[] paramArrayOfbyte, int paramInt) {
/* 56 */     if (paramInt < paramArrayOfbyte.length) {
/* 57 */       return paramArrayOfbyte;
/*    */     }
/* 59 */     int i = calculateNewLength(paramArrayOfbyte.length, paramInt);
/* 60 */     byte[] arrayOfByte = new byte[i];
/* 61 */     System.arraycopy(paramArrayOfbyte, 0, arrayOfByte, 0, paramArrayOfbyte.length);
/* 62 */     return arrayOfByte;
/*    */   }
/*    */ 
/*    */   
/*    */   public static char[] ensureCapacity(char[] paramArrayOfchar, int paramInt) {
/* 67 */     if (paramInt < paramArrayOfchar.length) {
/* 68 */       return paramArrayOfchar;
/*    */     }
/* 70 */     int i = calculateNewLength(paramArrayOfchar.length, paramInt);
/* 71 */     char[] arrayOfChar = new char[i];
/* 72 */     System.arraycopy(paramArrayOfchar, 0, arrayOfChar, 0, paramArrayOfchar.length);
/* 73 */     return arrayOfChar;
/*    */   }
/*    */ 
/*    */   
/*    */   public static int[] ensureCapacity(int[] paramArrayOfint, int paramInt) {
/* 78 */     if (paramInt < paramArrayOfint.length) {
/* 79 */       return paramArrayOfint;
/*    */     }
/* 81 */     int i = calculateNewLength(paramArrayOfint.length, paramInt);
/* 82 */     int[] arrayOfInt = new int[i];
/* 83 */     System.arraycopy(paramArrayOfint, 0, arrayOfInt, 0, paramArrayOfint.length);
/* 84 */     return arrayOfInt;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\ArrayUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */