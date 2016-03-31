package com.dabaicong.designModel.proxy;

import java.util.List;

public abstract class AbstractEntry  implements IEntry{
	
	public String name ; 
	public List<Integer> numberList;
	
	@Override
	public String toString() {
		return name+numberList.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Integer> getNumberList() {
		return numberList;
	}

	public void setNumberList(List<Integer> numberList) {
		this.numberList = numberList;
	}
}
