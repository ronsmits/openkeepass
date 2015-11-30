package de.slackspace.openkeepass.stream;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static org.junit.Assert.*;

/**
 * Created by Ron on 30-11-2015.
 */
public class HashedBlockOutputStreamTest {

    public static final String TEST_STRING = "Dit is een test";

    @Test
    public void testWrite() throws Exception {

        byte[] buffer = new byte[1024*1024];

        System.out.println("length of test string "+TEST_STRING.length());
        File file = new File("target/testfile.txt");
        System.out.println(file.getAbsolutePath());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        HashedBlockOutputStream outputStream = new HashedBlockOutputStream(fileOutputStream);
        outputStream.write(TEST_STRING.getBytes("UTF-8"));
        outputStream.close();

        FileInputStream fileInputStream = new FileInputStream(file);
        HashedBlockInputStream hashedBlockInputStream = new HashedBlockInputStream(fileInputStream);
        int read = hashedBlockInputStream.read(buffer);
        System.out.println("read "+read);
        String s = new String(buffer, "UTF-8");
        assertEquals("string should be equal", TEST_STRING, s);
    }
}