package com.perago.techtest;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * The object representing a diff.
 * Implement this class as you see fit. 
 *
 */
public class DiffService implements	 DiffEngine{

	public <T extends Serializable> T apply(T original, Diff <?> diff)
			throws DiffException {

		Diff diff2=calculate(original,(T)diff); 
		if(diff2==null){
			return null;
		}			
		return (T)diff2;
	}

	public <T extends Serializable> Diff<T> calculate(T original, T modified)
			throws DiffException {
	
		Diff diff=null;	
		try {

			Object obj=compareBeans(original, modified);
			
			if(obj ==null && original!=null && modified==null){
				//for deleted 
				return diff;
				
			}
			diff=(Diff)obj;
			
			if(original==null && modified!=null){
				
				 diff.setCrudIndicator("created");
			}				
			else{
				diff.setCrudIndicator("updated");
			}
			
		} catch (IOException e) {
			throw new DiffException(e.getMessage());
			
		} catch (ClassNotFoundException e) {
			throw new DiffException(e.getMessage());
		}
	    catch (IntrospectionException e) {
	    	throw new DiffException(e.getMessage());
	    } catch (IllegalAccessException e) {
	    	throw new DiffException(e.getMessage());
	    } catch (InvocationTargetException e) {
	    	throw new DiffException(e.getMessage());
	    }
	return diff;
	}

	public static Object compareBeans(Object bean1, Object bean2 )
	          throws IntrospectionException,
	          IllegalAccessException, InvocationTargetException, IOException, ClassNotFoundException,DiffException {
		
		  if(bean1==null && bean2!=null){
			  
			  return bean2;
		  }
		  else if(bean1!=null && bean2==null){
			  
			  return null;
		  }
		  		
		  Class c=bean1.getClass();
		  Field[] fields=c.getDeclaredFields();
		  String []propertyNames=new String[fields.length];
		  
		  DiffService ds=new DiffService();
		  Object bean3=ds.makeClone(bean2);
		  
			
		  for (int i = 0; i < fields.length; i++) {
			  propertyNames[i]=fields[i].getName();
		   }
	        Set<String> names =new HashSet();
	        Collections.addAll(names, propertyNames); 
	        BeanInfo beanInfo = Introspector.getBeanInfo(bean1
	            .getClass());
	        Object value2=null;
         for (PropertyDescriptor prop : beanInfo.getPropertyDescriptors()) {
	          if (names.remove(prop.getName())) {
	            Method getter = prop.getReadMethod();
	            Method setter = prop.getWriteMethod();
	            Object value1 = getter.invoke(bean1);
	            value2= getter.invoke(bean3); 
	            if (value1 == value2
	                || (value1 != null && value1.equals(value2))) {

	            	try {
						setInstanceValue(bean3, prop.getName(), null);
					} catch (SecurityException e) {
						throw new DiffException(e.getMessage());
						
					} catch (IllegalArgumentException e) {
						throw new DiffException(e.getMessage());
					} catch (NoSuchFieldException e) {
						throw new DiffException(e.getMessage());
					}

            	  //System.out.println("Matched Property = "+prop.getName() +" Value of been1 ="+value1 +" : Value of bean3 ="+value2);            	  
	              continue;
	            }

	            //System.out.println("Property = "+prop.getName() +" Value of been1 ="+value1 +" : Value of bean3 ="+value2);	            
	          }
	      }
        
        return bean3;
	        
	      }
	

	
	public static void setInstanceValue(final Object classInstance, final String fieldName, final Object newValue) throws SecurityException,
				NoSuchFieldException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
		// Get the private field
		final Field field = classInstance.getClass().getDeclaredField(fieldName);
		// Allow modification on the field
		field.setAccessible(true);
		// Sets the field to the new value for this instance
		field.set(classInstance, newValue);
	
	}
	

	
	public Object makeClone(Object bean2) throws IOException, ClassNotFoundException {
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    ObjectOutputStream out = new ObjectOutputStream(outputStream);
	    out.writeObject(bean2);
	
	    ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
	    ObjectInputStream in = new ObjectInputStream(inputStream);
	    Object clonedObject =  in.readObject();
	    return clonedObject;
	}

}
