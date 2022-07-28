/*    */ package com.sun.tools.internal.xjc.addon.locator;
/*    */ 
/*    */ import com.sun.codemodel.internal.JAssignmentTarget;
/*    */ import com.sun.codemodel.internal.JDefinedClass;
/*    */ import com.sun.codemodel.internal.JExpression;
/*    */ import com.sun.codemodel.internal.JFieldVar;
/*    */ import com.sun.codemodel.internal.JMethod;
/*    */ import com.sun.codemodel.internal.JVar;
/*    */ import com.sun.tools.internal.xjc.BadCommandLineException;
/*    */ import com.sun.tools.internal.xjc.Options;
/*    */ import com.sun.tools.internal.xjc.Plugin;
/*    */ import com.sun.tools.internal.xjc.outline.ClassOutline;
/*    */ import com.sun.tools.internal.xjc.outline.Outline;
/*    */ import com.sun.xml.internal.bind.Locatable;
/*    */ import com.sun.xml.internal.bind.annotation.XmlLocation;
/*    */ import java.io.IOException;
/*    */ import javax.xml.bind.annotation.XmlTransient;
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.Locator;
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
/*    */ public class SourceLocationAddOn
/*    */   extends Plugin
/*    */ {
/*    */   private static final String fieldName = "locator";
/*    */   
/*    */   public String getOptionName() {
/* 56 */     return "Xlocator";
/*    */   }
/*    */   
/*    */   public String getUsage() {
/* 60 */     return "  -Xlocator          :  enable source location support for generated code";
/*    */   }
/*    */   
/*    */   public int parseArgument(Options opt, String[] args, int i) throws BadCommandLineException, IOException {
/* 64 */     return 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean run(Outline outline, Options opt, ErrorHandler errorHandler) {
/* 74 */     for (ClassOutline ci : outline.getClasses()) {
/* 75 */       JDefinedClass impl = ci.implClass;
/* 76 */       if (ci.getSuperClass() == null) {
/* 77 */         JFieldVar jFieldVar = impl.field(2, Locator.class, "locator");
/* 78 */         jFieldVar.annotate(XmlLocation.class);
/* 79 */         jFieldVar.annotate(XmlTransient.class);
/*    */         
/* 81 */         impl._implements(Locatable.class);
/*    */         
/* 83 */         impl.method(1, Locator.class, "sourceLocation").body()._return((JExpression)jFieldVar);
/*    */         
/* 85 */         JMethod setter = impl.method(1, void.class, "setSourceLocation");
/* 86 */         JVar $newLoc = setter.param(Locator.class, "newLocator");
/* 87 */         setter.body().assign((JAssignmentTarget)jFieldVar, (JExpression)$newLoc);
/*    */       } 
/*    */     } 
/*    */     
/* 91 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\addon\locator\SourceLocationAddOn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */