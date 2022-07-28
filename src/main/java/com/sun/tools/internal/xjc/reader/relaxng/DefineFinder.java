/*    */ package com.sun.tools.internal.xjc.reader.relaxng;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.digested.DDefine;
/*    */ import com.sun.xml.internal.rngom.digested.DGrammarPattern;
/*    */ import com.sun.xml.internal.rngom.digested.DPatternVisitor;
/*    */ import com.sun.xml.internal.rngom.digested.DPatternWalker;
/*    */ import com.sun.xml.internal.rngom.digested.DRefPattern;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
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
/*    */ final class DefineFinder
/*    */   extends DPatternWalker
/*    */ {
/* 43 */   public final Set<DDefine> defs = new HashSet<>();
/*    */   
/*    */   public Void onGrammar(DGrammarPattern p) {
/* 46 */     for (DDefine def : p) {
/* 47 */       this.defs.add(def);
/* 48 */       def.getPattern().accept((DPatternVisitor)this);
/*    */     } 
/*    */     
/* 51 */     return (Void)p.getStart().accept((DPatternVisitor)this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Void onRef(DRefPattern p) {
/* 59 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\relaxng\DefineFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */