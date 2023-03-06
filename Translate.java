import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;



public class Translate {
	/*
	 * Patterns from grammar
	 */
	private static Pattern var_assign = Pattern.compile("^(@.+) : (.+)$");
	private static Pattern varVal = Pattern.compile("^(@\\w+) (@*.+)$");
	private static Pattern type = Pattern.compile("^int|bool|string$");
	private static Pattern bool = Pattern.compile("^(true|false)$");
	private static Pattern str = Pattern.compile("^(\\w+)$");
	private static Pattern var = Pattern.compile("^(@\\w+)$");
	private static Pattern intVal = Pattern.compile("^(\\d+)$");
	private static Pattern arith_ops = Pattern.compile("^\\s*(\\d+|@+\\w+)\\s*([-+?*$])\\s*(\\d+|@+\\w+)\\s*$");
	private static Pattern bool_ops = Pattern.compile("^\\s*(@+\\w+|true|false)\\s*(and|or)\\s*(@+\\w+|true|false)\\s*$");
	private static Pattern comp_ops = Pattern.compile("^\\s*(\\d+|@+\\w+)\\s*([<>=])(=)*\\s*(\\d+|@+\\w+)\\s*$");
	private static Pattern if_state = Pattern.compile("^(if)\\s*[(]\\s*(.+)\\s*[)]:\\s*$");
	private static Pattern loop_w = Pattern.compile("^(onlyIf)\\s*[(]\\s*(.+)\\s*[)]:\\s*$");
	private static Pattern out = Pattern.compile("^(out)[(](.+)[)]\\s*$");
	private static Pattern function = Pattern.compile("^(Function)\\s*(\\w+)\\s*[(]$");
	
	private static FileWriter writer; //Writing to a new file
	private static FileReader reader; //Reading in file
	private static int tabNumber = 1; //Using to get the tabs
	private static String stateType = "";//Used in error checking
	private static boolean inFunc = false; //Can only perform operations while in a function
	private static int funCount = 1;
	
	private static HashMap<String, String> varHolder = new HashMap<String, String>(); //Variable storage
	private static HashMap<Integer, String> funHolder = new HashMap<Integer, String>(); //Function names
	
