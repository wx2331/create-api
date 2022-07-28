/*    */ package com.sun.xml.internal.rngom.parse.host;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*    */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*    */ import com.sun.xml.internal.rngom.ast.builder.Grammar;
/*    */ import com.sun.xml.internal.rngom.ast.builder.Scope;
/*    */ import com.sun.xml.internal.rngom.ast.om.Location;
/*    */ import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
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
/*    */ public class GrammarHost
/*    */   extends ScopeHost
/*    */   implements Grammar
/*    */ {
/*    */   final Grammar lhs;
/*    */   final Grammar rhs;
/*    */   
/*    */   public GrammarHost(Grammar lhs, Grammar rhs) {
/* 72 */     super((Scope)lhs, (Scope)rhs);
/* 73 */     this.lhs = lhs;
/* 74 */     this.rhs = rhs;
/*    */   }
/*    */   
/*    */   public ParsedPattern endGrammar(Location _loc, Annotations _anno) throws BuildException {
/* 78 */     LocationHost loc = cast(_loc);
/* 79 */     AnnotationsHost anno = cast(_anno);
/*    */     
/* 81 */     return new ParsedPatternHost(this.lhs
/* 82 */         .endGrammar(loc.lhs, anno.lhs), this.rhs
/* 83 */         .endGrammar(loc.rhs, anno.rhs));
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\host\GrammarHost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */