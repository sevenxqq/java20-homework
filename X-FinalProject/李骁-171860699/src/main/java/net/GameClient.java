package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameClient implements Runnable { // Socket客户端
	private String host; // IP地址
	private int port; // 端口号

	Socket clientSocket; // 声明客户端的Socket连接
	PrintWriter out;
	BufferedReader in;
	boolean done = false;
	BufferedReader inFromUser;

	private ExecutorService exec = Executors.newCachedThreadPool(); // 线程池

	public GameClient(String host, int port) {
		this.host = host;
		this.port = port;
		//connect();// 调用连接方法
	}

	public void start() {
		exec.submit(this);
	}

	@Override
	public void run() {
		try {
			if (host.equals("localhost") || host.equals("127.0.0.1")) {// 判断IP地址(域名)如果是本机localhost
				clientSocket = new Socket(InetAddress.getLocalHost(), port);// 创建本地Socket连接
				// 如果该方法InetAddress.getLocalHost()报错，则要在sudo vi /private/etc/hosts
				// 中添加本机地址与你主机名的映射，类似 127.0.0.1 主机名
				// 然后终端执行命令 dscacheutil -flushcache，之后主机地址便可正常解析
			} else {
				clientSocket = new Socket(InetAddress.getByName(host), port);// 创建远程socket连接
			}

			out = new PrintWriter(clientSocket.getOutputStream(), true);// 往服务器写内容的数据流
			// 从服务器获得信息
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));// 接收服务器发送内容的输入流
			System.out.println("服务器信息：" + in.readLine());
			System.out.println("服务器信息：" + in.readLine());
			System.out.println("请输入>");
			inFromUser = new BufferedReader(new InputStreamReader(System.in)); // 用户输入
		} catch (SecurityException e) {
			System.out.println("连接服务器出现安全问题！" + e.getMessage());
		} catch (IOException e) {
			System.out.println("连接服务器出现I/O问题！" + e.getMessage());
		}

		while (!Thread.interrupted() && !done) {
			/*
			 * Platform.runLater(new Runnable() {
			 * 
			 * @Override public void run() { System.out.println("update"); update(); } });
			 */
			System.out.println("client update");
			update();
			try {
				Thread.sleep(20);
			} catch (Exception e) {
				Thread.currentThread().interrupt();
			}
		}
		close();
	}

	public void update() {
		try {
			while (!done) {
				/*System.out.println("等待输入...");
				String line = inFromUser.readLine();
				System.out.println("\n输入完毕");
				out.println(line);// 发送到服务器端 --该处的print要加ln，否则就会无法往服务器端传递消息
				if (line.equalsIgnoreCase("exit")) // 读到exit则结束循环
					done = true;
				*/
				System.out.println("等待服务器数据...");
				String info = in.readLine(); // 从服务器读取字符串
				System.out.println("服务器信息：" + info);// 显示从服务器发送来的数据
				if (!done)
					System.out.println("请输入>");
			}
		} catch (SecurityException e) {
			System.out.println("连接服务器出现安全问题！" + e.getMessage());
		} catch (IOException e) {
			System.out.println("连接服务器出现I/O问题！" + e.getMessage());
		}
		close();
	}

	public void close() {
		System.out.println("客户端关闭");
		done = true;
		try {
			if (clientSocket != null) {
				clientSocket.close();
			}
		} catch (Exception e) {
			System.out.println("关闭客户端出现问题" + e.getMessage());
		}
		exec.shutdownNow();
	}

	/*public void connect() {

		try {
			if (host.equals("localhost") || host.equals("127.0.0.1")) {// 判断IP地址(域名)如果是本机localhost
				clientSocket = new Socket(InetAddress.getLocalHost(), port);// 创建本地Socket连接
				// 如果该方法InetAddress.getLocalHost()报错，则要在sudo vi /private/etc/hosts
				// 中添加本机地址与你主机名的映射，类似 127.0.0.1 主机名
				// 然后终端执行命令 dscacheutil -flushcache，之后主机地址便可正常解析
			} else {
				clientSocket = new Socket(InetAddress.getByName(host), port);// 创建远程socket连接
			}

			out = new PrintWriter(clientSocket.getOutputStream(), true);// 往服务器写内容的数据流
			// 从服务器获得信息
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));// 接收服务器发送内容的输入流
			System.out.println("服务器信息：" + in.readLine());
			System.out.println("服务器信息：" + in.readLine());
			System.out.println("请输入>");
			inFromUser = new BufferedReader(new InputStreamReader(System.in)); // 用户输入
			while (!done) {
				System.out.println("等待输入...");
				// String line = stdin.readLine();// 获得从键盘输入的每行字符
				// String line = "exit";
				String line = inFromUser.readLine();
				System.out.println("\n输入完毕");
				out.println(line);// 发送到服务器端 --该处的print要加ln，否则就会无法往服务器端传递消息
				if (line.equalsIgnoreCase("exit")) // 读到exit则结束循环
					done = true;
				String info = in.readLine(); // 从服务器读取字符串
				System.out.println("服务器信息：" + info);// 显示从服务器发送来的数据
				if (!done)
					System.out.println("请输入>");
			}
			clientSocket.close(); // 关闭资源
		} catch (SecurityException e) {
			System.out.println("连接服务器出现安全问题！" + e.getMessage());
		} catch (IOException e) {
			System.out.println("连接服务器出现I/O问题！" + e.getMessage());
		}
	}*/
}