# Gutenberg Project <g-emoji class="g-emoji" alias="book" fallback-src="https://github.githubassets.com/images/icons/emoji/unicode/1f4d6.png">📖</g-emoji>

> _By cool guys - Murched Kayed, Hallur vid Neyst & Zaeem Shafiq_

<h1>Introduction <g-emoji class="g-emoji" alias="memo" fallback-src="https://github.githubassets.com/images/icons/emoji/unicode/1f4dd.png">📝</g-emoji></h1>

<p>This is our solution for the <a href="https://bit.ly/2EyCDsk" rel="https://github.com/datsoftlyngby/soft2019spring-databases/tree/master/Exam"> Gutenberg project </a>. We have used Java to generate the needed CSV & JSON files to import data into the databases. The two databases used are MySQL and MongoDB.
</p>
<p>We chose to use a scanner as the user interface of our program, to execute our queries in the application.</p>

<strong><a href="https://github.com/Hallur20/GutenbergDatabaseExamProject/blob/master/Gutenberg%20rapport.pdf">Click here to see the report</a></strong>

<h1>SQL <img src="http://icons.iconarchive.com/icons/papirus-team/papirus-apps/48/mysql-workbench-icon.png" style="margin-top:40px;" title="Mysql-workbench" alt="Mysql-workbench icon" width="48" height="48"></h1>

<h4>To run the quries you need to run this <a href="https://github.com/Hallur20/GutenbergDatabaseExamProject/blob/master/Dump20190530.sql">dump file</a> into you SQL-Database.</h4>

<h2>index's</h2>

<p>The dump file contains forent key's, which will auto ganaret indexs on the forent key's.
In addation we created two extra index's on 'authorName' in authorBooks table and 'cityName' in cities table:
</p>

```sql
create index cityName_index on Cities(cityName);
create index authorBooks_index on authorBooks(authorName);
```

<p>We also tried to create an index to 'title' in book table but since it is a long VARCHAR column, wich will be a bad idea because the index will be very bulky and inefficient.</p>

<h2>SQL Queries <g-emoji class="g-emoji" alias="mag" fallback-src="https://github.githubassets.com/images/icons/emoji/unicode/1f50d.png">🔍</g-emoji></h2>

<h3>1-Given a city name your application returns all book titles with corresponding authors that mention this city.</h3>

```sql
select title, authorName from book
inner join cityMention on cityMention.bookId = book.id
inner join Cities on Cities.id = cityMention.cityId
inner join authorBooks on authorBooks.bookId = book.id
where Cities.cityName = ?;
```
<h3>2-Given a book title, your application plots all cities mentioned in this book.</h3>

```sql
 SELECT book.id, cityName as cityMentioned, latitude, longitude, cityMention.count as cityOccurences, title
 FROM book INNER JOIN cityMention ON book.id = cityMention.bookId INNER JOIN Cities
 ON Cities.id = cityMention.cityId WHERE book.title = ?;
```

<h3>3-Given an author name your application lists all books written by that author and plots all cities mentioned in any of the book</h3>

```sql
 SELECT authorName, cityMention.bookId, cityName as mentionedCity , latitude, longitude, title as bookTitle FROM authorBooks 
INNER JOIN cityMention ON cityMention.bookId = authorBooks.bookId
INNER JOIN book ON authorBooks.bookId = book.id
INNER JOIN Cities ON Cities.id = cityMention.cityId
 WHERE authorBooks.authorName = ?;
```
<h3>4-Given an author name your application lists all books written by that author and plots all cities mentioned in any of the book</h3>

```sql
select ROUND(ST_Distance_Sphere(
            point(longitude, latitude),
            point(12.568337, 55.676098)
        ) / 1000,2) as km_distance, cityName as city_in_area, title as title_of_book_mentioning_city from Cities 
 inner join cityMention as cm on Cities.id = cm.cityId
 inner join book as b on cm.bookId = b.id
 where ST_Distance_Sphere(
            point(longitude, latitude),
            point(12.568337, 55.676098)
        ) / 1000 < 200;

```

<h1>MongoDB <img style="-webkit-user-select: none;" src="https://sitejerk.com/images/mongodb-png-10.png" width="45" height="45"></h1>

<h4>To run the quries you need to import this <a href="https://github.com/Hallur20/GutenbergDatabaseExamProject/blob/master/authorsJson.json">json file</a> into you Mongo-Database using this command:</h4>

<code>
 mongoimport --db <strong>your db Name</strong> --collection authors --file authorsJson.json --jsonArray
</code>

