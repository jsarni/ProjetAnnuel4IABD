<?php
	require_once 'bdd_connection.php';
	//Déclaration des variables du post
	//execution de la requete
	$id=$_POST['id'];
	$nom=$_POST['lastname'];
	$prenom=$_POST['firstname'];
	$email=$_POST['email'];
	$mdp=$_POST['password'];
	$telephone=$_POST['phonenumber'];

	$q='UPDATE  ParkIN.USER_ACCOUNT set user_lastname=:nom, user_firstname=:prenom, user_email=:email,user_password=:mdp,user_phone=:telephone where user_id=:id';
	$req=$pdo->prepare($q);
	$req->bindParam(':nom',$nom);
	$req->bindParam(':prenom',$prenom);
	$req->bindParam(':email',$email);
	$req->bindParam(':mdp',$mdp);
	$req->bindParam(':telephone',$telephone);
	$req->bindParam(':id',$id);

	$resp=$req->execute();
	if ($resp){
		$tmp["code"]="0";
		$output[]=$tmp;
		echo json_encode($output);
	}
	else{
		$tmp["code"]="1";
		$output[]=$tmp;
		echo json_encode($output);
	}
?>