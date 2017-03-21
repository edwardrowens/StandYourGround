package com.ede.standyourground.networking.impl;

import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.transmit.Callback;
import com.ede.standyourground.networking.api.NetworkingHandler;
import com.ede.standyourground.networking.api.exchange.payload.Exchange;
import com.ede.standyourground.networking.api.exchange.payload.request.CreateUnitRequest;
import com.ede.standyourground.networking.api.exchange.payload.response.OkResponse;
import com.ede.standyourground.networking.impl.exchange.handler.request.CreateUnitRequestHandler;
import com.google.gson.Gson;

import java.net.URI;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Retrofit;

@Singleton
public class NetworkingHandlerImpl implements NetworkingHandler {
    private static Logger logger = new Logger(NetworkingHandlerImpl.class);

    private final Lazy<CreateUnitRequestHandler> createUnitRequestHandler;
    private final Lazy<Gson> gson;
    private final Lazy<Retrofit> retrofit;

    @Inject
    public NetworkingHandlerImpl(Lazy<CreateUnitRequestHandler> createUnitRequestHandler,
                                 Lazy<Gson> gson,
                                 Lazy<Retrofit> retrofit) {
        this.createUnitRequestHandler = createUnitRequestHandler;
        this.gson = gson;
        this.retrofit = retrofit;
    }

    private static Socket socket;

    public void connect(final String gameSessionId, final Callback callback) {
        try {
            URI uri = new URI(retrofit.get().baseUrl().toString());
            logger.i("Connecting to player on %s:%d", retrofit.get().baseUrl().host(), retrofit.get().baseUrl().port());
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
                CreateUnitRequest createUnitRequest = gson.get().fromJson((String) args[0], CreateUnitRequest.class);
                if (createUnitRequest != null) {
                    logger.i("Handling createUnitRequest %s", createUnitRequest.getId().toString());
                    createUnitRequestHandler.get().handle(createUnitRequest);
                }
            }
        });

        socket.on("OkResponse", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                OkResponse okResponse = gson.get().fromJson((String) args[0], OkResponse.class);
                logger.i("Received ok response for %s", okResponse.getId());
            }
        });
    }


    public void sendExchange(final Exchange exchange) {
        String requestString = gson.get().toJson(exchange);
        if (exchange != null) {
            logger.i("Sending %s exchange. Id %s", exchange.getType(), exchange.getId().toString());
            socket.emit("gameEvent", requestString);
        }
    }

    public void closeConnection() {
        socket.close();
    }
}
