#!/usr/bin/python3
'''
Author: Bryson Mineart
Project: school_SQL.py
Desc:
This project fucntions to maintain and edit a database
that contains three tables. The database itself functions
as record keeping for a school of students and courses. Within
this program, a user has the ability to add, delete, and change
student records. The user can also add and delete courses. This
program utilizes MySQLdb to connect with the SQL database as well
as perform querys into the database for information.
'''
import json
import sys
import os
import MySQLdb
import passwords

conn = MySQLdb.connect(host=passwords.SQL_host, user=passwords.SQL_user, passwd=passwords.SQL_password, db="school")

def status_303(extraLink):
    '''
    This function is used in post, put, and delete
    methods. The function receives any extra link
    information that needs to be supplied for the
    redirect.
    '''
    location = "/cgi-bin/school.py/"
    location+=extraLink
    print("Status: 303 Redirect")
    print("Location: " + location)
    print()

def status(code):
    '''
    This function is used to print out any and
    all necessary status codes that may occur with
    a users get, post, put, or delete request
    '''
    if code == 200:
        print("Status: 200 OK")
        print("Content-Type: text/html")
        print()
        print("<html><body>")
        print()
    elif code == 404:
        print("Status: 404 Not Found")
        print()
        sys.exit(0)
    elif code == 400:
        print("Status: 400 Bad Request")
        print()
        sys.exit(0)

def post_student_newCourse(courseID, studentID):
    '''
    This function receives info as a course to be added to
    a student's courses. It also receives a student id for
    the course to be added to.

    This function first checks that the course exists. We
    then check that the student exists. If both the course
    and the student exist. We then check if the student is
    already enrolled in the course. If all is good, we write
    a new record into the registrations table.
    '''
    try:
        studentID = int(studentID)
    except:
        status(400)
    if type(studentID) != int or type(courseID) != str:
        status(400)

    cursor = conn.cursor()
    cursor.execute("SELECT * FROM courses WHERE id=%s", (courseID,))
    if cursor.rowcount == 0:
        cursor.close()
        status(404)
    cursor.execute("SELECT * FROM students WHERE id=%s", (studentID,))
    if cursor.rowcount == 0:
        cursor.close()
        status(404)
    cursor.execute("SELECT * FROM registrations WHERE studentID=%s AND courseID=%s", (studentID,courseID))
    if cursor.rowcount != 0:
        cursor.close()
        status(400)
    else:
        cursor.execute("INSERT INTO registrations(studentID, courseID) VALUES(%s,%s)", (studentID, courseID))
        cursor.close()
        conn.commit()
        location = "students/" + str(studentID) + "/courses"
        status_303(location)

def post_course(info):
    '''
    This function receives info as a course to be added to
    a list of available courses for students to enroll in.

    The function begins by error checking the input, if this
    is all good, the function then creates a link and checks
    that the course does not already exist. If the course does
    not exist we then add a new record to the courses table.
    '''
    try:
        courseID = info["id"] #error checking
    except:
        status(400)
    if type(courseID) != str:
        status(400)

    link = "http://35.174.139.107/cgi-bin/school_SQL.py/courses/" + courseID
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM courses WHERE id=%s", (courseID,))
    if cursor.rowcount != 0: #Checking for course
        cursor.close()
        status(400)
    else:
        cursor.execute("INSERT INTO courses(id, link) VALUES(%s,%s)", (courseID, link))
        cursor.close()
        conn.commit()
        status_303("courses")

def post_newStudent(info):
    '''
    This function receives a dictionary that is read from the command line
    input. This information is given to us by the user and is a new student
    to add to our database.

    This function first does error checking: checks that the right
    parameters were received, and checks that they are the right type.

    From here, the function builds a link that represents said student.
    Following this, the function creates a connection to the database and
    the right table. Then the function checks if the student already exists.
    If not, the function then utilizes an execute statement to add that
    student to the database.
    '''
    try:
        student_id = info["id"] #Error checking
        name = info["name"]
    except:
        status(400)
    if type(student_id) != int and type(name) != str: #Error Checking
        status(400)
    link = "http://35.174.139.107/cgi-bin/school_SQL.py/students/" #Link
    link += str(student_id)

    cursor=conn.cursor() #Checking for student
    cursor.execute("SELECT * FROM students WHERE id=%s", (student_id,))
    if cursor.rowcount != 0:
        cursor.close()
        status(400)
    else:
        cursor.execute("INSERT INTO students(id, name, link) VALUES(%s,%s,%s)", (student_id, name, link))
        cursor.close() #Inserting
        conn.commit()
        status_303("students")

def post_helper():
    '''
    This function is used to handle any post request.
    The function breaks down any information received from
    the command line and pin points exactly what type
    of post operation the user is trying to conduct.
    From there it sends the appropriate information
    to the appropriate functions.
    '''
    info = json.loads(sys.stdin.read())
    if "PATH_INFO" in os.environ:
        extra_path = os.environ["PATH_INFO"].split("/")
        if len(extra_path) == 2 and "students" in extra_path:
            post_newStudent(info)
        elif len(extra_path) == 4 and "courses" in extra_path:
            post_student_newCourse(info, extra_path[2])
        elif len(extra_path) == 2 and "courses" in extra_path:
            post_course(info)

def delete_student(studentID):
    '''
    This function receives a student id number from the
    user.

    The function will first error check the input. If the
    input is good, the function then checks that the student
    exists. If the student does exist we then check that the
    student is not present in our registration table. If the student
    is, we first have to delete any classes the student is in. From
    there we can remove the student altogether.
    '''
    try:
        studentID = int(studentID) # Error checking
    except:
        status(400)

    cursor = conn.cursor()
    cursor.execute("SELECT * FROM students WHERE id=%s", (studentID,))
    if cursor.rowcount == 0: #Looking for student
        cursor.close()
        status(404)
    else:
        cursor.execute("SELECT * FROM registrations WHERE studentID=%s", (studentID,))
        if cursor.rowcount != 0:
            cursor.execute("DELETE FROM registrations WHERE studentID=%s", (studentID,))
        cursor.execute("DELETE FROM students WHERE id=%s", (studentID,))
        cursor.close() #Deleting
        conn.commit()
        location = "students"
        status_303(location)

def delete_course(courseID):
    '''
    This function receives a course id to be deleted.

    This function begins by error checking the input from the user.
    If the input is good, we then check if the course exists. If the course
    exists we then check to see if any students are enrolled in it. If the
    class is empty, we can delete it from the courses table.
    '''
    if type(courseID) != str: #Error checking
        status(400)

    cursor = conn.cursor() #Checking for class
    cursor.execute("SELECT * FROM courses WHERE id=%s", (courseID,))
    if cursor.rowcount == 0:
        cursor.close()
        status(404)
    cursor.execute("SELECT * FROM registrations WHERE courseID=%s", (courseID,))
    if cursor.rowcount != 0: #Checking for enrollment
        cursor.close()
        status(400)

    cursor.execute("DELETE FROM courses WHERE id=%s", (courseID,))
    cursor.close() #Deleting
    conn.commit()
    location = "courses"
    status_303(location)

def delete_helper():
    '''
    This function is called whenver a delete request has been made.
    The function takes in the path info and decides what actions the
    user is trying to perform. From there, the function will call
    the correct function and send the needed parameters.
    '''
    if "PATH_INFO" in os.environ:
        extra_path = os.environ["PATH_INFO"]
        extra_path = extra_path.split("/")
        if len(extra_path) == 3 and "students" in extra_path:
            delete_student(extra_path[2])
        elif len(extra_path) == 3 and "courses" in extra_path:
            delete_course(extra_path[2])

def put_newName(info, studentID):
    '''
    This function receives info as a dictionary containing
    the new name to be added to a student. This function also
    receives a student id number to know which student
    is changing their name.

    The function first goes through some standard error checking
    on the input. From here we check that the student actually
    exists. If the student exists, we can change their name.
    '''
    try:
        name = info["name"] #Error Checking
        studentID = int(studentID)
    except:
        status(400)
    if type(name) != str or type(studentID) != int:
        status(400)
    cursor = conn.cursor() #Checking for student
    cursor.execute("SELECT * FROM students WHERE id=%s", (studentID,))
    if cursor.rowcount == 0:
        cursor.close()
        status(404)
    cursor.execute("UPDATE students SET name=%s WHERE id=%s", (name, studentID,))
    cursor.close() #Changing name
    conn.commit()
    location = "students"
    status_303(location)

def put_helper():
    '''
    This function is used whenever the user wants to change
    something in the collections (ONly the name of a student).

    This function gets the information and sends it to the correct function.
    '''
    info = json.loads(sys.stdin.read())
    if "PATH_INFO" in os.environ:
        extra_path = os.environ["PATH_INFO"]
        extra_path = extra_path.split("/")
        put_newName(info, extra_path[2])

def get_courses_all():
    '''
    This function is used if a user wants to view
    all of the classes currently available to begin
    taking. The function will display all of these courses
    to the user.
    '''
    status(200)
    cursor = conn.cursor() #Getting courses
    cursor.execute("SELECT * FROM courses")
    data = cursor.fetchall()
    courseList = []
    for val in data: #Changing to correct format
        tempD = {}
        tempD["id"] = val[0]
        tempD["link"] = val[1]
        courseList.append(tempD)
    courses = json.dumps(courseList, indent = 2)
    print(courses)

def get_students_all():
    '''
    This function will get every student in the system
    and display them for the user.
    '''
    status(200)
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM students") #Getting students
    data = cursor.fetchall()
    studentList = []
    for val in data:
        courses = []
        tempD = {}
        tempD["id"] = val[0]
        tempD["name"] = val[1] #Creating each student in correct format
        cursor.execute("SELECT * FROM registrations where studentID=%s", (val[0],))
        classes = cursor.fetchall()
        for val1 in classes:
            courses.append(val1[1])
        tempD["courses"] = courses
        tempD["link"] = val[2]
        studentList.append(tempD)
    information = json.dumps(studentList, indent = 2)
    cursor.close()
    print(information)

def get_student(studentID):
    '''
    This function receives an integer value that represents
    a specific student in the system (or not).

    The function first checks that this student exists, if the student
    does exist, we go through and format the information for the user
    to view.
    '''
    try:
        studentID = int(studentID)
    except:
        status(400)
    if type(studentID) != int:
        status(400)

    cursor = conn.cursor()
    cursor.execute("SELECT * FROM students WHERE id=%s", (studentID,))
    if cursor.rowcount == 0:
        cursor.close()
        status(404)
    status(200)
    data = cursor.fetchall()
    cursor.execute("SELECT * FROM registrations WHERE studentID=%s", (studentID,))
    courses = cursor.fetchall()
    classes = []
    for val in courses:
        classes.append(val[1])

    studentINFO = {}
    studentINFO["id"] = data[0][0]
    studentINFO["name"] = data[0][1]
    studentINFO["courses"] = classes
    studentINFO["link"] = data[0][2]

    info = json.dumps(studentINFO, indent = 2)
    print(info)

def get_helper():
    '''
    If the user uses a get request, this method is used
    to dissect what exactly they are trying to. The function will
    sift through a get all students, a get all courses, or even getting
    a specific student. From there the function will send the appropriate
    information.
    '''
    if "PATH_INFO" in os.environ:
        extra_path = os.environ["PATH_INFO"]
        extra_path = extra_path.split("/")

        if len(extra_path) == 2 and "students" in extra_path: #All students
            get_students_all()
        elif len(extra_path) == 3 and "students" in extra_path: #Specific student
            get_student(extra_path[2])
        elif len(extra_path) == 2 and "courses" in extra_path: #All courses
            get_courses_all()

def main():
    '''
    My main function is designed to simply find the request
    method and then call the right function to follow through
    with the users command.
    '''
    if os.environ["REQUEST_METHOD"] == "GET":
        get_helper()
    elif os.environ["REQUEST_METHOD"] == "POST":
        post_helper()
    elif os.environ["REQUEST_METHOD"] == "PUT":
        put_helper()
    elif os.environ["REQUEST_METHOD"] == "DELETE":
        delete_helper()

    print("</body></html>")
main()
