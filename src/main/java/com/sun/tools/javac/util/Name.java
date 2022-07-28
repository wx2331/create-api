/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import javax.lang.model.element.Name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Name
/*     */   implements Name
/*     */ {
/*     */   public final Table table;
/*     */   
/*     */   protected Name(Table paramTable) {
/*  42 */     this.table = paramTable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contentEquals(CharSequence paramCharSequence) {
/*  49 */     return toString().equals(paramCharSequence.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/*  56 */     return toString().length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char charAt(int paramInt) {
/*  63 */     return toString().charAt(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharSequence subSequence(int paramInt1, int paramInt2) {
/*  70 */     return toString().subSequence(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Name append(Name paramName) {
/*  76 */     int i = getByteLength();
/*  77 */     byte[] arrayOfByte = new byte[i + paramName.getByteLength()];
/*  78 */     getBytes(arrayOfByte, 0);
/*  79 */     paramName.getBytes(arrayOfByte, i);
/*  80 */     return this.table.fromUtf(arrayOfByte, 0, arrayOfByte.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Name append(char paramChar, Name paramName) {
/*  87 */     int i = getByteLength();
/*  88 */     byte[] arrayOfByte = new byte[i + 1 + paramName.getByteLength()];
/*  89 */     getBytes(arrayOfByte, 0);
/*  90 */     arrayOfByte[i] = (byte)paramChar;
/*  91 */     paramName.getBytes(arrayOfByte, i + 1);
/*  92 */     return this.table.fromUtf(arrayOfByte, 0, arrayOfByte.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Name paramName) {
/*  98 */     return paramName.getIndex() - getIndex();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 104 */     return (getByteLength() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int lastIndexOf(byte paramByte) {
/* 110 */     byte[] arrayOfByte = getByteArray();
/* 111 */     int i = getByteOffset();
/* 112 */     int j = getByteLength() - 1;
/* 113 */     for (; j >= 0 && arrayOfByte[i + j] != paramByte; j--);
/* 114 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean startsWith(Name paramName) {
/* 120 */     byte[] arrayOfByte1 = getByteArray();
/* 121 */     int i = getByteOffset();
/* 122 */     int j = getByteLength();
/* 123 */     byte[] arrayOfByte2 = paramName.getByteArray();
/* 124 */     int k = paramName.getByteOffset();
/* 125 */     int m = paramName.getByteLength();
/*     */     
/* 127 */     byte b = 0;
/* 128 */     while (b < m && b < j && arrayOfByte1[i + b] == arrayOfByte2[k + b])
/*     */     {
/*     */       
/* 131 */       b++; } 
/* 132 */     return (b == m);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Name subName(int paramInt1, int paramInt2) {
/* 139 */     if (paramInt2 < paramInt1) paramInt2 = paramInt1; 
/* 140 */     return this.table.fromUtf(getByteArray(), getByteOffset() + paramInt1, paramInt2 - paramInt1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 147 */     return Convert.utf2string(getByteArray(), getByteOffset(), getByteLength());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] toUtf() {
/* 153 */     byte[] arrayOfByte = new byte[getByteLength()];
/* 154 */     getBytes(arrayOfByte, 0);
/* 155 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int getIndex();
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int getByteLength();
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract byte getByteAt(int paramInt);
/*     */ 
/*     */ 
/*     */   
/*     */   public void getBytes(byte[] paramArrayOfbyte, int paramInt) {
/* 174 */     System.arraycopy(getByteArray(), getByteOffset(), paramArrayOfbyte, paramInt, getByteLength());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract byte[] getByteArray();
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int getByteOffset();
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class Table
/*     */   {
/*     */     public final Names names;
/*     */ 
/*     */ 
/*     */     
/*     */     Table(Names param1Names) {
/* 194 */       this.names = param1Names;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract Name fromChars(char[] param1ArrayOfchar, int param1Int1, int param1Int2);
/*     */ 
/*     */ 
/*     */     
/*     */     public Name fromString(String param1String) {
/* 204 */       char[] arrayOfChar = param1String.toCharArray();
/* 205 */       return fromChars(arrayOfChar, 0, arrayOfChar.length);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Name fromUtf(byte[] param1ArrayOfbyte) {
/* 212 */       return fromUtf(param1ArrayOfbyte, 0, param1ArrayOfbyte.length);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract Name fromUtf(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2);
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract void dispose();
/*     */ 
/*     */ 
/*     */     
/*     */     protected static int hashValue(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) {
/* 227 */       int i = 0;
/* 228 */       int j = param1Int1;
/*     */       
/* 230 */       for (byte b = 0; b < param1Int2; b++) {
/* 231 */         i = (i << 5) - i + param1ArrayOfbyte[j++];
/*     */       }
/* 233 */       return i;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected static boolean equals(byte[] param1ArrayOfbyte1, int param1Int1, byte[] param1ArrayOfbyte2, int param1Int2, int param1Int3) {
/* 240 */       byte b = 0;
/* 241 */       while (b < param1Int3 && param1ArrayOfbyte1[param1Int1 + b] == param1ArrayOfbyte2[param1Int2 + b]) {
/* 242 */         b++;
/*     */       }
/* 244 */       return (b == param1Int3);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\Name.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */