/*     */ package com.sun.tools.javac.tree;
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
/*     */ import com.sun.tools.javac.util.Convert;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
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
/*     */ public class DocPretty
/*     */   implements DocTreeVisitor<Void, Void>
/*     */ {
/*     */   final Writer out;
/*  54 */   int lmargin = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   final String lineSep;
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(DocTree paramDocTree) throws IOException {
/*     */     try {
/*  64 */       if (paramDocTree == null) {
/*  65 */         print("/*missing*/");
/*     */       } else {
/*  67 */         paramDocTree.accept(this, null);
/*     */       } 
/*  69 */     } catch (UncheckedIOException uncheckedIOException) {
/*  70 */       throw new IOException(uncheckedIOException.getMessage(), uncheckedIOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void print(Object paramObject) throws IOException {
/*  78 */     this.out.write(Convert.escapeUnicode(paramObject.toString()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(List<? extends DocTree> paramList) throws IOException {
/*  85 */     for (DocTree docTree : paramList) {
/*  86 */       print(docTree);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void print(List<? extends DocTree> paramList, String paramString) throws IOException {
/*  94 */     if (paramList.isEmpty())
/*     */       return; 
/*  96 */     boolean bool = true;
/*  97 */     for (DocTree docTree : paramList) {
/*  98 */       if (!bool)
/*  99 */         print(paramString); 
/* 100 */       print(docTree);
/* 101 */       bool = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void println() throws IOException {
/* 108 */     this.out.write(this.lineSep);
/*     */   }
/*     */   
/*     */   protected void printTagName(DocTree paramDocTree) throws IOException {
/* 112 */     this.out.write("@");
/* 113 */     this.out.write((paramDocTree.getKind()).tagName);
/*     */   }
/*     */   public DocPretty(Writer paramWriter) {
/* 116 */     this.lineSep = System.getProperty("line.separator");
/*     */     this.out = paramWriter;
/*     */   }
/*     */   
/*     */   private static class UncheckedIOException
/*     */     extends Error
/*     */   {
/*     */     static final long serialVersionUID = -4032692679158424751L;
/*     */     
/*     */     UncheckedIOException(IOException param1IOException) {
/* 126 */       super(param1IOException.getMessage(), param1IOException);
/*     */     }
/*     */   }
/*     */   
/*     */   public Void visitAttribute(AttributeTree paramAttributeTree, Void paramVoid) {
/*     */     try {
/*     */       String str;
/* 133 */       print(paramAttributeTree.getName());
/*     */       
/* 135 */       switch (paramAttributeTree.getValueKind()) {
/*     */         case EMPTY:
/* 137 */           str = null;
/*     */           break;
/*     */         case UNQUOTED:
/* 140 */           str = "";
/*     */           break;
/*     */         case SINGLE:
/* 143 */           str = "'";
/*     */           break;
/*     */         case DOUBLE:
/* 146 */           str = "\"";
/*     */           break;
/*     */         default:
/* 149 */           throw new AssertionError();
/*     */       } 
/* 151 */       if (str != null) {
/* 152 */         print("=" + str);
/* 153 */         print(paramAttributeTree.getValue());
/* 154 */         print(str);
/*     */       } 
/* 156 */     } catch (IOException iOException) {
/* 157 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 159 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitAuthor(AuthorTree paramAuthorTree, Void paramVoid) {
/*     */     try {
/* 164 */       printTagName((DocTree)paramAuthorTree);
/* 165 */       print(" ");
/* 166 */       print(paramAuthorTree.getName());
/* 167 */     } catch (IOException iOException) {
/* 168 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 170 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitComment(CommentTree paramCommentTree, Void paramVoid) {
/*     */     try {
/* 175 */       print(paramCommentTree.getBody());
/* 176 */     } catch (IOException iOException) {
/* 177 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 179 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitDeprecated(DeprecatedTree paramDeprecatedTree, Void paramVoid) {
/*     */     try {
/* 184 */       printTagName((DocTree)paramDeprecatedTree);
/* 185 */       if (!paramDeprecatedTree.getBody().isEmpty()) {
/* 186 */         print(" ");
/* 187 */         print(paramDeprecatedTree.getBody());
/*     */       } 
/* 189 */     } catch (IOException iOException) {
/* 190 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 192 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitDocComment(DocCommentTree paramDocCommentTree, Void paramVoid) {
/*     */     try {
/* 197 */       List<? extends DocTree> list1 = paramDocCommentTree.getFirstSentence();
/* 198 */       List<? extends DocTree> list2 = paramDocCommentTree.getBody();
/* 199 */       List<? extends DocTree> list3 = paramDocCommentTree.getBlockTags();
/* 200 */       print(list1);
/* 201 */       if (!list1.isEmpty() && !list2.isEmpty())
/* 202 */         print(" "); 
/* 203 */       print(list2);
/* 204 */       if ((!list1.isEmpty() || !list2.isEmpty()) && !list3.isEmpty())
/* 205 */         print("\n"); 
/* 206 */       print(list3, "\n");
/* 207 */     } catch (IOException iOException) {
/* 208 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 210 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitDocRoot(DocRootTree paramDocRootTree, Void paramVoid) {
/*     */     try {
/* 215 */       print("{");
/* 216 */       printTagName((DocTree)paramDocRootTree);
/* 217 */       print("}");
/* 218 */     } catch (IOException iOException) {
/* 219 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 221 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitEndElement(EndElementTree paramEndElementTree, Void paramVoid) {
/*     */     try {
/* 226 */       print("</");
/* 227 */       print(paramEndElementTree.getName());
/* 228 */       print(">");
/* 229 */     } catch (IOException iOException) {
/* 230 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 232 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitEntity(EntityTree paramEntityTree, Void paramVoid) {
/*     */     try {
/* 237 */       print("&");
/* 238 */       print(paramEntityTree.getName());
/* 239 */       print(";");
/* 240 */     } catch (IOException iOException) {
/* 241 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 243 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitErroneous(ErroneousTree paramErroneousTree, Void paramVoid) {
/*     */     try {
/* 248 */       print(paramErroneousTree.getBody());
/* 249 */     } catch (IOException iOException) {
/* 250 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 252 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitIdentifier(IdentifierTree paramIdentifierTree, Void paramVoid) {
/*     */     try {
/* 257 */       print(paramIdentifierTree.getName());
/* 258 */     } catch (IOException iOException) {
/* 259 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 261 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitInheritDoc(InheritDocTree paramInheritDocTree, Void paramVoid) {
/*     */     try {
/* 266 */       print("{");
/* 267 */       printTagName((DocTree)paramInheritDocTree);
/* 268 */       print("}");
/* 269 */     } catch (IOException iOException) {
/* 270 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 272 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitLink(LinkTree paramLinkTree, Void paramVoid) {
/*     */     try {
/* 277 */       print("{");
/* 278 */       printTagName((DocTree)paramLinkTree);
/* 279 */       print(" ");
/* 280 */       print((DocTree)paramLinkTree.getReference());
/* 281 */       if (!paramLinkTree.getLabel().isEmpty()) {
/* 282 */         print(" ");
/* 283 */         print(paramLinkTree.getLabel());
/*     */       } 
/* 285 */       print("}");
/* 286 */     } catch (IOException iOException) {
/* 287 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 289 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitLiteral(LiteralTree paramLiteralTree, Void paramVoid) {
/*     */     try {
/* 294 */       print("{");
/* 295 */       printTagName((DocTree)paramLiteralTree);
/* 296 */       print(" ");
/* 297 */       print((DocTree)paramLiteralTree.getBody());
/* 298 */       print("}");
/* 299 */     } catch (IOException iOException) {
/* 300 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 302 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitParam(ParamTree paramParamTree, Void paramVoid) {
/*     */     try {
/* 307 */       printTagName((DocTree)paramParamTree);
/* 308 */       print(" ");
/* 309 */       if (paramParamTree.isTypeParameter()) print("<"); 
/* 310 */       print((DocTree)paramParamTree.getName());
/* 311 */       if (paramParamTree.isTypeParameter()) print(">"); 
/* 312 */       if (!paramParamTree.getDescription().isEmpty()) {
/* 313 */         print(" ");
/* 314 */         print(paramParamTree.getDescription());
/*     */       } 
/* 316 */     } catch (IOException iOException) {
/* 317 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 319 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitReference(ReferenceTree paramReferenceTree, Void paramVoid) {
/*     */     try {
/* 324 */       print(paramReferenceTree.getSignature());
/* 325 */     } catch (IOException iOException) {
/* 326 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 328 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitReturn(ReturnTree paramReturnTree, Void paramVoid) {
/*     */     try {
/* 333 */       printTagName((DocTree)paramReturnTree);
/* 334 */       print(" ");
/* 335 */       print(paramReturnTree.getDescription());
/* 336 */     } catch (IOException iOException) {
/* 337 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 339 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitSee(SeeTree paramSeeTree, Void paramVoid) {
/*     */     try {
/* 344 */       printTagName((DocTree)paramSeeTree);
/* 345 */       boolean bool1 = true;
/* 346 */       boolean bool2 = true;
/* 347 */       for (DocTree docTree : paramSeeTree.getReference()) {
/* 348 */         if (bool2) print(" "); 
/* 349 */         bool2 = (bool1 && docTree instanceof ReferenceTree) ? true : false;
/* 350 */         bool1 = false;
/* 351 */         print(docTree);
/*     */       } 
/* 353 */     } catch (IOException iOException) {
/* 354 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 356 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitSerial(SerialTree paramSerialTree, Void paramVoid) {
/*     */     try {
/* 361 */       printTagName((DocTree)paramSerialTree);
/* 362 */       if (!paramSerialTree.getDescription().isEmpty()) {
/* 363 */         print(" ");
/* 364 */         print(paramSerialTree.getDescription());
/*     */       } 
/* 366 */     } catch (IOException iOException) {
/* 367 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 369 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitSerialData(SerialDataTree paramSerialDataTree, Void paramVoid) {
/*     */     try {
/* 374 */       printTagName((DocTree)paramSerialDataTree);
/* 375 */       if (!paramSerialDataTree.getDescription().isEmpty()) {
/* 376 */         print(" ");
/* 377 */         print(paramSerialDataTree.getDescription());
/*     */       } 
/* 379 */     } catch (IOException iOException) {
/* 380 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 382 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitSerialField(SerialFieldTree paramSerialFieldTree, Void paramVoid) {
/*     */     try {
/* 387 */       printTagName((DocTree)paramSerialFieldTree);
/* 388 */       print(" ");
/* 389 */       print((DocTree)paramSerialFieldTree.getName());
/* 390 */       print(" ");
/* 391 */       print((DocTree)paramSerialFieldTree.getType());
/* 392 */       if (!paramSerialFieldTree.getDescription().isEmpty()) {
/* 393 */         print(" ");
/* 394 */         print(paramSerialFieldTree.getDescription());
/*     */       } 
/* 396 */     } catch (IOException iOException) {
/* 397 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 399 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitSince(SinceTree paramSinceTree, Void paramVoid) {
/*     */     try {
/* 404 */       printTagName((DocTree)paramSinceTree);
/* 405 */       print(" ");
/* 406 */       print(paramSinceTree.getBody());
/* 407 */     } catch (IOException iOException) {
/* 408 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 410 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitStartElement(StartElementTree paramStartElementTree, Void paramVoid) {
/*     */     try {
/* 415 */       print("<");
/* 416 */       print(paramStartElementTree.getName());
/* 417 */       List<? extends DocTree> list = paramStartElementTree.getAttributes();
/* 418 */       if (!list.isEmpty()) {
/* 419 */         print(" ");
/* 420 */         print(list);
/* 421 */         DocTree docTree = paramStartElementTree.getAttributes().get(list.size() - 1);
/* 422 */         if (paramStartElementTree.isSelfClosing() && docTree instanceof AttributeTree && ((AttributeTree)docTree)
/* 423 */           .getValueKind() == AttributeTree.ValueKind.UNQUOTED)
/* 424 */           print(" "); 
/*     */       } 
/* 426 */       if (paramStartElementTree.isSelfClosing())
/* 427 */         print("/"); 
/* 428 */       print(">");
/* 429 */     } catch (IOException iOException) {
/* 430 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 432 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitText(TextTree paramTextTree, Void paramVoid) {
/*     */     try {
/* 437 */       print(paramTextTree.getBody());
/* 438 */     } catch (IOException iOException) {
/* 439 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 441 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitThrows(ThrowsTree paramThrowsTree, Void paramVoid) {
/*     */     try {
/* 446 */       printTagName((DocTree)paramThrowsTree);
/* 447 */       print(" ");
/* 448 */       print((DocTree)paramThrowsTree.getExceptionName());
/* 449 */       if (!paramThrowsTree.getDescription().isEmpty()) {
/* 450 */         print(" ");
/* 451 */         print(paramThrowsTree.getDescription());
/*     */       } 
/* 453 */     } catch (IOException iOException) {
/* 454 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 456 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitUnknownBlockTag(UnknownBlockTagTree paramUnknownBlockTagTree, Void paramVoid) {
/*     */     try {
/* 461 */       print("@");
/* 462 */       print(paramUnknownBlockTagTree.getTagName());
/* 463 */       print(" ");
/* 464 */       print(paramUnknownBlockTagTree.getContent());
/* 465 */     } catch (IOException iOException) {
/* 466 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 468 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitUnknownInlineTag(UnknownInlineTagTree paramUnknownInlineTagTree, Void paramVoid) {
/*     */     try {
/* 473 */       print("{");
/* 474 */       print("@");
/* 475 */       print(paramUnknownInlineTagTree.getTagName());
/* 476 */       print(" ");
/* 477 */       print(paramUnknownInlineTagTree.getContent());
/* 478 */       print("}");
/* 479 */     } catch (IOException iOException) {
/* 480 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 482 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitValue(ValueTree paramValueTree, Void paramVoid) {
/*     */     try {
/* 487 */       print("{");
/* 488 */       printTagName((DocTree)paramValueTree);
/* 489 */       if (paramValueTree.getReference() != null) {
/* 490 */         print(" ");
/* 491 */         print((DocTree)paramValueTree.getReference());
/*     */       } 
/* 493 */       print("}");
/* 494 */     } catch (IOException iOException) {
/* 495 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 497 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitVersion(VersionTree paramVersionTree, Void paramVoid) {
/*     */     try {
/* 502 */       printTagName((DocTree)paramVersionTree);
/* 503 */       print(" ");
/* 504 */       print(paramVersionTree.getBody());
/* 505 */     } catch (IOException iOException) {
/* 506 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 508 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitOther(DocTree paramDocTree, Void paramVoid) {
/*     */     try {
/* 513 */       print("(UNKNOWN: " + paramDocTree + ")");
/* 514 */       println();
/* 515 */     } catch (IOException iOException) {
/* 516 */       throw new UncheckedIOException(iOException);
/*     */     } 
/* 518 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\tree\DocPretty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */