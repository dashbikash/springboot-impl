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
import org.springframework.integration.file.transformer.FileToStringTransformer;
import org.springframework.integration.graph.IntegrationGraphServer;
import org.springframework.integration.http.config.EnableIntegrationGraphController;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
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

	private static final String CHANNEL_FILE_INPUT = "fileInputChannel";
	private static final String CHANNEL_FILE_PROCESS = "fileProcessingChannel";
	private static final String CHANNEL_FILE_OUTPUT = "fileOutputChannel";

	@Bean
	@InboundChannelAdapter(value = CHANNEL_FILE_INPUT,poller = @Poller(fixedDelay = "2000"))
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
	
	 @Splitter(inputChannel = "fileInputChannel", outputChannel = "splitFileChannel")
    public List<String> splitFile(Message<File> message) throws Exception {
        File file = message.getPayload();
        String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        // Split the file content by lines
        return List.of(content.split("\\r?\\n"));
    }

	@Transformer(inputChannel = CHANNEL_FILE_INPUT,outputChannel = CHANNEL_FILE_OUTPUT)
    public String transform(Message<File> message) throws Exception{
        File file = message.getPayload();
        
        // Perform your transformation logic here
        return  Files.toString(file, Charsets.UTF_8).toUpperCase();
    }


	@Bean
	@ServiceActivator(inputChannel =  CHANNEL_FILE_OUTPUT)
	public FileWritingMessageHandler fileWritingMessageHandler() {
		FileWritingMessageHandler writingMessageHandler=new FileWritingMessageHandler(new File(DESTINATION_DIR));
		writingMessageHandler.setAutoCreateDirectory(true);
		writingMessageHandler.setExpectReply(false);
		
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
