/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import java.lang.ref.SoftReference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SharedNameTable
/*     */   extends Name.Table
/*     */ {
/*  42 */   private static List<SoftReference<SharedNameTable>> freelist = List.nil(); private NameImpl[] hashes;
/*     */   
/*     */   public static synchronized SharedNameTable create(Names paramNames) {
/*  45 */     while (freelist.nonEmpty()) {
/*  46 */       SharedNameTable sharedNameTable = ((SoftReference<SharedNameTable>)freelist.head).get();
/*  47 */       freelist = freelist.tail;
/*  48 */       if (sharedNameTable != null) {
/*  49 */         return sharedNameTable;
/*     */       }
/*     */     } 
/*  52 */     return new SharedNameTable(paramNames);
/*     */   }
/*     */   public byte[] bytes; private int hashMask;
/*     */   private static synchronized void dispose(SharedNameTable paramSharedNameTable) {
/*  56 */     freelist = freelist.prepend(new SoftReference<>(paramSharedNameTable));
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
/*  73 */   private int nc = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SharedNameTable(Names paramNames, int paramInt1, int paramInt2) {
/*  82 */     super(paramNames);
/*  83 */     this.hashMask = paramInt1 - 1;
/*  84 */     this.hashes = new NameImpl[paramInt1];
/*  85 */     this.bytes = new byte[paramInt2];
/*     */   }
/*     */ 
/*     */   
/*     */   public SharedNameTable(Names paramNames) {
/*  90 */     this(paramNames, 32768, 131072);
/*     */   }
/*     */ 
/*     */   
/*     */   public Name fromChars(char[] paramArrayOfchar, int paramInt1, int paramInt2) {
/*  95 */     int i = this.nc;
/*  96 */     byte[] arrayOfByte = this.bytes = ArrayUtils.ensureCapacity(this.bytes, i + paramInt2 * 3);
/*  97 */     int j = Convert.chars2utf(paramArrayOfchar, paramInt1, arrayOfByte, i, paramInt2) - i;
/*  98 */     int k = hashValue(arrayOfByte, i, j) & this.hashMask;
/*  99 */     NameImpl nameImpl = this.hashes[k];
/* 100 */     while (nameImpl != null && (nameImpl
/* 101 */       .getByteLength() != j || 
/* 102 */       !equals(arrayOfByte, nameImpl.index, arrayOfByte, i, j))) {
/* 103 */       nameImpl = nameImpl.next;
/*     */     }
/* 105 */     if (nameImpl == null) {
/* 106 */       nameImpl = new NameImpl(this);
/* 107 */       nameImpl.index = i;
/* 108 */       nameImpl.length = j;
/* 109 */       nameImpl.next = this.hashes[k];
/* 110 */       this.hashes[k] = nameImpl;
/* 111 */       this.nc = i + j;
/* 112 */       if (j == 0) {
/* 113 */         this.nc++;
/*     */       }
/*     */     } 
/* 116 */     return nameImpl;
/*     */   }
/*     */ 
/*     */   
/*     */   public Name fromUtf(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 121 */     int i = hashValue(paramArrayOfbyte, paramInt1, paramInt2) & this.hashMask;
/* 122 */     NameImpl nameImpl = this.hashes[i];
/* 123 */     byte[] arrayOfByte = this.bytes;
/* 124 */     while (nameImpl != null && (nameImpl
/* 125 */       .getByteLength() != paramInt2 || !equals(arrayOfByte, nameImpl.index, paramArrayOfbyte, paramInt1, paramInt2))) {
/* 126 */       nameImpl = nameImpl.next;
/*     */     }
/* 128 */     if (nameImpl == null) {
/* 129 */       int j = this.nc;
/* 130 */       arrayOfByte = this.bytes = ArrayUtils.ensureCapacity(arrayOfByte, j + paramInt2);
/* 131 */       System.arraycopy(paramArrayOfbyte, paramInt1, arrayOfByte, j, paramInt2);
/* 132 */       nameImpl = new NameImpl(this);
/* 133 */       nameImpl.index = j;
/* 134 */       nameImpl.length = paramInt2;
/* 135 */       nameImpl.next = this.hashes[i];
/* 136 */       this.hashes[i] = nameImpl;
/* 137 */       this.nc = j + paramInt2;
/* 138 */       if (paramInt2 == 0) {
/* 139 */         this.nc++;
/*     */       }
/*     */     } 
/* 142 */     return nameImpl;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 147 */     dispose(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class NameImpl
/*     */     extends Name
/*     */   {
/*     */     NameImpl next;
/*     */ 
/*     */     
/*     */     int index;
/*     */ 
/*     */     
/*     */     int length;
/*     */ 
/*     */     
/*     */     NameImpl(SharedNameTable param1SharedNameTable) {
/* 165 */       super(param1SharedNameTable);
/*     */     }
/*     */ 
/*     */     
/*     */     public int getIndex() {
/* 170 */       return this.index;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getByteLength() {
/* 175 */       return this.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte getByteAt(int param1Int) {
/* 180 */       return getByteArray()[this.index + param1Int];
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] getByteArray() {
/* 185 */       return ((SharedNameTable)this.table).bytes;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getByteOffset() {
/* 190 */       return this.index;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 196 */       return this.index;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object) {
/* 202 */       if (param1Object instanceof Name)
/* 203 */         return (this.table == ((Name)param1Object).table && this.index == ((Name)param1Object)
/* 204 */           .getIndex()); 
/* 205 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\SharedNameTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */