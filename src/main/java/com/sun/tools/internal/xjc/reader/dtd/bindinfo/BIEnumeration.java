/*     */ package com.sun.tools.internal.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfoParent;
/*     */ import com.sun.tools.internal.xjc.model.CEnumConstant;
/*     */ import com.sun.tools.internal.xjc.model.CEnumLeafInfo;
/*     */ import com.sun.tools.internal.xjc.model.CNonElement;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.model.TypeUse;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import org.w3c.dom.Element;
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
/*     */ public final class BIEnumeration
/*     */   implements BIConversion
/*     */ {
/*     */   private final Element e;
/*     */   private final TypeUse xducer;
/*     */   
/*     */   private BIEnumeration(Element _e, TypeUse _xducer) {
/*  48 */     this.e = _e;
/*  49 */     this.xducer = _xducer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/*  57 */     return DOMUtil.getAttribute(this.e, "name");
/*     */   }
/*     */   public TypeUse getTransducer() {
/*  60 */     return this.xducer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static BIEnumeration create(Element dom, BindInfo parent) {
/*  68 */     return new BIEnumeration(dom, (TypeUse)new CEnumLeafInfo(parent.model, null, (CClassInfoParent)new CClassInfoParent.Package(parent
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*  73 */             .getTargetPackage()), 
/*  74 */           DOMUtil.getAttribute(dom, "name"), (CNonElement)CBuiltinLeafInfo.STRING, 
/*     */           
/*  76 */           buildMemberList(parent.model, dom), null, null, 
/*     */           
/*  78 */           DOMLocator.getLocationInfo(dom)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static BIEnumeration create(Element dom, BIElement parent) {
/*  84 */     return new BIEnumeration(dom, (TypeUse)new CEnumLeafInfo(parent.parent.model, null, (CClassInfoParent)parent.clazz, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  90 */           DOMUtil.getAttribute(dom, "name"), (CNonElement)CBuiltinLeafInfo.STRING, 
/*     */           
/*  92 */           buildMemberList(parent.parent.model, dom), null, null, 
/*     */           
/*  94 */           DOMLocator.getLocationInfo(dom)));
/*     */   }
/*     */   
/*     */   private static List<CEnumConstant> buildMemberList(Model model, Element dom) {
/*  98 */     List<CEnumConstant> r = new ArrayList<>();
/*     */     
/* 100 */     String members = DOMUtil.getAttribute(dom, "members");
/* 101 */     if (members == null) members = "";
/*     */     
/* 103 */     StringTokenizer tokens = new StringTokenizer(members);
/* 104 */     while (tokens.hasMoreTokens()) {
/* 105 */       String token = tokens.nextToken();
/* 106 */       r.add(new CEnumConstant(model.getNameConverter().toConstantName(token), null, token, null, null, null));
/*     */     } 
/*     */ 
/*     */     
/* 110 */     return r;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\dtd\bindinfo\BIEnumeration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */