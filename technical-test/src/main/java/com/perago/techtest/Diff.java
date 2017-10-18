package com.perago.techtest;

import java.io.Serializable;

/**
 * The object representing a diff.
 * Implement this class as you see fit. 
 *
 */
public class Diff<T extends Serializable> {
	
	
	public String crudIndicator;
	
	public Diff() {
	      
	}

	public String getCrudIndicator() {
		return crudIndicator;
	}

	public void setCrudIndicator(String crudIndicator) {
		this.crudIndicator = crudIndicator;
	}

}
