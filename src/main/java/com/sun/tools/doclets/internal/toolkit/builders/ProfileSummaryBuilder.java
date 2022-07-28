/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.ProfileSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
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
/*     */ public class ProfileSummaryBuilder
/*     */   extends AbstractBuilder
/*     */ {
/*     */   public static final String ROOT = "ProfileDoc";
/*     */   private final Profile profile;
/*     */   private final ProfileSummaryWriter profileWriter;
/*     */   private Content contentTree;
/*     */   private PackageDoc pkg;
/*     */
/*     */   private ProfileSummaryBuilder(Context paramContext, Profile paramProfile, ProfileSummaryWriter paramProfileSummaryWriter) {
/*  81 */     super(paramContext);
/*  82 */     this.profile = paramProfile;
/*  83 */     this.profileWriter = paramProfileSummaryWriter;
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
/*     */   public static ProfileSummaryBuilder getInstance(Context paramContext, Profile paramProfile, ProfileSummaryWriter paramProfileSummaryWriter) {
/*  98 */     return new ProfileSummaryBuilder(paramContext, paramProfile, paramProfileSummaryWriter);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void build() throws IOException {
/* 105 */     if (this.profileWriter == null) {
/*     */       return;
/*     */     }
/*     */
/* 109 */     build(this.layoutParser.parseXML("ProfileDoc"), this.contentTree);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public String getName() {
/* 116 */     return "ProfileDoc";
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildProfileDoc(XMLNode paramXMLNode, Content paramContent) throws Exception {
/* 126 */     paramContent = this.profileWriter.getProfileHeader(this.profile.name);
/* 127 */     buildChildren(paramXMLNode, paramContent);
/* 128 */     this.profileWriter.addProfileFooter(paramContent);
/* 129 */     this.profileWriter.printDocument(paramContent);
/* 130 */     this.profileWriter.close();
/* 131 */     Util.copyDocFiles(this.configuration, DocPaths.profileSummary(this.profile.name));
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
/* 142 */     Content content = this.profileWriter.getContentHeader();
/* 143 */     buildChildren(paramXMLNode, content);
/* 144 */     paramContent.addContent(content);
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
/* 155 */     Content content = this.profileWriter.getSummaryHeader();
/* 156 */     buildChildren(paramXMLNode, content);
/* 157 */     paramContent.addContent(this.profileWriter.getSummaryTree(content));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildPackageSummary(XMLNode paramXMLNode, Content paramContent) {
/* 168 */     PackageDoc[] arrayOfPackageDoc = (PackageDoc[])this.configuration.profilePackages.get(this.profile.name);
/* 169 */     for (byte b = 0; b < arrayOfPackageDoc.length; b++) {
/* 170 */       this.pkg = arrayOfPackageDoc[b];
/* 171 */       Content content = this.profileWriter.getPackageSummaryHeader(this.pkg);
/* 172 */       buildChildren(paramXMLNode, content);
/* 173 */       paramContent.addContent(this.profileWriter.getPackageSummaryTree(content));
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
/*     */   public void buildInterfaceSummary(XMLNode paramXMLNode, Content paramContent) {
/* 187 */     String str = this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 188 */         .getText("doclet.Interface_Summary"), this.configuration
/* 189 */         .getText("doclet.interfaces"));
/*     */
/*     */
/* 192 */     String[] arrayOfString = { this.configuration.getText("doclet.Interface"), this.configuration.getText("doclet.Description") };
/*     */
/* 194 */     ClassDoc[] arrayOfClassDoc = this.pkg.interfaces();
/* 195 */     if (arrayOfClassDoc.length > 0) {
/* 196 */       this.profileWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/*     */
/* 198 */           .getText("doclet.Interface_Summary"), str, arrayOfString, paramContent);
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
/* 212 */     String str = this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 213 */         .getText("doclet.Class_Summary"), this.configuration
/* 214 */         .getText("doclet.classes"));
/*     */
/*     */
/* 217 */     String[] arrayOfString = { this.configuration.getText("doclet.Class"), this.configuration.getText("doclet.Description") };
/*     */
/* 219 */     ClassDoc[] arrayOfClassDoc = this.pkg.ordinaryClasses();
/* 220 */     if (arrayOfClassDoc.length > 0) {
/* 221 */       this.profileWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/*     */
/* 223 */           .getText("doclet.Class_Summary"), str, arrayOfString, paramContent);
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
/* 237 */     String str = this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 238 */         .getText("doclet.Enum_Summary"), this.configuration
/* 239 */         .getText("doclet.enums"));
/*     */
/*     */
/* 242 */     String[] arrayOfString = { this.configuration.getText("doclet.Enum"), this.configuration.getText("doclet.Description") };
/*     */
/* 244 */     ClassDoc[] arrayOfClassDoc = this.pkg.enums();
/* 245 */     if (arrayOfClassDoc.length > 0) {
/* 246 */       this.profileWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/*     */
/* 248 */           .getText("doclet.Enum_Summary"), str, arrayOfString, paramContent);
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
/* 262 */     String str = this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 263 */         .getText("doclet.Exception_Summary"), this.configuration
/* 264 */         .getText("doclet.exceptions"));
/*     */
/*     */
/* 267 */     String[] arrayOfString = { this.configuration.getText("doclet.Exception"), this.configuration.getText("doclet.Description") };
/*     */
/* 269 */     ClassDoc[] arrayOfClassDoc = this.pkg.exceptions();
/* 270 */     if (arrayOfClassDoc.length > 0) {
/* 271 */       this.profileWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/*     */
/* 273 */           .getText("doclet.Exception_Summary"), str, arrayOfString, paramContent);
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
/* 287 */     String str = this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 288 */         .getText("doclet.Error_Summary"), this.configuration
/* 289 */         .getText("doclet.errors"));
/*     */
/*     */
/* 292 */     String[] arrayOfString = { this.configuration.getText("doclet.Error"), this.configuration.getText("doclet.Description") };
/*     */
/* 294 */     ClassDoc[] arrayOfClassDoc = this.pkg.errors();
/* 295 */     if (arrayOfClassDoc.length > 0) {
/* 296 */       this.profileWriter.addClassesSummary(arrayOfClassDoc, this.configuration
/*     */
/* 298 */           .getText("doclet.Error_Summary"), str, arrayOfString, paramContent);
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
/* 312 */     String str = this.configuration.getText("doclet.Member_Table_Summary", this.configuration
/* 313 */         .getText("doclet.Annotation_Types_Summary"), this.configuration
/* 314 */         .getText("doclet.annotationtypes"));
/*     */
/*     */
/* 317 */     String[] arrayOfString = { this.configuration.getText("doclet.AnnotationType"), this.configuration.getText("doclet.Description") };
/*     */
/* 319 */     AnnotationTypeDoc[] arrayOfAnnotationTypeDoc = this.pkg.annotationTypes();
/* 320 */     if (arrayOfAnnotationTypeDoc.length > 0)
/* 321 */       this.profileWriter.addClassesSummary((ClassDoc[])arrayOfAnnotationTypeDoc, this.configuration
/*     */
/* 323 */           .getText("doclet.Annotation_Types_Summary"), str, arrayOfString, paramContent);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\builders\ProfileSummaryBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
