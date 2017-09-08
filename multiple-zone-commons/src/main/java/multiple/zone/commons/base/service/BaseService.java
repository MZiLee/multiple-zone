package multiple.zone.commons.base.service;

import java.util.List;

import multiple.zone.commons.base.core.ResultData;


/**
 * 通用服务基类
 *
 * @param <T>
 * @author lichao
 */
public interface BaseService<T> {

    public ResultData<T> add(T entity);

    public ResultData<T> update(T entity);

    public ResultData<T> saveOrUpdate(T entity);

    public ResultData<T> updateBySelective(T entity);

    public ResultData<T> delete(Object... ids);

    public int queryByCount(T entity);

    public ResultData<List<T>> queryByList(T entity);

    public ResultData<T> queryById(Object id);

}
