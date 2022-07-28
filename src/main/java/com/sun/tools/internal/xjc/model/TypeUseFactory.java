/*    */ package com.sun.tools.internal.xjc.model;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.TODO;
/*    */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*    */ import javax.activation.MimeType;
/*    */ import javax.xml.bind.annotation.adapters.XmlAdapter;
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
/*    */ public final class TypeUseFactory
/*    */ {
/*    */   public static TypeUse makeID(TypeUse t, ID id) {
/* 44 */     if (t.idUse() != ID.NONE)
/*    */     {
/*    */       
/* 47 */       throw new IllegalStateException(); } 
/* 48 */     return new TypeUseImpl(t.getInfo(), t.isCollection(), id, t.getExpectedMimeType(), t.getAdapterUse());
/*    */   }
/*    */   
/*    */   public static TypeUse makeMimeTyped(TypeUse t, MimeType mt) {
/* 52 */     if (t.getExpectedMimeType() != null)
/*    */     {
/*    */       
/* 55 */       throw new IllegalStateException(); } 
/* 56 */     return new TypeUseImpl(t.getInfo(), t.isCollection(), t.idUse(), mt, t.getAdapterUse());
/*    */   }
/*    */   
/*    */   public static TypeUse makeCollection(TypeUse t) {
/* 60 */     if (t.isCollection()) return t; 
/* 61 */     CAdapter au = t.getAdapterUse();
/* 62 */     if (au != null && !au.isWhitespaceAdapter()) {
/*    */ 
/*    */       
/* 65 */       TODO.checkSpec();
/* 66 */       return CBuiltinLeafInfo.STRING_LIST;
/*    */     } 
/* 68 */     return new TypeUseImpl(t.getInfo(), true, t.idUse(), t.getExpectedMimeType(), null);
/*    */   }
/*    */   
/*    */   public static TypeUse adapt(TypeUse t, CAdapter adapter) {
/* 72 */     assert t.getAdapterUse() == null;
/* 73 */     return new TypeUseImpl(t.getInfo(), t.isCollection(), t.idUse(), t.getExpectedMimeType(), adapter);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static TypeUse adapt(TypeUse t, Class<? extends XmlAdapter> adapter, boolean copy) {
/* 80 */     return adapt(t, new CAdapter(adapter, copy));
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\TypeUseFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */