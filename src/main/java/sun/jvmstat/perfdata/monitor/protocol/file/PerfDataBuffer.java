/*    */ package sun.jvmstat.perfdata.monitor.protocol.file;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.RandomAccessFile;
/*    */ import java.nio.MappedByteBuffer;
/*    */ import java.nio.channels.FileChannel;
/*    */ import sun.jvmstat.monitor.MonitorException;
/*    */ import sun.jvmstat.monitor.VmIdentifier;
/*    */ import sun.jvmstat.perfdata.monitor.AbstractPerfDataBuffer;
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
/*    */ public class PerfDataBuffer
/*    */   extends AbstractPerfDataBuffer
/*    */ {
/*    */   public PerfDataBuffer(VmIdentifier paramVmIdentifier) throws MonitorException {
/* 56 */     File file = new File(paramVmIdentifier.getURI());
/* 57 */     String str = paramVmIdentifier.getMode();
/*    */     
/*    */     try {
/* 60 */       FileChannel fileChannel = (new RandomAccessFile(file, str)).getChannel();
/* 61 */       MappedByteBuffer mappedByteBuffer = null;
/*    */       
/* 63 */       if (str.compareTo("r") == 0) {
/* 64 */         mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, (int)fileChannel.size());
/* 65 */       } else if (str.compareTo("rw") == 0) {
/* 66 */         mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0L, (int)fileChannel.size());
/*    */       } else {
/* 68 */         throw new IllegalArgumentException("Invalid mode: " + str);
/*    */       } 
/*    */       
/* 71 */       fileChannel.close();
/*    */       
/* 73 */       createPerfDataBuffer(mappedByteBuffer, 0);
/* 74 */     } catch (FileNotFoundException fileNotFoundException) {
/* 75 */       throw new MonitorException("Could not find " + paramVmIdentifier.toString());
/* 76 */     } catch (IOException iOException) {
/* 77 */       throw new MonitorException("Could not read " + paramVmIdentifier.toString());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\protocol\file\PerfDataBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */