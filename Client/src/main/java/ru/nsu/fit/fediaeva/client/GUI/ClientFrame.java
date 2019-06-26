package ru.nsu.fit.fediaeva.client.GUI;

import ru.nsu.fit.fediaeva.client.ClientContext;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class ClientFrame extends JFrame {
    private JPanel loginFrame;
    private JPanel functionalFrame;
    private JLabel startMsg;
    private ClientContext clientContext;
    private Font font;
    private JTextArea showZone;

    public ClientFrame(ClientContext context) {
        clientContext = context;
        font = new Font("Consolas", Font.PLAIN, 18);
        startMsg = new JLabel();
        createLoginFrame();
    }

    private void createLoginFrame() {
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        loginFrame = new JPanel();
        GridBagLayout glb = new GridBagLayout();
        loginFrame.setLayout(glb);
        GridBagConstraints c = new GridBagConstraints();

        c.gridy = 0;
        c.gridx = 0;
        c.ipadx = 100;
        c.ipady = 15;
        JLabel hostText = new JLabel("Enter host:port");
        hostText.setFont(font);
        hostText.setHorizontalAlignment(JLabel.CENTER);
        loginFrame.add(hostText, c);
        JTextField host = new JTextField("localhost:8080");
        c.insets.set(0, 50, 0, 0);
        c.ipady = 5;
        c.gridy = 1;
        c.gridx = 0;
        c.ipadx = 200;
        loginFrame.add(host, c);
        c.insets.set(0, 0, 0, 0);
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 0;
        c.ipady = 15;
        c.ipadx = 200;
        JLabel loginText = new JLabel("Enter login");
        loginText.setHorizontalAlignment(JLabel.CENTER);
        loginText.setFont(font);
        loginFrame.add(loginText, c);

        JTextField login = new JTextField("admin");
        c.ipady = 5;
        c.gridx = 1;
        c.gridy = 1;
        loginFrame.add(login, c);

        c.ipady = 15;
        c.gridx = 1;
        c.gridy = 2;
        JLabel passText = new JLabel("Enter password");
        passText.setHorizontalAlignment(JLabel.CENTER);
        passText.setFont(font);
        loginFrame.add(passText, c);

        JPasswordField password = new JPasswordField("12345");
        c.ipady = 5;
        c.gridx = 1;
        c.gridy = 3;
        loginFrame.add(password, c);

        JButton connectBut = new JButton("CONNECT");
        c.gridx = 0;
        c.gridy = 2;
        c.ipady = 20;
        c.ipadx = 115;
        c.gridheight = 2;
        c.insets.set(20, 47, 0, 0);
        loginFrame.add(connectBut, c);
        HashMap loginMap = new HashMap<String, JTextField>() {{
            put("Hostname", host);
            put("Login", login);
            put("Password", password);
        }};
        getContentPane().add(loginFrame);
        connectBut.addMouseListener(new LoginListener(loginMap, clientContext, this));
        setPreferredSize(new Dimension(600, 600));
        setVisible(true);
        pack();
    }

    void addStartMsg(String msgStr) {
        loginFrame.remove(startMsg);
        GridBagConstraints c = new GridBagConstraints();
        c.ipady = 40;
        c.ipadx = 130;
        startMsg = new JLabel(msgStr);
        startMsg.setFont(font);
        startMsg.setHorizontalAlignment(JLabel.CENTER);
        c.gridx = 0;
        c.gridy = 5;
        loginFrame.add(startMsg, c);
        revalidate();
    }

    void createFunctionalFrame() {
        functionalFrame = new JPanel();
        getContentPane().removeAll();
        //functionalFrame.validate();
        setVisible(false);
        functionalFrame.repaint();
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        GridBagLayout glb = new GridBagLayout();
        functionalFrame.setLayout(glb);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 150;
        c.ipady = 20;
        JButton reqLogBut = new JButton("Show requests");
        reqLogBut.addMouseListener(new LogsListener(clientContext, this, "request"));
        functionalFrame.add(reqLogBut, c);

        c.gridy = 1;
        JButton respLogBut = new JButton("Show responses");
        respLogBut.addMouseListener(new LogsListener(clientContext, this, "response"));
        functionalFrame.add(respLogBut, c);

        c.gridy = 2;
        JButton timeBut = new JButton("Show time logs");
        timeBut.addMouseListener(new TimeListener(this, clientContext));
        functionalFrame.add(timeBut, c);

        c.gridy = 3;
        JButton reloadBut = new JButton("Reload server");
        reloadBut.addMouseListener(new ReloadListener(clientContext, this));
        functionalFrame.add(reloadBut, c);

        c.gridy = 4;
        JButton plugListBut = new JButton("Show plugin list");
        plugListBut.addMouseListener(new PluginListListener(this, clientContext));
        functionalFrame.add(plugListBut, c);

        c.gridy = 5;
        c.ipady = 5;
        c.insets.set(10, 0, 0, 0);
        JLabel load = new JLabel("Enter jar path for loading plugin");
        load.setHorizontalAlignment(JLabel.CENTER);
        load.setFont(new Font("Consolas", Font.PLAIN, 14));
        functionalFrame.add(load, c);
        c.insets.set(0, 0, 0, 0);
        c.gridy = 6;
        c.ipady = 5;
        c.ipadx = 300;
        JTextField jarPath = new JTextField();
        jarPath.setFont(new Font("Consolas", Font.PLAIN, 14));
        functionalFrame.add(jarPath, c);
        jarPath.addActionListener(new LoadPluginListener(clientContext, this, jarPath));

        c.gridx = 1;
        c.gridy = 0;
        c.ipadx = 350;
        c.ipady = 300;
        c.gridheight = 7;
        showZone = new JTextArea();
        JScrollPane scroll = new JScrollPane(showZone);
        functionalFrame.add(scroll, c);
        showZone.setFont(new Font("Consolas", Font.PLAIN, 12));
        getContentPane().add(functionalFrame);
        setPreferredSize(new Dimension(950, 800));
        setVisible(true);
        pack();
    }

    void setText(String body) {
        showZone.setText(body);
    }

}
