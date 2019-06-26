package ru.nsu.fit.fediaeva.client.GUI;

import ru.nsu.fit.fediaeva.client.ClientContext;
import ru.nsu.fit.fediaeva.client.clientRequestConstructors.RequestSender;
import ru.nsu.fit.fediaeva.server.exception.NotFound;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoadPluginListener implements ActionListener {
    private ClientContext clientContext;
    private ClientFrame frame;
    private JTextField jarPath;

    public LoadPluginListener(ClientContext clientContext, ClientFrame frame, JTextField jarPath) {
        this.clientContext = clientContext;
        this.frame = frame;
        this.jarPath = jarPath;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        RequestSender sender = new RequestSender(clientContext.getHostName(), clientContext.getPort());
        try {
            String res = sender.sendLoadPluginRequest(jarPath.getText());
            frame.setText(res);
        } catch (IOException e1) {
            frame.setText("IOException");
        } catch (ProgramException e1) {
            frame.setText("Program exception");
        } catch (NotFound notFound) {
            frame.setText("NotFound exception");
        }
    }
}
