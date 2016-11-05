from random import randint
from sys import stdout

n = 100;
m = 100;

stdout.write("{0} {1}\n".format(n, m))
for i in range(n):
    for j in range(m):
        stdout.write(("{0}\n" if j == m - 1 else "{0} ").format(randint(0, 100)))
