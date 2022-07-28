/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.ProfilePackageSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.javac.jvm.Profile;
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class ProfilePackageSummaryBuilder
/*     */   extends AbstractBuilder
/*     */ {
/*     */   public static final String ROOT = "PackageDoc";
/*     */   private final PackageDoc packageDoc;
/*     */   private final String profileName;
/*     */   private final int profileValue;
/*     */   private final ProfilePackageSummaryWriter profilePackageWriter;
/*     */   private Content contentTree;
/*     */
/*     */   private ProfilePackageSummaryBuilder(Context paramContext, PackageDoc paramPackageDoc, ProfilePackageSummaryWriter paramProfilePackageSummaryWriter, Profile paramProfile) {
/*  88 */     super(paramContext);
/*  89 */     this.packageDoc = paramPackageDoc;
/*  90 */     this.profilePackageWriter = paramProfilePackageSummaryWriter;
/*  91 */     this.profileName = paramProfile.name;
/*  92 */     this.profileValue = paramProfile.value;
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
/*     */   public static ProfilePackageSummaryBuilder getInstance(Context paramContext, PackageDoc paramPackageDoc, ProfilePackageSummaryWriter paramProfilePackageSummaryWriter, Profile paramProfile) {
/* 109 */     return new ProfilePackageSummaryBuilder(paramContext, paramPackageDoc, paramProfilePackageSummaryWriter, paramProfile);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void build() throws IOException {
/* 117 */     if (this.profilePackageWriter == null) {
/*     */       return;
/*     */     }
/*     */
/* 121 */     build(this.layoutParser.parseXML("PackageDoc"), this.contentTree);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public String getName() {
/* 128 */     return "PackageDoc";
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildPackageDoc(XMLNode paramXMLNode, Content paramContent) throws Exception {
/* 138 */     paramContent = this.profilePackageWriter.getPackageHeader(
/* 139 */         Util.getPackageName(this.packageDoc));
/* 140 */     buildChildren(paramXMLNode, paramContent);
/* 141 */     this.profilePackageWriter.addPackageFooter(paramContent);
/* 142 */     this.profilePackageWriter.printDocument(paramContent);
/* 143 */     this.profilePackageWriter.close();
/* 144 */     Util.copyDocFiles(this.configuration, this.packageDoc);
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
/* 155 */     Content content = this.profilePackageWriter.getContentHeader();
/* 156 */     buildChildren(paramXMLNode, content);
/* 157 */     paramContent.addContent(content);
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
/* 168 */     Content content = this.profilePackageWriter.getSummaryHeader();
/* 169 */     buildChildren(paramXMLNode, content);
/* 170 */     paramContent.addContent(content);
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
/* 182 */     String str = this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 183 */         .getText("doclet.Interface_Summary"), this.configuration
/* 184 */         .getText("doclet.interfaces"));
/*     */
/*     */
/* 187 */     String[] arrayOfString = { this.configuration.getText("doclet.Interface"), this.configuration.getText("doclet.Description") };
/*     */
/*     */
/*     */
/*     */
/* 192 */     ClassDoc[] arrayOfClassDoc = this.packageDoc.isIncluded() ? this.packageDoc.interfaces() : this.configuration.classDocCatalog.interfaces(
/* 193 */         Util.getPackageName(this.packageDoc));
/* 194 */     if (arrayOfClassDoc.length > 0) {
/* 195 */       this.profilePackageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/*     */
/* 197 */           .getText("doclet.Interface_Summary"), str, arrayOfString, paramContent);
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
/* 211 */     String str = this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 212 */         .getText("doclet.Class_Summary"), this.configuration
/* 213 */         .getText("doclet.classes"));
/*     */
/*     */
/* 216 */     String[] arrayOfString = { this.configuration.getText("doclet.Class"), this.configuration.getText("doclet.Description") };
/*     */
/*     */
/*     */
/*     */
/* 221 */     ClassDoc[] arrayOfClassDoc = this.packageDoc.isIncluded() ? this.packageDoc.ordinaryClasses() : this.configuration.classDocCatalog.ordinaryClasses(
/* 222 */         Util.getPackageName(this.packageDoc));
/* 223 */     if (arrayOfClassDoc.length > 0) {
/* 224 */       this.profilePackageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/*     */
/* 226 */           .getText("doclet.Class_Summary"), str, arrayOfString, paramContent);
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
/* 240 */     String str = this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 241 */         .getText("doclet.Enum_Summary"), this.configuration
/* 242 */         .getText("doclet.enums"));
/*     */
/*     */
/* 245 */     String[] arrayOfString = { this.configuration.getText("doclet.Enum"), this.configuration.getText("doclet.Description") };
/*     */
/*     */
/*     */
/*     */
/* 250 */     ClassDoc[] arrayOfClassDoc = this.packageDoc.isIncluded() ? this.packageDoc.enums() : this.configuration.classDocCatalog.enums(
/* 251 */         Util.getPackageName(this.packageDoc));
/* 252 */     if (arrayOfClassDoc.length > 0) {
/* 253 */       this.profilePackageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/*     */
/* 255 */           .getText("doclet.Enum_Summary"), str, arrayOfString, paramContent);
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
/* 269 */     String str = this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 270 */         .getText("doclet.Exception_Summary"), this.configuration
/* 271 */         .getText("doclet.exceptions"));
/*     */
/*     */
/* 274 */     String[] arrayOfString = { this.configuration.getText("doclet.Exception"), this.configuration.getText("doclet.Description") };
/*     */
/*     */
/*     */
/*     */
/* 279 */     ClassDoc[] arrayOfClassDoc = this.packageDoc.isIncluded() ? this.packageDoc.exceptions() : this.configuration.classDocCatalog.exceptions(
/* 280 */         Util.getPackageName(this.packageDoc));
/* 281 */     if (arrayOfClassDoc.length > 0) {
/* 282 */       this.profilePackageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/*     */
/* 284 */           .getText("doclet.Exception_Summary"), str, arrayOfString, paramContent);
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
/* 298 */     String str = this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 299 */         .getText("doclet.Error_Summary"), this.configuration
/* 300 */         .getText("doclet.errors"));
/*     */
/*     */
/* 303 */     String[] arrayOfString = { this.configuration.getText("doclet.Error"), this.configuration.getText("doclet.Description") };
/*     */
/*     */
/*     */
/*     */
/* 308 */     ClassDoc[] arrayOfClassDoc = this.packageDoc.isIncluded() ? this.packageDoc.errors() : this.configuration.classDocCatalog.errors(
/* 309 */         Util.getPackageName(this.packageDoc));
/* 310 */     if (arrayOfClassDoc.length > 0) {
/* 311 */       this.profilePackageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/*     */
/* 313 */           .getText("doclet.Error_Summary"), str, arrayOfString, paramContent);
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
/* 327 */     String str = this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 328 */         .getText("doclet.Annotation_Types_Summary"), this.configuration
/* 329 */         .getText("doclet.annotationtypes"));
/*     */
/*     */
/* 332 */     String[] arrayOfString = { this.configuration.getText("doclet.AnnotationType"), this.configuration.getText("doclet.Description") };
/*     */
/*     */
/*     */
/*     */
/* 337 */     ClassDoc[] arrayOfClassDoc = (ClassDoc[])(this.packageDoc.isIncluded() ? this.packageDoc.annotationTypes() : this.configuration.classDocCatalog.annotationTypes(
/* 338 */         Util.getPackageName(this.packageDoc)));
/* 339 */     if (arrayOfClassDoc.length > 0) {
/* 340 */       this.profilePackageWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/*     */
/* 342 */           .getText("doclet.Annotation_Types_Summary"), str, arrayOfString, paramContent);
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
/* 356 */     if (this.configuration.nocomment) {
/*     */       return;
/*     */     }
/* 359 */     this.profilePackageWriter.addPackageDescription(paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildPackageTags(XMLNode paramXMLNode, Content paramContent) {
/* 369 */     if (this.configuration.nocomment) {
/*     */       return;
/*     */     }
/* 372 */     this.profilePackageWriter.addPackageTags(paramContent);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\builders\ProfilePackageSummaryBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
