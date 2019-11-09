package fr.antodev.magicserver.support.exchange;

import fr.antodev.magicserver.support.Options;
import redis.clients.jedis.Jedis;

public abstract class JedisConnection {

    protected Jedis jedis = new Jedis(Options.JEDIS_HOST);

    public Jedis getJedis() {
        return jedis;
    }
}
