/*     */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.codemodel.internal.JJavaName;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIDeclaration;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSTerm;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.visitor.XSTermVisitor;
/*     */ import java.text.ParseException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
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
/*     */ public abstract class ParticleBinder
/*     */ {
/*  54 */   protected final BGMBuilder builder = (BGMBuilder)Ring.get(BGMBuilder.class);
/*     */ 
/*     */   
/*     */   protected ParticleBinder() {
/*  58 */     Ring.add(ParticleBinder.class, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void build(XSParticle p) {
/*  67 */     build(p, Collections.emptySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void build(XSParticle paramXSParticle, Collection<XSParticle> paramCollection);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean checkFallback(XSParticle paramXSParticle);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final CClassInfo getCurrentBean() {
/*  94 */     return getClassSelector().getCurrentBean();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final BIProperty getLocalPropCustomization(XSParticle p) {
/* 102 */     return getLocalCustomization(p, BIProperty.class);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final <T extends BIDeclaration> T getLocalCustomization(XSParticle p, Class<T> type) {
/* 107 */     BIDeclaration bIDeclaration = this.builder.getBindInfo((XSComponent)p).get(type);
/* 108 */     if (bIDeclaration != null) return (T)bIDeclaration;
/*     */ 
/*     */     
/* 111 */     bIDeclaration = this.builder.getBindInfo((XSComponent)p.getTerm()).get(type);
/* 112 */     if (bIDeclaration != null) return (T)bIDeclaration;
/*     */     
/* 114 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String computeLabel(XSParticle p) {
/* 124 */     BIProperty cust = getLocalPropCustomization(p);
/* 125 */     if (cust != null && cust.getPropertyName(false) != null) {
/* 126 */       return cust.getPropertyName(false);
/*     */     }
/*     */ 
/*     */     
/* 130 */     XSTerm t = p.getTerm();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     if (t.isElementDecl())
/*     */     {
/* 141 */       return makeJavaName(p, t.asElementDecl().getName()); } 
/* 142 */     if (t.isModelGroupDecl())
/*     */     {
/* 144 */       return makeJavaName(p, t.asModelGroupDecl().getName()); } 
/* 145 */     if (t.isWildcard())
/*     */     {
/* 147 */       return makeJavaName(p, "Any"); } 
/* 148 */     if (t.isModelGroup()) {
/*     */       try {
/* 150 */         return getSpecDefaultName(t.asModelGroup(), p.isRepeated());
/* 151 */       } catch (ParseException e) {
/*     */         
/* 153 */         getErrorReporter().error(t.getLocator(), "DefaultParticleBinder.UnableToGenerateNameFromModelGroup", new Object[0]);
/*     */         
/* 155 */         return "undefined";
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 160 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   
/*     */   protected final String makeJavaName(boolean isRepeated, String xmlName) {
/* 165 */     String name = this.builder.getNameConverter().toPropertyName(xmlName);
/* 166 */     if (this.builder.getGlobalBinding().isSimpleMode() && isRepeated)
/* 167 */       name = JJavaName.getPluralForm(name); 
/* 168 */     return name;
/*     */   }
/*     */   
/*     */   protected final String makeJavaName(XSParticle p, String xmlName) {
/* 172 */     return makeJavaName(p.isRepeated(), xmlName);
/*     */   }
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
/*     */   protected final String getSpecDefaultName(XSModelGroup mg, final boolean repeated) throws ParseException {
/* 190 */     final StringBuilder name = new StringBuilder();
/*     */     
/* 192 */     mg.visit(new XSTermVisitor()
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 197 */           private int count = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 202 */           private boolean rep = repeated;
/*     */           
/*     */           public void wildcard(XSWildcard wc) {
/* 205 */             append("any");
/*     */           }
/*     */           
/*     */           public void modelGroupDecl(XSModelGroupDecl mgd) {
/* 209 */             modelGroup(mgd.getModelGroup());
/*     */           }
/*     */           
/*     */           public void modelGroup(XSModelGroup mg) {
/*     */             String operator;
/* 214 */             if (mg.getCompositor() == XSModelGroup.CHOICE) { operator = "Or"; }
/* 215 */             else { operator = "And"; }
/*     */             
/* 217 */             int size = mg.getSize();
/* 218 */             for (int i = 0; i < size; i++) {
/* 219 */               XSParticle p = mg.getChild(i);
/* 220 */               boolean oldRep = this.rep;
/* 221 */               this.rep |= p.isRepeated();
/* 222 */               p.getTerm().visit(this);
/* 223 */               this.rep = oldRep;
/*     */               
/* 225 */               if (this.count == 3)
/* 226 */                 return;  if (i != size - 1) name.append(operator); 
/*     */             } 
/*     */           }
/*     */           
/*     */           public void elementDecl(XSElementDecl ed) {
/* 231 */             append(ed.getName());
/*     */           }
/*     */           
/*     */           private void append(String token) {
/* 235 */             if (this.count < 3) {
/* 236 */               name.append(ParticleBinder.this.makeJavaName(this.rep, token));
/* 237 */               this.count++;
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 242 */     if (name.length() == 0) throw new ParseException("no element", -1);
/*     */     
/* 244 */     return name.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ErrorReporter getErrorReporter() {
/* 250 */     return (ErrorReporter)Ring.get(ErrorReporter.class);
/*     */   }
/*     */   protected final ClassSelector getClassSelector() {
/* 253 */     return (ClassSelector)Ring.get(ClassSelector.class);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\ParticleBinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */