/*
 * 13 JavaScript functions requiring console.assert, selection, repetition, strings and
 * arrays. The code contains a few asserts to show the syntax. You should add many more
 * asserts to test your solutions. It is important to be able to write your own tests.
 *
 * Programmer: Bryson Mineart
 */
 
 
 ////////////////////////////////////////////////////////////////////
 // romanNumeral
 //
 // Complete the free function romanNumeral that returns the numeric
 // equivalent of an upper- or lower-case Roman numeral, which is a char.
 // Roman numerals and their decimal equivalents are as follows:
 
 // 'I' (or 'i') = 1
 // 'V' (or 'v') = 5
 // 'X' (or 'x') = 10
 // 'L' (or 'l') = 50
 // 'C' (or 'c') = 100
 // 'D' (or 'd') = 500
 // 'M' (or 'm') = 1,000
 //
 // If the input is not a valid Roman numeral, return -1
 //
 function romanNumeral(sequence) {
	if (sequence == 'I' | sequence == 'i') {
		return 1
	} else if (sequence == 'V' | sequence == 'v') {
		return 5
	}else if (sequence == 'X' | sequence == 'x') {
		return 10
	}else if (sequence == 'L' | sequence == 'l') {
		return 50
	}else if (sequence == 'C' | sequence == 'c') {
		return 100
	}else if (sequence == 'D' | sequence == 'd') {
		return 500
	}else if (sequence == 'M' | sequence == 'm') {
		return 1000
	} else {
		return 1
	}
}
 
 console.assert(romanNumeral('l') === 50);
 console.assert(romanNumeral('L') === 50);
 console.assert(romanNumeral('V') === 5);
 console.assert(romanNumeral('I') === 1);
 console.assert(romanNumeral('i') === 1);
 console.assert(romanNumeral('x') === 10);
 console.assert(romanNumeral('m') === 1000);
 console.assert(romanNumeral('D') === 500);
 console.assert(romanNumeral('t') === 1);
 console.assert(romanNumeral('0') === 1);
 // Write more asserts
 console.log("romanNumeral");
 
 
 
///////////////////////////////////////////////////////////////////
// sumOfFirstInts
//
// Return the sum of the first n integers.
//
// sumOfFirstInts(2) returns 3, which is 1 + 2
// sumOfFirstInts(3) returns 6, which is 1 + 2 + 3
// sumOfFirstInts(5) returns 15, which is 1 + 2 + 3 + 4 + 5
//
// Precondition: n >= 0 (no negatives, integers only)
//
function sumOfFirstInts(n) {
	var sum = 0;
	for (var i = 1; i<=n;i++) {
		sum += i
	}
	return sum;	
}

console.assert(sumOfFirstInts(1) === 1);
console.assert(sumOfFirstInts(3) === 1 + 2 + 3);
console.assert(sumOfFirstInts(2) === 1 + 2);
console.assert(sumOfFirstInts(4) === 1 + 2 + 3 + 4);
console.assert(sumOfFirstInts(5) === 1 + 2 + 3 + 4 + 5);
console.assert(sumOfFirstInts(6) === 1 + 2 + 3 + 4 + 5 + 6);
console.assert(sumOfFirstInts(7) === 1 + 2 + 3 + 4 + 5 + 6 + 7);
console.assert(sumOfFirstInts(8) === 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8);
// Write more asserts
console.log("sumOfFirstInts");
 


///////////////////////////////////////////////////////////////////
// factorial
//
// Given an integer n, return n!
// factorial(0) returns 1
// factorial(1) returns 1
// factorial(2) returns 2 * 1
// factorial(3) returns 3 * 2 * 1
// factorial(4) returns 4 * 3 * 2 * 1
//
// Precondition: n >= 0 and is an integer
//
function factorial(n) {
	var product = 1
	if (n == 0 | n == 1) {
		return 1
	} else {
		for (var i = 2; i<=n; i++) {
			product*=i
		}
		return product;	
	}
}

