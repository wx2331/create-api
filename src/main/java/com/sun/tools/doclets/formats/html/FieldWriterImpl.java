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
/*     */ import com.sun.tools.doclets.internal.toolkit.FieldWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.MemberSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
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
/*     */ public class FieldWriterImpl
/*     */   extends AbstractMemberWriter
/*     */   implements FieldWriter, MemberSummaryWriter
/*     */ {
/*     */   public FieldWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter, ClassDoc paramClassDoc) {
/*  52 */     super(paramSubWriterHolderWriter, paramClassDoc);
/*     */   }
/*     */   
/*     */   public FieldWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter) {
/*  56 */     super(paramSubWriterHolderWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMemberSummaryHeader(ClassDoc paramClassDoc, Content paramContent) {
/*  64 */     paramContent.addContent(HtmlConstants.START_OF_FIELD_SUMMARY);
/*  65 */     Content content = this.writer.getMemberTreeHeader();
/*  66 */     this.writer.addSummaryHeader(this, paramClassDoc, content);
/*  67 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getFieldDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent) {
/*  75 */     paramContent.addContent(HtmlConstants.START_OF_FIELD_DETAILS);
/*  76 */     Content content = this.writer.getMemberTreeHeader();
/*  77 */     content.addContent(this.writer.getMarkerAnchor(SectionName.FIELD_DETAIL));
/*     */     
/*  79 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.DETAILS_HEADING, this.writer.fieldDetailsLabel);
/*     */     
/*  81 */     content.addContent((Content)htmlTree);
/*  82 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getFieldDocTreeHeader(FieldDoc paramFieldDoc, Content paramContent) {
/*  90 */     paramContent.addContent(this.writer
/*  91 */         .getMarkerAnchor(paramFieldDoc.name()));
/*  92 */     Content content = this.writer.getMemberTreeHeader();
/*  93 */     HtmlTree htmlTree = new HtmlTree(HtmlConstants.MEMBER_HEADING);
/*  94 */     htmlTree.addContent(paramFieldDoc.name());
/*  95 */     content.addContent((Content)htmlTree);
/*  96 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getSignature(FieldDoc paramFieldDoc) {
/* 103 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.PRE);
/* 104 */     this.writer.addAnnotationInfo((ProgramElementDoc)paramFieldDoc, (Content)htmlTree);
/* 105 */     addModifiers((MemberDoc)paramFieldDoc, (Content)htmlTree);
/* 106 */     Content content = this.writer.getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.MEMBER, paramFieldDoc
/* 107 */           .type()));
/* 108 */     htmlTree.addContent(content);
/* 109 */     htmlTree.addContent(" ");
/* 110 */     if (this.configuration.linksource) {
/* 111 */       StringContent stringContent = new StringContent(paramFieldDoc.name());
/* 112 */       this.writer.addSrcLink((ProgramElementDoc)paramFieldDoc, (Content)stringContent, (Content)htmlTree);
/*     */     } else {
/* 114 */       addName(paramFieldDoc.name(), (Content)htmlTree);
/*     */     } 
/* 116 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDeprecated(FieldDoc paramFieldDoc, Content paramContent) {
/* 123 */     addDeprecatedInfo((ProgramElementDoc)paramFieldDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComments(FieldDoc paramFieldDoc, Content paramContent) {
/* 130 */     ClassDoc classDoc = paramFieldDoc.containingClass();
/* 131 */     if ((paramFieldDoc.inlineTags()).length > 0) {
/* 132 */       if (classDoc.equals(this.classdoc) || (
/* 133 */         !classDoc.isPublic() && !Util.isLinkable(classDoc, this.configuration))) {
/* 134 */         this.writer.addInlineComment((Doc)paramFieldDoc, paramContent);
/*     */       } else {
/*     */         
/* 137 */         Content content = this.writer.getDocLink(LinkInfoImpl.Kind.FIELD_DOC_COPY, classDoc, (MemberDoc)paramFieldDoc, 
/*     */             
/* 139 */             classDoc.isIncluded() ? classDoc
/* 140 */             .typeName() : classDoc.qualifiedTypeName(), false);
/*     */         
/* 142 */         HtmlTree htmlTree1 = HtmlTree.CODE(content);
/* 143 */         HtmlTree htmlTree2 = HtmlTree.SPAN(HtmlStyle.descfrmTypeLabel, classDoc.isClass() ? this.writer.descfrmClassLabel : this.writer.descfrmInterfaceLabel);
/*     */         
/* 145 */         htmlTree2.addContent(this.writer.getSpace());
/* 146 */         htmlTree2.addContent((Content)htmlTree1);
/* 147 */         paramContent.addContent((Content)HtmlTree.DIV(HtmlStyle.block, (Content)htmlTree2));
/* 148 */         this.writer.addInlineComment((Doc)paramFieldDoc, paramContent);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTags(FieldDoc paramFieldDoc, Content paramContent) {
/* 157 */     this.writer.addTagsInfo((Doc)paramFieldDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getFieldDetails(Content paramContent) {
/* 164 */     return getMemberTree(paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getFieldDoc(Content paramContent, boolean paramBoolean) {
/* 172 */     return getMemberTree(paramContent, paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 179 */     this.writer.close();
/*     */   }
/*     */   
/*     */   public int getMemberKind() {
/* 183 */     return 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSummaryLabel(Content paramContent) {
/* 190 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING, this.writer
/* 191 */         .getResource("doclet.Field_Summary"));
/* 192 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTableSummary() {
/* 199 */     return this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 200 */         .getText("doclet.Field_Summary"), this.configuration
/* 201 */         .getText("doclet.fields"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getCaption() {
/* 208 */     return this.configuration.getResource("doclet.Fields");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getSummaryTableHeader(ProgramElementDoc paramProgramElementDoc) {
/* 215 */     return new String[] { this.writer
/* 216 */         .getModifierTypeHeader(), this.configuration
/* 217 */         .getText("doclet.0_and_1", this.configuration
/* 218 */           .getText("doclet.Field"), this.configuration
/* 219 */           .getText("doclet.Description")) };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSummaryAnchor(ClassDoc paramClassDoc, Content paramContent) {
/* 228 */     paramContent.addContent(this.writer.getMarkerAnchor(SectionName.FIELD_SUMMARY));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInheritedSummaryAnchor(ClassDoc paramClassDoc, Content paramContent) {
/* 236 */     paramContent.addContent(this.writer.getMarkerAnchor(SectionName.FIELDS_INHERITANCE, this.configuration
/* 237 */           .getClassName(paramClassDoc)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInheritedSummaryLabel(ClassDoc paramClassDoc, Content paramContent) {
/* 244 */     Content content = this.writer.getPreQualifiedClassLink(LinkInfoImpl.Kind.MEMBER, paramClassDoc, false);
/*     */ 
/*     */ 
/*     */     
/* 248 */     StringContent stringContent = new StringContent(paramClassDoc.isClass() ? this.configuration.getText("doclet.Fields_Inherited_From_Class") : this.configuration.getText("doclet.Fields_Inherited_From_Interface"));
/* 249 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.INHERITED_SUMMARY_HEADING, (Content)stringContent);
/*     */     
/* 251 */     htmlTree.addContent(this.writer.getSpace());
/* 252 */     htmlTree.addContent(content);
/* 253 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addSummaryLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 261 */     HtmlTree htmlTree1 = HtmlTree.SPAN(HtmlStyle.memberNameLink, this.writer
/* 262 */         .getDocLink(paramKind, paramClassDoc, (MemberDoc)paramProgramElementDoc, paramProgramElementDoc.name(), false));
/* 263 */     HtmlTree htmlTree2 = HtmlTree.CODE((Content)htmlTree1);
/* 264 */     paramContent.addContent((Content)htmlTree2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addInheritedSummaryLink(ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 272 */     paramContent.addContent(this.writer
/* 273 */         .getDocLink(LinkInfoImpl.Kind.MEMBER, paramClassDoc, (MemberDoc)paramProgramElementDoc, paramProgramElementDoc
/* 274 */           .name(), false));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addSummaryType(ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 281 */     FieldDoc fieldDoc = (FieldDoc)paramProgramElementDoc;
/* 282 */     addModifierAndType((ProgramElementDoc)fieldDoc, fieldDoc.type(), paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getDeprecatedLink(ProgramElementDoc paramProgramElementDoc) {
/* 289 */     return this.writer.getDocLink(LinkInfoImpl.Kind.MEMBER, (MemberDoc)paramProgramElementDoc, ((FieldDoc)paramProgramElementDoc)
/* 290 */         .qualifiedName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavSummaryLink(ClassDoc paramClassDoc, boolean paramBoolean) {
/* 297 */     if (paramBoolean) {
/* 298 */       if (paramClassDoc == null) {
/* 299 */         return this.writer.getHyperLink(SectionName.FIELD_SUMMARY, this.writer
/*     */             
/* 301 */             .getResource("doclet.navField"));
/*     */       }
/* 303 */       return this.writer.getHyperLink(SectionName.FIELDS_INHERITANCE, this.configuration
/*     */           
/* 305 */           .getClassName(paramClassDoc), this.writer.getResource("doclet.navField"));
/*     */     } 
/*     */     
/* 308 */     return this.writer.getResource("doclet.navField");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addNavDetailLink(boolean paramBoolean, Content paramContent) {
/* 316 */     if (paramBoolean) {
/* 317 */       paramContent.addContent(this.writer.getHyperLink(SectionName.FIELD_DETAIL, this.writer
/*     */             
/* 319 */             .getResource("doclet.navField")));
/*     */     } else {
/* 321 */       paramContent.addContent(this.writer.getResource("doclet.navField"));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\FieldWriterImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */