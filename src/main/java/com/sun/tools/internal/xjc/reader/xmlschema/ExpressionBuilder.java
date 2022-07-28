/*     */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.reader.gbind.Choice;
/*     */ import com.sun.tools.internal.xjc.reader.gbind.Element;
/*     */ import com.sun.tools.internal.xjc.reader.gbind.Expression;
/*     */ import com.sun.tools.internal.xjc.reader.gbind.OneOrMore;
/*     */ import com.sun.tools.internal.xjc.reader.gbind.Sequence;
/*     */ import com.sun.xml.internal.xsom.XSDeclaration;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.visitor.XSTermFunction;
/*     */ import java.math.BigInteger;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
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
/*     */ public final class ExpressionBuilder
/*     */   implements XSTermFunction<Expression>
/*     */ {
/*     */   private GWildcardElement wildcard;
/*     */   private final Map<QName, GElementImpl> decls;
/*     */   private XSParticle current;
/*     */   
/*     */   public static Expression createTree(XSParticle p) {
/*  54 */     return (new ExpressionBuilder()).particle(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ExpressionBuilder() {
/*  63 */     this.wildcard = null;
/*     */     
/*  65 */     this.decls = new HashMap<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression wildcard(XSWildcard wc) {
/*  74 */     if (this.wildcard == null)
/*  75 */       this.wildcard = new GWildcardElement(); 
/*  76 */     this.wildcard.merge(wc);
/*  77 */     this.wildcard.particles.add(this.current);
/*  78 */     return (Expression)this.wildcard;
/*     */   }
/*     */   
/*     */   public Expression modelGroupDecl(XSModelGroupDecl decl) {
/*  82 */     return modelGroup(decl.getModelGroup());
/*     */   }
/*     */   public Expression modelGroup(XSModelGroup group) {
/*     */     Sequence sequence;
/*  86 */     XSModelGroup.Compositor comp = group.getCompositor();
/*  87 */     if (comp == XSModelGroup.CHOICE) {
/*     */       Choice choice;
/*     */ 
/*     */ 
/*     */       
/*  92 */       Expression expression = Expression.EPSILON;
/*  93 */       for (XSParticle p : group.getChildren()) {
/*  94 */         if (expression == null) { expression = particle(p); }
/*  95 */         else { choice = new Choice(expression, particle(p)); }
/*     */       
/*  97 */       }  return (Expression)choice;
/*     */     } 
/*  99 */     Expression e = Expression.EPSILON;
/* 100 */     for (XSParticle p : group.getChildren()) {
/* 101 */       if (e == null) { e = particle(p); }
/* 102 */       else { sequence = new Sequence(e, particle(p)); }
/*     */     
/* 104 */     }  return (Expression)sequence;
/*     */   }
/*     */ 
/*     */   
/*     */   public Element elementDecl(XSElementDecl decl) {
/* 109 */     QName n = BGMBuilder.getName((XSDeclaration)decl);
/*     */     
/* 111 */     GElementImpl e = this.decls.get(n);
/* 112 */     if (e == null) {
/* 113 */       this.decls.put(n, e = new GElementImpl(n, decl));
/*     */     }
/* 115 */     e.particles.add(this.current);
/* 116 */     assert this.current.getTerm() == decl;
/*     */     
/* 118 */     return e;
/*     */   } public Expression particle(XSParticle p) {
/*     */     OneOrMore oneOrMore;
/*     */     Choice choice;
/* 122 */     this.current = p;
/* 123 */     Expression e = (Expression)p.getTerm().apply(this);
/*     */     
/* 125 */     if (p.isRepeated()) {
/* 126 */       oneOrMore = new OneOrMore(e);
/*     */     }
/* 128 */     if (BigInteger.ZERO.equals(p.getMinOccurs())) {
/* 129 */       choice = new Choice((Expression)oneOrMore, Expression.EPSILON);
/*     */     }
/* 131 */     return (Expression)choice;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\ExpressionBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */