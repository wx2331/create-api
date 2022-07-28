/*     */ package com.sun.tools.doclets.formats.html;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlAttr;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlConstants;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlStyle;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTag;
/*     */ import com.sun.tools.doclets.formats.html.markup.HtmlTree;
/*     */ import com.sun.tools.doclets.formats.html.markup.StringContent;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.taglets.DeprecatedTaglet;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MethodTypes;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public abstract class AbstractMemberWriter
/*     */ {
/*     */   protected final ConfigurationImpl configuration;
/*     */   protected final SubWriterHolderWriter writer;
/*     */   protected final ClassDoc classdoc;
/*  55 */   protected Map<String, Integer> typeMap = new LinkedHashMap<>();
/*  56 */   protected Set<MethodTypes> methodTypes = EnumSet.noneOf(MethodTypes.class);
/*  57 */   private int methodTypesOr = 0;
/*     */   
/*     */   public final boolean nodepr;
/*     */   protected boolean printedSummaryHeader = false;
/*     */   
/*     */   public AbstractMemberWriter(SubWriterHolderWriter paramSubWriterHolderWriter, ClassDoc paramClassDoc) {
/*  63 */     this.configuration = paramSubWriterHolderWriter.configuration;
/*  64 */     this.writer = paramSubWriterHolderWriter;
/*  65 */     this.nodepr = this.configuration.nodeprecated;
/*  66 */     this.classdoc = paramClassDoc;
/*     */   }
/*     */   
/*     */   public AbstractMemberWriter(SubWriterHolderWriter paramSubWriterHolderWriter) {
/*  70 */     this(paramSubWriterHolderWriter, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void addSummaryLabel(Content paramContent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getTableSummary();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Content getCaption();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String[] getSummaryTableHeader(ProgramElementDoc paramProgramElementDoc);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void addInheritedSummaryLabel(ClassDoc paramClassDoc, Content paramContent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void addSummaryAnchor(ClassDoc paramClassDoc, Content paramContent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void addInheritedSummaryAnchor(ClassDoc paramClassDoc, Content paramContent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void addSummaryType(ProgramElementDoc paramProgramElementDoc, Content paramContent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addSummaryLink(ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 146 */     addSummaryLink(LinkInfoImpl.Kind.MEMBER, paramClassDoc, paramProgramElementDoc, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void addSummaryLink(LinkInfoImpl.Kind paramKind, ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void addInheritedSummaryLink(ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Content paramContent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Content getDeprecatedLink(ProgramElementDoc paramProgramElementDoc);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Content getNavSummaryLink(ClassDoc paramClassDoc, boolean paramBoolean);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void addNavDetailLink(boolean paramBoolean, Content paramContent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addName(String paramString, Content paramContent) {
/* 202 */     paramContent.addContent(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String modifierString(MemberDoc paramMemberDoc) {
/* 213 */     int i = paramMemberDoc.modifierSpecifier();
/* 214 */     char c = 'Ġ';
/* 215 */     return Modifier.toString(i & (c ^ 0xFFFFFFFF));
/*     */   }
/*     */   
/*     */   protected String typeString(MemberDoc paramMemberDoc) {
/* 219 */     String str = "";
/* 220 */     if (paramMemberDoc instanceof MethodDoc) {
/* 221 */       str = ((MethodDoc)paramMemberDoc).returnType().toString();
/* 222 */     } else if (paramMemberDoc instanceof FieldDoc) {
/* 223 */       str = ((FieldDoc)paramMemberDoc).type().toString();
/*     */     } 
/* 225 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addModifiers(MemberDoc paramMemberDoc, Content paramContent) {
/* 235 */     String str = modifierString(paramMemberDoc);
/*     */ 
/*     */     
/* 238 */     if ((paramMemberDoc.isField() || paramMemberDoc.isMethod()) && this.writer instanceof ClassWriterImpl && ((ClassWriterImpl)this.writer)
/*     */       
/* 240 */       .getClassDoc().isInterface())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 248 */       str = (paramMemberDoc.isMethod() && ((MethodDoc)paramMemberDoc).isDefault()) ? Util.replaceText(str, "public", "default").trim() : Util.replaceText(str, "public", "").trim();
/*     */     }
/* 250 */     if (str.length() > 0) {
/* 251 */       paramContent.addContent(str);
/* 252 */       paramContent.addContent(this.writer.getSpace());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected String makeSpace(int paramInt) {
/* 257 */     if (paramInt <= 0) {
/* 258 */       return "";
/*     */     }
/* 260 */     StringBuilder stringBuilder = new StringBuilder(paramInt);
/* 261 */     for (byte b = 0; b < paramInt; b++) {
/* 262 */       stringBuilder.append(' ');
/*     */     }
/* 264 */     return stringBuilder.toString();
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
/*     */   protected void addModifierAndType(ProgramElementDoc paramProgramElementDoc, Type paramType, Content paramContent) {
/* 276 */     HtmlTree htmlTree = new HtmlTree(HtmlTag.CODE);
/* 277 */     addModifier(paramProgramElementDoc, (Content)htmlTree);
/* 278 */     if (paramType == null) {
/* 279 */       if (paramProgramElementDoc.isClass()) {
/* 280 */         htmlTree.addContent("class");
/*     */       } else {
/* 282 */         htmlTree.addContent("interface");
/*     */       } 
/* 284 */       htmlTree.addContent(this.writer.getSpace());
/*     */     }
/* 286 */     else if (paramProgramElementDoc instanceof ExecutableMemberDoc && (((ExecutableMemberDoc)paramProgramElementDoc)
/* 287 */       .typeParameters()).length > 0) {
/* 288 */       Content content = ((AbstractExecutableMemberWriter)this).getTypeParameters((ExecutableMemberDoc)paramProgramElementDoc);
/*     */       
/* 290 */       htmlTree.addContent(content);
/*     */       
/* 292 */       if (content.charCount() > 10) {
/* 293 */         htmlTree.addContent((Content)new HtmlTree(HtmlTag.BR));
/*     */       } else {
/* 295 */         htmlTree.addContent(this.writer.getSpace());
/*     */       } 
/* 297 */       htmlTree.addContent(this.writer
/* 298 */           .getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.SUMMARY_RETURN_TYPE, paramType)));
/*     */     } else {
/*     */       
/* 301 */       htmlTree.addContent(this.writer
/* 302 */           .getLink(new LinkInfoImpl(this.configuration, LinkInfoImpl.Kind.SUMMARY_RETURN_TYPE, paramType)));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 307 */     paramContent.addContent((Content)htmlTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addModifier(ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 317 */     if (paramProgramElementDoc.isProtected()) {
/* 318 */       paramContent.addContent("protected ");
/* 319 */     } else if (paramProgramElementDoc.isPrivate()) {
/* 320 */       paramContent.addContent("private ");
/* 321 */     } else if (!paramProgramElementDoc.isPublic()) {
/* 322 */       paramContent.addContent(this.configuration.getText("doclet.Package_private"));
/* 323 */       paramContent.addContent(" ");
/*     */     } 
/* 325 */     if (paramProgramElementDoc.isMethod()) {
/* 326 */       if (!paramProgramElementDoc.containingClass().isInterface() && ((MethodDoc)paramProgramElementDoc)
/* 327 */         .isAbstract()) {
/* 328 */         paramContent.addContent("abstract ");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 335 */       if (((MethodDoc)paramProgramElementDoc).isDefault()) {
/* 336 */         paramContent.addContent("default ");
/*     */       }
/*     */     } 
/* 339 */     if (paramProgramElementDoc.isStatic()) {
/* 340 */       paramContent.addContent("static ");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addDeprecatedInfo(ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 351 */     Content content = (new DeprecatedTaglet()).getTagletOutput((Doc)paramProgramElementDoc, this.writer
/* 352 */         .getTagletWriterInstance(false));
/* 353 */     if (!content.isEmpty()) {
/* 354 */       Content content1 = content;
/* 355 */       HtmlTree htmlTree = HtmlTree.DIV(HtmlStyle.block, content1);
/* 356 */       paramContent.addContent((Content)htmlTree);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addComment(ProgramElementDoc paramProgramElementDoc, Content paramContent) {
/* 367 */     if ((paramProgramElementDoc.inlineTags()).length > 0) {
/* 368 */       this.writer.addInlineComment((Doc)paramProgramElementDoc, paramContent);
/*     */     }
/*     */   }
/*     */   
/*     */   protected String name(ProgramElementDoc paramProgramElementDoc) {
/* 373 */     return paramProgramElementDoc.name();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Content getHead(MemberDoc paramMemberDoc) {
/* 383 */     StringContent stringContent = new StringContent(paramMemberDoc.name());
/* 384 */     return (Content)HtmlTree.HEADING(HtmlConstants.MEMBER_HEADING, (Content)stringContent);
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
/*     */   protected boolean isInherited(ProgramElementDoc paramProgramElementDoc) {
/* 397 */     if (paramProgramElementDoc.isPrivate() || (paramProgramElementDoc.isPackagePrivate() && 
/* 398 */       !paramProgramElementDoc.containingPackage().equals(this.classdoc.containingPackage()))) {
/* 399 */       return false;
/*     */     }
/* 401 */     return true;
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
/*     */   protected void addDeprecatedAPI(List<Doc> paramList, String paramString1, String paramString2, String[] paramArrayOfString, Content paramContent) {
/* 415 */     if (paramList.size() > 0) {
/* 416 */       HtmlTree htmlTree1 = HtmlTree.TABLE(HtmlStyle.deprecatedSummary, 0, 3, 0, paramString2, this.writer
/* 417 */           .getTableCaption(this.configuration.getResource(paramString1)));
/* 418 */       htmlTree1.addContent(this.writer.getSummaryTableHeader(paramArrayOfString, "col"));
/* 419 */       HtmlTree htmlTree2 = new HtmlTree(HtmlTag.TBODY);
/* 420 */       for (byte b = 0; b < paramList.size(); b++) {
/* 421 */         ProgramElementDoc programElementDoc = (ProgramElementDoc)paramList.get(b);
/* 422 */         HtmlTree htmlTree5 = HtmlTree.TD(HtmlStyle.colOne, getDeprecatedLink(programElementDoc));
/* 423 */         if ((programElementDoc.tags("deprecated")).length > 0)
/* 424 */           this.writer.addInlineDeprecatedComment((Doc)programElementDoc, programElementDoc
/* 425 */               .tags("deprecated")[0], (Content)htmlTree5); 
/* 426 */         HtmlTree htmlTree6 = HtmlTree.TR((Content)htmlTree5);
/* 427 */         if (b % 2 == 0) {
/* 428 */           htmlTree6.addStyle(HtmlStyle.altColor);
/*     */         } else {
/* 430 */           htmlTree6.addStyle(HtmlStyle.rowColor);
/* 431 */         }  htmlTree2.addContent((Content)htmlTree6);
/*     */       } 
/* 433 */       htmlTree1.addContent((Content)htmlTree2);
/* 434 */       HtmlTree htmlTree3 = HtmlTree.LI(HtmlStyle.blockList, (Content)htmlTree1);
/* 435 */       HtmlTree htmlTree4 = HtmlTree.UL(HtmlStyle.blockList, (Content)htmlTree3);
/* 436 */       paramContent.addContent((Content)htmlTree4);
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
/*     */   protected void addUseInfo(List<? extends ProgramElementDoc> paramList, Content paramContent1, String paramString, Content paramContent2) {
/* 450 */     if (paramList == null) {
/*     */       return;
/*     */     }
/* 453 */     List<? extends ProgramElementDoc> list = paramList;
/* 454 */     boolean bool = false;
/* 455 */     if (list.size() > 0) {
/* 456 */       HtmlTree htmlTree1 = HtmlTree.TABLE(HtmlStyle.useSummary, 0, 3, 0, paramString, this.writer
/* 457 */           .getTableCaption(paramContent1));
/* 458 */       HtmlTree htmlTree2 = new HtmlTree(HtmlTag.TBODY);
/* 459 */       Iterator<? extends ProgramElementDoc> iterator = list.iterator();
/* 460 */       for (byte b = 0; iterator.hasNext(); b++) {
/* 461 */         ProgramElementDoc programElementDoc = iterator.next();
/* 462 */         ClassDoc classDoc = programElementDoc.containingClass();
/* 463 */         if (!bool) {
/* 464 */           htmlTree1.addContent(this.writer.getSummaryTableHeader(
/* 465 */                 getSummaryTableHeader(programElementDoc), "col"));
/* 466 */           bool = true;
/*     */         } 
/* 468 */         HtmlTree htmlTree3 = new HtmlTree(HtmlTag.TR);
/* 469 */         if (b % 2 == 0) {
/* 470 */           htmlTree3.addStyle(HtmlStyle.altColor);
/*     */         } else {
/* 472 */           htmlTree3.addStyle(HtmlStyle.rowColor);
/*     */         } 
/* 474 */         HtmlTree htmlTree4 = new HtmlTree(HtmlTag.TD);
/* 475 */         htmlTree4.addStyle(HtmlStyle.colFirst);
/* 476 */         this.writer.addSummaryType(this, programElementDoc, (Content)htmlTree4);
/* 477 */         htmlTree3.addContent((Content)htmlTree4);
/* 478 */         HtmlTree htmlTree5 = new HtmlTree(HtmlTag.TD);
/* 479 */         htmlTree5.addStyle(HtmlStyle.colLast);
/* 480 */         if (classDoc != null && !(programElementDoc instanceof com.sun.javadoc.ConstructorDoc) && !(programElementDoc instanceof ClassDoc)) {
/*     */           
/* 482 */           HtmlTree htmlTree = new HtmlTree(HtmlTag.SPAN);
/* 483 */           htmlTree.addStyle(HtmlStyle.typeNameLabel);
/* 484 */           htmlTree.addContent(classDoc.name() + ".");
/* 485 */           htmlTree5.addContent((Content)htmlTree);
/*     */         } 
/* 487 */         addSummaryLink((programElementDoc instanceof ClassDoc) ? LinkInfoImpl.Kind.CLASS_USE : LinkInfoImpl.Kind.MEMBER, classDoc, programElementDoc, (Content)htmlTree5);
/*     */ 
/*     */         
/* 490 */         this.writer.addSummaryLinkComment(this, programElementDoc, (Content)htmlTree5);
/* 491 */         htmlTree3.addContent((Content)htmlTree5);
/* 492 */         htmlTree2.addContent((Content)htmlTree3);
/*     */       } 
/* 494 */       htmlTree1.addContent((Content)htmlTree2);
/* 495 */       paramContent2.addContent((Content)htmlTree1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addNavDetailLink(List<?> paramList, Content paramContent) {
/* 506 */     addNavDetailLink((paramList.size() > 0), paramContent);
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
/*     */   protected void addNavSummaryLink(List<?> paramList, VisibleMemberMap paramVisibleMemberMap, Content paramContent) {
/* 518 */     if (paramList.size() > 0) {
/* 519 */       paramContent.addContent(getNavSummaryLink(null, true));
/*     */       return;
/*     */     } 
/* 522 */     ClassDoc classDoc = this.classdoc.superclass();
/* 523 */     while (classDoc != null) {
/* 524 */       List list = paramVisibleMemberMap.getMembersFor(classDoc);
/* 525 */       if (list.size() > 0) {
/* 526 */         paramContent.addContent(getNavSummaryLink(classDoc, true));
/*     */         return;
/*     */       } 
/* 529 */       classDoc = classDoc.superclass();
/*     */     } 
/* 531 */     paramContent.addContent(getNavSummaryLink(null, false));
/*     */   }
/*     */   
/*     */   protected void serialWarning(SourcePosition paramSourcePosition, String paramString1, String paramString2, String paramString3) {
/* 535 */     if (this.configuration.serialwarn) {
/* 536 */       this.configuration.getDocletSpecificMsg().warning(paramSourcePosition, paramString1, new Object[] { paramString2, paramString3 });
/*     */     }
/*     */   }
/*     */   
/*     */   public ProgramElementDoc[] eligibleMembers(ProgramElementDoc[] paramArrayOfProgramElementDoc) {
/* 541 */     return this.nodepr ? Util.excludeDeprecatedMembers(paramArrayOfProgramElementDoc) : paramArrayOfProgramElementDoc;
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
/*     */   public void addMemberSummary(ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Tag[] paramArrayOfTag, List<Content> paramList, int paramInt) {
/* 555 */     HtmlTree htmlTree1 = new HtmlTree(HtmlTag.TD);
/* 556 */     htmlTree1.addStyle(HtmlStyle.colFirst);
/* 557 */     this.writer.addSummaryType(this, paramProgramElementDoc, (Content)htmlTree1);
/* 558 */     HtmlTree htmlTree2 = new HtmlTree(HtmlTag.TD);
/* 559 */     setSummaryColumnStyle(htmlTree2);
/* 560 */     addSummaryLink(paramClassDoc, paramProgramElementDoc, (Content)htmlTree2);
/* 561 */     this.writer.addSummaryLinkComment(this, paramProgramElementDoc, paramArrayOfTag, (Content)htmlTree2);
/* 562 */     HtmlTree htmlTree3 = HtmlTree.TR((Content)htmlTree1);
/* 563 */     htmlTree3.addContent((Content)htmlTree2);
/* 564 */     if (paramProgramElementDoc instanceof MethodDoc && !paramProgramElementDoc.isAnnotationTypeElement()) {
/*     */       
/* 566 */       int i = paramProgramElementDoc.isStatic() ? MethodTypes.STATIC.value() : MethodTypes.INSTANCE.value();
/* 567 */       if (paramProgramElementDoc.containingClass().isInterface()) {
/*     */ 
/*     */         
/* 570 */         i = ((MethodDoc)paramProgramElementDoc).isAbstract() ? (i | MethodTypes.ABSTRACT.value()) : (i | MethodTypes.DEFAULT.value());
/*     */       }
/*     */       else {
/*     */         
/* 574 */         i = ((MethodDoc)paramProgramElementDoc).isAbstract() ? (i | MethodTypes.ABSTRACT.value()) : (i | MethodTypes.CONCRETE.value());
/*     */       } 
/* 576 */       if (Util.isDeprecated((Doc)paramProgramElementDoc) || Util.isDeprecated((Doc)this.classdoc)) {
/* 577 */         i |= MethodTypes.DEPRECATED.value();
/*     */       }
/* 579 */       this.methodTypesOr |= i;
/* 580 */       String str = "i" + paramInt;
/* 581 */       this.typeMap.put(str, Integer.valueOf(i));
/* 582 */       htmlTree3.addAttr(HtmlAttr.ID, str);
/*     */     } 
/* 584 */     if (paramInt % 2 == 0) {
/* 585 */       htmlTree3.addStyle(HtmlStyle.altColor);
/*     */     } else {
/* 587 */       htmlTree3.addStyle(HtmlStyle.rowColor);
/* 588 */     }  paramList.add(htmlTree3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean showTabs() {
/* 599 */     for (MethodTypes methodTypes : EnumSet.<MethodTypes>allOf(MethodTypes.class)) {
/* 600 */       int i = methodTypes.value();
/* 601 */       if ((i & this.methodTypesOr) == i) {
/* 602 */         this.methodTypes.add(methodTypes);
/*     */       }
/*     */     } 
/* 605 */     boolean bool = (this.methodTypes.size() > 1) ? true : false;
/* 606 */     if (bool) {
/* 607 */       this.methodTypes.add(MethodTypes.ALL);
/*     */     }
/* 609 */     return bool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSummaryColumnStyle(HtmlTree paramHtmlTree) {
/* 618 */     paramHtmlTree.addStyle(HtmlStyle.colLast);
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
/*     */   public void addInheritedMemberSummary(ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, boolean paramBoolean1, boolean paramBoolean2, Content paramContent) {
/* 633 */     this.writer.addInheritedMemberSummary(this, paramClassDoc, paramProgramElementDoc, paramBoolean1, paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getInheritedSummaryHeader(ClassDoc paramClassDoc) {
/* 644 */     Content content = this.writer.getMemberTreeHeader();
/* 645 */     this.writer.addInheritedSummaryHeader(this, paramClassDoc, content);
/* 646 */     return content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getInheritedSummaryLinksTree() {
/* 655 */     return (Content)new HtmlTree(HtmlTag.CODE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getSummaryTableTree(ClassDoc paramClassDoc, List<Content> paramList) {
/* 666 */     return this.writer.getSummaryTableTree(this, paramClassDoc, paramList, showTabs());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMemberTree(Content paramContent) {
/* 676 */     return this.writer.getMemberTree(paramContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Content getMemberTree(Content paramContent, boolean paramBoolean) {
/* 687 */     if (paramBoolean) {
/* 688 */       return (Content)HtmlTree.UL(HtmlStyle.blockListLast, paramContent);
/*     */     }
/* 690 */     return (Content)HtmlTree.UL(HtmlStyle.blockList, paramContent);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\AbstractMemberWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */