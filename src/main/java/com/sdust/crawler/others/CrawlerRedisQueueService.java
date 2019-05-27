package com.sdust.crawler.others;/*
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
//@Component
//public class CrawlerRedisQueueService implements ICrawlerRedisQueueService {
//
//	private static final Logger logger = LoggerFactory.getLogger(CrawlerRedisQueueService.class);
//
//	@Resource(name = "redisTemplate")
//    private RedisTemplate redisTemplate;
//
//	@Resource(name = "redisAllTemplate")
//	private RedisTemplate redisAllTemplate;
//
//	public static final String Task_Info_Identification = "taskdetail:"; //获取任务详细信息的hash表标识
//
//	public static final String Result_Identification="result:";
//
//	public static final String TaskCountIdentification="count:zset";
//
//    public static final String Task_Status_Identification="taskstatus:";
//
//	public static final String WorkerThreadsCount="thread:count";
//
//	public static final String Task_Worker_Relation="relation:";
//
//	public static final String QueryMysqlCount="query_mysql:count";//记录查询爬取信息的页面特征和爬取规则
//
//    public static final String SystemConfigureIdentification="sysconf:hash_data";//系统参数
//
//    public static final String Expire_Identification="expi:ti";
//
//    public static final String ErrorTaskQuene="errortask:queue_data";//失败任务队列
//
//    public static final String AllJobListQueue="alljob:result_list";
//
//	public static final String RESULTIDQUEUE="resultid:queue_data";//结果id队列
//
//	@Override
//	public boolean pushTaskToWaitingQueue(Task task) {
//		if(task == null) {
//			return false;
//		}
//		 try {
//			 //加入任务信息表
//			 putDataToRedisWithExpire(Task_Info_Identification, task.getHashcode(),task);
//	         return true;
//		 } catch (Exception e) {
//			 logger.error("任务{}添加失败！错误：{}", e.getMessage());
//			 return false;
//		 }
//	}
//
//	@Override
//	public boolean pushResultToQueryQueue(String key,String value) throws Exception
//	{
//		if(key!=null&&value!=null){
//			try{
//				putDataToRedisWithExpire(Result_Identification, key, value);
//				return true;
//			}catch(Exception e){
//				logger.error(key,"失败",e.getMessage());
//				throw e;
//			}
//		}
//		return false;
//	}
//
//	@Override
//	public String queryResult(String key) {
//		String result=null;
//		try{
//		 result = (String)redisTemplate.opsForValue().get(Result_Identification+key);
//		}catch(Exception e){
//			return result;
//		}
//		return result;
//	}
//
//	@Override
//	public Task getTaskByTaskId(String taskid) {
//		String json = (String)redisTemplate.opsForValue().get(Task_Info_Identification+taskid);
//		Task vo = JSON.parseObject(json, Task.class);
//		return vo;
//	}
//
//	@Override
//	public Task getTaskFromTaskInfoHash(String taskHashcode) {
//		String json = (String)redisTemplate.opsForValue().get(Task_Info_Identification+taskHashcode);
//		return JSON.parseObject(json, Task.class);
//	}
//
//	@Override
//	public boolean putWorkerTaskCount(String contextPath) {
//		try{
//			redisTemplate.opsForZSet().incrementScore(TaskCountIdentification, contextPath, 1);
//			return true;
//		}catch(Exception e){
//			return false;
//		}
//	}
//
//	@Override
//	public boolean minusWorkerTaskCount(String contextPath) {
//		try{
//			redisTemplate.opsForZSet().incrementScore(TaskCountIdentification, contextPath, -1);
//			return true;
//		}catch(Exception e){
//			return false;
//		}
//	}
//
//	@Override
//	public boolean initTaskCount(String contextPath) {
//		try{
//			redisTemplate.opsForZSet().add(TaskCountIdentification, contextPath, 0);
//			initSystemConfig(contextPath);
//			return true;
//		}catch(Exception e){
//			return false;
//		}
//	}
//	*/
/**
//	 * 因为拨号失败 将此worker停止接收请求
//	 * @param contextPath
//	 * @return
//	 *//*

//	@Override
//	public boolean maxTaskCount(String contextPath) {
//
//		try{
//			redisTemplate.opsForZSet().add(TaskCountIdentification, contextPath, Integer.MAX_VALUE);
//			initSystemConfig(contextPath);
//			return true;
//		}catch(Exception e){
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//	@Override
//	public String getMinTaskUrl(int index) {
//		Set<String> sets = redisTemplate.opsForZSet().range(TaskCountIdentification, index, index);
//		String result=null;
//		if(sets!=null&&sets.size()!=0){
//			for(String s:sets){
//			result =s;
//			break;
//			}
//		}
//		return result;
//	}
//
//
//	public boolean setTaskStatusFromTaskStatusInfoHash(String taskHashcode, String taskStatus) {
//		return putDataToRedisWithExpire(Task_Status_Identification, taskHashcode, taskStatus);
//	}
//
//	@Override
//	public String getTaskStatusFromTaskStatusInfoHash(String taskHashcode) {
//		return (String)redisTemplate.opsForValue().get(Task_Status_Identification+taskHashcode);
//	}
//
//	@Override
//	public boolean plusWorkerThreads(String contextPath,int i) {
//		try{
//			redisTemplate.opsForHash().increment(WorkerThreadsCount, contextPath, i);
//			return true;
//		}catch(Exception e){
//			return false;
//		}
//	}
//
//	@Override
//	public boolean setTaskWorkerRelation(String taskId, String worker) {
//		if(StringUtils.isNotBlank(taskId)&&StringUtils.isNotBlank(worker)){
//		try{
//			putDataToRedisWithExpire(Task_Worker_Relation, taskId, worker);
//			return true;
//		}catch(Exception e){
//			return false;
//		}
//		}else{
//		return false;
//		}
//	}
//
//	@Override
//	public String getWorkerByTaskId(String taskId) {
//		String result=null;
//		if(StringUtils.isNotBlank(taskId)){
//			result = (String)redisTemplate.opsForValue().get(Task_Worker_Relation+taskId);
//		}
//		return result;
//	}
//
//	@Override
//	public boolean plusQueryMysqlCount(String webSite) {
//		try {
//			redisTemplate.opsForZSet().incrementScore(QueryMysqlCount, webSite, 1);
//			return true;
//		} catch (Exception e) {
//			return false;
//		}
//	}
//
//	@Override
//	public String getTasknumByUrl(String workerUrl) {
//		try
//		{
//			return String.valueOf(redisTemplate.opsForZSet().score(TaskCountIdentification, workerUrl).intValue());
//		}
//		catch(Exception e)
//		{
//			logger.error("", e);
//		}
//		return null;
//	}
//
//	@Override
//	public boolean initSystemConfig(String workUrl) {
//		try
//		{
//			JSONObject jsonObj = new JSONObject();
//			jsonObj.put("ocr_try", "20");
//			jsonObj.put("corePoolSize", "16");
//			jsonObj.put("maximumPoolSize", "20");
//			jsonObj.put("keepAliveTime", "120");
//			jsonObj.put("workQueue", "20");
//			redisTemplate.opsForHash().put(SystemConfigureIdentification, workUrl, jsonObj.toJSONString());
//			return true;
//		}
//		catch(Exception e)
//		{
//			logger.error("", e);
//			return false;
//		}
//	}
//
//	@Override
//	public boolean updateSystemConfig(String confKey, String confVal) {
//		try
//		{
//			redisTemplate.opsForHash().put(SystemConfigureIdentification, confKey, confVal);
//			return true;
//		}
//		catch(Exception e)
//		{
//			logger.error("", e);
//			return false;
//		}
//	}
//
//	@Override
//	public String getSystemConfig(String confKey) {
//		try
//		{
//			Object obj = redisTemplate.opsForHash().get(SystemConfigureIdentification, confKey);
//			return obj == null ? "" : String.valueOf(obj);
//		}
//		catch(Exception e)
//		{
//			logger.error("", e);
//			return null;
//		}
//	}
//
//	@Override
//	public Set<String> queryAllWorker() {
//		try
//		{
//			return redisTemplate.opsForZSet().range(TaskCountIdentification, 0, -1);
//		}
//		catch(Exception e)
//		{
//			logger.error("", e);
//			return null;
//		}
//	}
//
//	*/
/**
//	 * 向redis的hash中添加数据
//	 * @param identification
//	 * @param key
//	 * @param val
//	 * @return
//	 *//*

//	private boolean putDataToRedisWithExpire(String identification, String key, Object val)
//	{
//		int expire = getExpireNum();
//		try
//		{
//			//向redis的中添加数据
//            redisTemplate.opsForValue().set(identification+key, (val instanceof String) ? val : JSON.toJSONString(val),expire,TimeUnit.MINUTES);
//			return true;
//		}
//		catch(Exception e)
//		{
//			logger.error("添加redis(hash添加key)异常", e);
//			return false;
//		}
//	}
//
//	public int getExpireNum(){
//		Integer i = (Integer)redisTemplate.opsForValue().get(Expire_Identification);
//		if(i==null){
//			return 2880;//默认两天
//		}else{
//			return i.intValue();
//		}
//	}
//
//	@Override
//	public boolean putExpireToRedis(Integer time) {
//		try{
//		redisTemplate.opsForValue().set(Expire_Identification, time);
//		return true;
//		}catch(Exception e){
//		return false;
//		}
//	}
//
//	@Override
//	public void pushErrorTaskQueue(String taskHashcode, String direction){
//		if("R".equalsIgnoreCase(direction))
//		{
//			redisTemplate.opsForList().rightPush(ErrorTaskQuene, taskHashcode);
//		}
//		else
//		{
//			redisTemplate.opsForList().leftPush(ErrorTaskQuene, taskHashcode);
//		}
//	}
//
//	@Override
//	public Task getErrorTask(){
//		try
//		{
//			String taskHashcode = (String) redisTemplate.opsForList().rightPop(ErrorTaskQuene);
//			if(StringUtils.isBlank(taskHashcode))
//			{
//				return null;
//			}
//			return getTaskFromTaskInfoHash(taskHashcode);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	*/
/**
//	 * 使用allRedisTemplate
//	 * @param result
//	 * @throws Exception
//	 *//*

//	@Override
//	public void pushResultToAllJobQueue(String result) throws Exception{
//		redisAllTemplate.opsForList().leftPush(AllJobListQueue, result);
//	}
//
//	@Override
//	public void pushResultIdToResultQueue(String resultId) {
//		redisTemplate.opsForList().leftPush(RESULTIDQUEUE, resultId);
//	}
//
//	@Override
//	public String popResultIdToResultQueue() {
//		return String.valueOf(redisTemplate.opsForList().rightPop(RESULTIDQUEUE));
//	}
//
//}
*/
