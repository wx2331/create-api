/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.MemberSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.MethodWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ImplementedMethods;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodWriterImpl
/*     */   extends AbstractExecutableMemberWriter
/*     */   implements MethodWriter, MemberSummaryWriter
/*     */ {
/*     */   public MethodWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter, ClassDoc paramClassDoc) {
/*  59 */     super(paramSubWriterHolderWriter, paramClassDoc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodWriterImpl(SubWriterHolderWriter paramSubWriterHolderWriter) {
/*  68 */     super(paramSubWriterHolderWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMemberSummaryHeader(ClassDoc paramClassDoc, Content paramContent) {
/*  76 */     paramContent.addContent(HtmlConstants.START_OF_METHOD_SUMMARY);
/*  77 */     Content content = this.writer.getMemberTreeHeader();
/*  78 */     this.writer.addSummaryHeader(this, paramClassDoc, content);
/*  79 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMethodDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent) {
/*  87 */     paramContent.addContent(HtmlConstants.START_OF_METHOD_DETAILS);
/*  88 */     Content content = this.writer.getMemberTreeHeader();
/*  89 */     content.addContent(this.writer.getMarkerAnchor(SectionName.METHOD_DETAIL));
/*     */     
/*  91 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.DETAILS_HEADING, this.writer.methodDetailsLabel);
/*     */     
/*  93 */     content.addContent((Content)htmlTree);
/*  94 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMethodDocTreeHeader(MethodDoc paramMethodDoc, Content paramContent) {
/*     */     String str;
/* 103 */     if ((str = getErasureAnchor((ExecutableMemberDoc)paramMethodDoc)) != null) {
/* 104 */       paramContent.addContent(this.writer.getMarkerAnchor(str));
/*     */     }
/* 106 */     paramContent.addContent(this.writer
/* 107 */         .getMarkerAnchor(this.writer.getAnchor((ExecutableMemberDoc)paramMethodDoc)));
/* 108 */     Content content = this.writer.getMemberTreeHeader();
/* 109 */     HtmlTree htmlTree = new HtmlTree(HtmlConstants.MEMBER_HEADING);
/* 110 */     htmlTree.addContent(paramMethodDoc.name());
/* 111 */     content.addContent((Content)htmlTree);
/* 112 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getSignature(MethodDoc paramMethodDoc) {
/* 122 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.PRE);
/* 123 */     this.writer.addAnnotationInfo((ProgramElementDoc)paramMethodDoc, (Content)htmlTree);
/* 124 */     addModifiers((MemberDoc)paramMethodDoc, (Content)htmlTree);
/* 125 */     addTypeParameters((ExecutableMemberDoc)paramMethodDoc, (Content)htmlTree);
/* 126 */     addReturnType(paramMethodDoc, (Content)htmlTree);
/* 127 */     if (this.configuration.linksource) {
/* 128 */       StringContent stringContent = new StringContent(paramMethodDoc.name());
/* 129 */       this.writer.addSrcLink((ProgramElementDoc)paramMethodDoc, (Content)stringContent, (Content)htmlTree);
/*     */     } else {
/* 131 */       addName(paramMethodDoc.name(), (Content)htmlTree);
/*     */     } 
/* 133 */     int i = htmlTree.charCount();
/* 134 */     addParameters((ExecutableMemberDoc)paramMethodDoc, (Content)htmlTree, i);
/* 135 */     addExceptions((ExecutableMemberDoc)paramMethodDoc, (Content)htmlTree, i);
/* 136 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDeprecated(MethodDoc paramMethodDoc, Content paramContent) {
/* 143 */     addDeprecatedInfo((ProgramElementDoc)paramMethodDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComments(Type paramType, MethodDoc paramMethodDoc, Content paramContent) {
/* 150 */     ClassDoc classDoc = paramType.asClassDoc();
/* 151 */     if ((paramMethodDoc.inlineTags()).length > 0) {
/* 152 */       if (paramType.asClassDoc().equals(this.classdoc) || (
/* 153 */         !classDoc.isPublic() && 
/* 154 */         !Util.isLinkable(classDoc, this.configuration))) {
/* 155 */         this.writer.addInlineComment((Doc)paramMethodDoc, paramContent);
/*     */       } else {
/*     */         
/* 158 */         Content content = this.writer.getDocLink(LinkInfoImpl.Kind.METHOD_DOC_COPY, paramType
/* 159 */             .asClassDoc(), (MemberDoc)paramMethodDoc, 
/* 160 */             paramType.asClassDoc().isIncluded() ? paramType
/* 161 */             .typeName() : paramType.qualifiedTypeName(), false);
/*     */         
/* 163 */         HtmlTree htmlTree1 = HtmlTree.CODE(content);
/* 164 */         HtmlTree htmlTree2 = HtmlTree.SPAN(HtmlStyle.descfrmTypeLabel, paramType.asClassDoc().isClass() ? this.writer.descfrmClassLabel : this.writer.descfrmInterfaceLabel);
/*     */         
/* 166 */         htmlTree2.addContent(this.writer.getSpace());
/* 167 */         htmlTree2.addContent((Content)htmlTree1);
/* 168 */         paramContent.addContent((Content)HtmlTree.DIV(HtmlStyle.block, (Content)htmlTree2));
/* 169 */         this.writer.addInlineComment((Doc)paramMethodDoc, paramContent);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTags(MethodDoc paramMethodDoc, Content paramContent) {
/* 178 */     this.writer.addTagsInfo((Doc)paramMethodDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMethodDetails(Content paramContent) {
/* 185 */     return getMemberTree(paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMethodDoc(Content paramContent, boolean paramBoolean) {
/* 193 */     return getMemberTree(paramContent, paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 200 */     this.writer.close();
/*     */   }
/*     */   
/*     */   public int getMemberKind() {
/* 204 */     return 4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSummaryLabel(Content paramContent) {
/* 211 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING, this.writer
/* 212 */         .getResource("doclet.Method_Summary"));
/* 213 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTableSummary() {
/* 220 */     return this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 221 */         .getText("doclet.Method_Summary"), this.configuration
/* 222 */         .getText("doclet.methods"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getCaption() {
/* 229 */     return this.configuration.getResource("doclet.Methods");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getSummaryTableHeader(ProgramElementDoc paramProgramElementDoc) {
/* 236 */     return new String[] { this.writer
/* 237 */         .getModifierTypeHeader(), this.configuration
/* 238 */         .getText("doclet.0_and_1", this.configuration
/* 239 */           .getText("doclet.Method"), this.configuration
/* 240 */           .getText("doclet.Description")) };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSummaryAnchor(ClassDoc paramClassDoc, Content paramContent) {
/* 249 */     paramContent.addContent(this.writer.getMarkerAnchor(SectionName.METHOD_SUMMARY));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInheritedSummaryAnchor(ClassDoc paramClassDoc, Content paramContent) {
/* 257 */     paramContent.addContent(this.writer.getMarkerAnchor(SectionName.METHODS_INHERITANCE, this.configuration
/* 258 */           .getClassName(paramClassDoc)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInheritedSummaryLabel(ClassDoc paramClassDoc, Content paramContent) {
/* 265 */     Content content = this.writer.getPreQualifiedClassLink(LinkInfoImpl.Kind.MEMBER, paramClassDoc, false);
/*     */ 
/*     */ 
/*     */     
/* 269 */     StringContent stringContent = new StringContent(paramClassDoc.isClass() ? this.configuration.getText("doclet.Methods_Inherited_From_Class") : this.configuration.getText("doclet.Methods_Inherited_From_Interface"));
/* 270 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.INHERITED_SUMMARY_HEADING, (Content)stringContent);
/*     */     
/* 272 */     htmlTree.addContent(this.writer.getSpace());
/* 273 */     htmlTree.addContent(content);
/* 274 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addSummaryType(ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 281 */     MethodDoc methodDoc = (MethodDoc)paramProgramElementDoc;
/* 282 */     addModifierAndType((ProgramElementDoc)methodDoc, methodDoc.returnType(), paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void addOverridden(HtmlDocletWriter paramHtmlDocletWriter, Type paramType, MethodDoc paramMethodDoc, Content paramContent) {
/* 290 */     if (paramHtmlDocletWriter.configuration.nocomment) {
/*     */       return;
/*     */     }
/* 293 */     ClassDoc classDoc = paramType.asClassDoc();
/* 294 */     if (!classDoc.isPublic() && 
/* 295 */       !Util.isLinkable(classDoc, paramHtmlDocletWriter.configuration)) {
/*     */       return;
/*     */     }
/*     */     
/* 299 */     if (paramType.asClassDoc().isIncluded() && !paramMethodDoc.isIncluded()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 304 */     Content content = paramHtmlDocletWriter.overridesLabel;
/* 305 */     LinkInfoImpl.Kind kind = LinkInfoImpl.Kind.METHOD_OVERRIDES;
/*     */     
/* 307 */     if (paramMethodDoc != null) {
/* 308 */       if (paramType.asClassDoc().isAbstract() && paramMethodDoc.isAbstract()) {
/*     */ 
/*     */         
/* 311 */         content = paramHtmlDocletWriter.specifiedByLabel;
/* 312 */         kind = LinkInfoImpl.Kind.METHOD_SPECIFIED_BY;
/*     */       } 
/* 314 */       HtmlTree htmlTree1 = HtmlTree.DT((Content)HtmlTree.SPAN(HtmlStyle.overrideSpecifyLabel, content));
/* 315 */       paramContent.addContent((Content)htmlTree1);
/*     */       
/* 317 */       Content content1 = paramHtmlDocletWriter.getLink(new LinkInfoImpl(paramHtmlDocletWriter.configuration, kind, paramType));
/* 318 */       HtmlTree htmlTree2 = HtmlTree.CODE(content1);
/* 319 */       String str = paramMethodDoc.name();
/* 320 */       Content content2 = paramHtmlDocletWriter.getLink((new LinkInfoImpl(paramHtmlDocletWriter.configuration, LinkInfoImpl.Kind.MEMBER, paramType
/*     */             
/* 322 */             .asClassDoc()))
/* 323 */           .where(paramHtmlDocletWriter.getName(paramHtmlDocletWriter.getAnchor((ExecutableMemberDoc)paramMethodDoc))).label(str));
/* 324 */       HtmlTree htmlTree3 = HtmlTree.CODE(content2);
/* 325 */       HtmlTree htmlTree4 = HtmlTree.DD((Content)htmlTree3);
/* 326 */       htmlTree4.addContent(paramHtmlDocletWriter.getSpace());
/* 327 */       htmlTree4.addContent(paramHtmlDocletWriter.getResource("doclet.in_class"));
/* 328 */       htmlTree4.addContent(paramHtmlDocletWriter.getSpace());
/* 329 */       htmlTree4.addContent((Content)htmlTree2);
/* 330 */       paramContent.addContent((Content)htmlTree4);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void addImplementsInfo(HtmlDocletWriter paramHtmlDocletWriter, MethodDoc paramMethodDoc, Content paramContent) {
/* 339 */     if (paramHtmlDocletWriter.configuration.nocomment) {
/*     */       return;
/*     */     }
/* 342 */     ImplementedMethods implementedMethods = new ImplementedMethods(paramMethodDoc, paramHtmlDocletWriter.configuration);
/*     */     
/* 344 */     MethodDoc[] arrayOfMethodDoc = implementedMethods.build();
/* 345 */     for (byte b = 0; b < arrayOfMethodDoc.length; b++) {
/* 346 */       MethodDoc methodDoc = arrayOfMethodDoc[b];
/* 347 */       Type type = implementedMethods.getMethodHolder(methodDoc);
/* 348 */       Content content1 = paramHtmlDocletWriter.getLink(new LinkInfoImpl(paramHtmlDocletWriter.configuration, LinkInfoImpl.Kind.METHOD_SPECIFIED_BY, type));
/*     */       
/* 350 */       HtmlTree htmlTree1 = HtmlTree.CODE(content1);
/* 351 */       HtmlTree htmlTree2 = HtmlTree.DT((Content)HtmlTree.SPAN(HtmlStyle.overrideSpecifyLabel, paramHtmlDocletWriter.specifiedByLabel));
/* 352 */       paramContent.addContent((Content)htmlTree2);
/* 353 */       Content content2 = paramHtmlDocletWriter.getDocLink(LinkInfoImpl.Kind.MEMBER, (MemberDoc)methodDoc, methodDoc
/*     */           
/* 355 */           .name(), false);
/* 356 */       HtmlTree htmlTree3 = HtmlTree.CODE(content2);
/* 357 */       HtmlTree htmlTree4 = HtmlTree.DD((Content)htmlTree3);
/* 358 */       htmlTree4.addContent(paramHtmlDocletWriter.getSpace());
/* 359 */       htmlTree4.addContent(paramHtmlDocletWriter.getResource("doclet.in_interface"));
/* 360 */       htmlTree4.addContent(paramHtmlDocletWriter.getSpace());
/* 361 */       htmlTree4.addContent((Content)htmlTree1);
/* 362 */       paramContent.addContent((Content)htmlTree4);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addReturnType(MethodDoc paramMethodDoc, Content paramContent) {
/* 373 */     Type type = paramMethodDoc.returnType();
/* 374 */     if (type != null) {
/* 375 */       Content content = this.writer.getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.RETURN_TYPE, type));
/*     */       
/* 377 */       paramContent.addContent(content);
/* 378 */       paramContent.addContent(this.writer.getSpace());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavSummaryLink(ClassDoc paramClassDoc, boolean paramBoolean) {
/* 386 */     if (paramBoolean) {
/* 387 */       if (paramClassDoc == null) {
/* 388 */         return this.writer.getHyperLink(SectionName.METHOD_SUMMARY, this.writer
/*     */             
/* 390 */             .getResource("doclet.navMethod"));
/*     */       }
/* 392 */       return this.writer.getHyperLink(SectionName.METHODS_INHERITANCE, this.configuration
/*     */           
/* 394 */           .getClassName(paramClassDoc), this.writer.getResource("doclet.navMethod"));
/*     */     } 
/*     */     
/* 397 */     return this.writer.getResource("doclet.navMethod");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addNavDetailLink(boolean paramBoolean, Content paramContent) {
/* 405 */     if (paramBoolean) {
/* 406 */       paramContent.addContent(this.writer.getHyperLink(SectionName.METHOD_DETAIL, this.writer
/* 407 */             .getResource("doclet.navMethod")));
/*     */     } else {
/* 409 */       paramContent.addContent(this.writer.getResource("doclet.navMethod"));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\MethodWriterImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */