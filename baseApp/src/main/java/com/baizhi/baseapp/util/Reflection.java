package com.baizhi.baseapp.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**   
 * Java Reflection Cookbook   
 *   
 * @author Michael Lee   
 * @since 2006-8-23   
 * @version 0.1a   
 */   
   
public class Reflection {    
    /**   
     * �õ�ĳ�����Ĺ�������   
     *   
     * @param owner, fieldName   
     * @return �����Զ���   
     * @throws Exception   
     *   
     */   
    public Object getProperty(Object owner, String fieldName) throws Exception 
{    
        Class ownerClass = owner.getClass();    
   
        Field field = ownerClass.getField(fieldName);    
   
        Object property = field.get(owner);    
   
        return property;    
    }    
   
    /**   
     * �õ�ĳ��ľ�̬��������   
     *   
     * @param className   ����   
     * @param fieldName   ������   
     * @return �����Զ���   
     * @throws Exception   
     */   
    public Object getStaticProperty(String className, String fieldName)    
            throws Exception {    
        Class ownerClass = Class.forName(className);    
   
        Field field = ownerClass.getField(fieldName);    
   
        Object property = field.get(ownerClass);    
   
        return property;    
    }    
   
   
    /**   
     * ִ��ĳ���󷽷�   
     *   
     * @param owner   
     *            ����   
     * @param methodName   
     *            ������   
     * @param args   
     *            ����   
     * @return ��������ֵ   
     * @throws Exception   
     */   
    public Object invokeMethod(Object owner, String methodName, Object[] args) 
   
            throws Exception {    
   
        Class ownerClass = owner.getClass();    
   
        Class[] argsClass = new Class[args.length];    
   
        for (int i = 0, j = args.length; i < j; i++) {    
            argsClass[i] = args[i].getClass();    
        }    
   
        Method method = ownerClass.getMethod(methodName, argsClass);    
   
        return method.invoke(owner, args);    
    }    
   
   
      /**   
     * ִ��ĳ��ľ�̬����   
     *   
     * @param className   
     *            ����   
     * @param methodName   
     *            ������   
     * @param args   
     *            ��������   
     * @return ִ�з������صĽ��   
     * @throws Exception   
     */   
    public Object invokeStaticMethod(String className, String methodName,    
            Object[] args) throws Exception {    
        Class ownerClass = Class.forName(className);    
   
        Class[] argsClass = new Class[args.length];    
   
        for (int i = 0, j = args.length; i < j; i++) {    
            argsClass[i] = args[i].getClass();    
        }    
   
        Method method = ownerClass.getMethod(methodName, argsClass);    
   
        return method.invoke(null, args);    
    }    
   
   
   
    /**   
     * �½�ʵ��   
     *   
     * @param className   
     *            ����   
     * @param args   
     *            ���캯��Ĳ���   
     * @return �½���ʵ��   
     * @throws Exception   
     */   
    public Object newInstance(String className, Object[] args) throws 
Exception {    
        Class newoneClass = Class.forName(className);    
   
        Class[] argsClass = new Class[args.length];    
   
        for (int i = 0, j = args.length; i < j; i++) {    
            argsClass[i] = args[i].getClass();    
        }    
   
        Constructor cons = newoneClass.getConstructor(argsClass);    
   
        return cons.newInstance(args);    
   
    }    
   
   
        
    /**   
     * �ǲ���ĳ�����ʵ��   
     * @param obj ʵ��   
     * @param cls ��   
     * @return ��� obj �Ǵ����ʵ���򷵻� true   
     */   
    public boolean isInstance(Object obj, Class cls) {    
        return cls.isInstance(obj);    
    }    
        
    /**   
     * �õ������е�ĳ��Ԫ��   
     * @param array ����   
     * @param index ����   
     * @return ����ָ��������������������ֵ   
     */   
    public Object getByArray(Object array, int index) {    
        return Array.get(array,index);    
    }
    
    public void invokeVoidMethod(Object owner, String methodName, boolean property) {
        try {
            Method method = owner.getClass().getMethod(methodName, boolean.class);
            method.invoke(owner, property);
        } catch (Exception e) {

        }  
    }

    /**
     * 兼容参数为基本类型的情形
     * @param ownerObj
     * @param methodName
     * @param parameterTypes
     * @param params
     * @return
     */
    public Object invokeMethod( Object ownerObj, String methodName, Class<?>[] parameterTypes,
            Object[] params ) {
        try {
            Class<?> ownerType = ownerObj.getClass();
            Method method = ownerType.getMethod( methodName, parameterTypes );
            return method.invoke( ownerObj, params );
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    /**
     * 兼容参数为基本类型的情形(会抛出异常)
     * @param ownerObj
     * @param methodName
     * @param parameterTypes
     * @param params
     * @return
     * @throws NoSuchMethodException 
     * @throws SecurityException 
     * @throws java.lang.reflect.InvocationTargetException
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
	public Object invokeParamsMethod(Object ownerObj, String methodName,
			Class<?>[] parameterTypes, Object[] params)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Class<?> ownerType = ownerObj.getClass();
		Method method = ownerType.getMethod(methodName, parameterTypes);
		method.setAccessible(true);
		return method.invoke(ownerObj, params);
    }

    /**
     * 兼容参数为基本类型的情形
     * @param className
     * @param methodName
     * @param parameterTypes
     * @param params
     * @return
     */
    public Object invokeStaticMethod( String className, String methodName,
            Class<?>[] parameterTypes, Object[] params ) {
        try {
            Class<?> ownerClass = Class.forName( className );
            Method method = ownerClass.getMethod( methodName, parameterTypes );

            return method.invoke( null, params );
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    /**
     * 兼容参数为基本类型的情形
     * @param c
     * @param methodName
     * @param parameterTypes
     * @param params
     * @return
     */
    public Object invokeStaticMethod( Class c, String methodName,
    		Class<?>[] parameterTypes, Object[] params ) {
    	try {
    		Method method = c.getMethod( methodName, parameterTypes );
    		
    		return method.invoke( null, params );
    	}
    	catch (SecurityException e) {
    		e.printStackTrace();
    	}
    	catch (NoSuchMethodException e) {
    		e.printStackTrace();
    	}
    	catch (IllegalArgumentException e) {
    		e.printStackTrace();
    	}
    	catch (IllegalAccessException e) {
    		e.printStackTrace();
    	}
    	catch (InvocationTargetException e) {
    		e.printStackTrace();
    	}
    	
    	return null;
    }
}

