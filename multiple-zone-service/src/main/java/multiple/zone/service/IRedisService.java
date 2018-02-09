package multiple.zone.service;

import java.util.List;

import multiple.zone.commons.base.service.BaseService;
import multiple.zone.entity.User;

/**
 * redis方法封装
 * @Description
 * @Author lichao
 * @Date 2017年10月11日17:33:35
 */
public interface IRedisService{
	
	   public boolean set(String key, String value);  
	      
	    public String get(String key);  
	      
	    public boolean expire(String key,long expire);  
	      
	    public <T> boolean setList(String key ,List<T> list);  
	      
//	    public <T> List<T> getList(String key,Class<T> clz);  
	      
	    public long lpush(String key,Object obj);  
	      
	    public long rpush(String key,Object obj);  
	      
	    public String lpop(String key); 
}
