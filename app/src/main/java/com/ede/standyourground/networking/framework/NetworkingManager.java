package com.ede.standyourground.networking.framework;

import android.os.Handler;
import android.os.HandlerThread;

import com.ede.standyourground.framework.Callback;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.networking.exchange.api.Exchange;
import com.ede.standyourground.networking.exchange.request.api.Request;
import com.ede.standyourground.networking.exchange.request.impl.CreateUnitRequest;
import com.ede.standyourground.networking.exchange.response.impl.OkResponse;
import com.ede.standyourground.networking.serialization.RuntimeTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
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

    private final BlockingQueue<Exchange> outgoingExchanges = new LinkedBlockingQueue<>();


    private final Gson GSON = new GsonBuilder().registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(Exchange.class)
            .registerSubtype(CreateUnitRequest.class)
            .registerSubtype(OkResponse.class)).create();

    private HandlerThread readerThread = new HandlerThread("ReaderThread");
    private HandlerThread writerThread = new HandlerThread("WriterThread");
    private Handler readHandler;
    private Handler writeHandler;

    private static NetworkingManager instance = new NetworkingManager();

    private static Socket socket;


    private NetworkingManager() {
        readerThread.start();
        writerThread.start();
        readHandler = new Handler(readerThread.getLooper());
        writeHandler = new Handler(writerThread.getLooper());
    }

    public static NetworkingManager getInstance() {
        return instance;
    }

    public void connect(final boolean isServer, final String ip, final Callback callback) {
        readHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isServer) {
                    logger.i("Starting server on port %d", PORT);
                    ServerSocket serverSocket = null;
                    try {
                        logger.d("creating server socket");
                        serverSocket = new ServerSocket(PORT);
                        logger.d("created server socket");
                    } catch (Exception e) {
                        readHandler.post(this);
                        logger.e("Could not establish server socket.", e);
                    }
                    try {
                        logger.d("accepting connections on %s:%d", serverSocket.getInetAddress().toString(), serverSocket.getLocalPort());
                        socket = serverSocket.accept();
                        logger.d("accepted connection");
                    } catch (Exception e) {
                        logger.e("Could not accept client connection.", e);
                        readHandler.post(this);
                    }
                    if (socket == null) {
                        readHandler.post(this);
                    }
                } else if (!isServer) {
                    try {
                        logger.i("Connecting to server on %s:%d", ip, PORT);
                        socket = new Socket();
                        socket.connect(new InetSocketAddress(ip, PORT), 10000);
                        logger.i("connected?");
                    } catch (Exception e) {
                        logger.e("Could not connect to server.", e);
                        readHandler.post(this);
                    }
                }

                if (socket.isConnected()) {
                    logger.i("Connection created. Starting read and write threads.");
                    callback.onSuccess();
                    readExchanges();
                    writeExchanges();
                }
            }
        });
    }

    public void sendRequest(final Request request) {
        outgoingExchanges.add(request);
    }

    private void writeExchanges() {
        writeHandler.post(new Runnable() {
            @Override
            public void run() {
                if (outgoingExchanges.size() > 0) {
                    String requestString = GSON.toJson(outgoingExchanges.peek());
                    if (requestString != null) {
                        try {
                            IOUtils.write(requestString, socket.getOutputStream());
                        } catch (IOException e) {
                            logger.e("Problem in writing exchange to output stream", e);
                        }
                        outgoingExchanges.poll();
                    }
                }
                writeHandler.post(this);
            }
        });
    }

    private void readExchanges() {
        readHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    Exchange exchange = GSON.fromJson(bufferedReader, Exchange.class);
                    if (exchange != null) {
                        logger.i("Handling exchange %s", exchange.getId().toString());
                        ExchangeHandlerUtil.handleExchange(exchange);
                    }
                } catch (IOException e) {
                    logger.e("Problem in reading exchange from input stream", e);
                }
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
