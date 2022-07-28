/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ConstructorDoc;
/*     */ import com.sun.javadoc.ProgramElementDoc;
/*     */ import com.sun.tools.doclets.internal.toolkit.ConstructorWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class ConstructorBuilder
/*     */   extends AbstractMemberBuilder
/*     */ {
/*     */   public static final String NAME = "ConstructorDetails";
/*     */   private int currentConstructorIndex;
/*     */   private final ClassDoc classDoc;
/*     */   private final VisibleMemberMap visibleMemberMap;
/*     */   private final ConstructorWriter writer;
/*     */   private final List<ProgramElementDoc> constructors;
/*     */
/*     */   private ConstructorBuilder(Context paramContext, ClassDoc paramClassDoc, ConstructorWriter paramConstructorWriter) {
/*  89 */     super(paramContext);
/*  90 */     this.classDoc = paramClassDoc;
/*  91 */     this.writer = paramConstructorWriter;
/*  92 */     this.visibleMemberMap = new VisibleMemberMap(paramClassDoc, 3, this.configuration);
/*     */
/*     */
/*     */
/*     */
/*  97 */     this
/*  98 */       .constructors = new ArrayList<>(this.visibleMemberMap.getMembersFor(paramClassDoc));
/*  99 */     for (byte b = 0; b < this.constructors.size(); b++) {
/* 100 */       if (((ProgramElementDoc)this.constructors.get(b)).isProtected() || ((ProgramElementDoc)this.constructors
/* 101 */         .get(b)).isPrivate()) {
/* 102 */         paramConstructorWriter.setFoundNonPubConstructor(true);
/*     */       }
/*     */     }
/* 105 */     if (this.configuration.getMemberComparator() != null) {
/* 106 */       Collections.sort(this.constructors, this.configuration.getMemberComparator());
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
/*     */   public static ConstructorBuilder getInstance(Context paramContext, ClassDoc paramClassDoc, ConstructorWriter paramConstructorWriter) {
/* 119 */     return new ConstructorBuilder(paramContext, paramClassDoc, paramConstructorWriter);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public String getName() {
/* 126 */     return "ConstructorDetails";
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean hasMembersToDocument() {
/* 133 */     return (this.constructors.size() > 0);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public List<ProgramElementDoc> members(ClassDoc paramClassDoc) {
/* 144 */     return this.visibleMemberMap.getMembersFor(paramClassDoc);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public ConstructorWriter getWriter() {
/* 153 */     return this.writer;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildConstructorDoc(XMLNode paramXMLNode, Content paramContent) {
/* 163 */     if (this.writer == null) {
/*     */       return;
/*     */     }
/* 166 */     int i = this.constructors.size();
/* 167 */     if (i > 0) {
/* 168 */       Content content = this.writer.getConstructorDetailsTreeHeader(this.classDoc, paramContent);
/*     */
/* 170 */       for (this.currentConstructorIndex = 0; this.currentConstructorIndex < i;
/* 171 */         this.currentConstructorIndex++) {
/* 172 */         Content content1 = this.writer.getConstructorDocTreeHeader((ConstructorDoc)this.constructors
/* 173 */             .get(this.currentConstructorIndex), content);
/*     */
/* 175 */         buildChildren(paramXMLNode, content1);
/* 176 */         content.addContent(this.writer.getConstructorDoc(content1, (this.currentConstructorIndex == i - 1)));
/*     */       }
/*     */
/* 179 */       paramContent.addContent(this.writer
/* 180 */           .getConstructorDetails(content));
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildSignature(XMLNode paramXMLNode, Content paramContent) {
/* 191 */     paramContent.addContent(this.writer
/* 192 */         .getSignature((ConstructorDoc)this.constructors
/* 193 */           .get(this.currentConstructorIndex)));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildDeprecationInfo(XMLNode paramXMLNode, Content paramContent) {
/* 203 */     this.writer.addDeprecated((ConstructorDoc)this.constructors
/* 204 */         .get(this.currentConstructorIndex), paramContent);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void buildConstructorComments(XMLNode paramXMLNode, Content paramContent) {
/* 215 */     if (!this.configuration.nocomment) {
/* 216 */       this.writer.addComments((ConstructorDoc)this.constructors
/* 217 */           .get(this.currentConstructorIndex), paramContent);
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
/*     */   public void buildTagInfo(XMLNode paramXMLNode, Content paramContent) {
/* 229 */     this.writer.addTags((ConstructorDoc)this.constructors.get(this.currentConstructorIndex), paramContent);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\builders\ConstructorBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