console.assert(factorial(3) === 3*2*1);
console.assert(factorial(0) === 1);
console.assert(factorial(1) === 1);
console.assert(factorial(2) === 2*1);
console.assert(factorial(4) === 4*3*2*1);
console.assert(factorial(5) === 5*4*3*2*1);
console.assert(factorial(6) === 6*5*4*3*2*1);
console.assert(factorial(7) === 7*6*5*4*3*2*1);
console.assert(factorial(8) === 8*7*6*5*4*3*2*1);
console.assert(factorial(9) === 9*8*7*6*5*4*3*2*1);
 // Write more asserts
console.log("factorial");




/////////////////////////////////////////////////////////////////////
// howSwedish
//
// ABBA is a band, they have many songs including Dancing Queen, and
// Fernando. ABBA is actually a Swedish band, so if we wanted to find
// out howSwedish a String is, we could simply find out how many times
// the String contains the substring "abba". We want to look for this
// substring in a case insensitive manner. So "abba" counts, and so
// does "aBbA". We also want to check for overlapping abba's such as
// in the String "abbAbba" that contains "abba" twice.
//
// howSwedish("ABBA a b b a") returns 1
// howSwedish("abbabba!") returns 2
//
// Preconditon: The arguments are always valid JavaScript strings
//
function howSwedish(str) {
	str = str.toLowerCase()
	swedArray = str.split("")
	abba = ['a', 'b', 'b', 'a']
	var length = swedArray.length
	var i = 0
	var count = 0
	var index = 0
	var found = false
	
	for (var i = 0; ((i+3)<length); i++) {
		while (swedArray[i+index] == abba[index] && found == false) {
			index++
			if (index == 4) {
				count+=1
				index=0
				found = true
			}
		}
		found = false
		index = 0
	}
   return count;
}

console.assert( howSwedish("AbBa") === 1);
console.assert(howSwedish("abbabba") == 2);
console.assert(howSwedish("abbabba!") == 2);
console.assert(howSwedish("ABBA a b b a") == 1);
console.assert(howSwedish("ABBAabbabbabbaabba a b b a") == 5);
console.assert(howSwedish("AB!BAab!bab!babb!aab!ba abba") == 1);
console.assert(howSwedish("AaBba abb abba b b a") == 2);
console.assert(howSwedish("Aaba aabb aababbb b a") == 0);
// Add more asserts
console.log("howSwedish");



//////////////////////////////////////////////////////////////////////////////
// mirrorEnds
//
// Complete method mirrorEnds that given a string, looks for a mirror image
// (backwards) string at both the beginning and end of the given string. 
// In other words, zero or more characters at the very beginning of the given 
// string, and at the very end of the string in reverse order (possibly
//  overlapping). For example, "abXYZba" has the mirror end "ab". 
//
// mirrorEnds("") returns ""
// mirrorEnds("abcde") returns ""
// mirrorEnds("a") returns "a"
// mirrorEnds("abca") "a"
// mirrorEnds("abba") returns "abba"
// mirrorEnds("abbA")); returns "a" This is case sensitive 'a' != 'A'
// mirrorEnds("RACECAR")); returns "RACECAR"  Palindromes are returned as is
//
// Preconditon: The arguments are always valid JavaScript strings
//
function mirrorEnds(str) {
	if (str == "") {
		return "";
	}else if (str == '') {
		return "";
	}
	mirArray = str.split("");
	var n = mirArray.length;
	if (n == 1) {
		return str;
	} else {
		n-=1;
		var newStr = "";
		for (var i = 0; i<=n;i++) {
			if (mirArray[i] == mirArray[n]) {
				newStr+=mirArray[i];
				n--;
			} else {
				return newStr;
			}
		}
		return str;
	}
}

