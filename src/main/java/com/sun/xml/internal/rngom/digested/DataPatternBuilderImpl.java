/*    */ package com.sun.xml.internal.rngom.digested;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*    */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*    */ import com.sun.xml.internal.rngom.ast.builder.DataPatternBuilder;
/*    */ import com.sun.xml.internal.rngom.ast.om.Location;
/*    */ import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;
/*    */ import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
/*    */ import com.sun.xml.internal.rngom.parse.Context;
/*    */ import org.xml.sax.Locator;
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
/*    */ final class DataPatternBuilderImpl
/*    */   implements DataPatternBuilder
/*    */ {
/*    */   private final DDataPattern p;
/*    */   
/*    */   public DataPatternBuilderImpl(String datatypeLibrary, String type, Location loc) {
/* 65 */     this.p = new DDataPattern();
/* 66 */     this.p.location = (Locator)loc;
/* 67 */     this.p.datatypeLibrary = datatypeLibrary;
/* 68 */     this.p.type = type;
/*    */   }
/*    */   
/*    */   public void addParam(String name, String value, Context context, String ns, Location loc, Annotations anno) throws BuildException {
/* 72 */     this.p.getClass(); this.p.params.add(new DDataPattern.Param(this.p, name, value, context.copy(), ns, loc, (Annotation)anno));
/*    */   }
/*    */ 
/*    */   
/*    */   public void annotation(ParsedElementAnnotation ea) {}
/*    */ 
/*    */   
/*    */   public ParsedPattern makePattern(Location loc, Annotations anno) throws BuildException {
/* 80 */     return makePattern(null, loc, anno);
/*    */   }
/*    */   
/*    */   public ParsedPattern makePattern(ParsedPattern except, Location loc, Annotations anno) throws BuildException {
/* 84 */     this.p.except = (DPattern)except;
/* 85 */     if (anno != null) {
/* 86 */       this.p.annotation = ((Annotation)anno).getResult();
/*    */     }
/* 88 */     return this.p;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\digested\DataPatternBuilderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */