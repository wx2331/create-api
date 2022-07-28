/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
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
/*     */ public class NestedClassWriterImpl
/*     */   extends AbstractMemberWriter
/*     */   implements MemberSummaryWriter
/*     */ {
/*     */   public NestedClassWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter, ClassDoc paramClassDoc) {
/*  53 */     super(paramSubWriterHolderWriter, paramClassDoc);
/*     */   }
/*     */   
/*     */   public NestedClassWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter) {
/*  57 */     super(paramSubWriterHolderWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMemberSummaryHeader(ClassDoc paramClassDoc, Content paramContent) {
/*  65 */     paramContent.addContent(HtmlConstants.START_OF_NESTED_CLASS_SUMMARY);
/*  66 */     Content content = this.writer.getMemberTreeHeader();
/*  67 */     this.writer.addSummaryHeader(this, paramClassDoc, content);
/*  68 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  75 */     this.writer.close();
/*     */   }
/*     */   
/*     */   public int getMemberKind() {
/*  79 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSummaryLabel(Content paramContent) {
/*  86 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING, this.writer
/*  87 */         .getResource("doclet.Nested_Class_Summary"));
/*  88 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTableSummary() {
/*  95 */     return this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/*  96 */         .getText("doclet.Nested_Class_Summary"), this.configuration
/*  97 */         .getText("doclet.nested_classes"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getCaption() {
/* 104 */     return this.configuration.getResource("doclet.Nested_Classes");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getSummaryTableHeader(ProgramElementDoc paramProgramElementDoc) {
/*     */     String[] arrayOfString;
/* 112 */     if (paramProgramElementDoc.isInterface()) {
/*     */ 
/*     */       
/* 115 */       arrayOfString = new String[] { this.writer.getModifierTypeHeader(), this.configuration.getText("doclet.0_and_1", this.configuration
/* 116 */             .getText("doclet.Interface"), this.configuration
/* 117 */             .getText("doclet.Description")) };
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 123 */       arrayOfString = new String[] { this.writer.getModifierTypeHeader(), this.configuration.getText("doclet.0_and_1", this.configuration
/* 124 */             .getText("doclet.Class"), this.configuration
/* 125 */             .getText("doclet.Description")) };
/*     */     } 
/*     */     
/* 128 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSummaryAnchor(ClassDoc paramClassDoc, Content paramContent) {
/* 135 */     paramContent.addContent(this.writer.getMarkerAnchor(SectionName.NESTED_CLASS_SUMMARY));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInheritedSummaryAnchor(ClassDoc paramClassDoc, Content paramContent) {
/* 143 */     paramContent.addContent(this.writer.getMarkerAnchor(SectionName.NESTED_CLASSES_INHERITANCE, paramClassDoc
/*     */           
/* 145 */           .qualifiedName()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInheritedSummaryLabel(ClassDoc paramClassDoc, Content paramContent) {
/* 152 */     Content content = this.writer.getPreQualifiedClassLink(LinkInfoImpl.Kind.MEMBER, paramClassDoc, false);
/*     */ 
/*     */ 
/*     */     
/* 156 */     StringContent stringContent = new StringContent(paramClassDoc.isInterface() ? this.configuration.getText("doclet.Nested_Classes_Interface_Inherited_From_Interface") : this.configuration.getText("doclet.Nested_Classes_Interfaces_Inherited_From_Class"));
/* 157 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.INHERITED_SUMMARY_HEADING, (Content)stringContent);
/*     */     
/* 159 */     htmlTree.addContent(this.writer.getSpace());
/* 160 */     htmlTree.addContent(content);
/* 161 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addSummaryLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 169 */     HtmlTree htmlTree1 = HtmlTree.SPAN(HtmlStyle.memberNameLink, this.writer
/* 170 */         .getLink(new LinkInfoImpl(this.configuration, paramKind, (ClassDoc)paramProgramElementDoc)));
/* 171 */     HtmlTree htmlTree2 = HtmlTree.CODE((Content)htmlTree1);
/* 172 */     paramContent.addContent((Content)htmlTree2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addInheritedSummaryLink(ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 180 */     paramContent.addContent(this.writer
/* 181 */         .getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.MEMBER, (ClassDoc)paramProgramElementDoc)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addSummaryType(ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 190 */     ClassDoc classDoc = (ClassDoc)paramProgramElementDoc;
/* 191 */     addModifierAndType((ProgramElementDoc)classDoc, null, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getDeprecatedLink(ProgramElementDoc paramProgramElementDoc) {
/* 198 */     return this.writer.getQualifiedClassLink(LinkInfoImpl.Kind.MEMBER, (ClassDoc)paramProgramElementDoc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavSummaryLink(ClassDoc paramClassDoc, boolean paramBoolean) {
/* 206 */     if (paramBoolean) {
/* 207 */       if (paramClassDoc == null) {
/* 208 */         return this.writer.getHyperLink(SectionName.NESTED_CLASS_SUMMARY, this.writer
/*     */             
/* 210 */             .getResource("doclet.navNested"));
/*     */       }
/* 212 */       return this.writer.getHyperLink(SectionName.NESTED_CLASSES_INHERITANCE, paramClassDoc
/*     */           
/* 214 */           .qualifiedName(), this.writer.getResource("doclet.navNested"));
/*     */     } 
/*     */     
/* 217 */     return this.writer.getResource("doclet.navNested");
/*     */   }
/*     */   
/*     */   protected void addNavDetailLink(boolean paramBoolean, Content paramContent) {}
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\NestedClassWriterImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */