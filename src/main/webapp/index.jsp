<html>
<head>
    <script src="http://code.jquery.com/jquery-1.8.3.min.js"></script>
        <script src="/rsa/jsencrypt.js"></script>
        <script type="text/javascript">
          $(function() {
            $('#submit').click(function() {

            alert('mimi');
              // Encrypt with the public key...
              var encrypt = new JSEncrypt();
              encrypt.setPublicKey($('#publicKey').val());
              var encrypted = encrypt.encrypt($('#text').val());
              $('#text').val(encrypted);

              /* Decrypt with the private key...
              var decrypt = new JSEncrypt();
              decrypt.setPrivateKey($('#privkey').val());
              var uncrypted = decrypt.decrypt(encrypted);

              // Now a simple check to see if the round-trip worked.
              if (uncrypted == $('#text').val()) {
                alert('It works!!!');
              }
              else {
                alert('Something went wrong....');
              }
              */
            });
          });
    </script>
    <title>RSA test</title>
</head>
<body>
<h2>RSA test!</h2>
    <form method="POST" action="testrsa">
        <textarea id="text" name="text">Enter text here...</textarea>
        </br>
        <input type="hidden" id="publicKey" name="publicKey"
            value="<%= request.getAttribute("publicKey") %>">
        <input type="submit" id="submit" value="Submit">
    </form>
</body>
</html>
