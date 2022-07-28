/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.AnnotationTypeElementDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeOptionalMemberWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.MemberSummaryWriter;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationTypeOptionalMemberWriterImpl
/*     */   extends AnnotationTypeRequiredMemberWriterImpl
/*     */   implements AnnotationTypeOptionalMemberWriter, MemberSummaryWriter
/*     */ {
/*     */   public AnnotationTypeOptionalMemberWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter, AnnotationTypeDoc paramAnnotationTypeDoc) {
/*  57 */     super(paramSubWriterHolderWriter, paramAnnotationTypeDoc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMemberSummaryHeader(ClassDoc paramClassDoc, Content paramContent) {
/*  65 */     paramContent.addContent(HtmlConstants.START_OF_ANNOTATION_TYPE_OPTIONAL_MEMBER_SUMMARY);
/*     */     
/*  67 */     Content content = this.writer.getMemberTreeHeader();
/*  68 */     this.writer.addSummaryHeader(this, paramClassDoc, content);
/*  69 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDefaultValueInfo(MemberDoc paramMemberDoc, Content paramContent) {
/*  76 */     if (((AnnotationTypeElementDoc)paramMemberDoc).defaultValue() != null) {
/*  77 */       HtmlTree htmlTree1 = HtmlTree.DT(this.writer.getResource("doclet.Default"));
/*  78 */       HtmlTree htmlTree2 = HtmlTree.DL((Content)htmlTree1);
/*  79 */       HtmlTree htmlTree3 = HtmlTree.DD((Content)new StringContent(((AnnotationTypeElementDoc)paramMemberDoc)
/*  80 */             .defaultValue().toString()));
/*  81 */       htmlTree2.addContent((Content)htmlTree3);
/*  82 */       paramContent.addContent((Content)htmlTree2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  90 */     this.writer.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSummaryLabel(Content paramContent) {
/*  97 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING, this.writer
/*  98 */         .getResource("doclet.Annotation_Type_Optional_Member_Summary"));
/*  99 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTableSummary() {
/* 106 */     return this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 107 */         .getText("doclet.Annotation_Type_Optional_Member_Summary"), this.configuration
/* 108 */         .getText("doclet.annotation_type_optional_members"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getCaption() {
/* 115 */     return this.configuration.getResource("doclet.Annotation_Type_Optional_Members");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getSummaryTableHeader(ProgramElementDoc paramProgramElementDoc) {
/* 122 */     return new String[] { this.writer
/* 123 */         .getModifierTypeHeader(), this.configuration
/* 124 */         .getText("doclet.0_and_1", this.configuration
/* 125 */           .getText("doclet.Annotation_Type_Optional_Member"), this.configuration
/* 126 */           .getText("doclet.Description")) };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSummaryAnchor(ClassDoc paramClassDoc, Content paramContent) {
/* 135 */     paramContent.addContent(this.writer.getMarkerAnchor(SectionName.ANNOTATION_TYPE_OPTIONAL_ELEMENT_SUMMARY));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavSummaryLink(ClassDoc paramClassDoc, boolean paramBoolean) {
/* 143 */     if (paramBoolean) {
/* 144 */       return this.writer.getHyperLink(SectionName.ANNOTATION_TYPE_OPTIONAL_ELEMENT_SUMMARY, this.writer
/*     */           
/* 146 */           .getResource("doclet.navAnnotationTypeOptionalMember"));
/*     */     }
/* 148 */     return this.writer.getResource("doclet.navAnnotationTypeOptionalMember");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\AnnotationTypeOptionalMemberWriterImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */