/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import java.util.BitSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Position
/*     */ {
/*     */   public static final int NOPOS = -1;
/*     */   public static final int FIRSTPOS = 0;
/*     */   public static final int FIRSTLINE = 1;
/*     */   public static final int FIRSTCOLUMN = 1;
/*     */   public static final int LINESHIFT = 10;
/*     */   public static final int MAXCOLUMN = 1023;
/*     */   public static final int MAXLINE = 4194303;
/*     */   public static final int MAXPOS = 2147483647;
/*     */   
/*     */   public static LineMap makeLineMap(char[] paramArrayOfchar, int paramInt, boolean paramBoolean) {
/*  75 */     LineMapImpl lineMapImpl = paramBoolean ? new LineTabMapImpl(paramInt) : new LineMapImpl();
/*     */     
/*  77 */     lineMapImpl.build(paramArrayOfchar, paramInt);
/*  78 */     return lineMapImpl;
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
/*     */   public static int encodePosition(int paramInt1, int paramInt2) {
/*  93 */     if (paramInt1 < 1)
/*  94 */       throw new IllegalArgumentException("line must be greater than 0"); 
/*  95 */     if (paramInt2 < 1) {
/*  96 */       throw new IllegalArgumentException("column must be greater than 0");
/*     */     }
/*  98 */     if (paramInt1 > 4194303 || paramInt2 > 1023) {
/*  99 */       return -1;
/*     */     }
/* 101 */     return (paramInt1 << 10) + paramInt2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface LineMap
/*     */     extends com.sun.source.tree.LineMap
/*     */   {
/*     */     int getStartPosition(int param1Int);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int getPosition(int param1Int1, int param1Int2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int getLineNumber(int param1Int);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int getColumnNumber(int param1Int);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class LineMapImpl
/*     */     implements LineMap
/*     */   {
/*     */     protected int[] startPosition;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void build(char[] param1ArrayOfchar, int param1Int) {
/* 151 */       byte b1 = 0;
/* 152 */       byte b2 = 0;
/* 153 */       int[] arrayOfInt = new int[param1Int];
/* 154 */       label24: while (b2 < param1Int) {
/* 155 */         arrayOfInt[b1++] = b2;
/*     */         while (true)
/* 157 */         { char c = param1ArrayOfchar[b2];
/* 158 */           if (c == '\r' || c == '\n') {
/* 159 */             if (c == '\r' && b2 + 1 < param1Int && param1ArrayOfchar[b2 + 1] == '\n') {
/* 160 */               b2 += 2; continue label24;
/*     */             } 
/* 162 */             b2++;
/*     */             continue label24;
/*     */           } 
/* 165 */           if (c == '\t')
/* 166 */             setTabPosition(b2); 
/* 167 */           if (++b2 >= param1Int)
/*     */             continue label24;  } 
/* 169 */       }  this.startPosition = new int[b1];
/* 170 */       System.arraycopy(arrayOfInt, 0, this.startPosition, 0, b1);
/*     */     }
/*     */     
/*     */     public int getStartPosition(int param1Int) {
/* 174 */       return this.startPosition[param1Int - 1];
/*     */     }
/*     */     
/*     */     public long getStartPosition(long param1Long) {
/* 178 */       return getStartPosition(longToInt(param1Long));
/*     */     }
/*     */     
/*     */     public int getPosition(int param1Int1, int param1Int2) {
/* 182 */       return this.startPosition[param1Int1 - 1] + param1Int2 - 1;
/*     */     }
/*     */     
/*     */     public long getPosition(long param1Long1, long param1Long2) {
/* 186 */       return getPosition(longToInt(param1Long1), longToInt(param1Long2));
/*     */     }
/*     */ 
/*     */     
/* 190 */     private int lastPosition = 0;
/* 191 */     private int lastLine = 1;
/*     */     
/*     */     public int getLineNumber(int param1Int) {
/* 194 */       if (param1Int == this.lastPosition) {
/* 195 */         return this.lastLine;
/*     */       }
/* 197 */       this.lastPosition = param1Int;
/*     */       
/* 199 */       int i = 0;
/* 200 */       int j = this.startPosition.length - 1;
/* 201 */       while (i <= j) {
/* 202 */         int k = i + j >> 1;
/* 203 */         int m = this.startPosition[k];
/*     */         
/* 205 */         if (m < param1Int) {
/* 206 */           i = k + 1; continue;
/* 207 */         }  if (m > param1Int) {
/* 208 */           j = k - 1; continue;
/*     */         } 
/* 210 */         this.lastLine = k + 1;
/* 211 */         return this.lastLine;
/*     */       } 
/*     */       
/* 214 */       this.lastLine = i;
/* 215 */       return this.lastLine;
/*     */     }
/*     */     
/*     */     public long getLineNumber(long param1Long) {
/* 219 */       return getLineNumber(longToInt(param1Long));
/*     */     }
/*     */     
/*     */     public int getColumnNumber(int param1Int) {
/* 223 */       return param1Int - this.startPosition[getLineNumber(param1Int) - 1] + 1;
/*     */     }
/*     */     
/*     */     public long getColumnNumber(long param1Long) {
/* 227 */       return getColumnNumber(longToInt(param1Long));
/*     */     }
/*     */     
/*     */     private static int longToInt(long param1Long) {
/* 231 */       int i = (int)param1Long;
/* 232 */       if (i != param1Long)
/* 233 */         throw new IndexOutOfBoundsException(); 
/* 234 */       return i;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void setTabPosition(int param1Int) {}
/*     */   }
/*     */ 
/*     */   
/*     */   public static class LineTabMapImpl
/*     */     extends LineMapImpl
/*     */   {
/*     */     private BitSet tabMap;
/*     */ 
/*     */     
/*     */     public LineTabMapImpl(int param1Int) {
/* 249 */       this.tabMap = new BitSet(param1Int);
/*     */     }
/*     */     
/*     */     protected void setTabPosition(int param1Int) {
/* 253 */       this.tabMap.set(param1Int);
/*     */     }
/*     */     
/*     */     public int getColumnNumber(int param1Int) {
/* 257 */       int i = this.startPosition[getLineNumber(param1Int) - 1];
/* 258 */       int j = 0;
/* 259 */       for (int k = i; k < param1Int; k++) {
/* 260 */         if (this.tabMap.get(k)) {
/* 261 */           j = j / 8 * 8 + 8;
/*     */         } else {
/* 263 */           j++;
/*     */         } 
/* 265 */       }  return j + 1;
/*     */     }
/*     */     
/*     */     public int getPosition(int param1Int1, int param1Int2) {
/* 269 */       int i = this.startPosition[param1Int1 - 1];
/* 270 */       param1Int2--;
/* 271 */       int j = 0;
/* 272 */       while (j < param1Int2) {
/* 273 */         i++;
/* 274 */         if (this.tabMap.get(i)) {
/* 275 */           j = j / 8 * 8 + 8; continue;
/*     */         } 
/* 277 */         j++;
/*     */       } 
/* 279 */       return i;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\Position.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */