<html>
<head>
    <title>3ch Chat</title>
</head>
<body>
<div style="float: left;">
    <h2>Hi, <%= request.getAttribute("user") %>!</h2>

    <form method="POST" action="chat">
        Type your message </br>
        <textarea id="text" name="text"></textarea>
        </br>
        <input type="hidden" id="publicKey" name="publicKey" value="<%= request.getAttribute("publicKey") %>">
        <input type="hidden" id="user" name="user" value="<%= request.getAttribute("user") %>">
        <input type="submit" id="send" value="Send">
    </form>
</div>
<div style="width: 40%; float: right;">
   <h2>Log</h2>
   <%= request.getAttribute("log") %>
</div>
</body>
</html>
