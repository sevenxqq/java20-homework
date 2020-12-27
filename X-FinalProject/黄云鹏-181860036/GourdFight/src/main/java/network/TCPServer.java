package network;

import java.net.*;

public class TCPServer { // TCP服务器类
	private String serverIP; // 服务器IP地址
	private String clientIP; // 客户端IP地址
	
	private int serverPort; // 服务器端口号
	private int clientPort; // 客户端端口号
	
	private boolean isAccept; // 客户端是否连接上了服务器
	
	// 初始化
	public TCPServer(String serverIP, int serverPort) {
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.isAccept = false;
	}
	
	// Getter
	public String getServerIP() { // 获取服务器IP地址
		return serverIP;
	}
	
	public int getServerPort() { // 获取服务器端口号
		return serverPort;
	}
	
	public String getClientIP() { // 获取客户端IP地址
		return clientIP;
	}
	
	public int getClientPort() { // 获取客户端端口号
		return clientPort;
	}
	
	public boolean isAccept() { // 判断客户端是否连上服务器
		return isAccept;
	}
	
	// Setter
	public void setClientIP(String clientIP) { // 设置客户端IP地址
		this.clientIP = clientIP;
	}
	
	public void setClientPort(int clientPort) { // 设置客户端端口号
		this.clientPort = clientPort;
	}
	
	// 启动服务器
	public void start() {
		try {
			ServerSocket serrverSocket = new ServerSocket(serverPort);
			
			Socket socket = serrverSocket.accept(); // 等待客户端连接上(阻塞状态)
			
			isAccept = true; // 客户端已经连接上
			
			new Thread(new TCPWorker(socket)).start(); // 启动工作线程
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
