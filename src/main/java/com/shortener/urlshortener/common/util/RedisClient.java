package com.shortener.urlshortener.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Component("com.shortener.urlshortener.common.util.RedisClient")
public class RedisClient {

  public static final String ENV_PROP_REDIS_HOST = "redis.host";
  public static final String ENV_PROP_REDIS_PORT = "redis.port";
  public static final String ENV_PROP_REDIS_POOL_MAX_TOTAL = "redis.pool.max-total";
  public static final String ENV_PROP_REDIS_POOL_MAX_IDLE = "redis.pool.max-idle";
  public static final String ENV_PROP_REDIS_POOL_MIN_IDLE = "redis.pool.min-idle";
  public static final String ENV_PROP_REDIS_POOL_MAX_WAIT = "redis.pool.max-wait";
  public static final String ENV_PROP_REDIS_POOL_TEST_ON_BORROW = "redis.pool.test-on-borrow";

  public static final String RESPONSE_OK = "OK";

  @Value("${redis.host:''}")
  private String redisHost;

  @Value("${redis.port:0}")
  private Integer redisPort;

  @Value("${redis.password:''}")
  private String password;

  @Value("${redis.database:0}")
  private Integer redisDb;

  @Value("${redis.pool.max-total:8}")
  private Integer maxTotal;

  @Value("${redis.pool.max-idle:8}")
  private Integer maxIdle;

  @Value("${redis.pool.min-idle:2}")
  private Integer minIdle;

  @Value("${redis.pool.max-wait:10000}")
  private Integer maxWaitMillis;

  @Value("${redis.pool.test-on-borrow:true}")
  private Boolean testOnBorrow;

  private JedisPool jedisPool;

  private ObjectMapper mapper;

  @PostConstruct
  void init() {
    GenericObjectPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setMaxTotal(maxTotal);
    poolConfig.setMaxIdle(maxIdle);
    poolConfig.setMinIdle(minIdle);
    poolConfig.setMaxWaitMillis(maxWaitMillis);
    poolConfig.setTestOnBorrow(testOnBorrow);
    if (StringUtils.isNotBlank(redisHost) && redisPort > 0) {
      log.info("using redis host : {} and port: {} and db : {} for connection", redisHost,
          redisPort, redisDb);
      jedisPool =
          new JedisPool(poolConfig, redisHost, redisPort, Protocol.DEFAULT_TIMEOUT, password,
              redisDb);
    } else {
      log.warn("redis host :{} and port :{} not found", redisHost, redisPort);
    }

    mapper = new ObjectMapper();
  }

  /**
   * Set the string value as value of the key. The string can't be longer than 1073741824 bytes (1
   * GB).
   *
   * @param key
   * @param value
   *
   * @return Status code reply
   */
  public String set(String key, String value) {
    try (Jedis jedis = jedisPool.getResource()) {
      jedis.set(key, value);
    }
    return value;
  }

