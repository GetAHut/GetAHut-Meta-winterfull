package com.winterfull.utils;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author : ytxu5
 * @date: 2023/3/28
 */
public class IdCreator {

    /*
     * 1. UUID
     * 2. Snowflake
     * 3. Leaf - 配置数据库
     * 4. ObjectId
     * 5. ulid
     * 6. AtomicLong.getAndIncrement().toHexString()
     */

    private static AtomicLong ids = new AtomicLong(0L);

    /**
     * 1. UUID
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 16
     * @return
     */
    public static String getIds(){
        return Long.toHexString(ids.getAndIncrement());
    }
}
