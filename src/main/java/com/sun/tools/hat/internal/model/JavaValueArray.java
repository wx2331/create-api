/*     */ package com.sun.tools.hat.internal.model;
/*     */ 
/*     */ import com.sun.tools.hat.internal.parser.ReadBuffer;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaValueArray
/*     */   extends JavaLazyReadObject
/*     */   implements ArrayTypeCodes
/*     */ {
/*     */   private JavaClass clazz;
/*     */   private int data;
/*     */   private static final int SIGNATURE_MASK = 255;
/*     */   private static final int LENGTH_DIVIDER_MASK = 65280;
/*     */   private static final int LENGTH_DIVIDER_SHIFT = 8;
/*     */   
/*     */   private static String arrayTypeName(byte paramByte) {
/*  47 */     switch (paramByte) {
/*     */       case 66:
/*  49 */         return "byte[]";
/*     */       case 90:
/*  51 */         return "boolean[]";
/*     */       case 67:
/*  53 */         return "char[]";
/*     */       case 83:
/*  55 */         return "short[]";
/*     */       case 73:
/*  57 */         return "int[]";
/*     */       case 70:
/*  59 */         return "float[]";
/*     */       case 74:
/*  61 */         return "long[]";
/*     */       case 68:
/*  63 */         return "double[]";
/*     */     } 
/*  65 */     throw new RuntimeException("invalid array element sig: " + paramByte);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int elementSize(byte paramByte) {
/*  70 */     switch (paramByte) {
/*     */       case 4:
/*     */       case 8:
/*  73 */         return 1;
/*     */       case 5:
/*     */       case 9:
/*  76 */         return 2;
/*     */       case 6:
/*     */       case 10:
/*  79 */         return 4;
/*     */       case 7:
/*     */       case 11:
/*  82 */         return 8;
/*     */     } 
/*  84 */     throw new RuntimeException("invalid array element type: " + paramByte);
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
/*     */   protected final int readValueLength() throws IOException {
/*  99 */     JavaClass javaClass = getClazz();
/* 100 */     ReadBuffer readBuffer = javaClass.getReadBuffer();
/* 101 */     int i = javaClass.getIdentifierSize();
/* 102 */     long l = getOffset() + i + 4L;
/*     */     
/* 104 */     int j = readBuffer.getInt(l);
/*     */     
/* 106 */     byte b = readBuffer.getByte(l + 4L);
/* 107 */     return j * elementSize(b);
/*     */   }
/*     */   
/*     */   protected final byte[] readValue() throws IOException {
/* 111 */     JavaClass javaClass = getClazz();
/* 112 */     ReadBuffer readBuffer = javaClass.getReadBuffer();
/* 113 */     int i = javaClass.getIdentifierSize();
/* 114 */     long l = getOffset() + i + 4L;
/*     */     
/* 116 */     int j = readBuffer.getInt(l);
/*     */     
/* 118 */     byte b = readBuffer.getByte(l + 4L);
/* 119 */     if (j == 0) {
/* 120 */       return Snapshot.EMPTY_BYTE_ARRAY;
/*     */     }
/* 122 */     j *= elementSize(b);
/* 123 */     byte[] arrayOfByte = new byte[j];
/* 124 */     readBuffer.get(l + 5L, arrayOfByte);
/* 125 */     return arrayOfByte;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaValueArray(byte paramByte, long paramLong) {
/* 148 */     super(paramLong);
/* 149 */     this.data = paramByte & 0xFF;
/*     */   }
/*     */   
/*     */   public JavaClass getClazz() {
/* 153 */     return this.clazz;
/*     */   }
/*     */   
/*     */   public void visitReferencedObjects(JavaHeapObjectVisitor paramJavaHeapObjectVisitor) {
/* 157 */     super.visitReferencedObjects(paramJavaHeapObjectVisitor);
/*     */   }
/*     */   
/*     */   public void resolve(Snapshot paramSnapshot) {
/* 161 */     if (this.clazz instanceof JavaClass) {
/*     */       return;
/*     */     }
/* 164 */     byte b = getElementType();
/* 165 */     this.clazz = paramSnapshot.findClass(arrayTypeName(b));
/* 166 */     if (this.clazz == null) {
/* 167 */       this.clazz = paramSnapshot.getArrayClass("" + (char)b);
/*     */     }
/* 169 */     getClazz().addInstance(this);
/* 170 */     super.resolve(paramSnapshot);
/*     */   }
/*     */   
/*     */   public int getLength() {
/* 174 */     int i = (this.data & 0xFF00) >>> 8;
/* 175 */     if (i == 0) {
/* 176 */       byte b = getElementType();
/* 177 */       switch (b) {
/*     */         case 66:
/*     */         case 90:
/* 180 */           i = 1;
/*     */           break;
/*     */         case 67:
/*     */         case 83:
/* 184 */           i = 2;
/*     */           break;
/*     */         case 70:
/*     */         case 73:
/* 188 */           i = 4;
/*     */           break;
/*     */         case 68:
/*     */         case 74:
/* 192 */           i = 8;
/*     */           break;
/*     */         default:
/* 195 */           throw new RuntimeException("unknown primitive type: " + b);
/*     */       } 
/*     */       
/* 198 */       this.data |= i << 8;
/*     */     } 
/* 200 */     return getValueLength() / i; } public Object getElements() { boolean[] arrayOfBoolean; byte[] arrayOfByte2; char[] arrayOfChar; short[] arrayOfShort; int[] arrayOfInt; long[] arrayOfLong;
/*     */     float[] arrayOfFloat;
/*     */     double[] arrayOfDouble;
/*     */     byte b2;
/* 204 */     int i = getLength();
/* 205 */     byte b = getElementType();
/* 206 */     byte[] arrayOfByte1 = getValue();
/* 207 */     byte b1 = 0;
/* 208 */     switch (b) {
/*     */       case 90:
/* 210 */         arrayOfBoolean = new boolean[i];
/* 211 */         for (b2 = 0; b2 < i; b2++) {
/* 212 */           arrayOfBoolean[b2] = booleanAt(b1, arrayOfByte1);
/* 213 */           b1++;
/*     */         } 
/* 215 */         return arrayOfBoolean;
/*     */       
/*     */       case 66:
/* 218 */         arrayOfByte2 = new byte[i];
/* 219 */         for (b2 = 0; b2 < i; b2++) {
/* 220 */           arrayOfByte2[b2] = byteAt(b1, arrayOfByte1);
/* 221 */           b1++;
/*     */         } 
/* 223 */         return arrayOfByte2;
/*     */       
/*     */       case 67:
/* 226 */         arrayOfChar = new char[i];
/* 227 */         for (b2 = 0; b2 < i; b2++) {
/* 228 */           arrayOfChar[b2] = charAt(b1, arrayOfByte1);
/* 229 */           b1 += 2;
/*     */         } 
/* 231 */         return arrayOfChar;
/*     */       
/*     */       case 83:
/* 234 */         arrayOfShort = new short[i];
/* 235 */         for (b2 = 0; b2 < i; b2++) {
/* 236 */           arrayOfShort[b2] = shortAt(b1, arrayOfByte1);
/* 237 */           b1 += 2;
/*     */         } 
/* 239 */         return arrayOfShort;
/*     */       
/*     */       case 73:
/* 242 */         arrayOfInt = new int[i];
/* 243 */         for (b2 = 0; b2 < i; b2++) {
/* 244 */           arrayOfInt[b2] = intAt(b1, arrayOfByte1);
/* 245 */           b1 += 4;
/*     */         } 
/* 247 */         return arrayOfInt;
/*     */       
/*     */       case 74:
/* 250 */         arrayOfLong = new long[i];
/* 251 */         for (b2 = 0; b2 < i; b2++) {
/* 252 */           arrayOfLong[b2] = longAt(b1, arrayOfByte1);
/* 253 */           b1 += 8;
/*     */         } 
/* 255 */         return arrayOfLong;
/*     */       
/*     */       case 70:
/* 258 */         arrayOfFloat = new float[i];
/* 259 */         for (b2 = 0; b2 < i; b2++) {
/* 260 */           arrayOfFloat[b2] = floatAt(b1, arrayOfByte1);
/* 261 */           b1 += 4;
/*     */         } 
/* 263 */         return arrayOfFloat;
/*     */       
/*     */       case 68:
/* 266 */         arrayOfDouble = new double[i];
/* 267 */         for (b2 = 0; b2 < i; b2++) {
/* 268 */           arrayOfDouble[b2] = doubleAt(b1, arrayOfByte1);
/* 269 */           b1 += 8;
/*     */         } 
/* 271 */         return arrayOfDouble;
/*     */     } 
/*     */     
/* 274 */     throw new RuntimeException("unknown primitive type?"); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getElementType() {
/* 280 */     return (byte)(this.data & 0xFF);
/*     */   }
/*     */   
/*     */   private void checkIndex(int paramInt) {
/* 284 */     if (paramInt < 0 || paramInt >= getLength()) {
/* 285 */       throw new ArrayIndexOutOfBoundsException(paramInt);
/*     */     }
/*     */   }
/*     */   
/*     */   private void requireType(char paramChar) {
/* 290 */     if (getElementType() != paramChar) {
/* 291 */       throw new RuntimeException("not of type : " + paramChar);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean getBooleanAt(int paramInt) {
/* 296 */     checkIndex(paramInt);
/* 297 */     requireType('Z');
/* 298 */     return booleanAt(paramInt, getValue());
/*     */   }
/*     */   
/*     */   public byte getByteAt(int paramInt) {
/* 302 */     checkIndex(paramInt);
/* 303 */     requireType('B');
/* 304 */     return byteAt(paramInt, getValue());
/*     */   }
/*     */   
/*     */   public char getCharAt(int paramInt) {
/* 308 */     checkIndex(paramInt);
/* 309 */     requireType('C');
/* 310 */     return charAt(paramInt << 1, getValue());
/*     */   }
/*     */   
/*     */   public short getShortAt(int paramInt) {
/* 314 */     checkIndex(paramInt);
/* 315 */     requireType('S');
/* 316 */     return shortAt(paramInt << 1, getValue());
/*     */   }
/*     */   
/*     */   public int getIntAt(int paramInt) {
/* 320 */     checkIndex(paramInt);
/* 321 */     requireType('I');
/* 322 */     return intAt(paramInt << 2, getValue());
/*     */   }
/*     */   
/*     */   public long getLongAt(int paramInt) {
/* 326 */     checkIndex(paramInt);
/* 327 */     requireType('J');
/* 328 */     return longAt(paramInt << 3, getValue());
/*     */   }
/*     */   
/*     */   public float getFloatAt(int paramInt) {
/* 332 */     checkIndex(paramInt);
/* 333 */     requireType('F');
/* 334 */     return floatAt(paramInt << 2, getValue());
/*     */   }
/*     */   
/*     */   public double getDoubleAt(int paramInt) {
/* 338 */     checkIndex(paramInt);
/* 339 */     requireType('D');
/* 340 */     return doubleAt(paramInt << 3, getValue());
/*     */   }
/*     */   
/*     */   public String valueString() {
/* 344 */     return valueString(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public String valueString(boolean paramBoolean) {
/*     */     StringBuffer stringBuffer;
/* 350 */     byte[] arrayOfByte = getValue();
/* 351 */     int i = arrayOfByte.length;
/* 352 */     byte b = getElementType();
/* 353 */     if (b == 67) {
/* 354 */       stringBuffer = new StringBuffer();
/* 355 */       for (byte b1 = 0; b1 < arrayOfByte.length; ) {
/* 356 */         char c = charAt(b1, arrayOfByte);
/* 357 */         stringBuffer.append(c);
/* 358 */         b1 += 2;
/*     */       } 
/*     */     } else {
/* 361 */       char c = '\b';
/* 362 */       if (paramBoolean) {
/* 363 */         c = 'Ϩ';
/*     */       }
/* 365 */       stringBuffer = new StringBuffer("{");
/* 366 */       byte b1 = 0;
/* 367 */       for (byte b2 = 0; b2 < arrayOfByte.length; ) {
/* 368 */         boolean bool; int j; long l; float f; double d; if (b1) {
/* 369 */           stringBuffer.append(", ");
/*     */         }
/* 371 */         if (b1 >= c) {
/* 372 */           stringBuffer.append("... ");
/*     */           break;
/*     */         } 
/* 375 */         b1++;
/* 376 */         switch (b) {
/*     */           case 90:
/* 378 */             bool = booleanAt(b2, arrayOfByte);
/* 379 */             if (bool) {
/* 380 */               stringBuffer.append("true");
/*     */             } else {
/* 382 */               stringBuffer.append("false");
/*     */             } 
/* 384 */             b2++;
/*     */             continue;
/*     */           
/*     */           case 66:
/* 388 */             j = 0xFF & byteAt(b2, arrayOfByte);
/* 389 */             stringBuffer.append("0x" + Integer.toString(j, 16));
/* 390 */             b2++;
/*     */             continue;
/*     */           
/*     */           case 83:
/* 394 */             j = shortAt(b2, arrayOfByte);
/* 395 */             b2 += 2;
/* 396 */             stringBuffer.append("" + j);
/*     */             continue;
/*     */           
/*     */           case 73:
/* 400 */             j = intAt(b2, arrayOfByte);
/* 401 */             b2 += 4;
/* 402 */             stringBuffer.append("" + j);
/*     */             continue;
/*     */           
/*     */           case 74:
/* 406 */             l = longAt(b2, arrayOfByte);
/* 407 */             stringBuffer.append("" + l);
/* 408 */             b2 += 8;
/*     */             continue;
/*     */           
/*     */           case 70:
/* 412 */             f = floatAt(b2, arrayOfByte);
/* 413 */             stringBuffer.append("" + f);
/* 414 */             b2 += 4;
/*     */             continue;
/*     */           
/*     */           case 68:
/* 418 */             d = doubleAt(b2, arrayOfByte);
/* 419 */             stringBuffer.append("" + d);
/* 420 */             b2 += 8;
/*     */             continue;
/*     */         } 
/*     */         
/* 424 */         throw new RuntimeException("unknown primitive type?");
/*     */       } 
/*     */ 
/*     */       
/* 428 */       stringBuffer.append("}");
/*     */     } 
/* 430 */     return stringBuffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\model\JavaValueArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */