/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.ConstantsSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class ConstantsSummaryBuilder
/*     */   extends AbstractBuilder
/*     */ {
/*     */   public static final String ROOT = "ConstantSummary";
/*     */   public static final int MAX_CONSTANT_VALUE_INDEX_LENGTH = 2;
/*     */   protected final ConstantsSummaryWriter writer;
/*     */   protected final Set<ClassDoc> classDocsWithConstFields;
/*     */   protected Set<String> printedPackageHeaders;
/*     */   private PackageDoc currentPackage;
/*     */   private ClassDoc currentClass;
/*     */   private Content contentTree;
/*     */
/*     */   private ConstantsSummaryBuilder(Context paramContext, ConstantsSummaryWriter paramConstantsSummaryWriter) {
/*  98 */     super(paramContext);
/*  99 */     this.writer = paramConstantsSummaryWriter;
/* 100 */     this.classDocsWithConstFields = new HashSet<>();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static ConstantsSummaryBuilder getInstance(Context paramContext, ConstantsSummaryWriter paramConstantsSummaryWriter) {
/* 111 */     return new ConstantsSummaryBuilder(paramContext, paramConstantsSummaryWriter);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void build() throws IOException {
/* 118 */     if (this.writer == null) {
/*     */       return;
/*     */     }
/*     */
/* 122 */     build(this.layoutParser.parseXML("ConstantSummary"), this.contentTree);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public String getName() {
/* 129 */     return "ConstantSummary";
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildConstantSummary(XMLNode paramXMLNode, Content paramContent) throws Exception {
/* 139 */     paramContent = this.writer.getHeader();
/* 140 */     buildChildren(paramXMLNode, paramContent);
/* 141 */     this.writer.addFooter(paramContent);
/* 142 */     this.writer.printDocument(paramContent);
/* 143 */     this.writer.close();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildContents(XMLNode paramXMLNode, Content paramContent) {
/* 153 */     Content content = this.writer.getContentsHeader();
/* 154 */     PackageDoc[] arrayOfPackageDoc = this.configuration.packages;
/* 155 */     this.printedPackageHeaders = new HashSet<>();
/* 156 */     for (byte b = 0; b < arrayOfPackageDoc.length; b++) {
/* 157 */       if (hasConstantField(arrayOfPackageDoc[b]) && !hasPrintedPackageIndex(arrayOfPackageDoc[b].name())) {
/* 158 */         this.writer.addLinkToPackageContent(arrayOfPackageDoc[b],
/* 159 */             parsePackageName(arrayOfPackageDoc[b].name()), this.printedPackageHeaders, content);
/*     */       }
/*     */     }
/*     */
/* 163 */     paramContent.addContent(this.writer.getContentsList(content));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildConstantSummaries(XMLNode paramXMLNode, Content paramContent) {
/* 173 */     PackageDoc[] arrayOfPackageDoc = this.configuration.packages;
/* 174 */     this.printedPackageHeaders = new HashSet<>();
/* 175 */     Content content = this.writer.getConstantSummaries();
/* 176 */     for (byte b = 0; b < arrayOfPackageDoc.length; b++) {
/* 177 */       if (hasConstantField(arrayOfPackageDoc[b])) {
/* 178 */         this.currentPackage = arrayOfPackageDoc[b];
/*     */
/* 180 */         buildChildren(paramXMLNode, content);
/*     */       }
/*     */     }
/* 183 */     paramContent.addContent(content);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildPackageHeader(XMLNode paramXMLNode, Content paramContent) {
/* 193 */     String str = parsePackageName(this.currentPackage.name());
/* 194 */     if (!this.printedPackageHeaders.contains(str)) {
/* 195 */       this.writer.addPackageName(this.currentPackage,
/* 196 */           parsePackageName(this.currentPackage.name()), paramContent);
/* 197 */       this.printedPackageHeaders.add(str);
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
/*     */   public void buildClassConstantSummary(XMLNode paramXMLNode, Content paramContent) {
/* 210 */     ClassDoc[] arrayOfClassDoc = (this.currentPackage.name().length() > 0) ? this.currentPackage.allClasses() : this.configuration.classDocCatalog.allClasses("<Unnamed>");
/*     */
/* 212 */     Arrays.sort((Object[])arrayOfClassDoc);
/* 213 */     Content content = this.writer.getClassConstantHeader();
/* 214 */     for (byte b = 0; b < arrayOfClassDoc.length; b++) {
/* 215 */       if (this.classDocsWithConstFields.contains(arrayOfClassDoc[b]) && arrayOfClassDoc[b]
/* 216 */         .isIncluded()) {
/*     */
/*     */
/* 219 */         this.currentClass = arrayOfClassDoc[b];
/*     */
/* 221 */         buildChildren(paramXMLNode, content);
/*     */       }
/* 223 */     }  paramContent.addContent(content);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildConstantMembers(XMLNode paramXMLNode, Content paramContent) {
/* 234 */     (new ConstantFieldBuilder(this.currentClass)).buildMembersSummary(paramXMLNode, paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private boolean hasConstantField(PackageDoc paramPackageDoc) {
/*     */     ClassDoc[] arrayOfClassDoc;
/* 245 */     if (paramPackageDoc.name().length() > 0) {
/* 246 */       arrayOfClassDoc = paramPackageDoc.allClasses();
/*     */     } else {
/* 248 */       arrayOfClassDoc = this.configuration.classDocCatalog.allClasses("<Unnamed>");
/*     */     }
/*     */
/* 251 */     boolean bool = false;
/* 252 */     for (byte b = 0; b < arrayOfClassDoc.length; b++) {
/* 253 */       if (arrayOfClassDoc[b].isIncluded() && hasConstantField(arrayOfClassDoc[b])) {
/* 254 */         bool = true;
/*     */       }
/*     */     }
/* 257 */     return bool;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private boolean hasConstantField(ClassDoc paramClassDoc) {
/* 267 */     VisibleMemberMap visibleMemberMap = new VisibleMemberMap(paramClassDoc, 2, this.configuration);
/*     */
/* 269 */     List list = visibleMemberMap.getLeafClassMembers(this.configuration);
/* 270 */     for (FieldDoc fieldDoc : list) {
/*     */
/* 272 */       if (fieldDoc.constantValueExpression() != null) {
/* 273 */         this.classDocsWithConstFields.add(paramClassDoc);
/* 274 */         return true;
/*     */       }
/*     */     }
/* 277 */     return false;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private boolean hasPrintedPackageIndex(String paramString) {
/* 287 */     String[] arrayOfString = this.printedPackageHeaders.<String>toArray(new String[0]);
/* 288 */     for (byte b = 0; b < arrayOfString.length; b++) {
/* 289 */       if (paramString.startsWith(arrayOfString[b])) {
/* 290 */         return true;
/*     */       }
/*     */     }
/* 293 */     return false;
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
/*     */   private class ConstantFieldBuilder
/*     */   {
/* 307 */     protected VisibleMemberMap visibleMemberMapFields = null;
/*     */
/*     */
/*     */
/*     */
/* 312 */     protected VisibleMemberMap visibleMemberMapEnumConst = null;
/*     */
/*     */
/*     */
/*     */
/*     */     protected ClassDoc classdoc;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public ConstantFieldBuilder(ClassDoc param1ClassDoc) {
/* 324 */       this.classdoc = param1ClassDoc;
/* 325 */       this.visibleMemberMapFields = new VisibleMemberMap(param1ClassDoc, 2, ConstantsSummaryBuilder.this.configuration);
/*     */
/* 327 */       this.visibleMemberMapEnumConst = new VisibleMemberMap(param1ClassDoc, 1, ConstantsSummaryBuilder.this.configuration);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     protected void buildMembersSummary(XMLNode param1XMLNode, Content param1Content) {
/* 339 */       ArrayList<FieldDoc> arrayList = new ArrayList<>(members());
/* 340 */       if (arrayList.size() > 0) {
/* 341 */         Collections.sort(arrayList);
/* 342 */         ConstantsSummaryBuilder.this.writer.addConstantMembers(this.classdoc, arrayList, param1Content);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     protected List<FieldDoc> members() {
/*     */       Iterator<FieldDoc> iterator;
/* 351 */       List list = this.visibleMemberMapFields.getLeafClassMembers(ConstantsSummaryBuilder.this.configuration);
/* 352 */       list.addAll(this.visibleMemberMapEnumConst.getLeafClassMembers(ConstantsSummaryBuilder.this.configuration));
/*     */
/*     */
/* 355 */       if (list != null) {
/* 356 */         iterator = list.iterator();
/*     */       } else {
/* 358 */         return null;
/*     */       }
/* 360 */       LinkedList<FieldDoc> linkedList = new LinkedList();
/*     */
/* 362 */       while (iterator.hasNext()) {
/* 363 */         FieldDoc fieldDoc = iterator.next();
/* 364 */         if (fieldDoc.constantValue() != null) {
/* 365 */           linkedList.add(fieldDoc);
/*     */         }
/*     */       }
/* 368 */       return linkedList;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private String parsePackageName(String paramString) {
/* 377 */     int i = -1;
/* 378 */     for (byte b = 0; b < 2; b++) {
/* 379 */       i = paramString.indexOf(".", i + 1);
/*     */     }
/* 381 */     if (i != -1) {
/* 382 */       paramString = paramString.substring(0, i);
/*     */     }
/* 384 */     return paramString;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\builders\ConstantsSummaryBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
