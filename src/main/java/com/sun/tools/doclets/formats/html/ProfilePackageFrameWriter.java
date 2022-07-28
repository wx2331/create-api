/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.RawHtml;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.javac.jvm.Profile;
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
/*     */ public class ProfilePackageFrameWriter
/*     */   extends HtmlDocletWriter
/*     */ {
/*     */   private PackageDoc packageDoc;
/*     */   
/*     */   public ProfilePackageFrameWriter(ConfigurationImpl paramConfigurationImpl, PackageDoc paramPackageDoc, String paramString) throws IOException {
/*  71 */     super(paramConfigurationImpl, DocPath.forPackage(paramPackageDoc).resolve(
/*  72 */           DocPaths.profilePackageFrame(paramString)));
/*  73 */     this.packageDoc = paramPackageDoc;
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
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, PackageDoc paramPackageDoc, int paramInt) {
/*     */     try {
/*  88 */       String str1 = (Profile.lookup(paramInt)).name;
/*  89 */       ProfilePackageFrameWriter profilePackageFrameWriter = new ProfilePackageFrameWriter(paramConfigurationImpl, paramPackageDoc, str1);
/*     */       
/*  91 */       StringBuilder stringBuilder = new StringBuilder(str1);
/*  92 */       String str2 = " - ";
/*  93 */       stringBuilder.append(str2);
/*  94 */       String str3 = Util.getPackageName(paramPackageDoc);
/*  95 */       stringBuilder.append(str3);
/*  96 */       HtmlTree htmlTree1 = profilePackageFrameWriter.getBody(false, profilePackageFrameWriter
/*  97 */           .getWindowTitle(stringBuilder.toString()));
/*  98 */       StringContent stringContent1 = new StringContent(str1);
/*  99 */       StringContent stringContent2 = new StringContent(str2);
/* 100 */       RawHtml rawHtml = new RawHtml(str3);
/* 101 */       HtmlTree htmlTree2 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, HtmlStyle.bar, profilePackageFrameWriter
/* 102 */           .getTargetProfileLink("classFrame", (Content)stringContent1, str1));
/* 103 */       htmlTree2.addContent((Content)stringContent2);
/* 104 */       htmlTree2.addContent(profilePackageFrameWriter.getTargetProfilePackageLink(paramPackageDoc, "classFrame", (Content)rawHtml, str1));
/*     */       
/* 106 */       htmlTree1.addContent((Content)htmlTree2);
/* 107 */       HtmlTree htmlTree3 = new HtmlTree(HtmlTag.DIV);
/* 108 */       htmlTree3.addStyle(HtmlStyle.indexContainer);
/* 109 */       profilePackageFrameWriter.addClassListing((Content)htmlTree3, paramInt);
/* 110 */       htmlTree1.addContent((Content)htmlTree3);
/* 111 */       profilePackageFrameWriter.printHtmlDocument(paramConfigurationImpl.metakeywords
/* 112 */           .getMetaKeywords(paramPackageDoc), false, (Content)htmlTree1);
/* 113 */       profilePackageFrameWriter.close();
/* 114 */     } catch (IOException iOException) {
/* 115 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { iOException
/*     */             
/* 117 */             .toString(), DocPaths.PACKAGE_FRAME.getPath() });
/* 118 */       throw new DocletAbortException(iOException);
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
/*     */   
/*     */   protected void addClassListing(Content paramContent, int paramInt) {
/* 131 */     if (this.packageDoc.isIncluded()) {
/* 132 */       addClassKindListing(this.packageDoc.interfaces(), 
/* 133 */           getResource("doclet.Interfaces"), paramContent, paramInt);
/* 134 */       addClassKindListing(this.packageDoc.ordinaryClasses(), 
/* 135 */           getResource("doclet.Classes"), paramContent, paramInt);
/* 136 */       addClassKindListing(this.packageDoc.enums(), 
/* 137 */           getResource("doclet.Enums"), paramContent, paramInt);
/* 138 */       addClassKindListing(this.packageDoc.exceptions(), 
/* 139 */           getResource("doclet.Exceptions"), paramContent, paramInt);
/* 140 */       addClassKindListing(this.packageDoc.errors(), 
/* 141 */           getResource("doclet.Errors"), paramContent, paramInt);
/* 142 */       addClassKindListing((ClassDoc[])this.packageDoc.annotationTypes(), 
/* 143 */           getResource("doclet.AnnotationTypes"), paramContent, paramInt);
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
/*     */ 
/*     */   
/*     */   protected void addClassKindListing(ClassDoc[] paramArrayOfClassDoc, Content paramContent1, Content paramContent2, int paramInt) {
/* 157 */     if (paramArrayOfClassDoc.length > 0) {
/* 158 */       Arrays.sort((Object[])paramArrayOfClassDoc);
/* 159 */       boolean bool = false;
/* 160 */       HtmlTree htmlTree = new HtmlTree(HtmlTag.UL);
/* 161 */       htmlTree.setTitle(paramContent1);
/* 162 */       for (byte b = 0; b < paramArrayOfClassDoc.length; b++) {
/* 163 */         if (isTypeInProfile(paramArrayOfClassDoc[b], paramInt))
/*     */         {
/*     */           
/* 166 */           if (Util.isCoreClass(paramArrayOfClassDoc[b]) && this.configuration
/* 167 */             .isGeneratedDoc(paramArrayOfClassDoc[b])) {
/*     */             HtmlTree htmlTree1;
/*     */             
/* 170 */             if (!bool) {
/* 171 */               htmlTree1 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, true, paramContent1);
/*     */               
/* 173 */               paramContent2.addContent((Content)htmlTree1);
/* 174 */               bool = true;
/*     */             } 
/* 176 */             StringContent stringContent = new StringContent(paramArrayOfClassDoc[b].name());
/* 177 */             if (paramArrayOfClassDoc[b].isInterface()) htmlTree1 = HtmlTree.SPAN(HtmlStyle.interfaceName, (Content)stringContent); 
/* 178 */             Content content = getLink((new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.PACKAGE_FRAME, paramArrayOfClassDoc[b]))
/* 179 */                 .label((Content)htmlTree1).target("classFrame"));
/* 180 */             HtmlTree htmlTree2 = HtmlTree.LI(content);
/* 181 */             htmlTree.addContent((Content)htmlTree2);
/*     */           }  } 
/* 183 */       }  paramContent2.addContent((Content)htmlTree);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\ProfilePackageFrameWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */