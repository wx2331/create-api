/*     */ package com.sun.tools.javac.tree;
/*     */ 
/*     */ import com.sun.source.doctree.AttributeTree;
/*     */ import com.sun.source.doctree.DocTree;
/*     */ import com.sun.tools.javac.parser.Tokens;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.DiagnosticSource;
/*     */ import com.sun.tools.javac.util.JCDiagnostic;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DocTreeMaker
/*     */ {
/*  51 */   protected static final Context.Key<DocTreeMaker> treeMakerKey = new Context.Key();
/*     */ 
/*     */ 
/*     */   
/*     */   public static DocTreeMaker instance(Context paramContext) {
/*  56 */     DocTreeMaker docTreeMaker = (DocTreeMaker)paramContext.get(treeMakerKey);
/*  57 */     if (docTreeMaker == null)
/*  58 */       docTreeMaker = new DocTreeMaker(paramContext); 
/*  59 */     return docTreeMaker;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  64 */   public int pos = -1;
/*     */ 
/*     */   
/*     */   private final JCDiagnostic.Factory diags;
/*     */ 
/*     */ 
/*     */   
/*     */   protected DocTreeMaker(Context paramContext) {
/*  72 */     paramContext.put(treeMakerKey, this);
/*  73 */     this.diags = JCDiagnostic.Factory.instance(paramContext);
/*  74 */     this.pos = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DocTreeMaker at(int paramInt) {
/*  80 */     this.pos = paramInt;
/*  81 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DocTreeMaker at(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/*  87 */     this.pos = (paramDiagnosticPosition == null) ? -1 : paramDiagnosticPosition.getStartPosition();
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public DCTree.DCAttribute Attribute(Name paramName, AttributeTree.ValueKind paramValueKind, List<DCTree> paramList) {
/*  92 */     DCTree.DCAttribute dCAttribute = new DCTree.DCAttribute(paramName, paramValueKind, paramList);
/*  93 */     dCAttribute.pos = this.pos;
/*  94 */     return dCAttribute;
/*     */   }
/*     */   
/*     */   public DCTree.DCAuthor Author(List<DCTree> paramList) {
/*  98 */     DCTree.DCAuthor dCAuthor = new DCTree.DCAuthor(paramList);
/*  99 */     dCAuthor.pos = this.pos;
/* 100 */     return dCAuthor;
/*     */   }
/*     */   
/*     */   public DCTree.DCLiteral Code(DCTree.DCText paramDCText) {
/* 104 */     DCTree.DCLiteral dCLiteral = new DCTree.DCLiteral(DocTree.Kind.CODE, paramDCText);
/* 105 */     dCLiteral.pos = this.pos;
/* 106 */     return dCLiteral;
/*     */   }
/*     */   
/*     */   public DCTree.DCComment Comment(String paramString) {
/* 110 */     DCTree.DCComment dCComment = new DCTree.DCComment(paramString);
/* 111 */     dCComment.pos = this.pos;
/* 112 */     return dCComment;
/*     */   }
/*     */   
/*     */   public DCTree.DCDeprecated Deprecated(List<DCTree> paramList) {
/* 116 */     DCTree.DCDeprecated dCDeprecated = new DCTree.DCDeprecated(paramList);
/* 117 */     dCDeprecated.pos = this.pos;
/* 118 */     return dCDeprecated;
/*     */   }
/*     */   
/*     */   public DCTree.DCDocComment DocComment(Tokens.Comment paramComment, List<DCTree> paramList1, List<DCTree> paramList2, List<DCTree> paramList3) {
/* 122 */     DCTree.DCDocComment dCDocComment = new DCTree.DCDocComment(paramComment, paramList1, paramList2, paramList3);
/* 123 */     dCDocComment.pos = this.pos;
/* 124 */     return dCDocComment;
/*     */   }
/*     */   
/*     */   public DCTree.DCDocRoot DocRoot() {
/* 128 */     DCTree.DCDocRoot dCDocRoot = new DCTree.DCDocRoot();
/* 129 */     dCDocRoot.pos = this.pos;
/* 130 */     return dCDocRoot;
/*     */   }
/*     */   
/*     */   public DCTree.DCEndElement EndElement(Name paramName) {
/* 134 */     DCTree.DCEndElement dCEndElement = new DCTree.DCEndElement(paramName);
/* 135 */     dCEndElement.pos = this.pos;
/* 136 */     return dCEndElement;
/*     */   }
/*     */   
/*     */   public DCTree.DCEntity Entity(Name paramName) {
/* 140 */     DCTree.DCEntity dCEntity = new DCTree.DCEntity(paramName);
/* 141 */     dCEntity.pos = this.pos;
/* 142 */     return dCEntity;
/*     */   }
/*     */   
/*     */   public DCTree.DCErroneous Erroneous(String paramString1, DiagnosticSource paramDiagnosticSource, String paramString2, Object... paramVarArgs) {
/* 146 */     DCTree.DCErroneous dCErroneous = new DCTree.DCErroneous(paramString1, this.diags, paramDiagnosticSource, paramString2, paramVarArgs);
/* 147 */     dCErroneous.pos = this.pos;
/* 148 */     return dCErroneous;
/*     */   }
/*     */   
/*     */   public DCTree.DCThrows Exception(DCTree.DCReference paramDCReference, List<DCTree> paramList) {
/* 152 */     DCTree.DCThrows dCThrows = new DCTree.DCThrows(DocTree.Kind.EXCEPTION, paramDCReference, paramList);
/* 153 */     dCThrows.pos = this.pos;
/* 154 */     return dCThrows;
/*     */   }
/*     */   
/*     */   public DCTree.DCIdentifier Identifier(Name paramName) {
/* 158 */     DCTree.DCIdentifier dCIdentifier = new DCTree.DCIdentifier(paramName);
/* 159 */     dCIdentifier.pos = this.pos;
/* 160 */     return dCIdentifier;
/*     */   }
/*     */   
/*     */   public DCTree.DCInheritDoc InheritDoc() {
/* 164 */     DCTree.DCInheritDoc dCInheritDoc = new DCTree.DCInheritDoc();
/* 165 */     dCInheritDoc.pos = this.pos;
/* 166 */     return dCInheritDoc;
/*     */   }
/*     */   
/*     */   public DCTree.DCLink Link(DCTree.DCReference paramDCReference, List<DCTree> paramList) {
/* 170 */     DCTree.DCLink dCLink = new DCTree.DCLink(DocTree.Kind.LINK, paramDCReference, paramList);
/* 171 */     dCLink.pos = this.pos;
/* 172 */     return dCLink;
/*     */   }
/*     */   
/*     */   public DCTree.DCLink LinkPlain(DCTree.DCReference paramDCReference, List<DCTree> paramList) {
/* 176 */     DCTree.DCLink dCLink = new DCTree.DCLink(DocTree.Kind.LINK_PLAIN, paramDCReference, paramList);
/* 177 */     dCLink.pos = this.pos;
/* 178 */     return dCLink;
/*     */   }
/*     */   
/*     */   public DCTree.DCLiteral Literal(DCTree.DCText paramDCText) {
/* 182 */     DCTree.DCLiteral dCLiteral = new DCTree.DCLiteral(DocTree.Kind.LITERAL, paramDCText);
/* 183 */     dCLiteral.pos = this.pos;
/* 184 */     return dCLiteral;
/*     */   }
/*     */   
/*     */   public DCTree.DCParam Param(boolean paramBoolean, DCTree.DCIdentifier paramDCIdentifier, List<DCTree> paramList) {
/* 188 */     DCTree.DCParam dCParam = new DCTree.DCParam(paramBoolean, paramDCIdentifier, paramList);
/* 189 */     dCParam.pos = this.pos;
/* 190 */     return dCParam;
/*     */   }
/*     */ 
/*     */   
/*     */   public DCTree.DCReference Reference(String paramString, JCTree paramJCTree, Name paramName, List<JCTree> paramList) {
/* 195 */     DCTree.DCReference dCReference = new DCTree.DCReference(paramString, paramJCTree, paramName, paramList);
/* 196 */     dCReference.pos = this.pos;
/* 197 */     return dCReference;
/*     */   }
/*     */   
/*     */   public DCTree.DCReturn Return(List<DCTree> paramList) {
/* 201 */     DCTree.DCReturn dCReturn = new DCTree.DCReturn(paramList);
/* 202 */     dCReturn.pos = this.pos;
/* 203 */     return dCReturn;
/*     */   }
/*     */   
/*     */   public DCTree.DCSee See(List<DCTree> paramList) {
/* 207 */     DCTree.DCSee dCSee = new DCTree.DCSee(paramList);
/* 208 */     dCSee.pos = this.pos;
/* 209 */     return dCSee;
/*     */   }
/*     */   
/*     */   public DCTree.DCSerial Serial(List<DCTree> paramList) {
/* 213 */     DCTree.DCSerial dCSerial = new DCTree.DCSerial(paramList);
/* 214 */     dCSerial.pos = this.pos;
/* 215 */     return dCSerial;
/*     */   }
/*     */   
/*     */   public DCTree.DCSerialData SerialData(List<DCTree> paramList) {
/* 219 */     DCTree.DCSerialData dCSerialData = new DCTree.DCSerialData(paramList);
/* 220 */     dCSerialData.pos = this.pos;
/* 221 */     return dCSerialData;
/*     */   }
/*     */   
/*     */   public DCTree.DCSerialField SerialField(DCTree.DCIdentifier paramDCIdentifier, DCTree.DCReference paramDCReference, List<DCTree> paramList) {
/* 225 */     DCTree.DCSerialField dCSerialField = new DCTree.DCSerialField(paramDCIdentifier, paramDCReference, paramList);
/* 226 */     dCSerialField.pos = this.pos;
/* 227 */     return dCSerialField;
/*     */   }
/*     */   
/*     */   public DCTree.DCSince Since(List<DCTree> paramList) {
/* 231 */     DCTree.DCSince dCSince = new DCTree.DCSince(paramList);
/* 232 */     dCSince.pos = this.pos;
/* 233 */     return dCSince;
/*     */   }
/*     */   
/*     */   public DCTree.DCStartElement StartElement(Name paramName, List<DCTree> paramList, boolean paramBoolean) {
/* 237 */     DCTree.DCStartElement dCStartElement = new DCTree.DCStartElement(paramName, paramList, paramBoolean);
/* 238 */     dCStartElement.pos = this.pos;
/* 239 */     return dCStartElement;
/*     */   }
/*     */   
/*     */   public DCTree.DCText Text(String paramString) {
/* 243 */     DCTree.DCText dCText = new DCTree.DCText(paramString);
/* 244 */     dCText.pos = this.pos;
/* 245 */     return dCText;
/*     */   }
/*     */   
/*     */   public DCTree.DCThrows Throws(DCTree.DCReference paramDCReference, List<DCTree> paramList) {
/* 249 */     DCTree.DCThrows dCThrows = new DCTree.DCThrows(DocTree.Kind.THROWS, paramDCReference, paramList);
/* 250 */     dCThrows.pos = this.pos;
/* 251 */     return dCThrows;
/*     */   }
/*     */   
/*     */   public DCTree.DCUnknownBlockTag UnknownBlockTag(Name paramName, List<DCTree> paramList) {
/* 255 */     DCTree.DCUnknownBlockTag dCUnknownBlockTag = new DCTree.DCUnknownBlockTag(paramName, paramList);
/* 256 */     dCUnknownBlockTag.pos = this.pos;
/* 257 */     return dCUnknownBlockTag;
/*     */   }
/*     */   
/*     */   public DCTree.DCUnknownInlineTag UnknownInlineTag(Name paramName, List<DCTree> paramList) {
/* 261 */     DCTree.DCUnknownInlineTag dCUnknownInlineTag = new DCTree.DCUnknownInlineTag(paramName, paramList);
/* 262 */     dCUnknownInlineTag.pos = this.pos;
/* 263 */     return dCUnknownInlineTag;
/*     */   }
/*     */   
/*     */   public DCTree.DCValue Value(DCTree.DCReference paramDCReference) {
/* 267 */     DCTree.DCValue dCValue = new DCTree.DCValue(paramDCReference);
/* 268 */     dCValue.pos = this.pos;
/* 269 */     return dCValue;
/*     */   }
/*     */   
/*     */   public DCTree.DCVersion Version(List<DCTree> paramList) {
/* 273 */     DCTree.DCVersion dCVersion = new DCTree.DCVersion(paramList);
/* 274 */     dCVersion.pos = this.pos;
/* 275 */     return dCVersion;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\tree\DocTreeMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */