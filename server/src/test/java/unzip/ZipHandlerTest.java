package unzip;

import java.io.File;

public class ZipHandlerTest {
  static String strPath = "C:/Users/GJen/Desktop/test/MvnQuickStart";

  public static void main(String[] args) {

    final File folder = new File(strPath);
    System.out.println(folder.list());
  }

  public static void listFilesForFolder(final File folder) {
    for (final File fileEntry : folder.listFiles()) {
      if (fileEntry.isDirectory()) {
        listFilesForFolder(fileEntry);
      } else {
        String entry = fileEntry.getAbsolutePath();
        System.out.println(entry);
        if (entry.contains("src\\test")) {
          String filePath = entry.substring(strPath.length(), entry.length());
          System.out.println(filePath);
        }
      }
    }
  }
}
