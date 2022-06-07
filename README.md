# SpeedTester

Run 10 trials of 10,000,000 jobs each counting from 0 to the integer limit, and doing some usless calculations for each step.

Oupts an average at the end with the number of nanoseconds, and a formated time.

Also outputs average data in the file "time.log"

## Arguments

### Options
-t [number of threads] : sets the number of threads to be used for the test. Default 10

-j [number of jobs] : Sets the number of jobs to do. Default 10,000,000

## Outputs
Prints info for each trial about run time.

Prints average time after all trials to console and to "time.log".

## Dependancies
Uses [PeterLibrary](https://github.com/Platratio34/PeterLibrary/tree/dev) Dev branch for comand line argument parsing and job pools
