/**
Author: Bryson Mineart
Class: CSC 337
filmsike.js
 */
let div2Change = document.getElementById("2Change");
let input = document.getElementById("info");


//Send AJAX request to php file with given input. Receive JSON information
//back and then parse the information into a table for the user to view.
function getInformation() {
	let text = input.value;
	let ajax = new XMLHttpRequest();
	ajax.open("GET", "filmslike.php?id=" + text, true);
	ajax.send();
	ajax.onreadystatechange = function() {
		if (ajax.readyState == 4 && ajax.status == 200) {
			let arr = JSON.parse(ajax.responseText);
			let str = "<table class='table'><tr><th>name</th><th>year</th><th>rank</th></tr>";
			var i = 0;
			while (i < arr.length) {
				str+= "<tr>";
				str+= "<td>" + arr[i]['name'] +"</td>";
				str+= "<td>" + arr[i]['year'] +"</td>";
				str+= "<td>" + arr[i]['rank'] +"</td>";
				str+="<tr>"
				i++;
			}
			str+="</table>";
			div2Change.innerHTML = str;
		}
	}
}
