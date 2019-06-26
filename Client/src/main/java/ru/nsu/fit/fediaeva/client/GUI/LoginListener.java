package ru.nsu.fit.fediaeva.client.GUI;

import ru.nsu.fit.fediaeva.client.ClientContext;
import ru.nsu.fit.fediaeva.client.clientRequestConstructors.RequestSender;
import ru.nsu.fit.fediaeva.server.exception.NotFound;
import ru.nsu.fit.fediaeva.server.exception.ProgramException;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Map;

public class LoginListener implements MouseListener {
    private Map<String, JTextField> logMap;
    private ClientContext clientContext;
    private ClientFrame frame;
    private JLabel h;

    public LoginListener(Map<String, JTextField> loginMap, ClientContext cont, ClientFrame jFrame) {
        frame = jFrame;
        clientContext = cont;
        logMap = loginMap;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        String login = logMap.get("Login").getText();
        String passw = logMap.get("Password").getText();
        if (login.equals("") || passw.equals("")) {
            frame.addStartMsg("Fill all fields, please");
            frame.revalidate();
            return;
        }
        String hostPort = logMap.get("Hostname").getText();
        String[] hostArr = hostPort.split(":");
        try {
            String host = hostArr[0];
            int port = Integer.parseInt(hostArr[1]);
            clientContext.setHostName(host);
            clientContext.setPort(port);
            RequestSender sender = new RequestSender(host, port);
            if (!sender.authoriseConnect(login, passw)) {
                frame.addStartMsg("Access denied");
            } else {
                clientContext.setLogin(login);
                clientContext.setPassword(passw);
                frame.createFunctionalFrame();
            }
        } catch (IndexOutOfBoundsException exc) {
            frame.addStartMsg("Fill all fields, please");
        } catch (IOException e1) {
            frame.addStartMsg("IOException");
        } catch (ProgramException e1) {
            frame.addStartMsg("Program exception");
        } catch (NotFound notFound) {
            frame.addStartMsg("Host not found");
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
