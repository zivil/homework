import threading
import sys
import socket
import os
from stat import *

def recv_to_end(client_connection):
	while client_connection.recv(4096) == 0:
		break;

'''judge the char if it is as follow'''
def is_space(c):
	return (c == ' ' or c == '\t' or c == '\r' or c == '\n' or c == '\v' or c == '\f') 
	
'''to put the buf into custom'''
def get_line(sock, buf, size):
	i = 0
	c = '\0'
	
	while ((i < size - 1) and (c != '\n')):
		c = sock.recv(1).decode('utf-8')
		if(c != '\0'):
			if(c == '\r'):
				c = sock.recv(1)
				if(c != '\0' and c == '\n'):
					c = sock.recv(1)
				else:
					c = '\n'
			buf[i] = c
			i += 1
		else:
			c = '\n'
		buf[i] = '\0'
	return i

'''send the msg unimplemented'''
def unimplemented(client_connection):
	buf = b"""HTTP/1.0 501 Method Not Implemented\n
Content-Type: text/html\n
<HTML><HEAD><TITLE>Method Not Implemented\n
</TITLE></HEAD>\n
<BODY><P>HTTP request method not supported.\n
</BODY></HTML>\n"""
	client_connection.sendall(buf)

'''send the msg 404'''
def not_found(client_connection):
	buf=b'''HTTP/1.0 404 NOT FOUND\r\n
Content-Type: text/html\r\n
\r\n
<HTML><TITLE>Not Found</TITLE>\r\n
<BODY><P>The server could not fulfill\r\n
your request because the resource specified\r\n
is unavailable or nonexistent.\r\n
</BODY></HTML>\r\n'''
	client_connection.sendall(buf)

def bad_request(client_connection):
	buf=b'''HTTP/1.0 400 BAD REQUEST\n;
Content-type: text/html\n\n"
<P>Your browser sent a bad request,
such as a POST without a Content-Length.\n'''
	client_connection.sendall(buf)

def cat(client_connection, resource):
	buf = bytes(resource.read(1024).encode('utf-8'))
	while buf != b'':
		client_connection.sendall(buf)
		buf = bytes(resource.read(1024).encode('utf-8'))

def cannot_execute(client_connection):
	buf = b'''HTTP/1.0 500 Internal Server Error\r\n
Content-type: text/html\r\n
\r\n
<P>Error prohibited CGI execution.\r\n'''
	client_connection.sendall(buf)

def error_die():
	print()

def execute_cgi(client_connection, path, method, query_string):
	buf				= ['' for x in range(0, 1024)]
	pid				= None
	cgi_output      = None
	cgi_input		= None
	status			= 0
	i				= 0
	c				= ''
	numchars		= 1
	content_length  = -1
	
	if(method == 'GET'):
		recv_to_end(client_connection)

	else:
		numchars = get_line(client_connection, buf, 1024)
		while((numchars > 0) and buf[0] != '\n'):
			if(''.join(buf[0:15]) == 'Content-Length:'):
				content_length = int(buf[16])
			numchars = get_line(client_connection, buf, 1024)
		if content_length == -1:
			bad_request(client_connection)
			return

	client_connection.sendall(b"HTTP/1.0 200 OK\r\n")
	

	cgi_input = os.pipe()
	cgi_output = os.pipe()
	pid = os.fork()
	if pid == 0:
		query_env = None
		lengh_env = None
		os.dup2(cgi_output[1], 1)
		os.dup2(cgi_input[0], 0)
		os.close(cgi_output[0])
		os.close(cgi_input[1])
		meth_env = 'REQUEST_METHOD=%s' % method
		os.putenv('meth_env', meth_env)
		
		if(method == 'GET'):
			query_env = "QUERY_STRING=%s" % query_string
			os.putenv('query_env', query_env)
		else:
			length_env = "CONTENT_LENGTH=%d"% content_length
			os.putenv('length_env', length_env)
		
		os.execl(path, path)
		exit(0)
	else:
		os.close(cgi_output[1])
		os.close(cgi_input[0])
		foutput = os.fdopen(cgi_input[1], 'w')
		finput  = os.fdopen(cgi_output[0])
		if(method == 'POST'):
			for i in range(content_length):
				c = client_connection.recv(1).decode()
				foutput.write(c)
		
		while True:
			b = finput.read(1)
			if b == '':
				break;
			else:
				client_connection.sendall(bytes(b.encode('utf-8')))
		os.close(cgi_input[1])
		os.close(cgi_output[0])
		os.waitpid(pid, status)

def headers(client_connection):
	buf=b'''HTTP/1.0 200 OK\r\n
Content-Type: text/html\r\n
\r\n'''
	client_connection.sendall(buf)

def serve_file(client_connection, path):
	resource = None
	numchars = 1
	buf      = ['' for x in range(0, 1024)]
	
	recv_to_end(client_connection)
	
	try:
		resource = open(path, 'r')
	except Exception as e:
		print(e)
		not_found(client_connection)
		return

	headers(client_connection)
	cat(client_connection, resource)
	resource.close()

'''recv and deal with the msg'''
def accept_request(client_connection):
	buf      = ['' for x in range(0, 1024)]
	numchars = 0
	method   = ['' for x in range(0, 255)]
	url      = ['' for x in range(0, 255)]
	i        = 0
	j        = 0
	cgi      = 0
	path     = None
	query_string = ''

	'''deal with method line'''
	numchars = get_line(client_connection, buf, 1024)
	
	i = 0
	j = 0
	'''deal with method name'''
	while (not is_space(buf[j]) and (i < len(method) - 1)):
		method[i] = buf[j]
		i += 1
		j += 1
	method = ''.join(method).upper()

	if (method != 'GET' and method != 'POST'):
		recv_to_end(client_connection)
		unimplemented(client_connection)
		client_connection.close()
		return
	
	if (method == 'POST'):
		cgi = 1
	
	'''deal with method URL'''
	i = 0
	while (is_space(buf[j]) and (j < len(buf))):
		j += 1
	while ((not is_space(buf[j])) and (i < len(url) - 1) and (j < len(buf))):
		url[i] = buf[j]
		i += 1
		j += 1

	url    = ''.join(url)

	'''deal with method GET'''
	if(method == 'GET'):
		query_string = url+'\0'
		query_it = 0
		while((query_string[query_it] != '?') and (query_string[query_it] != '\0')):
			query_it += 1
		if(query_string[query_it] == '?'):
			cgi = 1
			query_string[query_it] = '\0'
			query_it += 1

	'''deal with method PATH'''
	path = 'htdocs%s' % (url)
	if(path[(len(path) - 1)] == '/'):
		path = path + "index.html"
	
	try:
		st   = os.stat(path)
		mode = st.st_mode
		if (S_ISDIR(mode)):
			path += '/index.html'
		if (S_IXUSR & mode or S_IXGRP & mode or S_IXOTH & mode):
			cgi = 1
	except Exception as e:
		print(e)
		recv_to_end(client_connection)
		not_found(client_connection)
		client_connection.close()
		return

	if (cgi != 1):
		serve_file(client_connection, path)
	else: 
		execute_cgi(client_connection, path, method, query_string)
	client_connection.close()
 
HOST, PORT = '', 51596
listen_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
listen_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
listen_socket.bind((HOST, PORT))
listen_socket.listen(1)
print ('Serving HTTP on port %s ...' % PORT)
while True:
	client_connection, client_address = listen_socket.accept()
		
	handle_thread = threading.Thread(target = accept_request, args = (client_connection,))
	handle_thread.start()
	handle_thread.join()