/**
 * @Title: FileHelper.java
 * @Package: com.water.widget.File
 * @Description: TODO

 * @author: zhouli
 * @date: 2012-6-18
 * @version V1.0
 */

package com.water.widget.File;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @ClassName: FileHelper
 * @Description: TODO
 * @author: zhouli
 * @date: 2012-6-18
 * 
 */

public final class FileHelper {

  public static List<String> GetFileLines(String filePath) {
    List<String> values = new ArrayList<String>();

    FileInputStream fInputStream = null;
    BufferedReader fReader = null;
    try {
      fInputStream = new FileInputStream(filePath);
      fReader = new BufferedReader(new InputStreamReader(fInputStream));
      String str = null;
      while ((str = fReader.readLine()) != null) {
        values.add(str);
      }
    } catch (FileNotFoundException e) {
      LOG.error("Find file error", e);
    } catch (IOException e) {
      LOG.error("Read file error", e);
    } finally {
      if (fReader != null) {
        try {
          fReader.close();
        } catch (IOException e) {
          LOG.error("Close read error", e);
        } // try-catch
      } // if
    }

    return values;
  }

  private static Logger LOG = Logger.getLogger(FileHelper.class);
}