console.assert ( mirrorEnds ( 'abcdefcba' ) ===  'abc');
console.assert ( mirrorEnds ( 'radar' ) ===  'radar');
console.assert ( mirrorEnds ( 'raddar' ) ===  "raddar");
console.assert ( mirrorEnds ( '' ) ===  "");
console.assert ( mirrorEnds ( 'a' ) ===  'a');
console.assert ( mirrorEnds ( 'abba' ) ===  'abba');
console.assert ( mirrorEnds ( 'RACECAR' ) ===  'RACECAR');
console.assert ( mirrorEnds ( 'abbA' ) ===  '');
// Add more asserts
console.log("mirrorEnds");



////////////////////////////////////////////////////////////////////////
// isStringSorted
//
// Given a String, return true if the letters are in ascending order.
// Note: 'a' < 'b' and '5' < '8'
// isStringSorted("") returns true
// isStringSorted("a") returns true
// isStringSorted("abbcddef") returns true
// isStringSorted("123456") returns true
// isStringSorted("12321") returns false
//
// Preconditon: The arguments are always valid JavaScript strings
//
function isStringSorted(str) {
	if (str == "") {
		return true;
	} else {
		stringArray = str.split("");
		var n = stringArray.length;
		if (n==1) {
			return true;
		} else {
			var i = 1;
			var j = 0;
			while (i < n) {
				if (stringArray[i] < stringArray[j]) {
					return false;
				} else {
					i++;
					j++;
				}
			}
		}
	}
  return true;
}
console.assert (isStringSorted ( 'abcdefg' ) );
console.assert (! isStringSorted ( 'cba' ) );
console.assert (isStringSorted ( '' ) );
console.assert (isStringSorted ( 'a' ) );
console.assert (isStringSorted ( 'abbccddef' ) );
console.assert (isStringSorted ( '123456' ) );
console.assert (! isStringSorted ( '12321' ) );
console.assert (! isStringSorted ( 'aza' ) );
console.assert (isStringSorted ( 'cdef' ) );
console.assert (! isStringSorted ( 'hig' ) );
console.log("isStringSorted");



///////////////////////////////////////////////////////////////////////////
// maxBlock
//
// Given a string, return the length of the largest "block" in the string.
// A block is a run of adjacent chars that are the same.

// maxBlock("hoopla") returns 2
// maxBlock("abbCCCddBBBxx") returns 3
// maxBlock("") returns 0
//
// Preconditon: The arguments are always valid JavaScript strings
//
function maxBlock(str) {
	if (str == "") {
		return 0;
	}
	maxArray = str.split("");
	var n = maxArray.length;
	if (n == 1) {
		return 1;
	} else {
		var max = 0;
		var counter = 0;
		var char = "";
		for (var i = 0; i<n;i++) {
			if (i == 0) {
				counter++;
				char = maxArray[i];
			} else {
				if (char == maxArray[i]) {
					counter++;
				} else {
					if (max <= counter) {
						max = counter;
						char = maxArray[i];
						counter = 1;
					}
				}
			}
		}
	}
  return max;
}

console.assert(maxBlock("abbCCCddBBBxx") === 3)
console.assert(maxBlock("xyyyyyxyy") === 5)
console.assert(maxBlock("") === 0)
console.assert(maxBlock("xyxxyxxxyxxxxy") === 4)
console.assert(maxBlock("abcdefghijklm") === 1)
console.assert(maxBlock("x") === 1)
// Add more asserts
console.log("maxBlock");


///////////////////////////////////////////////////////////////////////
// isArraySorted
//
// Given an array , return true if the element are in ascending order.
// Note: 'abe' < 'ben' and 5 > 3
// Precondition arr has all the same type of elements
function isArraySorted(arr) {
	var n = arr.length
	if (n==0) {
		return true;
	} else if (n == 1) {
		return true;
	} else {
		for (var i = 0; i < n;i++) {
			if (i == 0) {
				var prev = arr[i];
			} else {
				if (prev > arr[i]) {
					return false;
				} else {
					prev = arr[i];
				}
			}
		}
	}
  return true;
}

