/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.IndexBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AbstractIndexWriter
/*     */   extends HtmlDocletWriter
/*     */ {
/*     */   protected IndexBuilder indexbuilder;
/*     */   
/*     */   protected AbstractIndexWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath, IndexBuilder paramIndexBuilder) throws IOException {
/*  70 */     super(paramConfigurationImpl, paramDocPath);
/*  71 */     this.indexbuilder = paramIndexBuilder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkIndex() {
/*  80 */     return (Content)HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.indexLabel);
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
/*     */   protected void addContents(Character paramCharacter, List<? extends Doc> paramList, Content paramContent) {
/*  94 */     String str = paramCharacter.toString();
/*  95 */     paramContent.addContent(getMarkerAnchorForIndex(str));
/*  96 */     StringContent stringContent = new StringContent(str);
/*  97 */     HtmlTree htmlTree = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, false, HtmlStyle.title, (Content)stringContent);
/*     */     
/*  99 */     paramContent.addContent((Content)htmlTree);
/* 100 */     int i = paramList.size();
/*     */     
/* 102 */     if (i > 0) {
/* 103 */       HtmlTree htmlTree1 = new HtmlTree(HtmlTag.DL);
/* 104 */       for (byte b = 0; b < i; b++) {
/* 105 */         Doc doc = paramList.get(b);
/* 106 */         if (doc instanceof MemberDoc) {
/* 107 */           addDescription((MemberDoc)doc, (Content)htmlTree1);
/* 108 */         } else if (doc instanceof ClassDoc) {
/* 109 */           addDescription((ClassDoc)doc, (Content)htmlTree1);
/* 110 */         } else if (doc instanceof PackageDoc) {
/* 111 */           addDescription((PackageDoc)doc, (Content)htmlTree1);
/*     */         } 
/*     */       } 
/* 114 */       paramContent.addContent((Content)htmlTree1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addDescription(PackageDoc paramPackageDoc, Content paramContent) {
/* 125 */     Content content = getPackageLink(paramPackageDoc, (Content)new StringContent(Util.getPackageName(paramPackageDoc)));
/* 126 */     HtmlTree htmlTree1 = HtmlTree.DT(content);
/* 127 */     htmlTree1.addContent(" - ");
/* 128 */     htmlTree1.addContent(getResource("doclet.package"));
/* 129 */     htmlTree1.addContent(" " + paramPackageDoc.name());
/* 130 */     paramContent.addContent((Content)htmlTree1);
/* 131 */     HtmlTree htmlTree2 = new HtmlTree(HtmlTag.DD);
/* 132 */     addSummaryComment((Doc)paramPackageDoc, (Content)htmlTree2);
/* 133 */     paramContent.addContent((Content)htmlTree2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addDescription(ClassDoc paramClassDoc, Content paramContent) {
/* 143 */     Content content = getLink((new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.INDEX, paramClassDoc))
/* 144 */         .strong(true));
/* 145 */     HtmlTree htmlTree1 = HtmlTree.DT(content);
/* 146 */     htmlTree1.addContent(" - ");
/* 147 */     addClassInfo(paramClassDoc, (Content)htmlTree1);
/* 148 */     paramContent.addContent((Content)htmlTree1);
/* 149 */     HtmlTree htmlTree2 = new HtmlTree(HtmlTag.DD);
/* 150 */     addComment((ProgramElementDoc)paramClassDoc, (Content)htmlTree2);
/* 151 */     paramContent.addContent((Content)htmlTree2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addClassInfo(ClassDoc paramClassDoc, Content paramContent) {
/* 162 */     paramContent.addContent(getResource("doclet.in", 
/* 163 */           Util.getTypeName(this.configuration, paramClassDoc, false), 
/* 164 */           getPackageLink(paramClassDoc.containingPackage(), 
/* 165 */             Util.getPackageName(paramClassDoc.containingPackage()))));
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
/*     */   protected void addDescription(MemberDoc paramMemberDoc, Content paramContent) {
/* 178 */     String str = (paramMemberDoc instanceof ExecutableMemberDoc) ? (paramMemberDoc.name() + ((ExecutableMemberDoc)paramMemberDoc).flatSignature()) : paramMemberDoc.name();
/* 179 */     HtmlTree htmlTree1 = HtmlTree.SPAN(HtmlStyle.memberNameLink, 
/* 180 */         getDocLink(LinkInfoImpl.Kind.INDEX, paramMemberDoc, str));
/* 181 */     HtmlTree htmlTree2 = HtmlTree.DT((Content)htmlTree1);
/* 182 */     htmlTree2.addContent(" - ");
/* 183 */     addMemberDesc(paramMemberDoc, (Content)htmlTree2);
/* 184 */     paramContent.addContent((Content)htmlTree2);
/* 185 */     HtmlTree htmlTree3 = new HtmlTree(HtmlTag.DD);
/* 186 */     addComment((ProgramElementDoc)paramMemberDoc, (Content)htmlTree3);
/* 187 */     paramContent.addContent((Content)htmlTree3);
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
/*     */   protected void addComment(ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 201 */     HtmlTree htmlTree1 = HtmlTree.SPAN(HtmlStyle.deprecatedLabel, this.deprecatedPhrase);
/* 202 */     HtmlTree htmlTree2 = new HtmlTree(HtmlTag.DIV);
/* 203 */     htmlTree2.addStyle(HtmlStyle.block);
/* 204 */     if (Util.isDeprecated((Doc)paramProgramElementDoc)) {
/* 205 */       htmlTree2.addContent((Content)htmlTree1); Tag[] arrayOfTag;
/* 206 */       if ((arrayOfTag = paramProgramElementDoc.tags("deprecated")).length > 0)
/* 207 */         addInlineDeprecatedComment((Doc)paramProgramElementDoc, arrayOfTag[0], (Content)htmlTree2); 
/* 208 */       paramContent.addContent((Content)htmlTree2);
/*     */     } else {
/* 210 */       ClassDoc classDoc = paramProgramElementDoc.containingClass();
/* 211 */       while (classDoc != null) {
/* 212 */         if (Util.isDeprecated((Doc)classDoc)) {
/* 213 */           htmlTree2.addContent((Content)htmlTree1);
/* 214 */           paramContent.addContent((Content)htmlTree2);
/*     */           break;
/*     */         } 
/* 217 */         classDoc = classDoc.containingClass();
/*     */       } 
/* 219 */       addSummaryComment((Doc)paramProgramElementDoc, paramContent);
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
/*     */   protected void addMemberDesc(MemberDoc paramMemberDoc, Content paramContent) {
/* 231 */     ClassDoc classDoc = paramMemberDoc.containingClass();
/* 232 */     String str = Util.getTypeName(this.configuration, classDoc, true) + " ";
/*     */     
/* 234 */     if (paramMemberDoc.isField()) {
/* 235 */       if (paramMemberDoc.isStatic()) {
/* 236 */         paramContent.addContent(
/* 237 */             getResource("doclet.Static_variable_in", str));
/*     */       } else {
/* 239 */         paramContent.addContent(
/* 240 */             getResource("doclet.Variable_in", str));
/*     */       } 
/* 242 */     } else if (paramMemberDoc.isConstructor()) {
/* 243 */       paramContent.addContent(
/* 244 */           getResource("doclet.Constructor_for", str));
/* 245 */     } else if (paramMemberDoc.isMethod()) {
/* 246 */       if (paramMemberDoc.isStatic()) {
/* 247 */         paramContent.addContent(
/* 248 */             getResource("doclet.Static_method_in", str));
/*     */       } else {
/* 250 */         paramContent.addContent(
/* 251 */             getResource("doclet.Method_in", str));
/*     */       } 
/*     */     } 
/* 254 */     addPreQualifiedClassLink(LinkInfoImpl.Kind.INDEX, classDoc, false, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMarkerAnchorForIndex(String paramString) {
/* 265 */     return getMarkerAnchor(getNameForIndex(paramString), (Content)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNameForIndex(String paramString) {
/* 275 */     return "I:" + getName(paramString);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\AbstractIndexWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */