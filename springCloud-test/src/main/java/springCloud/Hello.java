package springCloud;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Hello {

    public static void main(String[] args) {
//        int i = 8 | 8;
//        System.out.println("======" + i);
        //1011
        //1100
        //0111


        RealObject realObject = new RealObject();
        realObject.doSomething();
        System.out.println("======================");

        SimpleProxy simpleProxy = new SimpleProxy(realObject);
        simpleProxy.consumer();

        Interface iter = (Interface)Proxy.newProxyInstance(Interface.class.getClassLoader(), new Class[]{Interface.class}, new DynamicProxy(realObject));
        iter.doSomething();
        System.out.println("======================");

        SelectInter selectInter = (SelectInter)Proxy.newProxyInstance(SelectInter.class.getClassLoader(), new Class[] {SelectInter.class}, new DynamicSelectProxy(new SelectInterImpl()));
        selectInter.doSomething();
        selectInter.selectOne();
        selectInter.selectTwo();
    }
}


interface Interface{
    void doSomething();
}

class RealObject implements Interface {
    @Override
    public void doSomething() {
        System.out.println("real do some thing");
    }
}

class SimpleProxy implements Interface{

    private Interface iter;

    SimpleProxy(Interface inter) {
        this.iter = inter;
    }
    void consumer() {
        doSomething();
        iter.doSomething();
    }
    @Override
    public void doSomething() {
        System.out.println("SimpleProxy do some thing");
    }
}

class DynamicProxy implements InvocationHandler {
    private Object obj;

    DynamicProxy(Object obj) {
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(obj, args);
    }
}

interface SelectInter {
    void doSomething();
    void selectOne();
    void selectTwo();
}

class SelectInterImpl implements SelectInter {
    @Override
    public void doSomething() {
        System.out.println("select impl doSomething do some thing");
    }

    @Override
    public void selectOne() {
        System.out.println("select impl selectOne do some thing");
    }

    @Override
    public void selectTwo() {
        System.out.println("select impl do selectTwo some thing");
    }
}

class DynamicSelectProxy implements InvocationHandler {
    private Object obj;

    DynamicSelectProxy(Object obj) {
        this.obj = obj;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("doSomething".equals(method.getName())) {
            System.out.println("this doSomething proxy");
        }
        return method.invoke(obj, args);
    }
}