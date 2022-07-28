/*    */ package com.sun.tools.doclets.internal.toolkit.builders;
/*    */
/*    */ import com.sun.tools.doclets.internal.toolkit.Content;
/*    */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */ public abstract class AbstractMemberBuilder
/*    */   extends AbstractBuilder
/*    */ {
/*    */   public AbstractMemberBuilder(Context paramContext) {
/* 54 */     super(paramContext);
/*    */   }
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */   public void build() throws DocletAbortException {
/* 65 */     throw new DocletAbortException("not supported");
/*    */   }
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */   public void build(XMLNode paramXMLNode, Content paramContent) {
/* 77 */     if (hasMembersToDocument())
/* 78 */       super.build(paramXMLNode, paramContent);
/*    */   }
/*    */
/*    */   public abstract boolean hasMembersToDocument();
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\builders\AbstractMemberBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
