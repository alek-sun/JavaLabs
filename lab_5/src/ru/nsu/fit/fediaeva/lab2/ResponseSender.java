package ru.nsu.fit.fediaeva.lab2;

import ru.nsu.fit.fediaeva.lab2.exception.ProgramException;
import ru.nsu.fit.fediaeva.lab2.constructors.Response;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Sends response in OutputStream
 */
public class ResponseSender {
    private OutputStream os;

    public ResponseSender(OutputStream out) {
        os = out;
    }

    public void sendResponse(Response resp) throws ProgramException {
        try {
            os.write(("HTTP/1.1 " + resp.getDescription() + "\r\n" + resp.getHeadersString())
                    .getBytes());
            os.write(resp.getBody());
            os.close();
        } catch (IOException e) {
            throw new ProgramException();
        }
    }
}