	public static void main(String[] args) {
		try {
			writer = new FileWriter("new.java", false);
			initilizeJavaFIle();
			File newFile = new File("TESTING.txt");
            reader = new FileReader(newFile);
            BufferedReader BReader = new BufferedReader(reader);
            String line;
            while ((line = BReader.readLine()) != null) {
            	if (line.equals("\n") || line.equals("")) {
            		writer.write("\n"); //Going through empty strings and new lines that might exist
            	} else {
            		parseCmd(line, BReader);
            	}
            }
            writeMain();
            writer.close();
            BReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void writeMain() throws IOException {
		writer.write(tabGet() + "public static void main(String[] args) {\n");
		tabNumber+=1;
		int index = 1;
		while (funHolder.containsKey(index)) {
			String name = funHolder.get(index);
			index+=1;
			writer.write(tabGet() + name + "();\n");
			writer.flush();
		}
		tabNumber-=1;
		writer.write("}\n");
		writer.flush();
		tabNumber-=1;
		writer.write("}");
		writer.flush();
	}
	
	/*
	 * Initializing the new java file
	 */
	private static void initilizeJavaFIle() throws IOException {
		writer.write("public class Something {\n");
		writer.flush();
	}
	
	/*
	 * Parsing any of the command statements that may be present. We check for if statements,
	 * loops, printing, functions, and variable assigning.
	 * 
	 * This function receives a parameter to read the lines of the given file, and a command
	 * for the current line that we are on. Returns a boolean if a math is found or not found.
	 * 
	 * We also do a lot of error checking here. Cannot put anything in a file unless it is
	 * currently in a function. Even catches if there is something wrong and stops the parsing
	 * at that line for the user to see what is wrong.
	 */
	private static boolean parseCmd(String cmd, BufferedReader BReader) throws IOException {
		boolean match = false;
		Matcher m = if_state.matcher(cmd);
		Matcher i = loop_w.matcher(cmd);
		Matcher o = out.matcher(cmd);
		Matcher f = function.matcher(cmd);
		Matcher v = var_assign.matcher(cmd);
		if(m.find()) {
			if (!inFunc) { error("NOT IN FUNCTION."); }
			match = true;
			match = match && ifStatementCond(bool_ops.matcher(m.group(2)), comp_ops.matcher(m.group(2)), m, BReader);
		} else if (i.find()) {
			if (!inFunc) { error("NOT IN FUNCTION."); }
			match = true;
			match = match && loop(bool_ops.matcher(i.group(2)), comp_ops.matcher(i.group(2)), i, BReader);
		} else if (o.find()) {
			if (!inFunc) { error("NOT IN FUNCTION."); }
			out(cmd);
		} else if (f.find()) {
			if (inFunc) { error("CANNOT PLACE A FUNCTION WITHIN A FUNCTION."); }
			match = true;
			match = match && function(str.matcher(f.group(2)), BReader);
		} else if (v.find()){
			if (!inFunc) { error("NOT IN FUNCTION."); }
			match = true;
			match = match && varAssign(var_assign.matcher(cmd));
		} else if (cmd.equals("\n") || cmd.equals("")){
			return true;
		} else {
			error("YOU HAVE A SYNTAX ERROR."); //Only operations allowed standalone, if no match here then error.
		}
		return match;
	}
	/*
	 * This function is used for if the user creates a function. Our functions
	 * DO not allow for parameter passing which means each function will hold it's
	 * own variables.
	 * 
	 * This function checks that the function has a valid name, and then goes to
	 * check the actual contents that the function will hold. Seeing as we are translating to java.
	 * 
	 */
	private static boolean function(Matcher name, BufferedReader BReader) throws IOException {
		String line = "";
		boolean match = false;
		if(name.find()) {
			String funName = name.group(1);
			addFunction(funName);
			writer.write(tabGet() + "private static void " + funName + "(){\n");
			line = BReader.readLine();
			tabNumber +=1;
			match = true;
			inFunc = true;
			while (match) {
				stateType = "Function"; //Based off of what operation we are in, some other ops may not be allowed or the format may differ
				if (function.matcher(line).find()) { //No function in function
					error("CANNOT PUT A FUNCTION HERE.");
				}
				match = match && expressionCheckOut(line);
				if(!match) {
					match = true && parseCmd(line, BReader);
				}
				line = BReader.readLine();
				if (line.equals(") endFunc\n") || line.equals(") endFunc")) { //End of function
					match = true;
					tabNumber-=1;
					writer.write(tabGet() + "}\n");
					writer.flush();
					break;
				}
			}
		}
		inFunc = false;
		clearVariables();
		return match;
	}
	private static void addFunction(String name) throws IOException {
		if (funHolder.containsValue(name)) {
			error("FUNCTION ALREADY EXISTS");
		} else {
			funHolder.put(funCount, name);
			funCount+=1;
		}
	}
	
	/*
	 * Our functions do not receive or send any parameters. But by clearing
	 * the known variables with each new function we can create some slight
	 * scoping in terms of variable reach.
	 */
	private static void clearVariables() {
		varHolder = new HashMap<String, String>();
	}
	
	/*
	 * This function checks for the validity of the user's
	 * expressions. The user may define a function and want to have specific
	 * operations inside the function or maybe they want to define a variable, etc.
	 * 
	 * This function which check the validity of those expressions if a match is found.
	 * 
	 * Example(s): 5 < 5, true and true, @newVar 5 : int, etc.
	 */
	private static boolean expressionCheckOut(String cmd) throws IOException {
		boolean match = false;
		Matcher c = comp_ops.matcher(cmd);
		Matcher b = bool_ops.matcher(cmd);
		Matcher a = arith_ops.matcher(cmd);
		Matcher vA = var_assign.matcher(cmd);
		Matcher o = out.matcher(cmd);
		if (a.find()) {
			match=true;
			match = match && arithmetic(intVal.matcher(a.group(1)), var.matcher(a.group(1)), var.matcher(a.group(3)), intVal.matcher(a.group(3)), a.group(2));
			if (!match && a.group(2).equals("+")) {
				match = match && stringConcat(a.group(1), a.group(3));  
			}
		} else if (b.find()) {
			match=true;
			match = match && bools(bool.matcher(b.group(1)), bool.matcher(b.group(3)), var.matcher(b.group(1)), var.matcher(b.group(3)), b.group(2));
		} else if (c.find()) {
			match = true;
			match = match && compare(intVal.matcher(c.group(1)), var.matcher(c.group(1)), intVal.matcher(c.group(4)), var.matcher(c.group(4)), c);
		} else if (vA.find()) {
			match = true;
			match = match && varAssign(var_assign.matcher(cmd));
		} else if (o.find()) {
			match = true;
			match = match && out(cmd);
		} if (!match) {
			String[] temp = cmd.split(" ");
			match = stringConcat(temp[0], temp[2]);  
		}
		return match;
	}
	
	/*
	 * This function takes in two variable Matchers to test the input for if they are variables
	 * and then to tell if they exist in the system.
	 * 
	 * This functions primary purpose is to examine a given input for if the programmer is trying
	 * to perform string concatenation.
	 */
	private static boolean stringConcat(String val1, String val2) throws IOException {
		Matcher var1 = var.matcher(val1);
		Matcher var2 = var.matcher(val2);
		boolean isVar1 =  var1.find();
		boolean isVar2 = var2.find();
		String value1 = ""; String value2 = "";
		boolean match = false;
		if (isVar1 && isVar2) {
			match = true;
			String newVar1 = var1.group(1);
			String newVar2 = var2.group(1);
			match = match && doesVarExist(newVar1.replaceAll("@", "")) && doesVarExist(newVar2.replaceAll("@", ""));
			if (match) {
				concatWriter(newVar1.replaceAll("@", ""), newVar2.replaceAll("@", ""));
			}
		}else if (isVar1 && !isVar2) {
			match = true;
			String newVar1 = var1.group(1).replaceAll("@", "");
			match = match && doesVarExist(newVar1);
			if (val2.startsWith("\"") && val2.endsWith("\"")); {
				value1 = varHolder.get(newVar1);
				if (value1.startsWith("\"") && value1.endsWith("\"")); {
					concatWriter(newVar1, val2);
				}
			}
		} else if (!isVar1 && isVar2) {
			match = true;
			String newVar2 = var2.group(1).replaceAll("@", "");
			match = match && doesVarExist(newVar2);
			if (value1.startsWith("\"") && value1.endsWith("\"")); {
				value2 = varHolder.get(newVar2);
				if (value1.startsWith("\"") && value1.endsWith("\"")); {
					concatWriter(newVar2, val1);
				}
			}
		} else {
			if (val1.startsWith("\"") && val1.endsWith("\"")); {
				if (val2.startsWith("\"") && val2.endsWith("\"")); {
					error("CANNOT CONCAT TWO UNASSIGNED STRINGS.");
				}
			}
		}
		return match;
	}
	
	private static void concatWriter(String first, String second) throws IOException {
		writer.write(tabGet() + first+ " = " + first + " + " + second+";\n");
	}
	
	/*
	 * Function is used to match a new variable assignment.
	 * If the line matches the pattern for a variable assignment.
	 * Then we split it up into groups and check for the validity of each
	 * component.
	 * 
	 * Example: @variable 5 : int
	 */
	private static boolean varAssign(Matcher cmd) throws IOException {
		String group1 = "";
		String group2 = "";
		String typeV = "";
		boolean match = false;
		if(cmd.find()) {
			group1 = cmd.group(1);
			group2 = cmd.group(2);
			group2 = group2.replaceAll(" ", "");
			match = true;
			match = match && sepVarVal(group1, group2);
			match = match && type(group2);
		}
		if(match) {
			Matcher nameValue = varVal.matcher(group1);
			nameValue.find();
			String name = nameValue.group(1).replaceAll("@", "");
			if (doesVarExist(name)) {
				typeV = "";
			} else {
				typeV = group2;
			}
			match = match && assignVariable(group1, group2); //Storing variable(s) and value(s)
		}
		if (match) {
			Matcher varAndVal = varVal.matcher(group1);
			varAndVal.find();
			varWrite(varAndVal.group(1), varAndVal.group(2), typeV); //Writing to new java file
		}
		return match;
	}
	
	/*
	 * This function separates the given variable name and value.
	 * The function receives the type entered by the user. After
	 * After checking that the varVal pattern matches the given input.
	 * The function Then checks that we have a valid variable name, and that
	 * the assigned value matches the given type.
	 */
	private static boolean sepVarVal(String nameVal, String type) {
		boolean match = false;
		Matcher m = varVal.matcher(nameVal);
		if(m.find()) {
			match = true;
			match = match && var(m.group(1));
			match = match && val(m.group(2), type);
		} else {
			match = var(nameVal);
		}
		return match;
	}
	
	/*
	 * Checking that the given variable name is valid.
	 * Must start with an "@"
	 */
	private static boolean var(String cmd) {
		Matcher m = var.matcher(cmd);
		boolean match = m.find();
		return match;
	}
	
	/*
	 * This function checks that the assigned value is valid.
	 * It compares this value against the assigned type to make
	 * sure that they match up.
	 */
	private static boolean val(String cmd, String type) {
		Matcher m = intVal.matcher(cmd);
		boolean match = m.find();
		if(match) {
			match = match && (type.equals("int"));
		}
		else {
			m = bool.matcher(cmd);
			match = m.find();
			if(match) {
				match = match && (type.equals("bool"));
			}
			else {
				m = var.matcher(cmd);
				match = m.find();
				if(match) {
				} else {
					if (cmd.startsWith("\"") && cmd.endsWith("\"")); {
						match = true && (type.equals("string"));
					}
				}
			}
		}
		return match;
	}
	
	/*
	 * If the user creates a variable, we want to keep track of it
	 * for possible later use. We use a HashMap to store the variables
	 * their values. Returns a boolean, but it doesn't necessarily need to.
	 * At this point, when this function is called we have already error checked
	 * the input, however; it is better safe than sorry.
	 */
	private static boolean assignVariable(String nameVal, String type) throws IOException {
		boolean match = false;
		Matcher nameValue = varVal.matcher(nameVal);
		nameValue.find();
		String name = nameValue.group(1).replaceAll("@", "");
		String value = nameValue.group(2);
		Matcher v = var.matcher(value);
		if(v.find()) {
			value = value.replaceAll("@", "");
			if(doesVarExist(value)) {
				match = val(varHolder.get(value), type);
				if (match) {
					varHolder.put(name, varHolder.get(value));
				}
			} else {
				error("THIS VARIABLE DOES NOT YET EXIST.");
			}
		} else {
			match = true;
			varHolder.put(name, value);
		}
		return match;
	}
	
	/*
	 * Checking if the variable given has been assigned a value
	 * Returns true or false.
	 */
	private static boolean doesVarExist(String var) {
		var = var.replaceAll("@", "");
		if(varHolder.keySet().contains(var)) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Returns a type boolean if valid bool
	 * Takes in a string to test whether the given variable is assigned
	 * to an boolean.
	 */
	private static boolean isTypeBool(String var) {
		var = var.replaceAll(" ", "");
		var = var.replaceAll("@", "");
		if(varHolder.keySet().contains(var)) {
			if(varHolder.get(var).equals("true") || varHolder.get(var).equals("false")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/*
	 * Returns a type boolean if valid integer
	 * Takes in a string to test whether the given variable is assigned
	 * to an integer.
	 */
	private static boolean isTypeInt(String var) {
		var = var.replaceAll("@", "");
		if(varHolder.keySet().contains(var)) {
			if(intVal.matcher(varHolder.get(var)).find()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	/*
	 * Checking that the given type is allowed. We only allow for
	 * ints, bools, and strings.
	 */
	private static boolean type(String cmd) {
		Matcher m = type.matcher(cmd);
		boolean match = m.find();
		return match;
	}
	
	/*
	 * THis function is used to write into the java file a new variable declaration.
	 */
	private static void varWrite(String name, String val, String type) throws IOException {
		name = name.replaceFirst("@", ""); //Clearing our grammars formatting to fit java
		val = val.replaceAll("@", "");
		
		writer.write(tabGet()); //Tabs or indentions
		if (type.equals("string")) {
			writer.write("String " + name + " = " + val + ";");
		}else if (type.equals("int")) {
			writer.write("int " + name + " = " + val+";");
		} else if (type.equals("bool")) {
			writer.write("boolean " + name + " = " + val+";");
		} else if (type.equals("")) {
			writer.write(name + " = " + val+";");
		}
		writer.write("\n");
		writer.flush();
	}
	
	/*
	 * Returns a string that keeps the text aligned in the translated
	 * java document.
	 */
	private static String tabGet() {
		return "\t".repeat(tabNumber);
	}
	
	/*
	 * This is for simple arithmetic operations. We first check if we are outputting. Arithmetic operations will result
	 * in several different results depending on how they are used.
	 * 
	 * You can do out(5+5) but not do just 5+5
	 * You can output a variable being added to another: out(@var + @otherVar)
	 * You can also do a += a -= a /= or a %= like this: @var + 5 --> @var = @var + 5
	 * @var - 2 --> @var = @var - 2, 2 - @var --> @var = 2 - @var, etc.
	 * 
	 * This function type checks the matched groups to check that they are ints, or variables that
	 * are of type int.
	 */
	private static boolean arithmetic(Matcher intVal1, Matcher varVal1, Matcher varVal2, Matcher intVal2, String op) throws IOException {
		boolean isOut = stateType.equals("out");
		String stringVal1 = "";String stringVal2 = "";String stringInt1 = "";String stringInt2 = ""; //Intializing strings
		boolean bIntVal1 = intVal1.find();
		boolean bIntVal2 = intVal2.find();
		boolean boolVarVal1 = varVal1.find();
		boolean boolVarVal2 = varVal2.find();
		// Checking for matches, grabbing matched values
		if(boolVarVal1) {
			stringVal1 = varVal1.group(1).replaceAll("@", "");
		}if(boolVarVal2) {
			stringVal2 = varVal2.group(1).replaceAll("@", "");
		}if(bIntVal1) {
			stringInt1 = intVal1.group(1);
		} if (bIntVal2) {
			stringInt2 = intVal2.group(1);
		}
		//Seeing if there is a valid match
		//Depending on what type of state we are in, we may have an invalid line of code.
		//THe if and loops will have a conditional telling them how long to run.
		//They will also have the options of other operations when in their scope.
		boolean match = false;
		if(bIntVal1 && bIntVal2) {
			if (stateType.equals("loopCond") || stateType.equals("ifCond") || stateType.equals("out")) {
				match = true;
			} else {
					error("NOT THE PLACE FOR AN ARITHMETIC OPERATION.");
			}
		} else if (boolVarVal1 && boolVarVal2) {
			match = true;
			match = match && isTypeInt(stringVal1) && isTypeInt(stringVal2);
			if (match) {
				if (isOut == false && match) {
					incrVarWrite(stringVal1, stringVal1, stringVal2, op);
				}
			}
			if (!match) {
				match = stringConcat("@" + stringVal1, "@" + stringVal2);
				return match;
		} else if (bIntVal1 && boolVarVal2) {
			match = true;
			match = match && isTypeInt(stringVal2);
			if (match) {
				if (isOut == false && match) {
					incrVarWrite(stringVal2,stringInt1 , stringVal2, op);
				}
			}
		} else if (boolVarVal1 && bIntVal2) {
			match = true;
			match = match && isTypeInt(stringVal1);
			if (isOut == false && match) {
				incrVarWrite(stringVal1, stringVal1, stringInt2, op);
			}
		}

		if (match && isOut) {
			if (stateType.equals("out")) {
				return match;
			} else if (stateType.equals("loopCond")) {
				return match;
			}
			
		}
		}
		return match;
	}
	
	/*
	 * If both arithmetic values matched are variables, then we do the variable assign.
	 */
	private static void incrVarWrite(String first, String var1, String var2, String op) throws IOException {
		if (op.equals("?")) {
			op = "/";
		} else if (op.equals("$")) {
			op = "%";
		}
		writer.write(tabGet() + first + " = " + var1 + " " + op + " " + var2 + ";\n");
		writer.flush();
	}
	
	/*
	 * This is for our boolean operations, this function is nearly identical in style to
	 * the arithmetic one from above.
	 * 
	 * Checks that each matched component in the bool_ops pattern is of valid type, is a var or a bool,
	 * and differentiates which operation is being used and translates it to java code.
	 */
	private static boolean bools(Matcher bVal1, Matcher bVal2, Matcher varVal1, Matcher varVal2, String op)throws IOException {
		boolean isOut = stateType.equals("out");
		boolean stateCond = (stateType.equals("loopConditional") || stateType.equals("ifConditional"));
		String stringVal1 = "";String stringVal2 = "";String sBoolVal1 = "";String sBoolVal2 = "";
		boolean boolVal1 = bVal1.find();
		boolean boolVal2 = bVal2.find();
		boolean boolVarVal1 = varVal1.find();
		boolean boolVarVal2 = varVal2.find();
		
		if(boolVarVal1) {
			stringVal1 = varVal1.group(1).replaceAll("@", "");
		}if(boolVarVal2) {
			stringVal2 = varVal2.group(1).replaceAll("@", "");
		} if (boolVal1) {
			sBoolVal1 = bVal1.group(1);
		} if (boolVal2) {
			sBoolVal2 = bVal2.group(1);
		}
		
		boolean match = false;
		if(boolVal1 && boolVal2) {
			if (stateCond) {
				match = true;
				condWriter(sBoolVal1, sBoolVal2, op);
			} else if (isOut){
				match = true;
			}
		} else if (boolVarVal1 && boolVarVal2) {
			match = true;
			match = match && isTypeBool(stringVal1) && isTypeBool(stringVal2);
			if (stateCond && match) {
				condWriter(stringVal1, stringVal2, op);
			} else if (isOut == false && match) {
				incrBoolWrite(stringVal1, stringVal2, op);
			}
		} else if (boolVal1 && boolVarVal2) {
			match = true;
			match = match && isTypeBool(stringVal2);
			if (stateCond && match) {
				condWriter(stringVal2, sBoolVal1, op);
			} else if (isOut == false && match) {
				incrBoolWrite(stringVal2, sBoolVal1, op);
			}
		} else if (boolVarVal1 && boolVal2) {
			match = true;
			match = match && isTypeBool(stringVal1);
			if (stateCond && match) {
				condWriter(stringVal1, sBoolVal2, op);
			} else if (isOut == false && match) {
				incrBoolWrite(stringVal1, sBoolVal2, op);
			}
		}

		if(match && isOut && !stateCond) {
			return true;
		}

		return match;
	}
	
	/*
	 * If we have a boolean assignment operation.
	 */
	private static void incrBoolWrite(String first, String second, String op)throws IOException {
		if (op.equals("or")) {
			op = "||";
		} else if (op.equals("and")) {
			op = "&&";
		}
		writer.write(tabGet() + first + " = " + first + " "+ op + " " + second + ";\n");
		writer.flush();
	}
	
	/*
	 * If the boolean operations were used for an if statement or a loop
	 */
	private static void condWriter(String first, String second, String op) throws IOException {
		if (op.equals("or")) {
			op = "||";
		} else if (op.equals("and")) {
			op = "&&";
		}
		writer.write(first + " " + op + " " + second + ") {\n");
		writer.flush();
	}
	
	/*
	 * This comparable function parses and matches any comparable operations that the user may want to perform
	 * 
	 */
	private static boolean compare(Matcher intVal1, Matcher varVal1, Matcher intVal2, Matcher varVal2, Matcher cmd) throws IOException {
		String op = "";String stringVal1 = "";String stringVal2 = "";String sIntVal1 = "";String sIntVal2 = "";
		
		boolean isOut = stateType.equals("out");
		boolean stateCond = (stateType.equals("loopConditional") || stateType.equals("ifConditional"));
		
		if (isOut || stateCond) {
			String val = cmd.group(3);
				op = "";
			if (val != null) {
				op = cmd.group(2) + cmd.group(3);
			} else {
				op = cmd.group(2);
			}
		} else {
			error("NOT THE PLACE FOR A COMPARABLE OPERATION.");
		}

		boolean boolVal1 = intVal1.find();
		boolean boolVal2 = intVal2.find();
		boolean boolVarVal1 = varVal1.find();
		boolean boolVarVal2 = varVal2.find();
		
		if(boolVarVal1) {
			stringVal1 = varVal1.group(1).replaceAll("@", ""); //Formatting variables from our grammar to java
		}if(boolVarVal2) {
			stringVal2 = varVal2.group(1).replaceAll("@", "");
		} if (boolVal1) {
			sIntVal1 = intVal1.group(1);
		} if (boolVal2) {
			sIntVal2 = intVal2.group(1);
		}
		
		boolean match = false;
		if(boolVal1 && boolVal2) {
			match = true;
			if (stateCond && match) {
				incrCompWriter(sIntVal1, sIntVal2, op);
			}
		} else if (boolVarVal1 && boolVarVal2) {
			match = true;
			match = match && isTypeInt(stringVal1) && isTypeInt(stringVal2);
			if (isOut && match || stateCond && match) {
				incrCompWriter(stringVal1, stringVal2, op);
			}
		} else if (boolVal1 && boolVarVal2) {
			match = true;
			match = match && isTypeInt(stringVal2);
			if (isOut && match || stateCond && match) {
				incrCompWriter(sIntVal1, stringVal2, op);
			}
		} else if (boolVarVal1 && boolVal2) {
			match = true;
			match = match && isTypeInt(stringVal1);
		  if (isOut && match || stateCond && match){
				incrCompWriter(stringVal1, sIntVal2, op);
		}
		}
		/*
		if (match) {
			if (stateType.equals("out")) {
				return match;
			} else if (stateType.equals("ifCond")) {
				return true;
			}
		}
		*/
		return match;
	}
	
	/*
	 * This writer is used to differentiate between if the the compare operation is being used
	 * as a conditional for a loop or an if statement. Or if the comparable
	 */
	private static void incrCompWriter(String first, String second, String op) throws IOException {
		if (op.equals("=")) {
			op = "==";
		}
		if (stateType.equals("loopConditional") || stateType.equals("ifConditional")) {
			writer.write(first + " " + op + " " + second + ") {\n");
		} else {
			writer.write(tabGet() + first + " " + op + " " + second + "\n");
		}
		writer.flush();
	}
	
	/*
	 * This function is used for outputting. Anything can be outputted outside of statements (if, loop, function)
	 */
	private static boolean out(String cmd) throws IOException {		
		Matcher m = out.matcher(cmd);
		boolean match = true;
		if(m.find()) {
			String toPrint = m.group(2);
			Matcher v = var.matcher(toPrint);
			Matcher boole = bool.matcher(toPrint);
			Matcher dig = intVal.matcher(toPrint);
			Matcher string = str.matcher(toPrint);
			if (v.find() || boole.find() || dig.find() || string.find()) {
				match = true;
				toPrint = toPrint.replaceAll("@", "");
				printWriter(toPrint);
			} else if (toPrint.startsWith("\"") && toPrint.endsWith("\"")) {
				match = true;
				printWriter("\"" + toPrint + "\"");
			} else if (bool_ops.matcher(toPrint).find()){
				toPrint = toPrint.replaceAll(" and ", " && ").replaceAll(" or ", " || ").replaceAll("@", "");
				printWriter(toPrint);
			} else {
				stateType = "out";
				match = match && expressionCheckOut(toPrint);
				if (match) {
					toPrint = toPrint.replaceAll("@", "");
					printWriter(toPrint);
				} else {
					error("YOU CANNOT PRINT THAT");
				}
			}
		}
		return match;
	}
	
	/*
	 * Used to get the right format for a print statement in java
	 */
	private static void printWriter(String toPrint) throws IOException {
		writer.write(tabGet() + "System.out.println(" + toPrint + ");" + "\n");
		writer.flush();
	}
	
	/*
	 * This is for our loop statement.
	 * 
	 * The function first validates the loops conditional, increases the tab, and then loops until we find
	 * endOnlyIf, which then completes the loops contents.
	 */
	private static boolean loop(Matcher b, Matcher c, Matcher cmd, BufferedReader BReader) throws IOException {
		writer.write(tabGet() + "while(");
		writer.flush();
		stateType = "loopConditional";
		String plainBool = cmd.group(2);
		boolean match = false;
		if (b.find()) {
			String op = b.group(2);
			match = true;
			match = match && bools(bool.matcher(b.group(1)), bool.matcher(b.group(3)), var.matcher(b.group(1)), var.matcher(b.group(3)), op);
		} else if (c.find()) {
			match = true;
			match = match && compare(intVal.matcher(c.group(1)), var.matcher(c.group(1)), intVal.matcher(c.group(4)), var.matcher(c.group(4)), c);
		} else if (bool.matcher(plainBool).find()) {
			match = true;
			writer.write(plainBool + ") {\n");
		} else if (var.matcher(plainBool).find()) {
			match = true;
			match = match && isTypeBool(plainBool);
			if (match) {
				writer.write(plainBool + ") {\n");
			}
		}
		if (!match) {
			error("YOUR LOOP IS INCOMPLETE");
		} else {
			tabNumber +=1;
			while(match) {
				stateType = "loopCond";
				String line = BReader.readLine();
				if (line.equals("endOnlyIf")) {
					tabNumber-=1;
					writer.write(tabGet() + "}\n");
					writer.flush();
					return true;
				}
				if (function.matcher(line).find()) {
					error("CANNOT PUT A FUNCTION HERE.");
				}
				match = match && expressionCheckOut(line);
				if (!match) {
					match = true && parseCmd(line, BReader);
				}
			}
		}
		return match;
	}
	/*
	 * This is for our if statements.
	 * 
	 * Following a similar format to the loops, we first 
	 */
	private static boolean ifStatementCond(Matcher b, Matcher c, Matcher cmd, BufferedReader BReader) throws IOException {
		writer.write(tabGet() + "if(");
		writer.flush();
		stateType = "ifConditional";
		String plainBool = cmd.group(2);
		boolean match = false;
		if (b.find()) {
			String op = b.group(2);
			match = true;
			match = match && bools(bool.matcher(b.group(1)), bool.matcher(b.group(3)), var.matcher(b.group(1)), var.matcher(b.group(3)), op);
		} else if (c.find()) {
			match = true;
			match = match && compare(intVal.matcher(c.group(1)), var.matcher(c.group(1)), intVal.matcher(c.group(4)), var.matcher(c.group(4)), c);
		} else if (bool.matcher(plainBool).find()) {
			match = true;
			writer.write(plainBool + ") {\n");
		} else if (var.matcher(plainBool).find()) {
			match = true;
			match = match && isTypeBool(plainBool);
			if (match) {
				writer.write(plainBool + ") {\n");
			}
		} if (!match) {
			error("YOUR IF STATEMENT IS INCOMPLETE");
		}else {
			tabNumber +=1;
			while(match) {
				stateType = "ifCond";
				String line = BReader.readLine();
				if (line.equals("endIf")) {
					tabNumber-=1;
					writer.write(tabGet() + "}\n");
					writer.flush();
					return true;
				}
				if (function.matcher(line).find()) {
					error("CANNOT PUT A FUNCTION HERE.");
				}
				match = match && expressionCheckOut(line);
				if (!match) {
					match = true && parseCmd(line, BReader);
				}
			}
		}
		return match;
	}
	
	/*
	 * There are many instances where an expression or some other
	 * valid line (According to the grammar and patterns) would be
	 * allowed that we don't want. We use this error method to give some idea
	 * as to where and what is causing the errors.
	 */
	private static void error(String message) throws IOException {
		writer.write(message + "\n" + "PARSING PROCESS STOPPED. CHECK YOUR SOURCE CODE.\n");
		writer.flush();
		writer.close();
		System.exit(0);
	}
}
