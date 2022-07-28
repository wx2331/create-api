/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class AnnotationTypeBuilder
/*     */   extends AbstractBuilder
/*     */ {
/*     */   public static final String ROOT = "AnnotationTypeDoc";
/*     */   private final AnnotationTypeDoc annotationTypeDoc;
/*     */   private final AnnotationTypeWriter writer;
/*     */   private Content contentTree;
/*     */
/*     */   private AnnotationTypeBuilder(Context paramContext, AnnotationTypeDoc paramAnnotationTypeDoc, AnnotationTypeWriter paramAnnotationTypeWriter) {
/*  79 */     super(paramContext);
/*  80 */     this.annotationTypeDoc = paramAnnotationTypeDoc;
/*  81 */     this.writer = paramAnnotationTypeWriter;
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
/*     */   public static AnnotationTypeBuilder getInstance(Context paramContext, AnnotationTypeDoc paramAnnotationTypeDoc, AnnotationTypeWriter paramAnnotationTypeWriter) throws Exception {
/*  95 */     return new AnnotationTypeBuilder(paramContext, paramAnnotationTypeDoc, paramAnnotationTypeWriter);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void build() throws IOException {
/* 102 */     build(this.layoutParser.parseXML("AnnotationTypeDoc"), this.contentTree);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public String getName() {
/* 109 */     return "AnnotationTypeDoc";
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildAnnotationTypeDoc(XMLNode paramXMLNode, Content paramContent) throws Exception {
/* 119 */     paramContent = this.writer.getHeader(this.configuration.getText("doclet.AnnotationType") + " " + this.annotationTypeDoc
/* 120 */         .name());
/* 121 */     Content content = this.writer.getAnnotationContentHeader();
/* 122 */     buildChildren(paramXMLNode, content);
/* 123 */     paramContent.addContent(content);
/* 124 */     this.writer.addFooter(paramContent);
/* 125 */     this.writer.printDocument(paramContent);
/* 126 */     this.writer.close();
/* 127 */     copyDocFiles();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   private void copyDocFiles() {
/* 134 */     PackageDoc packageDoc = this.annotationTypeDoc.containingPackage();
/* 135 */     if ((this.configuration.packages == null ||
/* 136 */       Arrays.binarySearch((Object[])this.configuration.packages, packageDoc) < 0) &&
/*     */
/* 138 */       !this.containingPackagesSeen.contains(packageDoc.name())) {
/*     */
/*     */
/*     */
/* 142 */       Util.copyDocFiles(this.configuration, packageDoc);
/* 143 */       this.containingPackagesSeen.add(packageDoc.name());
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildAnnotationTypeInfo(XMLNode paramXMLNode, Content paramContent) {
/* 154 */     Content content = this.writer.getAnnotationInfoTreeHeader();
/* 155 */     buildChildren(paramXMLNode, content);
/* 156 */     paramContent.addContent(this.writer.getAnnotationInfo(content));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildDeprecationInfo(XMLNode paramXMLNode, Content paramContent) {
/* 166 */     this.writer.addAnnotationTypeDeprecationInfo(paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildAnnotationTypeSignature(XMLNode paramXMLNode, Content paramContent) {
/* 177 */     StringBuilder stringBuilder = new StringBuilder(this.annotationTypeDoc.modifiers() + " ");
/* 178 */     this.writer.addAnnotationTypeSignature(Util.replaceText(stringBuilder
/* 179 */           .toString(), "interface", "@interface"), paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildAnnotationTypeDescription(XMLNode paramXMLNode, Content paramContent) {
/* 189 */     this.writer.addAnnotationTypeDescription(paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildAnnotationTypeTagInfo(XMLNode paramXMLNode, Content paramContent) {
/* 199 */     this.writer.addAnnotationTypeTagInfo(paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildMemberSummary(XMLNode paramXMLNode, Content paramContent) throws Exception {
/* 210 */     Content content = this.writer.getMemberTreeHeader();
/* 211 */     this.configuration.getBuilderFactory()
/* 212 */       .getMemberSummaryBuilder(this.writer).buildChildren(paramXMLNode, content);
/* 213 */     paramContent.addContent(this.writer.getMemberSummaryTree(content));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildAnnotationTypeMemberDetails(XMLNode paramXMLNode, Content paramContent) {
/* 223 */     Content content = this.writer.getMemberTreeHeader();
/* 224 */     buildChildren(paramXMLNode, content);
/* 225 */     if (content.isValid()) {
/* 226 */       paramContent.addContent(this.writer.getMemberDetailsTree(content));
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
/*     */   public void buildAnnotationTypeFieldDetails(XMLNode paramXMLNode, Content paramContent) throws Exception {
/* 238 */     this.configuration.getBuilderFactory()
/* 239 */       .getAnnotationTypeFieldsBuilder(this.writer).buildChildren(paramXMLNode, paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildAnnotationTypeOptionalMemberDetails(XMLNode paramXMLNode, Content paramContent) throws Exception {
/* 250 */     this.configuration.getBuilderFactory()
/* 251 */       .getAnnotationTypeOptionalMemberBuilder(this.writer).buildChildren(paramXMLNode, paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildAnnotationTypeRequiredMemberDetails(XMLNode paramXMLNode, Content paramContent) throws Exception {
/* 262 */     this.configuration.getBuilderFactory()
/* 263 */       .getAnnotationTypeRequiredMemberBuilder(this.writer).buildChildren(paramXMLNode, paramContent);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\builders\AnnotationTypeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
