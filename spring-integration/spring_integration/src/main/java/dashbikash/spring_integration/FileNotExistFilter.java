package dashbikash.spring_integration;


import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.file.filters.AbstractFileListFilter;
import org.springframework.stereotype.Component;

@Component
public class FileNotExistFilter extends AbstractFileListFilter<File> {

	
	@Value("${flow.destination_dir:/tmp/destination}")
	private String DESTINATION_DIR;

    @Override
    public boolean accept(File sourceFile) {
        // Check if the file does not exist in the destination directory
        File destFile = new File(DESTINATION_DIR, sourceFile.getName());

        return !destFile.exists();
    }

}
