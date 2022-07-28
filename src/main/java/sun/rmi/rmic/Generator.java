package sun.rmi.rmic;

import java.io.File;
import sun.tools.java.ClassDefinition;

public interface Generator {
  boolean parseArgs(String[] paramArrayOfString, Main paramMain);
  
  void generate(BatchEnvironment paramBatchEnvironment, ClassDefinition paramClassDefinition, File paramFile);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\Generator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */