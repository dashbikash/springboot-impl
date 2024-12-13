package dashbikash.spring_integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.file.transformer.FileToStringTransformer;
import org.springframework.integration.graph.IntegrationGraphServer;
import org.springframework.integration.http.config.EnableIntegrationGraphController;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableAutoConfiguration
@EnableIntegrationGraphController(allowedOrigins = {"*"})
public class IntegrationConfig {
	
	 private static final Logger LOG = LoggerFactory.getLogger(IntegrationConfig.class);
	
	@Value("${flow.source_dir}")
	private String SOURCE_DIR;
	
	@Value("${flow.destination_dir}")
	private String DESTINATION_DIR;
	
	@Autowired
	private FileNotExistFilter existFilter;

	private static final String FILE_IN_CHANNEL = "fileInChannel";
	private static final String FILE_SPLIT_CHANNEL = "splitFileChannel";
	private static final String FILE_OUTPUT_CHANNEL = "fileOutputChannel";

	@Bean
	@InboundChannelAdapter(value = FILE_IN_CHANNEL,poller = @Poller(fixedDelay = "2000"))
	public FileReadingMessageSource fileReadingMessageSource() {
		FileReadingMessageSource readingMessageSource=new FileReadingMessageSource();
		readingMessageSource.setDirectory(new File(SOURCE_DIR));
		readingMessageSource.setAutoCreateDirectory(true);
		
		CompositeFileListFilter<File> compositeFileListFilter=new CompositeFileListFilter<>();
		compositeFileListFilter.addFilter(new SimplePatternFileListFilter("*.txt"));
		compositeFileListFilter.addFilter(existFilter);
		
		readingMessageSource.setFilter(compositeFileListFilter);
		return readingMessageSource;
	}
	
	@Splitter(inputChannel = FILE_IN_CHANNEL, outputChannel = FILE_SPLIT_CHANNEL)
    public List<String> splitFile(Message<File> message) throws Exception {
        File file = message.getPayload();
        
        return Files.readLines(file,Charsets.UTF_8);
    }

	@Transformer(inputChannel = FILE_SPLIT_CHANNEL,outputChannel = FILE_OUTPUT_CHANNEL)
    public byte[] transform(Message<List<String>> message) throws Exception{
		List<String> lines = message.getPayload();
		
        // Perform your transformation logic here
        return String.join("\n", lines.parallelStream().map(String::toUpperCase).toList()).getBytes();
    }


	@Bean
	@ServiceActivator(inputChannel =  FILE_OUTPUT_CHANNEL)
	public FileWritingMessageHandler fileWritingMessageHandler() {
		FileWritingMessageHandler writingMessageHandler=new FileWritingMessageHandler(new File(DESTINATION_DIR));
		writingMessageHandler.setAutoCreateDirectory(true);
		writingMessageHandler.setExpectReply(false);
		writingMessageHandler.setFileExistsMode(FileExistsMode.APPEND); // Ensure lines are appended to the file
		writingMessageHandler.setAppendNewLine(true); // Ensure each line is written on a new line

		
		return writingMessageHandler;
	}
	
	@Bean
	public IntegrationGraphServer integrationGraphServer()
	{
		IntegrationGraphServer server=new IntegrationGraphServer();
		server.setAdditionalPropertiesCallback(namedComponent -> {
            Map<String, Object> properties = null;
            if (namedComponent instanceof SmartLifecycle) {
                SmartLifecycle smartLifecycle = (SmartLifecycle) namedComponent;
                properties = new HashMap<>();
                properties.put("auto-startup", smartLifecycle.isAutoStartup());
                properties.put("running", smartLifecycle.isRunning());
            }
            return properties;
        });
		return server;
	}

    @Bean
    @ServiceActivator(inputChannel = "errorChannel")
    public MessageHandler errorHandler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) {
                LOG.error("Global Error handling message: " + message.getPayload());
            }
        };
    }

	
}
