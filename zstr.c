#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

//Custom Types
typedef char* zstr;
typedef int zstr_code;
//Global Constants
static const int ZSTR_OK = 0;
static const int ZSTR_ERROR = 100;
static const int ZSTR_GREATER = 1;
static const int ZSTR_LESS = -1;
static const int ZSTR_EQUAL = 0;
//Final Global Variable of
static const zstr_code zstr_status;

int getLength(zstr data) {
    int index = 0;
    while (data[index] != '\0') {
        index++;
    }
    return index;
}

int allocatedSpace(int length) {
    int allocated_size = (sizeof(char) * length) + 1 + 4 + 4; int size;
    if (allocated_size < 16) {
        size = 16;
    } else if (allocated_size < 32) {
        size = 32;
    } else if (allocated_size < 256) {
        size = 256;
    } else if (allocated_size < 1024) {
        size = 1024;
    } else if (allocated_size < 2048) {
        size = 2048;
    } //else {
        //error here
    //}
    return size;
}

int distanceToStart(zstr data) {
    int length; int space; int distance = 0;
    length = getLength(data);
    space = allocatedSpace(length);
    if (length < 10) {
        distance++;
    } else if(length < 100) {
        distance+=2;
    } else {
        distance+=3;
    }
    if (space < 256) {
        distance+=2;
    } else if(length < 1100) {
        distance+=3;
    } else {
        distance+=4;
    }
    return distance;
}

void zstr_destroy(zstr to_destroy) {
    int distance;
    distance = distanceToStart(to_destroy);
    char* addr = ((char*)to_destroy);
    zstr start = (zstr)(addr-distance);
    free(start);
}

zstr zstr_create(char* initial_data) {
    int length = getLength(initial_data); int size = 0;
    size = allocatedSpace(length);

    //converting length and allocated size to a char
    char lengthStr[5];
    sprintf(lengthStr, "%d", length);
    char sizeStr[5];
    sprintf(sizeStr, "%d", size);

    zstr toReturn = malloc(size); //Allocating space
    char* t1 = ((char*)toReturn);

    int i; int index  = 0;
    //Adding length
    for (i = 0; i<strlen(lengthStr);i++) {
        t1[i] = lengthStr[i];
    }
    //Adding size
    while (index < strlen(sizeStr)) {
        t1[i] = sizeStr[index];
        i++;
        index++;
    }
    toReturn = ((zstr) t1+i);
    //Adding the initial_data
    index = 0;
    while(index < length) {
        t1[i] = initial_data[index];
        index++;
        i++;
    }
    t1[i]='\0';
    return toReturn;
}

void zstr_append(zstr * base, zstr to_append) {
    zstr currentBase = base[0]; int lengthBase; int sizeBase;
    int lengthAppend; int sizeAppend;

    lengthBase = getLength(currentBase); sizeBase = allocatedSpace(lengthBase);
    lengthAppend = getLength(to_append);

    if(((lengthBase + lengthAppend)+9) >= sizeBase) {
        char newString[lengthBase+lengthAppend+1]; // Might need to change this size
        int index = 0;
        while(currentBase[index] != '\0') {
            newString[index] = currentBase[index];
            index++;
        }
        int index1 = index;
        index = 0;
        while(to_append[index] != '\0') {
            newString[index1] = to_append[index];
            index++; index1++;
        }
        newString[index1] = '\0';
        *base = zstr_create(newString);
    } else {
        bool done = false; int index = 0; int index1 = 0; char newLength[5]; int distance;
        sprintf(newLength, "%d", (lengthBase+lengthAppend));

        while(done == false) {
            if(currentBase[index] == '\0') {
                while(to_append[index1] != '\0') {
                    currentBase[index] = to_append[index1];
                    index++;
                    index1++;
                }
                done = true;
                currentBase[index] = '\0';
            }
            index++;
        }
        index = 0;
        distance = distanceToStart(currentBase);
        char* addr = ((char*)currentBase);
        zstr start = (zstr)(addr-distance);
        while(newLength[index] != '\0') {
            start[index] = newLength[index];
            index++;
        }

    }
}

int zstr_index(zstr base, zstr to_search) {
    int index = 0; bool finding = false; int index1 = 0; int start = -1;
    while(base[index] != '\0') {
        if ((base[index] == to_search[index1]) && finding == false) {
            finding = true;
            start = index;
            index1++;
        } else if ((base[index] == to_search[index1]) && finding == true) {
            index1++;
        } else if (finding == true) {
            finding= false;
            index1 = 0; 
            index = start+1;
            start = -1;
        } if ((to_search[index1] == '\0')) {
            int distance;
            distance = distanceToStart(base);
            return (start+distance);
        }
        index++;
    }
    return start;
}

void zstr_print_detailed(zstr data) {
    int length; int space; char lengthStr[5]; char spaceStr[5];
    length = getLength(data);
    space = allocatedSpace(length);
    sprintf(lengthStr, "%d", length);
    sprintf(spaceStr, "%d", space);
    printf("STRLENGTH: %s\n", lengthStr);
    printf(" DATASIZE: %s\n", spaceStr);
    int i = 0;
    printf("   STRING: ");
    while(data[i] != '\0') {
        printf("%c", data[i]);
        i++;
    }
    printf("\n");
}

int zstr_count(zstr base, zstr to_search) {
    int count = 0; int index = 0; int index1 = 0; int start = 0;
    bool finding = false;
    while (base[index] != '\0') {
        if (to_search[index1] == '\0') {
            count++; 
            index1=0;
            finding = false;
        } if (base[index] == to_search[index1] && finding == false) {
            finding = true;
            start = index;
            index1++;
        } else if (base[index] == to_search[index1] && finding == true) {
            index1++; 
        } else if (base[index] != to_search[index1] && finding == true){
            index1=0;
            index = start+1;
            finding = false;
        }
        index++;
    }
    if (count == 0) {
        return -1;
    } else {
        return count;
    }
}

int zstr_compare(zstr x, zstr y) {
    int index = 0;
    while(true) {
        if (x[index] > y[index]) {
            return ZSTR_GREATER;
        } else if (x[index] == y[index]) {
            index++;
        } else if (x[index] < y[index]) {
            return ZSTR_LESS;
        } if (x[index] == '\0' && y[index] == '\0') {
            return ZSTR_EQUAL;
        }
    }
}

int main() {
    char test1[] = "abcdefghi";
    zstr sentence;
    zstr sentence2;
    zstr sentence3;
    char test2[] = "abcdefghij";
    char test3[] = "drg";
    sentence = zstr_create(test1);
    sentence2 = zstr_create(test2);
    sentence3 = zstr_create(test3);
    printf("%d\n", zstr_index(sentence, sentence3));
}
