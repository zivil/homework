<% int a = 0; %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link type="text/css" href="css/success.css" rel = "stylesheet">
<title>�ɹ�</title>
</head>
<body>
<center><%=a %>��ϲ���ɹ�ע�᱾���У�&nbsp;���μ��������룺</center>
<br />
<center><font color=red><% Map<String, String> param = request.getParameterMap();for (Map.Entry<String, String> entry : param.entrySet()) { out.println(entry.getKey() + entry.getValue()); }  %></font></center>
</body>
</html>