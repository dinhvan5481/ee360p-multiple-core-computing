
## Mac Setup

Also if you use a compiler that supports OpenMP on Mac, to make sure `cmake` uses that compiler, you can use the following command:

`cmake <SOURCE DIRECTORY> -DCMAKE_C_COMPILER=/usr/bin/gcc -DCMAKE_CXX_COMPILER=/usr/bin/g++`

in my case I used `/usr/local/bin/gcc-6` and `/usr/local/bin/g++-6`
