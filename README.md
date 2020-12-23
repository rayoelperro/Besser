# Besser
Besser is a general-purpose programming language developed on kotlin

## A fast tutorial:


#### Types:
```
# this is a comment
'this is a string'
24 # this is a number
24,4 # this is another number. Decimals are separated with a comma instead of a dock
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

### Variables:
```
set x = 'hello'
print @x # hello
set y = (@x + ' world')
print @y # hello world
set y = 10
print @y # Error!
print (@y as STR) # 10
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

### Channels:
```
# Channels are used to get access to a reserved place in the memory
# Functions and closures can change global scope, however they can change channels scope
# Also you can send a channel through functions and elems in different libraries

set x = (chan 'hello') # Channel of strings, Channels can change their type with the assignment of a new value just like normal variables
set y = 'hello' # normal variable
fun change_xy then
  @x.set = 'bye'
  set y = 'bye'
end
print (@x.get) #bye
print @y #hello
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

#### Blocks:
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

#### Events:
```
elem info then
    set name as STR
end

fun rep of @info then
    self name = (read ': ')
end

fun unt of @info then
    set res = ((size (self name)) > 5)
end @res

set ins = (@info:'')

event longname of @ins repeat rep until unt

on @longname then
    print (self name)
end
```

#### Closures:
```
fun some_fun then
  yield: 'hello'
end

@some_fun then
  print (get 0) //hello
end

fun plus then
  yield: ((get 0) + (get 1))
end

@plus (:2 3) then
  print ((get 0) as STR)
end
```

#### Threads:
```
fun my_spawned_fun then
  set k = (get 0)
  while true then
    print @k
  end
end

spawn @my_spawned_fun: 'Hello'

# Also you can
fun my_spawned_fun then
  set k = (get 0)
  yield: @k
end

spawn @my_spawned_fun (:'Hi!') then
  set e = (get 0)
  while true then
    print @e
  end
end
```
