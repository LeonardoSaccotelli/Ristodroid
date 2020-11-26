<?php
header('Content-Type: application/json');

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ristodroid";

try {
// Create connection
    $conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    $lang_request = $_GET['lang'];

    getDbItems($dbname, $conn, $lang_request);

	} catch (mysqli_sql_exception $e) { // Failed to connect? Lets see the exception details..
		echo "MySQLi Error Code: " . $e->getCode() . "<br />";
		echo "Exception Msg: " . $e->getMessage();
		exit(); // exit and close connection.
}

$conn->close(); // finally, close the

function getJsonFromTable($conn, $table, $lang_request){
	$string_concat = "_translate";
	
	$sql = stringQueryBuilder($table,$lang_request);

    $result = mysqli_query($conn, $sql);
	
    $json_array = array();
    while ($row = mysqli_fetch_assoc($result)) {

        if ($table != "dish" && $table!= "category") {
            $json_array[] = $row;
            $json_array = mb_convert_encoding($json_array, 'UTF-8', 'UTF-8');
        } else {
            $row["photo"] = base64_encode($row["photo"]);
            $json_array[] = $row;
            $json_array = mb_convert_encoding($json_array, 'UTF-8', 'UTF-8');
            //echo '<div class="caption"><h3><img src="data:image/jpg;base64,'.$row["photo"].'"/>'. $row['name']. '</h3></div>';
        }
    }

    return array($table => $json_array);
}


function getDbItems($dbname, $conn, $lang_request) {
    $tables = mysqli_query($conn, "SHOW TABLES FROM " . $dbname);

    $json_final_array = array();

    while($fetch = mysqli_fetch_array($tables)) {
        $getTable = $fetch[0]!="orders" && $fetch[0] != "orderdetails" && $fetch[0] != "variationdishorder" &&
                    $fetch[0]!='language' && $fetch[0]!='allergenic_translate' && $fetch[0]!='category_translate' &&
                    $fetch[0]!='dish_translate' && $fetch[0]!='ingredient_translate' && $fetch[0]!='menu_translate' &&
                    $fetch[0]!='seat_translate' && $fetch[0]!='variation_translate';
        if($getTable){
            $json_final_array = array_merge($json_final_array,
                getJsonFromTable($conn, $fetch[0], $lang_request));
        }

    }
    $json_final_array = json_encode(["db" => $json_final_array], JSON_PRETTY_PRINT);
    echo $json_final_array;
}


function stringQueryBuilder($table, $lang_request){
    $string_concat = "_translate";

    switch($table){
        case 'dish':
            $table_translate = $table.$string_concat;
            $sql = "SELECT ".$table.".id,".$table_translate.".name, ".$table_translate.".description,".$table.".price, "
					.$table.".category,".$table.".photo "." FROM ".$table." INNER JOIN ".$table_translate ." ON "
				.$table.".id = ".$table_translate.".".$table
				." WHERE " . $table_translate.".language='".$lang_request."'";
            break;

        case 'allergenic':
        case 'ingredient':
        case 'menu':
            $table_translate = $table.$string_concat;
			$sql = "SELECT ".$table_translate.".".$table." AS id,".$table_translate.".name 
           		 FROM " .$table_translate 
				." WHERE " . $table_translate.".language='".$lang_request."'";
            break;

        case 'category':
            $table_translate = $table.$string_concat;
            $sql = "SELECT ".$table.".id,".$table_translate.".name, ".$table.".photo "
			." FROM ".$table." INNER JOIN ".$table_translate ." ON "
				.$table.".id = ".$table_translate.".".$table
				." WHERE " . $table_translate.".language='".$lang_request."'";
            break;

        case 'seat':
        case 'variation':
			$table_translate = $table.$string_concat;
            $sql = "SELECT ".$table.".id,".$table_translate.".name, ".$table.".price "
			." FROM ".$table." INNER JOIN ".$table_translate ." ON "
				.$table.".id = ".$table_translate.".".$table
				." WHERE " . $table_translate.".language='".$lang_request."'";
        break;
		
		//Il caso di default gestisce tutte le tabelle che non hanno traduzioni
        default:
            $sql = "SELECT * FROM ".$table;
            break;
    }

    return $sql;
}
