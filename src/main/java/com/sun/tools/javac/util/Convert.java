/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Convert
/*     */ {
/*     */   public static int string2int(String paramString, int paramInt) throws NumberFormatException {
/*  60 */     if (paramInt == 10) {
/*  61 */       return Integer.parseInt(paramString, paramInt);
/*     */     }
/*  63 */     char[] arrayOfChar = paramString.toCharArray();
/*  64 */     int i = Integer.MAX_VALUE / paramInt / 2;
/*  65 */     int j = 0;
/*  66 */     for (byte b = 0; b < arrayOfChar.length; b++) {
/*  67 */       int k = Character.digit(arrayOfChar[b], paramInt);
/*  68 */       if (!j || j > i || j * paramInt > Integer.MAX_VALUE - k)
/*     */       {
/*     */         
/*  71 */         throw new NumberFormatException(); } 
/*  72 */       j = j * paramInt + k;
/*     */     } 
/*  74 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long string2long(String paramString, int paramInt) throws NumberFormatException {
/*  82 */     if (paramInt == 10) {
/*  83 */       return Long.parseLong(paramString, paramInt);
/*     */     }
/*  85 */     char[] arrayOfChar = paramString.toCharArray();
/*  86 */     long l1 = Long.MAX_VALUE / (paramInt / 2);
/*  87 */     long l2 = 0L;
/*  88 */     for (byte b = 0; b < arrayOfChar.length; b++) {
/*  89 */       int i = Character.digit(arrayOfChar[b], paramInt);
/*  90 */       if (l2 < 0L || l2 > l1 || l2 * paramInt > Long.MAX_VALUE - i)
/*     */       {
/*     */         
/*  93 */         throw new NumberFormatException(); } 
/*  94 */       l2 = l2 * paramInt + i;
/*     */     } 
/*  96 */     return l2;
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
/*     */ 
/*     */   
/*     */   public static int utf2chars(byte[] paramArrayOfbyte, int paramInt1, char[] paramArrayOfchar, int paramInt2, int paramInt3) {
/* 116 */     int i = paramInt1;
/* 117 */     int j = paramInt2;
/* 118 */     int k = paramInt1 + paramInt3;
/* 119 */     while (i < k) {
/* 120 */       int m = paramArrayOfbyte[i++] & 0xFF;
/* 121 */       if (m >= 224) {
/* 122 */         m = (m & 0xF) << 12;
/* 123 */         m |= (paramArrayOfbyte[i++] & 0x3F) << 6;
/* 124 */         m |= paramArrayOfbyte[i++] & 0x3F;
/* 125 */       } else if (m >= 192) {
/* 126 */         m = (m & 0x1F) << 6;
/* 127 */         m |= paramArrayOfbyte[i++] & 0x3F;
/*     */       } 
/* 129 */       paramArrayOfchar[j++] = (char)m;
/*     */     } 
/* 131 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char[] utf2chars(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 140 */     char[] arrayOfChar1 = new char[paramInt2];
/* 141 */     int i = utf2chars(paramArrayOfbyte, paramInt1, arrayOfChar1, 0, paramInt2);
/* 142 */     char[] arrayOfChar2 = new char[i];
/* 143 */     System.arraycopy(arrayOfChar1, 0, arrayOfChar2, 0, i);
/* 144 */     return arrayOfChar2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char[] utf2chars(byte[] paramArrayOfbyte) {
/* 152 */     return utf2chars(paramArrayOfbyte, 0, paramArrayOfbyte.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String utf2string(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 161 */     char[] arrayOfChar = new char[paramInt2];
/* 162 */     int i = utf2chars(paramArrayOfbyte, paramInt1, arrayOfChar, 0, paramInt2);
/* 163 */     return new String(arrayOfChar, 0, i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String utf2string(byte[] paramArrayOfbyte) {
/* 171 */     return utf2string(paramArrayOfbyte, 0, paramArrayOfbyte.length);
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
/*     */   public static int chars2utf(char[] paramArrayOfchar, int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3) {
/* 188 */     int i = paramInt2;
/* 189 */     int j = paramInt1 + paramInt3;
/* 190 */     for (int k = paramInt1; k < j; k++) {
/* 191 */       char c = paramArrayOfchar[k];
/* 192 */       if ('\001' <= c && c <= '') {
/* 193 */         paramArrayOfbyte[i++] = (byte)c;
/* 194 */       } else if (c <= '߿') {
/* 195 */         paramArrayOfbyte[i++] = (byte)(0xC0 | c >> 6);
/* 196 */         paramArrayOfbyte[i++] = (byte)(0x80 | c & 0x3F);
/*     */       } else {
/* 198 */         paramArrayOfbyte[i++] = (byte)(0xE0 | c >> 12);
/* 199 */         paramArrayOfbyte[i++] = (byte)(0x80 | c >> 6 & 0x3F);
/* 200 */         paramArrayOfbyte[i++] = (byte)(0x80 | c & 0x3F);
/*     */       } 
/*     */     } 
/* 203 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] chars2utf(char[] paramArrayOfchar, int paramInt1, int paramInt2) {
/* 212 */     byte[] arrayOfByte1 = new byte[paramInt2 * 3];
/* 213 */     int i = chars2utf(paramArrayOfchar, paramInt1, arrayOfByte1, 0, paramInt2);
/* 214 */     byte[] arrayOfByte2 = new byte[i];
/* 215 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, i);
/* 216 */     return arrayOfByte2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] chars2utf(char[] paramArrayOfchar) {
/* 224 */     return chars2utf(paramArrayOfchar, 0, paramArrayOfchar.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] string2utf(String paramString) {
/* 230 */     return chars2utf(paramString.toCharArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String quote(String paramString) {
/* 238 */     StringBuilder stringBuilder = new StringBuilder();
/* 239 */     for (byte b = 0; b < paramString.length(); b++) {
/* 240 */       stringBuilder.append(quote(paramString.charAt(b)));
/*     */     }
/* 242 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String quote(char paramChar) {
/* 250 */     switch (paramChar) { case '\b':
/* 251 */         return "\\b";
/* 252 */       case '\f': return "\\f";
/* 253 */       case '\n': return "\\n";
/* 254 */       case '\r': return "\\r";
/* 255 */       case '\t': return "\\t";
/* 256 */       case '\'': return "\\'";
/* 257 */       case '"': return "\\\"";
/* 258 */       case '\\': return "\\\\"; }
/*     */     
/* 260 */     return isPrintableAscii(paramChar) ? 
/* 261 */       String.valueOf(paramChar) : 
/* 262 */       String.format("\\u%04x", new Object[] { Integer.valueOf(paramChar) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isPrintableAscii(char paramChar) {
/* 270 */     return (paramChar >= ' ' && paramChar <= '~');
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String escapeUnicode(String paramString) {
/* 276 */     int i = paramString.length();
/* 277 */     byte b = 0;
/* 278 */     while (b < i) {
/* 279 */       char c = paramString.charAt(b);
/* 280 */       if (c > 'ÿ') {
/* 281 */         StringBuilder stringBuilder = new StringBuilder();
/* 282 */         stringBuilder.append(paramString.substring(0, b));
/* 283 */         while (b < i) {
/* 284 */           c = paramString.charAt(b);
/* 285 */           if (c > 'ÿ') {
/* 286 */             stringBuilder.append("\\u");
/* 287 */             stringBuilder.append(Character.forDigit((c >> 12) % 16, 16));
/* 288 */             stringBuilder.append(Character.forDigit((c >> 8) % 16, 16));
/* 289 */             stringBuilder.append(Character.forDigit((c >> 4) % 16, 16));
/* 290 */             stringBuilder.append(Character.forDigit(c % 16, 16));
/*     */           } else {
/* 292 */             stringBuilder.append(c);
/*     */           } 
/* 294 */           b++;
/*     */         } 
/* 296 */         paramString = stringBuilder.toString(); continue;
/*     */       } 
/* 298 */       b++;
/*     */     } 
/*     */     
/* 301 */     return paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Name shortName(Name paramName) {
/* 309 */     return paramName.subName(paramName
/* 310 */         .lastIndexOf((byte)46) + 1, paramName.getByteLength());
/*     */   }
/*     */   
/*     */   public static String shortName(String paramString) {
/* 314 */     return paramString.substring(paramString.lastIndexOf('.') + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Name packagePart(Name paramName) {
/* 321 */     return paramName.subName(0, paramName.lastIndexOf((byte)46));
/*     */   }
/*     */   
/*     */   public static String packagePart(String paramString) {
/* 325 */     int i = paramString.lastIndexOf('.');
/* 326 */     return (i < 0) ? "" : paramString.substring(0, i);
/*     */   }
/*     */   
/*     */   public static List<Name> enclosingCandidates(Name paramName) {
/* 330 */     List<?> list = List.nil();
/*     */     int i;
/* 332 */     while ((i = paramName.lastIndexOf((byte)36)) > 0) {
/* 333 */       paramName = paramName.subName(0, i);
/* 334 */       list = list.prepend(paramName);
/*     */     } 
/* 336 */     return (List)list;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\Convert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */