package dehua.java.dynamic;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtField.Initializer;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory;

public class JavassistDemo {
	
	public String className = "com.dehua.JavassistClass";
	
	public HashMap<String, CtClass> clzMap = new HashMap<String, CtClass>();

	/**
	 * 生成 com.dehua.JavassistClass
	 * @throws NotFoundException 
	 * @throws Exception 
	 */
	ClassPool cp=ClassPool.getDefault();  
	public Class genericClass() throws Exception{
        CtClass ctClass=cp.makeClass(className);  
          
        StringBuffer body=null;  
        //参数  1：属性类型  2：属性名称  3：所属类CtClass  
        CtField ctField=new CtField(cp.get("java.lang.String"), "name", ctClass);  
        ctField.setModifiers(Modifier.PRIVATE);  
        //设置name属性的get set方法  
        ctClass.addMethod(CtNewMethod.setter("setName", ctField));  
        ctClass.addMethod(CtNewMethod.getter("getName", ctField));  
        ctClass.addField(ctField, Initializer.constant("default"));  
          
        //参数  1：参数类型   2：所属类CtClass  
        CtConstructor ctConstructor=new CtConstructor(new CtClass[]{}, ctClass);  
        body=new StringBuffer();  
        body.append("{\n name=\"me\";\n}");  
        ctConstructor.setBody(body.toString());  
        ctClass.addConstructor(ctConstructor);  
          
        //参数：  1：返回类型  2：方法名称  3：传入参数类型  4：所属类CtClass  
        CtMethod ctMethod=new CtMethod(CtClass.voidType,"execute",new CtClass[]{},ctClass);  
        ctMethod.setModifiers(Modifier.PUBLIC);  
        body=new StringBuffer();  
        body.append("{\n System.out.println(name);");  
        body.append("\n System.out.println(\"execute ok\");");  
        body.append("\n return ;");  
        body.append("\n}");  
        ctMethod.setBody(body.toString());  
        ctClass.addMethod(ctMethod);  
        Class<?> c=ctClass.toClass();  
        Object o=c.newInstance();  
        Method method=o.getClass().getMethod("execute", new Class[]{});  
        //调用字节码生成类的execute方法  
        method.invoke(o, new Object[]{});  
        
        clzMap.put(className, ctClass);
        
        return c;
	}
	
	public void interceptor(Class<?> clz) throws Exception{
		ProxyFactory factory=new ProxyFactory();  
        //设置父类，ProxyFactory将会动态生成一个类，继承该父类  
		System.out.println(clz.getName());
        factory.setSuperclass(clz);  
        //设置过滤器，判断哪些方法调用需要被拦截  
        factory.setFilter(new MethodFilter() {  
            @Override  
            public boolean isHandled(Method m) {  
                if(m.getName().equals("getName")){  
                	System.out.println("xxx");
                    return true;  
                }  
                return false;  
            }  
        });  
        
        MethodHandler mHandler = new MethodHandler() {  
            @Override  
            public Object invoke(Object self, Method thisMethod, Method proceed,  
                    Object[] args) throws Throwable {  
                //拦截后前置处理，改写name属性的内容  
                //实际情况可根据需求修改  
            	System.out.println("invoke");
            	
                Method setName = self.getClass().getMethod("setName", String.class);
                setName.invoke(self, "modified name");  
                
                //Method getName = self.getClass().getMethod("getName");
                //System.out.println(getName.invoke(self));  
                
                return proceed.invoke(self, args);  
            }  
        };
        
        
        //设置拦截处理  
        //Class<?> c = factory.createClass();
        //((Proxy)c.newInstance()).setHandler(mHandler);  
        factory.setHandler(mHandler);
        
        Class<?> clx =factory.createClass();   
        Method getName = clx.getMethod("getName");
        
        /*Method[] methods = clz.getMethods();
        for(Method method : methods){
        	if(method == null) continue;
        	System.out.println(method.getName() + ", " + method.getParameterCount());
        }*/
        System.out.println(getName.invoke(clx.newInstance()));  
	}
	
	public static void main(String[] args) throws Exception {
		JavassistDemo demo = new JavassistDemo();
		Class<?> clz = demo.genericClass();
		demo.interceptor(clz);
	}
	
}
