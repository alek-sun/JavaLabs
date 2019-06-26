package ru.nsu.fit.fediaeva.server.handlers;

import ru.nsu.fit.fediaeva.server.exception.InternalServerError;
import ru.nsu.fit.fediaeva.server.exception.NotFound;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileServer {
    private Path rootPath = Paths.get("static");
    private String path;
    private File f;

    FileServer(String p) {
        path = p;
    }

    public File getFile() {
        return f;
    }

    private void findFile(String path) throws NotFound {
        Path correctPath = rootPath.resolve(path);      // add root dir in path
        f = correctPath.toFile();
        if (!f.exists()) {
            throw new NotFound();
        }
        if (f.isDirectory()) {
            f = correctPath.resolve("index.html")
                    .toFile();
            if (!f.exists()) {
                throw new NotFound();
            }
        }
    }

    byte[] getData() throws NotFound, InternalServerError {
        findFile(path);
        try {
            FileInputStream fileStream = new FileInputStream(f);
            byte[] b = fileStream.readAllBytes();
            fileStream.close();
            return b;
        } catch (IOException i) {
            throw new InternalServerError();
        }
    }

    String getMimeType() {
        try {
            return Files.probeContentType(f.toPath());
        } catch (IOException e) {
            return "text/html";
        }
    }
}
