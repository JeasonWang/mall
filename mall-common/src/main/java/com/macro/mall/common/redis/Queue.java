package com.macro.mall.common.redis;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.macro.mall.common.function.EConsumer;
import com.macro.mall.common.manager.AsyncManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.TimerTask;

/**
 * @author jeason
 * @date 2020/7/24 10:51
 */
@Slf4j
public abstract class Queue {
    private Map<String,RedisQueueModel> queues= Maps.newHashMap();

    public synchronized void addQueue(String name, RedisQueueModel model){
        if (queues.containsKey(name)){
            log.warn("[{}]队列已存在,",name);
            return;
        }
        queues.put(name,model);
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                Queue.this.run(name);
            }
        });
    }

    private void run(String name){
        RedisQueueModel model=queues.get(name);
        if (model == null){
            log.warn("[{}]队列无消费者,",name);
            return;
        }
        if (model.isStart()){
            log.warn("[{}]已启动,",name);
            return;
        }
        try {
            log.info("启动队列[{}]",name);
            model.setStart(true);
            EConsumer<Object,Exception> eConsumer=model.getEConsumer();
            List<Object> list= Lists.newArrayList();
            Long time=System.currentTimeMillis();
            Long stepTime=model.getTime();
            Integer size=model.getSize() - 1;
            while (true){
                try {
                    Object object=get(name);
                    if (object != null) {
                        list.add(object);
                        if (list.size() > size){
                            log.debug("[{}]队列消息：[{}]",name,list);
                            eConsumer.accept(list);
                            list.clear();
                        }
                    }
                    if (System.currentTimeMillis()-time > stepTime){
                        time=System.currentTimeMillis();
                        if (list.size() > 0){
                            log.debug("[{}]队列消息：[{}]",name,list);
                            eConsumer.accept(list);
                            list.clear();
                        }
                    }
                }catch (Exception e){
                    list.clear();
                    log.error("[{}]队列消费错误：[{}]",name,e.toString());
                }
            }
        }catch (Exception e){
            log.error("[{}]队列消费错误退出：[{}]",name,e.toString());
        }finally {
            model.setStart(false);
        }

    }

    /**
     * 获取队列数据
     * @param key
     * @return
     */
    abstract Object get(String key) throws Exception;

}
