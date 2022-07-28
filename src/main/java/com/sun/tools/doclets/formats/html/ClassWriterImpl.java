/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.ClassWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.builders.MemberSummaryBuilder;
/*     */ import com.sun.tools.doclets.internal.toolkit.taglets.ParamTaglet;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.ClassTree;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPath;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocPaths;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletConstants;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import com.sun.tools.javadoc.RootDocImpl;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassWriterImpl
/*     */   extends SubWriterHolderWriter
/*     */   implements ClassWriter
/*     */ {
/*     */   protected final ClassDoc classDoc;
/*     */   protected final ClassTree classtree;
/*     */   protected final ClassDoc prev;
/*     */   protected final ClassDoc next;
/*     */   
/*     */   public ClassWriterImpl(ConfigurationImpl paramConfigurationImpl, ClassDoc paramClassDoc1, ClassDoc paramClassDoc2, ClassDoc paramClassDoc3, ClassTree paramClassTree) throws IOException {
/*  79 */     super(paramConfigurationImpl, DocPath.forClass(paramClassDoc1));
/*  80 */     this.classDoc = paramClassDoc1;
/*  81 */     paramConfigurationImpl.currentcd = paramClassDoc1;
/*  82 */     this.classtree = paramClassTree;
/*  83 */     this.prev = paramClassDoc2;
/*  84 */     this.next = paramClassDoc3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkPackage() {
/*  93 */     Content content = getHyperLink(DocPaths.PACKAGE_SUMMARY, this.packageLabel);
/*     */     
/*  95 */     return (Content)HtmlTree.LI(content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkClass() {
/* 105 */     return (Content)HtmlTree.LI(HtmlStyle.navBarCell1Rev, this.classLabel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkClassUse() {
/* 115 */     Content content = getHyperLink(DocPaths.CLASS_USE.resolve(this.filename), this.useLabel);
/* 116 */     return (Content)HtmlTree.LI(content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getNavLinkPrevious() {
/*     */     HtmlTree htmlTree;
/* 127 */     if (this.prev != null) {
/* 128 */       Content content = getLink((new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS, this.prev))
/*     */           
/* 130 */           .label(this.prevclassLabel).strong(true));
/* 131 */       htmlTree = HtmlTree.LI(content);
/*     */     } else {
/*     */       
/* 134 */       htmlTree = HtmlTree.LI(this.prevclassLabel);
/* 135 */     }  return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getNavLinkNext() {
/*     */     HtmlTree htmlTree;
/* 145 */     if (this.next != null) {
/* 146 */       Content content = getLink((new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS, this.next))
/*     */           
/* 148 */           .label(this.nextclassLabel).strong(true));
/* 149 */       htmlTree = HtmlTree.LI(content);
/*     */     } else {
/*     */       
/* 152 */       htmlTree = HtmlTree.LI(this.nextclassLabel);
/* 153 */     }  return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getHeader(String paramString) {
/* 161 */     String str1 = (this.classDoc.containingPackage() != null) ? this.classDoc.containingPackage().name() : "";
/* 162 */     String str2 = this.classDoc.name();
/* 163 */     HtmlTree htmlTree1 = getBody(true, getWindowTitle(str2));
/* 164 */     addTop((Content)htmlTree1);
/* 165 */     addNavLinks(true, (Content)htmlTree1);
/* 166 */     htmlTree1.addContent(HtmlConstants.START_OF_CLASS_DATA);
/* 167 */     HtmlTree htmlTree2 = new HtmlTree(HtmlTag.DIV);
/* 168 */     htmlTree2.addStyle(HtmlStyle.header);
/* 169 */     if (this.configuration.showProfiles) {
/* 170 */       String str = "";
/* 171 */       int i = this.configuration.profiles.getProfile(getTypeNameForProfile(this.classDoc));
/* 172 */       if (i > 0) {
/* 173 */         StringContent stringContent1 = new StringContent();
/* 174 */         for (int j = i; j < this.configuration.profiles.getProfileCount(); j++) {
/* 175 */           stringContent1.addContent(str);
/* 176 */           stringContent1.addContent((Profile.lookup(j)).name);
/* 177 */           str = ", ";
/*     */         } 
/* 179 */         HtmlTree htmlTree = HtmlTree.DIV(HtmlStyle.subTitle, (Content)stringContent1);
/* 180 */         htmlTree2.addContent((Content)htmlTree);
/*     */       } 
/*     */     } 
/* 183 */     if (str1.length() > 0) {
/* 184 */       StringContent stringContent1 = new StringContent(str1);
/* 185 */       HtmlTree htmlTree = HtmlTree.DIV(HtmlStyle.subTitle, (Content)stringContent1);
/* 186 */       htmlTree2.addContent((Content)htmlTree);
/*     */     } 
/* 188 */     LinkInfoImpl linkInfoImpl = new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_HEADER, this.classDoc);
/*     */ 
/*     */     
/* 191 */     linkInfoImpl.linkToSelf = false;
/* 192 */     StringContent stringContent = new StringContent(paramString);
/* 193 */     HtmlTree htmlTree3 = HtmlTree.HEADING(HtmlConstants.CLASS_PAGE_HEADING, true, HtmlStyle.title, (Content)stringContent);
/*     */     
/* 195 */     htmlTree3.addContent(getTypeParameterLinks(linkInfoImpl));
/* 196 */     htmlTree2.addContent((Content)htmlTree3);
/* 197 */     htmlTree1.addContent((Content)htmlTree2);
/* 198 */     return (Content)htmlTree1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getClassContentHeader() {
/* 205 */     return getContentHeader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFooter(Content paramContent) {
/* 212 */     paramContent.addContent(HtmlConstants.END_OF_CLASS_DATA);
/* 213 */     addNavLinks(false, paramContent);
/* 214 */     addBottom(paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printDocument(Content paramContent) throws IOException {
/* 221 */     printHtmlDocument(this.configuration.metakeywords.getMetaKeywords(this.classDoc), true, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getClassInfoTreeHeader() {
/* 229 */     return getMemberTreeHeader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getClassInfo(Content paramContent) {
/* 236 */     return getMemberTree(HtmlStyle.description, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addClassSignature(String paramString, Content paramContent) {
/* 243 */     boolean bool = this.classDoc.isInterface();
/* 244 */     paramContent.addContent((Content)new HtmlTree(HtmlTag.BR));
/* 245 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.PRE);
/* 246 */     addAnnotationInfo((ProgramElementDoc)this.classDoc, (Content)htmlTree);
/* 247 */     htmlTree.addContent(paramString);
/* 248 */     LinkInfoImpl linkInfoImpl = new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_SIGNATURE, this.classDoc);
/*     */ 
/*     */     
/* 251 */     linkInfoImpl.linkToSelf = false;
/* 252 */     StringContent stringContent = new StringContent(this.classDoc.name());
/* 253 */     Content content = getTypeParameterLinks(linkInfoImpl);
/* 254 */     if (this.configuration.linksource) {
/* 255 */       addSrcLink((ProgramElementDoc)this.classDoc, (Content)stringContent, (Content)htmlTree);
/* 256 */       htmlTree.addContent(content);
/*     */     } else {
/* 258 */       HtmlTree htmlTree1 = HtmlTree.SPAN(HtmlStyle.typeNameLabel, (Content)stringContent);
/* 259 */       htmlTree1.addContent(content);
/* 260 */       htmlTree.addContent((Content)htmlTree1);
/*     */     } 
/* 262 */     if (!bool) {
/* 263 */       Type type = Util.getFirstVisibleSuperClass(this.classDoc, this.configuration);
/*     */       
/* 265 */       if (type != null) {
/* 266 */         htmlTree.addContent(DocletConstants.NL);
/* 267 */         htmlTree.addContent("extends ");
/* 268 */         Content content1 = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_SIGNATURE_PARENT_NAME, type));
/*     */ 
/*     */         
/* 271 */         htmlTree.addContent(content1);
/*     */       } 
/*     */     } 
/* 274 */     Type[] arrayOfType = this.classDoc.interfaceTypes();
/* 275 */     if (arrayOfType != null && arrayOfType.length > 0) {
/* 276 */       byte b1 = 0;
/* 277 */       for (byte b2 = 0; b2 < arrayOfType.length; b2++) {
/* 278 */         ClassDoc classDoc = arrayOfType[b2].asClassDoc();
/* 279 */         if (classDoc.isPublic() || 
/* 280 */           Util.isLinkable(classDoc, this.configuration)) {
/*     */ 
/*     */           
/* 283 */           if (!b1) {
/* 284 */             htmlTree.addContent(DocletConstants.NL);
/* 285 */             htmlTree.addContent(bool ? "extends " : "implements ");
/*     */           } else {
/* 287 */             htmlTree.addContent(", ");
/*     */           } 
/* 289 */           Content content1 = getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_SIGNATURE_PARENT_NAME, arrayOfType[b2]));
/*     */ 
/*     */           
/* 292 */           htmlTree.addContent(content1);
/* 293 */           b1++;
/*     */         } 
/*     */       } 
/* 296 */     }  paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addClassDescription(Content paramContent) {
/* 303 */     if (!this.configuration.nocomment)
/*     */     {
/* 305 */       if ((this.classDoc.inlineTags()).length > 0) {
/* 306 */         addInlineComment((Doc)this.classDoc, paramContent);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addClassTagInfo(Content paramContent) {
/* 315 */     if (!this.configuration.nocomment)
/*     */     {
/* 317 */       addTagsInfo((Doc)this.classDoc, paramContent);
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
/*     */   private Content getClassInheritenceTree(Type paramType) {
/* 329 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.UL);
/* 330 */     htmlTree.addStyle(HtmlStyle.inheritance);
/* 331 */     Content content = null; while (true) {
/*     */       HtmlTree htmlTree1;
/* 333 */       Type type = Util.getFirstVisibleSuperClass((paramType instanceof ClassDoc) ? (ClassDoc)paramType : paramType
/* 334 */           .asClassDoc(), this.configuration);
/*     */       
/* 336 */       if (type != null) {
/* 337 */         HtmlTree htmlTree2 = new HtmlTree(HtmlTag.UL);
/* 338 */         htmlTree2.addStyle(HtmlStyle.inheritance);
/* 339 */         htmlTree2.addContent(getTreeForClassHelper(paramType));
/* 340 */         if (content != null)
/* 341 */           htmlTree2.addContent(content); 
/* 342 */         HtmlTree htmlTree3 = HtmlTree.LI((Content)htmlTree2);
/* 343 */         htmlTree1 = htmlTree3;
/* 344 */         paramType = type;
/*     */       } else {
/*     */         
/* 347 */         htmlTree.addContent(getTreeForClassHelper(paramType));
/*     */       } 
/* 349 */       if (type == null) {
/* 350 */         if (htmlTree1 != null)
/* 351 */           htmlTree.addContent((Content)htmlTree1); 
/* 352 */         return (Content)htmlTree;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Content getTreeForClassHelper(Type paramType) {
/* 362 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.LI);
/* 363 */     if (paramType.equals(this.classDoc)) {
/* 364 */       Content content = getTypeParameterLinks(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.TREE, this.classDoc));
/*     */ 
/*     */       
/* 367 */       if (this.configuration.shouldExcludeQualifier(this.classDoc
/* 368 */           .containingPackage().name())) {
/* 369 */         htmlTree.addContent(paramType.asClassDoc().name());
/* 370 */         htmlTree.addContent(content);
/*     */       } else {
/* 372 */         htmlTree.addContent(paramType.asClassDoc().qualifiedName());
/* 373 */         htmlTree.addContent(content);
/*     */       } 
/*     */     } else {
/* 376 */       Content content = getLink((new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS_TREE_PARENT, paramType))
/*     */           
/* 378 */           .label(this.configuration.getClassName(paramType.asClassDoc())));
/* 379 */       htmlTree.addContent(content);
/*     */     } 
/* 381 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addClassTree(Content paramContent) {
/* 388 */     if (!this.classDoc.isClass()) {
/*     */       return;
/*     */     }
/* 391 */     paramContent.addContent(getClassInheritenceTree((Type)this.classDoc));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTypeParamInfo(Content paramContent) {
/* 398 */     if ((this.classDoc.typeParamTags()).length > 0) {
/* 399 */       Content content = (new ParamTaglet()).getTagletOutput((Doc)this.classDoc, 
/* 400 */           getTagletWriterInstance(false));
/* 401 */       HtmlTree htmlTree = HtmlTree.DL(content);
/* 402 */       paramContent.addContent((Content)htmlTree);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSubClassInfo(Content paramContent) {
/* 410 */     if (this.classDoc.isClass()) {
/* 411 */       if (this.classDoc.qualifiedName().equals("java.lang.Object") || this.classDoc
/* 412 */         .qualifiedName().equals("org.omg.CORBA.Object")) {
/*     */         return;
/*     */       }
/* 415 */       List<?> list = this.classtree.subs(this.classDoc, false);
/* 416 */       if (list.size() > 0) {
/* 417 */         Content content = getResource("doclet.Subclasses");
/*     */         
/* 419 */         HtmlTree htmlTree1 = HtmlTree.DT(content);
/* 420 */         HtmlTree htmlTree2 = HtmlTree.DL((Content)htmlTree1);
/* 421 */         htmlTree2.addContent(getClassLinks(LinkInfoImpl.Kind.SUBCLASSES, list));
/*     */         
/* 423 */         paramContent.addContent((Content)htmlTree2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSubInterfacesInfo(Content paramContent) {
/* 432 */     if (this.classDoc.isInterface()) {
/* 433 */       List<?> list = this.classtree.allSubs(this.classDoc, false);
/* 434 */       if (list.size() > 0) {
/* 435 */         Content content = getResource("doclet.Subinterfaces");
/*     */         
/* 437 */         HtmlTree htmlTree1 = HtmlTree.DT(content);
/* 438 */         HtmlTree htmlTree2 = HtmlTree.DL((Content)htmlTree1);
/* 439 */         htmlTree2.addContent(getClassLinks(LinkInfoImpl.Kind.SUBINTERFACES, list));
/*     */         
/* 441 */         paramContent.addContent((Content)htmlTree2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInterfaceUsageInfo(Content paramContent) {
/* 450 */     if (!this.classDoc.isInterface()) {
/*     */       return;
/*     */     }
/* 453 */     if (this.classDoc.qualifiedName().equals("java.lang.Cloneable") || this.classDoc
/* 454 */       .qualifiedName().equals("java.io.Serializable")) {
/*     */       return;
/*     */     }
/* 457 */     List<?> list = this.classtree.implementingclasses(this.classDoc);
/* 458 */     if (list.size() > 0) {
/* 459 */       Content content = getResource("doclet.Implementing_Classes");
/*     */       
/* 461 */       HtmlTree htmlTree1 = HtmlTree.DT(content);
/* 462 */       HtmlTree htmlTree2 = HtmlTree.DL((Content)htmlTree1);
/* 463 */       htmlTree2.addContent(getClassLinks(LinkInfoImpl.Kind.IMPLEMENTED_CLASSES, list));
/*     */       
/* 465 */       paramContent.addContent((Content)htmlTree2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addImplementedInterfacesInfo(Content paramContent) {
/* 475 */     List<?> list = Util.getAllInterfaces((Type)this.classDoc, this.configuration);
/* 476 */     if (this.classDoc.isClass() && list.size() > 0) {
/* 477 */       Content content = getResource("doclet.All_Implemented_Interfaces");
/*     */       
/* 479 */       HtmlTree htmlTree1 = HtmlTree.DT(content);
/* 480 */       HtmlTree htmlTree2 = HtmlTree.DL((Content)htmlTree1);
/* 481 */       htmlTree2.addContent(getClassLinks(LinkInfoImpl.Kind.IMPLEMENTED_INTERFACES, list));
/*     */       
/* 483 */       paramContent.addContent((Content)htmlTree2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSuperInterfacesInfo(Content paramContent) {
/* 493 */     List<?> list = Util.getAllInterfaces((Type)this.classDoc, this.configuration);
/* 494 */     if (this.classDoc.isInterface() && list.size() > 0) {
/* 495 */       Content content = getResource("doclet.All_Superinterfaces");
/*     */       
/* 497 */       HtmlTree htmlTree1 = HtmlTree.DT(content);
/* 498 */       HtmlTree htmlTree2 = HtmlTree.DL((Content)htmlTree1);
/* 499 */       htmlTree2.addContent(getClassLinks(LinkInfoImpl.Kind.SUPER_INTERFACES, list));
/*     */       
/* 501 */       paramContent.addContent((Content)htmlTree2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNestedClassInfo(Content paramContent) {
/* 509 */     ClassDoc classDoc = this.classDoc.containingClass();
/* 510 */     if (classDoc != null) {
/*     */       Content content;
/* 512 */       if (classDoc.isInterface()) {
/* 513 */         content = getResource("doclet.Enclosing_Interface");
/*     */       } else {
/*     */         
/* 516 */         content = getResource("doclet.Enclosing_Class");
/*     */       } 
/*     */       
/* 519 */       HtmlTree htmlTree1 = HtmlTree.DT(content);
/* 520 */       HtmlTree htmlTree2 = HtmlTree.DL((Content)htmlTree1);
/* 521 */       HtmlTree htmlTree3 = new HtmlTree(HtmlTag.DD);
/* 522 */       htmlTree3.addContent(getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.CLASS, classDoc)));
/*     */       
/* 524 */       htmlTree2.addContent((Content)htmlTree3);
/* 525 */       paramContent.addContent((Content)htmlTree2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFunctionalInterfaceInfo(Content paramContent) {
/* 533 */     if (isFunctionalInterface()) {
/* 534 */       HtmlTree htmlTree1 = HtmlTree.DT(getResource("doclet.Functional_Interface"));
/* 535 */       HtmlTree htmlTree2 = HtmlTree.DL((Content)htmlTree1);
/* 536 */       HtmlTree htmlTree3 = new HtmlTree(HtmlTag.DD);
/* 537 */       htmlTree3.addContent(getResource("doclet.Functional_Interface_Message"));
/* 538 */       htmlTree2.addContent((Content)htmlTree3);
/* 539 */       paramContent.addContent((Content)htmlTree2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isFunctionalInterface() {
/* 544 */     if (this.configuration.root instanceof RootDocImpl) {
/* 545 */       RootDocImpl rootDocImpl = (RootDocImpl)this.configuration.root;
/* 546 */       AnnotationDesc[] arrayOfAnnotationDesc = this.classDoc.annotations();
/* 547 */       for (AnnotationDesc annotationDesc : arrayOfAnnotationDesc) {
/* 548 */         if (rootDocImpl.isFunctionalInterface(annotationDesc)) {
/* 549 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 553 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addClassDeprecationInfo(Content paramContent) {
/* 560 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.HR);
/* 561 */     paramContent.addContent((Content)htmlTree);
/* 562 */     Tag[] arrayOfTag = this.classDoc.tags("deprecated");
/* 563 */     if (Util.isDeprecated((Doc)this.classDoc)) {
/* 564 */       HtmlTree htmlTree1 = HtmlTree.SPAN(HtmlStyle.deprecatedLabel, this.deprecatedPhrase);
/* 565 */       HtmlTree htmlTree2 = HtmlTree.DIV(HtmlStyle.block, (Content)htmlTree1);
/* 566 */       if (arrayOfTag.length > 0) {
/* 567 */         Tag[] arrayOfTag1 = arrayOfTag[0].inlineTags();
/* 568 */         if (arrayOfTag1.length > 0) {
/* 569 */           htmlTree2.addContent(getSpace());
/* 570 */           addInlineDeprecatedComment((Doc)this.classDoc, arrayOfTag[0], (Content)htmlTree2);
/*     */         } 
/*     */       } 
/* 573 */       paramContent.addContent((Content)htmlTree2);
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
/*     */   private Content getClassLinks(LinkInfoImpl.Kind paramKind, List<?> paramList) {
/* 585 */     Object[] arrayOfObject = paramList.toArray();
/* 586 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.DD);
/* 587 */     for (byte b = 0; b < paramList.size(); b++) {
/* 588 */       if (b > 0) {
/* 589 */         StringContent stringContent = new StringContent(", ");
/* 590 */         htmlTree.addContent((Content)stringContent);
/*     */       } 
/* 592 */       if (arrayOfObject[b] instanceof ClassDoc) {
/* 593 */         Content content = getLink(new LinkInfoImpl(this.configuration, paramKind, (ClassDoc)arrayOfObject[b]));
/*     */         
/* 595 */         htmlTree.addContent(content);
/*     */       } else {
/* 597 */         Content content = getLink(new LinkInfoImpl(this.configuration, paramKind, (Type)arrayOfObject[b]));
/*     */         
/* 599 */         htmlTree.addContent(content);
/*     */       } 
/*     */     } 
/* 602 */     return (Content)htmlTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavLinkTree() {
/* 609 */     Content content = getHyperLink(DocPaths.PACKAGE_TREE, this.treeLabel, "", "");
/*     */     
/* 611 */     return (Content)HtmlTree.LI(content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addSummaryDetailLinks(Content paramContent) {
/*     */     try {
/* 622 */       HtmlTree htmlTree = HtmlTree.DIV(getNavSummaryLinks());
/* 623 */       htmlTree.addContent(getNavDetailLinks());
/* 624 */       paramContent.addContent((Content)htmlTree);
/* 625 */     } catch (Exception exception) {
/* 626 */       exception.printStackTrace();
/* 627 */       throw new DocletAbortException(exception);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavSummaryLinks() throws Exception {
/* 637 */     HtmlTree htmlTree1 = HtmlTree.LI(this.summaryLabel);
/* 638 */     htmlTree1.addContent(getSpace());
/* 639 */     HtmlTree htmlTree2 = HtmlTree.UL(HtmlStyle.subNavList, (Content)htmlTree1);
/*     */     
/* 641 */     MemberSummaryBuilder memberSummaryBuilder = (MemberSummaryBuilder)this.configuration.getBuilderFactory().getMemberSummaryBuilder(this);
/* 642 */     String[] arrayOfString = { "doclet.navNested", "doclet.navEnum", "doclet.navField", "doclet.navConstructor", "doclet.navMethod" };
/*     */ 
/*     */ 
/*     */     
/* 646 */     for (byte b = 0; b < arrayOfString.length; b++) {
/* 647 */       HtmlTree htmlTree = new HtmlTree(HtmlTag.LI);
/* 648 */       if (b != 1 || this.classDoc.isEnum())
/*     */       {
/*     */         
/* 651 */         if (b != 3 || !this.classDoc.isEnum()) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 656 */           AbstractMemberWriter abstractMemberWriter = (AbstractMemberWriter)memberSummaryBuilder.getMemberSummaryWriter(b);
/* 657 */           if (abstractMemberWriter == null) {
/* 658 */             htmlTree.addContent(getResource(arrayOfString[b]));
/*     */           } else {
/* 660 */             abstractMemberWriter.addNavSummaryLink(memberSummaryBuilder
/* 661 */                 .members(b), memberSummaryBuilder
/* 662 */                 .getVisibleMemberMap(b), (Content)htmlTree);
/*     */           } 
/* 664 */           if (b < arrayOfString.length - 1) {
/* 665 */             addNavGap((Content)htmlTree);
/*     */           }
/* 667 */           htmlTree2.addContent((Content)htmlTree);
/*     */         }  } 
/* 669 */     }  return (Content)htmlTree2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getNavDetailLinks() throws Exception {
/* 678 */     HtmlTree htmlTree1 = HtmlTree.LI(this.detailLabel);
/* 679 */     htmlTree1.addContent(getSpace());
/* 680 */     HtmlTree htmlTree2 = HtmlTree.UL(HtmlStyle.subNavList, (Content)htmlTree1);
/*     */     
/* 682 */     MemberSummaryBuilder memberSummaryBuilder = (MemberSummaryBuilder)this.configuration.getBuilderFactory().getMemberSummaryBuilder(this);
/* 683 */     String[] arrayOfString = { "doclet.navNested", "doclet.navEnum", "doclet.navField", "doclet.navConstructor", "doclet.navMethod" };
/*     */ 
/*     */ 
/*     */     
/* 687 */     for (byte b = 1; b < arrayOfString.length; b++) {
/* 688 */       HtmlTree htmlTree = new HtmlTree(HtmlTag.LI);
/*     */ 
/*     */       
/* 691 */       AbstractMemberWriter abstractMemberWriter = (AbstractMemberWriter)memberSummaryBuilder.getMemberSummaryWriter(b);
/* 692 */       if (b != 1 || this.classDoc.isEnum())
/*     */       {
/*     */         
/* 695 */         if (b != 3 || !this.classDoc.isEnum()) {
/*     */ 
/*     */           
/* 698 */           if (abstractMemberWriter == null) {
/* 699 */             htmlTree.addContent(getResource(arrayOfString[b]));
/*     */           } else {
/* 701 */             abstractMemberWriter.addNavDetailLink(memberSummaryBuilder.members(b), (Content)htmlTree);
/*     */           } 
/* 703 */           if (b < arrayOfString.length - 1) {
/* 704 */             addNavGap((Content)htmlTree);
/*     */           }
/* 706 */           htmlTree2.addContent((Content)htmlTree);
/*     */         }  } 
/* 708 */     }  return (Content)htmlTree2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addNavGap(Content paramContent) {
/* 717 */     paramContent.addContent(getSpace());
/* 718 */     paramContent.addContent("|");
/* 719 */     paramContent.addContent(getSpace());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDoc getClassDoc() {
/* 728 */     return this.classDoc;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\ClassWriterImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */