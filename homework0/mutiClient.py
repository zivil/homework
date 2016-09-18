import socket
import threading

def aConnect(i):
	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	sock.connect(('localhost', 8001))
	sock.send(bytes(("Thread:"+str(i)+"\n").encode('utf-8')))
	print (sock.recv(1024).decode('utf-8'))
	sock.close()
	
for i in range(10):
	t = threading.Thread(aConnect(i))
	t.start()
	t.join()