/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.TypeTag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Constants
/*     */ {
/*     */   public static Object decode(Object paramObject, Type paramType) {
/*  47 */     if (paramObject instanceof Integer) {
/*  48 */       int i = ((Integer)paramObject).intValue();
/*  49 */       switch (paramType.getTag()) { case BOOLEAN:
/*  50 */           return Boolean.valueOf((i != 0));
/*  51 */         case CHAR: return Character.valueOf((char)i);
/*  52 */         case BYTE: return Byte.valueOf((byte)i);
/*  53 */         case SHORT: return Short.valueOf((short)i); }
/*     */     
/*     */     } 
/*  56 */     return paramObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String format(Object paramObject, Type paramType) {
/*  64 */     paramObject = decode(paramObject, paramType);
/*  65 */     switch (paramType.getTag()) { case BYTE:
/*  66 */         return formatByte(((Byte)paramObject).byteValue());
/*  67 */       case LONG: return formatLong(((Long)paramObject).longValue());
/*  68 */       case FLOAT: return formatFloat(((Float)paramObject).floatValue());
/*  69 */       case DOUBLE: return formatDouble(((Double)paramObject).doubleValue());
/*  70 */       case CHAR: return formatChar(((Character)paramObject).charValue()); }
/*     */     
/*  72 */     if (paramObject instanceof String)
/*  73 */       return formatString((String)paramObject); 
/*  74 */     return paramObject + "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String format(Object paramObject) {
/*  83 */     if (paramObject instanceof Byte) return formatByte(((Byte)paramObject).byteValue()); 
/*  84 */     if (paramObject instanceof Short) return formatShort(((Short)paramObject).shortValue()); 
/*  85 */     if (paramObject instanceof Long) return formatLong(((Long)paramObject).longValue()); 
/*  86 */     if (paramObject instanceof Float) return formatFloat(((Float)paramObject).floatValue()); 
/*  87 */     if (paramObject instanceof Double) return formatDouble(((Double)paramObject).doubleValue()); 
/*  88 */     if (paramObject instanceof Character) return formatChar(((Character)paramObject).charValue()); 
/*  89 */     if (paramObject instanceof String) return formatString((String)paramObject); 
/*  90 */     if (paramObject instanceof Integer || paramObject instanceof Boolean) {
/*  91 */       return paramObject.toString();
/*     */     }
/*  93 */     throw new IllegalArgumentException("Argument is not a primitive type or a string; it " + ((paramObject == null) ? "is a null value." : ("has class " + paramObject
/*     */ 
/*     */ 
/*     */         
/*  97 */         .getClass().getName())) + ".");
/*     */   }
/*     */   
/*     */   private static String formatByte(byte paramByte) {
/* 101 */     return String.format("(byte)0x%02x", new Object[] { Byte.valueOf(paramByte) });
/*     */   }
/*     */   
/*     */   private static String formatShort(short paramShort) {
/* 105 */     return String.format("(short)%d", new Object[] { Short.valueOf(paramShort) });
/*     */   }
/*     */   
/*     */   private static String formatLong(long paramLong) {
/* 109 */     return paramLong + "L";
/*     */   }
/*     */   
/*     */   private static String formatFloat(float paramFloat) {
/* 113 */     if (Float.isNaN(paramFloat))
/* 114 */       return "0.0f/0.0f"; 
/* 115 */     if (Float.isInfinite(paramFloat)) {
/* 116 */       return (paramFloat < 0.0F) ? "-1.0f/0.0f" : "1.0f/0.0f";
/*     */     }
/* 118 */     return paramFloat + "f";
/*     */   }
/*     */   
/*     */   private static String formatDouble(double paramDouble) {
/* 122 */     if (Double.isNaN(paramDouble))
/* 123 */       return "0.0/0.0"; 
/* 124 */     if (Double.isInfinite(paramDouble)) {
/* 125 */       return (paramDouble < 0.0D) ? "-1.0/0.0" : "1.0/0.0";
/*     */     }
/* 127 */     return paramDouble + "";
/*     */   }
/*     */   
/*     */   private static String formatChar(char paramChar) {
/* 131 */     return '\'' + Convert.quote(paramChar) + '\'';
/*     */   }
/*     */   
/*     */   private static String formatString(String paramString) {
/* 135 */     return '"' + Convert.quote(paramString) + '"';
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\Constants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */