/*    */ package com.sun.xml.internal.rngom.parse.host;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*    */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*    */ import com.sun.xml.internal.rngom.ast.builder.GrammarSection;
/*    */ import com.sun.xml.internal.rngom.ast.builder.Include;
/*    */ import com.sun.xml.internal.rngom.ast.om.Location;
/*    */ import com.sun.xml.internal.rngom.parse.IllegalSchemaException;
/*    */ import com.sun.xml.internal.rngom.parse.Parseable;
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
/*    */ public class IncludeHost
/*    */   extends GrammarSectionHost
/*    */   implements Include
/*    */ {
/*    */   private final Include lhs;
/*    */   private final Include rhs;
/*    */   
/*    */   IncludeHost(Include lhs, Include rhs) {
/* 66 */     super((GrammarSection)lhs, (GrammarSection)rhs);
/* 67 */     this.lhs = lhs;
/* 68 */     this.rhs = rhs;
/*    */   }
/*    */   
/*    */   public void endInclude(Parseable current, String uri, String ns, Location _loc, Annotations _anno) throws BuildException, IllegalSchemaException {
/* 72 */     LocationHost loc = cast(_loc);
/* 73 */     AnnotationsHost anno = cast(_anno);
/*    */     
/* 75 */     this.lhs.endInclude(current, uri, ns, loc.lhs, anno.lhs);
/* 76 */     this.rhs.endInclude(current, uri, ns, loc.rhs, anno.rhs);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\host\IncludeHost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */