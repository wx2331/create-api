/*     */ package com.sun.source.util;
/*     */ 
/*     */ import com.sun.source.doctree.AttributeTree;
/*     */ import com.sun.source.doctree.AuthorTree;
/*     */ import com.sun.source.doctree.CommentTree;
/*     */ import com.sun.source.doctree.DeprecatedTree;
/*     */ import com.sun.source.doctree.DocCommentTree;
/*     */ import com.sun.source.doctree.DocRootTree;
/*     */ import com.sun.source.doctree.DocTree;
/*     */ import com.sun.source.doctree.DocTreeVisitor;
/*     */ import com.sun.source.doctree.EndElementTree;
/*     */ import com.sun.source.doctree.EntityTree;
/*     */ import com.sun.source.doctree.ErroneousTree;
/*     */ import com.sun.source.doctree.IdentifierTree;
/*     */ import com.sun.source.doctree.InheritDocTree;
/*     */ import com.sun.source.doctree.LinkTree;
/*     */ import com.sun.source.doctree.LiteralTree;
/*     */ import com.sun.source.doctree.ParamTree;
/*     */ import com.sun.source.doctree.ReferenceTree;
/*     */ import com.sun.source.doctree.ReturnTree;
/*     */ import com.sun.source.doctree.SeeTree;
/*     */ import com.sun.source.doctree.SerialDataTree;
/*     */ import com.sun.source.doctree.SerialFieldTree;
/*     */ import com.sun.source.doctree.SerialTree;
/*     */ import com.sun.source.doctree.SinceTree;
/*     */ import com.sun.source.doctree.StartElementTree;
/*     */ import com.sun.source.doctree.TextTree;
/*     */ import com.sun.source.doctree.ThrowsTree;
/*     */ import com.sun.source.doctree.UnknownBlockTagTree;
/*     */ import com.sun.source.doctree.UnknownInlineTagTree;
/*     */ import com.sun.source.doctree.ValueTree;
/*     */ import com.sun.source.doctree.VersionTree;
/*     */ import jdk.Exported;
/*     */ 
/*     */ @Exported
/*     */ public class SimpleDocTreeVisitor<R, P> implements DocTreeVisitor<R, P> {
/*     */   protected final R DEFAULT_VALUE;
/*     */   
/*     */   protected SimpleDocTreeVisitor() {
/*  40 */     this.DEFAULT_VALUE = null;
/*     */   }
/*     */   
/*     */   protected SimpleDocTreeVisitor(R paramR) {
/*  44 */     this.DEFAULT_VALUE = paramR;
/*     */   }
/*     */   
/*     */   protected R defaultAction(DocTree paramDocTree, P paramP) {
/*  48 */     return this.DEFAULT_VALUE;
/*     */   }
/*     */   
/*     */   public final R visit(DocTree paramDocTree, P paramP) {
/*  52 */     return (paramDocTree == null) ? null : (R)paramDocTree.accept(this, paramP);
/*     */   }
/*     */   
/*     */   public final R visit(Iterable<? extends DocTree> paramIterable, P paramP) {
/*  56 */     R r = null;
/*  57 */     if (paramIterable != null)
/*  58 */       for (DocTree docTree : paramIterable) {
/*  59 */         r = visit(docTree, paramP);
/*     */       } 
/*  61 */     return r;
/*     */   }
/*     */   
/*     */   public R visitAttribute(AttributeTree paramAttributeTree, P paramP) {
/*  65 */     return defaultAction((DocTree)paramAttributeTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitAuthor(AuthorTree paramAuthorTree, P paramP) {
/*  69 */     return defaultAction((DocTree)paramAuthorTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitComment(CommentTree paramCommentTree, P paramP) {
/*  73 */     return defaultAction((DocTree)paramCommentTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitDeprecated(DeprecatedTree paramDeprecatedTree, P paramP) {
/*  77 */     return defaultAction((DocTree)paramDeprecatedTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitDocComment(DocCommentTree paramDocCommentTree, P paramP) {
/*  81 */     return defaultAction((DocTree)paramDocCommentTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitDocRoot(DocRootTree paramDocRootTree, P paramP) {
/*  85 */     return defaultAction((DocTree)paramDocRootTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitEndElement(EndElementTree paramEndElementTree, P paramP) {
/*  89 */     return defaultAction((DocTree)paramEndElementTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitEntity(EntityTree paramEntityTree, P paramP) {
/*  93 */     return defaultAction((DocTree)paramEntityTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitErroneous(ErroneousTree paramErroneousTree, P paramP) {
/*  97 */     return defaultAction((DocTree)paramErroneousTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitIdentifier(IdentifierTree paramIdentifierTree, P paramP) {
/* 101 */     return defaultAction((DocTree)paramIdentifierTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitInheritDoc(InheritDocTree paramInheritDocTree, P paramP) {
/* 105 */     return defaultAction((DocTree)paramInheritDocTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitLink(LinkTree paramLinkTree, P paramP) {
/* 109 */     return defaultAction((DocTree)paramLinkTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitLiteral(LiteralTree paramLiteralTree, P paramP) {
/* 113 */     return defaultAction((DocTree)paramLiteralTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitParam(ParamTree paramParamTree, P paramP) {
/* 117 */     return defaultAction((DocTree)paramParamTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitReference(ReferenceTree paramReferenceTree, P paramP) {
/* 121 */     return defaultAction((DocTree)paramReferenceTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitReturn(ReturnTree paramReturnTree, P paramP) {
/* 125 */     return defaultAction((DocTree)paramReturnTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitSee(SeeTree paramSeeTree, P paramP) {
/* 129 */     return defaultAction((DocTree)paramSeeTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitSerial(SerialTree paramSerialTree, P paramP) {
/* 133 */     return defaultAction((DocTree)paramSerialTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitSerialData(SerialDataTree paramSerialDataTree, P paramP) {
/* 137 */     return defaultAction((DocTree)paramSerialDataTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitSerialField(SerialFieldTree paramSerialFieldTree, P paramP) {
/* 141 */     return defaultAction((DocTree)paramSerialFieldTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitSince(SinceTree paramSinceTree, P paramP) {
/* 145 */     return defaultAction((DocTree)paramSinceTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitStartElement(StartElementTree paramStartElementTree, P paramP) {
/* 149 */     return defaultAction((DocTree)paramStartElementTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitText(TextTree paramTextTree, P paramP) {
/* 153 */     return defaultAction((DocTree)paramTextTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitThrows(ThrowsTree paramThrowsTree, P paramP) {
/* 157 */     return defaultAction((DocTree)paramThrowsTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitUnknownBlockTag(UnknownBlockTagTree paramUnknownBlockTagTree, P paramP) {
/* 161 */     return defaultAction((DocTree)paramUnknownBlockTagTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitUnknownInlineTag(UnknownInlineTagTree paramUnknownInlineTagTree, P paramP) {
/* 165 */     return defaultAction((DocTree)paramUnknownInlineTagTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitValue(ValueTree paramValueTree, P paramP) {
/* 169 */     return defaultAction((DocTree)paramValueTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitVersion(VersionTree paramVersionTree, P paramP) {
/* 173 */     return defaultAction((DocTree)paramVersionTree, paramP);
/*     */   }
/*     */   
/*     */   public R visitOther(DocTree paramDocTree, P paramP) {
/* 177 */     return defaultAction(paramDocTree, paramP);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\sourc\\util\SimpleDocTreeVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */