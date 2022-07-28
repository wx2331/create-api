/*     */ package com.sun.tools.javac.model;
/*     */ 
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FilteredMemberList
/*     */   extends AbstractList<Symbol>
/*     */ {
/*     */   private final Scope scope;
/*     */   
/*     */   public FilteredMemberList(Scope paramScope) {
/*  51 */     this.scope = paramScope;
/*     */   }
/*     */   
/*     */   public int size() {
/*  55 */     byte b = 0;
/*  56 */     for (Scope.Entry entry = this.scope.elems; entry != null; entry = entry.sibling) {
/*  57 */       if (!unwanted(entry.sym))
/*  58 */         b++; 
/*     */     } 
/*  60 */     return b;
/*     */   }
/*     */   
/*     */   public Symbol get(int paramInt) {
/*  64 */     for (Scope.Entry entry = this.scope.elems; entry != null; entry = entry.sibling) {
/*  65 */       if (!unwanted(entry.sym) && paramInt-- == 0)
/*  66 */         return entry.sym; 
/*     */     } 
/*  68 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Symbol> iterator() {
/*  73 */     return new Iterator<Symbol>()
/*     */       {
/*     */         
/*  76 */         private Scope.Entry nextEntry = FilteredMemberList.this.scope.elems;
/*     */         
/*     */         private boolean hasNextForSure = false;
/*     */         
/*     */         public boolean hasNext() {
/*  81 */           if (this.hasNextForSure) {
/*  82 */             return true;
/*     */           }
/*  84 */           while (this.nextEntry != null && FilteredMemberList.unwanted(this.nextEntry.sym)) {
/*  85 */             this.nextEntry = this.nextEntry.sibling;
/*     */           }
/*  87 */           this.hasNextForSure = (this.nextEntry != null);
/*  88 */           return this.hasNextForSure;
/*     */         }
/*     */         
/*     */         public Symbol next() {
/*  92 */           if (hasNext()) {
/*  93 */             Symbol symbol = this.nextEntry.sym;
/*  94 */             this.nextEntry = this.nextEntry.sibling;
/*  95 */             this.hasNextForSure = false;
/*  96 */             return symbol;
/*     */           } 
/*  98 */           throw new NoSuchElementException();
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 103 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean unwanted(Symbol paramSymbol) {
/* 113 */     return (paramSymbol == null || (paramSymbol.flags() & 0x1000L) != 0L);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\model\FilteredMemberList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */