/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.ContentBuilder;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassUseMapper;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PackageUseWriter
/*     */   extends SubWriterHolderWriter
/*     */ {
/*     */   final PackageDoc pkgdoc;
/*  50 */   final SortedMap<String, Set<ClassDoc>> usingPackageToUsedClasses = new TreeMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PackageUseWriter(ConfigurationImpl paramConfigurationImpl, ClassUseMapper paramClassUseMapper, DocPath paramDocPath, PackageDoc paramPackageDoc) throws IOException {
/*  62 */     super(paramConfigurationImpl, DocPath.forPackage(paramPackageDoc).resolve(paramDocPath));
/*  63 */     this.pkgdoc = paramPackageDoc;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  68 */     ClassDoc[] arrayOfClassDoc = paramPackageDoc.allClasses();
/*  69 */     for (byte b = 0; b < arrayOfClassDoc.length; b++) {
/*  70 */       ClassDoc classDoc = arrayOfClassDoc[b];
/*  71 */       Set set = (Set)paramClassUseMapper.classToClass.get(classDoc.qualifiedName());
/*  72 */       if (set != null) {
/*  73 */         for (ClassDoc classDoc1 : set) {
/*     */           
/*  75 */           PackageDoc packageDoc = classDoc1.containingPackage();
/*     */           
/*  77 */           Set<ClassDoc> set1 = this.usingPackageToUsedClasses.get(packageDoc.name());
/*  78 */           if (set1 == null) {
/*  79 */             set1 = new TreeSet();
/*  80 */             this.usingPackageToUsedClasses.put(Util.getPackageName(packageDoc), set1);
/*     */           } 
/*     */           
/*  83 */           set1.add(classDoc);
/*     */         } 
/*     */       }
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
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, ClassUseMapper paramClassUseMapper, PackageDoc paramPackageDoc) {
/*  99 */     DocPath docPath = DocPaths.PACKAGE_USE;
/*     */     try {
/* 101 */       PackageUseWriter packageUseWriter = new PackageUseWriter(paramConfigurationImpl, paramClassUseMapper, docPath, paramPackageDoc);
/*     */       
/* 103 */       packageUseWriter.generatePackageUseFile();
/* 104 */       packageUseWriter.close();
/* 105 */     } catch (IOException iOException) {
/* 106 */       paramConfigurationImpl.standardmessage.error("doclet.exception_encountered", new Object[] { iOException
/*     */             
/* 108 */             .toString(), docPath });
/* 109 */       throw new DocletAbortException(iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generatePackageUseFile() throws IOException {
/* 118 */     Content content = getPackageUseHeader();
/* 119 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.DIV);
/* 120 */     htmlTree.addStyle(HtmlStyle.contentContainer);
/* 121 */     if (this.usingPackageToUsedClasses.isEmpty()) {
/* 122 */       htmlTree.addContent(getResource("doclet.ClassUse_No.usage.of.0", this.pkgdoc
/* 123 */             .name()));
/*     */     } else {
/* 125 */       addPackageUse((Content)htmlTree);
/*     */     } 
/* 127 */     content.addContent((Content)htmlTree);
/* 128 */     addNavLinks(false, content);
/* 129 */     addBottom(content);
/* 130 */     printHtmlDocument((String[])null, true, content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addPackageUse(Content paramContent) throws IOException {
/* 139 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.UL);
/* 140 */     htmlTree.addStyle(HtmlStyle.blockList);
/* 141 */     if (this.configuration.packages.length > 1) {
/* 142 */       addPackageList((Content)htmlTree);
/*     */     }
/* 144 */     addClassList((Content)htmlTree);
/* 145 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addPackageList(Content paramContent) throws IOException {
/* 154 */     HtmlTree htmlTree1 = HtmlTree.TABLE(HtmlStyle.useSummary, 0, 3, 0, this.useTableSummary, 
/* 155 */         getTableCaption(this.configuration.getResource("doclet.ClassUse_Packages.that.use.0", 
/*     */             
/* 157 */             getPackageLink(this.pkgdoc, Util.getPackageName(this.pkgdoc)))));
/* 158 */     htmlTree1.addContent(getSummaryTableHeader(this.packageTableHeader, "col"));
/* 159 */     HtmlTree htmlTree2 = new HtmlTree(HtmlTag.TBODY);
/* 160 */     Iterator<String> iterator = this.usingPackageToUsedClasses.keySet().iterator();
/* 161 */     for (byte b = 0; iterator.hasNext(); b++) {
/* 162 */       PackageDoc packageDoc = this.configuration.root.packageNamed(iterator.next());
/* 163 */       HtmlTree htmlTree = new HtmlTree(HtmlTag.TR);
/* 164 */       if (b % 2 == 0) {
/* 165 */         htmlTree.addStyle(HtmlStyle.altColor);
/*     */       } else {
/* 167 */         htmlTree.addStyle(HtmlStyle.rowColor);
/*     */       } 
/* 169 */       addPackageUse(packageDoc, (Content)htmlTree);
/* 170 */       htmlTree2.addContent((Content)htmlTree);
/*     */     } 
/* 172 */     htmlTree1.addContent((Content)htmlTree2);
/* 173 */     HtmlTree htmlTree3 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree1);
/* 174 */     paramContent.addContent((Content)htmlTree3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addClassList(Content paramContent) throws IOException {
/* 184 */     String[] arrayOfString = { this.configuration.getText("doclet.0_and_1", this.configuration
/* 185 */           .getText("doclet.Class"), this.configuration
/* 186 */           .getText("doclet.Description")) };
/*     */     
/* 188 */     Iterator<String> iterator = this.usingPackageToUsedClasses.keySet().iterator();
/* 189 */     while (iterator.hasNext()) {
/* 190 */       String str1 = iterator.next();
/* 191 */       PackageDoc packageDoc = this.configuration.root.packageNamed(str1);
/* 192 */       HtmlTree htmlTree1 = new HtmlTree(HtmlTag.LI);
/* 193 */       htmlTree1.addStyle(HtmlStyle.blockList);
/* 194 */       if (packageDoc != null) {
/* 195 */         htmlTree1.addContent(getMarkerAnchor(packageDoc.name()));
/*     */       }
/* 197 */       String str2 = this.configuration.getText("doclet.Use_Table_Summary", this.configuration
/* 198 */           .getText("doclet.classes"));
/* 199 */       HtmlTree htmlTree2 = HtmlTree.TABLE(HtmlStyle.useSummary, 0, 3, 0, str2, 
/* 200 */           getTableCaption(this.configuration.getResource("doclet.ClassUse_Classes.in.0.used.by.1", 
/*     */               
/* 202 */               getPackageLink(this.pkgdoc, Util.getPackageName(this.pkgdoc)), 
/* 203 */               getPackageLink(packageDoc, Util.getPackageName(packageDoc)))));
/* 204 */       htmlTree2.addContent(getSummaryTableHeader(arrayOfString, "col"));
/* 205 */       HtmlTree htmlTree3 = new HtmlTree(HtmlTag.TBODY);
/*     */       
/* 207 */       Iterator<ClassDoc> iterator1 = ((Set)this.usingPackageToUsedClasses.get(str1)).iterator();
/* 208 */       for (byte b = 0; iterator1.hasNext(); b++) {
/* 209 */         HtmlTree htmlTree = new HtmlTree(HtmlTag.TR);
/* 210 */         if (b % 2 == 0) {
/* 211 */           htmlTree.addStyle(HtmlStyle.altColor);
/*     */         } else {
/* 213 */           htmlTree.addStyle(HtmlStyle.rowColor);
/*     */         } 
/* 215 */         addClassRow(iterator1.next(), str1, (Content)htmlTree);
/* 216 */         htmlTree3.addContent((Content)htmlTree);
/*     */       } 
/* 218 */       htmlTree2.addContent((Content)htmlTree3);
/* 219 */       htmlTree1.addContent((Content)htmlTree2);
/* 220 */       paramContent.addContent((Content)htmlTree1);
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
/*     */   protected void addClassRow(ClassDoc paramClassDoc, String paramString, Content paramContent) {
/* 233 */     DocPath docPath = pathString(paramClassDoc, DocPaths.CLASS_USE
/* 234 */         .resolve(DocPath.forName(paramClassDoc)));
/* 235 */     HtmlTree htmlTree = HtmlTree.TD(HtmlStyle.colOne, 
/* 236 */         getHyperLink(docPath.fragment(paramString), (Content)new StringContent(paramClassDoc.name())));
/* 237 */     addIndexComment((Doc)paramClassDoc, (Content)htmlTree);
/* 238 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addPackageUse(PackageDoc paramPackageDoc, Content paramContent) throws IOException {
/* 248 */     HtmlTree htmlTree1 = HtmlTree.TD(HtmlStyle.colFirst, 
/* 249 */         getHyperLink(Util.getPackageName(paramPackageDoc), (Content)new StringContent(
/* 250 */             Util.getPackageName(paramPackageDoc))));
/* 251 */     paramContent.addContent((Content)htmlTree1);
/* 252 */     HtmlTree htmlTree2 = new HtmlTree(HtmlTag.TD);
/* 253 */     htmlTree2.addStyle(HtmlStyle.colLast);
/* 254 */     if (paramPackageDoc != null && paramPackageDoc.name().length() != 0) {
/* 255 */       addSummaryComment((Doc)paramPackageDoc, (Content)htmlTree2);
/*     */     } else {
/* 257 */       htmlTree2.addContent(getSpace());
/*     */     } 
/* 259 */     paramContent.addContent((Content)htmlTree2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getPackageUseHeader() {
/* 268 */     String str1 = this.configuration.getText("doclet.Package");
/* 269 */     String str2 = this.pkgdoc.name();
/* 270 */     String str3 = this.configuration.getText("doclet.Window_ClassUse_Header", str1, str2);
/*     */     
/* 272 */     HtmlTree htmlTree1 = getBody(true, getWindowTitle(str3));
/* 273 */     addTop((Content)htmlTree1);
/* 274 */     addNavLinks(true, (Content)htmlTree1);
/* 275 */     ContentBuilder contentBuilder = new ContentBuilder();
/* 276 */     contentBuilder.addContent(getResource("doclet.ClassUse_Title", str1));
/* 277 */     contentBuilder.addContent((Content)new HtmlTree(HtmlTag.BR));
/* 278 */     contentBuilder.addContent(str2);
/* 279 */     HtmlTree htmlTree2 = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true, HtmlStyle.title, (Content)contentBuilder);
/*     */     
/* 281 */     HtmlTree htmlTree3 = HtmlTree.DIV(HtmlStyle.header, (Content)htmlTree2);
/* 282 */     htmlTree1.addContent((Content)htmlTree3);
/* 283 */     return (Content)htmlTree1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkPackage() {
/* 292 */     Content content = getHyperLink(DocPaths.PACKAGE_SUMMARY, this.packageLabel);
/*     */     
/* 294 */     return (Content)HtmlTree.LI(content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkClassUse() {
/* 304 */     return (Content)HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.useLabel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkTree() {
/* 314 */     Content content = getHyperLink(DocPaths.PACKAGE_TREE, this.treeLabel);
/*     */     
/* 316 */     return (Content)HtmlTree.LI(content);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\PackageUseWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */