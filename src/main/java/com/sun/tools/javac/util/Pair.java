/*    */ package com.sun.tools.javac.util;
/*    */ 
/*    */ import java.util.Objects;
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
/*    */ 
/*    */ 
/*    */ public class Pair<A, B>
/*    */ {
/*    */   public final A fst;
/*    */   public final B snd;
/*    */   
/*    */   public Pair(A paramA, B paramB) {
/* 43 */     this.fst = paramA;
/* 44 */     this.snd = paramB;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 48 */     return "Pair[" + this.fst + "," + this.snd + "]";
/*    */   }
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 52 */     return (paramObject instanceof Pair && 
/*    */       
/* 54 */       Objects.equals(this.fst, ((Pair)paramObject).fst) && 
/* 55 */       Objects.equals(this.snd, ((Pair)paramObject).snd));
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 59 */     if (this.fst == null) return (this.snd == null) ? 0 : (this.snd.hashCode() + 1); 
/* 60 */     if (this.snd == null) return this.fst.hashCode() + 2; 
/* 61 */     return this.fst.hashCode() * 17 + this.snd.hashCode();
/*    */   }
/*    */   
/*    */   public static <A, B> Pair<A, B> of(A paramA, B paramB) {
/* 65 */     return new Pair<>(paramA, paramB);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\java\\util\Pair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */