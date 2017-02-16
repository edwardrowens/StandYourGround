package com.ede.standyourground.networking.framework;

import android.os.Handler;
import android.os.HandlerThread;

import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.networking.exchange.api.Exchange;
import com.ede.standyourground.networking.exchange.request.api.Request;
import com.ede.standyourground.networking.exchange.request.impl.CreateUnitRequest;
import com.ede.standyourground.networking.serialization.RuntimeTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
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

    private final BlockingQueue<Exchange> exchanges = new LinkedBlockingQueue<>();


    private final Gson GSON = new GsonBuilder().registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(Exchange.class)
            .registerSubtype(CreateUnitRequest.class)).create();

    private HandlerThread readerThread = new HandlerThread("ReaderThread");
    private HandlerThread writerThread = new HandlerThread("WriterThread");
    private Handler readHandler;
    private Handler writeHandler;

    private static NetworkingManager instance = new NetworkingManager();

    private Socket socket;


    private NetworkingManager() {
        readerThread.start();
        writerThread.start();
        readHandler = new Handler(readerThread.getLooper());
        writeHandler = new Handler(writerThread.getLooper());
    }

    public NetworkingManager getInstance() {
        return instance;
    }

    public void connect(final boolean isServer, final String ip) {
        readHandler.post(new Runnable() {
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
                        readHandler.post(this);
                    }
                } else if (!isServer) {
                    try {
                        socket = new Socket(ip, PORT);
                    } catch (IOException e) {
                        logger.e("Could not connect to server.", e);
                        readHandler.post(this);
                    }
                }
            }
        });
    }

    public void sendRequest(final Request request) {
        exchanges.add(request);
    }

    private void writeExchanges() {
        writeHandler.post(new Runnable() {
            @Override
            public void run() {
                if (exchanges.size() > 0) {
                    String requestString = GSON.toJson(exchanges.peek());
                    try {
                        IOUtils.write(requestString, socket.getOutputStream());
                    } catch (IOException e) {
                        logger.e("Problem in writing request to output stream", e);
                    }
                }
                writeHandler.post(this);
            }
        });
    }

    private void readExchanges() {
        // TODO
    }

    private void readRe

    public void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            logger.e("Problem closing socket", e);
        }
    }
}
