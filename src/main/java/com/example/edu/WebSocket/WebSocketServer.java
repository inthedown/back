package com.example.edu.WebSocket;

import com.alibaba.fastjson.JSONObject;
import com.example.edu.Repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
@Service
@ServerEndpoint("/websocket/{deviceId}")
public class WebSocketServer {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。
    //private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();
     private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //用户id
    private String userID;
    //对方用户id
    private String touserID;
    //设备id
    private String deviceID;

    private FileRepository fileRepository;

    private org.apache.commons.logging.Log Log = LogFactory.getLog(WebSocketServer.class);
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("deviceId") String deviceId) {
        log.info(String.valueOf(session.getRequestURI()));
        this.session = session;
        this.deviceID = deviceId;
        log.info("接受者"+deviceID);
        if(webSocketMap.containsKey(deviceID)){
            webSocketMap.remove(deviceID);
            webSocketMap.put(deviceID, this);
        }else {
            webSocketMap.put(deviceID, this);
            addOnlineCount();
        }

        try {
            sendMessage("connect_success");
            log.info("客户端 connect_success,deviceId=" + deviceId + ",当前连接设备数为:" + getOnlineCount());
        } catch (IOException e) {
            log.error("websocket IO Exception");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(deviceID)){
            webSocketMap.remove(deviceID);
            subOnlineCount();
        }
        // 断开连接情况下，更新主板占用情况为释放
        log.info("释放的deviceId=" + deviceID + ",当前连接设备数为:" + getOnlineCount());
        releaseResource();
    }

    private void releaseResource() {
        // 这里写释放资源和要处理的业务
        log.info("有一连接关闭！当前设备数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @Param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自客户端 deviceId=" + deviceID + " 的信息:" + message);

    }

    /**
     * 发生错误回调
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error(session.getBasicRemote() + "客户端发生错误");
        error.printStackTrace();
    }

    /**
     * 自定义消息
     */
    public static void sendMessage(String message, String deviceId) throws IOException {
        log.info("推送消息到客户端 " + deviceId + "，推送内容:" + message);
        JSONObject jsonObject = JSONObject.parseObject(message);
//        //webSocketSet.get();
//        for (WebSocketServer item : webSocketSet) {
//            try {
//                //这里可以设定只推送给传入的sid，为null则全部推送
//                if (toSids.size() <= 0) {
//                    item.sendMessage(jsonObject.getString("msg"));
//                } else if (toSids.contains(item.userID)) {
//                    item.sendMessage(jsonObject.getString("msg"));
//                }
//            } catch (IOException e) {
//                continue;
//            }
//        }
        if(webSocketMap.containsKey(deviceId)){
            webSocketMap.get(deviceId).sendMessage(message);
        }else{
            log.info("客户端 " + deviceId + "未连接");
        }
    }

    /**
     * 实现服务器主动推送消息到 指定客户端
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 获取当前在线人数
     *
     * @return
     */
    public static int getOnlineCount() {
        return onlineCount.get();
    }

    /**
     * 当前在线人数 +1
     *
     * @return
     */
    public static void addOnlineCount() {
        onlineCount.getAndIncrement();
    }

    /**
     * 当前在线人数 -1
     *
     * @return
     */
    public static void subOnlineCount() {
        onlineCount.getAndDecrement();
    }

    /**
     * 获取当前在线客户端对应的WebSocket对象
     *
     * @return
     */
    public static ConcurrentHashMap<String, WebSocketServer> getWebSocketMap() {
        return webSocketMap;
    }

}
