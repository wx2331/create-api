/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PackageTreeWriter
/*     */   extends AbstractTreeWriter
/*     */ {
/*     */   protected PackageDoc packagedoc;
/*     */   protected PackageDoc prev;
/*     */   protected PackageDoc next;
/*     */   
/*     */   public PackageTreeWriter(ConfigurationImpl paramConfigurationImpl, DocPath paramDocPath, PackageDoc paramPackageDoc1, PackageDoc paramPackageDoc2, PackageDoc paramPackageDoc3) throws IOException {
/*  74 */     super(paramConfigurationImpl, paramDocPath, new ClassTree(paramConfigurationImpl.classDocCatalog
/*     */           
/*  76 */           .allClasses(paramPackageDoc1), paramConfigurationImpl));
/*     */     
/*  78 */     this.packagedoc = paramPackageDoc1;
/*  79 */     this.prev = paramPackageDoc2;
/*  80 */     this.next = paramPackageDoc3;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, PackageDoc paramPackageDoc1, PackageDoc paramPackageDoc2, PackageDoc paramPackageDoc3, boolean paramBoolean) {
/*  98 */     DocPath docPath = DocPath.forPackage(paramPackageDoc1).resolve(DocPaths.PACKAGE_TREE);
/*     */     try {
/* 100 */       PackageTreeWriter packageTreeWriter = new PackageTreeWriter(paramConfigurationImpl, docPath, paramPackageDoc1, paramPackageDoc2, paramPackageDoc3);
/*     */       
/* 102 */       packageTreeWriter.generatePackageTreeFile();
/* 103 */       packageTreeWriter.close();
/* 104 */     } catch (IOException iOException) {
/* 105 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { iOException
/*     */             
/* 107 */             .toString(), docPath.getPath() });
/* 108 */       throw new DocletAbortException(iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generatePackageTreeFile() throws IOException {
/* 116 */     Content content1 = getPackageTreeHeader();
/* 117 */     Content content2 = getResource("doclet.Hierarchy_For_Package", 
/* 118 */         Util.getPackageName(this.packagedoc));
/* 119 */     HtmlTree htmlTree1 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, false, HtmlStyle.title, content2);
/*     */     
/* 121 */     HtmlTree htmlTree2 = HtmlTree.DIV(HtmlStyle.header, (Content)htmlTree1);
/* 122 */     if (this.configuration.packages.length > 1) {
/* 123 */       addLinkToMainTree((Content)htmlTree2);
/*     */     }
/* 125 */     content1.addContent((Content)htmlTree2);
/* 126 */     HtmlTree htmlTree3 = new HtmlTree(HtmlTag.DIV);
/* 127 */     htmlTree3.addStyle(HtmlStyle.contentContainer);
/* 128 */     addTree(this.classtree.baseclasses(), "doclet.Class_Hierarchy", (Content)htmlTree3);
/* 129 */     addTree(this.classtree.baseinterfaces(), "doclet.Interface_Hierarchy", (Content)htmlTree3);
/* 130 */     addTree(this.classtree.baseAnnotationTypes(), "doclet.Annotation_Type_Hierarchy", (Content)htmlTree3);
/* 131 */     addTree(this.classtree.baseEnums(), "doclet.Enum_Hierarchy", (Content)htmlTree3);
/* 132 */     content1.addContent((Content)htmlTree3);
/* 133 */     addNavLinks(false, content1);
/* 134 */     addBottom(content1);
/* 135 */     printHtmlDocument((String[])null, true, content1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getPackageTreeHeader() {
/* 145 */     String str = this.packagedoc.name() + " " + this.configuration.getText("doclet.Window_Class_Hierarchy");
/* 146 */     HtmlTree htmlTree = getBody(true, getWindowTitle(str));
/* 147 */     addTop((Content)htmlTree);
/* 148 */     addNavLinks(true, (Content)htmlTree);
/* 149 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addLinkToMainTree(Content paramContent) {
/* 158 */     HtmlTree htmlTree1 = HtmlTree.SPAN(HtmlStyle.packageHierarchyLabel, 
/* 159 */         getResource("doclet.Package_Hierarchies"));
/* 160 */     paramContent.addContent((Content)htmlTree1);
/* 161 */     HtmlTree htmlTree2 = new HtmlTree(HtmlTag.UL);
/* 162 */     htmlTree2.addStyle(HtmlStyle.horizontal);
/* 163 */     htmlTree2.addContent(getNavLinkMainTree(this.configuration.getText("doclet.All_Packages")));
/* 164 */     paramContent.addContent((Content)htmlTree2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkPrevious() {
/* 173 */     if (this.prev == null) {
/* 174 */       return getNavLinkPrevious((DocPath)null);
/*     */     }
/* 176 */     DocPath docPath = DocPath.relativePath(this.packagedoc, this.prev);
/* 177 */     return getNavLinkPrevious(docPath.resolve(DocPaths.PACKAGE_TREE));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkNext() {
/* 187 */     if (this.next == null) {
/* 188 */       return getNavLinkNext((DocPath)null);
/*     */     }
/* 190 */     DocPath docPath = DocPath.relativePath(this.packagedoc, this.next);
/* 191 */     return getNavLinkNext(docPath.resolve(DocPaths.PACKAGE_TREE));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkPackage() {
/* 201 */     Content content = getHyperLink(DocPaths.PACKAGE_SUMMARY, this.packageLabel);
/*     */     
/* 203 */     return (Content)HtmlTree.LI(content);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\PackageTreeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */