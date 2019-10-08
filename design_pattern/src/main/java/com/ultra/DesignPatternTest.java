package com.ultra;

import com.ultra.observer.ISubject;

import com.ultra.chain.FaceFilter;
import com.ultra.chain.FilterChain;
import com.ultra.chain.HTMLFilter;
import com.ultra.chain.Request;
import com.ultra.chain.Response;
import com.ultra.chain.SensitiveFilter;
import com.ultra.factory.abstr.AbstactFactory;
import com.ultra.factory.abstr.Button;
import com.ultra.factory.abstr.Text;
import com.ultra.factory.abstr.impl.LinuxFactory;
import com.ultra.factory.abstr.impl.WindowsFactory;
import com.ultra.factory.method.IMessage;
import com.ultra.factory.method.IMessageFactory;
import com.ultra.factory.method.impl.EmailMessageFactory;
import com.ultra.factory.method.impl.SmsMessageFactory;
import com.ultra.factory.simple.Conn;
import com.ultra.factory.simple.SimpleFactory;
import com.ultra.observer.IObserver;
import com.ultra.observer.impl.DataSubject;
import com.ultra.observer.impl.CacheObserver;
import com.ultra.observer.impl.DbObserver;
import org.junit.jupiter.api.Test;

/**
 * Hello world!
 */
public class DesignPatternTest {
    // =============================工厂模式=================================================

    /**
     * @return void
     * @Title testSimpleFactory 违背开闭原则（对于扩展是开放的，对于修改是封闭的）
     * @Description 简单工厂模式缺点:①所有的实现提前写好,扩展性差(添加新类型,需要修改工厂类).②不同的产品需要不同额外参数
     * create()方法是静态的所以又叫静态工厂模式
     */
    @Test
    public void testSimpleFactory() {
        Conn conn = SimpleFactory.create(1);
        conn.db();
    }

    /**
     * @return void
     * @throws Exception
     * @Title testMethodFactory
     * @Description 工厂方法模式:产品和工厂都是接口,扩展时实现工厂和产品接口
     */
    @Test
    public void testMethodFactory() throws Exception {

        IMessageFactory sms = new SmsMessageFactory();
        IMessage smsm = sms.create();
        smsm.sendMesage();

        IMessageFactory email = new EmailMessageFactory();
        IMessage emailm = email.create();
        emailm.sendMesage();
    }

    /**
     * @return void
     * @Title testAbstrFactory
     * @Description 抽象工厂模式:系统的产品多于一个产品族,而系统只消费某一族的产品.
     */
    @Test
    public void testAbstrFactory() throws Exception {
        AbstactFactory windowsFactory = new WindowsFactory();
        Button windowsButton = windowsFactory.createButton();
        windowsButton.process();
        Text windowsText = windowsFactory.createText();
        windowsText.getAll();
        AbstactFactory linuxFactory = new LinuxFactory();
        Button linuxButton = linuxFactory.createButton();
        linuxButton.process();
        Text linuxText = linuxFactory.createText();
        linuxText.getAll();

    }

    // =============================观察者模式=================================================

    /**
     * @return void
     * @Title testOberver
     * @Description 观察者模式
     */
    @Test
    public void testOberver() {
        DataSubject subject = new DataSubject();
        IObserver db = new DbObserver();
        subject.registerObserver(db);
        IObserver cache = new CacheObserver();
        subject.registerObserver(cache);
        subject.setMessage("success");
    }

    // =============================责任链模式=================================================
    @Test
    public void testChain() {
        // 设定过滤规则，对msg字符串进行过滤处理
        String msg = ":):,<script>,敏感,被就业,网络授课";
        // 过滤请求
        Request request = new Request();
        // set方法，将待处理字符串传递进去
        request.setRequest(msg);
        // 处理过程结束，给出的响应
        Response response = new Response();
        // 设置响应信息
        response.setResponse("response:");
        // FilterChain,过滤规则形成的拦截链条
        FilterChain fc = new FilterChain();
        // 规则链条添加过滤规则，采用的是链式调用
        fc.addFilter(new HTMLFilter()).addFilter(new SensitiveFilter()).addFilter(new FaceFilter());
        // 按照FilterChain的规则顺序，依次应用过滤规则
        fc.doFilter(request, response, fc);
        // 打印请求信息
        System.out.println(request.getRequest());
        // 打印响应信息
        System.out.println(response.getResponse());
        /*
         * 处理器对数据进行处理 --|----|---数据--|-----|--- 规则1 规则2 规则3 规则4
         */
    }
}
