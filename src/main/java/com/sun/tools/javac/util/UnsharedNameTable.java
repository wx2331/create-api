/*     */ package com.sun.tools.javac.util;
/*     */
/*     */ import java.lang.ref.WeakReference;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class UnsharedNameTable
/*     */   extends Name.Table
/*     */ {
/*     */   public static Name.Table create(Names paramNames) {
/*  42 */     return new UnsharedNameTable(paramNames);
/*     */   }
/*     */
/*     */   static class HashEntry
/*     */     extends WeakReference<NameImpl> {
/*     */     HashEntry(NameImpl param1NameImpl) {
/*  48 */       super(param1NameImpl);
/*     */     }
/*     */
/*     */     HashEntry next;
/*     */   }
/*     */
/*  54 */   private HashEntry[] hashes = null;
/*     */
/*     */
/*     */
/*     */
/*     */   private int hashMask;
/*     */
/*     */
/*     */
/*     */
/*     */   public int index;
/*     */
/*     */
/*     */
/*     */
/*     */   public UnsharedNameTable(Names paramNames, int paramInt) {
/*  70 */     super(paramNames);
/*  71 */     this.hashMask = paramInt - 1;
/*  72 */     this.hashes = new HashEntry[paramInt];
/*     */   }
/*     */
/*     */   public UnsharedNameTable(Names paramNames) {
/*  76 */     this(paramNames, 32768);
/*     */   }
/*     */
/*     */
/*     */
/*     */   public Name fromChars(char[] paramArrayOfchar, int paramInt1, int paramInt2) {
/*  82 */     byte[] arrayOfByte = new byte[paramInt2 * 3];
/*  83 */     int i = Convert.chars2utf(paramArrayOfchar, paramInt1, arrayOfByte, 0, paramInt2);
/*  84 */     return fromUtf(arrayOfByte, 0, i);
/*     */   }
/*     */
/*     */
/*     */   public Name fromUtf(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/*  89 */     int i = hashValue(paramArrayOfbyte, paramInt1, paramInt2) & this.hashMask;
/*     */
/*  91 */     HashEntry hashEntry1 = this.hashes[i];
/*     */
/*  93 */     NameImpl nameImpl = null;
/*     */
/*  95 */     HashEntry hashEntry2 = null;
/*  96 */     HashEntry hashEntry3 = hashEntry1;
/*     */
/*  98 */     while (hashEntry1 != null &&
/*  99 */       hashEntry1 != null) {
/*     */
/*     */
/*     */
/* 103 */       nameImpl = hashEntry1.get();
/*     */
/* 105 */       if (nameImpl == null) {
/* 106 */         if (hashEntry3 == hashEntry1) {
/* 107 */           this.hashes[i] = hashEntry3 = hashEntry1.next;
/*     */         } else {
/*     */
/* 110 */           Assert.checkNonNull(hashEntry2, "previousNonNullTableEntry cannot be null here.");
/* 111 */           hashEntry2.next = hashEntry1.next;
/*     */         }
/*     */       } else {
/*     */
/* 115 */         if (nameImpl.getByteLength() == paramInt2 && equals(nameImpl.bytes, 0, paramArrayOfbyte, paramInt1, paramInt2)) {
/* 116 */           return nameImpl;
/*     */         }
/* 118 */         hashEntry2 = hashEntry1;
/*     */       }
/*     */
/* 121 */       hashEntry1 = hashEntry1.next;
/*     */     }
/*     */
/* 124 */     byte[] arrayOfByte = new byte[paramInt2];
/* 125 */     System.arraycopy(paramArrayOfbyte, paramInt1, arrayOfByte, 0, paramInt2);
/* 126 */     nameImpl = new NameImpl(this, arrayOfByte, this.index++);
/*     */
/* 128 */     HashEntry hashEntry4 = new HashEntry(nameImpl);
/*     */
/* 130 */     if (hashEntry2 == null) {
/* 131 */       this.hashes[i] = hashEntry4;
/*     */     } else {
/*     */
/* 134 */       Assert.checkNull(hashEntry2.next, "previousNonNullTableEntry.next must be null.");
/* 135 */       hashEntry2.next = hashEntry4;
/*     */     }
/*     */
/* 138 */     return nameImpl;
/*     */   }
/*     */
/*     */
/*     */   public void dispose() {
/* 143 */     this.hashes = null;
/*     */   }
/*     */
/*     */   static class NameImpl extends Name {
/*     */     NameImpl(UnsharedNameTable param1UnsharedNameTable, byte[] param1ArrayOfbyte, int param1Int) {
/* 148 */       super(param1UnsharedNameTable);
/* 149 */       this.bytes = param1ArrayOfbyte;
/* 150 */       this.index = param1Int;
/*     */     }
/*     */
/*     */
/*     */     final byte[] bytes;
/*     */     final int index;
/*     */
/*     */     public int getIndex() {
/* 158 */       return this.index;
/*     */     }
/*     */
/*     */
/*     */     public int getByteLength() {
/* 163 */       return this.bytes.length;
/*     */     }
/*     */
/*     */
/*     */     public byte getByteAt(int param1Int) {
/* 168 */       return this.bytes[param1Int];
/*     */     }
/*     */
/*     */
/*     */     public byte[] getByteArray() {
/* 173 */       return this.bytes;
/*     */     }
/*     */
/*     */
/*     */     public int getByteOffset() {
/* 178 */       return 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\UnsharedNameTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
