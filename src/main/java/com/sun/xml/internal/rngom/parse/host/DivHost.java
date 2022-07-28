/*    */ package com.sun.xml.internal.rngom.parse.host;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*    */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*    */ import com.sun.xml.internal.rngom.ast.builder.Div;
/*    */ import com.sun.xml.internal.rngom.ast.builder.GrammarSection;
/*    */ import com.sun.xml.internal.rngom.ast.om.Location;
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
/*    */ public class DivHost
/*    */   extends GrammarSectionHost
/*    */   implements Div
/*    */ {
/*    */   private final Div lhs;
/*    */   private final Div rhs;
/*    */   
/*    */   DivHost(Div lhs, Div rhs) {
/* 63 */     super((GrammarSection)lhs, (GrammarSection)rhs);
/* 64 */     this.lhs = lhs;
/* 65 */     this.rhs = rhs;
/*    */   }
/*    */   
/*    */   public void endDiv(Location _loc, Annotations _anno) throws BuildException {
/* 69 */     LocationHost loc = cast(_loc);
/* 70 */     AnnotationsHost anno = cast(_anno);
/*    */     
/* 72 */     this.lhs.endDiv(loc.lhs, anno.lhs);
/* 73 */     this.rhs.endDiv(loc.rhs, anno.rhs);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\host\DivHost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */