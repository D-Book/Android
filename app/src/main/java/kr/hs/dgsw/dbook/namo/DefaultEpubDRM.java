package kr.hs.dgsw.dbook.namo;

import com.namoletter.drm.EpubDRM;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class DefaultEpubDRM extends EpubDRM {
    public static final String TAG = DefaultEpubDRM.class.getSimpleName();

    private ZipFile zipFile;

    public static class DefaultZipEntry extends EpubDRM.Entry {
        private ZipEntry entry;
        private InputStream is;

        public DefaultZipEntry(ZipEntry entry, InputStream is) {
            this.entry = entry;
            this.is = is;
        }

        @Override
        public long skip(long byteCount) throws IOException {
            return is.skip(byteCount);
        }

        @Override
        public int read() throws IOException {
            return is.read();
        }

        @Override
        public int read(byte[] buffer) throws IOException {
            return is.read(buffer);
        }

        @Override
        public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
            return is.read(buffer, byteOffset, byteCount);
        }

        @Override
        public void close() {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                }
            is = null;
        }

        @Override
        public long getSize() {
            if (entry != null)
                return entry.getSize();

            return 0;
        }
    }

    public DefaultEpubDRM(String filePath) throws IOException {
        zipFile = new ZipFile(filePath);
    }

    //	public void close() {
    //		if (zipFile != null) {
    //			try {
    //				zipFile.close();
    //			} catch (IOException e) {}
    //		}
    //		zipFile = null;
    //
    //	}

    @Override
    public Entry getEntry(String entryName) {
        if (destroyed)
            return null;

        ZipEntry entry = zipFile.getEntry(entryName);
        if (entry != null)
            try {
                return new DefaultZipEntry(entry, zipFile.getInputStream(entry));
            } catch (IOException e) {
            }

        return null;
    }

}
