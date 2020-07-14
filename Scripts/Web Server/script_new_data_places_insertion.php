	<?php
		require_once 'bdd_connection.php';
		
		function horodateur($lat1, $lng1, $lat2, $lng2,$horodateur)
		{
			$epsilon=0,000001;
			if(((abs($lat1)-abs($lat2))<$epsilon) and ((abs($lng1)-abs($lng2))<$epsilon)){
				return $horodateur;
			}
		}
		
		$user	   =$_POST['user_id'];
		$latitude  =$_POST['latitude'];
		$longitude =$_POST['longitude'];
		$jour	   =$_POST['validation_datetime'];
		$day	   =$_POST['day'];
		$heure	   =$_POST['hour'];
		$status	   =$_POST['status'];
		
		//récuperer d'abord tous les horodateurs pour pouvoir définir lequel
		$query= 'SELECT * FROM ParkIN.HORODATEURS_CAPACITY where horodateur_latitude=:latitude and horodateur_longitude=:longitude';
		$verif=$pdo->prepare($query);
		$verif->bindParam(':latitude',$latitude);
		$verif->bindParam(':longitude',$longitude);
		$resp= $verif->execute();
		$output=[];
		 while ($data = $req->fetch(PDO::FETCH_ASSOC)){
				$mid=horodateur($latitude,$longitude,$data['horodateur_latitude'],$data['horodateur_longitude'],$data['horodateur_id']);
		
		}
		if($mid != null ){
			$q=" INSERT into ParkIN.ParkIN.NEW_DATA_PLACES (user_id, latitude, longitude, horodateur_id,day, validation_datetime, hour, statut)
			values (:user,:latitude,:longitude,:horodateur,:day,:validation_time,:hour,:status)" ;
			$req=$pdo->prepare($q);
			$req->bindParam(':user',$user);
			$req->bindParam(':latitude',$latitude);
			$req->bindParam(':longitude',$longitude);
			$req->bindParam(':horodateur',$mid);
			$req->bindParam(':validation_datetime',$jour);
			$req->bindParam(':day',$day);
			$req->bindParam(':hour',$heure);
			$req->bindParam(':status',$status);
			$resp=$req->execute();

		}
		 
		/*
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
    	*/
	?>
