	<?php
		require_once 'bdd_connection.php';


		// on vérifie si il n'existse pas de compte associé à l'email saisi
		$query= 'SELECT * from ParkIN.USER_ACCOUNT WHERE user_email =:email ';
		$verif=$pdo->prepare($query);
		$verif->bindParam(':email',$_POST['email']);
		$res= $verif->execute();
		$nb = $verif->rowCount();


		
		if ($nb == 0){ 
			$q=" INSERT into ParkIN.USER_ACCOUNT (user_lastname,user_firstname,user_email,user_password,user_phone,user_subscription_date)
			values (:lastname,:firstname,:email,:password,:phonenumber,CONVERT_TZ(current_timestamp,'+00:00','+02:00'))" ;
			$req=$pdo->prepare($q);
			$req->bindParam(':lastname',$_POST['lastname']);
			$req->bindParam(':firstname',$_POST['firstname']);
			$req->bindParam(':email',$_POST['email']);
			$req->bindParam(':password',$_POST['password']);
			$req->bindParam(':phonenumber',$_POST['phonenumber']);
			$resp=$req->execute();

			if($resp){
				$tmp["code"] = "0";
				$tmp["description"]= "insertion réussie";
				$output[]=$tmp;
				echo json_encode($output);
			} 
			else{
				$tmp["code"] = "1";
				$tmp["description_code"]= "erreur d'insertion";
				$output[]=$tmp;
				echo json_encode($output);
			}



		}
		else{
			$tmp["code"] = "2";
			$tmp["description_code"]= "Il existe un compte associé à l'email saisi";
			$output[]=$tmp;
			echo json_encode($output);
		}
    	
	?>



