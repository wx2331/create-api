/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.DocErrorReporter;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.internal.toolkit.AbstractDoclet;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.builders.AbstractBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFile;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.FatalError;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.IndexBuilder;
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
/*     */ public class HtmlDoclet
/*     */   extends AbstractDoclet
/*     */ {
/*  52 */   private static HtmlDoclet docletToStart = null;
/*     */ 
/*     */   
/*  55 */   public final ConfigurationImpl configuration = new ConfigurationImpl();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean start(RootDoc paramRootDoc) {
/*     */     HtmlDoclet htmlDoclet;
/*  74 */     if (docletToStart != null) {
/*  75 */       htmlDoclet = docletToStart;
/*  76 */       docletToStart = null;
/*     */     } else {
/*  78 */       htmlDoclet = new HtmlDoclet();
/*     */     } 
/*  80 */     return htmlDoclet.start(htmlDoclet, paramRootDoc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Configuration configuration() {
/*  89 */     return this.configuration;
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
/*     */   protected void generateOtherFiles(RootDoc paramRootDoc, ClassTree paramClassTree) throws Exception {
/* 104 */     super.generateOtherFiles(paramRootDoc, paramClassTree);
/* 105 */     if (this.configuration.linksource) {
/* 106 */       SourceToHTMLConverter.convertRoot(this.configuration, paramRootDoc, DocPaths.SOURCE_OUTPUT);
/*     */     }
/*     */ 
/*     */     
/* 110 */     if (this.configuration.topFile.isEmpty()) {
/* 111 */       this.configuration.standardmessage
/* 112 */         .error("doclet.No_Non_Deprecated_Classes_To_Document", new Object[0]);
/*     */       return;
/*     */     } 
/* 115 */     boolean bool = this.configuration.nodeprecated;
/* 116 */     performCopy(this.configuration.helpfile);
/* 117 */     performCopy(this.configuration.stylesheetfile);
/*     */     
/* 119 */     if (this.configuration.classuse) {
/* 120 */       ClassUseWriter.generate(this.configuration, paramClassTree);
/*     */     }
/* 122 */     IndexBuilder indexBuilder = new IndexBuilder(this.configuration, bool);
/*     */     
/* 124 */     if (this.configuration.createtree) {
/* 125 */       TreeWriter.generate(this.configuration, paramClassTree);
/*     */     }
/* 127 */     if (this.configuration.createindex) {
/* 128 */       if (this.configuration.splitindex) {
/* 129 */         SplitIndexWriter.generate(this.configuration, indexBuilder);
/*     */       } else {
/* 131 */         SingleIndexWriter.generate(this.configuration, indexBuilder);
/*     */       } 
/*     */     }
/*     */     
/* 135 */     if (!this.configuration.nodeprecatedlist && !bool) {
/* 136 */       DeprecatedListWriter.generate(this.configuration);
/*     */     }
/*     */     
/* 139 */     AllClassesFrameWriter.generate(this.configuration, new IndexBuilder(this.configuration, bool, true));
/*     */ 
/*     */     
/* 142 */     FrameOutputWriter.generate(this.configuration);
/*     */     
/* 144 */     if (this.configuration.createoverview) {
/* 145 */       PackageIndexWriter.generate(this.configuration);
/*     */     }
/* 147 */     if (this.configuration.helpfile.length() == 0 && !this.configuration.nohelp)
/*     */     {
/* 149 */       HelpWriter.generate(this.configuration);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 154 */     if (this.configuration.stylesheetfile.length() == 0) {
/* 155 */       DocFile docFile1 = DocFile.createFileForOutput(this.configuration, DocPaths.STYLESHEET);
/* 156 */       docFile1.copyResource(DocPaths.RESOURCES.resolve(DocPaths.STYLESHEET), false, true);
/*     */     } 
/* 158 */     DocFile docFile = DocFile.createFileForOutput(this.configuration, DocPaths.JAVASCRIPT);
/* 159 */     docFile.copyResource(DocPaths.RESOURCES.resolve(DocPaths.JAVASCRIPT), true, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateClassFiles(ClassDoc[] paramArrayOfClassDoc, ClassTree paramClassTree) {
/* 166 */     Arrays.sort((Object[])paramArrayOfClassDoc);
/* 167 */     for (byte b = 0; b < paramArrayOfClassDoc.length; b++) {
/* 168 */       if (this.configuration.isGeneratedDoc(paramArrayOfClassDoc[b]) && paramArrayOfClassDoc[b].isIncluded()) {
/*     */ 
/*     */         
/* 171 */         ClassDoc classDoc1 = (b == 0) ? null : paramArrayOfClassDoc[b - 1];
/*     */ 
/*     */         
/* 174 */         ClassDoc classDoc2 = paramArrayOfClassDoc[b];
/* 175 */         ClassDoc classDoc3 = (b + 1 == paramArrayOfClassDoc.length) ? null : paramArrayOfClassDoc[b + 1];
/*     */ 
/*     */         
/*     */         try {
/* 179 */           if (classDoc2.isAnnotationType()) {
/*     */ 
/*     */             
/* 182 */             AbstractBuilder abstractBuilder = this.configuration.getBuilderFactory().getAnnotationTypeBuilder((AnnotationTypeDoc)classDoc2, (Type)classDoc1, (Type)classDoc3);
/*     */             
/* 184 */             abstractBuilder.build();
/*     */           }
/*     */           else {
/*     */             
/* 188 */             AbstractBuilder abstractBuilder = this.configuration.getBuilderFactory().getClassBuilder(classDoc2, classDoc1, classDoc3, paramClassTree);
/* 189 */             abstractBuilder.build();
/*     */           } 
/* 191 */         } catch (IOException iOException) {
/* 192 */           throw new DocletAbortException(iOException);
/* 193 */         } catch (FatalError fatalError) {
/* 194 */           throw fatalError;
/* 195 */         } catch (DocletAbortException docletAbortException) {
/* 196 */           throw docletAbortException;
/* 197 */         } catch (Exception exception) {
/* 198 */           exception.printStackTrace();
/* 199 */           throw new DocletAbortException(exception);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateProfileFiles() throws Exception {
/* 208 */     if (this.configuration.showProfiles && this.configuration.profilePackages.size() > 0) {
/* 209 */       ProfileIndexFrameWriter.generate(this.configuration);
/* 210 */       Profile profile = null;
/*     */       
/* 212 */       for (byte b = 1; b < this.configuration.profiles.getProfileCount(); b++) {
/* 213 */         String str = (Profile.lookup(b)).name;
/*     */ 
/*     */ 
/*     */         
/* 217 */         if (this.configuration.shouldDocumentProfile(str)) {
/*     */           
/* 219 */           ProfilePackageIndexFrameWriter.generate(this.configuration, str);
/* 220 */           PackageDoc[] arrayOfPackageDoc = (PackageDoc[])this.configuration.profilePackages.get(str);
/*     */           
/* 222 */           PackageDoc packageDoc = null;
/* 223 */           for (byte b1 = 0; b1 < arrayOfPackageDoc.length; b1++) {
/*     */ 
/*     */ 
/*     */             
/* 227 */             if (!this.configuration.nodeprecated || !Util.isDeprecated((Doc)arrayOfPackageDoc[b1])) {
/* 228 */               ProfilePackageFrameWriter.generate(this.configuration, arrayOfPackageDoc[b1], b);
/*     */               
/* 230 */               PackageDoc packageDoc1 = (b1 + 1 < arrayOfPackageDoc.length && arrayOfPackageDoc[b1 + 1].name().length() > 0) ? arrayOfPackageDoc[b1 + 1] : null;
/*     */               
/* 232 */               AbstractBuilder abstractBuilder1 = this.configuration.getBuilderFactory().getProfilePackageSummaryBuilder(arrayOfPackageDoc[b1], packageDoc, packageDoc1, 
/* 233 */                   Profile.lookup(b));
/* 234 */               abstractBuilder1.build();
/* 235 */               packageDoc = arrayOfPackageDoc[b1];
/*     */             } 
/*     */           } 
/*     */           
/* 239 */           Profile profile1 = (b + 1 < this.configuration.profiles.getProfileCount()) ? Profile.lookup(b + 1) : null;
/*     */           
/* 241 */           AbstractBuilder abstractBuilder = this.configuration.getBuilderFactory().getProfileSummaryBuilder(
/* 242 */               Profile.lookup(b), profile, profile1);
/* 243 */           abstractBuilder.build();
/* 244 */           profile = Profile.lookup(b);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generatePackageFiles(ClassTree paramClassTree) throws Exception {
/* 253 */     PackageDoc[] arrayOfPackageDoc = this.configuration.packages;
/* 254 */     if (arrayOfPackageDoc.length > 1) {
/* 255 */       PackageIndexFrameWriter.generate(this.configuration);
/*     */     }
/* 257 */     PackageDoc packageDoc = null;
/* 258 */     for (byte b = 0; b < arrayOfPackageDoc.length; b++) {
/*     */ 
/*     */ 
/*     */       
/* 262 */       if (!this.configuration.nodeprecated || !Util.isDeprecated((Doc)arrayOfPackageDoc[b])) {
/* 263 */         PackageFrameWriter.generate(this.configuration, arrayOfPackageDoc[b]);
/*     */         
/* 265 */         PackageDoc packageDoc1 = (b + 1 < arrayOfPackageDoc.length && arrayOfPackageDoc[b + 1].name().length() > 0) ? arrayOfPackageDoc[b + 1] : null;
/*     */         
/* 267 */         packageDoc1 = (b + 2 < arrayOfPackageDoc.length && packageDoc1 == null) ? arrayOfPackageDoc[b + 2] : packageDoc1;
/*     */         
/* 269 */         AbstractBuilder abstractBuilder = this.configuration.getBuilderFactory().getPackageSummaryBuilder(arrayOfPackageDoc[b], packageDoc, packageDoc1);
/*     */         
/* 271 */         abstractBuilder.build();
/* 272 */         if (this.configuration.createtree) {
/* 273 */           PackageTreeWriter.generate(this.configuration, arrayOfPackageDoc[b], packageDoc, packageDoc1, this.configuration.nodeprecated);
/*     */         }
/*     */ 
/*     */         
/* 277 */         packageDoc = arrayOfPackageDoc[b];
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/* 282 */   public static final ConfigurationImpl sharedInstanceForOptions = new ConfigurationImpl();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int optionLength(String paramString) {
/* 293 */     return sharedInstanceForOptions.optionLength(paramString);
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
/*     */   public static boolean validOptions(String[][] paramArrayOfString, DocErrorReporter paramDocErrorReporter) {
/* 309 */     docletToStart = new HtmlDoclet();
/* 310 */     return docletToStart.configuration.validOptions(paramArrayOfString, paramDocErrorReporter);
/*     */   }
/*     */   
/*     */   private void performCopy(String paramString) {
/* 314 */     if (paramString.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     try {
/* 318 */       DocFile docFile1 = DocFile.createFileForInput(this.configuration, paramString);
/* 319 */       DocPath docPath = DocPath.create(docFile1.getName());
/* 320 */       DocFile docFile2 = DocFile.createFileForOutput(this.configuration, docPath);
/* 321 */       if (docFile2.isSameFile(docFile1)) {
/*     */         return;
/*     */       }
/* 324 */       this.configuration.message.notice((SourcePosition)null, "doclet.Copying_File_0_To_File_1", new Object[] { docFile1
/*     */             
/* 326 */             .toString(), docPath.getPath() });
/* 327 */       docFile2.copyFile(docFile1);
/* 328 */     } catch (IOException iOException) {
/* 329 */       this.configuration.message.error((SourcePosition)null, "doclet.perform_copy_exception_encountered", new Object[] { iOException
/*     */             
/* 331 */             .toString() });
/* 332 */       throw new DocletAbortException(iOException);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\HtmlDoclet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */