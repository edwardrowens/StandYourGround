package com.ede.standyourground.networking.framework.impl;

import com.ede.standyourground.app.service.ServiceGenerator;
import com.ede.standyourground.framework.Callback;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.networking.exchange.api.Exchange;
import com.ede.standyourground.networking.exchange.handler.impl.request.CreateUnitRequestHandler;
import com.ede.standyourground.networking.exchange.request.impl.CreateUnitRequest;
import com.ede.standyourground.networking.exchange.response.impl.OkResponse;
import com.ede.standyourground.networking.framework.api.NetworkingManager;
import com.google.gson.Gson;

import java.net.URI;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

@Singleton
public class NetworkingManagerImpl implements NetworkingManager {
    private static Logger logger = new Logger(NetworkingManagerImpl.class);

    private final Lazy<CreateUnitRequestHandler> createUnitRequestHandler;

    @Inject
    public NetworkingManagerImpl(Lazy<CreateUnitRequestHandler> createUnitRequestHandler) {
        this.createUnitRequestHandler = createUnitRequestHandler;
    }

    private static Socket socket;

    public void connect(final String gameSessionId, final Callback callback) {
        try {
            URI uri = new URI(ServiceGenerator.BASE_URL);
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
                socket.emit("handshake", gameSessionId);
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

        socket.on("CreateUnitRequest", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                CreateUnitRequest createUnitRequest = new Gson().fromJson((String) args[0], CreateUnitRequest.class);
                if (createUnitRequest != null) {
                    logger.i("Handling createUnitRequest %s", createUnitRequest.getId().toString());
                    createUnitRequestHandler.get().handle(createUnitRequest);
                }
            }
        });

        socket.on("OkResponse", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                OkResponse okResponse = new Gson().fromJson((String) args[0], OkResponse.class);
                logger.i("Received ok response for %s", okResponse.getId());
            }
        });
    }


    public void sendExchange(final Exchange exchange) {
        String requestString = new Gson().toJson(exchange);
        if (exchange != null) {
            logger.i("Sending %s exchange. Id %s", exchange.getType(), exchange.getId().toString());
            socket.emit("gameEvent", requestString);
        }
    }

    public void closeConnection() {
        socket.close();
    }
}
