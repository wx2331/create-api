/*    */ package com.sun.xml.internal.rngom.parse.host;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*    */ import com.sun.xml.internal.rngom.ast.om.Location;
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
/*    */ public class Base
/*    */ {
/*    */   protected AnnotationsHost cast(Annotations ann) {
/* 58 */     if (ann == null) {
/* 59 */       return nullAnnotations;
/*    */     }
/* 61 */     return (AnnotationsHost)ann;
/*    */   }
/*    */   
/*    */   protected LocationHost cast(Location loc) {
/* 65 */     if (loc == null) {
/* 66 */       return nullLocation;
/*    */     }
/* 68 */     return (LocationHost)loc;
/*    */   }
/*    */   
/* 71 */   private static final AnnotationsHost nullAnnotations = new AnnotationsHost(null, null);
/* 72 */   private static final LocationHost nullLocation = new LocationHost(null, null);
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\host\Base.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */