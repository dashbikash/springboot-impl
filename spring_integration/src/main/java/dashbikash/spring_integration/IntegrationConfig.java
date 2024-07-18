package dashbikash.spring_integration;

import java.io.File;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableAutoConfiguration
public class IntegrationConfig {
	@Bean
	public MessageChannel filChannel() {
		return new DirectChannel();
	}
	
	@Bean
	@InboundChannelAdapter(value = "filChannel",poller = @Poller(fixedDelay = "1000"))
	public FileReadingMessageSource fileReadingMessageSource() {
		FileReadingMessageSource readingMessageSource=new FileReadingMessageSource();
		readingMessageSource.setDirectory(new File(System.getenv("HOME")+"/tmp/source"));
		readingMessageSource.setAutoCreateDirectory(true);
		readingMessageSource.setFilter(new SimplePatternFileListFilter("*.csv"));
		return readingMessageSource;
	}
	@Bean
	@ServiceActivator(inputChannel =  "filChannel")
	public FileWritingMessageHandler fileWritingMessageHandler() {
		FileWritingMessageHandler writingMessageHandler=new FileWritingMessageHandler(new File(System.getenv("HOME")+"/tmp/destination"));
		writingMessageHandler.setAutoCreateDirectory(true);
		writingMessageHandler.setExpectReply(false);
		return writingMessageHandler;
	}
	
}
