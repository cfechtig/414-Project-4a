# compile
javac -cp ./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar setup.java Gradebook.java gradebookadd.java gradebookdisplay.java

# remove mygradebook if it exist
rm mygradebook

# setup
java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar setup -N mygradebook

# add student 'John Smith'
java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar gradebookadd -N mygradebook -K 45ea448f -AS -FN John -LN Smith

# add student 'Russell Tyler'
java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar gradebookadd -N mygradebook -K 45ea448f -AS -FN Russell -LN Tyler

# add student 'Ted Mason'
java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar gradebookadd -N mygradebook -K 45ea448f -AS -FN Ted -LN Mason

# add assignment 'Midterm'
java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar gradebookadd -N mygradebook -K 45ea448f -AA -AN Midterm -P 100 -W 0.25

# add assignment 'Final'
java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar gradebookadd -N mygradebook -K 45ea448f -AA -AN Final -P 200 -W 0.5

# add assignment 'Project'
java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar gradebookadd -N mygradebook -K 45ea448f -AA -AN Project -P 50 -W 0.25

# add grade 'Midterm'-'John Smith'
java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar gradebookadd -N mygradebook -K 45ea448f -AG -AN Midterm -FN John -LN Smith -G 95

# add grade 'Midterm'-'Russell Tyler'
java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar gradebookadd -N mygradebook -K 45ea448f -AG -AN Midterm -FN Russell -LN Tyler -G 80

# add grade 'Midterm'-'Ted Mason'
java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar gradebookadd -N mygradebook -K 45ea448f -AG -AN Midterm -FN Ted -LN Mason -G 90

# add grade 'Final'-'John Smith'
java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar gradebookadd -N mygradebook -K 45ea448f -AG -AN Final -FN John -LN Smith -G 180

# add grade 'Final'-'Russell Tyler'
java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar gradebookadd -N mygradebook -K 45ea448f -AG -AN Final -FN Russell -LN Tyler -G 190

# add grade 'Final'-'Ted Mason'
java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar gradebookadd -N mygradebook -K 45ea448f -AG -AN Final -FN Ted -LN Mason -G 150

# add grade 'Project'-'John Smith'
java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar gradebookadd -N mygradebook -K 45ea448f -AG -AN Project -FN John -LN Smith -G 48

# add grade 'Project'-'Russell Tyler'
java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar gradebookadd -N mygradebook -K 45ea448f -AG -AN Project -FN Russell -LN Tyler -G 49

# add grade 'Project'-'Ted Mason'
java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar gradebookadd -N mygradebook -K 45ea448f -AG -AN Project -FN Ted -LN Mason -G 50

# # delete assignmeent 'Final'
# java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar gradebookadd -N mygradebook -K 45ea448f -DA -AN Final

# # delete student 'John Smith'
# java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar gradebookadd -N mygradebook -K 45ea448f -DS -FN John -LN Smith

# print assignment 'Midterm'
java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar gradebookdisplay -N mygradebook -K 45ea448f -PA -G -AN Midterm

# print assignment 'Midterm'
java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar gradebookdisplay -N mygradebook -K 45ea448f -PA -A -AN Midterm

# print student 'John Smith'
java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar gradebookdisplay -N mygradebook -K 45ea448f -PS -FN John -LN Smith

# print final
java -ea -cp .:./jackson-annotations-2.13.2.jar:jackson-core-2.13.2.jar:jackson-databind-2.13.2.2.jar:commons-codec-1.15.jar gradebookdisplay -N mygradebook -K 45ea448f -PF -G
