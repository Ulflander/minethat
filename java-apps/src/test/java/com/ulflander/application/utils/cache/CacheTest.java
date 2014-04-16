package com.ulflander.application.utils.cache;

import com.ulflander.mining.rdf.model.Person;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
public class CacheTest {

    @Test
    public void basicCacheTest () {
        Cache<String, Person> cache = new Cache<String, Person>();
        Person person = new Person();
        cache.put("xav", person);

        Assert.assertEquals("Cache size should be one when we insert one object", 1, cache.size());
    }


    @Test
    public void putMapCacheTest () {

        Map<String, Person> map = new HashMap<String, Person>();
        Person person = new Person();
        map.put("xav", person);

        Cache<String, Person> cache = new Cache<String, Person>();
        cache.putMap(map);

        Assert.assertEquals("Cache size should be one when we insert one object using putMap", 1, cache.size());
    }



    @Test
    public void putAllCacheTest () {

        Map<String, Person> map = new HashMap<String, Person>();
        Person person = new Person();
        map.put("xav", person);

        Cache<String, Person> cache = new Cache<String, Person>();
        cache.putAll(map);

        Assert.assertEquals("Cache size should be zero when we insert one object using putAll", 0, cache.size());
    }




    @Test
    public void cleanCacheTest () throws InterruptedException {
        Cache<String, Person> cache = new Cache<String, Person>();
        Person person = new Person();
        cache.put("xav", person);
        cache.setTimeout(1);
        Thread.sleep(1200);
        cache.clean ();
        Assert.assertEquals("Calling clean ", 0, cache.size());
    }


    @Test
    public void noNeedCleanCacheTest () throws InterruptedException {
        Cache<String, Person> cache = new Cache<String, Person>();
        Person person = new Person();
        cache.put("xav", person);
        cache.setTimeout(1);
        Thread.sleep(800);
        cache.clean ();
        Assert.assertEquals("Cache size should be zero when we insert one object using putAll", 1, cache.size());
    }
}
