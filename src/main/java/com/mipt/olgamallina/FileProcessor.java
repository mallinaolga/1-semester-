package com.mipt.olgamallina;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardOpenOption.*;

public class FileProcessor {

    public List<Path> splitFile(String sourcePath, String outputDir, int partSize) throws IOException {
        if (partSize <= 0) {
            throw new IllegalArgumentException("partSize must be > 0");
        }

        Path src = Paths.get(sourcePath);
        if (!Files.exists(src)) {
            throw new NoSuchFileException("Source file not found: " + sourcePath);
        }

        Path outDir = Paths.get(outputDir);
        Files.createDirectories(outDir);

        List<Path> parts = new ArrayList<>();
        String baseName = src.getFileName().toString();

        try (FileChannel in = FileChannel.open(src, READ)) {
            long remainingInFile = in.size();
            long position = 0L;

            int partIndex = 1;
            ByteBuffer buffer = ByteBuffer.allocate(Math.min(partSize, 64 * 1024));

            while (remainingInFile > 0) {
                long bytesInThisPart = Math.min(remainingInFile, (long) partSize);
                Path partPath = outDir.resolve(baseName + ".part" + partIndex);

                try (FileChannel out = FileChannel.open(partPath, CREATE, TRUNCATE_EXISTING, WRITE)) {
                    long toWrite = bytesInThisPart;
                    long pos = position;

                    while (toWrite > 0) {
                        buffer.clear();
                        int read = in.read(buffer, pos);
                        if (read == -1) break;
                        buffer.flip();

                        int writeNow = (int) Math.min(toWrite, buffer.remaining());
                        int oldLimit = buffer.limit();
                        buffer.limit(buffer.position() + writeNow);
                        out.write(buffer);
                        buffer.limit(oldLimit);

                        pos += writeNow;
                        toWrite -= writeNow;
                    }
                }

                parts.add(partPath);
                position += bytesInThisPart;
                remainingInFile -= bytesInThisPart;
                partIndex++;
            }
        }

        return parts;
    }


    public void mergeFiles(List<Path> partPaths, String outputPath) throws IOException {
        if (partPaths == null || partPaths.isEmpty()) {
            throw new IllegalArgumentException("partPaths is empty");
        }

        Path out = Paths.get(outputPath);
        if (out.getParent() != null) {
            Files.createDirectories(out.getParent());
        }

        try (FileChannel outCh = FileChannel.open(out, CREATE, TRUNCATE_EXISTING, WRITE)) {
            ByteBuffer buffer = ByteBuffer.allocate(64 * 1024);

            for (Path part : partPaths) {
                if (!Files.exists(part)) {
                    throw new NoSuchFileException("Part not found: " + part);
                }
                try (FileChannel inCh = FileChannel.open(part, READ)) {
                    buffer.clear();
                    int read;
                    while ((read = inCh.read(buffer)) != -1) {
                        if (read == 0) {
                            buffer.flip();
                            if (buffer.hasRemaining()) {
                                outCh.write(buffer);
                            }
                            buffer.compact();
                            continue;
                        }
                        buffer.flip();
                        outCh.write(buffer);
                        buffer.compact();
                    }
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        outCh.write(buffer);
                    }
                    buffer.clear();
                }
            }
        }
    }
}
