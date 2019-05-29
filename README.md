# GutenbergProject

> _By cool guys - Murched Kayed, Hallur vid Neyst & Zaeem Shafiq_

<h1>Introduction</h1>

<p>This is a solution for the <a href="https://bit.ly/2EyCDsk" rel="https://github.com/datsoftlyngby/soft2019spring-databases/tree/master/Exam"> Gutenber project </a>, using Java to generate the needed csv & json files to import into the database, there ben used two diffrent databases SQL and MongodDB</p>

<h1>SQL</h1>
<h4>To run the quries you need to run this <a href="https://bit.ly/2EyCDsk" rel="https://github.com/Hallur20/GutenbergDatabaseExamProject/blob/master/dump.sql">dump file</a> into you SQL-Database.</h4>

<h2>index's</h2>

```sql
create index authors_book_id_index on authors(book_id);
```
<h2>SQL Queries</h2>

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

<h1>MongoDB</h1>

<h4>To run the quries you need to import this <a href="https://github.com/Hallur20/GutenbergDatabaseExamProject/blob/master/authorsJson.json">json file</a> into you Mongo-Database using this command:</h4>

<code>
 mongoimport --db <strong>your db Name</strong> --collection authors --file authorsJson.json --jsonArray
</code>

<h2>Mongo Queries</h2>

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

<h3>4-Given a geolocation, your application lists all books mentioning a city in vicinity of the given geolocation.</h3>

```mongo
db.authors.aggregate([
{ "$unwind": "$books" },
{"$match" : {"authorName" : "Various"}},
{"$group" : {"_id" : {"_id" : "$_id", "book" : "$books.title", "cities" : "$books.cities"}}}
])
```
