package com.sdust.crawler.others;//package com.mobanker.crawler.service;
//
//import com.mobanker.crawler.common.model.Task;
//
//import java.util.Set;
//
//public interface ICrawlerRedisQueueService {
//
//	/**
//	 *
//	 * @param vo
//	 * @param removeDuplicated
//	 * @return
//	 */
////	boolean pushTasksToWaitingQueue(SpiderVO vo, boolean removeDuplicated);
////	boolean pushTasksToWaitingQueue(SpiderVO vo);
//
//	boolean pushTaskToWaitingQueue(Task task);
//
//	/**
//	 *
//	 * @return
//	 */
////	SpiderVO getTaskFromWaitingQueue();
////	Task getTaskFromWaitingQueue();
//	Task getTaskFromTaskInfoHash(String taskHashcode);
////	 void setFlag(boolean flag);
//
//	 boolean pushResultToQueryQueue(String key, String value) throws Exception;
//	 String queryResult(String key);
//
//	 Task getTaskByTaskId(String taskid);
////	 String getCodeByTaskId(String taskid);
////	 boolean pushCodeToRedis(String taskHashcode,String code);
//
//
//	 /**
//		 * redis新增验证码信息
//		 * @param validateType 验证码类型:1-cell;2-img
//		 * @param taskHashcode
//		 * @return
//		 */
////		boolean putValidateToValidateInfoHash(String validateType, String taskHashcode, Object value);
//
//		/**
//		 * redis删除验证码信息
//		 * @param validateType 验证码类型:1-cell;2-img
//		 * @param taskHashcode
//		 * @return
//		 */
////		boolean delValidateInfoFromValidateInfoHash(String validateType, String taskHashcode);
//
//		/**
//		 * redis查询验证码记录是否存在，用于验证是否需要多次登录
//		 * @param validateType 验证码类型:1-cell;2-img
//		 * @param taskHashcode
//		 * @return
//		 */
////		boolean isExitFromValidateInfoHash(String validateType, String taskHashcode);
//
////		String getValidateFromValidateInfoHash(String validateType, String taskHashcode);
//
//
//		boolean putWorkerTaskCount(String contextPath);
//
//		boolean minusWorkerTaskCount(String contextPath);
//
//		boolean initTaskCount(String contextPath);
//
//		boolean maxTaskCount(String contextPath);
//
//		String getMinTaskUrl(int index);
//
//		/**
//		 * redis记录
//		 * @param taskHashcode
//		 * @param taskStatus
//		 * @return
//		 */
//		boolean setTaskStatusFromTaskStatusInfoHash(String taskHashcode, String taskStatus);
//
//		/**
//		 * 获取爬虫任务状态
//		 * @param taskHashcode
//		 * @return
//		 */
//		String getTaskStatusFromTaskStatusInfoHash(String taskHashcode);
//
//		boolean plusWorkerThreads(String contextPath, int i);
//
//		/**
//		 * 插入taskId 和woker的对应关系
//		 */
//		boolean setTaskWorkerRelation(String taskId, String worker);
//		String getWorkerByTaskId(String taskId);
//
//		public boolean plusQueryMysqlCount(String webSite);
//
//		public String getTasknumByUrl(String workerUrl);
//
//		boolean initSystemConfig(String workUrl);
//
//		boolean updateSystemConfig(String confKey, String confVal);
//
//		String getSystemConfig(String confKey);
//
//		Set<String> queryAllWorker();
//
//		boolean putExpireToRedis(Integer time);
//
////		List<Map> queryHashDatas(String identification, String matckKey);
//
//		void pushErrorTaskQueue(String taskHashcode, String direction);
//
//		Task getErrorTask();
//
//		public void pushResultToAllJobQueue(String result) throws Exception;
//
//		void pushResultIdToResultQueue(String resultId);
//
//		String popResultIdToResultQueue();
//
//}
