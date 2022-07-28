/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FrameOutputWriter
/*     */   extends HtmlDocletWriter
/*     */ {
/*     */   int noOfPackages;
/*  58 */   private final String SCROLL_YES = "yes";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FrameOutputWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath) throws IOException {
/*  67 */     super(paramConfigurationImpl, paramDocPath);
/*  68 */     this.noOfPackages = paramConfigurationImpl.packages.length;
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
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl) {
/*  80 */     DocPath docPath = DocPath.empty;
/*     */     try {
/*  82 */       docPath = DocPaths.INDEX;
/*  83 */       FrameOutputWriter frameOutputWriter = new FrameOutputWriter(paramConfigurationImpl, docPath);
/*  84 */       frameOutputWriter.generateFrameFile();
/*  85 */       frameOutputWriter.close();
/*  86 */     } catch (IOException iOException) {
/*  87 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { iOException
/*     */             
/*  89 */             .toString(), docPath });
/*  90 */       throw new DocletAbortException(iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateFrameFile() throws IOException {
/*  99 */     Content content = getFrameDetails();
/* 100 */     if (this.configuration.windowtitle.length() > 0) {
/* 101 */       printFramesetDocument(this.configuration.windowtitle, this.configuration.notimestamp, content);
/*     */     } else {
/*     */       
/* 104 */       printFramesetDocument(this.configuration.getText("doclet.Generated_Docs_Untitled"), this.configuration.notimestamp, content);
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
/*     */   protected void addFrameWarning(Content paramContent) {
/* 116 */     HtmlTree htmlTree1 = new HtmlTree(HtmlTag.NOFRAMES);
/* 117 */     HtmlTree htmlTree2 = HtmlTree.NOSCRIPT(
/* 118 */         (Content)HtmlTree.DIV(getResource("doclet.No_Script_Message")));
/* 119 */     htmlTree1.addContent((Content)htmlTree2);
/* 120 */     HtmlTree htmlTree3 = HtmlTree.HEADING(HtmlConstants.CONTENT_HEADING, 
/* 121 */         getResource("doclet.Frame_Alert"));
/* 122 */     htmlTree1.addContent((Content)htmlTree3);
/* 123 */     HtmlTree htmlTree4 = HtmlTree.P(getResource("doclet.Frame_Warning_Message", 
/* 124 */           getHyperLink(this.configuration.topFile, this.configuration
/* 125 */             .getText("doclet.Non_Frame_Version"))));
/* 126 */     htmlTree1.addContent((Content)htmlTree4);
/* 127 */     paramContent.addContent((Content)htmlTree1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getFrameDetails() {
/* 136 */     HtmlTree htmlTree = HtmlTree.FRAMESET("20%,80%", null, "Documentation frame", "top.loadFrames()");
/*     */     
/* 138 */     if (this.noOfPackages <= 1) {
/* 139 */       addAllClassesFrameTag((Content)htmlTree);
/* 140 */     } else if (this.noOfPackages > 1) {
/* 141 */       HtmlTree htmlTree1 = HtmlTree.FRAMESET(null, "30%,70%", "Left frames", "top.loadFrames()");
/*     */       
/* 143 */       addAllPackagesFrameTag((Content)htmlTree1);
/* 144 */       addAllClassesFrameTag((Content)htmlTree1);
/* 145 */       htmlTree.addContent((Content)htmlTree1);
/*     */     } 
/* 147 */     addClassFrameTag((Content)htmlTree);
/* 148 */     addFrameWarning((Content)htmlTree);
/* 149 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addAllPackagesFrameTag(Content paramContent) {
/* 158 */     HtmlTree htmlTree = HtmlTree.FRAME(DocPaths.OVERVIEW_FRAME.getPath(), "packageListFrame", this.configuration
/* 159 */         .getText("doclet.All_Packages"));
/* 160 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addAllClassesFrameTag(Content paramContent) {
/* 169 */     HtmlTree htmlTree = HtmlTree.FRAME(DocPaths.ALLCLASSES_FRAME.getPath(), "packageFrame", this.configuration
/* 170 */         .getText("doclet.All_classes_and_interfaces"));
/* 171 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addClassFrameTag(Content paramContent) {
/* 180 */     HtmlTree htmlTree = HtmlTree.FRAME(this.configuration.topFile.getPath(), "classFrame", this.configuration
/* 181 */         .getText("doclet.Package_class_and_interface_descriptions"), "yes");
/*     */     
/* 183 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\FrameOutputWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */