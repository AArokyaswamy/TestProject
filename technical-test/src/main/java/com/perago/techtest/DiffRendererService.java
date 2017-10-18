package com.perago.techtest;

/**
 * The object representing a diff.
 * Implement this class as you see fit. 
 *
 */
public class DiffRendererService implements DiffRenderer   {

	public String render(Diff<?> diff) throws DiffException {
		if(diff==null){
			return "deleted";
		}
		return diff.getCrudIndicator();
	}
	



}
