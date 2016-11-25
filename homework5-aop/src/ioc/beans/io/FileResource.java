package ioc.beans.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileResource implements Resource {
    private String path = null;

    public FileResource(String path) {
        this.path = path;
    }

	@Override
	public InputStream getInputStream() throws IOException {
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		return is;
	}

}
