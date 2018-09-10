# Besser
Besser is a general-purpose programming language developed on kotlin

## A fast tutorial:


#### Types:
```
# this is a comment
'this is a string'
24 # this is a number
24.4 # this is another number
true # this is a bool
false # this is another bool
:'STR' 2 false 4 true # this is an array
# these are channels
chan 'str'
chan 3
chan false
chan (:1 3)
chan (chan 'smt')
```

#### Functions:
```
fun my_fun then
  set args = (get as ARRAY)
  for i of @args then
    print (@i as STR)
  end
end

@my_fun: 'One' 'Two' 'Three'
```

#### Elements:
```
elem person then
  set name as STR
  set age as NUM
end

fun can_drive of @person then
  book can)
  if ((self age) > 17) then
    set can = true
  else
    set can = false
  end
end @can

# also
fun print_name_and_age then
  print (self name)
  print (self age)
end
@print_name_and_age of @person

set mine = (@person: (read 'Type name: ') ((read 'Type age: ') as NUM))
print ((@mine.can_drive:) as STR)
@mine.print_name_and_age:
```

#### BLOCKS:
```
set a = 'hello'
then
  set a = 'bye'
  set b = 'bye'
end
print @a #bye
print @b #Error!

try
  print @no_var #Error outside try block
except
  print (get 0) #Show error message
end

isolated (:@a) then
  print (get 0) //bye
  print @a //Error!
end

isolated then
  print 'Here you have a new clear and isolated scope'
end

if true then
  print 'true'
else
  print 'false'
end

set x = (read 'Type: ')
when then
  | x == 'hello'
  print 'Hi!'
  | x == 'bye'
  print 'good bye!'
  | true # this is like a default case
  print 'ok!'
end

for i of (0 until 100) then
  print @i
end

repeat 30 then
  print 'Inside'
end

repeat 30 as count then
  print @count
end

while true then
  print 'This loop will never end'
end
```
