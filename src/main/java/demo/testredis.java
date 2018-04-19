package demo;

import redis.clients.jedis.Jedis;

public class testredis {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.zadd("score_stu", 100,"john");
        jedis.zadd("score_stu",90,"jack");


    }
}
