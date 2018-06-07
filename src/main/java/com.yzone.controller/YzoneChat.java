package com.yzone.controller;

import com.yzone.model.Message;
import com.yzone.model.User;
import com.yzone.service.MessageService;
import com.yzone.service.UserService;
import com.yzone.utils.SpringContextUtil;
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

@ServerEndpoint(value = "/websocket/{fromName}/{toName}")
public class YzoneChat {
    private static int onlineCount = 0;
    private static Map<String, YzoneChat> webSocketMap = new HashMap();

    private Session session;

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     * @throws IOException
     */
    MessageService messageService = (MessageService) SpringContextUtil.getBean(MessageService.class);
    UserService userService = (UserService) SpringContextUtil.getBean(UserService.class);
    NewsController nc = (NewsController) SpringContextUtil.getBean(NewsController.class);

    /*处理用户登陆时候的操作,登陆之后会把数据库未读的消息都推送给用户*/
    @OnOpen
    public void onOpen(@PathParam("fromName") String fromName, Session session) throws IOException {
        System.out.println("用户" + fromName + "上线");
        this.session = session;
        //用户上线之后马上检测是否是新用户,因为已经在线的用户是一直保存在webSocketMap中的
        if (webSocketMap.containsKey(fromName)) {
            int uid = userService.getUserByUsername(fromName).getId();
            //从数据库中读出该用户所有未读的消息
            List<Message> allUnread = messageService.checkUnreadByUid(uid);
            //把这些消息推送给用户
            for (Message m : allUnread
                    ) {
                webSocketMap.get(fromName).sendMessage(m.getSend() + "于" + m.getCreateTime() + "发送");
            }
        }
        //否则就把用户添加到当前map里面
        else {
            webSocketMap.put(fromName, this); // 加入map中}
            System.out.println(webSocketMap.get(fromName) + "========");
        }

        addOnlineCount(); // 在线数加1
        System.err.println("有新连接加入！当前在线人数为" + getOnlineCount());

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("fromName") String fromName) {
        System.err.println("删除了" + fromName);
        //这里如何remove用户?
        webSocketMap.remove(fromName); // 从Map中删除
        subOnlineCount();
        System.err.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param send    客户端发送过来的消息
     * @param session 可选的参数 表示是谁发送的
     * @throws IOException
     */


    @OnMessage
    public void onMessage(@PathParam("fromName") String fromName, @PathParam("toName") String toName, Session
            session, String send) throws IOException {
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
        //从当前的map去查询是否有用户为toName的人,如果有的话就得到这个人的session 把消息发送给他
        if (webSocketMap.containsKey(toName)) {

            webSocketMap.get(toName).sendMessage(send+"   译文:"+nc.getTransResult("自动检测","自动检测",send));
            User user = userService.getUserByUsername(toName);
            messageService.updateReadStateByUid(user.getId());
        } else {
            //也可以发送到对方的聊天框里  提示待会再来
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


    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.err.println("聊天发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        //给谁发这里就是谁的session
        System.err.println("正在发送");
        this.session.getBasicRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        YzoneChat.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        YzoneChat.onlineCount--;
    }
}

