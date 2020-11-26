<?php
header('Content-Type: application/json');

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ristodroid";

try {
	//Create connection
    $conn = new mysqli($servername, $username, $password, $dbname);

	//Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

	$body_request_id_order = $_GET['id_order'];
    $lang_request = $_GET['lang'];
	

 $sql = "SELECT seat.id, seat_translate.name,seat.price,orders.seatnumber FROM seat INNER JOIN seat_translate ON seat.id = seat_translate.seat
        INNER JOIN orders ON seat.id = orders.seat 
        WHERE seat_translate.language='$lang_request' AND orders.id = '$body_request_id_order'";
			

	$result = mysqli_query($conn, $sql);
	
	$result_array = array();
    while ($row = mysqli_fetch_assoc($result)) {
		$result_array[] = $row;
	}
	
	echo (json_encode(["order_information" => $result_array], JSON_PRETTY_PRINT));

}catch (mysqli_sql_exception $e) { 
	// Failed to connect? Lets see the exception details..
    echo "MySQLi Error Code: " . $e->getCode() . "<br />";
    echo "Exception Msg: " . $e->getMessage();
    exit(); // exit and close connection.
}

$conn->close(); // finally, close the connection
