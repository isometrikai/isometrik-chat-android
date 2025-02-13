package io.isometrik.samples.chat.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import io.tus.java.client.TusUpload;

public class TusAndroidUpload extends TusUpload {

    private String name;
    private long size;
    public TusAndroidUpload(String path) throws FileNotFoundException {

        File file = new File(path);
        if (file.exists()) {
            size = file.length();
            name = file.getName();
        }

        FileInputStream fileInputStream = new FileInputStream(file);
        setSize(size);
        setInputStream(fileInputStream);

        setFingerprint(String.format("%s-%d", path, size));

        Map<String, String> metadata = new HashMap<>();
        metadata.put("filename", name);
        setMetadata(metadata);
    }
}
