/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.ct;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.WildcardNameClassBuilder;
/*     */ import com.sun.xml.internal.rngom.nc.ChoiceNameClass;
/*     */ import com.sun.xml.internal.rngom.nc.NameClass;
/*     */ import com.sun.xml.internal.rngom.nc.SimpleNameClass;
/*     */ import com.sun.xml.internal.xsom.XSAttributeUse;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSContentType;
/*     */ import com.sun.xml.internal.xsom.XSDeclaration;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.visitor.XSTermFunction;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
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
/*     */ abstract class AbstractExtendedComplexTypeBuilder
/*     */   extends CTBuilder
/*     */ {
/*  63 */   protected final Map<XSComplexType, NameClass[]> characteristicNameClasses = (Map)new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   protected final XSTermFunction<NameClass> contentModelNameClassBuilder = new XSTermFunction<NameClass>()
/*     */     {
/*     */       public NameClass wildcard(XSWildcard wc) {
/*  71 */         return WildcardNameClassBuilder.build(wc);
/*     */       }
/*     */ 
/*     */       
/*     */       public NameClass modelGroupDecl(XSModelGroupDecl decl) {
/*  76 */         return modelGroup(decl.getModelGroup());
/*     */       }
/*     */       
/*     */       public NameClass modelGroup(XSModelGroup group) {
/*     */         ChoiceNameClass choiceNameClass;
/*  81 */         NameClass nc = NameClass.NULL;
/*  82 */         for (int i = 0; i < group.getSize(); i++)
/*  83 */           choiceNameClass = new ChoiceNameClass(nc, (NameClass)group.getChild(i).getTerm().apply(this)); 
/*  84 */         return (NameClass)choiceNameClass;
/*     */       }
/*     */       
/*     */       public NameClass elementDecl(XSElementDecl decl) {
/*  88 */         return AbstractExtendedComplexTypeBuilder.this.getNameClass((XSDeclaration)decl);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkCollision(NameClass anc, NameClass enc, XSComplexType type) {
/*     */     ChoiceNameClass choiceNameClass;
/*  99 */     NameClass[] chnc = this.characteristicNameClasses.get(type);
/* 100 */     if (chnc == null) {
/* 101 */       ChoiceNameClass choiceNameClass1; chnc = new NameClass[2];
/* 102 */       chnc[0] = getNameClass(type.getContentType());
/*     */ 
/*     */       
/* 105 */       NameClass nc = NameClass.NULL;
/* 106 */       Iterator<XSAttributeUse> itr = type.iterateAttributeUses();
/* 107 */       while (itr.hasNext())
/* 108 */         choiceNameClass = new ChoiceNameClass(anc, getNameClass((XSDeclaration)((XSAttributeUse)itr.next()).getDecl())); 
/* 109 */       XSWildcard wc = type.getAttributeWildcard();
/* 110 */       if (wc != null)
/* 111 */         choiceNameClass1 = new ChoiceNameClass(nc, WildcardNameClassBuilder.build(wc)); 
/* 112 */       chnc[1] = (NameClass)choiceNameClass1;
/*     */       
/* 114 */       this.characteristicNameClasses.put(type, chnc);
/*     */     } 
/*     */     
/* 117 */     return (chnc[0].hasOverlapWith(enc) || chnc[1].hasOverlapWith((NameClass)choiceNameClass));
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
/*     */   protected XSComplexType getLastRestrictedType(XSComplexType t) {
/* 129 */     if (t.getBaseType() == this.schemas.getAnyType()) {
/* 130 */       return null;
/*     */     }
/* 132 */     if (t.getDerivationMethod() == 2) {
/* 133 */       return t;
/*     */     }
/*     */     
/* 136 */     XSComplexType baseType = t.getBaseType().asComplexType();
/* 137 */     if (baseType != null) {
/* 138 */       return getLastRestrictedType(baseType);
/*     */     }
/* 140 */     return null;
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
/*     */   protected boolean checkIfExtensionSafe(XSComplexType baseType, XSComplexType thisType) {
/*     */     ChoiceNameClass choiceNameClass;
/* 175 */     XSComplexType lastType = getLastRestrictedType(baseType);
/*     */     
/* 177 */     if (lastType == null) {
/* 178 */       return true;
/*     */     }
/* 180 */     NameClass anc = NameClass.NULL;
/*     */     
/* 182 */     Iterator<XSAttributeUse> itr = thisType.iterateDeclaredAttributeUses();
/* 183 */     while (itr.hasNext()) {
/* 184 */       choiceNameClass = new ChoiceNameClass(anc, getNameClass((XSDeclaration)((XSAttributeUse)itr.next()).getDecl()));
/*     */     }
/*     */ 
/*     */     
/* 188 */     NameClass enc = getNameClass(thisType.getExplicitContent());
/*     */ 
/*     */     
/* 191 */     while (lastType != lastType.getBaseType()) {
/* 192 */       if (checkCollision((NameClass)choiceNameClass, enc, lastType)) {
/* 193 */         return false;
/*     */       }
/*     */       
/* 196 */       if (lastType.getBaseType().isSimpleType())
/*     */       {
/*     */         
/* 199 */         return true;
/*     */       }
/*     */       
/* 202 */       lastType = lastType.getBaseType().asComplexType();
/*     */     } 
/*     */     
/* 205 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private NameClass getNameClass(XSContentType t) {
/* 213 */     if (t == null) return NameClass.NULL; 
/* 214 */     XSParticle p = t.asParticle();
/* 215 */     if (p == null) return NameClass.NULL; 
/* 216 */     return (NameClass)p.getTerm().apply(this.contentModelNameClassBuilder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private NameClass getNameClass(XSDeclaration decl) {
/* 223 */     return (NameClass)new SimpleNameClass(new QName(decl.getTargetNamespace(), decl.getName()));
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\ct\AbstractExtendedComplexTypeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */