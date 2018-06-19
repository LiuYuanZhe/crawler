package com.sdust.crawler.parser.inject;

import com.sdust.crawler.parser.Inject;
import com.sdust.crawler.parser.bean.SpiderBean;
import com.sdust.crawler.parser.request.HttpRequest;
import com.sdust.crawler.parser.response.HttpResponse;
import net.sf.cglib.beans.BeanMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * render抽象方法，主要包括注入基本的属性和自定义属性注入。将特定的html、json、xml注入放入实现类
 *
 * @author huchengyi
 */
public abstract class AbstractInject implements Inject {

    private static Log log = LogFactory.getLog(AbstractInject.class);

    @Override
    public SpiderBean inject(Class<? extends SpiderBean> clazz, HttpRequest request, HttpResponse response) {
        try {
            SpiderBean bean = clazz.newInstance();
            BeanMap beanMap = BeanMap.create(bean);
            fieldInject(request, response, beanMap, bean);
            return bean;
        } catch (Exception ex) {
            //throw new RenderException(ex.getMessage(), clazz);
            log.error("instance SpiderBean error", ex);
            return null;
        }
    }

    public abstract void fieldInject(HttpRequest request, HttpResponse response, BeanMap beanMap, SpiderBean bean);

}
