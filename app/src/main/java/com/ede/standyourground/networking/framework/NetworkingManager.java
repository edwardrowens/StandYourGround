package com.ede.standyourground.networking.framework;

import android.os.Handler;
import android.os.HandlerThread;

import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.networking.serialization.RuntimeTypeAdapterFactory;
import com.ede.standyourground.networking.serialization.Serialized;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Eddie on 2/13/2017.
 */

public class NetworkingManager {
    private static Logger logger = new Logger(NetworkingManager.class);

    private static final int PORT = 8008;

    private final BlockingQueue<Serialized> requests = new LinkedBlockingQueue<>();

    private final Gson GSON = new GsonBuilder().registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(Serialized.class)).create();

    private HandlerThread networkThread = new HandlerThread("NetworkThread");
    private Handler handler;

    private static NetworkingManager instance = new NetworkingManager();

    private Socket socket;


    private NetworkingManager() {
        networkThread.start();
        handler = new Handler(networkThread.getLooper());
    }

    public NetworkingManager getInstance() {
        return instance;
    }

    public void connect(final boolean isServer, final String ip) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isServer) {
                    logger.i("Starting server on port %d", PORT);
                    ServerSocket serverSocket = null;
                    try {
                        serverSocket = new ServerSocket(PORT);
                    } catch (IOException e) {
                        logger.e("Could not establish server socket.", e);
                    }
                    try {
                        socket = serverSocket.accept();
                    } catch (IOException e) {
                        logger.e("Could not accept client connection.", e);
                    }
                    if (socket == null) {
                        handler.post(this);
                    }
                } else if (!isServer) {
                    try {
                        socket = new Socket(ip, PORT);
                    } catch (IOException e) {
                        logger.e("Could not connect to server.", e);
                        handler.post(this);
                    }
                }
            }
        });
    }

    public void sendRequest(final Serialized request) {
        requests.add(request);
    }

    private void handleRequests() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                String requestString = null;
                requestString = GSON.toJson(requests.peek());
                DataOutputStream dataOutputStream = null;
                try {
                    IOUtils.write(requestString, socket.getOutputStream());
                } catch (IOException e) {
                    logger.e("Problem in writing request to output stream", e);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        BufferedReader bufferedReader = null;
                        try {
                            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        } catch (IOException e) {
                            logger.e("Problem retrieving input stream from socket", e);
                        }

                        Type serializedType = new TypeToken<Serialized>(){}.getType();
                        Serialized serialized = GSON.fromJson(bufferedReader, serializedType);
                    }
                });
            }
        });
    }

    public void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            logger.e("Problem closing socket", e);
        }
    }
}
