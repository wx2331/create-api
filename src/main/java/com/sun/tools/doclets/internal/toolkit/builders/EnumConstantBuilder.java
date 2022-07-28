/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.EnumConstantWriter;
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
/*     */ public class EnumConstantBuilder
/*     */   extends AbstractMemberBuilder
/*     */ {
/*     */   private final ClassDoc classDoc;
/*     */   private final VisibleMemberMap visibleMemberMap;
/*     */   private final EnumConstantWriter writer;
/*     */   private final List<ProgramElementDoc> enumConstants;
/*     */   private int currentEnumConstantsIndex;
/*     */
/*     */   private EnumConstantBuilder(Context paramContext, ClassDoc paramClassDoc, EnumConstantWriter paramEnumConstantWriter) {
/*  83 */     super(paramContext);
/*  84 */     this.classDoc = paramClassDoc;
/*  85 */     this.writer = paramEnumConstantWriter;
/*  86 */     this.visibleMemberMap = new VisibleMemberMap(paramClassDoc, 1, this.configuration);
/*     */
/*     */
/*     */
/*     */
/*  91 */     this
/*  92 */       .enumConstants = new ArrayList<>(this.visibleMemberMap.getMembersFor(paramClassDoc));
/*  93 */     if (this.configuration.getMemberComparator() != null) {
/*  94 */       Collections.sort(this.enumConstants, this.configuration.getMemberComparator());
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
/*     */   public static EnumConstantBuilder getInstance(Context paramContext, ClassDoc paramClassDoc, EnumConstantWriter paramEnumConstantWriter) {
/* 107 */     return new EnumConstantBuilder(paramContext, paramClassDoc, paramEnumConstantWriter);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public String getName() {
/* 114 */     return "EnumConstantDetails";
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
/* 126 */     return this.visibleMemberMap.getMembersFor(paramClassDoc);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public VisibleMemberMap getVisibleMemberMap() {
/* 135 */     return this.visibleMemberMap;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean hasMembersToDocument() {
/* 142 */     return (this.enumConstants.size() > 0);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildEnumConstant(XMLNode paramXMLNode, Content paramContent) {
/* 152 */     if (this.writer == null) {
/*     */       return;
/*     */     }
/* 155 */     int i = this.enumConstants.size();
/* 156 */     if (i > 0) {
/* 157 */       Content content = this.writer.getEnumConstantsDetailsTreeHeader(this.classDoc, paramContent);
/*     */
/* 159 */       for (this.currentEnumConstantsIndex = 0; this.currentEnumConstantsIndex < i;
/* 160 */         this.currentEnumConstantsIndex++) {
/* 161 */         Content content1 = this.writer.getEnumConstantsTreeHeader((FieldDoc)this.enumConstants
/* 162 */             .get(this.currentEnumConstantsIndex), content);
/*     */
/* 164 */         buildChildren(paramXMLNode, content1);
/* 165 */         content.addContent(this.writer.getEnumConstants(content1, (this.currentEnumConstantsIndex == i - 1)));
/*     */       }
/*     */
/* 168 */       paramContent.addContent(this.writer
/* 169 */           .getEnumConstantsDetails(content));
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
/* 180 */     paramContent.addContent(this.writer.getSignature((FieldDoc)this.enumConstants
/* 181 */           .get(this.currentEnumConstantsIndex)));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildDeprecationInfo(XMLNode paramXMLNode, Content paramContent) {
/* 191 */     this.writer.addDeprecated((FieldDoc)this.enumConstants
/* 192 */         .get(this.currentEnumConstantsIndex), paramContent);
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
/*     */   public void buildEnumConstantComments(XMLNode paramXMLNode, Content paramContent) {
/* 204 */     if (!this.configuration.nocomment) {
/* 205 */       this.writer.addComments((FieldDoc)this.enumConstants
/* 206 */           .get(this.currentEnumConstantsIndex), paramContent);
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
/*     */   public void buildTagInfo(XMLNode paramXMLNode, Content paramContent) {
/* 218 */     this.writer.addTags((FieldDoc)this.enumConstants
/* 219 */         .get(this.currentEnumConstantsIndex), paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public EnumConstantWriter getWriter() {
/* 229 */     return this.writer;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\builders\EnumConstantBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
