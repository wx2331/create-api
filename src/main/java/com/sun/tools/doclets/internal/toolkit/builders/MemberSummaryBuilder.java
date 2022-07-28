/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.tools.doclets.internal.toolkit.AnnotationTypeWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.ClassWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.MemberSummaryWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.WriterFactory;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
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
/*     */
/*     */
/*     */
/*     */
/*     */ public class MemberSummaryBuilder
/*     */   extends AbstractMemberBuilder
/*     */ {
/*     */   public static final String NAME = "MemberSummary";
/*     */   private final VisibleMemberMap[] visibleMemberMaps;
/*     */   private MemberSummaryWriter[] memberSummaryWriters;
/*     */   private final ClassDoc classDoc;
/*     */
/*     */   private MemberSummaryBuilder(Context paramContext, ClassDoc paramClassDoc) {
/*  77 */     super(paramContext);
/*  78 */     this.classDoc = paramClassDoc;
/*  79 */     this.visibleMemberMaps = new VisibleMemberMap[9];
/*     */
/*  81 */     for (byte b = 0; b < 9; b++) {
/*  82 */       this.visibleMemberMaps[b] = new VisibleMemberMap(paramClassDoc, b, this.configuration);
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static MemberSummaryBuilder getInstance(ClassWriter paramClassWriter, Context paramContext) throws Exception {
/* 101 */     MemberSummaryBuilder memberSummaryBuilder = new MemberSummaryBuilder(paramContext, paramClassWriter.getClassDoc());
/* 102 */     memberSummaryBuilder.memberSummaryWriters = new MemberSummaryWriter[9];
/*     */
/* 104 */     WriterFactory writerFactory = paramContext.configuration.getWriterFactory();
/* 105 */     for (byte b = 0; b < 9; b++) {
/* 106 */       memberSummaryBuilder.memberSummaryWriters[b] =
/* 107 */         memberSummaryBuilder.visibleMemberMaps[b].noVisibleMembers() ? null : writerFactory
/*     */
/* 109 */         .getMemberSummaryWriter(paramClassWriter, b);
/*     */     }
/* 111 */     return memberSummaryBuilder;
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
/*     */   public static MemberSummaryBuilder getInstance(AnnotationTypeWriter paramAnnotationTypeWriter, Context paramContext) throws Exception {
/* 125 */     MemberSummaryBuilder memberSummaryBuilder = new MemberSummaryBuilder(paramContext, (ClassDoc)paramAnnotationTypeWriter.getAnnotationTypeDoc());
/* 126 */     memberSummaryBuilder.memberSummaryWriters = new MemberSummaryWriter[9];
/*     */
/* 128 */     WriterFactory writerFactory = paramContext.configuration.getWriterFactory();
/* 129 */     for (byte b = 0; b < 9; b++) {
/* 130 */       memberSummaryBuilder.memberSummaryWriters[b] =
/* 131 */         memberSummaryBuilder.visibleMemberMaps[b].noVisibleMembers() ? null : writerFactory
/*     */
/* 133 */         .getMemberSummaryWriter(paramAnnotationTypeWriter, b);
/*     */     }
/*     */
/* 136 */     return memberSummaryBuilder;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public String getName() {
/* 143 */     return "MemberSummary";
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
/*     */   public VisibleMemberMap getVisibleMemberMap(int paramInt) {
/* 155 */     return this.visibleMemberMaps[paramInt];
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
/*     */   public MemberSummaryWriter getMemberSummaryWriter(int paramInt) {
/* 167 */     return this.memberSummaryWriters[paramInt];
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
/*     */   public List<ProgramElementDoc> members(int paramInt) {
/* 180 */     return this.visibleMemberMaps[paramInt].getLeafClassMembers(this.configuration);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean hasMembersToDocument() {
/* 189 */     if (this.classDoc instanceof AnnotationTypeDoc) {
/* 190 */       return ((((AnnotationTypeDoc)this.classDoc).elements()).length > 0);
/*     */     }
/* 192 */     for (byte b = 0; b < 9; b++) {
/* 193 */       VisibleMemberMap visibleMemberMap = this.visibleMemberMaps[b];
/* 194 */       if (!visibleMemberMap.noVisibleMembers()) {
/* 195 */         return true;
/*     */       }
/*     */     }
/* 198 */     return false;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildEnumConstantsSummary(XMLNode paramXMLNode, Content paramContent) {
/* 208 */     MemberSummaryWriter memberSummaryWriter = this.memberSummaryWriters[1];
/*     */
/* 210 */     VisibleMemberMap visibleMemberMap = this.visibleMemberMaps[1];
/*     */
/* 212 */     addSummary(memberSummaryWriter, visibleMemberMap, false, paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildAnnotationTypeFieldsSummary(XMLNode paramXMLNode, Content paramContent) {
/* 222 */     MemberSummaryWriter memberSummaryWriter = this.memberSummaryWriters[5];
/*     */
/* 224 */     VisibleMemberMap visibleMemberMap = this.visibleMemberMaps[5];
/*     */
/* 226 */     addSummary(memberSummaryWriter, visibleMemberMap, false, paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildAnnotationTypeOptionalMemberSummary(XMLNode paramXMLNode, Content paramContent) {
/* 236 */     MemberSummaryWriter memberSummaryWriter = this.memberSummaryWriters[6];
/*     */
/* 238 */     VisibleMemberMap visibleMemberMap = this.visibleMemberMaps[6];
/*     */
/* 240 */     addSummary(memberSummaryWriter, visibleMemberMap, false, paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildAnnotationTypeRequiredMemberSummary(XMLNode paramXMLNode, Content paramContent) {
/* 250 */     MemberSummaryWriter memberSummaryWriter = this.memberSummaryWriters[7];
/*     */
/* 252 */     VisibleMemberMap visibleMemberMap = this.visibleMemberMaps[7];
/*     */
/* 254 */     addSummary(memberSummaryWriter, visibleMemberMap, false, paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildFieldsSummary(XMLNode paramXMLNode, Content paramContent) {
/* 264 */     MemberSummaryWriter memberSummaryWriter = this.memberSummaryWriters[2];
/*     */
/* 266 */     VisibleMemberMap visibleMemberMap = this.visibleMemberMaps[2];
/*     */
/* 268 */     addSummary(memberSummaryWriter, visibleMemberMap, true, paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildPropertiesSummary(XMLNode paramXMLNode, Content paramContent) {
/* 275 */     MemberSummaryWriter memberSummaryWriter = this.memberSummaryWriters[8];
/*     */
/* 277 */     VisibleMemberMap visibleMemberMap = this.visibleMemberMaps[8];
/*     */
/* 279 */     addSummary(memberSummaryWriter, visibleMemberMap, true, paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildNestedClassesSummary(XMLNode paramXMLNode, Content paramContent) {
/* 289 */     MemberSummaryWriter memberSummaryWriter = this.memberSummaryWriters[0];
/*     */
/* 291 */     VisibleMemberMap visibleMemberMap = this.visibleMemberMaps[0];
/*     */
/* 293 */     addSummary(memberSummaryWriter, visibleMemberMap, true, paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildMethodsSummary(XMLNode paramXMLNode, Content paramContent) {
/* 303 */     MemberSummaryWriter memberSummaryWriter = this.memberSummaryWriters[4];
/*     */
/* 305 */     VisibleMemberMap visibleMemberMap = this.visibleMemberMaps[4];
/*     */
/* 307 */     addSummary(memberSummaryWriter, visibleMemberMap, true, paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildConstructorsSummary(XMLNode paramXMLNode, Content paramContent) {
/* 317 */     MemberSummaryWriter memberSummaryWriter = this.memberSummaryWriters[3];
/*     */
/* 319 */     VisibleMemberMap visibleMemberMap = this.visibleMemberMaps[3];
/*     */
/* 321 */     addSummary(memberSummaryWriter, visibleMemberMap, false, paramContent);
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
/*     */   private void buildSummary(MemberSummaryWriter paramMemberSummaryWriter, VisibleMemberMap paramVisibleMemberMap, LinkedList<Content> paramLinkedList) {
/* 333 */     ArrayList<Comparable> arrayList = new ArrayList(paramVisibleMemberMap.getLeafClassMembers(this.configuration));
/*     */
/* 335 */     if (arrayList.size() > 0) {
/* 336 */       Collections.sort(arrayList);
/* 337 */       LinkedList linkedList = new LinkedList();
/* 338 */       for (byte b = 0; b < arrayList.size(); b++) {
/* 339 */         ProgramElementDoc programElementDoc1 = (ProgramElementDoc)arrayList.get(b);
/*     */
/* 341 */         ProgramElementDoc programElementDoc2 = paramVisibleMemberMap.getPropertyMemberDoc(programElementDoc1);
/* 342 */         if (programElementDoc2 != null) {
/* 343 */           processProperty(paramVisibleMemberMap, programElementDoc1, programElementDoc2);
/*     */         }
/* 345 */         Tag[] arrayOfTag = programElementDoc1.firstSentenceTags();
/* 346 */         if (programElementDoc1 instanceof MethodDoc && arrayOfTag.length == 0) {
/*     */
/*     */
/*     */
/* 350 */           DocFinder.Output output = DocFinder.search(new DocFinder.Input(programElementDoc1));
/* 351 */           if (output.holder != null && (output.holder
/* 352 */             .firstSentenceTags()).length > 0) {
/* 353 */             arrayOfTag = output.holder.firstSentenceTags();
/*     */           }
/*     */         }
/* 356 */         paramMemberSummaryWriter.addMemberSummary(this.classDoc, programElementDoc1, arrayOfTag, linkedList, b);
/*     */       }
/*     */
/* 359 */       paramLinkedList.add(paramMemberSummaryWriter.getSummaryTableTree(this.classDoc, linkedList));
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private void processProperty(VisibleMemberMap paramVisibleMemberMap, ProgramElementDoc paramProgramElementDoc1, ProgramElementDoc paramProgramElementDoc2) {
/* 378 */     StringBuilder stringBuilder = new StringBuilder();
/* 379 */     boolean bool1 = isSetter(paramProgramElementDoc1);
/* 380 */     boolean bool2 = isGetter(paramProgramElementDoc1);
/* 381 */     if (bool2 || bool1) {
/*     */
/* 383 */       if (bool1)
/* 384 */         stringBuilder.append(
/* 385 */             MessageFormat.format(this.configuration
/* 386 */               .getText("doclet.PropertySetterWithName"), new Object[] {
/* 387 */                 Util.propertyNameFromMethodName(this.configuration, paramProgramElementDoc1.name())
/*     */               }));
/* 389 */       if (bool2)
/* 390 */         stringBuilder.append(
/* 391 */             MessageFormat.format(this.configuration
/* 392 */               .getText("doclet.PropertyGetterWithName"), new Object[] {
/* 393 */                 Util.propertyNameFromMethodName(this.configuration, paramProgramElementDoc1.name())
/*     */               }));
/* 395 */       if (paramProgramElementDoc2.commentText() != null &&
/* 396 */         !paramProgramElementDoc2.commentText().isEmpty()) {
/* 397 */         stringBuilder.append(" \n @propertyDescription ");
/*     */       }
/*     */     }
/* 400 */     stringBuilder.append(paramProgramElementDoc2.commentText());
/*     */
/*     */
/* 403 */     LinkedList linkedList = new LinkedList();
/* 404 */     String[] arrayOfString = { "@defaultValue", "@since" };
/* 405 */     for (String str : arrayOfString) {
/* 406 */       Tag[] arrayOfTag = paramProgramElementDoc2.tags(str);
/* 407 */       if (arrayOfTag != null) {
/* 408 */         linkedList.addAll(Arrays.asList(arrayOfTag));
/*     */       }
/*     */     }
/* 411 */     for (Tag tag : linkedList) {
/* 412 */       stringBuilder.append("\n")
/* 413 */         .append(tag.name())
/* 414 */         .append(" ")
/* 415 */         .append(tag.text());
/*     */     }
/*     */
/*     */
/* 419 */     if (!bool2 && !bool1) {
/* 420 */       MethodDoc methodDoc1 = (MethodDoc)paramVisibleMemberMap.getGetterForProperty(paramProgramElementDoc1);
/* 421 */       MethodDoc methodDoc2 = (MethodDoc)paramVisibleMemberMap.getSetterForProperty(paramProgramElementDoc1);
/*     */
/* 423 */       if (null != methodDoc1 && stringBuilder
/* 424 */         .indexOf("@see #" + methodDoc1.name()) == -1) {
/* 425 */         stringBuilder.append("\n @see #")
/* 426 */           .append(methodDoc1.name())
/* 427 */           .append("() ");
/*     */       }
/*     */
/* 430 */       if (null != methodDoc2 && stringBuilder
/* 431 */         .indexOf("@see #" + methodDoc2.name()) == -1) {
/* 432 */         String str = methodDoc2.parameters()[0].typeName();
/*     */
/* 434 */         str = str.split("<")[0];
/* 435 */         if (str.contains(".")) {
/* 436 */           str = str.substring(str.lastIndexOf(".") + 1);
/*     */         }
/* 438 */         stringBuilder.append("\n @see #").append(methodDoc2.name());
/*     */
/* 440 */         if (methodDoc2.parameters()[0].type().asTypeVariable() == null) {
/* 441 */           stringBuilder.append("(").append(str).append(")");
/*     */         }
/* 443 */         stringBuilder.append(" \n");
/*     */       }
/*     */     }
/* 446 */     paramProgramElementDoc1.setRawCommentText(stringBuilder.toString());
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private boolean isGetter(ProgramElementDoc paramProgramElementDoc) {
/* 455 */     String str = paramProgramElementDoc.name();
/* 456 */     return (str.startsWith("get") || str.startsWith("is"));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private boolean isSetter(ProgramElementDoc paramProgramElementDoc) {
/* 466 */     return paramProgramElementDoc.name().startsWith("set");
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
/*     */   private void buildInheritedSummary(MemberSummaryWriter paramMemberSummaryWriter, VisibleMemberMap paramVisibleMemberMap, LinkedList<Content> paramLinkedList) {
/* 478 */     Iterator<ClassDoc> iterator = paramVisibleMemberMap.getVisibleClassesList().iterator();
/* 479 */     while (iterator.hasNext()) {
/* 480 */       ClassDoc classDoc = iterator.next();
/* 481 */       if (!classDoc.isPublic() &&
/* 482 */         !Util.isLinkable(classDoc, this.configuration)) {
/*     */         continue;
/*     */       }
/* 485 */       if (classDoc == this.classDoc) {
/*     */         continue;
/*     */       }
/* 488 */       List<Comparable> list = paramVisibleMemberMap.getMembersFor(classDoc);
/* 489 */       if (list.size() > 0) {
/* 490 */         Collections.sort(list);
/* 491 */         Content content1 = paramMemberSummaryWriter.getInheritedSummaryHeader(classDoc);
/* 492 */         Content content2 = paramMemberSummaryWriter.getInheritedSummaryLinksTree();
/* 493 */         for (byte b = 0; b < list.size(); b++) {
/* 494 */           paramMemberSummaryWriter.addInheritedMemberSummary((classDoc
/* 495 */               .isPackagePrivate() &&
/* 496 */               !Util.isLinkable(classDoc, this.configuration)) ? this.classDoc : classDoc, (ProgramElementDoc)list
/*     */
/* 498 */               .get(b), (b == 0),
/*     */
/* 500 */               (b == list.size() - 1), content2);
/*     */         }
/* 502 */         content1.addContent(content2);
/* 503 */         paramLinkedList.add(paramMemberSummaryWriter.getMemberTree(content1));
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
/*     */
/*     */   private void addSummary(MemberSummaryWriter paramMemberSummaryWriter, VisibleMemberMap paramVisibleMemberMap, boolean paramBoolean, Content paramContent) {
/* 519 */     LinkedList<Content> linkedList = new LinkedList();
/* 520 */     buildSummary(paramMemberSummaryWriter, paramVisibleMemberMap, linkedList);
/* 521 */     if (paramBoolean)
/* 522 */       buildInheritedSummary(paramMemberSummaryWriter, paramVisibleMemberMap, linkedList);
/* 523 */     if (!linkedList.isEmpty()) {
/* 524 */       Content content = paramMemberSummaryWriter.getMemberSummaryHeader(this.classDoc, paramContent);
/*     */
/* 526 */       for (byte b = 0; b < linkedList.size(); b++) {
/* 527 */         content.addContent(linkedList.get(b));
/*     */       }
/* 529 */       paramContent.addContent(paramMemberSummaryWriter.getMemberTree(content));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\builders\MemberSummaryBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
