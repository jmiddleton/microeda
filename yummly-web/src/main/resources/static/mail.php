<?php
if(isset($_POST['email'])){
		$mailTo = "solido@solido.com";
		$subject = "mail from web";
		$body = "New message from web
<br><br>
FROM: ".$_POST['email']."<br>
NAME: ".$_POST['author']."<br>
COMMENTS: ".$_POST['message']."<br>";	
		$headers = "To: Solido <".$mailTo.">\r\n";
		$headers .= "From: ".$_POST['author']." <".$_POST['email'].">\r\n";
		$headers .= "Content-Type: text/html";
		//envio destinatario
		$mail_success =  mail($mailTo, utf8_decode($subject), utf8_decode($body), $headers);		
}
?>  