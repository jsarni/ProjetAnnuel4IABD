	<?php
		require_once 'bdd_connection.php';

		
		$q="SELECT * FROM USER_ACCOUNT where user_email = :email and user_password = :password" ;    
		$req=$pdo->prepare($q);
		$req->bindParam(':email',$_POST['email']);
		$req->bindParam(':password',$_POST['password']);
		$resp=$req->execute();
		if($req->rowCount()){
			while ($data = $req->fetch()){
					$output[]= $data;
				
			}
			
			print(json_encode($output));
		}
		
    	
	?>
