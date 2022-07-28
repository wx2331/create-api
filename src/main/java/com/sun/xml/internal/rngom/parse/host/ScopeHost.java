/*    */ package com.sun.xml.internal.rngom.parse.host;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*    */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*    */ import com.sun.xml.internal.rngom.ast.builder.GrammarSection;
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
/*    */ public class ScopeHost
/*    */   extends GrammarSectionHost
/*    */   implements Scope
/*    */ {
/*    */   protected final Scope lhs;
/*    */   protected final Scope rhs;
/*    */   
/*    */   protected ScopeHost(Scope lhs, Scope rhs) {
/* 64 */     super((GrammarSection)lhs, (GrammarSection)rhs);
/* 65 */     this.lhs = lhs;
/* 66 */     this.rhs = rhs;
/*    */   }
/*    */   
/*    */   public ParsedPattern makeParentRef(String name, Location _loc, Annotations _anno) throws BuildException {
/* 70 */     LocationHost loc = cast(_loc);
/* 71 */     AnnotationsHost anno = cast(_anno);
/*    */     
/* 73 */     return new ParsedPatternHost(this.lhs
/* 74 */         .makeParentRef(name, loc.lhs, anno.lhs), this.rhs
/* 75 */         .makeParentRef(name, loc.rhs, anno.rhs));
/*    */   }
/*    */   
/*    */   public ParsedPattern makeRef(String name, Location _loc, Annotations _anno) throws BuildException {
/* 79 */     LocationHost loc = cast(_loc);
/* 80 */     AnnotationsHost anno = cast(_anno);
/*    */     
/* 82 */     return new ParsedPatternHost(this.lhs
/* 83 */         .makeRef(name, loc.lhs, anno.lhs), this.rhs
/* 84 */         .makeRef(name, loc.rhs, anno.rhs));
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\host\ScopeHost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */