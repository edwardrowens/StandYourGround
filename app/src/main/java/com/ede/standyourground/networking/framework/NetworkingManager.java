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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class NetworkingManager {
    private static Logger logger = new Logger(NetworkingManager.class);

    private static final int PORT = 80;

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

    public void connect(final Callback callback) {
        readHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
//                    URI uri = new URI("https://standyourground.herokuapp.com/");
                    URI uri = new URI("http://192.168.0.102:8080/");
                    logger.i("Connecting to player on %s:%d", uri.getHost(), uri.getPort());
                    socket = IO.socket(uri);
                } catch (Exception e) {
                    logger.e("Could not connect to player.", e);
                    callback.onFail();
                }
                socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        socket.send("hi");
                    }
                });
                callback.onSuccess();
//                    readExchanges();
//                    writeExchanges();
            }
        });
    }

    public void sendRequest(final Request request) {
        outgoingExchanges.add(request);
    }

//    private void writeExchanges() {
//        writeHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (outgoingExchanges.size() > 0) {
//                    String requestString = GSON.toJson(outgoingExchanges.peek());
//                    if (requestString != null) {
//                        try {
//                            IOUtils.write(requestString, socket.getOutputStream());
//                        } catch (IOException e) {
//                            logger.e("Problem in writing exchange to output stream", e);
//                        }
//                        outgoingExchanges.poll();
//                    }
//                }
//                writeHandler.post(this);
//            }
//        });
//    }
//
//    private void readExchanges() {
//        readHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                    Exchange exchange = GSON.fromJson(bufferedReader, Exchange.class);
//                    if (exchange != null) {
//                        logger.i("Handling exchange %s", exchange.getId().toString());
//                        ExchangeHandlerUtil.handleExchange(exchange);
//                    }
//                } catch (IOException e) {
//                    logger.e("Problem in reading exchange from input stream", e);
//                }
//            }
//        });
//    }
//
//    public void closeConnection() {
//        try {
//            socket.close();
//        } catch (IOException e) {
//            logger.e("Problem closing socket", e);
//        }
//    }
}
