package demo;

import com.yzone.controller.NewsController;
import com.yzone.controller.TopicController;
import com.yzone.model.Topic;
import com.yzone.service.JedisClient;
import com.yzone.service.impl.JedisClientPool;
import com.yzone.utils.SearchRecomModel;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;


public class testredis {

    static JedisClientPool jedisClient= new JedisClientPool();
    public static void main(String[] args) {
   System.out.println(jedisClient.zrange("hotTopic",0L,100L));


    }
}