array = [1, 2, 2, 99];
console.assert(isArraySorted(array));	
array = [99, 100, 101, 100];
console.assert(! isArraySorted(array));
array = [99,99 ,99 , 99];
console.assert(isArraySorted(array));
array = ['a', 'b', 'c', 'd'];
console.assert(isArraySorted(array));
array = [];
console.assert(isArraySorted(array));
array = [1];
console.assert(isArraySorted(array));
console.log("isArraySorted");
  
  
    
    
//////////////////////////////////////////////////////////////////////////    
// numberOfPairs
//
// Return the number of times a pair occurs in array. A pair is any two String
// values that are equal (case sensitive) in consecutive array elements. The 
// array may be empty or have only one element. In both of these cases, return 0.
//
// numberOfPairs( array('a', 'b', 'c') ) returns 0
// numberOfPairs( array('a', 'a', 'a') ) returns 2
// numberOfPairs( array ( ) ) returns 0
// numberOfPairs( array ('a') ) returns 0
//
// Precondition: arr has all the same type of elements
//
function numberOfPairs(arr) {
	var length = arr.length
	if (length == 0) {
		return 0;
	} else if (length == 1) {
		return 0;
	}
    var count = 0
    var curLetter
    var tempLetter
    for (var i = 0; i < length; i++) {
    	if (i === 0) {
    		curLetter = arr[i]
		} else {
			tempLetter = arr[i]
			if (curLetter === tempLetter) {
				count++;
				} else {
					curLetter = arr[i];
				}
			}
		}

 	 return count;
}

console.assert(numberOfPairs( ['a', 'a', 'b', 'b'] ) === 2);
console.assert(numberOfPairs( [] ) === 0);
console.assert(numberOfPairs( ['a'] ) === 0);
console.assert(numberOfPairs( ['a', 'a', 'a'] ) === 2);
console.assert(numberOfPairs( ['a', 'b', 'b', 'b'] ) === 2);
console.assert(numberOfPairs( ['b', 'b', 'b', 'b'] ) === 3);
console.assert(numberOfPairs( ['a', 'b', 'c', 'd'] ) === 0);
console.assert(numberOfPairs( ['a', 'a'] ) === 1);
// Write more asserts
console.log("numberOfPairs");
  



///////////////////////////////////////////////////////////////////////////////
// frequency
//
// Given an  array of integers in the range of 0..10 (like quiz scores), 
// return an array of 11 integers where the first value (at index 0) is the
// number of 0s in the array argument, the second value (at index 1) is the
// number lof ones in the array and the 11th value (at index 10) is the 
// number of tens in the array. 
//
// Precondition: arr has valid ints in the range of 0..10
function frequency(arr) {
	freqArray = [0,0,0,0,0,0,0,0,0,0,0];
	for (var i = 0; i < arr.length;i++) {
		freqArray[arr[i]]++;
	}
    return freqArray;
}

arr = [0, 9, 8, 7, 7, 5, 9, 9, 10];
result = frequency(arr);
console.assert(result[1] === 0);
console.assert(result[2] === 0);
console.assert(result[3] === 0);
console.assert(result[4] === 0);
console.assert(result[0] === 1);
console.assert(result[9] === 3);
console.assert(result[10] === 1);
console.assert(result[7] === 2);
console.assert(result[8] === 1);
arr = [0, 0, 0, 0, 0, 0, 0, 0, 0];
result = frequency(arr);
console.assert(result[1] === 0);
console.assert(result[2] === 0);
console.assert(result[3] === 0);
console.assert(result[4] === 0);
console.assert(result[0] === 9);
console.assert(result[9] === 0);
console.assert(result[10] === 0);
console.assert(result[7] === 0);
console.assert(result[8] === 0);
arr = [1, 2, 3, 4, 5, 6, 7, 8, 9];
result = frequency(arr);
console.assert(result[1] === 1);
console.assert(result[2] === 1);
console.assert(result[3] === 1);
console.assert(result[4] === 1);
console.assert(result[0] === 0);
console.assert(result[9] === 1);
console.assert(result[10] === 0);
console.assert(result[7] === 1);
console.assert(result[8] === 1);

