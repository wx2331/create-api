/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.MethodWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class MethodBuilder
/*     */   extends AbstractMemberBuilder
/*     */ {
/*     */   private int currentMethodIndex;
/*     */   private final ClassDoc classDoc;
/*     */   private final VisibleMemberMap visibleMemberMap;
/*     */   private final MethodWriter writer;
/*     */   private List<ProgramElementDoc> methods;
/*     */
/*     */   private MethodBuilder(Context paramContext, ClassDoc paramClassDoc, MethodWriter paramMethodWriter) {
/*  85 */     super(paramContext);
/*  86 */     this.classDoc = paramClassDoc;
/*  87 */     this.writer = paramMethodWriter;
/*  88 */     this.visibleMemberMap = new VisibleMemberMap(paramClassDoc, 4, this.configuration);
/*     */
/*     */
/*     */
/*  92 */     this
/*  93 */       .methods = new ArrayList<>(this.visibleMemberMap.getLeafClassMembers(this.configuration));
/*     */
/*  95 */     if (this.configuration.getMemberComparator() != null) {
/*  96 */       Collections.sort(this.methods, this.configuration.getMemberComparator());
/*     */     }
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
/*     */   public static MethodBuilder getInstance(Context paramContext, ClassDoc paramClassDoc, MethodWriter paramMethodWriter) {
/* 111 */     return new MethodBuilder(paramContext, paramClassDoc, paramMethodWriter);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public String getName() {
/* 118 */     return "MethodDetails";
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
/*     */   public List<ProgramElementDoc> members(ClassDoc paramClassDoc) {
/* 130 */     return this.visibleMemberMap.getMembersFor(paramClassDoc);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public VisibleMemberMap getVisibleMemberMap() {
/* 139 */     return this.visibleMemberMap;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean hasMembersToDocument() {
/* 146 */     return (this.methods.size() > 0);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildMethodDoc(XMLNode paramXMLNode, Content paramContent) {
/* 156 */     if (this.writer == null) {
/*     */       return;
/*     */     }
/* 159 */     int i = this.methods.size();
/* 160 */     if (i > 0) {
/* 161 */       Content content = this.writer.getMethodDetailsTreeHeader(this.classDoc, paramContent);
/*     */
/* 163 */       for (this.currentMethodIndex = 0; this.currentMethodIndex < i;
/* 164 */         this.currentMethodIndex++) {
/* 165 */         Content content1 = this.writer.getMethodDocTreeHeader((MethodDoc)this.methods
/* 166 */             .get(this.currentMethodIndex), content);
/*     */
/* 168 */         buildChildren(paramXMLNode, content1);
/* 169 */         content.addContent(this.writer.getMethodDoc(content1, (this.currentMethodIndex == i - 1)));
/*     */       }
/*     */
/* 172 */       paramContent.addContent(this.writer
/* 173 */           .getMethodDetails(content));
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildSignature(XMLNode paramXMLNode, Content paramContent) {
/* 184 */     paramContent.addContent(this.writer
/* 185 */         .getSignature((MethodDoc)this.methods.get(this.currentMethodIndex)));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildDeprecationInfo(XMLNode paramXMLNode, Content paramContent) {
/* 195 */     this.writer.addDeprecated((MethodDoc)this.methods
/* 196 */         .get(this.currentMethodIndex), paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildMethodComments(XMLNode paramXMLNode, Content paramContent) {
/* 207 */     if (!this.configuration.nocomment) {
/* 208 */       MethodDoc methodDoc = (MethodDoc)this.methods.get(this.currentMethodIndex);
/*     */
/* 210 */       if ((methodDoc.inlineTags()).length == 0) {
/* 211 */         DocFinder.Output output = DocFinder.search(new DocFinder.Input((ProgramElementDoc)methodDoc));
/*     */
/* 213 */         methodDoc = (output.inlineTags != null && output.inlineTags.length > 0) ? (MethodDoc)output.holder : methodDoc;
/*     */       }
/*     */
/*     */
/*     */
/*     */
/* 219 */       this.writer.addComments((Type)methodDoc.containingClass(), methodDoc, paramContent);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildTagInfo(XMLNode paramXMLNode, Content paramContent) {
/* 230 */     this.writer.addTags((MethodDoc)this.methods.get(this.currentMethodIndex), paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public MethodWriter getWriter() {
/* 240 */     return this.writer;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\builders\MethodBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
