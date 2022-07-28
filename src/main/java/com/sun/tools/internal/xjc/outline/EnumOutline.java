/*    */ package com.sun.tools.internal.xjc.outline;
/*    */ 
/*    */ import com.sun.codemodel.internal.JDefinedClass;
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.tools.internal.xjc.model.CEnumLeafInfo;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ 
/*    */ public abstract class EnumOutline
/*    */ {
/*    */   public final CEnumLeafInfo target;
/*    */   public final JDefinedClass clazz;
/* 58 */   public final List<EnumConstantOutline> constants = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public PackageOutline _package() {
/* 65 */     return parent().getPackageContext(this.clazz._package());
/*    */   }
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public abstract Outline parent();
/*    */ 
/*    */   
/*    */   protected EnumOutline(CEnumLeafInfo target, JDefinedClass clazz) {
/* 74 */     this.target = target;
/* 75 */     this.clazz = clazz;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\outline\EnumOutline.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */