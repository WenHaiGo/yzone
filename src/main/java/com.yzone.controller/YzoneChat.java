package com.yzone.controller;

import com.yzone.model.Message;
import com.yzone.model.User;
import com.yzone.service.MessageService;
import com.yzone.service.UserService;
import com.yzone.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */

@Controller
@ServerEndpoint(value = "/websocket/{fromName}/{toName}")
public class YzoneChat {
    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static Map<String, YzoneChat> webSocketMap = new HashMap();

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     * @throws IOException
     */
    MessageService messageService = (MessageService) SpringContextUtil.getBean(MessageService.class);
    UserService userService = (UserService) SpringContextUtil.getBean(UserService.class);

    @OnOpen
    public void onOpen(@PathParam("fromName") String fromName, Session session) throws IOException {
        String aString = session.getQueryString();
        System.out.println("用户" + fromName + "上线");
        this.session = session;
        //用户上线之后马上检测是否是新用户,因为已经登录的用户是一直保存在webSocketMap中的
        if (webSocketMap.containsKey(fromName)) {
            int uid = userService.getUserByUsername(fromName).getId();
            List<Message> allUnread = messageService.checkUnreadByUid(uid);
            //把这些消息推送给用户
            for (Message m:allUnread
                 ) {
                webSocketMap.get(fromName).sendMessage(m.getSend()+"于"+m.getCreateTime()+"发送");
            }
        }

        //否则就把用户添加到当前map里面
        else {
            webSocketMap.put(fromName, this); // 加入map中}

            System.out.println(webSocketMap.get(fromName) + "========");
        }

        addOnlineCount(); // 在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());

            // 群发消息
//				for (YzoneChat item : webSocketSet) {
//					try {
//						item.sendMessage(session.toString()+"用户上线了");
//					} catch (IOException e) {
//						e.printStackTrace();
//						//如果有一个挂了 还是要继续发送消息
//						continue;
//					}
//				}
        }

        /**
         * 连接关闭调用的方法
         */
        @OnClose
        public void onClose (@PathParam("fromName") String fromName){
            System.out.println("删除了" + fromName);
            //这里如何remove用户?
            webSocketMap.remove(fromName); // 从Map中删除
            subOnlineCount();
            System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
        }

        /**
         * 收到客户端消息后调用的方法
         *
         * @param send    客户端发送过来的消息
         * @param session 可选的参数 表示是谁发送的
         * @throws IOException
         */


        @OnMessage
        public void onMessage (@PathParam("fromName") String fromName, @PathParam("toName") String toName, Session
        session, String send) throws IOException {
            System.out.println("进入了onmessage也卖弄");
            //获取发送人的uid
            int uid = userService.getUserByUsername(fromName).getId();
            //发送接收人的uid
            int otherUid = userService.getUserByUsername(toName).getId();
            Message m = new Message();
            m.setUid(uid);
            m.setOtherUid(otherUid);
            m.setSend(send);
            //先保存到数据库  1表示保存成功  0表示保存失败
            int isSave = messageService.save(m);
            System.out.println("保存" + isSave);
            //从当前的map去查询是否有用户为toName的人,如果有的话就得到这个人的session 把消息发送给他
            if (webSocketMap.containsKey(toName)) {
                System.out.println("当期那用户是否存在"+toName);
                webSocketMap.get(toName).sendMessage(send);
                //发送了之后就表明已读,所以现在就把数据库状态的改过来  TODO 其实现在也是不太好的,最好是用户点开看了之后就发送一条ajax告知
                User user = userService.getUserByUsername(toName);
                System.out.println("xiayibu");
                messageService.updateReadStateByUid(user.getId());
            } else {
                //也可以发送到对方的聊天框里  提示待会再来
                System.out.println("该用户不再线");
            }

            // 群发消息
//		for (YzoneChat item : webSocketSet) {
//			try {
//				item.sendMessage(message);
//			} catch (IOException e) {
//				e.printStackTrace();
//				//如果有一个挂了 还是要继续发送消息
//				continue;
//			}
//		}
        }

        /*
         * 私聊
         * Object[] my = webSocketSet.toArray(); //发给第二个人 ((YzoneChat)
         * my[1]).sendMessage(message);
         *
         */

        /**
         * 发生错误时调用
         *
         * @param session
         * @param error
         */
        @OnError
        public void onError (Session session, Throwable error){
            System.out.println("聊天发生错误");
            error.printStackTrace();
        }

        /**
         * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
         *
         * @param message
         * @throws IOException
         */
        public void sendMessage (String message) throws IOException {
            //给谁发这里就是谁的session
            System.out.println("正在发送");
            this.session.getBasicRemote().sendText(message);
            // this.session.getAsyncRemote().sendText(message);
        }

        public static synchronized int getOnlineCount () {
            return onlineCount;
        }

        public static synchronized void addOnlineCount () {
            YzoneChat.onlineCount++;
        }

        public static synchronized void subOnlineCount () {
            YzoneChat.onlineCount--;
        }
    }