// Write more test cases
console.log("frequency");



//////////////////////////////////////////////////////////////////////////////////////
// shiftNTimes
//
// Modify array so it is "left shifted" n times -- so shiftNTimes(array(6, 2, 5, 3), 1) 
// changes the array argument to (2, 5, 3, 6) and shiftNTimes(array(6, 2, 5, 3), 2) 
// changes the array argument to (5, 3, 6, 2). You must modify the array argument by 
// changing the parameter array inside method shiftNTimes. A change to the 
// parameter inside the method shiftNTimes changes the argument if the 
// argument is passed by reference, that means it is preceded by an ampersand &
//
// shiftNTimes( [1, 2, 3, 4, 5, 6, 7], 3 ) modifies array to ( 4, 5, 6, 7, 1, 2, 3 )
// shiftNTimes( array(1, 2, 3), 5) modifies array to (3, 1, 2)
// shiftNTimes( array(3), 5) modifies array to (3)
//
function shiftNTimes(array, numShifts) {
	var n = array.length;
	if (n == 0) {
		return array;
	} else if (n == 1) {
		return array;
	} else if (numShifts == 0){
		return array;
	} else {
		var newChar;
		var temp;
		var index = 0;
		var tempCounter = numShifts;
		var found = false;
		for (var i  = 0; i< n; i++) {
			if (i == 0) {
				newChar = array[i];
			}
			while (found == false) {
				if (tempCounter == 0) {
					temp = array[index];
					array[index] = newChar;
					newChar = temp;
					tempCounter = numShifts;
					found = true;
				} else if (index == 0) {
					index = (n-1);
					tempCounter-=1;
				} else {
					index-=1;
					tempCounter-=1;
				}
			}
		found=false;
		}
	}
	return array;
}

// This test case that is provided to help explain the expected behavior:
x = [1, 2, 3, 4, 5];
shiftNTimes(x, 2);
console.assert(x[0] === 3);
console.assert(x[1] === 4);
console.assert(x[2] === 5);
console.assert(x[3] === 1);
console.assert(x[4] === 2);
x = [1, 2, 3, 4, 5, 6, 7];
shiftNTimes(x, 3);
console.assert(x[0] === 4);
console.assert(x[1] === 5);
console.assert(x[2] === 6);
console.assert(x[3] === 7);
console.assert(x[4] === 1);
console.assert(x[5] === 2);
console.assert(x[6] === 3);
x = [1, 2, 3];
shiftNTimes(x, 5);
console.assert(x[0] === 3);
console.assert(x[1] === 1);
console.assert(x[2] === 2);
x = [3];
shiftNTimes(x, 5);
console.assert(x[0] === 3);
x = [1,2,3,4,5,6,7,8,9];
shiftNTimes(x, 9);
console.assert(x[0] === 1);
console.assert(x[1] === 2);
console.assert(x[2] === 3);
console.assert(x[3] === 4);
console.assert(x[4] === 5);
console.assert(x[5] === 6);
console.assert(x[6] === 7);
console.assert(x[7] === 8);
console.assert(x[8] === 9);
// Write more asserts
console.log("shiftNTimes");




