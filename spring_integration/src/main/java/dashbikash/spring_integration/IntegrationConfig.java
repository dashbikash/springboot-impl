package dashbikash.spring_integration;

import org.apache.curator.framework.recipes.locks.InterProcessMutex;
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
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.graph.IntegrationGraphServer;
import org.springframework.integration.http.config.EnableIntegrationGraphController;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import jakarta.annotation.PreDestroy;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableAutoConfiguration
@EnableIntegrationGraphController(allowedOrigins = {"*"})
public class IntegrationConfig {
	
	 private static final Logger LOG = LoggerFactory.getLogger(IntegrationConfig.class);

	
	@Bean
    public MessageChannel fileInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel fileOutputChannel() {
        return new DirectChannel();
    }

	
	@Value("${flow.source_dir}")
	private String SOURCE_DIR;
	@Value("${flow.destination_dir}")
	private String DESTINATION_DIR;
	
	@Autowired
	private FileNotExistFilter existFilter;

	@Autowired
    private InterProcessMutex lock;
	
	
	@Bean
	@InboundChannelAdapter(value = "fileInputChannel",poller = @Poller(fixedDelay = "1000"))
	public FileReadingMessageSource fileReadingMessageSource() {
		FileReadingMessageSource readingMessageSource=new FileReadingMessageSource();
		readingMessageSource.setDirectory(new File(SOURCE_DIR));
		readingMessageSource.setAutoCreateDirectory(true);
		CompositeFileListFilter<File> compositeFileListFilter=new CompositeFileListFilter<>();
		compositeFileListFilter.addFilter(new SimplePatternFileListFilter("*.csv"));
		compositeFileListFilter.addFilter(existFilter);
		
		readingMessageSource.setFilter(compositeFileListFilter);
		return readingMessageSource;
	}
	
	
	@Bean
    @ServiceActivator(inputChannel = "fileInputChannel")
    public MessageHandler fileProcessor() {
        return message -> {
            try {
                lock.acquire();
                // Process the file
                File inputFile = (File) message.getPayload();
                LOG.info(inputFile.getCanonicalPath());
                // Add your file processing logic here
                Thread.sleep(10000);
                // Pass the message to the output channel for writing
                fileOutputChannel().send(message);
                
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
					lock.release();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        };
    }


	@Bean
	@ServiceActivator(inputChannel =  "fileOutputChannel")
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
	
	@PreDestroy
    public void releaseLockOnShutdown() {
        try {
            if (lock.isAcquiredInThisProcess()) {
                lock.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	
	
}
