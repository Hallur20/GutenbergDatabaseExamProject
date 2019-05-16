LOAD DATA LOCAL INFILE 'home/hallur/NetBeansProjects/Gutenberg/src/main/java/Files/yourfile.csv' 
INTO TABLE Cities 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;