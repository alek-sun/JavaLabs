package ru.nsu.fit.fediaeva.client.GUI;

import ru.nsu.fit.fediaeva.client.ClientContext;
import ru.nsu.fit.fediaeva.client.clientRequestConstructors.RequestSender;
import ru.nsu.fit.fediaeva.server.exception.NotFound;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class TimeListener implements MouseListener {
    private ClientFrame frame;
    private ClientContext clientContext;

    TimeListener(ClientFrame frame, ClientContext clientContext) {
        this.frame = frame;
        this.clientContext = clientContext;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        RequestSender sender = new RequestSender(clientContext.getHostName(), clientContext.getPort());
        try {
            String body = sender.sendTimeRequest();
            frame.setText(body);
        } catch (IOException e1) {
            frame.setText("IOException");
        } catch (ProgramException e1) {
            frame.setText("Program exception");
        } catch (NotFound notFound) {
            frame.setText("NotFound exception");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
