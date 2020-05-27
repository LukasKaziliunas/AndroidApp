<?php 

$filter = "";
$limit = 6;

if(isset($_GET["page"]))
{
	$page = $_GET["page"];
	if($page > 0)
	$filter = " LIMIT $limit OFFSET " . $limit * ($page - 1);
	else
	$pageError = "Invalid page number.";
}

$server = "localhost";
$user = "root";
$password = "";
$db= "leaderboard";
		$dbc=new mysqli($server,$user,$password,$db);
		if($dbc->connect_error){
			die ("Negaliu prisijungti prie MySQL:"	.$dbc->connect_error);}
$sql = ("SET CHARACTER SET utf8"); $dbc->query($sql);      // del lietuviškų raidžių

$sql = "SELECT * FROM results ORDER BY score DESC" . $filter;

if(!isset($pageError))
{
	if($result =  $dbc->query($sql))
	{
	$outp = $result->fetch_all(MYSQLI_ASSOC);
	echo json_encode($outp);
	}
	else
	{
	echo $dbc -> error;
	}
}
else
{
	echo $pageError;
}
?>