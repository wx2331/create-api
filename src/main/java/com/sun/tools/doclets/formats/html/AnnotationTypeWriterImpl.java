/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.builders.MemberSummaryBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationTypeWriterImpl
/*     */   extends SubWriterHolderWriter
/*     */   implements AnnotationTypeWriter
/*     */ {
/*     */   protected AnnotationTypeDoc annotationType;
/*     */   protected Type prev;
/*     */   protected Type next;
/*     */   
/*     */   public AnnotationTypeWriterImpl(ConfigurationImpl paramConfigurationImpl, AnnotationTypeDoc paramAnnotationTypeDoc, Type paramType1, Type paramType2) throws Exception {
/*  71 */     super(paramConfigurationImpl, DocPath.forClass((ClassDoc)paramAnnotationTypeDoc));
/*  72 */     this.annotationType = paramAnnotationTypeDoc;
/*  73 */     paramConfigurationImpl.currentcd = paramAnnotationTypeDoc.asClassDoc();
/*  74 */     this.prev = paramType1;
/*  75 */     this.next = paramType2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkPackage() {
/*  84 */     Content content = getHyperLink(DocPaths.PACKAGE_SUMMARY, this.packageLabel);
/*     */     
/*  86 */     return (Content)HtmlTree.LI(content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkClass() {
/*  96 */     return (Content)HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.classLabel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkClassUse() {
/* 106 */     Content content = getHyperLink(DocPaths.CLASS_USE.resolve(this.filename), this.useLabel);
/* 107 */     return (Content)HtmlTree.LI(content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getNavLinkPrevious() {
/*     */     HtmlTree htmlTree;
/* 118 */     if (this.prev != null) {
/* 119 */       Content content = getLink((new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS, this.prev
/* 120 */             .asClassDoc()))
/* 121 */           .label(this.prevclassLabel).strong(true));
/* 122 */       htmlTree = HtmlTree.LI(content);
/*     */     } else {
/*     */       
/* 125 */       htmlTree = HtmlTree.LI(this.prevclassLabel);
/* 126 */     }  return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getNavLinkNext() {
/*     */     HtmlTree htmlTree;
/* 136 */     if (this.next != null) {
/* 137 */       Content content = getLink((new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS, this.next
/* 138 */             .asClassDoc()))
/* 139 */           .label(this.nextclassLabel).strong(true));
/* 140 */       htmlTree = HtmlTree.LI(content);
/*     */     } else {
/*     */       
/* 143 */       htmlTree = HtmlTree.LI(this.nextclassLabel);
/* 144 */     }  return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getHeader(String paramString) {
/* 152 */     String str1 = (this.annotationType.containingPackage() != null) ? this.annotationType.containingPackage().name() : "";
/* 153 */     String str2 = this.annotationType.name();
/* 154 */     HtmlTree htmlTree1 = getBody(true, getWindowTitle(str2));
/* 155 */     addTop((Content)htmlTree1);
/* 156 */     addNavLinks(true, (Content)htmlTree1);
/* 157 */     htmlTree1.addContent(HtmlConstants.START_OF_CLASS_DATA);
/* 158 */     HtmlTree htmlTree2 = new HtmlTree(HtmlTag.DIV);
/* 159 */     htmlTree2.addStyle(HtmlStyle.header);
/* 160 */     if (str1.length() > 0) {
/* 161 */       StringContent stringContent1 = new StringContent(str1);
/* 162 */       HtmlTree htmlTree = HtmlTree.DIV(HtmlStyle.subTitle, (Content)stringContent1);
/* 163 */       htmlTree2.addContent((Content)htmlTree);
/*     */     } 
/* 165 */     LinkInfoImpl linkInfoImpl = new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_HEADER, (ClassDoc)this.annotationType);
/*     */     
/* 167 */     StringContent stringContent = new StringContent(paramString);
/* 168 */     HtmlTree htmlTree3 = HtmlTree.HEADING(HtmlConstants.CLASS_PAGE_HEADING, true, HtmlStyle.title, (Content)stringContent);
/*     */     
/* 170 */     htmlTree3.addContent(getTypeParameterLinks(linkInfoImpl));
/* 171 */     htmlTree2.addContent((Content)htmlTree3);
/* 172 */     htmlTree1.addContent((Content)htmlTree2);
/* 173 */     return (Content)htmlTree1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getAnnotationContentHeader() {
/* 180 */     return getContentHeader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFooter(Content paramContent) {
/* 187 */     paramContent.addContent(HtmlConstants.END_OF_CLASS_DATA);
/* 188 */     addNavLinks(false, paramContent);
/* 189 */     addBottom(paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printDocument(Content paramContent) throws IOException {
/* 196 */     printHtmlDocument(this.configuration.metakeywords.getMetaKeywords((ClassDoc)this.annotationType), true, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getAnnotationInfoTreeHeader() {
/* 204 */     return getMemberTreeHeader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getAnnotationInfo(Content paramContent) {
/* 211 */     return getMemberTree(HtmlStyle.description, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAnnotationTypeSignature(String paramString, Content paramContent) {
/* 218 */     paramContent.addContent((Content)new HtmlTree(HtmlTag.BR));
/* 219 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.PRE);
/* 220 */     addAnnotationInfo((ProgramElementDoc)this.annotationType, (Content)htmlTree);
/* 221 */     htmlTree.addContent(paramString);
/* 222 */     LinkInfoImpl linkInfoImpl = new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_SIGNATURE, (ClassDoc)this.annotationType);
/*     */     
/* 224 */     StringContent stringContent = new StringContent(this.annotationType.name());
/* 225 */     Content content = getTypeParameterLinks(linkInfoImpl);
/* 226 */     if (this.configuration.linksource) {
/* 227 */       addSrcLink((ProgramElementDoc)this.annotationType, (Content)stringContent, (Content)htmlTree);
/* 228 */       htmlTree.addContent(content);
/*     */     } else {
/* 230 */       HtmlTree htmlTree1 = HtmlTree.SPAN(HtmlStyle.memberNameLabel, (Content)stringContent);
/* 231 */       htmlTree1.addContent(content);
/* 232 */       htmlTree.addContent((Content)htmlTree1);
/*     */     } 
/* 234 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAnnotationTypeDescription(Content paramContent) {
/* 241 */     if (!this.configuration.nocomment && (
/* 242 */       this.annotationType.inlineTags()).length > 0) {
/* 243 */       addInlineComment((Doc)this.annotationType, paramContent);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAnnotationTypeTagInfo(Content paramContent) {
/* 252 */     if (!this.configuration.nocomment) {
/* 253 */       addTagsInfo((Doc)this.annotationType, paramContent);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAnnotationTypeDeprecationInfo(Content paramContent) {
/* 261 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.HR);
/* 262 */     paramContent.addContent((Content)htmlTree);
/* 263 */     Tag[] arrayOfTag = this.annotationType.tags("deprecated");
/* 264 */     if (Util.isDeprecated((Doc)this.annotationType)) {
/* 265 */       HtmlTree htmlTree1 = HtmlTree.SPAN(HtmlStyle.deprecatedLabel, this.deprecatedPhrase);
/* 266 */       HtmlTree htmlTree2 = HtmlTree.DIV(HtmlStyle.block, (Content)htmlTree1);
/* 267 */       if (arrayOfTag.length > 0) {
/* 268 */         Tag[] arrayOfTag1 = arrayOfTag[0].inlineTags();
/* 269 */         if (arrayOfTag1.length > 0) {
/* 270 */           htmlTree2.addContent(getSpace());
/* 271 */           addInlineDeprecatedComment((Doc)this.annotationType, arrayOfTag[0], (Content)htmlTree2);
/*     */         } 
/*     */       } 
/* 274 */       paramContent.addContent((Content)htmlTree2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkTree() {
/* 282 */     Content content = getHyperLink(DocPaths.PACKAGE_TREE, this.treeLabel, "", "");
/*     */     
/* 284 */     return (Content)HtmlTree.LI(content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addSummaryDetailLinks(Content paramContent) {
/*     */     try {
/* 295 */       HtmlTree htmlTree = HtmlTree.DIV(getNavSummaryLinks());
/* 296 */       htmlTree.addContent(getNavDetailLinks());
/* 297 */       paramContent.addContent((Content)htmlTree);
/* 298 */     } catch (Exception exception) {
/* 299 */       exception.printStackTrace();
/* 300 */       throw new DocletAbortException(exception);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavSummaryLinks() throws Exception {
/* 310 */     HtmlTree htmlTree1 = HtmlTree.LI(this.summaryLabel);
/* 311 */     htmlTree1.addContent(getSpace());
/* 312 */     HtmlTree htmlTree2 = HtmlTree.UL(HtmlStyle.subNavList, (Content)htmlTree1);
/*     */     
/* 314 */     MemberSummaryBuilder memberSummaryBuilder = (MemberSummaryBuilder)this.configuration.getBuilderFactory().getMemberSummaryBuilder(this);
/* 315 */     HtmlTree htmlTree3 = new HtmlTree(HtmlTag.LI);
/* 316 */     addNavSummaryLink(memberSummaryBuilder, "doclet.navField", 5, (Content)htmlTree3);
/*     */ 
/*     */     
/* 319 */     addNavGap((Content)htmlTree3);
/* 320 */     htmlTree2.addContent((Content)htmlTree3);
/* 321 */     HtmlTree htmlTree4 = new HtmlTree(HtmlTag.LI);
/* 322 */     addNavSummaryLink(memberSummaryBuilder, "doclet.navAnnotationTypeRequiredMember", 7, (Content)htmlTree4);
/*     */ 
/*     */     
/* 325 */     addNavGap((Content)htmlTree4);
/* 326 */     htmlTree2.addContent((Content)htmlTree4);
/* 327 */     HtmlTree htmlTree5 = new HtmlTree(HtmlTag.LI);
/* 328 */     addNavSummaryLink(memberSummaryBuilder, "doclet.navAnnotationTypeOptionalMember", 6, (Content)htmlTree5);
/*     */ 
/*     */     
/* 331 */     htmlTree2.addContent((Content)htmlTree5);
/* 332 */     return (Content)htmlTree2;
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
/*     */   protected void addNavSummaryLink(MemberSummaryBuilder paramMemberSummaryBuilder, String paramString, int paramInt, Content paramContent) {
/* 346 */     AbstractMemberWriter abstractMemberWriter = (AbstractMemberWriter)paramMemberSummaryBuilder.getMemberSummaryWriter(paramInt);
/* 347 */     if (abstractMemberWriter == null) {
/* 348 */       paramContent.addContent(getResource(paramString));
/*     */     } else {
/* 350 */       paramContent.addContent(abstractMemberWriter.getNavSummaryLink(null, 
/* 351 */             !paramMemberSummaryBuilder.getVisibleMemberMap(paramInt).noVisibleMembers()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavDetailLinks() throws Exception {
/* 361 */     HtmlTree htmlTree1 = HtmlTree.LI(this.detailLabel);
/* 362 */     htmlTree1.addContent(getSpace());
/* 363 */     HtmlTree htmlTree2 = HtmlTree.UL(HtmlStyle.subNavList, (Content)htmlTree1);
/*     */     
/* 365 */     MemberSummaryBuilder memberSummaryBuilder = (MemberSummaryBuilder)this.configuration.getBuilderFactory().getMemberSummaryBuilder(this);
/*     */ 
/*     */     
/* 368 */     AbstractMemberWriter abstractMemberWriter1 = (AbstractMemberWriter)memberSummaryBuilder.getMemberSummaryWriter(5);
/*     */ 
/*     */     
/* 371 */     AbstractMemberWriter abstractMemberWriter2 = (AbstractMemberWriter)memberSummaryBuilder.getMemberSummaryWriter(6);
/*     */ 
/*     */     
/* 374 */     AbstractMemberWriter abstractMemberWriter3 = (AbstractMemberWriter)memberSummaryBuilder.getMemberSummaryWriter(7);
/* 375 */     HtmlTree htmlTree3 = new HtmlTree(HtmlTag.LI);
/* 376 */     if (abstractMemberWriter1 != null) {
/* 377 */       abstractMemberWriter1.addNavDetailLink(((this.annotationType.fields()).length > 0), (Content)htmlTree3);
/*     */     } else {
/* 379 */       htmlTree3.addContent(getResource("doclet.navField"));
/*     */     } 
/* 381 */     addNavGap((Content)htmlTree3);
/* 382 */     htmlTree2.addContent((Content)htmlTree3);
/* 383 */     if (abstractMemberWriter2 != null) {
/* 384 */       HtmlTree htmlTree = new HtmlTree(HtmlTag.LI);
/* 385 */       abstractMemberWriter2.addNavDetailLink(((this.annotationType.elements()).length > 0), (Content)htmlTree);
/* 386 */       htmlTree2.addContent((Content)htmlTree);
/* 387 */     } else if (abstractMemberWriter3 != null) {
/* 388 */       HtmlTree htmlTree = new HtmlTree(HtmlTag.LI);
/* 389 */       abstractMemberWriter3.addNavDetailLink(((this.annotationType.elements()).length > 0), (Content)htmlTree);
/* 390 */       htmlTree2.addContent((Content)htmlTree);
/*     */     } else {
/* 392 */       HtmlTree htmlTree = HtmlTree.LI(getResource("doclet.navAnnotationTypeMember"));
/* 393 */       htmlTree2.addContent((Content)htmlTree);
/*     */     } 
/* 395 */     return (Content)htmlTree2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addNavGap(Content paramContent) {
/* 404 */     paramContent.addContent(getSpace());
/* 405 */     paramContent.addContent("|");
/* 406 */     paramContent.addContent(getSpace());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationTypeDoc getAnnotationTypeDoc() {
/* 413 */     return this.annotationType;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\AnnotationTypeWriterImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */