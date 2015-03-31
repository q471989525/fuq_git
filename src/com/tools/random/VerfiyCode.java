package com.tools.random;

import java.util.Random;

public class VerfiyCode {
	  private VerfiyCode(){
	        
	    }
	    /**
	     * 四个随机字符
	     */
	    public static String getSeqId(){
	        char[] c = {'0','1','2', '3', '4', '5', '6', '7', '8', '9'};
	        Random random = new Random();
	        StringBuffer sRand = new StringBuffer();                       
	        for (int i = 0; i < 4; i++) {                           
	            String rand = String.valueOf(c[Math.abs(random.nextInt()) %
	                                           c.length]);
	            sRand.append( rand );
	        }       
	        return sRand.toString();
	    }
	    /**
	     * 四个随机字符
	     */
	    public synchronized static String buildVerfiyCode(){
	        return getSeqId();
	    }
}
