/*    */ package com.sun.tools.internal.ws.resources;
/*    */ 
/*    */ import com.sun.istack.internal.localization.Localizable;
/*    */ import com.sun.istack.internal.localization.LocalizableMessageFactory;
/*    */ import com.sun.istack.internal.localization.Localizer;
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
/*    */ public final class UtilMessages
/*    */ {
/* 39 */   private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.sun.tools.internal.ws.resources.util");
/* 40 */   private static final Localizer localizer = new Localizer();
/*    */   
/*    */   public static Localizable localizableSAX_2_DOM_NOTSUPPORTED_CREATEELEMENT(Object arg0) {
/* 43 */     return messageFactory.getMessage("sax2dom.notsupported.createelement", new Object[] { arg0 });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String SAX_2_DOM_NOTSUPPORTED_CREATEELEMENT(Object arg0) {
/* 51 */     return localizer.localize(localizableSAX_2_DOM_NOTSUPPORTED_CREATEELEMENT(arg0));
/*    */   }
/*    */   
/*    */   public static Localizable localizableNULL_NAMESPACE_FOUND(Object arg0) {
/* 55 */     return messageFactory.getMessage("null.namespace.found", new Object[] { arg0 });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String NULL_NAMESPACE_FOUND(Object arg0) {
/* 63 */     return localizer.localize(localizableNULL_NAMESPACE_FOUND(arg0));
/*    */   }
/*    */   
/*    */   public static Localizable localizableHOLDER_VALUEFIELD_NOT_FOUND(Object arg0) {
/* 67 */     return messageFactory.getMessage("holder.valuefield.not.found", new Object[] { arg0 });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String HOLDER_VALUEFIELD_NOT_FOUND(Object arg0) {
/* 75 */     return localizer.localize(localizableHOLDER_VALUEFIELD_NOT_FOUND(arg0));
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\resources\UtilMessages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */