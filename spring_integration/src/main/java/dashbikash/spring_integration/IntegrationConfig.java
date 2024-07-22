package dashbikash.spring_integration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.graph.IntegrationGraphServer;
import org.springframework.integration.http.config.EnableIntegrationGraphController;

@Configuration
@EnableAutoConfiguration
@EnableIntegrationGraphController(allowedOrigins = {"*"})
public class IntegrationConfig {
//	@Bean
//	public MessageChannel fileChannel() {
//		return new DirectChannel();
//	}
//	
	@Bean
	@InboundChannelAdapter(value = "fileChannel",poller = @Poller(fixedDelay = "1000"))
	public FileReadingMessageSource fileReadingMessageSource() {
		FileReadingMessageSource readingMessageSource=new FileReadingMessageSource();
		readingMessageSource.setDirectory(new File(System.getenv("HOME")+"/tmp/source"));
		readingMessageSource.setAutoCreateDirectory(true);
		readingMessageSource.setFilter(new SimplePatternFileListFilter("*.csv"));
		return readingMessageSource;
	}
	@Bean
	@ServiceActivator(inputChannel =  "fileChannel")
	public FileWritingMessageHandler fileWritingMessageHandler() {
		FileWritingMessageHandler writingMessageHandler=new FileWritingMessageHandler(new File(System.getenv("HOME")+"/tmp/destination"));
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
	
}
