/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*    */ import com.sun.tools.internal.xjc.reader.Ring;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BindGreen;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.ClassSelector;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.SimpleTypeBuilder;
/*    */ import com.sun.xml.internal.xsom.XSComplexType;
/*    */ import com.sun.xml.internal.xsom.XSSchemaSet;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class CTBuilder
/*    */ {
/* 63 */   protected final ComplexTypeFieldBuilder builder = (ComplexTypeFieldBuilder)Ring.get(ComplexTypeFieldBuilder.class);
/* 64 */   protected final ClassSelector selector = (ClassSelector)Ring.get(ClassSelector.class);
/* 65 */   protected final SimpleTypeBuilder simpleTypeBuilder = (SimpleTypeBuilder)Ring.get(SimpleTypeBuilder.class);
/* 66 */   protected final ErrorReceiver errorReceiver = (ErrorReceiver)Ring.get(ErrorReceiver.class);
/* 67 */   protected final BindGreen green = (BindGreen)Ring.get(BindGreen.class);
/* 68 */   protected final XSSchemaSet schemas = (XSSchemaSet)Ring.get(XSSchemaSet.class);
/* 69 */   protected final BGMBuilder bgmBuilder = (BGMBuilder)Ring.get(BGMBuilder.class);
/*    */   
/*    */   abstract boolean isApplicable(XSComplexType paramXSComplexType);
/*    */   
/*    */   abstract void build(XSComplexType paramXSComplexType);
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\ct\CTBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */