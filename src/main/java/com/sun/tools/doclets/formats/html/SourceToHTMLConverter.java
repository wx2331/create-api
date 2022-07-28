/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.tools.doclets.formats.html.markup.DocType;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlDocument;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFile;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletConstants;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.javadoc.SourcePositionImpl;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.LineNumberReader;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import javax.tools.FileObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SourceToHTMLConverter
/*     */ {
/*     */   private static final int NUM_BLANK_LINES = 60;
/*  61 */   private static final String NEW_LINE = DocletConstants.NL;
/*     */ 
/*     */   
/*     */   private final ConfigurationImpl configuration;
/*     */ 
/*     */   
/*     */   private final RootDoc rootDoc;
/*     */ 
/*     */   
/*     */   private DocPath outputdir;
/*     */ 
/*     */   
/*  73 */   private DocPath relativePath = DocPath.empty;
/*     */ 
/*     */   
/*     */   private SourceToHTMLConverter(ConfigurationImpl paramConfigurationImpl, RootDoc paramRootDoc, DocPath paramDocPath) {
/*  77 */     this.configuration = paramConfigurationImpl;
/*  78 */     this.rootDoc = paramRootDoc;
/*  79 */     this.outputdir = paramDocPath;
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
/*     */   public static void convertRoot(ConfigurationImpl paramConfigurationImpl, RootDoc paramRootDoc, DocPath paramDocPath) {
/*  91 */     (new SourceToHTMLConverter(paramConfigurationImpl, paramRootDoc, paramDocPath)).generate();
/*     */   }
/*     */   
/*     */   void generate() {
/*  95 */     if (this.rootDoc == null || this.outputdir == null) {
/*     */       return;
/*     */     }
/*  98 */     PackageDoc[] arrayOfPackageDoc = this.rootDoc.specifiedPackages();
/*  99 */     for (byte b1 = 0; b1 < arrayOfPackageDoc.length; b1++) {
/*     */ 
/*     */       
/* 102 */       if (!this.configuration.nodeprecated || !Util.isDeprecated((Doc)arrayOfPackageDoc[b1]))
/* 103 */         convertPackage(arrayOfPackageDoc[b1], this.outputdir); 
/*     */     } 
/* 105 */     ClassDoc[] arrayOfClassDoc = this.rootDoc.specifiedClasses();
/* 106 */     for (byte b2 = 0; b2 < arrayOfClassDoc.length; b2++) {
/*     */ 
/*     */ 
/*     */       
/* 110 */       if (!this.configuration.nodeprecated || (
/* 111 */         !Util.isDeprecated((Doc)arrayOfClassDoc[b2]) && !Util.isDeprecated((Doc)arrayOfClassDoc[b2].containingPackage()))) {
/* 112 */         convertClass(arrayOfClassDoc[b2], this.outputdir);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void convertPackage(PackageDoc paramPackageDoc, DocPath paramDocPath) {
/* 123 */     if (paramPackageDoc == null) {
/*     */       return;
/*     */     }
/* 126 */     ClassDoc[] arrayOfClassDoc = paramPackageDoc.allClasses();
/* 127 */     for (byte b = 0; b < arrayOfClassDoc.length; b++) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 132 */       if (!this.configuration.nodeprecated || !Util.isDeprecated((Doc)arrayOfClassDoc[b])) {
/* 133 */         convertClass(arrayOfClassDoc[b], paramDocPath);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void convertClass(ClassDoc paramClassDoc, DocPath paramDocPath) {
/* 144 */     if (paramClassDoc == null)
/*     */       return; 
/*     */     try {
/*     */       Reader reader;
/* 148 */       SourcePosition sourcePosition = paramClassDoc.position();
/* 149 */       if (sourcePosition == null) {
/*     */         return;
/*     */       }
/*     */       
/* 153 */       if (sourcePosition instanceof SourcePositionImpl) {
/* 154 */         FileObject fileObject = ((SourcePositionImpl)sourcePosition).fileObject();
/* 155 */         if (fileObject == null)
/*     */           return; 
/* 157 */         reader = fileObject.openReader(true);
/*     */       } else {
/* 159 */         File file = sourcePosition.file();
/* 160 */         if (file == null)
/*     */           return; 
/* 162 */         reader = new FileReader(file);
/*     */       } 
/* 164 */       LineNumberReader lineNumberReader = new LineNumberReader(reader);
/* 165 */       byte b = 1;
/*     */       
/* 167 */       this
/*     */         
/* 169 */         .relativePath = DocPaths.SOURCE_OUTPUT.resolve(DocPath.forPackage(paramClassDoc)).invert();
/* 170 */       Content content = getHeader();
/* 171 */       HtmlTree htmlTree1 = new HtmlTree(HtmlTag.PRE); try {
/*     */         String str;
/* 173 */         while ((str = lineNumberReader.readLine()) != null) {
/* 174 */           addLineNo((Content)htmlTree1, b);
/* 175 */           addLine((Content)htmlTree1, str, b);
/* 176 */           b++;
/*     */         } 
/*     */       } finally {
/* 179 */         lineNumberReader.close();
/*     */       } 
/* 181 */       addBlankLines((Content)htmlTree1);
/* 182 */       HtmlTree htmlTree2 = HtmlTree.DIV(HtmlStyle.sourceContainer, (Content)htmlTree1);
/* 183 */       content.addContent((Content)htmlTree2);
/* 184 */       writeToFile(content, paramDocPath.resolve(DocPath.forClass(paramClassDoc)));
/* 185 */     } catch (IOException iOException) {
/* 186 */       iOException.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeToFile(Content paramContent, DocPath paramDocPath) throws IOException {
/* 197 */     DocType docType = DocType.TRANSITIONAL;
/* 198 */     HtmlTree htmlTree1 = new HtmlTree(HtmlTag.HEAD);
/* 199 */     htmlTree1.addContent((Content)HtmlTree.TITLE((Content)new StringContent(this.configuration
/* 200 */             .getText("doclet.Window_Source_title"))));
/* 201 */     htmlTree1.addContent((Content)getStyleSheetProperties());
/* 202 */     HtmlTree htmlTree2 = HtmlTree.HTML(this.configuration.getLocale().getLanguage(), (Content)htmlTree1, paramContent);
/*     */     
/* 204 */     HtmlDocument htmlDocument = new HtmlDocument((Content)docType, (Content)htmlTree2);
/* 205 */     this.configuration.message.notice("doclet.Generating_0", new Object[] { paramDocPath.getPath() });
/* 206 */     DocFile docFile = DocFile.createFileForOutput(this.configuration, paramDocPath);
/* 207 */     Writer writer = docFile.openWriter();
/*     */     try {
/* 209 */       htmlDocument.write(writer, true);
/*     */     } finally {
/* 211 */       writer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HtmlTree getStyleSheetProperties() {
/*     */     DocPath docPath1;
/* 222 */     String str = this.configuration.stylesheetfile;
/*     */     
/* 224 */     if (str.length() > 0) {
/* 225 */       DocFile docFile = DocFile.createFileForInput(this.configuration, str);
/* 226 */       docPath1 = DocPath.create(docFile.getName());
/*     */     } else {
/* 228 */       docPath1 = DocPaths.STYLESHEET;
/*     */     } 
/* 230 */     DocPath docPath2 = this.relativePath.resolve(docPath1);
/* 231 */     return HtmlTree.LINK("stylesheet", "text/css", docPath2.getPath(), "Style");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Content getHeader() {
/* 241 */     return (Content)new HtmlTree(HtmlTag.BODY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addLineNo(Content paramContent, int paramInt) {
/* 251 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.SPAN);
/* 252 */     htmlTree.addStyle(HtmlStyle.sourceLineNo);
/* 253 */     if (paramInt < 10) {
/* 254 */       htmlTree.addContent("00" + Integer.toString(paramInt));
/* 255 */     } else if (paramInt < 100) {
/* 256 */       htmlTree.addContent("0" + Integer.toString(paramInt));
/*     */     } else {
/* 258 */       htmlTree.addContent(Integer.toString(paramInt));
/*     */     } 
/* 260 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addLine(Content paramContent, String paramString, int paramInt) {
/* 271 */     if (paramString != null) {
/* 272 */       paramContent.addContent(Util.replaceTabs(this.configuration, paramString));
/* 273 */       HtmlTree htmlTree = HtmlTree.A_NAME("line." + Integer.toString(paramInt));
/* 274 */       paramContent.addContent((Content)htmlTree);
/* 275 */       paramContent.addContent(NEW_LINE);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addBlankLines(Content paramContent) {
/* 285 */     for (byte b = 0; b < 60; b++) {
/* 286 */       paramContent.addContent(NEW_LINE);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getAnchorName(Doc paramDoc) {
/* 297 */     return "line." + paramDoc.position().line();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\SourceToHTMLConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */