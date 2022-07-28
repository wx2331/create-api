/*     */ package sun.tools.attach;
/*     */ 
/*     */ import com.sun.tools.attach.AgentLoadException;
/*     */ import com.sun.tools.attach.AttachNotSupportedException;
/*     */ import com.sun.tools.attach.AttachOperationFailedException;
/*     */ import com.sun.tools.attach.spi.AttachProvider;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Random;
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
/*     */ public class WindowsVirtualMachine
/*     */   extends HotSpotVirtualMachine
/*     */ {
/*     */   WindowsVirtualMachine(AttachProvider paramAttachProvider, String paramString) throws AttachNotSupportedException, IOException {
/*  48 */     super(paramAttachProvider, paramString);
/*     */     
/*     */     int i;
/*     */     try {
/*  52 */       i = Integer.parseInt(paramString);
/*  53 */     } catch (NumberFormatException numberFormatException) {
/*  54 */       throw new AttachNotSupportedException("Invalid process identifier");
/*     */     } 
/*  56 */     this.hProcess = openProcess(i);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  62 */       enqueue(this.hProcess, stub, (String)null, (String)null, new Object[0]);
/*  63 */     } catch (IOException iOException) {
/*  64 */       throw new AttachNotSupportedException(iOException.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void detach() throws IOException {
/*  69 */     synchronized (this) {
/*  70 */       if (this.hProcess != -1L) {
/*  71 */         closeProcess(this.hProcess);
/*  72 */         this.hProcess = -1L;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   InputStream execute(String paramString, Object... paramVarArgs) throws AgentLoadException, IOException {
/*  80 */     assert paramVarArgs.length <= 3;
/*     */ 
/*     */     
/*  83 */     int i = (new Random()).nextInt();
/*  84 */     String str = "\\\\.\\pipe\\javatool" + i;
/*  85 */     long l = createPipe(str);
/*     */ 
/*     */ 
/*     */     
/*  89 */     if (this.hProcess == -1L) {
/*  90 */       closePipe(l);
/*  91 */       throw new IOException("Detached from target VM");
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/*  96 */       enqueue(this.hProcess, stub, paramString, str, paramVarArgs);
/*     */ 
/*     */ 
/*     */       
/* 100 */       connectPipe(l);
/*     */ 
/*     */       
/* 103 */       PipedInputStream pipedInputStream = new PipedInputStream(l);
/*     */ 
/*     */       
/* 106 */       int j = readInt(pipedInputStream);
/* 107 */       if (j != 0) {
/*     */         
/* 109 */         String str1 = readErrorMessage(pipedInputStream);
/*     */         
/* 111 */         if (paramString.equals("load")) {
/* 112 */           throw new AgentLoadException("Failed to load agent library");
/*     */         }
/* 114 */         if (str1 == null) {
/* 115 */           throw new AttachOperationFailedException("Command failed in target VM");
/*     */         }
/* 117 */         throw new AttachOperationFailedException(str1);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 123 */       return pipedInputStream;
/*     */     }
/* 125 */     catch (IOException iOException) {
/* 126 */       closePipe(l);
/* 127 */       throw iOException;
/*     */     } 
/*     */   } static native void init(); static native byte[] generateStub(); static native long openProcess(int paramInt) throws IOException;
/*     */   static native void closeProcess(long paramLong) throws IOException;
/*     */   static native long createPipe(String paramString) throws IOException;
/*     */   static native void closePipe(long paramLong) throws IOException;
/*     */   static native void connectPipe(long paramLong) throws IOException;
/*     */   static native int readPipe(long paramLong, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;
/*     */   static native void enqueue(long paramLong, byte[] paramArrayOfbyte, String paramString1, String paramString2, Object... paramVarArgs) throws IOException;
/*     */   private class PipedInputStream extends InputStream { public PipedInputStream(long param1Long) {
/* 137 */       this.hPipe = param1Long;
/*     */     }
/*     */     private long hPipe;
/*     */     public synchronized int read() throws IOException {
/* 141 */       byte[] arrayOfByte = new byte[1];
/* 142 */       int i = read(arrayOfByte, 0, 1);
/* 143 */       if (i == 1) {
/* 144 */         return arrayOfByte[0] & 0xFF;
/*     */       }
/* 146 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public synchronized int read(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
/* 151 */       if (param1Int1 < 0 || param1Int1 > param1ArrayOfbyte.length || param1Int2 < 0 || param1Int1 + param1Int2 > param1ArrayOfbyte.length || param1Int1 + param1Int2 < 0)
/*     */       {
/* 153 */         throw new IndexOutOfBoundsException(); } 
/* 154 */       if (param1Int2 == 0) {
/* 155 */         return 0;
/*     */       }
/* 157 */       return WindowsVirtualMachine.readPipe(this.hPipe, param1ArrayOfbyte, param1Int1, param1Int2);
/*     */     }
/*     */     
/*     */     public void close() throws IOException {
/* 161 */       if (this.hPipe != -1L) {
/* 162 */         WindowsVirtualMachine.closePipe(this.hPipe);
/* 163 */         this.hPipe = -1L;
/*     */       } 
/*     */     } }
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
/*     */   static {
/* 191 */     System.loadLibrary("attach");
/* 192 */     init();
/* 193 */   } private static byte[] stub = generateStub();
/*     */   private volatile long hProcess;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\attach\WindowsVirtualMachine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */