package com.foxer;

public class Main {

	public static void main(String[] args) {
		Service service = new Service();
		service.setPort(8888);
		service.serve();
	}

}