//////////////////////////////////////////////////////////////////////////////
// Say that a "clump" in an array is a series of 2 or more adjacent elements of
// the same value. Return the number of clumps in the given array.
//
//  runOfCount( [1, 2, 2, 3, 4, 4 ] ) returns 2
//  runOfCount( ["a", "a", "b", "b", "b", "c", "c", "X", "X"] ) returns 4
//  runOfCount( [1, 1, 1, 1, 1 ] ) returns 1
//  runOfCount( [1, 2, 3] ) returns 0
//  runOfCount( [1, 2, 3] ) returns 0
//  runOfCount( [ 2 ] returns 0
//
// Precondition: The array elements are always of the same type just try integers and strings
//  
function runOfCount(arr) {
	var n = arr.length;
	if (n <= 1) {
		return 0;
	} else {
		var clumps = 0;
		var prevVal = arr[0];
		var curVal;
		var i = 1;
		while (i < n) {
			if (prevVal == arr[i]) {
				if (curVal == prevVal) {
					prevVal = arr[i];
					i+=1;
				} else {
					clumps +=1;
					prevVal = arr[i];
					curVal = prevVal;
					i+=1;
					
				}
			} else {
				curVal = " ";
				prevVal = arr[i];
				i+=1;
			}
		}
		return clumps;
	}
}

console.assert(runOfCount([1, 4, 4, 3, 2, 2, 1, 1, 1, 1]) === 3);
console.assert(runOfCount(["a", "a", "b", "b", "b", "c"]) === 2);
console.assert(runOfCount([1]) === 0);
console.assert(runOfCount(["a", "a", "a"]) === 1);
console.assert(runOfCount([1, 4]) === 0);
console.assert(runOfCount(["a", "a"]) === 1);
console.assert(runOfCount([1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]) === 1);
console.assert(runOfCount(["a", "a", "b", "a", "a"]) === 2);
// Add more asserts
console.log("runOfCount");




///////////////////////////////////////////////////////////////////////////////
// evensLeft(array)
//
// Modify array that contains the exact same numbers as the given array,
// but rearranged so that all the even numbers come before all the odd numbers.
//  Other than that, the numbers can be in any order.
//
// evensLeft([1, 0, 1, 0, 0, 1, 1]) changes the argument to [0, 0, 0, 1, 1, 1, 1]
// evensLeft([3, 3, 2]) changes the argument to [2, 3, 3]
// evensLeft([3, 5, 4, 2]) changes the argument to [2, 4, 3, 5] or [4, 2, 3, 5]
// or [2, 4, 5, 3] or [4, 2, 5, 3]
//
// Precondition: All array elements are integers, whole nmbers with no fractional part
//
function evensLeft(array) {
	var n = array.length;
	if (n <=1 ) {
		return array;
  	}
  	n-=1;
  	var i = 0;
  	var temp;
  	while (i < n) {
		if (array[i] % 2 != 0) {
			temp = array[n];
			array[n] = array[i];
			array[i] = temp;
			n-=1;
		} else {
			i+=1;
		}
	}
  	return array;
}

sequence = [1, 2, 3, 4, -5, -16];
evensLeft(sequence);
console.assert(sequence[0] % 2 === 0);
console.assert(sequence[1] % 2 === 0);
console.assert(sequence[2] % 2 === 0);
console.assert(sequence[3] % 2 != 0);
console.assert(sequence[4] % 2 != 0);
console.assert(sequence[5] % 2 != 0);
sequence = [1];
evensLeft(sequence);
console.assert(sequence[0] % 2 != 0);
sequence = [2, 2, 2, 2, 2, 2];
evensLeft(sequence);
console.assert(sequence[0] % 2 === 0);
console.assert(sequence[1] % 2 === 0);
console.assert(sequence[2] % 2 === 0);
console.assert(sequence[3] % 2 === 0);
console.assert(sequence[4] % 2 === 0);
console.assert(sequence[5] % 2 === 0);
sequence = [1,1,1,1,1,1];
evensLeft(sequence);
console.assert(sequence[0] % 2 != 0);
console.assert(sequence[1] % 2 != 0);
console.assert(sequence[2] % 2 != 0);
console.assert(sequence[3] % 2 != 0);
console.assert(sequence[4] % 2 != 0);
console.assert(sequence[5] % 2 != 0);

// Add more test cases
console.log("evensLeft");




