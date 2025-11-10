package com.mipt.olgamallina;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TextFileAnalyzerTest {

    @Test
    void testAnalyzeFile() throws IOException {
        TextFileAnalyzer analyzer = new TextFileAnalyzer();

        Path testFile = Files.createTempFile("test", ".txt");
        List<String> lines = Arrays.asList("Hello world!", "This is test.");
        Files.write(testFile, lines);

        TextFileAnalyzer.AnalysisResult result = analyzer.analyzeFile(testFile.toString());


        assertEquals(2, result.getLineCount(), "Должно быть 2 строки");
        assertEquals(5, result.getWordCount(), "Слов должно быть 5 (Hello world! This is test.)");

        String content = Files.readString(testFile);
        assertEquals(content.length(), result.getCharCount(), "Неверное число символов");

        assertTrue(result.getCharFrequency().containsKey('\n') || System.lineSeparator().equals("\r\n"),
                "Должен учитываться перевод строки");
    }

    @Test
    void testSaveAnalysisResult() throws IOException {
        TextFileAnalyzer analyzer = new TextFileAnalyzer();

        Path input = Files.createTempFile("input", ".txt");
        Files.writeString(input, "One two\nthree");
        TextFileAnalyzer.AnalysisResult result = analyzer.analyzeFile(input.toString());

        Path outputFile = Files.createTempFile("analysis", ".txt");
        analyzer.saveAnalysisResult(result, outputFile.toString());

        assertTrue(Files.exists(outputFile), "Файл с результатами должен существовать");
        assertTrue(Files.size(outputFile) > 0, "Файл с результатами не должен быть пустым");

        String out = Files.readString(outputFile);
        assertTrue(out.contains("Lines: " + result.getLineCount()));
        assertTrue(out.contains("Words: " + result.getWordCount()));
        assertTrue(out.contains("Chars: " + result.getCharCount()));
    }
}
