/*    */ package com.sun.tools.internal.ws.processor.modeler.annotation;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import javax.annotation.processing.ProcessingEnvironment;
/*    */ import javax.lang.model.element.TypeElement;
/*    */ import javax.lang.model.type.DeclaredType;
/*    */ import javax.lang.model.type.NoType;
/*    */ import javax.lang.model.type.TypeMirror;
/*    */ import javax.lang.model.util.SimpleTypeVisitor6;
/*    */ import javax.lang.model.util.Types;
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
/*    */ public class MakeSafeTypeVisitor
/*    */   extends SimpleTypeVisitor6<TypeMirror, Types>
/*    */ {
/*    */   TypeElement collectionType;
/*    */   TypeElement mapType;
/*    */   
/*    */   public MakeSafeTypeVisitor(ProcessingEnvironment processingEnvironment) {
/* 51 */     this.collectionType = processingEnvironment.getElementUtils().getTypeElement(Collection.class.getName());
/* 52 */     this.mapType = processingEnvironment.getElementUtils().getTypeElement(Map.class.getName());
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeMirror visitDeclared(DeclaredType t, Types types) {
/* 57 */     if (TypeModeler.isSubElement((TypeElement)t.asElement(), this.collectionType) || 
/* 58 */       TypeModeler.isSubElement((TypeElement)t.asElement(), this.mapType)) {
/* 59 */       Collection<? extends TypeMirror> args = t.getTypeArguments();
/* 60 */       TypeMirror[] safeArgs = new TypeMirror[args.size()];
/* 61 */       int i = 0;
/* 62 */       for (TypeMirror arg : args) {
/* 63 */         safeArgs[i++] = visit(arg, types);
/*    */       }
/* 65 */       return types.getDeclaredType((TypeElement)t.asElement(), safeArgs);
/*    */     } 
/* 67 */     return types.erasure(t);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeMirror visitNoType(NoType type, Types types) {
/* 72 */     return type;
/*    */   }
/*    */   
/*    */   protected TypeMirror defaultAction(TypeMirror e, Types types) {
/* 76 */     return types.erasure(e);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\modeler\annotation\MakeSafeTypeVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */