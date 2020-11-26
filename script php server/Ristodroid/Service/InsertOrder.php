<?php
header('Content-Type: application/json');

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ristodroid";

$inputJSON = file_get_contents('php://input');
$json= json_decode( $inputJSON, true);


try {
// Create connection
    $conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    insertOrder($conn, $json['id'], $json['time'], $json['table']['id'], $json['seat']['id'], $json['seatNumber'], $json['extraInfo']);
    insertOrderDetails($conn, $json['orderDetails']);


} catch (mysqli_sql_exception $e) { // Failed to connect? Lets see the exception details..
    echo "MySQLi Error Code: " . $e->getCode() . "<br />";
    echo "Exception Msg: " . $e->getMessage();
    exit(); // exit and close connection.
}

$conn->close(); // finally, close the connection


function insertOrder($conn, $id, $timestamp, $table, $seat, $seatnumber, $extrainfo) {
    //inserting data order
    $toinsert = "INSERT INTO orders
			(id, time, tables, seat, seatnumber, extra_info)
			VALUES
			('$id', '$timestamp','$table', $seat, $seatnumber, '$extrainfo');";

//declare in the order variable
    $result = mysqli_query($conn, $toinsert);	//order executes
    if($result){
        echo(json_encode(["message" => "Inserimento avvenuto correttamente"], JSON_PRETTY_PRINT));
    } else{
        echo(json_encode(["message" => "Inserimento non avvenuto correttamente"], JSON_PRETTY_PRINT));
    }
}

function insertOrderDetails($conn, $orderdetails) {
    $id_order_details="";
    $order="";
    $id_dish=0;
    $quantity=0;

    $stmt = $conn-> prepare("INSERT INTO orderdetails
			(id, orders, dish, quantity) VALUES (?, ?, ?, ?)");


    foreach($orderdetails as $row) {
        $stmt->bind_param("ssii", $id_order_details, $order, $id_dish, $quantity);
        $id_order_details = $row['id'];
        $order = $row['order'];
        $id_dish = $row['dish']['id'];
        $quantity = $row['quantity'];
        $stmt->execute();

        insertVariationDishOrder($conn, $row['variationPlusList'], $id_order_details);
    }
    $stmt->close();
}

function insertVariationDishOrder($conn, $variations, $orderdetail) {
    $variation=0;
    $var_stmt = $conn-> prepare("INSERT INTO variationdishorder
			(variation, orderdetails) VALUES (?, ?)");
    foreach($variations as $var) {
        $var_stmt->bind_param("is", $variation, $orderdetail);
        $variation = $var['id'];
        $var_stmt->execute();
    }
    $var_stmt->close();
}

?>