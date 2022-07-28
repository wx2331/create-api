/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeOptionalMemberWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeRequiredMemberWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class AnnotationTypeOptionalMemberBuilder
/*     */   extends AnnotationTypeRequiredMemberBuilder
/*     */ {
/*     */   private AnnotationTypeOptionalMemberBuilder(Context paramContext, ClassDoc paramClassDoc, AnnotationTypeOptionalMemberWriter paramAnnotationTypeOptionalMemberWriter) {
/*  58 */     super(paramContext, paramClassDoc, (AnnotationTypeRequiredMemberWriter)paramAnnotationTypeOptionalMemberWriter, 6);
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
/*     */   public static AnnotationTypeOptionalMemberBuilder getInstance(Context paramContext, ClassDoc paramClassDoc, AnnotationTypeOptionalMemberWriter paramAnnotationTypeOptionalMemberWriter) {
/*  73 */     return new AnnotationTypeOptionalMemberBuilder(paramContext, paramClassDoc, paramAnnotationTypeOptionalMemberWriter);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public String getName() {
/*  82 */     return "AnnotationTypeOptionalMemberDetails";
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildAnnotationTypeOptionalMember(XMLNode paramXMLNode, Content paramContent) {
/*  92 */     buildAnnotationTypeMember(paramXMLNode, paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildDefaultValueInfo(XMLNode paramXMLNode, Content paramContent) {
/* 102 */     ((AnnotationTypeOptionalMemberWriter)this.writer).addDefaultValueInfo((MemberDoc)this.members
/* 103 */         .get(this.currentMemberIndex), paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public AnnotationTypeRequiredMemberWriter getWriter() {
/* 112 */     return this.writer;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\builders\AnnotationTypeOptionalMemberBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
