package com.miiskin.videolibraryproject.utils;

import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit.mime.TypedInput;

public class StreamUtils {

    // private static final int CLOSE_TRY_COUNT = 3;

    @Nullable
    public static String readFully(final TypedInput typedInput) {
        InputStream inputStream = null;
        try {
            inputStream = typedInput.in();

            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final byte[] buffer = new byte[1024];

            int n;
            while ((n = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, n);
            }

            return new String(baos.toByteArray());

        } catch (final IOException ioe) {
            ioe.printStackTrace();
            return null;

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*public static String streamToString(InputStream stream) {
        return streamToString(stream, "UTF-8");
    }*/

    /*public static String streamToString(InputStream stream, String encoding) {
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(stream, encoding), 65535);
            try {
                StringBuilder sb = new StringBuilder();
                char[] buffer = new char[4 * 1024];
                int n;

                while ((n = rd.read(buffer)) > -1) {
                    sb.append(buffer, 0, n);
                }
                return sb.toString();
            } finally {
                rd.close();
            }
        } catch (Throwable err) {
            throw new RuntimeException(err.getMessage(), err);
        }
    }*/

    /*public static boolean writeToFile(String data, File file) {
        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            osw.write(data);
            osw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeQuietly(osw);
        }
    }*/

    /*public static String readStringFromFile(File file) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

            String receiveString;
            StringBuilder sb = new StringBuilder();

            while ((receiveString = br.readLine()) != null) {
                sb.append(receiveString);
                sb.append('\n');
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeQuietly(br);
        }
    }*/

    /*public static boolean writeToFile(byte[] data, File file) {
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file, false));
            os.write(data);
            os.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeQuietly(os);
        }
    }*/

    /*public static byte[] readBytesFromFile(File file) {
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buff = new byte[8 * 1024];
            int i;
            while ((i = is.read(buff, 0, buff.length)) > 0) {
                baos.write(buff, 0, i);
            }
            return baos.size() == 0 ? null : baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(is);
        }
        return null;
    }*/

    /*private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            for (int i = 0; i < CLOSE_TRY_COUNT; i++) {
                try {
                    closeable.close();
                    return;
                } catch (IOException ignored) {
                }
            }
        }
    }*/

    private StreamUtils() {
    }

}
