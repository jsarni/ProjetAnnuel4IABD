<?php
	require_once 'bdd_connection.php';
		
	$user	   			=$_POST['user_id'];
	$latitude  			=$_POST['latitude'];
	$longitude 			=$_POST['longitude'];
	$validation_day	   	=$_POST['validation_datetime'];
	$search_day	   	   	=$_POST['day'];
	$heure	   			=$_POST['hour'];
	$status	   			=$_POST['status'];
		//récuperer d'abord tous les horodateurs pour pouvoir définir lequel
		$query='SELECT NVL(horodateur_id,"") as horodateur_id FROM ParkIN.HORODATEURS_CAPACITY where horodateur_latitude=:lati and horodateur_longitude=:longi';
		$query->bindParam(':lati',$latitude);
		$query->bindParam(':longi',$longitude);
		$verif=$pdo->prepare($query);
		$req  = $verif->execute();
		 while ($data = $verif->fetch(PDO::FETCH_ASSOC)){
				//$mid=horodateur_from_localization($latitude,$longitude,$data['horodateur_latitude'],$data['horodateur_longitude'],$data['horodateur_id']);
				//if(!is_null($mid)){
			$q='INSERT into ParkIN.NEW_DATA_PLACES (user_id, latitude, longitude, horodateur_id,jour, jour_validation, heure, statut)
													 values(:user,  :latitude,:longitude,:horodateur,  :day ,:validation_datetime, :hour, :status)' ;
			$req =$pdo->prepare($q);
			$req->bindParam(':user',$user);
			$req->bindParam(':latitude',$latitude);
			$req->bindParam(':longitude',$longitude);
			$req->bindParam(':horodateur',$data['horodateur_id']);
			$req->bindParam(':validation_datetime',$validation_day);
			$req->bindParam(':day',$search_day);
			$req->bindParam(':hour',$heure);
			$req->bindParam(':status',$status);
			$reponse=$req->execute();
			if($reponse){
					echo 1;
			}
			else{
					echo 0; 
			}
					
				}
		
		
		
?>