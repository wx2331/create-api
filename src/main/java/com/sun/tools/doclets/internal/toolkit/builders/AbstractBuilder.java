/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */ 
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.FatalError;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Set;
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
/*     */ public abstract class AbstractBuilder
/*     */ {
/*     */   protected final Configuration configuration;
/*     */   protected final Set<String> containingPackagesSeen;
/*     */   protected final LayoutParser layoutParser;
/*     */   protected static final boolean DEBUG = false;
/*     */   
/*     */   public static class Context
/*     */   {
/*     */     final Configuration configuration;
/*     */     final Set<String> containingPackagesSeen;
/*     */     final LayoutParser layoutParser;
/*     */     
/*     */     Context(Configuration param1Configuration, Set<String> param1Set, LayoutParser param1LayoutParser) {
/*  76 */       this.configuration = param1Configuration;
/*  77 */       this.containingPackagesSeen = param1Set;
/*  78 */       this.layoutParser = param1LayoutParser;
/*     */     }
/*     */   }
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
/*     */   public AbstractBuilder(Context paramContext) {
/* 107 */     this.configuration = paramContext.configuration;
/* 108 */     this.containingPackagesSeen = paramContext.containingPackagesSeen;
/* 109 */     this.layoutParser = paramContext.layoutParser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void build() throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build(XMLNode paramXMLNode, Content paramContent) {
/* 133 */     String str = paramXMLNode.name;
/*     */     try {
/* 135 */       invokeMethod("build" + str, new Class[] { XMLNode.class, Content.class }, new Object[] { paramXMLNode, paramContent });
/*     */     
/*     */     }
/* 138 */     catch (NoSuchMethodException noSuchMethodException) {
/* 139 */       noSuchMethodException.printStackTrace();
/* 140 */       this.configuration.root.printError("Unknown element: " + str);
/* 141 */       throw new DocletAbortException(noSuchMethodException);
/* 142 */     } catch (InvocationTargetException invocationTargetException) {
/* 143 */       Throwable throwable = invocationTargetException.getCause();
/* 144 */       if (throwable instanceof FatalError)
/* 145 */         throw (FatalError)throwable; 
/* 146 */       if (throwable instanceof DocletAbortException) {
/* 147 */         throw (DocletAbortException)throwable;
/*     */       }
/* 149 */       throw new DocletAbortException(throwable);
/*     */     }
/* 151 */     catch (Exception exception) {
/* 152 */       exception.printStackTrace();
/* 153 */       this.configuration.root.printError("Exception " + exception
/* 154 */           .getClass().getName() + " thrown while processing element: " + str);
/*     */       
/* 156 */       throw new DocletAbortException(exception);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void buildChildren(XMLNode paramXMLNode, Content paramContent) {
/* 167 */     for (XMLNode xMLNode : paramXMLNode.children) {
/* 168 */       build(xMLNode, paramContent);
/*     */     }
/*     */   }
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
/*     */   protected void invokeMethod(String paramString, Class<?>[] paramArrayOfClass, Object[] paramArrayOfObject) throws Exception {
/* 186 */     Method method = getClass().getMethod(paramString, paramArrayOfClass);
/* 187 */     method.invoke(this, paramArrayOfObject);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\builders\AbstractBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */