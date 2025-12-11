# read -p "Enter Number : " num
# echo "The Number is $num"

# num="helloo"

# echo "$num"
# echo "$num"
# printf "$num"
# printf "$num"


read -p "Enter name : " name
read -p "Enter Roll No : " roll
read -p "Enter Class : " class

echo "$name"
echo "$roll"
echo "$class"

# Array
names=("Ali" "Sara" "Umar")

echo "${names[1]}"

for(( i=0; i<3; i++ ))
do  
    echo "${names[i]}"
done

#!/bin/bash

# empty array
fruits=()

echo "Enter fruit names. Type 'q' to stop."

while true
do
    read -p "Enter fruit name: " fruit

    # if user types q or Q → stop
    if [ "$fruit" == "q" ] || [ "$fruit" == "Q" ]; then
        break
    fi

    # if user just presses Enter → skip
    if [ -z "$fruit" ]; then
        echo "You didn't type anything. Try again."
        continue
    fi

    # add to array
    fruits+=("$fruit")
done

echo
echo "You entered ${#fruits[@]} fruits:"
for (( i=0; i<${#fruits[@]}; i++ ))
do
    echo "$((i+1)). ${fruits[i]}"
done


# Loops

# for (( i=1; i<=10; i++ ))
# do
#     echo $i
# done


i=1
while [ $i -le 10 ]
do
    echo $i
    ((i++))
done

| Operator | Meaning            | Example              |
| -------- | ------------------ | -------------------- |
| `-lt`    | less than          | `[ 3 -lt 5 ]` → true |
| `-le`    | less than or equal | `[ 3 -le 3 ]` → true |
| `-gt`    | greater than       | `[ 7 -gt 2 ]` → true |
| `-ge`    | greater or equal   | `[ 7 -ge 7 ]` → true |
| `-eq`    | equal              | `[ 5 -eq 5 ]` → true |
| `-ne`    | not equal          | `[ 5 -ne 3 ]` → true |

| Operator    | Meaning               |
| ----------- | --------------------- |
| `=` or `==` | strings are equal     |
| `!=`        | strings are not equal |
| `-z`        | string is empty       |
| `-n`        | string is not empty   |




# if esle


num=10

if [ $num -gt 5 ]
then
    echo "true"
fi

---------------------
num=3

if [ $num -ge 5 ]
then
    echo "Number is >= 5"
else
    echo "Number is less than 5"
fi

---------------------

num=10

if [ $num -eq 10 ]
then
    echo "Number is 10"
elif [ $num -gt 10 ]
then
    echo "Number is greater than 10"
else
    echo "Number is less than 10"
fi


# function

greet() {
    echo "Hello $1"
}

greet "bro"


# case


read -p "Enter choice: " num

case $num in
    1) echo "Option 1";;
    2) echo "Option 2";;
    *) echo "Invalid";;
esac
