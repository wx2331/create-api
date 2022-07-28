/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeFieldWriter;
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
/*     */ public class AnnotationTypeFieldWriterImpl
/*     */   extends AbstractMemberWriter
/*     */   implements AnnotationTypeFieldWriter, MemberSummaryWriter
/*     */ {
/*     */   public AnnotationTypeFieldWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter, AnnotationTypeDoc paramAnnotationTypeDoc) {
/*  55 */     super(paramSubWriterHolderWriter, (ClassDoc)paramAnnotationTypeDoc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMemberSummaryHeader(ClassDoc paramClassDoc, Content paramContent) {
/*  63 */     paramContent.addContent(HtmlConstants.START_OF_ANNOTATION_TYPE_FIELD_SUMMARY);
/*     */     
/*  65 */     Content content = this.writer.getMemberTreeHeader();
/*  66 */     this.writer.addSummaryHeader(this, paramClassDoc, content);
/*  67 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMemberTreeHeader() {
/*  74 */     return this.writer.getMemberTreeHeader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAnnotationFieldDetailsMarker(Content paramContent) {
/*  81 */     paramContent.addContent(HtmlConstants.START_OF_ANNOTATION_TYPE_FIELD_DETAILS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAnnotationDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent) {
/*  89 */     if (!this.writer.printedAnnotationFieldHeading) {
/*  90 */       paramContent.addContent(this.writer.getMarkerAnchor(SectionName.ANNOTATION_TYPE_FIELD_DETAIL));
/*     */       
/*  92 */       HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.DETAILS_HEADING, this.writer.fieldDetailsLabel);
/*     */       
/*  94 */       paramContent.addContent((Content)htmlTree);
/*  95 */       this.writer.printedAnnotationFieldHeading = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getAnnotationDocTreeHeader(MemberDoc paramMemberDoc, Content paramContent) {
/* 104 */     paramContent.addContent(this.writer
/* 105 */         .getMarkerAnchor(paramMemberDoc.name()));
/* 106 */     Content content = this.writer.getMemberTreeHeader();
/* 107 */     HtmlTree htmlTree = new HtmlTree(HtmlConstants.MEMBER_HEADING);
/* 108 */     htmlTree.addContent(paramMemberDoc.name());
/* 109 */     content.addContent((Content)htmlTree);
/* 110 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getSignature(MemberDoc paramMemberDoc) {
/* 117 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.PRE);
/* 118 */     this.writer.addAnnotationInfo((ProgramElementDoc)paramMemberDoc, (Content)htmlTree);
/* 119 */     addModifiers(paramMemberDoc, (Content)htmlTree);
/*     */     
/* 121 */     Content content = this.writer.getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.MEMBER, 
/* 122 */           getType(paramMemberDoc)));
/* 123 */     htmlTree.addContent(content);
/* 124 */     htmlTree.addContent(this.writer.getSpace());
/* 125 */     if (this.configuration.linksource) {
/* 126 */       StringContent stringContent = new StringContent(paramMemberDoc.name());
/* 127 */       this.writer.addSrcLink((ProgramElementDoc)paramMemberDoc, (Content)stringContent, (Content)htmlTree);
/*     */     } else {
/* 129 */       addName(paramMemberDoc.name(), (Content)htmlTree);
/*     */     } 
/* 131 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDeprecated(MemberDoc paramMemberDoc, Content paramContent) {
/* 138 */     addDeprecatedInfo((ProgramElementDoc)paramMemberDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComments(MemberDoc paramMemberDoc, Content paramContent) {
/* 145 */     addComment((ProgramElementDoc)paramMemberDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTags(MemberDoc paramMemberDoc, Content paramContent) {
/* 152 */     this.writer.addTagsInfo((Doc)paramMemberDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getAnnotationDetails(Content paramContent) {
/* 159 */     return getMemberTree(paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getAnnotationDoc(Content paramContent, boolean paramBoolean) {
/* 167 */     return getMemberTree(paramContent, paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 174 */     this.writer.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSummaryLabel(Content paramContent) {
/* 181 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING, this.writer
/* 182 */         .getResource("doclet.Field_Summary"));
/* 183 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTableSummary() {
/* 190 */     return this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 191 */         .getText("doclet.Field_Summary"), this.configuration
/* 192 */         .getText("doclet.fields"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getCaption() {
/* 199 */     return this.configuration.getResource("doclet.Fields");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getSummaryTableHeader(ProgramElementDoc paramProgramElementDoc) {
/* 206 */     return new String[] { this.writer
/* 207 */         .getModifierTypeHeader(), this.configuration
/* 208 */         .getText("doclet.0_and_1", this.configuration
/* 209 */           .getText("doclet.Fields"), this.configuration
/* 210 */           .getText("doclet.Description")) };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSummaryAnchor(ClassDoc paramClassDoc, Content paramContent) {
/* 219 */     paramContent.addContent(this.writer.getMarkerAnchor(SectionName.ANNOTATION_TYPE_FIELD_SUMMARY));
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
/* 240 */     HtmlTree htmlTree1 = HtmlTree.SPAN(HtmlStyle.memberNameLink, this.writer
/* 241 */         .getDocLink(paramKind, (MemberDoc)paramProgramElementDoc, paramProgramElementDoc.name(), false));
/* 242 */     HtmlTree htmlTree2 = HtmlTree.CODE((Content)htmlTree1);
/* 243 */     paramContent.addContent((Content)htmlTree2);
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
/*     */   protected void addSummaryType(ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 258 */     MemberDoc memberDoc = (MemberDoc)paramProgramElementDoc;
/* 259 */     addModifierAndType((ProgramElementDoc)memberDoc, getType(memberDoc), paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getDeprecatedLink(ProgramElementDoc paramProgramElementDoc) {
/* 266 */     return this.writer.getDocLink(LinkInfoImpl.Kind.MEMBER, (MemberDoc)paramProgramElementDoc, ((MemberDoc)paramProgramElementDoc)
/* 267 */         .qualifiedName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavSummaryLink(ClassDoc paramClassDoc, boolean paramBoolean) {
/* 274 */     if (paramBoolean) {
/* 275 */       return this.writer.getHyperLink(SectionName.ANNOTATION_TYPE_FIELD_SUMMARY, this.writer
/*     */           
/* 277 */           .getResource("doclet.navField"));
/*     */     }
/* 279 */     return this.writer.getResource("doclet.navField");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addNavDetailLink(boolean paramBoolean, Content paramContent) {
/* 287 */     if (paramBoolean) {
/* 288 */       paramContent.addContent(this.writer.getHyperLink(SectionName.ANNOTATION_TYPE_FIELD_DETAIL, this.writer
/*     */             
/* 290 */             .getResource("doclet.navField")));
/*     */     } else {
/* 292 */       paramContent.addContent(this.writer.getResource("doclet.navField"));
/*     */     } 
/*     */   }
/*     */   
/*     */   private Type getType(MemberDoc paramMemberDoc) {
/* 297 */     if (paramMemberDoc instanceof FieldDoc) {
/* 298 */       return ((FieldDoc)paramMemberDoc).type();
/*     */     }
/* 300 */     return ((MethodDoc)paramMemberDoc).returnType();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\AnnotationTypeFieldWriterImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */