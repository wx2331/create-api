/*    */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.model.Multiplicity;
/*    */ import com.sun.xml.internal.xsom.XSElementDecl;
/*    */ import com.sun.xml.internal.xsom.XSModelGroup;
/*    */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*    */ import com.sun.xml.internal.xsom.XSParticle;
/*    */ import com.sun.xml.internal.xsom.XSWildcard;
/*    */ import com.sun.xml.internal.xsom.visitor.XSTermFunction;
/*    */ import java.math.BigInteger;
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
/*    */ public final class MultiplicityCounter
/*    */   implements XSTermFunction<Multiplicity>
/*    */ {
/* 47 */   public static final MultiplicityCounter theInstance = new MultiplicityCounter();
/*    */ 
/*    */   
/*    */   public Multiplicity particle(XSParticle p) {
/*    */     BigInteger max;
/* 52 */     Multiplicity m = (Multiplicity)p.getTerm().apply(this);
/*    */ 
/*    */     
/* 55 */     if (m.max == null || BigInteger.valueOf(-1L).equals(p.getMaxOccurs())) {
/* 56 */       max = null;
/*    */     } else {
/* 58 */       max = p.getMaxOccurs();
/*    */     } 
/* 60 */     return Multiplicity.multiply(m, Multiplicity.create(p.getMinOccurs(), max));
/*    */   }
/*    */   
/*    */   public Multiplicity wildcard(XSWildcard wc) {
/* 64 */     return Multiplicity.ONE;
/*    */   }
/*    */   
/*    */   public Multiplicity modelGroupDecl(XSModelGroupDecl decl) {
/* 68 */     return modelGroup(decl.getModelGroup());
/*    */   }
/*    */   
/*    */   public Multiplicity modelGroup(XSModelGroup group) {
/* 72 */     boolean isChoice = (group.getCompositor() == XSModelGroup.CHOICE);
/*    */     
/* 74 */     Multiplicity r = Multiplicity.ZERO;
/*    */     
/* 76 */     for (XSParticle p : group.getChildren()) {
/* 77 */       Multiplicity m = particle(p);
/*    */       
/* 79 */       if (r == null) {
/* 80 */         r = m;
/*    */       
/*    */       }
/* 83 */       else if (isChoice) {
/* 84 */         r = Multiplicity.choice(r, m);
/*    */       } else {
/* 86 */         r = Multiplicity.group(r, m);
/*    */       } 
/*    */     } 
/* 89 */     return r;
/*    */   }
/*    */   
/*    */   public Multiplicity elementDecl(XSElementDecl decl) {
/* 93 */     return Multiplicity.ONE;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\MultiplicityCounter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */