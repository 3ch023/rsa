<html>
<head>
    <title>3ch Chat</title>
</head>
<body>
<div style="float: left;">
    <h2>3ch Chat!</h2>

    <form method="POST" action="testdh">
        <input type="submit" id="enter" value="Enter chat">
    </form>
</div>
<div>
   <h2>Log</h2>
   <%= request.getAttribute("log") %>
</div>
</body>
</html>
