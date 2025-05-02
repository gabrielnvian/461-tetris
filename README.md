### From the root of the project:

to compile tests:
`javac -cp "lib/*" -d bin @sources.txt`

to run tests:
`java -cp "bin;lib/*" org.junit.runner.JUnitCore test.Test`