/*     */ package sun.tools.jinfo;
/*     */ 
/*     */ import com.sun.tools.attach.VirtualMachine;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import sun.tools.attach.HotSpotVirtualMachine;
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
/*     */ public class JInfo
/*     */ {
/*     */   public static void main(String[] paramArrayOfString) throws Exception {
/*  45 */     if (paramArrayOfString.length == 0) {
/*  46 */       usage(1);
/*     */     }
/*     */     
/*  49 */     boolean bool = true;
/*  50 */     String str = paramArrayOfString[0];
/*  51 */     if (str.startsWith("-")) {
/*  52 */       if (str.equals("-flags") || str
/*  53 */         .equals("-sysprops")) {
/*     */ 
/*     */ 
/*     */         
/*  57 */         if (paramArrayOfString.length != 2 && paramArrayOfString.length != 3) {
/*  58 */           usage(1);
/*     */         }
/*  60 */       } else if (str.equals("-flag")) {
/*     */         
/*  62 */         bool = false;
/*     */       } else {
/*     */         boolean bool1;
/*     */         
/*  66 */         if (str.equals("-help") || str.equals("-h")) {
/*  67 */           bool1 = false;
/*     */         } else {
/*  69 */           bool1 = true;
/*     */         } 
/*  71 */         usage(bool1);
/*     */       } 
/*     */     }
/*     */     
/*  75 */     if (bool) {
/*  76 */       runTool(paramArrayOfString);
/*     */     }
/*  78 */     else if (paramArrayOfString.length == 3) {
/*  79 */       String str1 = paramArrayOfString[2];
/*  80 */       String str2 = paramArrayOfString[1];
/*  81 */       flag(str1, str2);
/*     */     } else {
/*     */       boolean bool1;
/*  84 */       if (str.equals("-help") || str.equals("-h")) {
/*  85 */         bool1 = false;
/*     */       } else {
/*  87 */         bool1 = true;
/*     */       } 
/*  89 */       usage(bool1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void runTool(String[] paramArrayOfString) throws Exception {
/*  96 */     String str = "sun.jvm.hotspot.tools.JInfo";
/*     */     
/*  98 */     Class<?> clazz = loadClass(str);
/*  99 */     if (clazz == null) {
/* 100 */       usage(1);
/*     */     }
/*     */ 
/*     */     
/* 104 */     Class[] arrayOfClass = { String[].class };
/* 105 */     Method method = clazz.getDeclaredMethod("main", arrayOfClass);
/*     */     
/* 107 */     Object[] arrayOfObject = { paramArrayOfString };
/* 108 */     method.invoke(null, arrayOfObject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?> loadClass(String paramString) {
/*     */     try {
/* 120 */       return Class.forName(paramString, true, 
/* 121 */           ClassLoader.getSystemClassLoader());
/* 122 */     } catch (Exception exception) {
/* 123 */       return null;
/*     */     } 
/*     */   } private static void flag(String paramString1, String paramString2) throws IOException {
/*     */     InputStream inputStream;
/* 127 */     VirtualMachine virtualMachine = attach(paramString1);
/*     */ 
/*     */     
/* 130 */     int i = paramString2.indexOf('=');
/* 131 */     if (i != -1) {
/* 132 */       String str1 = paramString2.substring(0, i);
/* 133 */       String str2 = paramString2.substring(i + 1);
/* 134 */       inputStream = ((HotSpotVirtualMachine)virtualMachine).setFlag(str1, str2);
/*     */     } else {
/* 136 */       String str; char c = paramString2.charAt(0);
/* 137 */       switch (c) {
/*     */         case '+':
/* 139 */           str = paramString2.substring(1);
/* 140 */           inputStream = ((HotSpotVirtualMachine)virtualMachine).setFlag(str, "1");
/*     */           break;
/*     */         case '-':
/* 143 */           str = paramString2.substring(1);
/* 144 */           inputStream = ((HotSpotVirtualMachine)virtualMachine).setFlag(str, "0");
/*     */           break;
/*     */         default:
/* 147 */           str = paramString2;
/* 148 */           inputStream = ((HotSpotVirtualMachine)virtualMachine).printFlag(str);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 153 */     drain(virtualMachine, inputStream);
/*     */   }
/*     */ 
/*     */   
/*     */   private static VirtualMachine attach(String paramString) {
/*     */     try {
/* 159 */       return VirtualMachine.attach(paramString);
/* 160 */     } catch (Exception exception) {
/* 161 */       String str = exception.getMessage();
/* 162 */       if (str != null) {
/* 163 */         System.err.println(paramString + ": " + str);
/*     */       } else {
/* 165 */         exception.printStackTrace();
/*     */       } 
/* 167 */       System.exit(1);
/* 168 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void drain(VirtualMachine paramVirtualMachine, InputStream paramInputStream) throws IOException {
/* 175 */     byte[] arrayOfByte = new byte[256];
/*     */     
/*     */     while (true) {
/* 178 */       int i = paramInputStream.read(arrayOfByte);
/* 179 */       if (i > 0) {
/* 180 */         String str = new String(arrayOfByte, 0, i, "UTF-8");
/* 181 */         System.out.print(str);
/*     */       } 
/* 183 */       if (i <= 0) {
/* 184 */         paramInputStream.close();
/* 185 */         paramVirtualMachine.detach();
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void usage(int paramInt) {
/* 192 */     Class<?> clazz = loadClass("sun.jvm.hotspot.tools.JInfo");
/* 193 */     boolean bool = (clazz != null) ? true : false;
/*     */     
/* 195 */     System.err.println("Usage:");
/* 196 */     if (bool) {
/* 197 */       System.err.println("    jinfo [option] <pid>");
/* 198 */       System.err.println("        (to connect to running process)");
/* 199 */       System.err.println("    jinfo [option] <executable <core>");
/* 200 */       System.err.println("        (to connect to a core file)");
/* 201 */       System.err.println("    jinfo [option] [server_id@]<remote server IP or hostname>");
/* 202 */       System.err.println("        (to connect to remote debug server)");
/* 203 */       System.err.println("");
/* 204 */       System.err.println("where <option> is one of:");
/* 205 */       System.err.println("    -flag <name>         to print the value of the named VM flag");
/* 206 */       System.err.println("    -flag [+|-]<name>    to enable or disable the named VM flag");
/* 207 */       System.err.println("    -flag <name>=<value> to set the named VM flag to the given value");
/* 208 */       System.err.println("    -flags               to print VM flags");
/* 209 */       System.err.println("    -sysprops            to print Java system properties");
/* 210 */       System.err.println("    <no option>          to print both of the above");
/* 211 */       System.err.println("    -h | -help           to print this help message");
/*     */     } else {
/* 213 */       System.err.println("    jinfo <option> <pid>");
/* 214 */       System.err.println("       (to connect to a running process)");
/* 215 */       System.err.println("");
/* 216 */       System.err.println("where <option> is one of:");
/* 217 */       System.err.println("    -flag <name>         to print the value of the named VM flag");
/* 218 */       System.err.println("    -flag [+|-]<name>    to enable or disable the named VM flag");
/* 219 */       System.err.println("    -flag <name>=<value> to set the named VM flag to the given value");
/* 220 */       System.err.println("    -h | -help           to print this help message");
/*     */     } 
/*     */     
/* 223 */     System.exit(paramInt);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jinfo\JInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */