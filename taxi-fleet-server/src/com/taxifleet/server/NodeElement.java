package com.taxifleet.server;

import java.io.Serializable;
public interface NodeElement<T> extends Serializable, Cloneable {

	public T getNextNode();

	public void setNextNode(T node);

	public T clone();
}
