<html>
<head>
    <title>3ch Chat</title>
</head>
<body>
<div style="float: left;">
    <h2>3ch Chat!</h2>

    <form method="POST" action="sendmessage">
        Type your message </br>
        <textarea id="text" name="text"></textarea>
        </br>
        <input type="hidden" id="publicKey" name="publicKey"
            value="<%= request.getAttribute("publicKey") %>">
        <input type="submit" id="send" value="Send">
    </form>
</div>
<div>
   <h2>Log</h2>
   <%= request.getAttribute("log") %>
</div>
</body>
</html>
