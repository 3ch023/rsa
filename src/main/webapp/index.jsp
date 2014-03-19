<html>
<head>
    <!-- <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"/>
    <script type="javascript">
    $( document ).ready(function() {
      alert('mimi');
    });
    </script> -->

    <script src="http://code.jquery.com/jquery-1.8.3.min.js"></script>
        <script src="bin/jsencrypt.min.js"></script>
        <script type="text/javascript">

          // Call this code when the page is done loading.
          $(function() {

            // Run a quick encryption/decryption when they click.
            $('#testme').click(function() {

              // Encrypt with the public key...
              var encrypt = new JSEncrypt();
              encrypt.setPublicKey($('#pubkey').val());
              var encrypted = encrypt.encrypt($('#input').val());

              // Decrypt with the private key...
              var decrypt = new JSEncrypt();
              decrypt.setPrivateKey($('#privkey').val());
              var uncrypted = decrypt.decrypt(encrypted);

              // Now a simple check to see if the round-trip worked.
              if (uncrypted == $('#input').val()) {
                alert('It works!!!');
              }
              else {
                alert('Something went wrong....');
              }
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
        <input type="submit" value="Submit">
    </form>
</body>
</html>
