/*     */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIDeclaration;
/*     */ import com.sun.xml.internal.xsom.XSAnnotation;
/*     */ import com.sun.xml.internal.xsom.XSAttContainer;
/*     */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeUse;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSContentType;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSFacet;
/*     */ import com.sun.xml.internal.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.internal.xsom.XSListSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSNotation;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSRestrictionSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSSchema;
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSUnionSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.XSXPath;
/*     */ import com.sun.xml.internal.xsom.visitor.XSSimpleTypeVisitor;
/*     */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class UnusedCustomizationChecker
/*     */   extends BindingComponent
/*     */   implements XSVisitor, XSSimpleTypeVisitor
/*     */ {
/*  75 */   private final BGMBuilder builder = (BGMBuilder)Ring.get(BGMBuilder.class);
/*  76 */   private final SimpleTypeBuilder stb = (SimpleTypeBuilder)Ring.get(SimpleTypeBuilder.class);
/*     */   
/*  78 */   private final Set<XSComponent> visitedComponents = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void run() {
/*  84 */     for (XSSchema s : ((XSSchemaSet)Ring.get(XSSchemaSet.class)).getSchemas()) {
/*  85 */       schema(s);
/*  86 */       run(s.getAttGroupDecls());
/*  87 */       run(s.getAttributeDecls());
/*  88 */       run(s.getComplexTypes());
/*  89 */       run(s.getElementDecls());
/*  90 */       run(s.getModelGroupDecls());
/*  91 */       run(s.getNotations());
/*  92 */       run(s.getSimpleTypes());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void run(Map<String, ? extends XSComponent> col) {
/*  97 */     for (XSComponent c : col.values()) {
/*  98 */       c.visit(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean check(XSComponent c) {
/* 108 */     if (!this.visitedComponents.add(c)) {
/* 109 */       return false;
/*     */     }
/* 111 */     for (BIDeclaration decl : this.builder.getBindInfo(c).getDecls()) {
/* 112 */       check(decl, c);
/*     */     }
/* 114 */     checkExpectedContentTypes(c);
/*     */     
/* 116 */     return true;
/*     */   }
/*     */   
/*     */   private void checkExpectedContentTypes(XSComponent c) {
/* 120 */     if (c.getForeignAttribute("http://www.w3.org/2005/05/xmlmime", "expectedContentTypes") == null)
/*     */       return; 
/* 122 */     if (c instanceof XSParticle) {
/*     */       return;
/*     */     }
/*     */     
/* 126 */     if (!this.stb.isAcknowledgedXmimeContentTypes(c))
/*     */     {
/* 128 */       getErrorReporter().warning(c.getLocator(), "UnusedCustomizationChecker.WarnUnusedExpectedContentTypes", new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   private void check(BIDeclaration decl, XSComponent c) {
/* 133 */     if (!decl.isAcknowledged()) {
/* 134 */       getErrorReporter().error(decl
/* 135 */           .getLocation(), "UnusedCustomizationChecker.UnacknolwedgedCustomization", new Object[] { decl
/*     */             
/* 137 */             .getName().getLocalPart() });
/*     */       
/* 139 */       getErrorReporter().error(c
/* 140 */           .getLocator(), "UnusedCustomizationChecker.UnacknolwedgedCustomization.Relevant", new Object[0]);
/*     */ 
/*     */ 
/*     */       
/* 144 */       decl.markAsAcknowledged();
/*     */     } 
/* 146 */     for (BIDeclaration d : decl.getChildren()) {
/* 147 */       check(d, c);
/*     */     }
/*     */   }
/*     */   
/*     */   public void annotation(XSAnnotation ann) {}
/*     */   
/*     */   public void attGroupDecl(XSAttGroupDecl decl) {
/* 154 */     if (check((XSComponent)decl))
/* 155 */       attContainer((XSAttContainer)decl); 
/*     */   }
/*     */   
/*     */   public void attributeDecl(XSAttributeDecl decl) {
/* 159 */     if (check((XSComponent)decl))
/* 160 */       decl.getType().visit(this); 
/*     */   }
/*     */   
/*     */   public void attributeUse(XSAttributeUse use) {
/* 164 */     if (check((XSComponent)use))
/* 165 */       use.getDecl().visit(this); 
/*     */   }
/*     */   
/*     */   public void complexType(XSComplexType type) {
/* 169 */     if (check((XSComponent)type)) {
/*     */ 
/*     */       
/* 172 */       type.getContentType().visit(this);
/* 173 */       attContainer((XSAttContainer)type);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void attContainer(XSAttContainer cont) {
/* 178 */     for (Iterator<XSAttGroupDecl> iterator = cont.iterateAttGroups(); iterator.hasNext();) {
/* 179 */       ((XSAttGroupDecl)iterator.next()).visit(this);
/*     */     }
/* 181 */     for (Iterator<XSAttributeUse> itr = cont.iterateDeclaredAttributeUses(); itr.hasNext();) {
/* 182 */       ((XSAttributeUse)itr.next()).visit(this);
/*     */     }
/* 184 */     XSWildcard wc = cont.getAttributeWildcard();
/* 185 */     if (wc != null) wc.visit(this); 
/*     */   }
/*     */   
/*     */   public void schema(XSSchema schema) {
/* 189 */     check((XSComponent)schema);
/*     */   }
/*     */   
/*     */   public void facet(XSFacet facet) {
/* 193 */     check((XSComponent)facet);
/*     */   }
/*     */   
/*     */   public void notation(XSNotation notation) {
/* 197 */     check((XSComponent)notation);
/*     */   }
/*     */   
/*     */   public void wildcard(XSWildcard wc) {
/* 201 */     check((XSComponent)wc);
/*     */   }
/*     */   
/*     */   public void modelGroupDecl(XSModelGroupDecl decl) {
/* 205 */     if (check((XSComponent)decl))
/* 206 */       decl.getModelGroup().visit(this); 
/*     */   }
/*     */   
/*     */   public void modelGroup(XSModelGroup group) {
/* 210 */     if (check((XSComponent)group))
/* 211 */       for (int i = 0; i < group.getSize(); i++) {
/* 212 */         group.getChild(i).visit(this);
/*     */       } 
/*     */   }
/*     */   
/*     */   public void elementDecl(XSElementDecl decl) {
/* 217 */     if (check((XSComponent)decl)) {
/* 218 */       decl.getType().visit(this);
/* 219 */       for (XSIdentityConstraint id : decl.getIdentityConstraints())
/* 220 */         id.visit(this); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void simpleType(XSSimpleType simpleType) {
/* 225 */     if (check((XSComponent)simpleType))
/* 226 */       simpleType.visit(this); 
/*     */   }
/*     */   
/*     */   public void particle(XSParticle particle) {
/* 230 */     if (check((XSComponent)particle))
/* 231 */       particle.getTerm().visit(this); 
/*     */   }
/*     */   
/*     */   public void empty(XSContentType empty) {
/* 235 */     check((XSComponent)empty);
/*     */   }
/*     */   
/*     */   public void listSimpleType(XSListSimpleType type) {
/* 239 */     if (check((XSComponent)type))
/* 240 */       type.getItemType().visit(this); 
/*     */   }
/*     */   
/*     */   public void restrictionSimpleType(XSRestrictionSimpleType type) {
/* 244 */     if (check((XSComponent)type))
/* 245 */       type.getBaseType().visit(this); 
/*     */   }
/*     */   
/*     */   public void unionSimpleType(XSUnionSimpleType type) {
/* 249 */     if (check((XSComponent)type))
/* 250 */       for (int i = 0; i < type.getMemberSize(); i++) {
/* 251 */         type.getMember(i).visit(this);
/*     */       } 
/*     */   }
/*     */   
/*     */   public void identityConstraint(XSIdentityConstraint id) {
/* 256 */     if (check((XSComponent)id)) {
/* 257 */       id.getSelector().visit(this);
/* 258 */       for (XSXPath xp : id.getFields())
/* 259 */         xp.visit(this); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void xpath(XSXPath xp) {
/* 264 */     check((XSComponent)xp);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\UnusedCustomizationChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */