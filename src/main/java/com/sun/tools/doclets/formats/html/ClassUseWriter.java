/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.tools.doclets.formats.html.markup.ContentBuilder;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassUseMapper;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
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
/*     */ 
/*     */ public class ClassUseWriter
/*     */   extends SubWriterHolderWriter
/*     */ {
/*     */   final ClassDoc classdoc;
/*  58 */   Set<PackageDoc> pkgToPackageAnnotations = null;
/*     */   
/*     */   final Map<String, List<ProgramElementDoc>> pkgToClassTypeParameter;
/*     */   
/*     */   final Map<String, List<ProgramElementDoc>> pkgToClassAnnotations;
/*     */   
/*     */   final Map<String, List<ProgramElementDoc>> pkgToMethodTypeParameter;
/*     */   
/*     */   final Map<String, List<ProgramElementDoc>> pkgToMethodArgTypeParameter;
/*     */   
/*     */   final Map<String, List<ProgramElementDoc>> pkgToMethodReturnTypeParameter;
/*     */   
/*     */   final Map<String, List<ProgramElementDoc>> pkgToMethodAnnotations;
/*     */   
/*     */   final Map<String, List<ProgramElementDoc>> pkgToMethodParameterAnnotations;
/*     */   
/*     */   final Map<String, List<ProgramElementDoc>> pkgToFieldTypeParameter;
/*     */   
/*     */   final Map<String, List<ProgramElementDoc>> pkgToFieldAnnotations;
/*     */   
/*     */   final Map<String, List<ProgramElementDoc>> pkgToSubclass;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToSubinterface;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToImplementingClass;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToField;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToMethodReturn;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToMethodArgs;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToMethodThrows;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToConstructorAnnotations;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToConstructorParameterAnnotations;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToConstructorArgs;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToConstructorArgTypeParameter;
/*     */   final Map<String, List<ProgramElementDoc>> pkgToConstructorThrows;
/*     */   final SortedSet<PackageDoc> pkgSet;
/*     */   final MethodWriterImpl methodSubWriter;
/*     */   final ConstructorWriterImpl constrSubWriter;
/*     */   final FieldWriterImpl fieldSubWriter;
/*     */   final NestedClassWriterImpl classSubWriter;
/*     */   final String classUseTableSummary;
/*     */   final String subclassUseTableSummary;
/*     */   final String subinterfaceUseTableSummary;
/*     */   final String fieldUseTableSummary;
/*     */   final String methodUseTableSummary;
/*     */   final String constructorUseTableSummary;
/*     */   
/*     */   public ClassUseWriter(ConfigurationImpl paramConfigurationImpl, ClassUseMapper paramClassUseMapper, DocPath paramDocPath, ClassDoc paramClassDoc) throws IOException {
/* 103 */     super(paramConfigurationImpl, paramDocPath);
/* 104 */     this.classdoc = paramClassDoc;
/* 105 */     if (paramClassUseMapper.classToPackageAnnotations.containsKey(paramClassDoc.qualifiedName()))
/* 106 */       this.pkgToPackageAnnotations = new TreeSet<>((Collection<? extends PackageDoc>)paramClassUseMapper.classToPackageAnnotations.get(paramClassDoc.qualifiedName())); 
/* 107 */     paramConfigurationImpl.currentcd = paramClassDoc;
/* 108 */     this.pkgSet = new TreeSet<>();
/* 109 */     this.pkgToClassTypeParameter = pkgDivide(paramClassUseMapper.classToClassTypeParam);
/* 110 */     this.pkgToClassAnnotations = pkgDivide(paramClassUseMapper.classToClassAnnotations);
/* 111 */     this.pkgToMethodTypeParameter = pkgDivide(paramClassUseMapper.classToExecMemberDocTypeParam);
/* 112 */     this.pkgToMethodArgTypeParameter = pkgDivide(paramClassUseMapper.classToExecMemberDocArgTypeParam);
/* 113 */     this.pkgToFieldTypeParameter = pkgDivide(paramClassUseMapper.classToFieldDocTypeParam);
/* 114 */     this.pkgToFieldAnnotations = pkgDivide(paramClassUseMapper.annotationToFieldDoc);
/* 115 */     this.pkgToMethodReturnTypeParameter = pkgDivide(paramClassUseMapper.classToExecMemberDocReturnTypeParam);
/* 116 */     this.pkgToMethodAnnotations = pkgDivide(paramClassUseMapper.classToExecMemberDocAnnotations);
/* 117 */     this.pkgToMethodParameterAnnotations = pkgDivide(paramClassUseMapper.classToExecMemberDocParamAnnotation);
/* 118 */     this.pkgToSubclass = pkgDivide(paramClassUseMapper.classToSubclass);
/* 119 */     this.pkgToSubinterface = pkgDivide(paramClassUseMapper.classToSubinterface);
/* 120 */     this.pkgToImplementingClass = pkgDivide(paramClassUseMapper.classToImplementingClass);
/* 121 */     this.pkgToField = pkgDivide(paramClassUseMapper.classToField);
/* 122 */     this.pkgToMethodReturn = pkgDivide(paramClassUseMapper.classToMethodReturn);
/* 123 */     this.pkgToMethodArgs = pkgDivide(paramClassUseMapper.classToMethodArgs);
/* 124 */     this.pkgToMethodThrows = pkgDivide(paramClassUseMapper.classToMethodThrows);
/* 125 */     this.pkgToConstructorAnnotations = pkgDivide(paramClassUseMapper.classToConstructorAnnotations);
/* 126 */     this.pkgToConstructorParameterAnnotations = pkgDivide(paramClassUseMapper.classToConstructorParamAnnotation);
/* 127 */     this.pkgToConstructorArgs = pkgDivide(paramClassUseMapper.classToConstructorArgs);
/* 128 */     this.pkgToConstructorArgTypeParameter = pkgDivide(paramClassUseMapper.classToConstructorDocArgTypeParam);
/* 129 */     this.pkgToConstructorThrows = pkgDivide(paramClassUseMapper.classToConstructorThrows);
/*     */     
/* 131 */     if (this.pkgSet.size() > 0 && paramClassUseMapper.classToPackage
/* 132 */       .containsKey(paramClassDoc.qualifiedName()) && 
/* 133 */       !this.pkgSet.equals(paramClassUseMapper.classToPackage.get(paramClassDoc.qualifiedName()))) {
/* 134 */       paramConfigurationImpl.root.printWarning("Internal error: package sets don't match: " + this.pkgSet + " with: " + paramClassUseMapper.classToPackage
/* 135 */           .get(paramClassDoc.qualifiedName()));
/*     */     }
/* 137 */     this.methodSubWriter = new MethodWriterImpl(this);
/* 138 */     this.constrSubWriter = new ConstructorWriterImpl(this);
/* 139 */     this.fieldSubWriter = new FieldWriterImpl(this);
/* 140 */     this.classSubWriter = new NestedClassWriterImpl(this);
/* 141 */     this.classUseTableSummary = paramConfigurationImpl.getText("doclet.Use_Table_Summary", paramConfigurationImpl
/* 142 */         .getText("doclet.classes"));
/* 143 */     this.subclassUseTableSummary = paramConfigurationImpl.getText("doclet.Use_Table_Summary", paramConfigurationImpl
/* 144 */         .getText("doclet.subclasses"));
/* 145 */     this.subinterfaceUseTableSummary = paramConfigurationImpl.getText("doclet.Use_Table_Summary", paramConfigurationImpl
/* 146 */         .getText("doclet.subinterfaces"));
/* 147 */     this.fieldUseTableSummary = paramConfigurationImpl.getText("doclet.Use_Table_Summary", paramConfigurationImpl
/* 148 */         .getText("doclet.fields"));
/* 149 */     this.methodUseTableSummary = paramConfigurationImpl.getText("doclet.Use_Table_Summary", paramConfigurationImpl
/* 150 */         .getText("doclet.methods"));
/* 151 */     this.constructorUseTableSummary = paramConfigurationImpl.getText("doclet.Use_Table_Summary", paramConfigurationImpl
/* 152 */         .getText("doclet.constructors"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, ClassTree paramClassTree) {
/* 161 */     ClassUseMapper classUseMapper = new ClassUseMapper(paramConfigurationImpl.root, paramClassTree);
/* 162 */     ClassDoc[] arrayOfClassDoc = paramConfigurationImpl.root.classes();
/* 163 */     for (byte b1 = 0; b1 < arrayOfClassDoc.length; b1++) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 168 */       if (!paramConfigurationImpl.nodeprecated || 
/* 169 */         !Util.isDeprecated((Doc)arrayOfClassDoc[b1].containingPackage()))
/* 170 */         generate(paramConfigurationImpl, classUseMapper, arrayOfClassDoc[b1]); 
/*     */     } 
/* 172 */     PackageDoc[] arrayOfPackageDoc = paramConfigurationImpl.packages;
/* 173 */     for (byte b2 = 0; b2 < arrayOfPackageDoc.length; b2++) {
/*     */ 
/*     */       
/* 176 */       if (!paramConfigurationImpl.nodeprecated || !Util.isDeprecated((Doc)arrayOfPackageDoc[b2]))
/* 177 */         PackageUseWriter.generate(paramConfigurationImpl, classUseMapper, arrayOfPackageDoc[b2]); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private Map<String, List<ProgramElementDoc>> pkgDivide(Map<String, ? extends List<? extends ProgramElementDoc>> paramMap) {
/* 182 */     HashMap<Object, Object> hashMap = new HashMap<>();
/* 183 */     List<Comparable> list = (List)paramMap.get(this.classdoc.qualifiedName());
/* 184 */     if (list != null) {
/* 185 */       Collections.sort(list);
/* 186 */       Iterator<Comparable> iterator = list.iterator();
/* 187 */       while (iterator.hasNext()) {
/* 188 */         ProgramElementDoc programElementDoc = (ProgramElementDoc)iterator.next();
/* 189 */         PackageDoc packageDoc = programElementDoc.containingPackage();
/* 190 */         this.pkgSet.add(packageDoc);
/* 191 */         List<ProgramElementDoc> list1 = (List)hashMap.get(packageDoc.name());
/* 192 */         if (list1 == null) {
/* 193 */           list1 = new ArrayList();
/* 194 */           hashMap.put(packageDoc.name(), list1);
/*     */         } 
/* 196 */         list1.add(programElementDoc);
/*     */       } 
/*     */     } 
/* 199 */     return (Map)hashMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void generate(ConfigurationImpl paramConfigurationImpl, ClassUseMapper paramClassUseMapper, ClassDoc paramClassDoc) {
/* 210 */     DocPath docPath = DocPath.forPackage(paramClassDoc).resolve(DocPaths.CLASS_USE).resolve(DocPath.forName(paramClassDoc));
/*     */     try {
/* 212 */       ClassUseWriter classUseWriter = new ClassUseWriter(paramConfigurationImpl, paramClassUseMapper, docPath, paramClassDoc);
/*     */ 
/*     */       
/* 215 */       classUseWriter.generateClassUseFile();
/* 216 */       classUseWriter.close();
/* 217 */     } catch (IOException iOException) {
/* 218 */       paramConfigurationImpl.standardmessage
/* 219 */         .error("doclet.exception_encountered", new Object[] {
/* 220 */             iOException.toString(), docPath.getPath() });
/* 221 */       throw new DocletAbortException(iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateClassUseFile() throws IOException {
/* 229 */     Content content = getClassUseHeader();
/* 230 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.DIV);
/* 231 */     htmlTree.addStyle(HtmlStyle.classUseContainer);
/* 232 */     if (this.pkgSet.size() > 0) {
/* 233 */       addClassUse((Content)htmlTree);
/*     */     } else {
/* 235 */       htmlTree.addContent(getResource("doclet.ClassUse_No.usage.of.0", this.classdoc
/* 236 */             .qualifiedName()));
/*     */     } 
/* 238 */     content.addContent((Content)htmlTree);
/* 239 */     addNavLinks(false, content);
/* 240 */     addBottom(content);
/* 241 */     printHtmlDocument((String[])null, true, content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addClassUse(Content paramContent) throws IOException {
/* 250 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.UL);
/* 251 */     htmlTree.addStyle(HtmlStyle.blockList);
/* 252 */     if (this.configuration.packages.length > 1) {
/* 253 */       addPackageList((Content)htmlTree);
/* 254 */       addPackageAnnotationList((Content)htmlTree);
/*     */     } 
/* 256 */     addClassList((Content)htmlTree);
/* 257 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addPackageList(Content paramContent) throws IOException {
/* 266 */     HtmlTree htmlTree1 = HtmlTree.TABLE(HtmlStyle.useSummary, 0, 3, 0, this.useTableSummary, 
/* 267 */         getTableCaption(this.configuration.getResource("doclet.ClassUse_Packages.that.use.0", 
/*     */             
/* 269 */             getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_USE_HEADER, this.classdoc)))));
/*     */     
/* 271 */     htmlTree1.addContent(getSummaryTableHeader(this.packageTableHeader, "col"));
/* 272 */     HtmlTree htmlTree2 = new HtmlTree(HtmlTag.TBODY);
/* 273 */     Iterator<PackageDoc> iterator = this.pkgSet.iterator();
/* 274 */     for (byte b = 0; iterator.hasNext(); b++) {
/* 275 */       PackageDoc packageDoc = iterator.next();
/* 276 */       HtmlTree htmlTree = new HtmlTree(HtmlTag.TR);
/* 277 */       if (b % 2 == 0) {
/* 278 */         htmlTree.addStyle(HtmlStyle.altColor);
/*     */       } else {
/* 280 */         htmlTree.addStyle(HtmlStyle.rowColor);
/*     */       } 
/* 282 */       addPackageUse(packageDoc, (Content)htmlTree);
/* 283 */       htmlTree2.addContent((Content)htmlTree);
/*     */     } 
/* 285 */     htmlTree1.addContent((Content)htmlTree2);
/* 286 */     HtmlTree htmlTree3 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree1);
/* 287 */     paramContent.addContent((Content)htmlTree3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addPackageAnnotationList(Content paramContent) throws IOException {
/* 296 */     if (!this.classdoc.isAnnotationType() || this.pkgToPackageAnnotations == null || this.pkgToPackageAnnotations
/*     */       
/* 298 */       .isEmpty()) {
/*     */       return;
/*     */     }
/* 301 */     HtmlTree htmlTree1 = HtmlTree.TABLE(HtmlStyle.useSummary, 0, 3, 0, this.useTableSummary, 
/* 302 */         getTableCaption(this.configuration.getResource("doclet.ClassUse_PackageAnnotation", 
/*     */             
/* 304 */             getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_USE_HEADER, this.classdoc)))));
/*     */     
/* 306 */     htmlTree1.addContent(getSummaryTableHeader(this.packageTableHeader, "col"));
/* 307 */     HtmlTree htmlTree2 = new HtmlTree(HtmlTag.TBODY);
/* 308 */     Iterator<PackageDoc> iterator = this.pkgToPackageAnnotations.iterator();
/* 309 */     for (byte b = 0; iterator.hasNext(); b++) {
/* 310 */       PackageDoc packageDoc = iterator.next();
/* 311 */       HtmlTree htmlTree4 = new HtmlTree(HtmlTag.TR);
/* 312 */       if (b % 2 == 0) {
/* 313 */         htmlTree4.addStyle(HtmlStyle.altColor);
/*     */       } else {
/* 315 */         htmlTree4.addStyle(HtmlStyle.rowColor);
/*     */       } 
/* 317 */       HtmlTree htmlTree5 = HtmlTree.TD(HtmlStyle.colFirst, 
/* 318 */           getPackageLink(packageDoc, (Content)new StringContent(packageDoc.name())));
/* 319 */       htmlTree4.addContent((Content)htmlTree5);
/* 320 */       HtmlTree htmlTree6 = new HtmlTree(HtmlTag.TD);
/* 321 */       htmlTree6.addStyle(HtmlStyle.colLast);
/* 322 */       addSummaryComment((Doc)packageDoc, (Content)htmlTree6);
/* 323 */       htmlTree4.addContent((Content)htmlTree6);
/* 324 */       htmlTree2.addContent((Content)htmlTree4);
/*     */     } 
/* 326 */     htmlTree1.addContent((Content)htmlTree2);
/* 327 */     HtmlTree htmlTree3 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree1);
/* 328 */     paramContent.addContent((Content)htmlTree3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addClassList(Content paramContent) throws IOException {
/* 337 */     HtmlTree htmlTree1 = new HtmlTree(HtmlTag.UL);
/* 338 */     htmlTree1.addStyle(HtmlStyle.blockList);
/* 339 */     for (PackageDoc packageDoc : this.pkgSet) {
/*     */       
/* 341 */       HtmlTree htmlTree3 = HtmlTree.LI(HtmlStyle.blockList, getMarkerAnchor(packageDoc.name()));
/* 342 */       Content content = getResource("doclet.ClassUse_Uses.of.0.in.1", 
/* 343 */           getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_USE_HEADER, this.classdoc)), 
/*     */           
/* 345 */           getPackageLink(packageDoc, Util.getPackageName(packageDoc)));
/* 346 */       HtmlTree htmlTree4 = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING, content);
/* 347 */       htmlTree3.addContent((Content)htmlTree4);
/* 348 */       addClassUse(packageDoc, (Content)htmlTree3);
/* 349 */       htmlTree1.addContent((Content)htmlTree3);
/*     */     } 
/* 351 */     HtmlTree htmlTree2 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree1);
/* 352 */     paramContent.addContent((Content)htmlTree2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addPackageUse(PackageDoc paramPackageDoc, Content paramContent) throws IOException {
/* 362 */     HtmlTree htmlTree1 = HtmlTree.TD(HtmlStyle.colFirst, 
/* 363 */         getHyperLink(paramPackageDoc.name(), (Content)new StringContent(Util.getPackageName(paramPackageDoc))));
/* 364 */     paramContent.addContent((Content)htmlTree1);
/* 365 */     HtmlTree htmlTree2 = new HtmlTree(HtmlTag.TD);
/* 366 */     htmlTree2.addStyle(HtmlStyle.colLast);
/* 367 */     addSummaryComment((Doc)paramPackageDoc, (Content)htmlTree2);
/* 368 */     paramContent.addContent((Content)htmlTree2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addClassUse(PackageDoc paramPackageDoc, Content paramContent) throws IOException {
/* 378 */     Content content1 = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_USE_HEADER, this.classdoc));
/*     */     
/* 380 */     Content content2 = getPackageLink(paramPackageDoc, Util.getPackageName(paramPackageDoc));
/* 381 */     this.classSubWriter.addUseInfo(this.pkgToClassAnnotations.get(paramPackageDoc.name()), this.configuration
/* 382 */         .getResource("doclet.ClassUse_Annotation", content1, content2), this.classUseTableSummary, paramContent);
/*     */     
/* 384 */     this.classSubWriter.addUseInfo(this.pkgToClassTypeParameter.get(paramPackageDoc.name()), this.configuration
/* 385 */         .getResource("doclet.ClassUse_TypeParameter", content1, content2), this.classUseTableSummary, paramContent);
/*     */     
/* 387 */     this.classSubWriter.addUseInfo(this.pkgToSubclass.get(paramPackageDoc.name()), this.configuration
/* 388 */         .getResource("doclet.ClassUse_Subclass", content1, content2), this.subclassUseTableSummary, paramContent);
/*     */     
/* 390 */     this.classSubWriter.addUseInfo(this.pkgToSubinterface.get(paramPackageDoc.name()), this.configuration
/* 391 */         .getResource("doclet.ClassUse_Subinterface", content1, content2), this.subinterfaceUseTableSummary, paramContent);
/*     */     
/* 393 */     this.classSubWriter.addUseInfo(this.pkgToImplementingClass.get(paramPackageDoc.name()), this.configuration
/* 394 */         .getResource("doclet.ClassUse_ImplementingClass", content1, content2), this.classUseTableSummary, paramContent);
/*     */     
/* 396 */     this.fieldSubWriter.addUseInfo(this.pkgToField.get(paramPackageDoc.name()), this.configuration
/* 397 */         .getResource("doclet.ClassUse_Field", content1, content2), this.fieldUseTableSummary, paramContent);
/*     */     
/* 399 */     this.fieldSubWriter.addUseInfo(this.pkgToFieldAnnotations.get(paramPackageDoc.name()), this.configuration
/* 400 */         .getResource("doclet.ClassUse_FieldAnnotations", content1, content2), this.fieldUseTableSummary, paramContent);
/*     */     
/* 402 */     this.fieldSubWriter.addUseInfo(this.pkgToFieldTypeParameter.get(paramPackageDoc.name()), this.configuration
/* 403 */         .getResource("doclet.ClassUse_FieldTypeParameter", content1, content2), this.fieldUseTableSummary, paramContent);
/*     */     
/* 405 */     this.methodSubWriter.addUseInfo(this.pkgToMethodAnnotations.get(paramPackageDoc.name()), this.configuration
/* 406 */         .getResource("doclet.ClassUse_MethodAnnotations", content1, content2), this.methodUseTableSummary, paramContent);
/*     */     
/* 408 */     this.methodSubWriter.addUseInfo(this.pkgToMethodParameterAnnotations.get(paramPackageDoc.name()), this.configuration
/* 409 */         .getResource("doclet.ClassUse_MethodParameterAnnotations", content1, content2), this.methodUseTableSummary, paramContent);
/*     */     
/* 411 */     this.methodSubWriter.addUseInfo(this.pkgToMethodTypeParameter.get(paramPackageDoc.name()), this.configuration
/* 412 */         .getResource("doclet.ClassUse_MethodTypeParameter", content1, content2), this.methodUseTableSummary, paramContent);
/*     */     
/* 414 */     this.methodSubWriter.addUseInfo(this.pkgToMethodReturn.get(paramPackageDoc.name()), this.configuration
/* 415 */         .getResource("doclet.ClassUse_MethodReturn", content1, content2), this.methodUseTableSummary, paramContent);
/*     */     
/* 417 */     this.methodSubWriter.addUseInfo(this.pkgToMethodReturnTypeParameter.get(paramPackageDoc.name()), this.configuration
/* 418 */         .getResource("doclet.ClassUse_MethodReturnTypeParameter", content1, content2), this.methodUseTableSummary, paramContent);
/*     */     
/* 420 */     this.methodSubWriter.addUseInfo(this.pkgToMethodArgs.get(paramPackageDoc.name()), this.configuration
/* 421 */         .getResource("doclet.ClassUse_MethodArgs", content1, content2), this.methodUseTableSummary, paramContent);
/*     */     
/* 423 */     this.methodSubWriter.addUseInfo(this.pkgToMethodArgTypeParameter.get(paramPackageDoc.name()), this.configuration
/* 424 */         .getResource("doclet.ClassUse_MethodArgsTypeParameters", content1, content2), this.methodUseTableSummary, paramContent);
/*     */     
/* 426 */     this.methodSubWriter.addUseInfo(this.pkgToMethodThrows.get(paramPackageDoc.name()), this.configuration
/* 427 */         .getResource("doclet.ClassUse_MethodThrows", content1, content2), this.methodUseTableSummary, paramContent);
/*     */     
/* 429 */     this.constrSubWriter.addUseInfo(this.pkgToConstructorAnnotations.get(paramPackageDoc.name()), this.configuration
/* 430 */         .getResource("doclet.ClassUse_ConstructorAnnotations", content1, content2), this.constructorUseTableSummary, paramContent);
/*     */     
/* 432 */     this.constrSubWriter.addUseInfo(this.pkgToConstructorParameterAnnotations.get(paramPackageDoc.name()), this.configuration
/* 433 */         .getResource("doclet.ClassUse_ConstructorParameterAnnotations", content1, content2), this.constructorUseTableSummary, paramContent);
/*     */     
/* 435 */     this.constrSubWriter.addUseInfo(this.pkgToConstructorArgs.get(paramPackageDoc.name()), this.configuration
/* 436 */         .getResource("doclet.ClassUse_ConstructorArgs", content1, content2), this.constructorUseTableSummary, paramContent);
/*     */     
/* 438 */     this.constrSubWriter.addUseInfo(this.pkgToConstructorArgTypeParameter.get(paramPackageDoc.name()), this.configuration
/* 439 */         .getResource("doclet.ClassUse_ConstructorArgsTypeParameters", content1, content2), this.constructorUseTableSummary, paramContent);
/*     */     
/* 441 */     this.constrSubWriter.addUseInfo(this.pkgToConstructorThrows.get(paramPackageDoc.name()), this.configuration
/* 442 */         .getResource("doclet.ClassUse_ConstructorThrows", content1, content2), this.constructorUseTableSummary, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getClassUseHeader() {
/* 452 */     String str1 = this.configuration.getText(this.classdoc.isInterface() ? "doclet.Interface" : "doclet.Class");
/*     */     
/* 454 */     String str2 = this.classdoc.qualifiedName();
/* 455 */     String str3 = this.configuration.getText("doclet.Window_ClassUse_Header", str1, str2);
/*     */     
/* 457 */     HtmlTree htmlTree1 = getBody(true, getWindowTitle(str3));
/* 458 */     addTop((Content)htmlTree1);
/* 459 */     addNavLinks(true, (Content)htmlTree1);
/* 460 */     ContentBuilder contentBuilder = new ContentBuilder();
/* 461 */     contentBuilder.addContent(getResource("doclet.ClassUse_Title", str1));
/* 462 */     contentBuilder.addContent((Content)new HtmlTree(HtmlTag.BR));
/* 463 */     contentBuilder.addContent(str2);
/* 464 */     HtmlTree htmlTree2 = HtmlTree.HEADING(HtmlConstants.CLASS_PAGE_HEADING, true, HtmlStyle.title, (Content)contentBuilder);
/*     */     
/* 466 */     HtmlTree htmlTree3 = HtmlTree.DIV(HtmlStyle.header, (Content)htmlTree2);
/* 467 */     htmlTree1.addContent((Content)htmlTree3);
/* 468 */     return (Content)htmlTree1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkPackage() {
/* 478 */     Content content = getHyperLink(DocPath.parent.resolve(DocPaths.PACKAGE_SUMMARY), this.packageLabel);
/* 479 */     return (Content)HtmlTree.LI(content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkClass() {
/* 489 */     Content content = getLink((new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_USE_HEADER, this.classdoc))
/*     */         
/* 491 */         .label(this.configuration.getText("doclet.Class")));
/* 492 */     return (Content)HtmlTree.LI(content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkClassUse() {
/* 502 */     return (Content)HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.useLabel);
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
/*     */   protected Content getNavLinkTree() {
/* 514 */     Content content = this.classdoc.containingPackage().isIncluded() ? getHyperLink(DocPath.parent.resolve(DocPaths.PACKAGE_TREE), this.treeLabel) : getHyperLink(this.pathToRoot.resolve(DocPaths.OVERVIEW_TREE), this.treeLabel);
/* 515 */     return (Content)HtmlTree.LI(content);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\ClassUseWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */