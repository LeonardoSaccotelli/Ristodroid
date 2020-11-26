<?php
header('Content-Type: application/json');

	$servername = "localhost";
	$username = "root";
	$password = "";
	$dbname = "ristodroid";
	
	
	$id = $_POST['ID_REVIEW'];
	$text = $_POST['TEXT_REVIEW'];
	$stringScore = $_POST['SCORE_REVIEW'];
	$date = $_POST['DATE_REVIEW'];
	$stringDish = $_POST['DISH_REVIEW'];
	
	$score = (int)$stringScore;
	$dish = (int)$stringDish;
	
	if($score!=0 && $dish!=0 && $id!="" && $text!="" && $date!=""){
	
		try {
			// Create connection
			$conn = new mysqli($servername, $username, $password, $dbname);

			// Check connection
			if ($conn->connect_error) {
				die("Connection failed: " . $conn->connect_error);
			}
			
		}catch (mysqli_sql_exception $e) { // Failed to connect? Lets see the exception details..
			echo "MySQLi Error Code: " . $e->getCode() . "<br />";
			echo "Exception Msg: " . $e->getMessage();
			exit(); // exit and close connection.
		}
		
		$date_for_database = date ("'Y-m-d H:i:s'", strtotime($date));		
		
		$sql = "INSERT INTO review(id,score,text_review,reviewDate,dish)VALUES ('$id',$score,'$text',$date_for_database,$dish)";
		$result = mysqli_query($conn,$sql);
		
		if($result){
			echo 'ENTER SUCCESS';
		}else{
			echo 'ENTER FAIL';
		}
		
		mysqli_close($conn);
	}else{
		echo 'ERROR TO SEND DATA';
	}