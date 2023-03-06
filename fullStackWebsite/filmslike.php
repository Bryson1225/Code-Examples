<?php
/*
 * Author: Bryson Mineart
 * Class: CSC 337
 * File: filmslike.php
 */
//Access the database, create object, send query, send query feedback to the JS file.
require "DatabaseAdaptor.php";
$theDBA = new DatabaseAdaptor();
$search = $_GET["id"];
$array = $theDBA->getAllMoviesLike($search);
echo json_encode($array);
?>