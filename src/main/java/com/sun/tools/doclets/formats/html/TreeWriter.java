/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassTree;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TreeWriter
/*     */   extends AbstractTreeWriter
/*     */ {
/*     */   private PackageDoc[] packages;
/*     */   private boolean classesonly;
/*     */   
/*     */   public TreeWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath, ClassTree paramClassTree) throws IOException {
/*  72 */     super(paramConfigurationImpl, paramDocPath, paramClassTree);
/*  73 */     this.packages = paramConfigurationImpl.packages;
/*  74 */     this.classesonly = (this.packages.length == 0);
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
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, ClassTree paramClassTree) {
/*  87 */     DocPath docPath = DocPaths.OVERVIEW_TREE;
/*     */     try {
/*  89 */       TreeWriter treeWriter = new TreeWriter(paramConfigurationImpl, docPath, paramClassTree);
/*  90 */       treeWriter.generateTreeFile();
/*  91 */       treeWriter.close();
/*  92 */     } catch (IOException iOException) {
/*  93 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { iOException
/*     */             
/*  95 */             .toString(), docPath });
/*  96 */       throw new DocletAbortException(iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generateTreeFile() throws IOException {
/* 104 */     Content content1 = getTreeHeader();
/* 105 */     Content content2 = getResource("doclet.Hierarchy_For_All_Packages");
/* 106 */     HtmlTree htmlTree1 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, false, HtmlStyle.title, content2);
/*     */     
/* 108 */     HtmlTree htmlTree2 = HtmlTree.DIV(HtmlStyle.header, (Content)htmlTree1);
/* 109 */     addPackageTreeLinks((Content)htmlTree2);
/* 110 */     content1.addContent((Content)htmlTree2);
/* 111 */     HtmlTree htmlTree3 = new HtmlTree(HtmlTag.DIV);
/* 112 */     htmlTree3.addStyle(HtmlStyle.contentContainer);
/* 113 */     addTree(this.classtree.baseclasses(), "doclet.Class_Hierarchy", (Content)htmlTree3);
/* 114 */     addTree(this.classtree.baseinterfaces(), "doclet.Interface_Hierarchy", (Content)htmlTree3);
/* 115 */     addTree(this.classtree.baseAnnotationTypes(), "doclet.Annotation_Type_Hierarchy", (Content)htmlTree3);
/* 116 */     addTree(this.classtree.baseEnums(), "doclet.Enum_Hierarchy", (Content)htmlTree3);
/* 117 */     content1.addContent((Content)htmlTree3);
/* 118 */     addNavLinks(false, content1);
/* 119 */     addBottom(content1);
/* 120 */     printHtmlDocument((String[])null, true, content1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addPackageTreeLinks(Content paramContent) {
/* 130 */     if (this.packages.length == 1 && this.packages[0].name().length() == 0) {
/*     */       return;
/*     */     }
/* 133 */     if (!this.classesonly) {
/* 134 */       HtmlTree htmlTree1 = HtmlTree.SPAN(HtmlStyle.packageHierarchyLabel, 
/* 135 */           getResource("doclet.Package_Hierarchies"));
/* 136 */       paramContent.addContent((Content)htmlTree1);
/* 137 */       HtmlTree htmlTree2 = new HtmlTree(HtmlTag.UL);
/* 138 */       htmlTree2.addStyle(HtmlStyle.horizontal);
/* 139 */       for (byte b = 0; b < this.packages.length; b++) {
/*     */ 
/*     */ 
/*     */         
/* 143 */         if (this.packages[b].name().length() != 0 && (!this.configuration.nodeprecated || 
/* 144 */           !Util.isDeprecated((Doc)this.packages[b]))) {
/*     */ 
/*     */           
/* 147 */           DocPath docPath = pathString(this.packages[b], DocPaths.PACKAGE_TREE);
/* 148 */           HtmlTree htmlTree = HtmlTree.LI(getHyperLink(docPath, (Content)new StringContent(this.packages[b]
/* 149 */                   .name())));
/* 150 */           if (b < this.packages.length - 1) {
/* 151 */             htmlTree.addContent(", ");
/*     */           }
/* 153 */           htmlTree2.addContent((Content)htmlTree);
/*     */         } 
/* 155 */       }  paramContent.addContent((Content)htmlTree2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getTreeHeader() {
/* 165 */     String str = this.configuration.getText("doclet.Window_Class_Hierarchy");
/* 166 */     HtmlTree htmlTree = getBody(true, getWindowTitle(str));
/* 167 */     addTop((Content)htmlTree);
/* 168 */     addNavLinks(true, (Content)htmlTree);
/* 169 */     return (Content)htmlTree;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\TreeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */