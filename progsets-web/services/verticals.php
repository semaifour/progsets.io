<?php
	$subdata = [
		[
            "totalbookings",
            549422914.7900001,
            1094076882.12,
            6417595.99,
            55211676.48000001,
            89601311.46
        ],
        [
            "be",
            "Meraki",
            "Data Center Switching",
            "Mobility",
            "Security",
            "Industrial IOT Networking"
        ],
        [
            "quarter",
            "Q4 FY2017",
            "Q1 FY2018",
            "Q2 FY2018",
            "Q2 FY2018",
            "Q4 FY2017"
        ]
    ];
	$bodydata = ["data"=>$subdata,"columns"=>["quarter","be","totalbookings"],"name"=>"bookings","type"=>"vertical","status"=>"success"];
	$data = ["success"=>true,"code"=>200,"timestamp"=>1512350737830,"message"=>"Requested operation completed successfully.",
   "timetaken"=>3230,"session"=> "node0usdcjw17km9j1a9m2fm8shapt0","body"=>[$bodydata]];
	echo json_encode($data);
?>