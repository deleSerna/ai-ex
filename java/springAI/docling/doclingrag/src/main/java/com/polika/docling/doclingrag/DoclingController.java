package com.polika.docling.doclingrag;

import java.io.IOException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DoclingController {
  DoclingService doclingService;

  public DoclingController() {
    this.doclingService = new DoclingService();
  }

  /**
   * Convert the given local file to markdown using docling server.
   * Eg: http://localhost:8080/convertlocalfile?filepath="<patht-to-doclingrag>/doclingrag/src/main/resources/documents/story.pdf
   */
  @GetMapping("/convertlocalfile")
  String convertLocalFile(@RequestParam("filepath") String filePath) {
    try {
      return doclingService.convertLocalFileToMarkDown(filePath);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Convert the file from the URL to markdown using docling server.
   * Eg: http://localhost:8080/convertfilefromurl?pdfurl="https://arxiv.org/pdf/2501.17887"
   */
  @GetMapping("/convertfilefromurl")
  String convertHttpFile(@RequestParam("pdfurl") String pdfurl) {
    try {
      return doclingService.convertFileFromUrlToMarkDown(pdfurl);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
