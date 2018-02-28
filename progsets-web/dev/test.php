<?php
	require __DIR__.'/../../vendor/autoload.php';
	$options = ['ssl' => false ];
	$client = new MongoDB\Client(
    'mongodb://demo:7lNSlzVDXdWeDHVH@cluster0-shard-00-00-2aqoa.mongodb.net:27017,cluster0-shard-00-01-2aqoa.mongodb.net:27017,cluster0-shard-00-02-2aqoa.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin',$options);

	$db = $client->test;
	$collection = $db->mycollection;
	$cursor = $collection->find();
	foreach ($cursor as $document) {
	    echo $document["name"] . "\n";
	}
	//$collection = $db->selectCollection('test', 'phpmanual');

	// If an array literal is used, there is no way to access the generated _id
	//$collection->insert(array('x' => 1));
	// $db->tutorialspointinsert({"name" : "tutorialspoint"})
	// $collection = $db->selectCollection(‘posts’);
	// $doc = array(
	//     "name" => "MongoDB",
	//     "type" => "database",
	//     "count" => 1,
	//     "info" => (object)array( "x" => 203, "y" => 102),
	//     "versions" => array("0.9.7", "0.9.8", "0.9.9")
	// );
	// $collection->insert( $doc );
?>