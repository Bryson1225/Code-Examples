<?php
// File name: DatabaseAdaptor.php, which must be the same name as the class
// Programer: Rick Mercer and Bryson Mineart
class DatabaseAdaptor {  
    // The instance variable used in every method  
    private $DB;  
    // Connect to an existing data based named 'imdb_small'  
    public function __construct() {       
        $dataBase = 'mysql:dbname=imdb_small;charset=utf8;host=127.0.0.1';       
        $user = 'root';       
        $password = ''; // Use the empty string with as root's password       
        try {           
            $this->DB = new PDO($dataBase, $user, $password);           
            $this->DB->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);           
        } catch (PDOException $e) {           
            echo ('Error establishing Connection');           
            exit();            
        }
    }
    
    public function getId($first, $last) {
        $stmt = $this->DB->prepare("SELECT * FROM actors WHERE first_name='" . $first . "'");// AND last_name='%" . $last . "%'");
        $stmt->execute();
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }
    
    // Return a PHP array of all moves LIKE $part
    //public function getAllMoviesLike($part) {
    //    $stmt = $this->DB->prepare("SELECT * FROM movies WHERE name LIKE '%" . $part . "%'");
    //    $stmt->execute();
    //    return $stmt->fetchAll(PDO::FETCH_ASSOC);
//    }
    
} // End class DatabaseAdaptor
?>