  /**
   * The command is exactly equivalent to the following group of commands:
   * {@link #set(String, String) SET} + {@link #expire(String, int) EXPIRE}. The operation is
   * atomic.
   * <p>
   * Time complexity: O(1)
   *
   * @param key
   * @param seconds
   * @param value
   *
   * @return Status code reply
   */
  public String setex(final String key, final int seconds, final String value) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.setex(key, seconds, value);
    }
  }

  /**
   * Set the the respective keys to the respective values. MSET will replace old values with new
   * values. MSET is atomic operation. This means that for instance if the keys A and B
   * are modified, another client talking to Redis can either see the changes to both A and B at
   * once, or no modification at all.
   *
   * @param valueMap
   *
   * @return Status code reply Basically +OK as MSET can't fail
   */
  public Map<String, String> mset(final Map<String, String> valueMap) {
    //NOTE: instead of using List and converting it into array, use array only since u already
    // know the size
    List<String> keysvalues = new ArrayList<>();
    for (Map.Entry<String, String> entry : valueMap.entrySet()) {
      keysvalues.add(entry.getKey());
      keysvalues.add(entry.getValue());
    }
    if (!CollectionUtils.isEmpty(keysvalues)) {
      try (Jedis jedis = jedisPool.getResource()) {
        jedis.mset(keysvalues.toArray(new String[0]));
      }
    }
    return valueMap;
  }

  /**
   * Get the value of the specified key. If the key does not exist null is returned. If the value
   * stored at key is not a string an error is returned because GET can only handle string values.
   *
   * @param key
   */
  public String get(String key) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.get(key);
    }
  }

  /**
   * Get the value of the specified key. If the key does not exist null is returned. If the value
   * stored at key is not a string an error is returned because GET can only handle string values.
   *
   * @param key
   * @param clazz
   *
   * @return value at given key or null
   */
  public <T> T get(String key, Class<T> clazz) throws IOException {
    try (Jedis jedis = jedisPool.getResource()) {
      String jsonString = jedis.get(key);
      if (StringUtils.isNotBlank(jsonString)) {
        return mapper.readValue(jsonString, clazz);
      }
      return null;
    }
  }

  /**
   * Get the values of all the specified keys. If one or more keys dont exist or is not of type
   * String, a 'nil' value is returned instead of the value of the specified key, but the operation
   * never fails.
   * <p>
   * Time complexity: O(1) for every key
   *
   * @param keys
   * @param clazz
   *
   * @return Multi bulk reply
   */
  public <T> List<T> mget(Class<T> clazz, final String... keys) throws IOException {
    try (Jedis jedis = jedisPool.getResource()) {
      List<String> jsonList = jedis.mget(keys);
      List<T> result = new ArrayList<>();
      if (!CollectionUtils.isEmpty(jsonList)) {
        for (String json : jsonList) {
          result.add(mapper.readValue(json, clazz));
        }
      }
      return result;
    }
  }

  public <T> Map<String, T> mgetAsMap(Class<T> clazz, final String... keys) {
    try (Jedis jedis = jedisPool.getResource()) {
      List<String> jsonList = jedis.mget(keys);
      Map<String, T> result = new LinkedHashMap<>();
      if (!CollectionUtils.isEmpty(jsonList)) {
        for (int i = 0; i < keys.length; i++) {
          String key = keys[i];
          String json = jsonList.get(i);
          if (StringUtils.isNotBlank(json)) {
            try {
              result.put(key, mapper.readValue(json, clazz));
            } catch (IOException e) {
              log.error("unable to parse key {} with value {} : ", key, json, e);
            }
          } else {
            log.warn("value not found for key {}", key);
          }
        }
      }
      return result;
    }
  }

  /**
   * Remove the specified keys. If a given key does not exist no operation is performed for this
   * key. The command returns the number of keys removed. Time complexity: O(1)
   *
   * @param keys
   *
   * @return Integer reply, specifically: an integer greater than 0 if one or more keys were removed
   * 0 if none of the specified key existed
   */
  public Long del(final String... keys) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.del(keys);
    }
  }

  public Long del(String key) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.del(key);
    }
  }

  /**
   * Test if the specified key exists. The command returns the number of keys existed Time
   * complexity: O(N)
   *
   * @param keys
   *
   * @return Integer reply, specifically: an integer greater than 0 if one or more keys were removed
   * 0 if none of the specified key existed
   */
  public Long exists(final String... keys) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.exists(keys);
    }
  }

  /**
   * Test if the specified key exists. The command returns "1" if the key exists, otherwise "0" is
   * returned. Note that even keys set with an empty string as value will return "1". Time
   * complexity: O(1)
   *
   * @param key
   *
   * @return Boolean reply, true if the key exists, otherwise false
   */
  public Boolean exists(final String key) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.exists(key);
    }
  }

  /**
   * Rename oldkey into newkey but fails if the destination key newkey already exists.
   * <p>
   * Time complexity: O(1)
   *
   * @param oldkey
   * @param newkey
   *
   * @return Integer reply, specifically: 1 if the key was renamed 0 if the target key already exist
   */
  public Long rename(final String oldkey, final String newkey) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.renamenx(oldkey, newkey);
    }
  }

  /**
   * Set a timeout on the specified key. After the timeout the key will be automatically deleted by
   * the server. A key with an associated timeout is said to be volatile in Redis terminology.
   *
   * @param key
   * @param seconds
   *
   * @return Integer reply, specifically: 1: the timeout was set. 0: the timeout was not set since
   * the key already has an associated timeout , or the key does not exist.
   */
  public Long expire(final String key, final int seconds) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.expire(key, seconds);
    }
  }

  /**
   * EXPIREAT works exctly like {@link #expire(String, int) EXPIRE} but it takes an absolute one
   * in the form of a UNIX timestamp (Number of seconds elapsed since 1 Gen 1970).
   *
   * @param key
   * @param unixTime
   *
   * @return Integer reply, specifically: 1: the timeout was set. 0: the timeout was not set since
   * the key already has an associated timeout , or the key does not exist.
   */
  public Long expireAt(final String key, final long unixTime) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.expireAt(key, unixTime);
    }
  }

  /**
   * Add the string value to the tail of the list stored at key. If no list is present then it will
   * first create an empty list. Time complexity: O(1)
   *
   * @param key
   * @param values
   *
   * @return Long reply, specifically, the number of elements inside the list after the push
   * operation.
   */
  public Long appendValuesToList(String key, final String... values) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.rpush(key, values);
    }
  }

  /**
   * Return the specified elements of the list stored at the specified key. Start and end are
   * zero-based indexes. 0 is the first element of the list (the list head), 1 the next element and
   * so on.
   * <p>
   * For example LRANGE foobar 0 2 will return the first three elements of the list.
   * <p>
   * start and end can also be negative numbers indicating offsets from the end of the list. For
   * example -1 is the last element of the list, -2 the penultimate element and so on.
   * <p>
   * <b>Consistency with range functions in various programming languages</b>
   * <p>
   * Note that if you have a list of numbers from 0 to 100, LRANGE 0 10 will return 11 elements,
   * that is, rightmost item is included.
   * <p>
   * Indexes out of range will not produce an error: if start is over the end of the list, or start
   * &gt; end, an empty list is returned. If end is over the end of the list Redis will threat it
   * just like the last element of the list.
   * <p>
   * Time complexity: O(start+n) (with n being the length of the range and start being the start
   * offset)
   *
   * @param key
   * @param start
   * @param end
   *
   * @return List of string
   */
  public List<String> getRangeFromList(String key, final long start, final long end) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.lrange(key, start, end);
    }
  }

  /**
   * same as getRangeFromList(String key, final long start, final long end). It will just be
   * mapped to passed Class.
   *
   * @param key
   * @param start
   * @param end
   * @param clazz
   *
   * @return List of string
   */
  public <T> List<T> getRangeFromList(String key, final long start, final long end, Class<T> clazz)
      throws IOException {
    try (Jedis jedis = jedisPool.getResource()) {
      List<String> jsonList = jedis.lrange(key, start, end);
      List<T> result = new ArrayList<>();
      if (!CollectionUtils.isEmpty(jsonList)) {
        for (String json : jsonList) {
          result.add(mapper.readValue(json, clazz));
        }
      }
      return result;
    }
  }

  /**
   * Return the specified element of the list stored at the specified key. 0 is the first element, 1
   * the second and so on. Negative indexes are supported, for example -1 is the last element, -2
   * the penultimate and so on.
   * <p>
   * If the value stored at key is not of list type an error is returned. If the index is out of
   * range a 'nil' reply is returned.
   *
   * @param key
   * @param index
   *
   * @return value at index
   */
  public String getAtIndex(String key, final long index) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.lindex(key, index);
    }
  }

  /**
   * Return the specified element of the list stored at the specified key. 0 is the first element, 1
   * the second and so on. Negative indexes are supported, for example -1 is the last element, -2
   * the penultimate and so on.
   * <p>
   * If the value stored at key is not of list type an error is returned. If the index is out of
   * range a 'nil' reply is returned.
   *
   * @param key
   * @param index
   * @param clazz
   *
   * @return value at index
   */
  public <T> T getAtIndex(String key, final long index, Class<T> clazz) throws IOException {
    try (Jedis jedis = jedisPool.getResource()) {
      String json = jedis.lindex(key, index);
      if (StringUtils.isNotBlank(json)) {
        return mapper.readValue(json, clazz);
      }
      return null;
    }
  }

  /**
   * Set a new value as the element at index position of the List at key.
   * <p>
   * Out of range indexes will generate an error.
   * <p>
   * Similarly to other list commands accepting indexes, the index can be negative to access
   * elements starting from the end of the list. So -1 is the last element, -2 is the penultimate,
   * and so forth.
   * <p>
   * <b>Time complexity:</b>
   * <p>
   * O(N) (with N being the length of the list), setting the first or last elements of the list is
   * O(1).
   *
   * @param key
   * @param index
   * @param value
   *
   * @return passed value
   */
  public String setValueInList(String key, final long index, final String value) {
    try (Jedis jedis = jedisPool.getResource()) {
      jedis.lset(key, index, value);
    }
    return value;
  }


  /**
   * Set the specified hash field to the specified value.
   *
   * @param key
   * @param field
   * @param value
   *
   * @return If the field already exists, and the HSET just produced an update of the value, 0 is
   * returned, otherwise if a new field is created 1 is returned.
   */
  public Long hset(final String key, final String field, final String value) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.hset(key, field, value);
    }
  }


  public String hget(final String key, final String field) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.hget(key, field);
    }
  }

  public Map<String, String> hgetAll(final String key) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.hgetAll(key);
    }
  }

  public Boolean hmset(String redisKey, Map<String, String> fieldValueMap) {
    Boolean result = Boolean.FALSE;
    if (StringUtils.isNotBlank(redisKey) && MapUtils.isNotEmpty(fieldValueMap)) {
      try (Jedis jedis = jedisPool.getResource()) {
        if (RESPONSE_OK.equals(jedis.hmset(redisKey, fieldValueMap))) {
          result = Boolean.TRUE;
        }
      }
    }
    return result;
  }

  public Long hdel(String redisKey, String[] fields) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.hdel(redisKey, fields);
    }
  }

  public List<String> hmget(String redisKey, String[] fields) {
    List<String> result = new ArrayList<>();
    if (StringUtils.isNotBlank(redisKey) && fields != null && fields.length > 0) {
      try (Jedis jedis = jedisPool.getResource()) {
        result.addAll(jedis.hmget(redisKey, fields));
      }
    }
    return result;
  }

  public List<String> findKeysWithPattern(String pattern) {
    List<String> keySet;
    if (StringUtils.isNotBlank(pattern)) {
      try (Jedis jedis = jedisPool.getResource()) {
        keySet = new ArrayList<>();

        String nextCursor = ScanParams.SCAN_POINTER_START;
        ScanParams scanParams = new ScanParams();
        scanParams.match(pattern);
        scanParams.count(1000);

        ScanResult<String> scanResult;
        do {
          scanResult = jedis.scan(nextCursor, scanParams);
          if (CollectionUtils.isNotEmpty(scanResult.getResult())) {
            keySet.addAll(scanResult.getResult());
          }
          nextCursor = scanResult.getStringCursor();
        } while (!ScanParams.SCAN_POINTER_START.equals(nextCursor));
      }
    } else {
      keySet = Collections.EMPTY_LIST;
    }
    return keySet;
  }

  public Boolean deleteKeysWithPattern(String pattern) {
    Boolean result = Boolean.FALSE;
    List<String> keyList = findKeysWithPattern(pattern);
    if (CollectionUtils.isNotEmpty(keyList)) {
      del(keyList.toArray(new String[0]));
      result = Boolean.TRUE;
    }
    return result;
  }

  /**
   * check's if specified member is present in set value stored at key.
   *
   * @param key
   * @param member
   *
   * @return
   */
  public Boolean sismember(String key, String member) {
    Boolean result = Boolean.FALSE;
    if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(member)) {
      try (Jedis jedis = jedisPool.getResource()) {
        result = jedis.sismember(key, member);
      }
    }
    return result;
  }

  /**
   * Remove the specified member from the set value stored at key. If member was not a member of the
   * set no operation is performed. If key does not hold a set value an error is returned.
   * <p>
   * Time complexity O(1)
   *
   * @param key
   * @param members
   *
   * @return Integer reply, specifically: 1 if the new element was removed 0 if the new element was
   * not a member of the set
   */
  public Long srem(final String key, final String... members) {
    Long result = null;
    if (StringUtils.isNotBlank(key)) {
      try (Jedis jedis = jedisPool.getResource()) {
        result = jedis.srem(key, members);
      }
    }
    return result;
  }

  /**
   * Add the specified member to the set value stored at key. If member is already a member of the
   * set no operation is performed. If key does not exist a new set with the specified member as
   * sole member is created. If the key exists but does not hold a set value an error is returned.
   * <p>
   * Time complexity O(1)
   *
   * @param key
   * @param members
   *
   * @return Integer reply, specifically: 1 if the new element was added 0 if the element was
   * already a member of the set
   */
  public Long sadd(final String key, final String... members) {
    Long result = null;
    if (StringUtils.isNotBlank(key)) {
      try (Jedis jedis = jedisPool.getResource()) {
        result = jedis.sadd(key, members);
      }
    }
    return result;
  }
}
