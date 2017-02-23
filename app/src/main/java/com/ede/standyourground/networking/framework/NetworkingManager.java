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

import java.net.URI;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class NetworkingManager {
    private static Logger logger = new Logger(NetworkingManager.class);

    private static final int PORT = 8000;

    private final Gson GSON = new GsonBuilder().registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(Exchange.class)
            .registerSubtype(CreateUnitRequest.class)
            .registerSubtype(OkResponse.class)).create();

    private final HandlerThread networkingThread = new HandlerThread("ReaderThread");
    private final Handler networkingHandler;

    private static NetworkingManager instance = new NetworkingManager();

    private static Socket socket;


    private NetworkingManager() {
        networkingThread.start();
        networkingHandler = new Handler(networkingThread.getLooper());
    }

    public static NetworkingManager getInstance() {
        return instance;
    }

    public void connect(final String gameSessionId, final Callback callback) {
        networkingHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
//                    URI uri = new URI("https://standyourground.herokuapp.com/:" + PORT);
                    URI uri = new URI("http://192.168.0.102:8000/");
                    logger.i("Connecting to player on %s:%d", uri.getHost(), uri.getPort());
                    socket = IO.socket(uri);
                    socket.connect();
                } catch (Exception e) {
                    logger.e("Could not connect to player.", e);
                    callback.onFail();
                }
                socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        logger.i("Connected to server! Sending game session ID of %s", gameSessionId);
                        socket.send(gameSessionId);
                        socket.on("StartGame", new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {
                                logger.i("Game session is ready. Starting game.");
                                callback.onSuccess();
                            }
                        });
                    }
                });

                socket.on("error", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        callback.onFail();
                    }
                });

                socket.on("message", new Emitter.Listener() {
                    @Override
                    public void call(final Object... args) {
                        networkingHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Exchange exchange = GSON.fromJson((String) args[0], Exchange.class);
                                if (exchange != null) {
                                    logger.i("Handling exchange %s", exchange.getId().toString());
                                    ExchangeHandlerUtil.handleExchange(exchange);
                                }
                            }
                        });
                    }
                });

            }
        });
    }

    public void sendRequest(final Request request) {
        networkingHandler.post(new Runnable() {
            @Override
            public void run() {
                String requestString = GSON.toJson(request);
                if (requestString != null) {
                    logger.i("Sending request %s", request.getId().toString());
                    socket.send(requestString);
                }
            }
        });
    }

    public void closeConnection() {
        networkingThread.quit();
        socket.close();
    }
}
