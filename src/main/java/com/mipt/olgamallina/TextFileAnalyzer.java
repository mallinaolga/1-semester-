package com.mipt.olgamallina;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TextFileAnalyzer {

    public static class AnalysisResult {
        private final long lineCount;
        private final long wordCount;
        private final long charCount;
        private final Map<Character, Long> charFrequency;

        public AnalysisResult(long lineCount, long wordCount, long charCount,
                              Map<Character, Long> charFrequency) {
            this.lineCount = lineCount;
            this.wordCount = wordCount;
            this.charCount = charCount;
            this.charFrequency = Collections.unmodifiableMap(charFrequency);
        }

        public long getLineCount() { return lineCount; }
        public long getWordCount() { return wordCount; }
        public long getCharCount() { return charCount; }
        public Map<Character, Long> getCharFrequency() { return charFrequency; }

        @Override
        public String toString() {
            return "lines=" + lineCount + ", words=" + wordCount + ", chars=" + charCount;
        }
    }

    public AnalysisResult analyzeFile(String filePath) throws IOException {
        long lines = 0, words = 0, chars = 0;
        Map<Character, Long> freq = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            int code;
            boolean inWord = false;
            boolean seenAny = false;
            int last = -1;

            while ((code = br.read()) != -1) {
                seenAny = true;
                chars++;
                char c = (char) code;

                freq.merge(c, 1L, Long::sum);


                if (c == '\n' || c == '\r') lines++;


                if (Character.isWhitespace(c)) {
                    inWord = false;
                } else if (!inWord) {
                    words++;
                    inWord = true;
                }

                last = code;
            }

            if (seenAny && last != '\n' && last != '\r') {
                lines++;
            }
        }

        return new AnalysisResult(lines, words, chars, freq);
    }

    public void saveAnalysisResult(AnalysisResult result, String outputPath) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {
            bw.write("Lines: " + result.getLineCount());
            bw.newLine();
            bw.write("Words: " + result.getWordCount());
            bw.newLine();
            bw.write("Chars: " + result.getCharCount());
            bw.newLine();
            bw.write("Char frequency:");
            bw.newLine();
            for (Map.Entry<Character, Long> e : result.getCharFrequency().entrySet()) {
                char c = e.getKey();
                String printable = switch (c) {
                    case '\n' -> "\\n";
                    case '\r' -> "\\r";
                    case '\t' -> "\\t";
                    default -> String.valueOf(c);
                };
                bw.write("'" + printable + "': " + e.getValue());
                bw.newLine();
            }
        }
    }
}
