package ru.nsu.fit.fediaeva.server;

import ru.nsu.fit.fediaeva.server.exception.ProgramException;
import ru.nsu.fit.fediaeva.server.matchers.MatcherTree;
import ru.nsu.fit.fediaeva.server.pipeline.MetricsPack;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

public class ServerContext {
    private ArrayList<ServerPlugin> plugins;
    private Server server;
    private MatcherTree matcherTree;
    private MetricsPack pack;
    private final Path dir;
    private final Path pluginList;

    ServerContext(int port) {
        server = new Server(port);
        plugins = new ArrayList<>();
        pack = new MetricsPack();
        dir = Paths.get("plugins");
        pluginList = Paths.get("pluginList.txt");
    }

    public void registerPlugins() throws ProgramException {
        FileInputStream fin;
        try {
            fin = new FileInputStream(pluginList.toFile());
        } catch (FileNotFoundException e) {
            throw new ProgramException();
        }
        Scanner sc = new Scanner(fin);
        sc.useDelimiter("[\r\n]");
        sc.tokens().map(name -> Paths.get(dir.toFile().getAbsolutePath() + "/" + name))
                .forEach(path -> {
                    try {
                        loadPlugin(path);
                    } catch (ProgramException ignored) {
                    }
                });
        try {
            fin.close();
        } catch (IOException ignored) {

        }
    }

    public ArrayList<ServerPlugin> getPlugins() {
        return plugins;
    }

    public Server getServer() {
        return server;
    }

    public MatcherTree getMatcherTree() {
        return matcherTree;
    }

    public void setMatcherTree(MatcherTree matcherTree) {
        this.matcherTree = matcherTree;
    }

    public void startServer() throws ProgramException {
        matcherTree = new MatcherTree();
        plugins = new ArrayList<>();
        registerPlugins();
        ServerContext cb = this;
        plugins.forEach(plugin -> {
            try {
                plugin.initialize(cb);
            } catch (ProgramException ignored) {
            }
        });
        try {
            server.start(matcherTree);
        } catch (IOException e) {
            throw new ProgramException();
        }
    }


    public void restartServer() {
        ServerContext loop = this;
        Thread thread = new Thread(() -> {
            synchronized (loop) {
                server.stop();
                pack = new MetricsPack();
                plugins.clear();
            }
            try {
                startServer();
            } catch (ProgramException e) {
                System.out.println("Restart server error");
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private Path copyPlugin(Path jarPath) throws ProgramException { // Копирует плагин в папку рядом с сервером
        Path newJar;
        try {
            newJar = Files.copy(jarPath, dir.resolve(jarPath.getFileName()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ProgramException();
        }
        return newJar;
    }

    private void loadPlugin(Path jarPath) throws ProgramException {  //  Загрузка зарегестрированного плагина на сервер
        // Открываем файл как JAR
        JarInputStream is;
        try {
            is = new JarInputStream(new FileInputStream(jarPath.toFile()));
        } catch (IOException e) {
            throw new ProgramException();
        }
        // Берем манифест
        Manifest manifest = is.getManifest();
        // Манифест вида
        // Ключ: значение
        // То есть мапа
        // Изначально мы можем взять только мапу <Object, Object>
        Map<Object, Object> valuesObj = manifest.getMainAttributes();
        // Нам нужна <String, String>
        // Мы знаем, что и ключ, и значение методом toString() преобразуются в String
        Map<String, String> values = new HashMap<>();
        valuesObj.forEach((Object key, Object value) -> {
            values.put(
                    key.toString(),
                    value.toString()
            );
        });
        // Ну или так
        //is.getManifest().getMainAttributes().forEach((a,b) -> values.put(a.toString(), b.toString()));
        // Берем нужную строку
        // Plugin: class.name
        String className = values.get("Plugin");
        // Переводим в URL
        URL url;
        try {
            url = jarPath.toUri().toURL();
        } catch (MalformedURLException e) {
            System.out.println("Cannot get URL");
            return;
        }
        // Создаем загрузчик класса
        ClassLoader loader = new URLClassLoader(new URL[]{url});
        Class<?> pluginClass;
        try {
            // Он может загружать произвольный класс по его имени
            // Здесь нам и пригодится строка из манифеста
            pluginClass = loader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new ProgramException();
        }
        Constructor<?> pluginConstr;
        try {
            // Берем конструктор без параметров для этого класса
            pluginConstr = pluginClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new ProgramException();
        }
        ServerPlugin plugin;
        try {
            // Создаем экземпляр класса
            plugin = (ServerPlugin) pluginConstr.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ProgramException();
        }
        //addPluginToList(newJar, plugin);
        // Пользуемся на здодровье
        plugins.add(plugin);
    }

    public void registerPlugin(Path oldJar) throws ProgramException { // Регистрирует плагин
        File fileList = pluginList.toFile();
        Path newJar = copyPlugin(oldJar);
        String pluginStr = newJar.getFileName().toString() + "\n";
        FileOutputStream fout;
        try {
            fout = new FileOutputStream(fileList, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new ProgramException();
        }
        try {
            fout.write(pluginStr.getBytes());
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ProgramException();
        }
    }

    public ArrayList<String> getPluginsNames() {
        ArrayList<String> names = new ArrayList<>();
        for (ServerPlugin p : plugins) {
            names.add(p.getName());
        }
        return names;
    }

    public MetricsPack getPack() {
        return pack;
    }
}
