package com.polika.docling.doclingrag;

import ai.docling.serve.api.DoclingServeApi;
import ai.docling.serve.api.convert.request.ConvertDocumentRequest;
import ai.docling.serve.api.convert.request.options.ConvertDocumentOptions;
import ai.docling.serve.api.convert.request.options.OutputFormat;
import ai.docling.serve.api.convert.request.source.FileSource;
import ai.docling.serve.api.convert.request.source.HttpSource;
import ai.docling.serve.api.convert.request.target.InBodyTarget;
import ai.docling.serve.api.convert.response.ConvertDocumentResponse;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class DoclingService {
  private static final String DOCLING_SERVER_URL = "http://0.0.0.0:5001/";
  private final DoclingServeApi api;

  public DoclingService() {
    this.api =
        DoclingServeApi.builder()
            .baseUrl(DOCLING_SERVER_URL)
            .logRequests() // log HTTP requests
            .logResponses() // log HTTP responses
            .prettyPrint() // pretty-print JSON requests/responses
            .build();
  }

  /**
   * Convert the given local file to markdown using docling server.
   */
  public String convertLocalFileToMarkDown(String filePath) throws IOException {
    Path pdfPath = Path.of(filePath);
    byte[] bytes = Files.readAllBytes(pdfPath);
    String base64 = Base64.getEncoder().encodeToString(bytes);
    String[] filePaths = filePath.split(File.separator);

    FileSource fileSource =
        FileSource.builder().filename(filePaths[filePaths.length - 1]).base64String(base64).build();

    ConvertDocumentRequest request =
        ConvertDocumentRequest.builder()
            .source(fileSource)
            .options(
                ConvertDocumentOptions.builder()
                    .toFormat(OutputFormat.MARKDOWN)
                    .includeImages(true)
                    .build())
            .target(InBodyTarget.builder().build())
            .build();

    ConvertDocumentResponse response = api.convertSource(request);
    return response.getDocument().getMarkdownContent();
  }

  /**
   * Convert the file from the URL to markdown using docling server.
   *
   */
  public String convertFileFromUrlToMarkDown(String url) throws IOException {
    ConvertDocumentRequest request =
        ConvertDocumentRequest.builder()
            .source(HttpSource.builder().url(URI.create(url)).build())
            .options(
                ConvertDocumentOptions.builder()
                    .toFormat(OutputFormat.MARKDOWN)
                    .includeImages(true)
                    .build())
            .target(InBodyTarget.builder().build())
            .build();

    ConvertDocumentResponse response = api.convertSource(request);
    return response.getDocument().getMarkdownContent();
  }
}
