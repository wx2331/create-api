/*     */ package sun.tools.jstack;
/*     */ 
/*     */ import com.sun.tools.attach.VirtualMachine;
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
/*     */ public class JStack
/*     */ {
/*     */   public static void main(String[] paramArrayOfString) throws Exception {
/*  44 */     if (paramArrayOfString.length == 0) {
/*  45 */       usage(1);
/*     */     }
/*     */     
/*  48 */     boolean bool1 = false;
/*  49 */     boolean bool2 = false;
/*  50 */     boolean bool3 = false;
/*     */ 
/*     */     
/*  53 */     byte b = 0;
/*  54 */     while (b < paramArrayOfString.length) {
/*  55 */       String str = paramArrayOfString[b];
/*  56 */       if (!str.startsWith("-")) {
/*     */         break;
/*     */       }
/*  59 */       if (str.equals("-help") || str.equals("-h")) {
/*  60 */         usage(0);
/*     */       }
/*  62 */       else if (str.equals("-F")) {
/*  63 */         bool1 = true;
/*     */       
/*     */       }
/*  66 */       else if (str.equals("-m")) {
/*  67 */         bool2 = true;
/*     */       }
/*  69 */       else if (str.equals("-l")) {
/*  70 */         bool3 = true;
/*     */       } else {
/*  72 */         usage(1);
/*     */       } 
/*     */ 
/*     */       
/*  76 */       b++;
/*     */     } 
/*     */ 
/*     */     
/*  80 */     if (bool2) {
/*  81 */       bool1 = true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  86 */     int i = paramArrayOfString.length - b;
/*  87 */     if (i == 0 || i > 2) {
/*  88 */       usage(1);
/*     */     }
/*  90 */     if (i == 2) {
/*  91 */       bool1 = true;
/*     */     
/*     */     }
/*  94 */     else if (!paramArrayOfString[b].matches("[0-9]+")) {
/*  95 */       bool1 = true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 100 */     if (bool1) {
/*     */       
/* 102 */       String[] arrayOfString = new String[i];
/* 103 */       for (byte b1 = b; b1 < paramArrayOfString.length; b1++) {
/* 104 */         arrayOfString[b1 - b] = paramArrayOfString[b1];
/*     */       }
/* 106 */       runJStackTool(bool2, bool3, arrayOfString);
/*     */     } else {
/*     */       
/* 109 */       String arrayOfString[], str = paramArrayOfString[b];
/*     */       
/* 111 */       if (bool3) {
/* 112 */         arrayOfString = new String[] { "-l" };
/*     */       } else {
/* 114 */         arrayOfString = new String[0];
/*     */       } 
/* 116 */       runThreadDump(str, arrayOfString);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void runJStackTool(boolean paramBoolean1, boolean paramBoolean2, String[] paramArrayOfString) throws Exception {
/* 123 */     Class<?> clazz = loadSAClass();
/* 124 */     if (clazz == null) {
/* 125 */       usage(1);
/*     */     }
/*     */ 
/*     */     
/* 129 */     if (paramBoolean1) {
/* 130 */       paramArrayOfString = prepend("-m", paramArrayOfString);
/*     */     }
/* 132 */     if (paramBoolean2) {
/* 133 */       paramArrayOfString = prepend("-l", paramArrayOfString);
/*     */     }
/*     */     
/* 136 */     Class[] arrayOfClass = { String[].class };
/* 137 */     Method method = clazz.getDeclaredMethod("main", arrayOfClass);
/*     */     
/* 139 */     Object[] arrayOfObject = { paramArrayOfString };
/* 140 */     method.invoke(null, arrayOfObject);
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
/*     */   private static Class<?> loadSAClass() {
/*     */     try {
/* 153 */       return Class.forName("sun.jvm.hotspot.tools.JStack", true, 
/* 154 */           ClassLoader.getSystemClassLoader());
/* 155 */     } catch (Exception exception) {
/* 156 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void runThreadDump(String paramString, String[] paramArrayOfString) throws Exception {
/* 161 */     VirtualMachine virtualMachine = null;
/*     */     try {
/* 163 */       virtualMachine = VirtualMachine.attach(paramString);
/* 164 */     } catch (Exception exception) {
/* 165 */       String str = exception.getMessage();
/* 166 */       if (str != null) {
/* 167 */         System.err.println(paramString + ": " + str);
/*     */       } else {
/* 169 */         exception.printStackTrace();
/*     */       } 
/* 171 */       if (exception instanceof com.sun.tools.attach.AttachNotSupportedException && 
/* 172 */         loadSAClass() != null) {
/* 173 */         System.err.println("The -F option can be used when the target process is not responding");
/*     */       }
/*     */       
/* 176 */       System.exit(1);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 181 */     InputStream inputStream = ((HotSpotVirtualMachine)virtualMachine).remoteDataDump((Object[])paramArrayOfString);
/*     */ 
/*     */     
/* 184 */     byte[] arrayOfByte = new byte[256];
/*     */     
/*     */     while (true) {
/* 187 */       int i = inputStream.read(arrayOfByte);
/* 188 */       if (i > 0) {
/* 189 */         String str = new String(arrayOfByte, 0, i, "UTF-8");
/* 190 */         System.out.print(str);
/*     */       } 
/* 192 */       if (i <= 0) {
/* 193 */         inputStream.close();
/* 194 */         virtualMachine.detach();
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   } private static String[] prepend(String paramString, String[] paramArrayOfString) {
/* 199 */     String[] arrayOfString = new String[paramArrayOfString.length + 1];
/* 200 */     arrayOfString[0] = paramString;
/* 201 */     System.arraycopy(paramArrayOfString, 0, arrayOfString, 1, paramArrayOfString.length);
/* 202 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void usage(int paramInt) {
/* 207 */     System.err.println("Usage:");
/* 208 */     System.err.println("    jstack [-l] <pid>");
/* 209 */     System.err.println("        (to connect to running process)");
/*     */     
/* 211 */     if (loadSAClass() != null) {
/* 212 */       System.err.println("    jstack -F [-m] [-l] <pid>");
/* 213 */       System.err.println("        (to connect to a hung process)");
/* 214 */       System.err.println("    jstack [-m] [-l] <executable> <core>");
/* 215 */       System.err.println("        (to connect to a core file)");
/* 216 */       System.err.println("    jstack [-m] [-l] [server_id@]<remote server IP or hostname>");
/* 217 */       System.err.println("        (to connect to a remote debug server)");
/*     */     } 
/*     */     
/* 220 */     System.err.println("");
/* 221 */     System.err.println("Options:");
/*     */     
/* 223 */     if (loadSAClass() != null) {
/* 224 */       System.err.println("    -F  to force a thread dump. Use when jstack <pid> does not respond (process is hung)");
/*     */       
/* 226 */       System.err.println("    -m  to print both java and native frames (mixed mode)");
/*     */     } 
/*     */     
/* 229 */     System.err.println("    -l  long listing. Prints additional information about locks");
/* 230 */     System.err.println("    -h or -help to print this help message");
/* 231 */     System.exit(paramInt);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstack\JStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */