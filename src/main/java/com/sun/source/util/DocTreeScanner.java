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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Exported
/*     */ public class DocTreeScanner<R, P>
/*     */   implements DocTreeVisitor<R, P>
/*     */ {
/*     */   public R scan(DocTree paramDocTree, P paramP) {
/*  77 */     return (paramDocTree == null) ? null : (R)paramDocTree.accept(this, paramP);
/*     */   }
/*     */   
/*     */   private R scanAndReduce(DocTree paramDocTree, P paramP, R paramR) {
/*  81 */     return reduce(scan(paramDocTree, paramP), paramR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public R scan(Iterable<? extends DocTree> paramIterable, P paramP) {
/*  88 */     R r = null;
/*  89 */     if (paramIterable != null) {
/*  90 */       boolean bool = true;
/*  91 */       for (DocTree docTree : paramIterable) {
/*  92 */         r = bool ? scan(docTree, paramP) : scanAndReduce(docTree, paramP, r);
/*  93 */         bool = false;
/*     */       } 
/*     */     } 
/*  96 */     return r;
/*     */   }
/*     */   
/*     */   private R scanAndReduce(Iterable<? extends DocTree> paramIterable, P paramP, R paramR) {
/* 100 */     return reduce(scan(paramIterable, paramP), paramR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public R reduce(R paramR1, R paramR2) {
/* 109 */     return paramR1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public R visitAttribute(AttributeTree paramAttributeTree, P paramP) {
/* 119 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitAuthor(AuthorTree paramAuthorTree, P paramP) {
/* 124 */     return scan(paramAuthorTree.getName(), paramP);
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitComment(CommentTree paramCommentTree, P paramP) {
/* 129 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitDeprecated(DeprecatedTree paramDeprecatedTree, P paramP) {
/* 134 */     return scan(paramDeprecatedTree.getBody(), paramP);
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitDocComment(DocCommentTree paramDocCommentTree, P paramP) {
/* 139 */     R r = scan(paramDocCommentTree.getFirstSentence(), paramP);
/* 140 */     r = scanAndReduce(paramDocCommentTree.getBody(), paramP, r);
/* 141 */     r = scanAndReduce(paramDocCommentTree.getBlockTags(), paramP, r);
/* 142 */     return r;
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitDocRoot(DocRootTree paramDocRootTree, P paramP) {
/* 147 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitEndElement(EndElementTree paramEndElementTree, P paramP) {
/* 152 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitEntity(EntityTree paramEntityTree, P paramP) {
/* 157 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitErroneous(ErroneousTree paramErroneousTree, P paramP) {
/* 162 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitIdentifier(IdentifierTree paramIdentifierTree, P paramP) {
/* 167 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitInheritDoc(InheritDocTree paramInheritDocTree, P paramP) {
/* 172 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitLink(LinkTree paramLinkTree, P paramP) {
/* 177 */     R r = scan((DocTree)paramLinkTree.getReference(), paramP);
/* 178 */     r = scanAndReduce(paramLinkTree.getLabel(), paramP, r);
/* 179 */     return r;
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitLiteral(LiteralTree paramLiteralTree, P paramP) {
/* 184 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitParam(ParamTree paramParamTree, P paramP) {
/* 189 */     R r = scan((DocTree)paramParamTree.getName(), paramP);
/* 190 */     r = scanAndReduce(paramParamTree.getDescription(), paramP, r);
/* 191 */     return r;
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitReference(ReferenceTree paramReferenceTree, P paramP) {
/* 196 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitReturn(ReturnTree paramReturnTree, P paramP) {
/* 201 */     return scan(paramReturnTree.getDescription(), paramP);
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitSee(SeeTree paramSeeTree, P paramP) {
/* 206 */     return scan(paramSeeTree.getReference(), paramP);
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitSerial(SerialTree paramSerialTree, P paramP) {
/* 211 */     return scan(paramSerialTree.getDescription(), paramP);
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitSerialData(SerialDataTree paramSerialDataTree, P paramP) {
/* 216 */     return scan(paramSerialDataTree.getDescription(), paramP);
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitSerialField(SerialFieldTree paramSerialFieldTree, P paramP) {
/* 221 */     R r = scan((DocTree)paramSerialFieldTree.getName(), paramP);
/* 222 */     r = scanAndReduce((DocTree)paramSerialFieldTree.getType(), paramP, r);
/* 223 */     r = scanAndReduce(paramSerialFieldTree.getDescription(), paramP, r);
/* 224 */     return r;
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitSince(SinceTree paramSinceTree, P paramP) {
/* 229 */     return scan(paramSinceTree.getBody(), paramP);
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitStartElement(StartElementTree paramStartElementTree, P paramP) {
/* 234 */     return scan(paramStartElementTree.getAttributes(), paramP);
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitText(TextTree paramTextTree, P paramP) {
/* 239 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitThrows(ThrowsTree paramThrowsTree, P paramP) {
/* 244 */     R r = scan((DocTree)paramThrowsTree.getExceptionName(), paramP);
/* 245 */     r = scanAndReduce(paramThrowsTree.getDescription(), paramP, r);
/* 246 */     return r;
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitUnknownBlockTag(UnknownBlockTagTree paramUnknownBlockTagTree, P paramP) {
/* 251 */     return scan(paramUnknownBlockTagTree.getContent(), paramP);
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitUnknownInlineTag(UnknownInlineTagTree paramUnknownInlineTagTree, P paramP) {
/* 256 */     return scan(paramUnknownInlineTagTree.getContent(), paramP);
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitValue(ValueTree paramValueTree, P paramP) {
/* 261 */     return scan((DocTree)paramValueTree.getReference(), paramP);
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitVersion(VersionTree paramVersionTree, P paramP) {
/* 266 */     return scan(paramVersionTree.getBody(), paramP);
/*     */   }
/*     */ 
/*     */   
/*     */   public R visitOther(DocTree paramDocTree, P paramP) {
/* 271 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\sourc\\util\DocTreeScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */