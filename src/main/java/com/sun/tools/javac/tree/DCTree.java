/*     */ package com.sun.tools.javac.tree;
/*     */
/*     */ import com.sun.source.doctree.AttributeTree;
/*     */ import com.sun.source.doctree.AuthorTree;
/*     */ import com.sun.source.doctree.BlockTagTree;
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
/*     */ import com.sun.source.doctree.InlineTagTree;
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
/*     */ import com.sun.tools.javac.parser.Tokens;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.DiagnosticSource;
/*     */ import com.sun.tools.javac.util.JCDiagnostic;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import java.util.List;
/*     */ import javax.lang.model.element.Name;
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
/*     */ public abstract class DCTree
/*     */   implements DocTree
/*     */ {
/*     */   public int pos;
/*     */
/*     */   public long getSourcePosition(DCDocComment paramDCDocComment) {
/*  64 */     return paramDCDocComment.comment.getSourcePos(this.pos);
/*     */   }
/*     */
/*     */   public JCDiagnostic.DiagnosticPosition pos(DCDocComment paramDCDocComment) {
/*  68 */     return (JCDiagnostic.DiagnosticPosition)new JCDiagnostic.SimpleDiagnosticPosition(paramDCDocComment.comment.getSourcePos(this.pos));
/*     */   }
/*     */
/*     */
/*     */
/*     */   public String toString() {
/*  74 */     StringWriter stringWriter = new StringWriter();
/*     */     try {
/*  76 */       (new DocPretty(stringWriter)).print(this);
/*     */     }
/*  78 */     catch (IOException iOException) {
/*     */
/*     */
/*  81 */       throw new AssertionError(iOException);
/*     */     }
/*  83 */     return stringWriter.toString();
/*     */   }
/*     */
/*     */   public static abstract class DCEndPosTree<T extends DCEndPosTree<T>>
/*     */     extends DCTree {
/*  88 */     private int endPos = -1;
/*     */
/*     */     public int getEndPos(DCDocComment param1DCDocComment) {
/*  91 */       return param1DCDocComment.comment.getSourcePos(this.endPos);
/*     */     }
/*     */
/*     */
/*     */     public T setEndPos(int param1Int) {
/*  96 */       this.endPos = param1Int;
/*  97 */       return (T)this;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCDocComment
/*     */     extends DCTree
/*     */     implements DocCommentTree
/*     */   {
/*     */     public final Tokens.Comment comment;
/*     */     public final List<DCTree> firstSentence;
/*     */     public final List<DCTree> body;
/*     */     public final List<DCTree> tags;
/*     */
/*     */     public DCDocComment(Tokens.Comment param1Comment, List<DCTree> param1List1, List<DCTree> param1List2, List<DCTree> param1List3) {
/* 111 */       this.comment = param1Comment;
/* 112 */       this.firstSentence = param1List1;
/* 113 */       this.body = param1List2;
/* 114 */       this.tags = param1List3;
/*     */     }
/*     */
/*     */     public Kind getKind() {
/* 118 */       return Kind.DOC_COMMENT;
/*     */     }
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 122 */       return (R)param1DocTreeVisitor.visitDocComment(this, param1D);
/*     */     }
/*     */
/*     */     public List<? extends DocTree> getFirstSentence() {
/* 126 */       return (List)this.firstSentence;
/*     */     }
/*     */
/*     */     public List<? extends DocTree> getBody() {
/* 130 */       return (List)this.body;
/*     */     }
/*     */
/*     */     public List<? extends DocTree> getBlockTags() {
/* 134 */       return (List)this.tags;
/*     */     }
/*     */   }
/*     */
/*     */   public static abstract class DCBlockTag
/*     */     extends DCTree implements BlockTagTree {
/*     */     public String getTagName() {
/* 141 */       return (getKind()).tagName;
/*     */     }
/*     */   }
/*     */
/*     */   public static abstract class DCInlineTag extends DCEndPosTree<DCInlineTag> implements InlineTagTree {
/*     */     public String getTagName() {
/* 147 */       return (getKind()).tagName;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCAttribute extends DCTree implements AttributeTree {
/*     */     public final Name name;
/*     */     public final ValueKind vkind;
/*     */     public final List<DCTree> value;
/*     */
/*     */     DCAttribute(Name param1Name, ValueKind param1ValueKind, List<DCTree> param1List) {
/* 157 */       Assert.check((param1ValueKind == ValueKind.EMPTY) ? ((param1List == null)) : ((param1List != null)));
/* 158 */       this.name = param1Name;
/* 159 */       this.vkind = param1ValueKind;
/* 160 */       this.value = param1List;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 165 */       return Kind.ATTRIBUTE;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 170 */       return (R)param1DocTreeVisitor.visitAttribute(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public Name getName() {
/* 175 */       return this.name;
/*     */     }
/*     */
/*     */
/*     */     public ValueKind getValueKind() {
/* 180 */       return this.vkind;
/*     */     }
/*     */
/*     */
/*     */     public List<DCTree> getValue() {
/* 185 */       return this.value;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCAuthor extends DCBlockTag implements AuthorTree {
/*     */     public final List<DCTree> name;
/*     */
/*     */     DCAuthor(List<DCTree> param1List) {
/* 193 */       this.name = param1List;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 198 */       return Kind.AUTHOR;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 203 */       return (R)param1DocTreeVisitor.visitAuthor(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public List<? extends DocTree> getName() {
/* 208 */       return (List)this.name;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCComment extends DCTree implements CommentTree {
/*     */     public final String body;
/*     */
/*     */     DCComment(String param1String) {
/* 216 */       this.body = param1String;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 221 */       return Kind.COMMENT;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 226 */       return (R)param1DocTreeVisitor.visitComment(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public String getBody() {
/* 231 */       return this.body;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCDeprecated extends DCBlockTag implements DeprecatedTree {
/*     */     public final List<DCTree> body;
/*     */
/*     */     DCDeprecated(List<DCTree> param1List) {
/* 239 */       this.body = param1List;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 244 */       return Kind.DEPRECATED;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 249 */       return (R)param1DocTreeVisitor.visitDeprecated(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public List<? extends DocTree> getBody() {
/* 254 */       return (List)this.body;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCDocRoot
/*     */     extends DCInlineTag
/*     */     implements DocRootTree {
/*     */     public Kind getKind() {
/* 262 */       return Kind.DOC_ROOT;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 267 */       return (R)param1DocTreeVisitor.visitDocRoot(this, param1D);
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCEndElement extends DCTree implements EndElementTree {
/*     */     public final Name name;
/*     */
/*     */     DCEndElement(Name param1Name) {
/* 275 */       this.name = param1Name;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 280 */       return Kind.END_ELEMENT;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 285 */       return (R)param1DocTreeVisitor.visitEndElement(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public Name getName() {
/* 290 */       return this.name;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCEntity extends DCTree implements EntityTree {
/*     */     public final Name name;
/*     */
/*     */     DCEntity(Name param1Name) {
/* 298 */       this.name = param1Name;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 303 */       return Kind.ENTITY;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 308 */       return (R)param1DocTreeVisitor.visitEntity(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public Name getName() {
/* 313 */       return this.name;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCErroneous extends DCTree implements ErroneousTree, JCDiagnostic.DiagnosticPosition {
/*     */     public final String body;
/*     */     public final JCDiagnostic diag;
/*     */
/*     */     DCErroneous(String param1String1, JCDiagnostic.Factory param1Factory, DiagnosticSource param1DiagnosticSource, String param1String2, Object... param1VarArgs) {
/* 322 */       this.body = param1String1;
/* 323 */       this.diag = param1Factory.error(param1DiagnosticSource, this, param1String2, param1VarArgs);
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 328 */       return Kind.ERRONEOUS;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 333 */       return (R)param1DocTreeVisitor.visitErroneous(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public String getBody() {
/* 338 */       return this.body;
/*     */     }
/*     */
/*     */
/*     */     public Diagnostic<JavaFileObject> getDiagnostic() {
/* 343 */       return (Diagnostic<JavaFileObject>)this.diag;
/*     */     }
/*     */
/*     */
/*     */     public JCTree getTree() {
/* 348 */       return null;
/*     */     }
/*     */
/*     */
/*     */     public int getStartPosition() {
/* 353 */       return this.pos;
/*     */     }
/*     */
/*     */
/*     */     public int getPreferredPosition() {
/* 358 */       return this.pos + this.body.length() - 1;
/*     */     }
/*     */
/*     */
/*     */     public int getEndPosition(EndPosTable param1EndPosTable) {
/* 363 */       return this.pos + this.body.length();
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCIdentifier
/*     */     extends DCTree implements IdentifierTree {
/*     */     public final Name name;
/*     */
/*     */     DCIdentifier(Name param1Name) {
/* 372 */       this.name = param1Name;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 377 */       return Kind.IDENTIFIER;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 382 */       return (R)param1DocTreeVisitor.visitIdentifier(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public Name getName() {
/* 387 */       return this.name;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCInheritDoc
/*     */     extends DCInlineTag implements InheritDocTree {
/*     */     public Kind getKind() {
/* 394 */       return Kind.INHERIT_DOC;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 399 */       return (R)param1DocTreeVisitor.visitInheritDoc(this, param1D);
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCLink extends DCInlineTag implements LinkTree {
/*     */     public final Kind kind;
/*     */     public final DCReference ref;
/*     */     public final List<DCTree> label;
/*     */
/*     */     DCLink(Kind param1Kind, DCReference param1DCReference, List<DCTree> param1List) {
/* 409 */       Assert.check((param1Kind == Kind.LINK || param1Kind == Kind.LINK_PLAIN));
/* 410 */       this.kind = param1Kind;
/* 411 */       this.ref = param1DCReference;
/* 412 */       this.label = param1List;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 417 */       return this.kind;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 422 */       return (R)param1DocTreeVisitor.visitLink(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public ReferenceTree getReference() {
/* 427 */       return this.ref;
/*     */     }
/*     */
/*     */
/*     */     public List<? extends DocTree> getLabel() {
/* 432 */       return (List)this.label;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCLiteral extends DCInlineTag implements LiteralTree {
/*     */     public final Kind kind;
/*     */     public final DCText body;
/*     */
/*     */     DCLiteral(Kind param1Kind, DCText param1DCText) {
/* 441 */       Assert.check((param1Kind == Kind.CODE || param1Kind == Kind.LITERAL));
/* 442 */       this.kind = param1Kind;
/* 443 */       this.body = param1DCText;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 448 */       return this.kind;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 453 */       return (R)param1DocTreeVisitor.visitLiteral(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public DCText getBody() {
/* 458 */       return this.body;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCParam extends DCBlockTag implements ParamTree {
/*     */     public final boolean isTypeParameter;
/*     */     public final DCIdentifier name;
/*     */     public final List<DCTree> description;
/*     */
/*     */     DCParam(boolean param1Boolean, DCIdentifier param1DCIdentifier, List<DCTree> param1List) {
/* 468 */       this.isTypeParameter = param1Boolean;
/* 469 */       this.name = param1DCIdentifier;
/* 470 */       this.description = param1List;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 475 */       return Kind.PARAM;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 480 */       return (R)param1DocTreeVisitor.visitParam(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public boolean isTypeParameter() {
/* 485 */       return this.isTypeParameter;
/*     */     }
/*     */
/*     */
/*     */     public IdentifierTree getName() {
/* 490 */       return this.name;
/*     */     }
/*     */
/*     */
/*     */     public List<? extends DocTree> getDescription() {
/* 495 */       return (List)this.description;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public static class DCReference
/*     */     extends DCEndPosTree<DCReference>
/*     */     implements ReferenceTree
/*     */   {
/*     */     public final String signature;
/*     */     public final JCTree qualifierExpression;
/*     */     public final Name memberName;
/*     */     public final List<JCTree> paramTypes;
/*     */
/*     */     DCReference(String param1String, JCTree param1JCTree, Name param1Name, List<JCTree> param1List) {
/* 510 */       this.signature = param1String;
/* 511 */       this.qualifierExpression = param1JCTree;
/* 512 */       this.memberName = param1Name;
/* 513 */       this.paramTypes = param1List;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 518 */       return Kind.REFERENCE;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 523 */       return (R)param1DocTreeVisitor.visitReference(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public String getSignature() {
/* 528 */       return this.signature;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCReturn extends DCBlockTag implements ReturnTree {
/*     */     public final List<DCTree> description;
/*     */
/*     */     DCReturn(List<DCTree> param1List) {
/* 536 */       this.description = param1List;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 541 */       return Kind.RETURN;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 546 */       return (R)param1DocTreeVisitor.visitReturn(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public List<? extends DocTree> getDescription() {
/* 551 */       return (List)this.description;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCSee extends DCBlockTag implements SeeTree {
/*     */     public final List<DCTree> reference;
/*     */
/*     */     DCSee(List<DCTree> param1List) {
/* 559 */       this.reference = param1List;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 564 */       return Kind.SEE;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 569 */       return (R)param1DocTreeVisitor.visitSee(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public List<? extends DocTree> getReference() {
/* 574 */       return (List)this.reference;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCSerial extends DCBlockTag implements SerialTree {
/*     */     public final List<DCTree> description;
/*     */
/*     */     DCSerial(List<DCTree> param1List) {
/* 582 */       this.description = param1List;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 587 */       return Kind.SERIAL;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 592 */       return (R)param1DocTreeVisitor.visitSerial(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public List<? extends DocTree> getDescription() {
/* 597 */       return (List)this.description;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCSerialData extends DCBlockTag implements SerialDataTree {
/*     */     public final List<DCTree> description;
/*     */
/*     */     DCSerialData(List<DCTree> param1List) {
/* 605 */       this.description = param1List;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 610 */       return Kind.SERIAL_DATA;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 615 */       return (R)param1DocTreeVisitor.visitSerialData(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public List<? extends DocTree> getDescription() {
/* 620 */       return (List)this.description;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCSerialField extends DCBlockTag implements SerialFieldTree {
/*     */     public final DCIdentifier name;
/*     */     public final DCReference type;
/*     */     public final List<DCTree> description;
/*     */
/*     */     DCSerialField(DCIdentifier param1DCIdentifier, DCReference param1DCReference, List<DCTree> param1List) {
/* 630 */       this.description = param1List;
/* 631 */       this.name = param1DCIdentifier;
/* 632 */       this.type = param1DCReference;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 637 */       return Kind.SERIAL_FIELD;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 642 */       return (R)param1DocTreeVisitor.visitSerialField(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public List<? extends DocTree> getDescription() {
/* 647 */       return (List)this.description;
/*     */     }
/*     */
/*     */
/*     */     public IdentifierTree getName() {
/* 652 */       return this.name;
/*     */     }
/*     */
/*     */
/*     */     public ReferenceTree getType() {
/* 657 */       return this.type;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCSince extends DCBlockTag implements SinceTree {
/*     */     public final List<DCTree> body;
/*     */
/*     */     DCSince(List<DCTree> param1List) {
/* 665 */       this.body = param1List;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 670 */       return Kind.SINCE;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 675 */       return (R)param1DocTreeVisitor.visitSince(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public List<? extends DocTree> getBody() {
/* 680 */       return (List)this.body;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCStartElement extends DCEndPosTree<DCStartElement> implements StartElementTree {
/*     */     public final Name name;
/*     */     public final List<DCTree> attrs;
/*     */     public final boolean selfClosing;
/*     */
/*     */     DCStartElement(Name param1Name, List<DCTree> param1List, boolean param1Boolean) {
/* 690 */       this.name = param1Name;
/* 691 */       this.attrs = param1List;
/* 692 */       this.selfClosing = param1Boolean;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 697 */       return Kind.START_ELEMENT;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 702 */       return (R)param1DocTreeVisitor.visitStartElement(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public Name getName() {
/* 707 */       return this.name;
/*     */     }
/*     */
/*     */
/*     */     public List<? extends DocTree> getAttributes() {
/* 712 */       return (List)this.attrs;
/*     */     }
/*     */
/*     */
/*     */     public boolean isSelfClosing() {
/* 717 */       return this.selfClosing;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCText extends DCTree implements TextTree {
/*     */     public final String text;
/*     */
/*     */     DCText(String param1String) {
/* 725 */       this.text = param1String;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 730 */       return Kind.TEXT;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 735 */       return (R)param1DocTreeVisitor.visitText(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public String getBody() {
/* 740 */       return this.text;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCThrows extends DCBlockTag implements ThrowsTree {
/*     */     public final Kind kind;
/*     */     public final DCReference name;
/*     */     public final List<DCTree> description;
/*     */
/*     */     DCThrows(Kind param1Kind, DCReference param1DCReference, List<DCTree> param1List) {
/* 750 */       Assert.check((param1Kind == Kind.EXCEPTION || param1Kind == Kind.THROWS));
/* 751 */       this.kind = param1Kind;
/* 752 */       this.name = param1DCReference;
/* 753 */       this.description = param1List;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 758 */       return this.kind;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 763 */       return (R)param1DocTreeVisitor.visitThrows(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public ReferenceTree getExceptionName() {
/* 768 */       return this.name;
/*     */     }
/*     */
/*     */
/*     */     public List<? extends DocTree> getDescription() {
/* 773 */       return (List)this.description;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCUnknownBlockTag extends DCBlockTag implements UnknownBlockTagTree {
/*     */     public final Name name;
/*     */     public final List<DCTree> content;
/*     */
/*     */     DCUnknownBlockTag(Name param1Name, List<DCTree> param1List) {
/* 782 */       this.name = param1Name;
/* 783 */       this.content = param1List;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 788 */       return Kind.UNKNOWN_BLOCK_TAG;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 793 */       return (R)param1DocTreeVisitor.visitUnknownBlockTag(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public String getTagName() {
/* 798 */       return this.name.toString();
/*     */     }
/*     */
/*     */
/*     */     public List<? extends DocTree> getContent() {
/* 803 */       return (List)this.content;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCUnknownInlineTag extends DCInlineTag implements UnknownInlineTagTree {
/*     */     public final Name name;
/*     */     public final List<DCTree> content;
/*     */
/*     */     DCUnknownInlineTag(Name param1Name, List<DCTree> param1List) {
/* 812 */       this.name = param1Name;
/* 813 */       this.content = param1List;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 818 */       return Kind.UNKNOWN_INLINE_TAG;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 823 */       return (R)param1DocTreeVisitor.visitUnknownInlineTag(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public String getTagName() {
/* 828 */       return this.name.toString();
/*     */     }
/*     */
/*     */
/*     */     public List<? extends DocTree> getContent() {
/* 833 */       return (List)this.content;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCValue extends DCInlineTag implements ValueTree {
/*     */     public final DCReference ref;
/*     */
/*     */     DCValue(DCReference param1DCReference) {
/* 841 */       this.ref = param1DCReference;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 846 */       return Kind.VALUE;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 851 */       return (R)param1DocTreeVisitor.visitValue(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public ReferenceTree getReference() {
/* 856 */       return this.ref;
/*     */     }
/*     */   }
/*     */
/*     */   public static class DCVersion extends DCBlockTag implements VersionTree {
/*     */     public final List<DCTree> body;
/*     */
/*     */     DCVersion(List<DCTree> param1List) {
/* 864 */       this.body = param1List;
/*     */     }
/*     */
/*     */
/*     */     public Kind getKind() {
/* 869 */       return Kind.VERSION;
/*     */     }
/*     */
/*     */
/*     */     public <R, D> R accept(DocTreeVisitor<R, D> param1DocTreeVisitor, D param1D) {
/* 874 */       return (R)param1DocTreeVisitor.visitVersion(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public List<? extends DocTree> getBody() {
/* 879 */       return (List)this.body;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\tree\DCTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
