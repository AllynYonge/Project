package com.king.reading.encyption.glide;

import android.util.Base64;

import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.google.common.io.ByteStreams;
import com.king.reading.common.utils.Check;
import com.king.reading.common.utils.EncryptUtil;
import com.king.reading.exception.SecretKeyMissException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by AllynYonge on 27/07/2017.
 */

public class EncryptionFileToStreamDecoder<T> implements ResourceDecoder<File, T> {
    private static final FileOpener DEFAULT_FILE_OPENER = new FileOpener();

    private ResourceDecoder<InputStream, T> streamDecoder;
    private final FileOpener fileOpener;
    private String secretKey;

    public EncryptionFileToStreamDecoder(ResourceDecoder<InputStream, T> streamDecoder) {
        this(streamDecoder, DEFAULT_FILE_OPENER);
    }

    // Exposed for testing.
    EncryptionFileToStreamDecoder(ResourceDecoder<InputStream, T> streamDecoder, FileOpener fileOpener) {
        this.streamDecoder = streamDecoder;
        this.fileOpener = fileOpener;
    }

    @Override
    public Resource<T> decode(File source, int width, int height) throws IOException {
        InputStream is = null;
        InputStream encryptStream = null;
        Resource<T> result = null;
        if (Check.isEmpty(secretKey)){
            new SecretKeyMissException();
        }
        try {
            is = fileOpener.open(source);
            encryptStream = new ByteArrayInputStream(EncryptUtil.desDecrypt(secretKey, Base64.decode(ByteStreams.toByteArray(is), Base64.DEFAULT)));
            result = streamDecoder.decode(encryptStream, width, height);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // Do nothing.
                }
            }
        }
        return result;
    }

    @Override
    public String getId() {
        return "";
    }

    public void setSecretKey(String secretKey){
        this.secretKey = secretKey;
    }

    // Visible for testing.
    static class FileOpener {
        public InputStream open(File file) throws FileNotFoundException {
            return new FileInputStream(file);
        }
    }
}
