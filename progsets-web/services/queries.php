<?php
	class queryclass {
	    var $queryout;
	    var $querycolumns;

	   function __construct( $e, $t ) {
		   $this->queryout = $e;
		   $this->querycolumns = $t;
		}

	    function query1() {
	    	$type = "Visualization";
	    	$metadata = json_encode($this->querycolumns[0]);
	    	$status = "Active";

			// CREATE TABLE `wayreachprd`.`graph_table` (
			//  `id` INT NOT NULL AUTO_INCREMENT,
			//  `type` VARCHAR(45) NULL,
			//  `metadata` LONGTEXT NULL,
			//  `status` VARCHAR(45) NULL,
			//  PRIMARY KEY (`id`));
			// type,  metadata,  status
			// type is Visualization or dashboard 
			// metadata is json 
			// and status if for deleted ones 

	    	mysql_query("INSERT INTO procsets.graph_table(type,metadata,status) VALUES('$type','$metadata','$status')");
			$rowsiii = array();
			$rowsiii["data"] = $metadata;
			echo json_encode($rowsiii);
	    }

	    function query2() {
	    	// $selected_state = $this->querycolumns[0];
	    	$status = "Active";
	    	$type = "Visualization";
	    	$query = mysql_query("SELECT * FROM procsets.graph_table WHERE status ='$status' AND type='$type'");
			$rowsiii = array();
			while($rowttt = mysql_fetch_assoc($query)) {
		    	$rowsiii[] = $rowttt;
			}
			echo json_encode($rowsiii);
	    }

	    function query3() {
			$type = "Dashboard";
	    	$metadata = json_encode($this->querycolumns[0]);
	    	$status = "Active";

			// CREATE TABLE `procsets`.`graph_table` (
			//  `id` INT NOT NULL AUTO_INCREMENT,
			//  `type` VARCHAR(45) NULL,
			//  `metadata` LONGTEXT NULL,
			//  `status` VARCHAR(45) NULL,
			//  PRIMARY KEY (`id`));
			// type,  metadata,  status
			// type is Visualization or dashboard 
			// metadata is json 
			// and status if for deleted ones 

	    	mysql_query("INSERT INTO procsets.graph_table(type,metadata,status) VALUES('$type','$metadata','$status')");
			$rowsiii = array();
			$rowsiii["data"] = $metadata;
			echo json_encode($rowsiii);
	    }
	}

	include_once 'connection.php';
	$queryout = (int) $_POST['queryout'];
	$newquery = new queryclass( $queryout, json_decode(stripslashes($_POST['querycolumns'])));
	for ($x=1; $x<=12; $x++) { if($queryout == $x) { $call = ("query".$x); } } 
	$newquery->$call();
?>