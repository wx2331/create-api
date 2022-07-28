/*    */ package com.sun.tools.internal.xjc.api.util;
/*    */ 
/*    */ import java.io.File;
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
/*    */ public final class ToolsJarNotFoundException
/*    */   extends Exception
/*    */ {
/*    */   public final File toolsJar;
/*    */   
/*    */   public ToolsJarNotFoundException(File toolsJar) {
/* 44 */     super(calcMessage(toolsJar));
/* 45 */     this.toolsJar = toolsJar;
/*    */   }
/*    */   
/*    */   private static String calcMessage(File toolsJar) {
/* 49 */     return Messages.TOOLS_JAR_NOT_FOUND.format(new Object[] { toolsJar.getPath() });
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\ap\\util\ToolsJarNotFoundException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */