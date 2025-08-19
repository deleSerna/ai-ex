# Read Me First
This is sample mcp server project. It exposes 2 tools 
 - One is a basic "EchoToolService" which does not do much.
 - 2nd one connect to the Perplexity chat model and answer the user input.
This MCP server only exposes STDIO transport mechanism and hence client can only connect to
the MCP server using the STDIO transport.

Supporting the Perplexity was not straightforward as expected because constructor injection for
openAiChatModel for the perplexity chat model did not work. Most probably this could be due to
the ambiguity to choose between Perplexity and OpenAI chat model.
Hence, created the chat model explicitly for the Perplexity and exposed it as `primary bean` in the McpServerStdioApplication class.

# Run
 - Build  the project using `./gradlew clean build`
 -  Use the generated `build/libs/mcp-server_stdio-0.0.1-SNAPSHOT.jar` in the MCP client.
   - For eg: If you are using  [MCP inspector](https://modelcontextprotocol.io/legacy/tools/inspector)
     as the MCP client, then start the inspector using `npx @modelcontextprotocol/inspector` and
     provided `java` as the command and  `-jar replace-with-full-path/mcp-server_stdio/build/libs/mcp-server_stdio-0.0.1-SNAPSHOT.jar` as the argument.

 
### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.4/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.4/gradle-plugin/packaging-oci-image.html)
* [Model Context Protocol Server](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-server-boot-starter-docs.html)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

