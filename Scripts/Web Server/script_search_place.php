<?php
	require_once 'bdd_connection.php';
		function distance($lat1, $lng1, $lat2, $lng2){
			$earth_radius = 6378137;   // Terre = sphère de 6378km de rayon
			$rlo1 = deg2rad($lng1);
			$rla1 = deg2rad($lat1);
			$rlo2 = deg2rad($lng2);
			$rla2 = deg2rad($lat2);
			$dlo = ($rlo2 - $rlo1) / 2;
			$dla = ($rla2 - $rla1) / 2;
			$a = (sin($dla) * sin($dla)) + cos($rla1) * cos($rla2) * (sin($dlo) * sin($dlo));
			$d = 2 * atan2(sqrt($a), sqrt(1 - $a));
			return ($earth_radius * $d);
			
		}
		function tranche_set($h){
		/*****************************************************************************************************/
			$tranches=array('00:00:00-00:30:00', '00:30:00-01:00:00', '01:00:00-01:30:00', '01:30:00-02:00:00','02:00:00-02:30:00','02:30:00-03:00:00', '03:00:00-03:30:00', '03:30:00-04:00:00', '04:00:00-04:30:00', '04:30:00-05:00:00','05:00:00-05:30:00', '05:30:00-06:00:00', '06:00:00-06:30:00', '06:30:00-07:00:00', '07:00:00-07:30:00','07:30:00-08:00:00', '08:00:00-08:30:00', '08:30:00-09:00:00', '09:00:00-09:30:00', '09:30:00-10:00:00','10:00:00-10:30:00', '10:30:00-11:00:00', '11:00:00-11:30:00', '11:30:00-12:00:00', '12:00:00-12:30:00','12:30:00-13:00:00', '13:00:00-13:30:00', '13:30:00-14:00:00', '14:00:00-14:30:00', '14:30:00-15:00:00','15:00:00-15:30:00', '15:30:00-16:00:00', '16:00:00-16:30:00', '16:30:00-17:00:00', '17:00:00-17:30:00','17:30:00-18:00:00', '18:00:00-18:30:00', '18:30:00-19:00:00', '19:00:00-19:30:00', '19:30:00-20:00:00','20:00:00-20:30:00', '20:30:00-21:00:00', '21:00:00-21:30:00', '21:30:00-22:00:00', 	'22:00:00-22:30:00','22:30:00-23:00:00', '23:00:00-23:30:00', '23:30:00-00:00:00');
		/****************************************************************************************************/
			$ar2=array_fill_keys($tranches,0);
		/************************************************************************************************/
		//Séparer le temps en heures minutes:
			$tmp=explode(":",$h);
		//mettre l'heure sous forme de tranche: 
		if(intval($tmp[1])>30){
			$tranche=$tmp[0].":30:00-". strval((intval($tmp[0])+1)).":00:00";
		}
		else{
			$tranche=$tmp[0].":00:00-". $tmp[0].":30:00";
		}
		//remplacer la valeur de la tranche concernée dans l'array:
		$ar2[$tranche]=1;
		return array_values($ar2);
		}

			//Déclaration des variables du post
			$rayon =$_POST['rayon'];
			$lat   =$_POST['latitude'];
			$long  =$_POST['longitude'];
			$heure =$_POST['heure'];
			$day   =DateTime::createFromFormat('d-mY',$_POST['date']);
			$day_dmy=$day->format('d-m-Y');
			//Récupération du jour de semaine au format entier correspondant au format pandas -python
            $daytmp=$day->format('N');
			$day_of_week=$daytmp-1;
			//récupération du mois:
			$mois=$day->format('n');
			
			//execution de la requete
			$q	="SELECT * FROM ParkIN.HORODATEURS_CAPACITY ";
			$req=$pdo->prepare($q);
			$resp=$req->execute();
			$output=[];
			$res=[];
			$res['day_of_week']=$day_of_week;
			$res['tranche']=tranche_set($heure);
			$res['month']=$mois;
			
			while ($data = $req->fetch(PDO::FETCH_ASSOC)){
				$mid=distance($lat,$long,$data['horodateur_latitude'],$data['horodateur_longitude']);
			if ($mid<= $rayon){
					
					array_push($output,[number_format(floatval($data['horodateur_latitude']),8),number_format(floatval($data['horodateur_longitude']),8),intval($res['month']),intval($res['day_of_week'])]+$res['tranche']));
					//array_push($output,$data);
					 		
					
			}
		}
		
			$send_to_p1=json_encode($output);
            $response_from_p1=shell_exec("python /var/www/html/python.py ".escapeshellarg($send_to_p1));
            
            echo $response_from_p1;

					
					
?>