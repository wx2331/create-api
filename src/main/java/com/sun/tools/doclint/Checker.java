/*     */ package com.sun.tools.doclint;
/*     */
/*     */ import com.sun.source.doctree.AttributeTree;
/*     */ import com.sun.source.doctree.AuthorTree;
/*     */ import com.sun.source.doctree.DocCommentTree;
/*     */ import com.sun.source.doctree.DocRootTree;
/*     */ import com.sun.source.doctree.DocTree;
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
/*     */ import com.sun.source.doctree.SerialDataTree;
/*     */ import com.sun.source.doctree.SerialFieldTree;
/*     */ import com.sun.source.doctree.SinceTree;
/*     */ import com.sun.source.doctree.StartElementTree;
/*     */ import com.sun.source.doctree.TextTree;
/*     */ import com.sun.source.doctree.ThrowsTree;
/*     */ import com.sun.source.doctree.UnknownBlockTagTree;
/*     */ import com.sun.source.doctree.UnknownInlineTagTree;
/*     */ import com.sun.source.doctree.ValueTree;
/*     */ import com.sun.source.doctree.VersionTree;
/*     */ import com.sun.source.util.DocTreePath;
/*     */ import com.sun.source.util.DocTreePathScanner;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.javac.tree.DocPretty;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Deque;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.Name;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.tools.JavaFileObject;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class Checker
/*     */   extends DocTreePathScanner<Void, Void>
/*     */ {
/*     */   final Env env;
/*  98 */   Set<Element> foundParams = new HashSet<>();
/*  99 */   Set<TypeMirror> foundThrows = new HashSet<>();
/* 100 */   Map<Element, Set<String>> foundAnchors = new HashMap<>(); boolean foundInheritDoc = false; boolean foundReturn = false;
/*     */   private final Deque<TagStackItem> tagStack;
/*     */   private HtmlTag currHeaderTag;
/*     */   private final int implicitHeaderLevel;
/*     */
/* 105 */   public enum Flag { TABLE_HAS_CAPTION,
/* 106 */     HAS_ELEMENT,
/* 107 */     HAS_INLINE_TAG,
/* 108 */     HAS_TEXT,
/* 109 */     REPORTED_BAD_INLINE; }
/*     */
/*     */   static class TagStackItem {
/*     */     final DocTree tree;
/*     */     final HtmlTag tag;
/*     */     final Set<HtmlTag.Attr> attrs;
/*     */     final Set<Flag> flags;
/*     */
/*     */     TagStackItem(DocTree param1DocTree, HtmlTag param1HtmlTag) {
/* 118 */       this.tree = param1DocTree;
/* 119 */       this.tag = param1HtmlTag;
/* 120 */       this.attrs = EnumSet.noneOf(HtmlTag.Attr.class);
/* 121 */       this.flags = EnumSet.noneOf(Flag.class);
/*     */     }
/*     */
/*     */     public String toString() {
/* 125 */       return String.valueOf(this.tag);
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
/*     */   Checker(Env paramEnv) {
/* 137 */     paramEnv.getClass();
/* 138 */     this.env = paramEnv;
/* 139 */     this.tagStack = new LinkedList<>();
/* 140 */     this.implicitHeaderLevel = paramEnv.implicitHeaderLevel;
/*     */   }
/*     */
/*     */   public Void scan(DocCommentTree paramDocCommentTree, TreePath paramTreePath) {
/* 144 */     this.env.setCurrent(paramTreePath, paramDocCommentTree);
/*     */
/* 146 */     boolean bool = !this.env.currOverriddenMethods.isEmpty() ? true : false;
/*     */
/* 148 */     if (paramTreePath.getLeaf() == paramTreePath.getCompilationUnit()) {
/*     */
/*     */
/*     */
/*     */
/* 153 */       JavaFileObject javaFileObject = paramTreePath.getCompilationUnit().getSourceFile();
/* 154 */       boolean bool1 = javaFileObject.isNameCompatible("package-info", JavaFileObject.Kind.SOURCE);
/* 155 */       if (paramDocCommentTree == null) {
/* 156 */         if (bool1)
/* 157 */           reportMissing("dc.missing.comment", new Object[0]);
/* 158 */         return null;
/*     */       }
/* 160 */       if (!bool1) {
/* 161 */         reportReference("dc.unexpected.comment", new Object[0]);
/*     */       }
/*     */     }
/* 164 */     else if (paramDocCommentTree == null) {
/* 165 */       if (!isSynthetic() && !bool)
/* 166 */         reportMissing("dc.missing.comment", new Object[0]);
/* 167 */       return null;
/*     */     }
/*     */
/*     */
/* 171 */     this.tagStack.clear();
/* 172 */     this.currHeaderTag = null;
/*     */
/* 174 */     this.foundParams.clear();
/* 175 */     this.foundThrows.clear();
/* 176 */     this.foundInheritDoc = false;
/* 177 */     this.foundReturn = false;
/*     */
/* 179 */     scan(new DocTreePath(paramTreePath, paramDocCommentTree), (Object)null);
/*     */
/* 181 */     if (!bool) {
/* 182 */       ExecutableElement executableElement; switch (this.env.currElement.getKind()) {
/*     */         case NAME:
/*     */         case ID:
/* 185 */           executableElement = (ExecutableElement)this.env.currElement;
/* 186 */           checkParamsDocumented((List)executableElement.getTypeParameters());
/* 187 */           checkParamsDocumented((List)executableElement.getParameters());
/* 188 */           switch (executableElement.getReturnType().getKind()) {
/*     */             case NAME:
/*     */             case ID:
/*     */               break;
/*     */             default:
/* 193 */               if (!this.foundReturn && !this.foundInheritDoc &&
/*     */
/* 195 */                 !this.env.types.isSameType(executableElement.getReturnType(), this.env.java_lang_Void))
/* 196 */                 reportMissing("dc.missing.return", new Object[0]);
/*     */               break;
/*     */           }
/* 199 */           checkThrowsDocumented(executableElement.getThrownTypes());
/*     */           break;
/*     */       }
/*     */
/*     */     }
/* 204 */     return null;
/*     */   }
/*     */
/*     */   private void reportMissing(String paramString, Object... paramVarArgs) {
/* 208 */     this.env.messages.report(Messages.Group.MISSING, Diagnostic.Kind.WARNING, this.env.currPath.getLeaf(), paramString, paramVarArgs);
/*     */   }
/*     */
/*     */   private void reportReference(String paramString, Object... paramVarArgs) {
/* 212 */     this.env.messages.report(Messages.Group.REFERENCE, Diagnostic.Kind.WARNING, this.env.currPath.getLeaf(), paramString, paramVarArgs);
/*     */   }
/*     */
/*     */
/*     */   public Void visitDocComment(DocCommentTree paramDocCommentTree, Void paramVoid) {
/* 217 */     super.visitDocComment(paramDocCommentTree, paramVoid);
/* 218 */     for (TagStackItem tagStackItem : this.tagStack) {
/* 219 */       warnIfEmpty(tagStackItem, (DocTree)null);
/* 220 */       if (tagStackItem.tree.getKind() == DocTree.Kind.START_ELEMENT && tagStackItem.tag.endKind == HtmlTag.EndKind.REQUIRED) {
/*     */
/* 222 */         StartElementTree startElementTree = (StartElementTree)tagStackItem.tree;
/* 223 */         this.env.messages.error(Messages.Group.HTML, (DocTree)startElementTree, "dc.tag.not.closed", new Object[] { startElementTree.getName() });
/*     */       }
/*     */     }
/* 226 */     return null;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Void visitText(TextTree paramTextTree, Void paramVoid) {
/* 234 */     if (hasNonWhitespace(paramTextTree)) {
/* 235 */       checkAllowsText((DocTree)paramTextTree);
/* 236 */       markEnclosingTag(Flag.HAS_TEXT);
/*     */     }
/* 238 */     return null;
/*     */   }
/*     */
/*     */
/*     */   public Void visitEntity(EntityTree paramEntityTree, Void paramVoid) {
/* 243 */     checkAllowsText((DocTree)paramEntityTree);
/* 244 */     markEnclosingTag(Flag.HAS_TEXT);
/* 245 */     String str = paramEntityTree.getName().toString();
/* 246 */     if (str.startsWith("#")) {
/*     */
/*     */
/* 249 */       int i = StringUtils.toLowerCase(str).startsWith("#x") ? Integer.parseInt(str.substring(2), 16) : Integer.parseInt(str.substring(1), 10);
/* 250 */       if (!Entity.isValid(i)) {
/* 251 */         this.env.messages.error(Messages.Group.HTML, (DocTree)paramEntityTree, "dc.entity.invalid", new Object[] { str });
/*     */       }
/* 253 */     } else if (!Entity.isValid(str)) {
/* 254 */       this.env.messages.error(Messages.Group.HTML, (DocTree)paramEntityTree, "dc.entity.invalid", new Object[] { str });
/*     */     }
/* 256 */     return null;
/*     */   }
/*     */
/*     */   void checkAllowsText(DocTree paramDocTree) {
/* 260 */     TagStackItem tagStackItem = this.tagStack.peek();
/* 261 */     if (tagStackItem != null && tagStackItem.tree
/* 262 */       .getKind() == DocTree.Kind.START_ELEMENT &&
/* 263 */       !tagStackItem.tag.acceptsText() &&
/* 264 */       tagStackItem.flags.add(Flag.REPORTED_BAD_INLINE)) {
/* 265 */       this.env.messages.error(Messages.Group.HTML, paramDocTree, "dc.text.not.allowed", new Object[] { ((StartElementTree)tagStackItem.tree)
/* 266 */             .getName() });
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Void visitStartElement(StartElementTree paramStartElementTree, Void paramVoid) {
/* 277 */     Name name = paramStartElementTree.getName();
/* 278 */     HtmlTag htmlTag = HtmlTag.get(name);
/* 279 */     if (htmlTag == null) {
/* 280 */       this.env.messages.error(Messages.Group.HTML, (DocTree)paramStartElementTree, "dc.tag.unknown", new Object[] { name });
/*     */     } else {
/* 282 */       boolean bool = false;
/* 283 */       for (TagStackItem tagStackItem : this.tagStack) {
/* 284 */         if (tagStackItem.tag.accepts(htmlTag)) {
/* 285 */           while (this.tagStack.peek() != tagStackItem) {
/* 286 */             warnIfEmpty(this.tagStack.peek(), (DocTree)null);
/* 287 */             this.tagStack.pop();
/*     */           }
/* 289 */           bool = true; break;
/*     */         }
/* 291 */         if (tagStackItem.tag.endKind != HtmlTag.EndKind.OPTIONAL) {
/* 292 */           bool = true;
/*     */           break;
/*     */         }
/*     */       }
/* 296 */       if (!bool && HtmlTag.BODY.accepts(htmlTag)) {
/* 297 */         while (!this.tagStack.isEmpty()) {
/* 298 */           warnIfEmpty(this.tagStack.peek(), (DocTree)null);
/* 299 */           this.tagStack.pop();
/*     */         }
/*     */       }
/*     */
/* 303 */       markEnclosingTag(Flag.HAS_ELEMENT);
/* 304 */       checkStructure(paramStartElementTree, htmlTag);
/*     */
/*     */
/* 307 */       switch (htmlTag) { case NAME: case ID: case HREF: case VALUE:
/*     */         case null:
/*     */         case null:
/* 310 */           checkHeader(paramStartElementTree, htmlTag);
/*     */           break; }
/*     */
/*     */
/* 314 */       if (htmlTag.flags.contains(HtmlTag.Flag.NO_NEST)) {
/* 315 */         for (TagStackItem tagStackItem : this.tagStack) {
/* 316 */           if (htmlTag == tagStackItem.tag) {
/* 317 */             this.env.messages.warning(Messages.Group.HTML, (DocTree)paramStartElementTree, "dc.tag.nested.not.allowed", new Object[] { name });
/*     */
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */
/* 325 */     if (paramStartElementTree.isSelfClosing()) {
/* 326 */       this.env.messages.error(Messages.Group.HTML, (DocTree)paramStartElementTree, "dc.tag.self.closing", new Object[] { name });
/*     */     }
/*     */
/*     */     try {
/* 330 */       TagStackItem tagStackItem1 = this.tagStack.peek();
/* 331 */       TagStackItem tagStackItem2 = new TagStackItem((DocTree)paramStartElementTree, htmlTag);
/* 332 */       this.tagStack.push(tagStackItem2);
/*     */
/* 334 */       super.visitStartElement(paramStartElementTree, paramVoid);
/*     */
/*     */
/* 337 */       if (htmlTag != null) {
/* 338 */         switch (htmlTag) {
/*     */           case null:
/* 340 */             if (tagStackItem1 != null && tagStackItem1.tag == HtmlTag.TABLE) {
/* 341 */               tagStackItem1.flags.add(Flag.TABLE_HAS_CAPTION);
/*     */             }
/*     */             break;
/*     */           case null:
/* 345 */             if (!tagStackItem2.attrs.contains(HtmlTag.Attr.ALT)) {
/* 346 */               this.env.messages.error(Messages.Group.ACCESSIBILITY, (DocTree)paramStartElementTree, "dc.no.alt.attr.for.image", new Object[0]);
/*     */             }
/*     */             break;
/*     */         }
/*     */       }
/* 351 */       return null;
/*     */     } finally {
/*     */
/* 354 */       if (htmlTag == null || htmlTag.endKind == HtmlTag.EndKind.NONE)
/* 355 */         this.tagStack.pop();
/*     */     }
/*     */   }
/*     */   private void checkStructure(StartElementTree paramStartElementTree, HtmlTag paramHtmlTag) {
/*     */     String str;
/* 360 */     Name name = paramStartElementTree.getName();
/* 361 */     TagStackItem tagStackItem = this.tagStack.peek();
/* 362 */     switch (paramHtmlTag.blockType) {
/*     */       case NAME:
/* 364 */         if (tagStackItem == null || tagStackItem.tag.accepts(paramHtmlTag)) {
/*     */           return;
/*     */         }
/* 367 */         switch (tagStackItem.tree.getKind()) {
/*     */           case NAME:
/* 369 */             if (tagStackItem.tag.blockType == HtmlTag.BlockType.INLINE) {
/* 370 */               Name name1 = ((StartElementTree)tagStackItem.tree).getName();
/* 371 */               this.env.messages.error(Messages.Group.HTML, (DocTree)paramStartElementTree, "dc.tag.not.allowed.inline.element", new Object[] { name, name1 });
/*     */               return;
/*     */             }
/*     */             break;
/*     */
/*     */
/*     */
/*     */           case ID:
/*     */           case HREF:
/* 380 */             str = (tagStackItem.tree.getKind()).tagName;
/* 381 */             this.env.messages.error(Messages.Group.HTML, (DocTree)paramStartElementTree, "dc.tag.not.allowed.inline.tag", new Object[] { name, str });
/*     */             return;
/*     */         }
/*     */
/*     */
/*     */         break;
/*     */
/*     */       case ID:
/* 389 */         if (tagStackItem == null || tagStackItem.tag.accepts(paramHtmlTag)) {
/*     */           return;
/*     */         }
/*     */         break;
/*     */       case HREF:
/*     */       case VALUE:
/* 395 */         if (tagStackItem != null) {
/*     */
/* 397 */           tagStackItem.flags.remove(Flag.REPORTED_BAD_INLINE);
/* 398 */           if (tagStackItem.tag.accepts(paramHtmlTag)) {
/*     */             return;
/*     */           }
/*     */         }
/*     */         break;
/*     */       case null:
/* 404 */         switch (paramHtmlTag) {
/*     */           case null:
/*     */             return;
/*     */         }
/*     */
/*     */
/*     */
/*     */
/* 412 */         this.env.messages.error(Messages.Group.HTML, (DocTree)paramStartElementTree, "dc.tag.not.allowed", new Object[] { name });
/*     */     }
/*     */
/*     */
/*     */
/* 417 */     this.env.messages.error(Messages.Group.HTML, (DocTree)paramStartElementTree, "dc.tag.not.allowed.here", new Object[] { name });
/*     */   }
/*     */
/*     */
/*     */   private void checkHeader(StartElementTree paramStartElementTree, HtmlTag paramHtmlTag) {
/* 422 */     if (getHeaderLevel(paramHtmlTag) > getHeaderLevel(this.currHeaderTag) + 1) {
/* 423 */       if (this.currHeaderTag == null) {
/* 424 */         this.env.messages.error(Messages.Group.ACCESSIBILITY, (DocTree)paramStartElementTree, "dc.tag.header.sequence.1", new Object[] { paramHtmlTag });
/*     */       } else {
/* 426 */         this.env.messages.error(Messages.Group.ACCESSIBILITY, (DocTree)paramStartElementTree, "dc.tag.header.sequence.2", new Object[] { paramHtmlTag, this.currHeaderTag });
/*     */       }
/*     */     }
/*     */
/*     */
/* 431 */     this.currHeaderTag = paramHtmlTag;
/*     */   }
/*     */
/*     */   private int getHeaderLevel(HtmlTag paramHtmlTag) {
/* 435 */     if (paramHtmlTag == null)
/* 436 */       return this.implicitHeaderLevel;
/* 437 */     switch (paramHtmlTag) { case NAME:
/* 438 */         return 1;
/* 439 */       case ID: return 2;
/* 440 */       case HREF: return 3;
/* 441 */       case VALUE: return 4;
/* 442 */       case null: return 5;
/* 443 */       case null: return 6; }
/* 444 */      throw new IllegalArgumentException();
/*     */   }
/*     */
/*     */
/*     */
/*     */   public Void visitEndElement(EndElementTree paramEndElementTree, Void paramVoid) {
/* 450 */     Name name = paramEndElementTree.getName();
/* 451 */     HtmlTag htmlTag = HtmlTag.get(name);
/* 452 */     if (htmlTag == null) {
/* 453 */       this.env.messages.error(Messages.Group.HTML, (DocTree)paramEndElementTree, "dc.tag.unknown", new Object[] { name });
/* 454 */     } else if (htmlTag.endKind == HtmlTag.EndKind.NONE) {
/* 455 */       this.env.messages.error(Messages.Group.HTML, (DocTree)paramEndElementTree, "dc.tag.end.not.permitted", new Object[] { name });
/*     */     } else {
/* 457 */       boolean bool = false;
/* 458 */       while (!this.tagStack.isEmpty()) {
/* 459 */         TagStackItem tagStackItem = this.tagStack.peek();
/* 460 */         if (htmlTag == tagStackItem.tag) {
/* 461 */           switch (htmlTag) {
/*     */             case null:
/* 463 */               if (!tagStackItem.attrs.contains(HtmlTag.Attr.SUMMARY) &&
/* 464 */                 !tagStackItem.flags.contains(Flag.TABLE_HAS_CAPTION)) {
/* 465 */                 this.env.messages.error(Messages.Group.ACCESSIBILITY, (DocTree)paramEndElementTree, "dc.no.summary.or.caption.for.table", new Object[0]);
/*     */               }
/*     */               break;
/*     */           }
/* 469 */           warnIfEmpty(tagStackItem, (DocTree)paramEndElementTree);
/* 470 */           this.tagStack.pop();
/* 471 */           bool = true; break;
/*     */         }
/* 473 */         if (tagStackItem.tag == null || tagStackItem.tag.endKind != HtmlTag.EndKind.REQUIRED) {
/* 474 */           this.tagStack.pop(); continue;
/*     */         }
/* 476 */         boolean bool1 = false;
/* 477 */         for (TagStackItem tagStackItem1 : this.tagStack) {
/* 478 */           if (tagStackItem1.tag == htmlTag) {
/* 479 */             bool1 = true;
/*     */             break;
/*     */           }
/*     */         }
/* 483 */         if (bool1 && tagStackItem.tree.getKind() == DocTree.Kind.START_ELEMENT) {
/* 484 */           this.env.messages.error(Messages.Group.HTML, tagStackItem.tree, "dc.tag.start.unmatched", new Object[] { ((StartElementTree)tagStackItem.tree)
/* 485 */                 .getName() });
/* 486 */           this.tagStack.pop(); continue;
/*     */         }
/* 488 */         this.env.messages.error(Messages.Group.HTML, (DocTree)paramEndElementTree, "dc.tag.end.unexpected", new Object[] { name });
/* 489 */         bool = true;
/*     */       }
/*     */
/*     */
/*     */
/*     */
/* 495 */       if (!bool && this.tagStack.isEmpty()) {
/* 496 */         this.env.messages.error(Messages.Group.HTML, (DocTree)paramEndElementTree, "dc.tag.end.unexpected", new Object[] { name });
/*     */       }
/*     */     }
/*     */
/* 500 */     return (Void)super.visitEndElement(paramEndElementTree, paramVoid);
/*     */   }
/*     */
/*     */   void warnIfEmpty(TagStackItem paramTagStackItem, DocTree paramDocTree) {
/* 504 */     if (paramTagStackItem.tag != null && paramTagStackItem.tree instanceof StartElementTree &&
/* 505 */       paramTagStackItem.tag.flags.contains(HtmlTag.Flag.EXPECT_CONTENT) &&
/* 506 */       !paramTagStackItem.flags.contains(Flag.HAS_TEXT) &&
/* 507 */       !paramTagStackItem.flags.contains(Flag.HAS_ELEMENT) &&
/* 508 */       !paramTagStackItem.flags.contains(Flag.HAS_INLINE_TAG)) {
/* 509 */       DocTree docTree = (paramDocTree != null) ? paramDocTree : paramTagStackItem.tree;
/* 510 */       Name name = ((StartElementTree)paramTagStackItem.tree).getName();
/* 511 */       this.env.messages.warning(Messages.Group.HTML, docTree, "dc.tag.empty", new Object[] { name });
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Void visitAttribute(AttributeTree paramAttributeTree, Void paramVoid) {
/* 522 */     HtmlTag htmlTag = ((TagStackItem)this.tagStack.peek()).tag;
/* 523 */     if (htmlTag != null) {
/* 524 */       Name name = paramAttributeTree.getName();
/* 525 */       HtmlTag.Attr attr = htmlTag.getAttr(name);
/* 526 */       if (attr != null) {
/* 527 */         boolean bool = ((TagStackItem)this.tagStack.peek()).attrs.add(attr);
/* 528 */         if (!bool) {
/* 529 */           this.env.messages.error(Messages.Group.HTML, (DocTree)paramAttributeTree, "dc.attr.repeated", new Object[] { name });
/*     */         }
/*     */       }
/*     */
/*     */
/* 534 */       if (!name.toString().startsWith("on")) {
/* 535 */         HtmlTag.AttrKind attrKind = htmlTag.getAttrKind(name);
/* 536 */         switch (attrKind) {
/*     */
/*     */
/*     */
/*     */           case ID:
/* 541 */             this.env.messages.error(Messages.Group.HTML, (DocTree)paramAttributeTree, "dc.attr.unknown", new Object[] { name });
/*     */             break;
/*     */
/*     */           case HREF:
/* 545 */             this.env.messages.warning(Messages.Group.ACCESSIBILITY, (DocTree)paramAttributeTree, "dc.attr.obsolete", new Object[] { name });
/*     */             break;
/*     */
/*     */           case VALUE:
/* 549 */             this.env.messages.warning(Messages.Group.ACCESSIBILITY, (DocTree)paramAttributeTree, "dc.attr.obsolete.use.css", new Object[] { name });
/*     */             break;
/*     */         }
/*     */
/*     */       }
/* 554 */       if (attr != null) {
/* 555 */         String str; switch (attr) {
/*     */           case NAME:
/* 557 */             if (htmlTag != HtmlTag.A) {
/*     */               break;
/*     */             }
/*     */
/*     */           case ID:
/* 562 */             str = getAttrValue(paramAttributeTree);
/* 563 */             if (str == null) {
/* 564 */               this.env.messages.error(Messages.Group.HTML, (DocTree)paramAttributeTree, "dc.anchor.value.missing", new Object[0]); break;
/*     */             }
/* 566 */             if (!validName.matcher(str).matches()) {
/* 567 */               this.env.messages.error(Messages.Group.HTML, (DocTree)paramAttributeTree, "dc.invalid.anchor", new Object[] { str });
/*     */             }
/* 569 */             if (!checkAnchor(str)) {
/* 570 */               this.env.messages.error(Messages.Group.HTML, (DocTree)paramAttributeTree, "dc.anchor.already.defined", new Object[] { str });
/*     */             }
/*     */             break;
/*     */
/*     */
/*     */           case HREF:
/* 576 */             if (htmlTag == HtmlTag.A) {
/* 577 */               String str1 = getAttrValue(paramAttributeTree);
/* 578 */               if (str1 == null || str1.isEmpty()) {
/* 579 */                 this.env.messages.error(Messages.Group.HTML, (DocTree)paramAttributeTree, "dc.attr.lacks.value", new Object[0]); break;
/*     */               }
/* 581 */               Matcher matcher = docRoot.matcher(str1);
/* 582 */               if (matcher.matches()) {
/* 583 */                 String str2 = matcher.group(2);
/* 584 */                 if (!str2.isEmpty())
/* 585 */                   checkURI(paramAttributeTree, str2);  break;
/*     */               }
/* 587 */               checkURI(paramAttributeTree, str1);
/*     */             }
/*     */             break;
/*     */
/*     */
/*     */
/*     */           case VALUE:
/* 594 */             if (htmlTag == HtmlTag.LI) {
/* 595 */               String str1 = getAttrValue(paramAttributeTree);
/* 596 */               if (str1 == null || str1.isEmpty()) {
/* 597 */                 this.env.messages.error(Messages.Group.HTML, (DocTree)paramAttributeTree, "dc.attr.lacks.value", new Object[0]); break;
/* 598 */               }  if (!validNumber.matcher(str1).matches()) {
/* 599 */                 this.env.messages.error(Messages.Group.HTML, (DocTree)paramAttributeTree, "dc.attr.not.number", new Object[0]);
/*     */               }
/*     */             }
/*     */             break;
/*     */         }
/*     */
/*     */
/*     */
/*     */       }
/*     */     }
/* 609 */     return (Void)super.visitAttribute(paramAttributeTree, paramVoid);
/*     */   }
/*     */
/*     */   private boolean checkAnchor(String paramString) {
/* 613 */     Element element = getEnclosingPackageOrClass(this.env.currElement);
/* 614 */     if (element == null)
/* 615 */       return true;
/* 616 */     Set<String> set = this.foundAnchors.get(element);
/* 617 */     if (set == null)
/* 618 */       this.foundAnchors.put(element, set = new HashSet<>());
/* 619 */     return set.add(paramString);
/*     */   }
/*     */
/*     */   private Element getEnclosingPackageOrClass(Element paramElement) {
/* 623 */     while (paramElement != null) {
/* 624 */       switch (paramElement.getKind()) {
/*     */         case HREF:
/*     */         case VALUE:
/*     */         case null:
/*     */         case null:
/* 629 */           return paramElement;
/*     */       }
/* 631 */       paramElement = paramElement.getEnclosingElement();
/*     */     }
/*     */
/* 634 */     return paramElement;
/*     */   }
/*     */
/*     */
/* 638 */   private static final Pattern validName = Pattern.compile("[A-Za-z][A-Za-z0-9-_:.]*");
/*     */
/* 640 */   private static final Pattern validNumber = Pattern.compile("-?[0-9]+");
/*     */
/*     */
/* 643 */   private static final Pattern docRoot = Pattern.compile("(?i)(\\{@docRoot *\\}/?)?(.*)");
/*     */
/*     */   private String getAttrValue(AttributeTree paramAttributeTree) {
/* 646 */     if (paramAttributeTree.getValue() == null) {
/* 647 */       return null;
/*     */     }
/* 649 */     StringWriter stringWriter = new StringWriter();
/*     */     try {
/* 651 */       (new DocPretty(stringWriter)).print(paramAttributeTree.getValue());
/* 652 */     } catch (IOException iOException) {}
/*     */
/*     */
/*     */
/* 656 */     return stringWriter.toString();
/*     */   }
/*     */
/*     */
/*     */   private void checkURI(AttributeTree paramAttributeTree, String paramString) {
/* 661 */     if (paramString.startsWith("javascript:"))
/*     */       return;
/*     */     try {
/* 664 */       URI uRI = new URI(paramString);
/* 665 */     } catch (URISyntaxException uRISyntaxException) {
/* 666 */       this.env.messages.error(Messages.Group.HTML, (DocTree)paramAttributeTree, "dc.invalid.uri", new Object[] { paramString });
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Void visitAuthor(AuthorTree paramAuthorTree, Void paramVoid) {
/* 675 */     warnIfEmpty((DocTree)paramAuthorTree, paramAuthorTree.getName());
/* 676 */     return (Void)super.visitAuthor(paramAuthorTree, paramVoid);
/*     */   }
/*     */
/*     */
/*     */   public Void visitDocRoot(DocRootTree paramDocRootTree, Void paramVoid) {
/* 681 */     markEnclosingTag(Flag.HAS_INLINE_TAG);
/* 682 */     return (Void)super.visitDocRoot(paramDocRootTree, paramVoid);
/*     */   }
/*     */
/*     */
/*     */   public Void visitInheritDoc(InheritDocTree paramInheritDocTree, Void paramVoid) {
/* 687 */     markEnclosingTag(Flag.HAS_INLINE_TAG);
/*     */
/* 689 */     this.foundInheritDoc = true;
/* 690 */     return (Void)super.visitInheritDoc(paramInheritDocTree, paramVoid);
/*     */   }
/*     */
/*     */
/*     */   public Void visitLink(LinkTree paramLinkTree, Void paramVoid) {
/* 695 */     markEnclosingTag(Flag.HAS_INLINE_TAG);
/*     */
/* 697 */     HtmlTag htmlTag = (paramLinkTree.getKind() == DocTree.Kind.LINK) ? HtmlTag.CODE : HtmlTag.SPAN;
/*     */
/* 699 */     this.tagStack.push(new TagStackItem((DocTree)paramLinkTree, htmlTag));
/*     */     try {
/* 701 */       return (Void)super.visitLink(paramLinkTree, paramVoid);
/*     */     } finally {
/* 703 */       this.tagStack.pop();
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public Void visitLiteral(LiteralTree paramLiteralTree, Void paramVoid) {
/* 709 */     markEnclosingTag(Flag.HAS_INLINE_TAG);
/* 710 */     if (paramLiteralTree.getKind() == DocTree.Kind.CODE) {
/* 711 */       for (TagStackItem tagStackItem : this.tagStack) {
/* 712 */         if (tagStackItem.tag == HtmlTag.CODE) {
/* 713 */           this.env.messages.warning(Messages.Group.HTML, (DocTree)paramLiteralTree, "dc.tag.code.within.code", new Object[0]);
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 718 */     return (Void)super.visitLiteral(paramLiteralTree, paramVoid);
/*     */   }
/*     */
/*     */
/*     */
/*     */   public Void visitParam(ParamTree paramParamTree, Void paramVoid) {
/* 724 */     boolean bool = paramParamTree.isTypeParameter();
/* 725 */     IdentifierTree identifierTree = paramParamTree.getName();
/* 726 */     Element element = (identifierTree != null) ? this.env.trees.getElement(new DocTreePath(getCurrentPath(), (DocTree)identifierTree)) : null;
/*     */
/* 728 */     if (element == null)
/* 729 */     { switch (this.env.currElement.getKind()) { case HREF:
/*     */         case null:
/* 731 */           if (!bool)
/* 732 */           { this.env.messages.error(Messages.Group.REFERENCE, (DocTree)paramParamTree, "dc.invalid.param", new Object[0]);
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 749 */             warnIfEmpty((DocTree)paramParamTree, paramParamTree.getDescription());
/* 750 */             return (Void)super.visitParam(paramParamTree, paramVoid); } case NAME: case ID: this.env.messages.error(Messages.Group.REFERENCE, (DocTree)identifierTree, "dc.param.name.not.found", new Object[0]); warnIfEmpty((DocTree)paramParamTree, paramParamTree.getDescription()); return (Void)super.visitParam(paramParamTree, paramVoid); }  this.env.messages.error(Messages.Group.REFERENCE, (DocTree)paramParamTree, "dc.invalid.param", new Object[0]); } else { this.foundParams.add(element); }  warnIfEmpty((DocTree)paramParamTree, paramParamTree.getDescription()); return (Void)super.visitParam(paramParamTree, paramVoid);
/*     */   }
/*     */
/*     */   private void checkParamsDocumented(List<? extends Element> paramList) {
/* 754 */     if (this.foundInheritDoc) {
/*     */       return;
/*     */     }
/* 757 */     for (Element element : paramList) {
/* 758 */       if (!this.foundParams.contains(element)) {
/*     */
/*     */
/* 761 */         Object object = (element.getKind() == ElementKind.TYPE_PARAMETER) ? ("<" + element.getSimpleName() + ">") : element.getSimpleName();
/* 762 */         reportMissing("dc.missing.param", new Object[] { object });
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public Void visitReference(ReferenceTree paramReferenceTree, Void paramVoid) {
/* 769 */     String str = paramReferenceTree.getSignature();
/* 770 */     if (str.contains("<") || str.contains(">")) {
/* 771 */       this.env.messages.error(Messages.Group.REFERENCE, (DocTree)paramReferenceTree, "dc.type.arg.not.allowed", new Object[0]);
/*     */     }
/* 773 */     Element element = this.env.trees.getElement(getCurrentPath());
/* 774 */     if (element == null)
/* 775 */       this.env.messages.error(Messages.Group.REFERENCE, (DocTree)paramReferenceTree, "dc.ref.not.found", new Object[0]);
/* 776 */     return (Void)super.visitReference(paramReferenceTree, paramVoid);
/*     */   }
/*     */
/*     */
/*     */   public Void visitReturn(ReturnTree paramReturnTree, Void paramVoid) {
/* 781 */     Element element = this.env.trees.getElement(this.env.currPath);
/* 782 */     if (element.getKind() != ElementKind.METHOD || ((ExecutableElement)element)
/* 783 */       .getReturnType().getKind() == TypeKind.VOID)
/* 784 */       this.env.messages.error(Messages.Group.REFERENCE, (DocTree)paramReturnTree, "dc.invalid.return", new Object[0]);
/* 785 */     this.foundReturn = true;
/* 786 */     warnIfEmpty((DocTree)paramReturnTree, paramReturnTree.getDescription());
/* 787 */     return (Void)super.visitReturn(paramReturnTree, paramVoid);
/*     */   }
/*     */
/*     */
/*     */   public Void visitSerialData(SerialDataTree paramSerialDataTree, Void paramVoid) {
/* 792 */     warnIfEmpty((DocTree)paramSerialDataTree, paramSerialDataTree.getDescription());
/* 793 */     return (Void)super.visitSerialData(paramSerialDataTree, paramVoid);
/*     */   }
/*     */
/*     */
/*     */   public Void visitSerialField(SerialFieldTree paramSerialFieldTree, Void paramVoid) {
/* 798 */     warnIfEmpty((DocTree)paramSerialFieldTree, paramSerialFieldTree.getDescription());
/* 799 */     return (Void)super.visitSerialField(paramSerialFieldTree, paramVoid);
/*     */   }
/*     */
/*     */
/*     */   public Void visitSince(SinceTree paramSinceTree, Void paramVoid) {
/* 804 */     warnIfEmpty((DocTree)paramSinceTree, paramSinceTree.getBody());
/* 805 */     return (Void)super.visitSince(paramSinceTree, paramVoid);
/*     */   }
/*     */
/*     */
/*     */   public Void visitThrows(ThrowsTree paramThrowsTree, Void paramVoid) {
/* 810 */     ReferenceTree referenceTree = paramThrowsTree.getExceptionName();
/* 811 */     Element element = this.env.trees.getElement(new DocTreePath(getCurrentPath(), (DocTree)referenceTree));
/* 812 */     if (element == null)
/* 813 */     { this.env.messages.error(Messages.Group.REFERENCE, (DocTree)paramThrowsTree, "dc.ref.not.found", new Object[0]); }
/* 814 */     else if (isThrowable(element.asType()))
/* 815 */     { switch (this.env.currElement.getKind())
/*     */       { case NAME:
/*     */         case ID:
/* 818 */           if (isCheckedException(element.asType())) {
/* 819 */             ExecutableElement executableElement = (ExecutableElement)this.env.currElement;
/* 820 */             checkThrowsDeclared(referenceTree, element.asType(), executableElement.getThrownTypes());
/*     */           }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 829 */           warnIfEmpty((DocTree)paramThrowsTree, paramThrowsTree.getDescription());
/* 830 */           return (Void)scan(paramThrowsTree.getDescription(), paramVoid); }  this.env.messages.error(Messages.Group.REFERENCE, (DocTree)paramThrowsTree, "dc.invalid.throws", new Object[0]); } else { this.env.messages.error(Messages.Group.REFERENCE, (DocTree)paramThrowsTree, "dc.invalid.throws", new Object[0]); }  warnIfEmpty((DocTree)paramThrowsTree, paramThrowsTree.getDescription()); return (Void)scan(paramThrowsTree.getDescription(), paramVoid);
/*     */   }
/*     */
/*     */   private boolean isThrowable(TypeMirror paramTypeMirror) {
/* 834 */     switch (paramTypeMirror.getKind()) {
/*     */       case HREF:
/*     */       case VALUE:
/* 837 */         return this.env.types.isAssignable(paramTypeMirror, this.env.java_lang_Throwable);
/*     */     }
/* 839 */     return false;
/*     */   }
/*     */
/*     */   private void checkThrowsDeclared(ReferenceTree paramReferenceTree, TypeMirror paramTypeMirror, List<? extends TypeMirror> paramList) {
/* 843 */     boolean bool = false;
/* 844 */     for (TypeMirror typeMirror : paramList) {
/* 845 */       if (this.env.types.isAssignable(paramTypeMirror, typeMirror)) {
/* 846 */         this.foundThrows.add(typeMirror);
/* 847 */         bool = true;
/*     */       }
/*     */     }
/* 850 */     if (!bool)
/* 851 */       this.env.messages.error(Messages.Group.REFERENCE, (DocTree)paramReferenceTree, "dc.exception.not.thrown", new Object[] { paramTypeMirror });
/*     */   }
/*     */
/*     */   private void checkThrowsDocumented(List<? extends TypeMirror> paramList) {
/* 855 */     if (this.foundInheritDoc) {
/*     */       return;
/*     */     }
/* 858 */     for (TypeMirror typeMirror : paramList) {
/* 859 */       if (isCheckedException(typeMirror) && !this.foundThrows.contains(typeMirror)) {
/* 860 */         reportMissing("dc.missing.throws", new Object[] { typeMirror });
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   public Void visitUnknownBlockTag(UnknownBlockTagTree paramUnknownBlockTagTree, Void paramVoid) {
/* 866 */     checkUnknownTag((DocTree)paramUnknownBlockTagTree, paramUnknownBlockTagTree.getTagName());
/* 867 */     return (Void)super.visitUnknownBlockTag(paramUnknownBlockTagTree, paramVoid);
/*     */   }
/*     */
/*     */
/*     */   public Void visitUnknownInlineTag(UnknownInlineTagTree paramUnknownInlineTagTree, Void paramVoid) {
/* 872 */     checkUnknownTag((DocTree)paramUnknownInlineTagTree, paramUnknownInlineTagTree.getTagName());
/* 873 */     return (Void)super.visitUnknownInlineTag(paramUnknownInlineTagTree, paramVoid);
/*     */   }
/*     */
/*     */   private void checkUnknownTag(DocTree paramDocTree, String paramString) {
/* 877 */     if (this.env.customTags != null && !this.env.customTags.contains(paramString)) {
/* 878 */       this.env.messages.error(Messages.Group.SYNTAX, paramDocTree, "dc.tag.unknown", new Object[] { paramString });
/*     */     }
/*     */   }
/*     */
/*     */   public Void visitValue(ValueTree paramValueTree, Void paramVoid) {
/* 883 */     ReferenceTree referenceTree = paramValueTree.getReference();
/* 884 */     if (referenceTree == null || referenceTree.getSignature().isEmpty()) {
/* 885 */       if (!isConstant(this.env.currElement))
/* 886 */         this.env.messages.error(Messages.Group.REFERENCE, (DocTree)paramValueTree, "dc.value.not.allowed.here", new Object[0]);
/*     */     } else {
/* 888 */       Element element = this.env.trees.getElement(new DocTreePath(getCurrentPath(), (DocTree)referenceTree));
/* 889 */       if (!isConstant(element)) {
/* 890 */         this.env.messages.error(Messages.Group.REFERENCE, (DocTree)paramValueTree, "dc.value.not.a.constant", new Object[0]);
/*     */       }
/*     */     }
/* 893 */     markEnclosingTag(Flag.HAS_INLINE_TAG);
/* 894 */     return (Void)super.visitValue(paramValueTree, paramVoid);
/*     */   }
/*     */   private boolean isConstant(Element paramElement) {
/*     */     Object object;
/* 898 */     if (paramElement == null) {
/* 899 */       return false;
/*     */     }
/* 901 */     switch (paramElement.getKind()) {
/*     */       case null:
/* 903 */         object = ((VariableElement)paramElement).getConstantValue();
/* 904 */         return (object != null);
/*     */     }
/* 906 */     return false;
/*     */   }
/*     */
/*     */
/*     */
/*     */   public Void visitVersion(VersionTree paramVersionTree, Void paramVoid) {
/* 912 */     warnIfEmpty((DocTree)paramVersionTree, paramVersionTree.getBody());
/* 913 */     return (Void)super.visitVersion(paramVersionTree, paramVoid);
/*     */   }
/*     */
/*     */
/*     */   public Void visitErroneous(ErroneousTree paramErroneousTree, Void paramVoid) {
/* 918 */     this.env.messages.error(Messages.Group.SYNTAX, (DocTree)paramErroneousTree, null, new Object[] { paramErroneousTree.getDiagnostic().getMessage(null) });
/* 919 */     return null;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   private boolean isCheckedException(TypeMirror paramTypeMirror) {
/* 926 */     return (!this.env.types.isAssignable(paramTypeMirror, this.env.java_lang_Error) &&
/* 927 */       !this.env.types.isAssignable(paramTypeMirror, this.env.java_lang_RuntimeException));
/*     */   }
/*     */   private boolean isSynthetic() {
/*     */     TreePath treePath;
/* 931 */     switch (this.env.currElement.getKind()) {
/*     */
/*     */
/*     */       case ID:
/* 935 */         treePath = this.env.currPath;
/* 936 */         return (this.env.getPos(treePath) == this.env.getPos(treePath.getParentPath()));
/*     */     }
/* 938 */     return false;
/*     */   }
/*     */
/*     */   void markEnclosingTag(Flag paramFlag) {
/* 942 */     TagStackItem tagStackItem = this.tagStack.peek();
/* 943 */     if (tagStackItem != null)
/* 944 */       tagStackItem.flags.add(paramFlag);
/*     */   }
/*     */
/*     */   String toString(TreePath paramTreePath) {
/* 948 */     StringBuilder stringBuilder = new StringBuilder("TreePath[");
/* 949 */     toString(paramTreePath, stringBuilder);
/* 950 */     stringBuilder.append("]");
/* 951 */     return stringBuilder.toString();
/*     */   }
/*     */
/*     */   void toString(TreePath paramTreePath, StringBuilder paramStringBuilder) {
/* 955 */     TreePath treePath = paramTreePath.getParentPath();
/* 956 */     if (treePath != null) {
/* 957 */       toString(treePath, paramStringBuilder);
/* 958 */       paramStringBuilder.append(",");
/*     */     }
/* 960 */     paramStringBuilder.append(paramTreePath.getLeaf().getKind()).append(":").append(this.env.getPos(paramTreePath)).append(":S").append(this.env.getStartPos(paramTreePath));
/*     */   }
/*     */
/*     */   void warnIfEmpty(DocTree paramDocTree, List<? extends DocTree> paramList) {
/* 964 */     for (DocTree docTree : paramList) {
/* 965 */       switch (docTree.getKind()) {
/*     */         case VALUE:
/* 967 */           if (hasNonWhitespace((TextTree)docTree)) {
/*     */             return;
/*     */           }
/*     */           continue;
/*     */       }
/*     */       return;
/*     */     }
/* 974 */     this.env.messages.warning(Messages.Group.SYNTAX, paramDocTree, "dc.empty", new Object[] { (paramDocTree.getKind()).tagName });
/*     */   }
/*     */
/*     */   boolean hasNonWhitespace(TextTree paramTextTree) {
/* 978 */     String str = paramTextTree.getBody();
/* 979 */     for (byte b = 0; b < str.length(); b++) {
/* 980 */       if (!Character.isWhitespace(str.charAt(b)))
/* 981 */         return true;
/*     */     }
/* 983 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclint\Checker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
