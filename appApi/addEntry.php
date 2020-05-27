<?php

if(isset($_GET["name"]))
{
	$name = $_GET["name"];
	$score = $_GET["score"];
	$difficulty = $_GET["difficulty"];
}


if(isset($_POST["name"]))
{
	$name = $_POST["name"];
	$score = $_POST["score"];
	$difficulty = $_POST["difficulty"];
}



//$data = $_REQUEST;

$server = "localhost";
$user = "root";
$password = "";
$db= "leaderboard";
		$dbc=new mysqli($server,$user,$password,$db);
		if($dbc->connect_error){
			die ("Negaliu prisijungti prie MySQL:"	.$dbc->connect_error);}


$sql = "INSERT INTO results (name, score, diff ) VALUES ('$name', $score, $difficulty)";

//echo $sql;

$result =  $dbc->query($sql);

?>