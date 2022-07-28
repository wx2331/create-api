/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.EnumConstantWriter;
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
/*     */ public class EnumConstantWriterImpl
/*     */   extends AbstractMemberWriter
/*     */   implements EnumConstantWriter, MemberSummaryWriter
/*     */ {
/*     */   public EnumConstantWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter, ClassDoc paramClassDoc) {
/*  51 */     super(paramSubWriterHolderWriter, paramClassDoc);
/*     */   }
/*     */   
/*     */   public EnumConstantWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter) {
/*  55 */     super(paramSubWriterHolderWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMemberSummaryHeader(ClassDoc paramClassDoc, Content paramContent) {
/*  63 */     paramContent.addContent(HtmlConstants.START_OF_ENUM_CONSTANT_SUMMARY);
/*  64 */     Content content = this.writer.getMemberTreeHeader();
/*  65 */     this.writer.addSummaryHeader(this, paramClassDoc, content);
/*  66 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getEnumConstantsDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent) {
/*  74 */     paramContent.addContent(HtmlConstants.START_OF_ENUM_CONSTANT_DETAILS);
/*  75 */     Content content = this.writer.getMemberTreeHeader();
/*  76 */     content.addContent(this.writer.getMarkerAnchor(SectionName.ENUM_CONSTANT_DETAIL));
/*     */     
/*  78 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.DETAILS_HEADING, this.writer.enumConstantsDetailsLabel);
/*     */     
/*  80 */     content.addContent((Content)htmlTree);
/*  81 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getEnumConstantsTreeHeader(FieldDoc paramFieldDoc, Content paramContent) {
/*  89 */     paramContent.addContent(this.writer
/*  90 */         .getMarkerAnchor(paramFieldDoc.name()));
/*  91 */     Content content = this.writer.getMemberTreeHeader();
/*  92 */     HtmlTree htmlTree = new HtmlTree(HtmlConstants.MEMBER_HEADING);
/*  93 */     htmlTree.addContent(paramFieldDoc.name());
/*  94 */     content.addContent((Content)htmlTree);
/*  95 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getSignature(FieldDoc paramFieldDoc) {
/* 102 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.PRE);
/* 103 */     this.writer.addAnnotationInfo((ProgramElementDoc)paramFieldDoc, (Content)htmlTree);
/* 104 */     addModifiers((MemberDoc)paramFieldDoc, (Content)htmlTree);
/* 105 */     Content content = this.writer.getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.MEMBER, paramFieldDoc
/* 106 */           .type()));
/* 107 */     htmlTree.addContent(content);
/* 108 */     htmlTree.addContent(" ");
/* 109 */     if (this.configuration.linksource) {
/* 110 */       StringContent stringContent = new StringContent(paramFieldDoc.name());
/* 111 */       this.writer.addSrcLink((ProgramElementDoc)paramFieldDoc, (Content)stringContent, (Content)htmlTree);
/*     */     } else {
/* 113 */       addName(paramFieldDoc.name(), (Content)htmlTree);
/*     */     } 
/* 115 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDeprecated(FieldDoc paramFieldDoc, Content paramContent) {
/* 122 */     addDeprecatedInfo((ProgramElementDoc)paramFieldDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComments(FieldDoc paramFieldDoc, Content paramContent) {
/* 129 */     addComment((ProgramElementDoc)paramFieldDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTags(FieldDoc paramFieldDoc, Content paramContent) {
/* 136 */     this.writer.addTagsInfo((Doc)paramFieldDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getEnumConstantsDetails(Content paramContent) {
/* 143 */     return getMemberTree(paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getEnumConstants(Content paramContent, boolean paramBoolean) {
/* 151 */     return getMemberTree(paramContent, paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 158 */     this.writer.close();
/*     */   }
/*     */   
/*     */   public int getMemberKind() {
/* 162 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSummaryLabel(Content paramContent) {
/* 169 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING, this.writer
/* 170 */         .getResource("doclet.Enum_Constant_Summary"));
/* 171 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTableSummary() {
/* 178 */     return this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 179 */         .getText("doclet.Enum_Constant_Summary"), this.configuration
/* 180 */         .getText("doclet.enum_constants"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getCaption() {
/* 187 */     return this.configuration.getResource("doclet.Enum_Constants");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getSummaryTableHeader(ProgramElementDoc paramProgramElementDoc) {
/* 194 */     return new String[] { this.configuration
/* 195 */         .getText("doclet.0_and_1", this.configuration
/* 196 */           .getText("doclet.Enum_Constant"), this.configuration
/* 197 */           .getText("doclet.Description")) };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSummaryAnchor(ClassDoc paramClassDoc, Content paramContent) {
/* 206 */     paramContent.addContent(this.writer.getMarkerAnchor(SectionName.ENUM_CONSTANT_SUMMARY));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInheritedSummaryAnchor(ClassDoc paramClassDoc, Content paramContent) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInheritedSummaryLabel(ClassDoc paramClassDoc, Content paramContent) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addSummaryLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 227 */     HtmlTree htmlTree1 = HtmlTree.SPAN(HtmlStyle.memberNameLink, this.writer
/* 228 */         .getDocLink(paramKind, (MemberDoc)paramProgramElementDoc, paramProgramElementDoc.name(), false));
/* 229 */     HtmlTree htmlTree2 = HtmlTree.CODE((Content)htmlTree1);
/* 230 */     paramContent.addContent((Content)htmlTree2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSummaryColumnStyle(HtmlTree paramHtmlTree) {
/* 238 */     paramHtmlTree.addStyle(HtmlStyle.colOne);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addInheritedSummaryLink(ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addSummaryType(ProgramElementDoc paramProgramElementDoc, Content paramContent) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getDeprecatedLink(ProgramElementDoc paramProgramElementDoc) {
/* 259 */     return this.writer.getDocLink(LinkInfoImpl.Kind.MEMBER, (MemberDoc)paramProgramElementDoc, ((FieldDoc)paramProgramElementDoc)
/* 260 */         .qualifiedName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavSummaryLink(ClassDoc paramClassDoc, boolean paramBoolean) {
/* 267 */     if (paramBoolean) {
/* 268 */       if (paramClassDoc == null) {
/* 269 */         return this.writer.getHyperLink(SectionName.ENUM_CONSTANT_SUMMARY, this.writer
/* 270 */             .getResource("doclet.navEnum"));
/*     */       }
/* 272 */       return this.writer.getHyperLink(SectionName.ENUM_CONSTANTS_INHERITANCE, this.configuration
/*     */           
/* 274 */           .getClassName(paramClassDoc), this.writer.getResource("doclet.navEnum"));
/*     */     } 
/*     */     
/* 277 */     return this.writer.getResource("doclet.navEnum");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addNavDetailLink(boolean paramBoolean, Content paramContent) {
/* 285 */     if (paramBoolean) {
/* 286 */       paramContent.addContent(this.writer.getHyperLink(SectionName.ENUM_CONSTANT_DETAIL, this.writer
/*     */             
/* 288 */             .getResource("doclet.navEnum")));
/*     */     } else {
/* 290 */       paramContent.addContent(this.writer.getResource("doclet.navEnum"));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\EnumConstantWriterImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */