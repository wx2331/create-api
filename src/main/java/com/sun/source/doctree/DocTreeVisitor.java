package com.sun.source.doctree;

import jdk.Exported;

@Exported
public interface DocTreeVisitor<R, P> {
  R visitAttribute(AttributeTree paramAttributeTree, P paramP);
  
  R visitAuthor(AuthorTree paramAuthorTree, P paramP);
  
  R visitComment(CommentTree paramCommentTree, P paramP);
  
  R visitDeprecated(DeprecatedTree paramDeprecatedTree, P paramP);
  
  R visitDocComment(DocCommentTree paramDocCommentTree, P paramP);
  
  R visitDocRoot(DocRootTree paramDocRootTree, P paramP);
  
  R visitEndElement(EndElementTree paramEndElementTree, P paramP);
  
  R visitEntity(EntityTree paramEntityTree, P paramP);
  
  R visitErroneous(ErroneousTree paramErroneousTree, P paramP);
  
  R visitIdentifier(IdentifierTree paramIdentifierTree, P paramP);
  
  R visitInheritDoc(InheritDocTree paramInheritDocTree, P paramP);
  
  R visitLink(LinkTree paramLinkTree, P paramP);
  
  R visitLiteral(LiteralTree paramLiteralTree, P paramP);
  
  R visitParam(ParamTree paramParamTree, P paramP);
  
  R visitReference(ReferenceTree paramReferenceTree, P paramP);
  
  R visitReturn(ReturnTree paramReturnTree, P paramP);
  
  R visitSee(SeeTree paramSeeTree, P paramP);
  
  R visitSerial(SerialTree paramSerialTree, P paramP);
  
  R visitSerialData(SerialDataTree paramSerialDataTree, P paramP);
  
  R visitSerialField(SerialFieldTree paramSerialFieldTree, P paramP);
  
  R visitSince(SinceTree paramSinceTree, P paramP);
  
  R visitStartElement(StartElementTree paramStartElementTree, P paramP);
  
  R visitText(TextTree paramTextTree, P paramP);
  
  R visitThrows(ThrowsTree paramThrowsTree, P paramP);
  
  R visitUnknownBlockTag(UnknownBlockTagTree paramUnknownBlockTagTree, P paramP);
  
  R visitUnknownInlineTag(UnknownInlineTagTree paramUnknownInlineTagTree, P paramP);
  
  R visitValue(ValueTree paramValueTree, P paramP);
  
  R visitVersion(VersionTree paramVersionTree, P paramP);
  
  R visitOther(DocTree paramDocTree, P paramP);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\doctree\DocTreeVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */