package com.mipt.olgamallina;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class FileProcessorTest {

    @Test
    void testSplitAndMergeFile() throws IOException {
        FileProcessor processor = new FileProcessor();

        Path testFile = Files.createTempFile("test", ".dat");
        byte[] testData = new byte[1500];
        new Random().nextBytes(testData);
        Files.write(testFile, testData);

        String outputDir = Files.createTempDirectory("parts").toString();
        List<Path> parts = processor.splitFile(testFile.toString(), outputDir, 500);

        assertEquals(3, parts.size(), "Должно получиться 3 части");
        for (int i = 0; i < parts.size(); i++) {
            Path p = parts.get(i);
            assertTrue(Files.exists(p), "Часть должна существовать: " + p);
            assertTrue(p.getFileName().toString().endsWith(".part" + (i + 1)),
                    "Имя части должно быть *.part" + (i + 1));
        }
        assertEquals(500, Files.size(parts.get(0)));
        assertEquals(500, Files.size(parts.get(1)));
        assertEquals(500, Files.size(parts.get(2)));

        Path mergedFile = Files.createTempFile("merged", ".dat");
        processor.mergeFiles(new ArrayList<>(parts), mergedFile.toString());

        assertArrayEquals(Files.readAllBytes(testFile), Files.readAllBytes(mergedFile),
                "Исходный и объединённый файлы должны совпадать");
    }
}
