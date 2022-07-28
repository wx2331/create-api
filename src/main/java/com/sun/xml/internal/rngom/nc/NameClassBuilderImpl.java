/*     */ package com.sun.xml.internal.rngom.nc;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*     */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*     */ import com.sun.xml.internal.rngom.ast.builder.CommentList;
/*     */ import com.sun.xml.internal.rngom.ast.builder.NameClassBuilder;
/*     */ import com.sun.xml.internal.rngom.ast.om.Location;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedNameClass;
/*     */ import java.util.List;
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
/*     */ public class NameClassBuilderImpl<E extends ParsedElementAnnotation, L extends Location, A extends Annotations<E, L, CL>, CL extends CommentList<L>>
/*     */   implements NameClassBuilder<NameClass, E, L, A, CL>
/*     */ {
/*     */   public NameClass makeChoice(List<NameClass> nameClasses, L loc, A anno) {
/*  70 */     NameClass result = nameClasses.get(0);
/*  71 */     for (int i = 1; i < nameClasses.size(); i++) {
/*  72 */       result = new ChoiceNameClass(result, nameClasses.get(i));
/*     */     }
/*  74 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public NameClass makeName(String ns, String localName, String prefix, L loc, A anno) {
/*  79 */     if (prefix == null) {
/*  80 */       return new SimpleNameClass(ns, localName);
/*     */     }
/*  82 */     return new SimpleNameClass(ns, localName, prefix);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NameClass makeNsName(String ns, L loc, A anno) {
/*  88 */     return new NsNameClass(ns);
/*     */   }
/*     */   
/*     */   public NameClass makeNsName(String ns, NameClass except, L loc, A anno) {
/*  92 */     return new NsNameExceptNameClass(ns, except);
/*     */   }
/*     */   
/*     */   public NameClass makeAnyName(L loc, A anno) {
/*  96 */     return NameClass.ANY;
/*     */   }
/*     */   
/*     */   public NameClass makeAnyName(NameClass except, L loc, A anno) {
/* 100 */     return new AnyNameExceptNameClass(except);
/*     */   }
/*     */   
/*     */   public NameClass makeErrorNameClass() {
/* 104 */     return NameClass.NULL;
/*     */   }
/*     */   
/*     */   public NameClass annotate(NameClass nc, A anno) throws BuildException {
/* 108 */     return nc;
/*     */   }
/*     */   
/*     */   public NameClass annotateAfter(NameClass nc, E e) throws BuildException {
/* 112 */     return nc;
/*     */   }
/*     */   
/*     */   public NameClass commentAfter(NameClass nc, CL comments) throws BuildException {
/* 116 */     return nc;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\nc\NameClassBuilderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */