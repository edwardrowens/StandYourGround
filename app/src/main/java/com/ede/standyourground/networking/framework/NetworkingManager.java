package com.ede.standyourground.networking.framework;

import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.framework.WorldManager;
import com.ede.standyourground.networking.serialization.RuntimeTypeAdapterFactory;
import com.ede.standyourground.networking.serialization.Serialized;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Eddie on 2/13/2017.
 */

public class NetworkingManager {
    private static Logger logger = new Logger(NetworkingManager.class);

    private static final int PORT = 8008;

    private static NetworkingManager instance = new NetworkingManager();

    private WorldManager worldManager = WorldManager.getInstance();

    private Socket socket;

    private static final Gson GSON = new GsonBuilder().registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(Serialized.class)).create();

    private NetworkingManager() {
    }

    public NetworkingManager getInstance() {
        return instance;
    }

    public void startServer() {
        logger.i("Starting server on port %d", PORT);
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            logger.e("Could not establish server socket.", e);
        }

        while(true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                logger.e("Could not accept client connection.", e);
            }

        }
    }

    public void startClient(String ip) {
        logger.i("Starting client. Connecting to server on port %d and IP %s", PORT, ip);
    }
}
