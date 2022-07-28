/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.MemberSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.PropertyWriter;
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
/*     */ public class PropertyWriterImpl
/*     */   extends AbstractMemberWriter
/*     */   implements PropertyWriter, MemberSummaryWriter
/*     */ {
/*     */   public PropertyWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter, ClassDoc paramClassDoc) {
/*  52 */     super(paramSubWriterHolderWriter, paramClassDoc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMemberSummaryHeader(ClassDoc paramClassDoc, Content paramContent) {
/*  60 */     paramContent.addContent(HtmlConstants.START_OF_PROPERTY_SUMMARY);
/*  61 */     Content content = this.writer.getMemberTreeHeader();
/*  62 */     this.writer.addSummaryHeader(this, paramClassDoc, content);
/*  63 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getPropertyDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent) {
/*  71 */     paramContent.addContent(HtmlConstants.START_OF_PROPERTY_DETAILS);
/*  72 */     Content content = this.writer.getMemberTreeHeader();
/*  73 */     content.addContent(this.writer.getMarkerAnchor(SectionName.PROPERTY_DETAIL));
/*     */     
/*  75 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.DETAILS_HEADING, this.writer.propertyDetailsLabel);
/*     */     
/*  77 */     content.addContent((Content)htmlTree);
/*  78 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getPropertyDocTreeHeader(MethodDoc paramMethodDoc, Content paramContent) {
/*  86 */     paramContent.addContent(this.writer
/*  87 */         .getMarkerAnchor(paramMethodDoc.name()));
/*  88 */     Content content = this.writer.getMemberTreeHeader();
/*  89 */     HtmlTree htmlTree = new HtmlTree(HtmlConstants.MEMBER_HEADING);
/*  90 */     htmlTree.addContent(paramMethodDoc.name().substring(0, paramMethodDoc.name().lastIndexOf("Property")));
/*  91 */     content.addContent((Content)htmlTree);
/*  92 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getSignature(MethodDoc paramMethodDoc) {
/*  99 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.PRE);
/* 100 */     this.writer.addAnnotationInfo((ProgramElementDoc)paramMethodDoc, (Content)htmlTree);
/* 101 */     addModifiers((MemberDoc)paramMethodDoc, (Content)htmlTree);
/* 102 */     Content content = this.writer.getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.MEMBER, paramMethodDoc
/*     */           
/* 104 */           .returnType()));
/* 105 */     htmlTree.addContent(content);
/* 106 */     htmlTree.addContent(" ");
/* 107 */     if (this.configuration.linksource) {
/* 108 */       StringContent stringContent = new StringContent(paramMethodDoc.name());
/* 109 */       this.writer.addSrcLink((ProgramElementDoc)paramMethodDoc, (Content)stringContent, (Content)htmlTree);
/*     */     } else {
/* 111 */       addName(paramMethodDoc.name(), (Content)htmlTree);
/*     */     } 
/* 113 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDeprecated(MethodDoc paramMethodDoc, Content paramContent) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComments(MethodDoc paramMethodDoc, Content paramContent) {
/* 126 */     ClassDoc classDoc = paramMethodDoc.containingClass();
/* 127 */     if ((paramMethodDoc.inlineTags()).length > 0) {
/* 128 */       if (classDoc.equals(this.classdoc) || (
/* 129 */         !classDoc.isPublic() && !Util.isLinkable(classDoc, this.configuration))) {
/* 130 */         this.writer.addInlineComment((Doc)paramMethodDoc, paramContent);
/*     */       } else {
/*     */         
/* 133 */         Content content = this.writer.getDocLink(LinkInfoImpl.Kind.PROPERTY_DOC_COPY, classDoc, (MemberDoc)paramMethodDoc, 
/*     */             
/* 135 */             classDoc.isIncluded() ? classDoc
/* 136 */             .typeName() : classDoc.qualifiedTypeName(), false);
/*     */         
/* 138 */         HtmlTree htmlTree1 = HtmlTree.CODE(content);
/* 139 */         HtmlTree htmlTree2 = HtmlTree.SPAN(HtmlStyle.descfrmTypeLabel, classDoc.isClass() ? this.writer.descfrmClassLabel : this.writer.descfrmInterfaceLabel);
/*     */         
/* 141 */         htmlTree2.addContent(this.writer.getSpace());
/* 142 */         htmlTree2.addContent((Content)htmlTree1);
/* 143 */         paramContent.addContent((Content)HtmlTree.DIV(HtmlStyle.block, (Content)htmlTree2));
/* 144 */         this.writer.addInlineComment((Doc)paramMethodDoc, paramContent);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTags(MethodDoc paramMethodDoc, Content paramContent) {
/* 153 */     this.writer.addTagsInfo((Doc)paramMethodDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getPropertyDetails(Content paramContent) {
/* 160 */     return getMemberTree(paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getPropertyDoc(Content paramContent, boolean paramBoolean) {
/* 168 */     return getMemberTree(paramContent, paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 175 */     this.writer.close();
/*     */   }
/*     */   
/*     */   public int getMemberKind() {
/* 179 */     return 8;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSummaryLabel(Content paramContent) {
/* 186 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING, this.writer
/* 187 */         .getResource("doclet.Property_Summary"));
/* 188 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTableSummary() {
/* 195 */     return this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 196 */         .getText("doclet.Property_Summary"), this.configuration
/* 197 */         .getText("doclet.properties"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getCaption() {
/* 204 */     return this.configuration.getResource("doclet.Properties");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getSummaryTableHeader(ProgramElementDoc paramProgramElementDoc) {
/* 211 */     return new String[] { this.configuration
/* 212 */         .getText("doclet.Type"), this.configuration
/* 213 */         .getText("doclet.0_and_1", this.configuration
/* 214 */           .getText("doclet.Property"), this.configuration
/* 215 */           .getText("doclet.Description")) };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSummaryAnchor(ClassDoc paramClassDoc, Content paramContent) {
/* 224 */     paramContent.addContent(this.writer.getMarkerAnchor(SectionName.PROPERTY_SUMMARY));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInheritedSummaryAnchor(ClassDoc paramClassDoc, Content paramContent) {
/* 232 */     paramContent.addContent(this.writer.getMarkerAnchor(SectionName.PROPERTIES_INHERITANCE, this.configuration
/*     */           
/* 234 */           .getClassName(paramClassDoc)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInheritedSummaryLabel(ClassDoc paramClassDoc, Content paramContent) {
/* 241 */     Content content = this.writer.getPreQualifiedClassLink(LinkInfoImpl.Kind.MEMBER, paramClassDoc, false);
/*     */ 
/*     */ 
/*     */     
/* 245 */     StringContent stringContent = new StringContent(paramClassDoc.isClass() ? this.configuration.getText("doclet.Properties_Inherited_From_Class") : this.configuration.getText("doclet.Properties_Inherited_From_Interface"));
/* 246 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.INHERITED_SUMMARY_HEADING, (Content)stringContent);
/*     */     
/* 248 */     htmlTree.addContent(this.writer.getSpace());
/* 249 */     htmlTree.addContent(content);
/* 250 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addSummaryLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 258 */     HtmlTree htmlTree1 = HtmlTree.SPAN(HtmlStyle.memberNameLink, this.writer
/* 259 */         .getDocLink(paramKind, paramClassDoc, (MemberDoc)paramProgramElementDoc, paramProgramElementDoc
/*     */           
/* 261 */           .name().substring(0, paramProgramElementDoc.name().lastIndexOf("Property")), false, true));
/*     */ 
/*     */ 
/*     */     
/* 265 */     HtmlTree htmlTree2 = HtmlTree.CODE((Content)htmlTree1);
/* 266 */     paramContent.addContent((Content)htmlTree2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addInheritedSummaryLink(ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 274 */     paramContent.addContent(this.writer
/* 275 */         .getDocLink(LinkInfoImpl.Kind.MEMBER, paramClassDoc, (MemberDoc)paramProgramElementDoc, (paramProgramElementDoc
/* 276 */           .name().lastIndexOf("Property") != -1 && this.configuration.javafx) ? paramProgramElementDoc
/* 277 */           .name().substring(0, paramProgramElementDoc.name().length() - "Property".length()) : paramProgramElementDoc
/* 278 */           .name(), false, true));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addSummaryType(ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 286 */     MethodDoc methodDoc = (MethodDoc)paramProgramElementDoc;
/* 287 */     addModifierAndType((ProgramElementDoc)methodDoc, methodDoc.returnType(), paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getDeprecatedLink(ProgramElementDoc paramProgramElementDoc) {
/* 294 */     return this.writer.getDocLink(LinkInfoImpl.Kind.MEMBER, (MemberDoc)paramProgramElementDoc, ((MethodDoc)paramProgramElementDoc)
/* 295 */         .qualifiedName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavSummaryLink(ClassDoc paramClassDoc, boolean paramBoolean) {
/* 302 */     if (paramBoolean) {
/* 303 */       if (paramClassDoc == null) {
/* 304 */         return this.writer.getHyperLink(SectionName.PROPERTY_SUMMARY, this.writer
/*     */             
/* 306 */             .getResource("doclet.navProperty"));
/*     */       }
/* 308 */       return this.writer.getHyperLink(SectionName.PROPERTIES_INHERITANCE, this.configuration
/*     */           
/* 310 */           .getClassName(paramClassDoc), this.writer.getResource("doclet.navProperty"));
/*     */     } 
/*     */     
/* 313 */     return this.writer.getResource("doclet.navProperty");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addNavDetailLink(boolean paramBoolean, Content paramContent) {
/* 321 */     if (paramBoolean) {
/* 322 */       paramContent.addContent(this.writer.getHyperLink(SectionName.PROPERTY_DETAIL, this.writer
/*     */             
/* 324 */             .getResource("doclet.navProperty")));
/*     */     } else {
/* 326 */       paramContent.addContent(this.writer.getResource("doclet.navProperty"));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\PropertyWriterImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */