import socket
import threading

def msgPos(sock):
	connection,address = sock.accept()
	buf = connection.recv(1024).decode('utf-8')[::-1]
	connection.send(bytes(buf.encode('utf-8')))
	connection.close()


sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.bind(('localhost', 8001))
sock.listen(5)
while True:
	try:
		t = threading.Thread(msgPos(sock))
		t.start()
		t.join()
	except socket.timeout:
		print ('time out')
	