<h2>Mongo Queries <g-emoji class="g-emoji" alias="mag" fallback-src="https://github.githubassets.com/images/icons/emoji/unicode/1f50d.png">🔍</g-emoji></h2>

<h3>1-Given a city name your application returns all book titles with corresponding authors that mention this city 'London'.</h3>

```mongo
db.authors.aggregate([
{ "$unwind": "$books" },
{"$unwind" : "$books.cities"},
{"$match" : {"books.cities.cityName" : "London"}},
{"$group" : {"_id" : {"_id" : "$_id", "book" : "$books.cities.cityName"}}}
])
```

<h3>2-Given a book title, your application plots all cities mentioned in this book "The Life and Most Surprising Adventures of Robinson Crusoe, of York, Mariner (1801)".</h3>

```mongo
db.authors.aggregate([
{ "$unwind": "$books" },
{"$unwind" : "$books.cities"},
{"$match" : {"books.title" : "The Life and Most Surprising Adventures of Robinson Crusoe, of York, Mariner (1801)"}},
{"$group" : {"_id" : {"_id" : "$_id", "book" : "$books.title", "cities" : "$books.cities.cityName"}}}
])
```

<h3>3-Given an author name 'Various' your application lists all books written by that author and plots all cities mentioned in any of the books.</h3>

```mongo
db.authors.aggregate([
{ "$unwind": "$books" },
{"$match" : {"authorName" : "Various"}},
{"$group" : {"_id" : {"_id" : "$_id", "book" : "$books.title", "cities" : "$books.cities"}}}
])
```
<h1>Measure Behavior</h1>
<h4>The applection behavior measurements are obtained, after creating a connection to the database's for each execute query (Time= DbConnectionTime + queryExcutionTime), that's why it took longer time then expected, but if we connect to the database before executing the queries then take time only for the query execution, with that process we will obtain better results  (Time =  queryExcutionTime).</h4>
 
<table>
<thead>
<tr>
<th>Queries</th>
<th>Inputs</th>
<th>Mongo-app</th>
<th>SQL-app</th>
<th>Mongo-shell</th>
<th>MySQL-shell</th>
</tr>
</thead>
<tbody>
<tr>
<td>Query_1</td>
<td>London</td>
<td>3.1 s</td>
<td>3.6 s</td>
<td>0.0s</td>
<td>0.016 s</td>
</tr>
<tr>
<td>Query_1</td>
<td>Copenhagen</td>
<td>3.2 s</td>
<td>3.4 s</td>
<td>0.0 s</td>
<td>0.0 s</td>
</tr>
<tr>
<td>Query_1</td>
<td>Helsingborg</td>
<td>3.1 s</td>
<td>3.2 s</td>
<td>0.0 s</td>
<td>0.0 s</td>
</tr>
<tr>
<td>Query_1</td>
<td>Berlin</td>
<td>3.2 s</td>
<td>3.67 s</td>
<td>0.0 s</td>
<td>0.0 s</td>
</tr>
<tr>
<td>Query_1</td>
<td>Nothing</td>
<td>3.1 s</td>
<td>3.1 s</td>
<td>0.0 s</td>
<td>0.0 s</td>
</tr>
<tr>
<td>Query_3</td>
<td>Schlesinger, Max</td>
<td>3.2 s</td>
<td>3.3 s</td>
<td>0.0 s</td>
<td>0.0 s</td>
</tr>
<tr>
<td>Query_3</td>
<td>Fontenoy, marquise de</td>
<td>2.9 s</td>
<td>3.4s</td>
<td>0.0 s</td>
<td>0.0 s</td>
</tr>

<tr>
<td>Query_2</td>
<td>The Three Musketeers</td>
<td>3 s</td>
<td>3.3 s</td>
<td>0.0 s</td>
<td>0.0 s</td>
</tr>
<tr>
<td>Query_2</td>
<td>History of Modern PhilosophyFrom Nicolas of Cusa to the Present Time</td>
<td>2.7 s</td>
<td>3.2 s</td>
<td>0.0 s</td>
<td>0.0 s</td>
</tr>

<tr>
<td>Query_4</td>
<td>12.568337, 55.676098, 200</td>
<td>did not make query</td>
<td>4.1 s</td>
<td>did not made the query</td>
<td>0.0593 s</td>
</tr>
<tr>
<td>Query_4</td>
<td>-0.1277583, 51.5073509, 200</td>
<td>did not made the query</td>
<td>7.2 s</td>
<td>did not made the query</td>
<td>0.078 s</td>
</tr>
</tbody>
</table>
