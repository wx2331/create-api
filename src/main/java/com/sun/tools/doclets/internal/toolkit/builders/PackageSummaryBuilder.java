/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.PackageSummaryWriter;
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class PackageSummaryBuilder
/*     */   extends AbstractBuilder
/*     */ {
/*     */   public static final String ROOT = "PackageDoc";
/*     */   private final PackageDoc packageDoc;
/*     */   private final PackageSummaryWriter packageWriter;
/*     */   private Content contentTree;
/*     */
/*     */   private PackageSummaryBuilder(Context paramContext, PackageDoc paramPackageDoc, PackageSummaryWriter paramPackageSummaryWriter) {
/*  78 */     super(paramContext);
/*  79 */     this.packageDoc = paramPackageDoc;
/*  80 */     this.packageWriter = paramPackageSummaryWriter;
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
/*     */   public static PackageSummaryBuilder getInstance(Context paramContext, PackageDoc paramPackageDoc, PackageSummaryWriter paramPackageSummaryWriter) {
/*  95 */     return new PackageSummaryBuilder(paramContext, paramPackageDoc, paramPackageSummaryWriter);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void build() throws IOException {
/* 102 */     if (this.packageWriter == null) {
/*     */       return;
/*     */     }
/*     */
/* 106 */     build(this.layoutParser.parseXML("PackageDoc"), this.contentTree);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public String getName() {
/* 113 */     return "PackageDoc";
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildPackageDoc(XMLNode paramXMLNode, Content paramContent) throws Exception {
/* 123 */     paramContent = this.packageWriter.getPackageHeader(Util.getPackageName(this.packageDoc));
/* 124 */     buildChildren(paramXMLNode, paramContent);
/* 125 */     this.packageWriter.addPackageFooter(paramContent);
/* 126 */     this.packageWriter.printDocument(paramContent);
/* 127 */     this.packageWriter.close();
/* 128 */     Util.copyDocFiles(this.configuration, this.packageDoc);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildContent(XMLNode paramXMLNode, Content paramContent) {
/* 139 */     Content content = this.packageWriter.getContentHeader();
/* 140 */     buildChildren(paramXMLNode, content);
/* 141 */     paramContent.addContent(content);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildSummary(XMLNode paramXMLNode, Content paramContent) {
/* 152 */     Content content = this.packageWriter.getSummaryHeader();
/* 153 */     buildChildren(paramXMLNode, content);
/* 154 */     paramContent.addContent(content);
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
/*     */   public void buildInterfaceSummary(XMLNode paramXMLNode, Content paramContent) {
/* 166 */     String str = this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 167 */         .getText("doclet.Interface_Summary"), this.configuration
/* 168 */         .getText("doclet.interfaces"));
/*     */
/*     */
/* 171 */     String[] arrayOfString = { this.configuration.getText("doclet.Interface"), this.configuration.getText("doclet.Description") };
/*     */
/*     */
/*     */
/*     */
/* 176 */     ClassDoc[] arrayOfClassDoc = this.packageDoc.isIncluded() ? this.packageDoc.interfaces() : this.configuration.classDocCatalog.interfaces(
/* 177 */         Util.getPackageName(this.packageDoc));
/* 178 */     arrayOfClassDoc = Util.filterOutPrivateClasses(arrayOfClassDoc, this.configuration.javafx);
/* 179 */     if (arrayOfClassDoc.length > 0) {
/* 180 */       this.packageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/*     */
/* 182 */           .getText("doclet.Interface_Summary"), str, arrayOfString, paramContent);
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
/*     */   public void buildClassSummary(XMLNode paramXMLNode, Content paramContent) {
/* 196 */     String str = this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 197 */         .getText("doclet.Class_Summary"), this.configuration
/* 198 */         .getText("doclet.classes"));
/*     */
/*     */
/* 201 */     String[] arrayOfString = { this.configuration.getText("doclet.Class"), this.configuration.getText("doclet.Description") };
/*     */
/*     */
/*     */
/*     */
/* 206 */     ClassDoc[] arrayOfClassDoc = this.packageDoc.isIncluded() ? this.packageDoc.ordinaryClasses() : this.configuration.classDocCatalog.ordinaryClasses(
/* 207 */         Util.getPackageName(this.packageDoc));
/* 208 */     arrayOfClassDoc = Util.filterOutPrivateClasses(arrayOfClassDoc, this.configuration.javafx);
/* 209 */     if (arrayOfClassDoc.length > 0) {
/* 210 */       this.packageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/*     */
/* 212 */           .getText("doclet.Class_Summary"), str, arrayOfString, paramContent);
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
/*     */   public void buildEnumSummary(XMLNode paramXMLNode, Content paramContent) {
/* 226 */     String str = this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 227 */         .getText("doclet.Enum_Summary"), this.configuration
/* 228 */         .getText("doclet.enums"));
/*     */
/*     */
/* 231 */     String[] arrayOfString = { this.configuration.getText("doclet.Enum"), this.configuration.getText("doclet.Description") };
/*     */
/*     */
/*     */
/*     */
/* 236 */     ClassDoc[] arrayOfClassDoc = this.packageDoc.isIncluded() ? this.packageDoc.enums() : this.configuration.classDocCatalog.enums(
/* 237 */         Util.getPackageName(this.packageDoc));
/* 238 */     arrayOfClassDoc = Util.filterOutPrivateClasses(arrayOfClassDoc, this.configuration.javafx);
/* 239 */     if (arrayOfClassDoc.length > 0) {
/* 240 */       this.packageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/*     */
/* 242 */           .getText("doclet.Enum_Summary"), str, arrayOfString, paramContent);
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
/*     */   public void buildExceptionSummary(XMLNode paramXMLNode, Content paramContent) {
/* 256 */     String str = this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 257 */         .getText("doclet.Exception_Summary"), this.configuration
/* 258 */         .getText("doclet.exceptions"));
/*     */
/*     */
/* 261 */     String[] arrayOfString = { this.configuration.getText("doclet.Exception"), this.configuration.getText("doclet.Description") };
/*     */
/*     */
/*     */
/*     */
/* 266 */     ClassDoc[] arrayOfClassDoc = this.packageDoc.isIncluded() ? this.packageDoc.exceptions() : this.configuration.classDocCatalog.exceptions(
/* 267 */         Util.getPackageName(this.packageDoc));
/* 268 */     arrayOfClassDoc = Util.filterOutPrivateClasses(arrayOfClassDoc, this.configuration.javafx);
/* 269 */     if (arrayOfClassDoc.length > 0) {
/* 270 */       this.packageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/*     */
/* 272 */           .getText("doclet.Exception_Summary"), str, arrayOfString, paramContent);
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
/*     */   public void buildErrorSummary(XMLNode paramXMLNode, Content paramContent) {
/* 286 */     String str = this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 287 */         .getText("doclet.Error_Summary"), this.configuration
/* 288 */         .getText("doclet.errors"));
/*     */
/*     */
/* 291 */     String[] arrayOfString = { this.configuration.getText("doclet.Error"), this.configuration.getText("doclet.Description") };
/*     */
/*     */
/*     */
/*     */
/* 296 */     ClassDoc[] arrayOfClassDoc = this.packageDoc.isIncluded() ? this.packageDoc.errors() : this.configuration.classDocCatalog.errors(
/* 297 */         Util.getPackageName(this.packageDoc));
/* 298 */     arrayOfClassDoc = Util.filterOutPrivateClasses(arrayOfClassDoc, this.configuration.javafx);
/* 299 */     if (arrayOfClassDoc.length > 0) {
/* 300 */       this.packageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/*     */
/* 302 */           .getText("doclet.Error_Summary"), str, arrayOfString, paramContent);
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
/*     */   public void buildAnnotationTypeSummary(XMLNode paramXMLNode, Content paramContent) {
/* 316 */     String str = this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 317 */         .getText("doclet.Annotation_Types_Summary"), this.configuration
/* 318 */         .getText("doclet.annotationtypes"));
/*     */
/*     */
/* 321 */     String[] arrayOfString = { this.configuration.getText("doclet.AnnotationType"), this.configuration.getText("doclet.Description") };
/*     */
/*     */
/*     */
/*     */
/* 326 */     ClassDoc[] arrayOfClassDoc = (ClassDoc[])(this.packageDoc.isIncluded() ? this.packageDoc.annotationTypes() : this.configuration.classDocCatalog.annotationTypes(
/* 327 */         Util.getPackageName(this.packageDoc)));
/* 328 */     arrayOfClassDoc = Util.filterOutPrivateClasses(arrayOfClassDoc, this.configuration.javafx);
/* 329 */     if (arrayOfClassDoc.length > 0) {
/* 330 */       this.packageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/*     */
/* 332 */           .getText("doclet.Annotation_Types_Summary"), str, arrayOfString, paramContent);
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
/*     */   public void buildPackageDescription(XMLNode paramXMLNode, Content paramContent) {
/* 346 */     if (this.configuration.nocomment) {
/*     */       return;
/*     */     }
/* 349 */     this.packageWriter.addPackageDescription(paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildPackageTags(XMLNode paramXMLNode, Content paramContent) {
/* 359 */     if (this.configuration.nocomment) {
/*     */       return;
/*     */     }
/* 362 */     this.packageWriter.addPackageTags(paramContent);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\builders\PackageSummaryBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
