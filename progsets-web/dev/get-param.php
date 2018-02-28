<?php
    if(isset($_GET["proc"])) {
        $proc =  $_GET["proc"];
        if($proc=="new") {

        }
        elseif($proc=="edit") {
            if(isset($_GET["id"])) {
                echo $_GET["id"];
            }
        }
        elseif($proc=="list") {

        }
    }
?>